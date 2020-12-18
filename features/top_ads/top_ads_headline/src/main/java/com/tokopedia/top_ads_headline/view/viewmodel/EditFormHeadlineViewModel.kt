package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.usecase.CreateHeadlineAdsUseCase
import javax.inject.Inject

class EditFormHeadlineViewModel @Inject constructor(
        private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase
) : ViewModel() {

    fun editHeadlineAd(input: TopAdsManageHeadlineInput) {
        createHeadlineAdsUseCase.setParams(input)
    }

}