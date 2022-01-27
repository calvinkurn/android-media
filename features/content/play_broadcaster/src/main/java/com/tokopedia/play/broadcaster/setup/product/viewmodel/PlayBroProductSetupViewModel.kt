package com.tokopedia.play.broadcaster.setup.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserUiState
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val _selectedProductList = MutableStateFlow(emptyList<String>())

    val uiState = combine(
        _campaignList,
        _etalaseList,
        _selectedProductList,
    ) { campaignList, etalaseList, selectedProductList ->
        PlayBroProductChooserUiState(
            campaignList = campaignList,
            etalaseList = etalaseList,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayBroProductChooserUiState.Empty,
    )

    init {
        getCampaignList()
        getEtalaseList()
    }

    fun submitAction(action: PlayBroProductChooserAction) {

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