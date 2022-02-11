package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductSaveStateUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.ProductSectionKey
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductSetupViewModel @AssistedInject constructor(
    @Assisted productMap: EtalaseProductListMap,
    private val repo: PlayBroadcastRepository,
    private val configStore: HydraConfigStore,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            productMap: EtalaseProductListMap,
        ): PlayBroProductSetupViewModel
    }

//    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
//    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _campaignAndEtalase = MutableStateFlow(CampaignAndEtalaseUiModel.Empty)
    private val _selectedProductMap = MutableStateFlow(productMap)
    private val _focusedProductList = MutableStateFlow(ProductListPaging.Empty)
    private val _saveState = MutableStateFlow(ProductSaveStateUiModel.Empty)

    private val _loadParam = MutableStateFlow(ProductListPaging.Param.Empty)

    private val searchQuery = MutableStateFlow("")

    private var getProductListJob: Job? = null

    private val _uiEvent = MutableSharedFlow<PlayBroProductChooserEvent>(extraBufferCapacity = 5)

    val uiEvent: SharedFlow<PlayBroProductChooserEvent>
        get() = _uiEvent

    val uiState = combine(
        _campaignAndEtalase,
        _focusedProductList,
        _selectedProductMap,
        _loadParam,
        _saveState,
    ) { campaignAndEtalase, focusedProductList, selectedProductList, loadParam, saveState ->
        PlayBroProductChooserUiState(
            campaignAndEtalase = campaignAndEtalase,
            focusedProductList = focusedProductList,
            selectedProductList = selectedProductList,
            loadParam = loadParam,
            shopName = userSession.shopName,
            saveState = saveState,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayBroProductChooserUiState.Empty,
    )

    init {
        getCampaignAndEtalaseList()

        viewModelScope.launch {
            searchQuery.debounce(300)
                .collectLatest { query ->
                    _loadParam.update {
                        it.copy(keyword = query)
                    }
                }
        }

        viewModelScope.launch {
            _loadParam.collectLatest {
                handleLoadProductList(it, true)
            }
        }

        viewModelScope.launch {
            _selectedProductMap.collectLatest { map ->
                _saveState.update {
                    it.copy(
                        canSave = map.values.flatten().isNotEmpty()
                    )
                }
            }
        }
    }

    fun submitAction(action: PlayBroProductChooserAction) {
        when (action) {
            is PlayBroProductChooserAction.SetSort -> handleSetSort(action.sort)
            is PlayBroProductChooserAction.SelectEtalase -> handleSelectEtalase(action.etalase)
            is PlayBroProductChooserAction.SelectCampaign -> handleSelectCampaign(action.campaign)
            is PlayBroProductChooserAction.SelectProduct -> handleSelectProduct(action.product)
            is PlayBroProductChooserAction.LoadProductList -> handleLoadProductList(
                param = _loadParam.value,
                resetList = false,
            )
            is PlayBroProductChooserAction.SearchProduct -> handleSearchProduct(action.keyword)
            PlayBroProductChooserAction.SaveProducts -> handleSaveProducts()
            PlayBroProductChooserAction.AddProduct -> handleAddProduct()
        }
    }

    private fun getCampaignAndEtalaseList() {
        _campaignAndEtalase.update {
            it.copy(state = NetworkState.Loading)
        }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val campaignListDeferred = asyncCatchError(block = {
                repo.getCampaignList()
            }) { emptyList() }
            val etalaseListDeferred = asyncCatchError(block = {
                repo.getEtalaseList()
            }) { emptyList() }

            _campaignAndEtalase.update {
                it.copy(
                    campaignList = campaignListDeferred.await().orEmpty(),
                    etalaseList = etalaseListDeferred.await().orEmpty(),
                    state = NetworkState.Success,
                )
            }
        }) {
            _campaignAndEtalase.update {
                it.copy(state = NetworkState.Failed)
            }
        }
    }


    private fun handleSetSort(sort: SortUiModel) {
        _loadParam.update {
            it.copy(sort = sort)
        }
    }

    private fun handleSelectEtalase(etalase: EtalaseUiModel) {
        _loadParam.update {
            it.copy(
                etalase = SelectedEtalaseModel.Etalase(etalase),
            )
        }
    }

    private fun handleSelectCampaign(campaign: CampaignUiModel) {
        _loadParam.update {
            it.copy(
                etalase = SelectedEtalaseModel.Campaign(campaign),
                sort = null,
                keyword = "",
            )
        }
    }

    private fun handleSelectProduct(product: ProductUiModel) {
        //when select product, we can just use default key
        val etalase = ProductSectionKey("", "")
        _selectedProductMap.update { map ->
            val prevSelectedProducts = map[etalase].orEmpty()
            val newSelectedProducts = if (prevSelectedProducts.any { it.id == product.id }) {
                prevSelectedProducts.filter { it.id != product.id }
            } else prevSelectedProducts + product
            map + mapOf(etalase to newSelectedProducts)
        }
    }

    private fun handleLoadProductList(
        param: ProductListPaging.Param,
        resetList: Boolean,
    ) {
        if (!resetList && when (val result = _focusedProductList.value.resultState) {
            PageResultState.Loading -> true
            is PageResultState.Success -> !result.hasNextPage
            else -> false
        }) return

        _focusedProductList.update {
            it.copy(
                productList = if (resetList) emptyList() else it.productList,
                resultState = PageResultState.Loading,
            )
        }
        getProductListJob = viewModelScope.launchCatchError(dispatchers.io, block = {
            val page = if (resetList) 1 else _focusedProductList.value.page + 1
            when (val selectedEtalase = _loadParam.value.etalase) {
                is SelectedEtalaseModel.Campaign -> {
                    val pagedProductList = repo.getProductsInCampaign(
                        campaignId = selectedEtalase.campaign.id,
                        page = page
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + pagedProductList.dataList,
                            resultState = PageResultState.Success(pagedProductList.hasNextPage),
                            page = page,
                        )
                    }
                }
                is SelectedEtalaseModel.Etalase,
                SelectedEtalaseModel.None -> {
                    val pagedProductList = repo.getProductsInEtalase(
                        etalaseId = if (selectedEtalase is SelectedEtalaseModel.Etalase) {
                            selectedEtalase.etalase.id
                        } else "",
                        page = page,
                        keyword = param.keyword,
                        sort = param.sort?.id ?: SortUiModel.supportedSortList.first().id,
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + pagedProductList.dataList,
                            resultState = PageResultState.Success(pagedProductList.hasNextPage),
                            page = page,
                        )
                    }
                }
            }
        }) { err ->
            _focusedProductList.update {
                it.copy(
                    resultState = PageResultState.Fail(err),
                )
            }
        }

        getProductListJob?.invokeOnCompletion { cause ->
            if (cause is CancellationException) {
                _focusedProductList.update {
                    it.copy(resultState = PageResultState.Fail(cause))
                }
            }
        }
    }

    private fun handleSearchProduct(keyword: String) {
        searchQuery.value = keyword
    }

    private fun handleSaveProducts() {
        _saveState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            repo.saveProducts(
                channelId = configStore.getChannelId(),
                products = _selectedProductMap.value.values.flatten(),
            )
            _uiEvent.emit(PlayBroProductChooserEvent.SaveProductSuccess)
        }) {
            _uiEvent.emit(PlayBroProductChooserEvent.ShowError(it))
        }.apply {
            invokeOnCompletion {
                _saveState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun handleAddProduct() {
        viewModelScope.launch {
            _uiEvent.emit(PlayBroProductChooserEvent.OpenShopPage(userSession.shopId))
        }
    }
}