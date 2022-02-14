package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductSummaryUiState
import com.tokopedia.play.broadcaster.setup.product.model.ProductSaveStateUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    @Assisted productList: List<ProductUiModel>,
    private val repo: PlayBroadcastRepository,
    private val configStore: HydraConfigStore,
    private val userSession: UserSessionInterface,
    private val setupDataStore: PlayBroadcastSetupDataStore,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            productList: List<ProductUiModel>
        ): PlayBroProductSetupViewModel
    }

    val channelId: String
        get() = configStore.getChannelId()

    val maxProduct: Int
        get() = configStore.getMaxProduct()

    private val _selectedEtalase = MutableStateFlow<SelectedEtalaseModel>(SelectedEtalaseModel.None)
    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _selectedProductMap = MutableStateFlow<EtalaseProductListMap>(
        mapOf(SelectedEtalaseModel.None to productList)
    )
    private val _focusedProductList = MutableStateFlow(ProductListPaging.Empty)
    private val _saveState = MutableStateFlow(ProductSaveStateUiModel.Empty)

    private val _loadParam = MutableStateFlow(ProductListPaging.Param.Empty)

    private val searchQuery = MutableStateFlow("")

    private var getProductListJob: Job? = null

    private val _uiEvent = MutableSharedFlow<PlayBroProductChooserEvent>(extraBufferCapacity = 5)

    private val _productTagSectionList = MutableStateFlow(emptyList<ProductTagSectionUiModel>())
    private val _productTagSummary = MutableStateFlow<ProductTagSummaryUiModel>(ProductTagSummaryUiModel.Unknown)

    private val _campaignAndEtalase = combine(
        _selectedEtalase,
        _loadParam,
        _campaignList,
        _etalaseList
    ) { selectedEtalase, loadParam, campaignList, etalaseList ->
        if(selectedEtalase !is SelectedEtalaseModel.None) {
            /** TODO: TechDebt - isChecked should be val, then find a good way to update this checked status */
            campaignList.forEach {
                it.isChecked = selectedEtalase is SelectedEtalaseModel.Campaign && selectedEtalase.campaign == it
            }

            etalaseList.forEach {
                it.isChecked = selectedEtalase is SelectedEtalaseModel.Etalase && selectedEtalase.etalase == it
            }
        }

        CampaignAndEtalaseUiModel(
            selected = loadParam.etalase,
            campaignList = campaignList,
            etalaseList = etalaseList,
        )
    }

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
            sort = loadParam.sort,
            shopName = userSession.shopName,
            saveState = saveState,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayBroProductChooserUiState.Empty,
    )

    val summaryUiState = combine(
        _productTagSectionList,
        _productTagSummary,
    ) { productTagSectionList, productTagSummary ->
        PlayBroProductSummaryUiState(
            productTagSectionList = productTagSectionList,
            productTagSummary = productTagSummary
        )
    }

    init {
        getCampaignList()
        getEtalaseList()

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
            is PlayBroProductChooserAction.LoadProductSummary -> handleLoadProductSummary()
            is PlayBroProductChooserAction.DeleteSelectedProduct -> handleDeleteProduct(action.product)
        }
    }

    private fun getEtalaseList() {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _etalaseList.value = repo.getEtalaseList()
        }) {
            print(it)
        }
    }

    private fun getCampaignList() {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _campaignList.value = repo.getCampaignList()
        }) {
            print(it)
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
        //when select product, we can just treat it as no etalase/campaign
        val etalase = SelectedEtalaseModel.None
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

    /** Product Summary */

    /** TODO: gonna delete this later */
    @OptIn(ExperimentalStdlibApi::class)
    private fun handleLoadProductSummary() {
        /** TODO: change dispatchers.io -> dispatchers.main instead later */
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _productTagSummary.value = ProductTagSummaryUiModel.LoadingWithPlaceholder

            getProductTagSummary()
        }) {
            _productTagSummary.value = ProductTagSummaryUiModel.Unknown
            _uiEvent.emit(
                PlayBroProductChooserEvent.GetDataError(it) {
                    submitAction(PlayBroProductChooserAction.LoadProductSummary)
                }
            )
        }
    }

    /** TODO: gonna delete this later */
    @OptIn(ExperimentalStdlibApi::class)
    private fun handleDeleteProduct(product: ProductUiModel) {
        /** TODO: change dispatchers.io -> dispatchers.main instead later */
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _productTagSummary.value = ProductTagSummaryUiModel.Loading

            val productSectionList = _productTagSectionList.value
            /** TODO: gonna delete this later */
            delay(1000)

            throw Exception("Error")
            /** TODO: gonna uncomment this later */
//                val productIds = productSectionList.sections.flatMap { section ->
//                    section.products.filter { it.id != product.id }.map { it.id }
//                }
//                repo.addProductTag(channelId, productIds)

            getProductTagSummary()
        }) {
            _productTagSummary.value = ProductTagSummaryUiModel.Unknown
            _uiEvent.emit(
                PlayBroProductChooserEvent.DeleteProductError(it) {
                    submitAction(PlayBroProductChooserAction.DeleteSelectedProduct(product))
                }
            )
        }
    }

    /** TODO: gonna delete this later */
    @ExperimentalStdlibApi
    private suspend fun getProductTagSummary() {
        /** TODO: gonna remove this delay */
//        delay(1000)
        val (response, productCount) = repo.getProductTagSummarySection(channelId.toLong())

        _productTagSectionList.value = response
        _productTagSummary.value = ProductTagSummaryUiModel.Success(productCount)
    }

    private fun handleSearchProduct(keyword: String) {
        searchQuery.value = keyword
    }

    private fun handleSaveProducts() {
        _saveState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            repo.addProductTag(
                channelId = configStore.getChannelId(),
                productIds = _selectedProductMap.value.values.flatMap { productList ->
                    productList.map { it.id }
                },
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
}