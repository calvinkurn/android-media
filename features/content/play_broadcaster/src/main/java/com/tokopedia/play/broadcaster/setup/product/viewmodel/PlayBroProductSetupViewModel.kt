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
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductSetupViewModel @Inject constructor(
    private val repo: PlayBroadcastRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val _selectedEtalase = MutableStateFlow<SelectedEtalaseModel>(SelectedEtalaseModel.None)
    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _selectedProductMap = MutableStateFlow<EtalaseProductListMap>(emptyMap())
    private val _focusedProductList = MutableStateFlow(ProductListPaging.Empty)

    private val _sort = MutableStateFlow<SortUiModel?>(null)

    private var getProductListJob: Job? = null

    private val _campaignAndEtalase = combine(
        _selectedEtalase,
        _campaignList,
        _etalaseList
    ) { selectedEtalase, campaignList, etalaseList ->
        CampaignAndEtalaseUiModel(
            selected = selectedEtalase,
            campaignList = campaignList,
            etalaseList = etalaseList,
        )
    }

    val uiState = combine(
        _campaignAndEtalase,
        _focusedProductList,
        _selectedProductMap,
        _sort,
    ) { campaignAndEtalase, focusedProductList, selectedProductList, sort ->
        PlayBroProductChooserUiState(
            campaignAndEtalase = campaignAndEtalase,
            focusedProductList = focusedProductList,
            selectedProductList = selectedProductList,
            sort = sort,
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
            _selectedEtalase.collectLatest {
                getProductListJob?.cancel()
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
                keyword = action.keyword,
                sort = action.sort,
                page = action.page,
                resetList = action.resetList,
            )
        }
    }

    private suspend fun getProductsInEtalase(
        etalaseId: String,
        page: Int,
        keyword: String,
    ) = withContext(dispatchers.io) {
        return@withContext repo.getProductsInEtalase(
            etalaseId = etalaseId,
            page = page,
            keyword = keyword,
        )
        /*viewModelScope.launchCatchError(dispatchers.io, block = {
            val map = _productInEtalaseMap.value
            val page = map[model]?.productMap?.keys?.maxOrNull() ?: 0

            if (model is SelectedEtalaseModel.Campaign) {
                //TODO("Campaign")
            } else {
                val etalaseId = when (model) {
                    is SelectedEtalaseModel.Etalase -> model.etalase.id
                    else -> ""
                }
                val productList = repo.getProductsInEtalase(etalaseId, page, keyword)
                _productInEtalaseMap.update {
                    val prevProductPagingMap = it[model] ?: ProductPagingMap(
                        productMap = emptyMap(),
                        hasMore = false,
                    )
                    val newProductPagingMap = ProductPagingMap(
                        productMap = prevProductPagingMap.productMap + mapOf(page to productList),
                        hasMore = false,
                    )

                    it + mapOf(model to newProductPagingMap)
                }
            }

        }) {
            println(it)
        }*/

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
        _sort.value = sort
    }

    private fun handleSelectEtalase(etalase: EtalaseUiModel) {
        _selectedEtalase.value = SelectedEtalaseModel.Etalase(etalase)
    }

    private fun handleSelectCampaign(campaign: CampaignUiModel) {
        _selectedEtalase.value = SelectedEtalaseModel.Campaign(campaign)
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
        keyword: String,
        sort: SortUiModel,
        page: Int,
        resetList: Boolean,
    ) {
        _focusedProductList.update {
            it.copy(
                productList = if (resetList) emptyList() else it.productList,
                resultState = PageResultState.Loading,
            )
        }
        getProductListJob = viewModelScope.launchCatchError(dispatchers.io, block = {
            when (val selectedEtalase = _selectedEtalase.value) {
                is SelectedEtalaseModel.Campaign -> return@launchCatchError
                is SelectedEtalaseModel.Etalase,
                SelectedEtalaseModel.None -> {
                    val productList = repo.getProductsInEtalase(
                        etalaseId = if (selectedEtalase is SelectedEtalaseModel.Etalase) {
                            selectedEtalase.etalase.id
                        } else "",
                        page = page,
                        keyword = keyword,
                    )

                    _focusedProductList.update {
                        it.copy(
                            productList = it.productList + productList,
                            resultState = PageResultState.Success(productList.isNotEmpty()),
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

    data class Param(
        val etalase: SelectedEtalaseModel,
        val page: Int,
        val keyword: String,
    ) {
        companion object {
            val Empty: Param
                get() = Param(
                    etalase = SelectedEtalaseModel.None,
                    page = 0,
                    keyword = "",
                )
        }
    }
}