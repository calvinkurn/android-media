package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import javax.inject.Inject

class EditAdCostViewModel @Inject constructor(
        private val getSuggestionKeywordUseCase: SuggestionKeywordUseCase
) : ViewModel() {

    fun getSuggestionKeyword(productIds: String?, groupId: Int?, onSuccess: ((List<KeywordData>) -> Unit)) {
        getSuggestionKeywordUseCase.setParams(groupId, productIds?.trim())
        getSuggestionKeywordUseCase.executeQuerySafeMode({
            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

}