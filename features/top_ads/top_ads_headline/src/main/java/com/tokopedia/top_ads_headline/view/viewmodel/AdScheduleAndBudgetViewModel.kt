package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.usecase.CreateHeadlineAdsUseCase
import javax.inject.Inject

class AdScheduleAndBudgetViewModel @Inject constructor(private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase) : ViewModel() {

    fun createHeadlineAd(input: TopAdsManageHeadlineInput, onSuccess: (() -> Unit),
                         onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    createHeadlineAdsUseCase.setParams(input)
                    val response = createHeadlineAdsUseCase.executeOnBackground()
                    if (response.topadsManageHeadlineAd.success.id.isNotEmpty()) {
                        onSuccess()
                    } else {
                        onError(response.topadsManageHeadlineAd.errors.first().detail)
                    }
                },
                onError = {
                    onError(it.message ?: "")
                    it.printStackTrace()
                }
        )
    }
}