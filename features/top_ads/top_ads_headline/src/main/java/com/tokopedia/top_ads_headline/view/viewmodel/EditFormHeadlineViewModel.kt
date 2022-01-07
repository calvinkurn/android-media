package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.top_ads_headline_usecase.CreateHeadlineAdsUseCase
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline_usecase.model.TopadsManageHeadlineAdResponse
import javax.inject.Inject

class EditFormHeadlineViewModel @Inject constructor(
        private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase
) : ViewModel() {

    fun editHeadlineAd(input: TopAdsManageHeadlineInput, onSuccess: (() -> Unit),
                       onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    createHeadlineAdsUseCase.setParams(input)
                    val response : TopadsManageHeadlineAdResponse.Data = createHeadlineAdsUseCase.executeOnBackground()
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