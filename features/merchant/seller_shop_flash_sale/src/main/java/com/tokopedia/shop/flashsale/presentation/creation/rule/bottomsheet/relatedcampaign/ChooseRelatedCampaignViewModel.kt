package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.usecase.GetRelatedCampaignsUseCase
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter.RelatedCampaignItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseRelatedCampaignViewModel @Inject constructor(
    private val getRelatedCampaignsUseCase: GetRelatedCampaignsUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {
    companion object {
        private const val DEFAULT_CAMPAIGN_ID = -1L
        private const val MAX_SELECTED_CAMPAIGN = 10
        private const val KEYWORD_SEARCH_DEBOUNCE_DURATION = 300L
    }

    private var _selectedCampaigns = MutableLiveData<List<RelatedCampaignItem>>()
    private val selectedCampaignCount: Int
        get() = _selectedCampaigns.value?.size ?: 0

    private val _relatedCampaignsResult = MutableLiveData<ChooseRelatedCampaignResult>()
    val relatedCampaignsResult: LiveData<ChooseRelatedCampaignResult>
        get() = _relatedCampaignsResult

    private val isMaxSelectable: Boolean
        get() = selectedCampaignCount >= MAX_SELECTED_CAMPAIGN

    private val keyword = MutableLiveData<String>()
    private var campaignId: Long = DEFAULT_CAMPAIGN_ID

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val searchKeywordFlow = keyword.asFlow()
        .debounce(KEYWORD_SEARCH_DEBOUNCE_DURATION)
        .flowOn(dispatchers.io)
        .onEach {
            _relatedCampaignsResult.postValue(ChooseRelatedCampaignResult.Loading)
        }
        .mapLatest { keyword ->
            try {
                val result = getRelatedCampaignsUseCase.execute(keyword, campaignId)
                if (keyword.isNotEmpty() && result.isEmpty()) {
                    ChooseRelatedCampaignResult.SearchEmptyResult
                } else {
                    val previousCampaigns = mapToRelatedCampaignItem(result)
                    ChooseRelatedCampaignResult.Success(
                        keyword,
                        previousCampaigns
                    )
                }
            } catch (e: Throwable) {
                ChooseRelatedCampaignResult.Fail(e)
            }
        }

    val isAddRelatedCampaignButtonActive: LiveData<Boolean> = _selectedCampaigns.map {
        it.isNotEmpty()
    }

    init {
        collectPreviousCampaignResult()
    }

    private fun collectPreviousCampaignResult() {
        viewModelScope.launch {
            searchKeywordFlow.collect { result ->
                _relatedCampaignsResult.postValue(result)
            }
        }
    }

    fun setCampaignId(campaignId: Long) {
        this.campaignId = campaignId
    }

    fun setSelectedCampaigns(relatedCampaignIds: List<RelatedCampaignItem>) {
        this._selectedCampaigns.value = relatedCampaignIds
    }

    fun getPreviousCampaign(keyword: String = "") {
        this.keyword.value = keyword
    }

    private fun mapToRelatedCampaignItem(
        campaigns: List<CampaignUiModel>
    ): List<RelatedCampaignItem> {
        val selectedCampaigns = _selectedCampaigns.value ?: emptyList()
        val isMaxSelected = isMaxSelectable
        return campaigns.map {
            val isSelected = selectedCampaigns.any { selectedCampaign ->
                selectedCampaign.id == it.campaignId
            }
            RelatedCampaignItem(it.campaignId, it.campaignName, isSelected, isMaxSelected)
        }
    }

    fun onCampaignClicked(relatedCampaign: RelatedCampaignItem) {
        val previousCampaignsResult = _relatedCampaignsResult.value
        if (previousCampaignsResult is ChooseRelatedCampaignResult.Success) {
            val newValue = !relatedCampaign.isSelected
            updateSelectedCampaigns(newValue, relatedCampaign)
            val isMaxSelected = isMaxSelectable

            val newList = previousCampaignsResult.relatedCampaigns.map {
                if (it.id == relatedCampaign.id) {
                    it.copy(isSelected = newValue, isMaxSelected = isMaxSelected)
                } else it.copy(isMaxSelected = isMaxSelected)
            }
            val newResult = previousCampaignsResult.copy(
                relatedCampaigns = newList,
            )
            _relatedCampaignsResult.postValue(newResult)
        }
    }

    private fun updateSelectedCampaigns(
        isSelected: Boolean,
        relatedCampaign: RelatedCampaignItem
    ) {
        val selectedCampaigns = _selectedCampaigns.value ?: emptyList()
        _selectedCampaigns.value = if (isSelected) {
            selectedCampaigns + relatedCampaign
        } else {
            selectedCampaigns.filter { it.id != relatedCampaign.id }
        }
    }

    fun getRelatedCampaignsSelected(): List<RelatedCampaign> {
        val selectedCampaigns = _selectedCampaigns.value ?: emptyList()
        return selectedCampaigns.map {
            RelatedCampaign(it.id, it.name)
        }
    }
}