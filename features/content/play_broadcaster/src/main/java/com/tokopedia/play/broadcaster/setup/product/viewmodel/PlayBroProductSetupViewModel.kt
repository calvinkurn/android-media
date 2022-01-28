package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortFilterUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductSetupViewModel @Inject constructor(
    private val repo: PlayBroadcastRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val _selectedEtalase = MutableStateFlow(SelectedEtalaseModel.None)
    private val _campaignList = MutableStateFlow(emptyList<CampaignUiModel>())
    private val _etalaseList = MutableStateFlow(emptyList<EtalaseUiModel>())
    private val _focusedProductList = MutableStateFlow(emptyList<ProductUiModel>())
    private val _selectedProductList = MutableStateFlow(emptyList<ProductUiModel>())

    private val _sortFilter = MutableStateFlow(SortFilterUiModel.Empty)

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
        _selectedProductList,
        _sortFilter,
    ) { campaignAndEtalase, focusedProductList, selectedProductList, sortFilter ->
        PlayBroProductChooserUiState(
            campaignAndEtalase = campaignAndEtalase,
            focusedProductList = focusedProductList,
            selectedProductList = selectedProductList,
            sortFilter = sortFilter,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayBroProductChooserUiState.Empty,
    )

    init {
//        getCampaignList()
//        getEtalaseList()
        getProductsInEtalase("0")
    }

    fun submitAction(action: PlayBroProductChooserAction) {

    }

    private fun getProductsInEtalase(etalaseId: String) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _focusedProductList.value = repo.getProductsInEtalase(etalaseId, 0, "")
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
}