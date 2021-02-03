package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.top_ads_headline.Constants.HEADLINE
import com.tokopedia.top_ads_headline.Constants.SOURCE_CREATE_HEADLINE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import javax.inject.Inject

/**
 * Created by Pika on 6/11/20.
 */
private const val TYPE_HEADLINE = 3
class TopAdsHeadlineKeyViewModel @Inject constructor(
        private val bidInfoUseCase: BidInfoUseCase,
        private val suggestionKeywordUseCase: SuggestionKeywordUseCase) : ViewModel() {

    fun getSuggestionKeyword(productIds: String?, groupId: Int?, onSuccess: ((List<KeywordData>) -> Unit), onEmpty: (() -> Unit)) {

        suggestionKeywordUseCase.setParams(groupId, productIds?.trim(),TYPE_HEADLINE)
        suggestionKeywordUseCase.executeQuerySafeMode({
            if (it.topAdsGetKeywordSuggestionV3.data.isEmpty())
                onEmpty()
            else
                onSuccess(it.topAdsGetKeywordSuggestionV3.data)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun getBidInfo(suggestion: List<DataSuggestions>, onSuccess: ((List<TopadsBidInfo.DataItem>) -> Unit), onEmpty: (() -> Unit)) {
        bidInfoUseCase.setParams(suggestion, HEADLINE, SOURCE_CREATE_HEADLINE)
        bidInfoUseCase.executeQuerySafeMode({
            if (it.topadsBidInfo.data.isEmpty())
                onEmpty()
            else
                onSuccess(it.topadsBidInfo.data)

        }, {
            it.printStackTrace()
        })
    }

}
