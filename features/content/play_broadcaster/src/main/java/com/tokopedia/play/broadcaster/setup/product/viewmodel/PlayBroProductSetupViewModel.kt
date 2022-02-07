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
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

    private val _selectedEtalase = MutableStateFlow<SelectedEtalaseModel>(SelectedEtalaseModel.None)
    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _selectedProductMap = MutableStateFlow<EtalaseProductListMap>(emptyMap())

    private val _productInEtalaseMap = MutableStateFlow<Map<SelectedEtalaseModel, ProductPagingMap>>(emptyMap())
    private val _focusedEtalase = MutableStateFlow<SelectedEtalaseModel>(SelectedEtalaseModel.None)

    private val _sort = MutableStateFlow<SortUiModel?>(null)

    private val _campaignAndEtalase = combine(
        _selectedEtalase,
        _campaignList,
        _etalaseList
    ) { selectedEtalase, campaignList, etalaseList ->
        if(selectedEtalase !is SelectedEtalaseModel.None) {
            campaignList.forEach {
                it.isChecked = selectedEtalase is SelectedEtalaseModel.Campaign && selectedEtalase.campaign == it
            }

            etalaseList.forEach {
                it.isChecked = selectedEtalase is SelectedEtalaseModel.Etalase && selectedEtalase.etalase == it
            }
        }

        CampaignAndEtalaseUiModel(
            selected = selectedEtalase,
            campaignList = campaignList,
            etalaseList = etalaseList,
        )
    }

    private val _focusedProductList = combine(
        _productInEtalaseMap,
        _focusedEtalase
    ) { productInEtalase, focusedEtalase ->
        productInEtalase[focusedEtalase]?.productMap?.values?.flatten().orEmpty()
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
        getProductsInEtalase(
            SelectedEtalaseModel.None
        )

//        _selectedProductList.value = mapOf(
//            SelectedEtalaseModel.Campaign(
//                CampaignUiModel(
//                    id = "1",
//                    title = "12.12",
//                    imageUrl = "",
//                    startDateFmt = "abc",
//                    endDateFmt = "def",
//                    status = CampaignStatusUiModel(
//                        status = CampaignStatus.Ongoing,
//                        text = "Berlangsung"
//                    ),
//                    totalProduct = 5,
//                )
//            ) to listOf(
//                ProductUiModel(
//                    id = "1",
//                    name = "Product 1",
//                    imageUrl = "",
//                    stock = 5,
//                    price = OriginalPrice("Rp1250", 1250.0)
//                )
//            )
//        )
    }

    fun submitAction(action: PlayBroProductChooserAction) {
        when (action) {
            is PlayBroProductChooserAction.SetSort -> handleSetSort(action.sort)
            is PlayBroProductChooserAction.SelectEtalase -> handleSelectEtalase(action.etalase)
            is PlayBroProductChooserAction.SelectCampaign -> handleSelectCampaign(action.campaign)
            is PlayBroProductChooserAction.SelectProduct -> handleSelectProduct(action.product)
        }
    }

    private fun getProductsInEtalase(model: SelectedEtalaseModel, keyword: String = "") {
        viewModelScope.launchCatchError(dispatchers.io, block = {
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
        _sort.value = sort
    }

    private fun handleSelectEtalase(etalase: EtalaseUiModel) {
        _selectedEtalase.value = SelectedEtalaseModel.Etalase(etalase)
    }

    private fun handleSelectCampaign(campaign: CampaignUiModel) {
        _selectedEtalase.value = SelectedEtalaseModel.Campaign(campaign)
    }

    private fun handleSelectProduct(product: ProductUiModel) {
        viewModelScope.launch(dispatchers.computation) {
            val theEtalase = _productInEtalaseMap.value.entries.firstOrNull { entry ->
                entry.value.productMap.values.flatten().any { it.id == product.id }
            }?.key ?: return@launch

            _selectedProductMap.update {
                val prevSelectedProducts = it[theEtalase].orEmpty()
                val newSelectedProducts = if (prevSelectedProducts.contains(product)) {
                    prevSelectedProducts - product
                } else prevSelectedProducts + product
                it + mapOf(theEtalase to newSelectedProducts)
            }
        }
    }


    private data class ProductPagingMap(
        val productMap: Map<Int, List<ProductUiModel>>,
        val hasMore: Boolean,
    )
}