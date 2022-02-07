package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductSetupViewModel @Inject constructor(
    private val repo: PlayBroadcastRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _selectedProductMap = MutableStateFlow<EtalaseProductListMap>(emptyMap())
    private val _focusedProductList = MutableStateFlow(ProductListPaging.Empty)

    private val _loadParam = MutableStateFlow(ProductListPaging.Param.Empty)

    private var getProductListJob: Job? = null

    private val _campaignAndEtalase = combine(
        _loadParam,
        _campaignList,
        _etalaseList
    ) { loadParam, campaignList, etalaseList ->
        CampaignAndEtalaseUiModel(
            selected = loadParam.etalase,
            campaignList = campaignList,
            etalaseList = etalaseList,
        )
    }

    val uiState = combine(
        _campaignAndEtalase,
        _focusedProductList,
        _selectedProductMap,
        _loadParam,
    ) { campaignAndEtalase, focusedProductList, selectedProductList, loadParam ->
        PlayBroProductChooserUiState(
            campaignAndEtalase = campaignAndEtalase,
            focusedProductList = focusedProductList,
            selectedProductList = selectedProductList,
            sort = loadParam.sort,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayBroProductChooserUiState.Empty,
    )

    init {
        getCampaignList()
        getEtalaseList()

        viewModelScope.launch {
            _loadParam.collectLatest {
                handleLoadProductList(it, true)
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
                sort = SortUiModel.supportedSortList.first(),
            )
        }
    }

    private fun handleSelectProduct(product: ProductUiModel) {
        //when select product, we can just treat it as no etalase/campaign
        val etalase = SelectedEtalaseModel.None
        _selectedProductMap.update {
            val prevSelectedProducts = it[etalase].orEmpty()
            val newSelectedProducts = if (prevSelectedProducts.contains(product)) {
                prevSelectedProducts - product
            } else prevSelectedProducts + product
            it + mapOf(etalase to newSelectedProducts)
        }
    }

    private fun handleLoadProductList(
        param: ProductListPaging.Param,
        resetList: Boolean,
    ) {
        if (!resetList && _focusedProductList.value.resultState == PageResultState.Loading) return

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
                    val productList = repo.getProductsInCampaign(
                        campaignId = selectedEtalase.campaign.id,
                        page = page
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + productList,
                            resultState = PageResultState.Success(productList.isNotEmpty()),
                            page = page,
                        )
                    }
                }
                is SelectedEtalaseModel.Etalase,
                SelectedEtalaseModel.None -> {
                    val productList = repo.getProductsInEtalase(
                        etalaseId = if (selectedEtalase is SelectedEtalaseModel.Etalase) {
                            selectedEtalase.etalase.id
                        } else "",
                        page = page,
                        keyword = param.keyword,
                        sort = param.sort.id,
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + productList,
                            resultState = PageResultState.Success(productList.isNotEmpty()),
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
}