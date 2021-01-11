package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.top_ads_headline.Constants.EDIT_HEADLINE_PAGE
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_NEGATIVE_EXACT
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.top_ads_headline.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.top_ads_headline.view.fragment.KEYWORD_POSITIVE
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import javax.inject.Inject

class HeadlineEditKeywordViewModel @Inject constructor(
        private val getAdKeywordUseCase: GetAdKeywordUseCase
) : ViewModel() {

    private var selectedKeywords: ArrayList<GetKeywordResponse.KeywordsItem> = ArrayList()

    fun getAdKeyword(shopId: String, groupId: Int, cursor: String, onSuccess: (List<GetKeywordResponse.KeywordsItem>, cursor: String) -> Unit, keywordType: String) {
        getAdKeywordUseCase.setParams(groupId, cursor, shopId, EDIT_HEADLINE_PAGE, keywordStatus = listOf(1, 3))
        getAdKeywordUseCase.executeQuerySafeMode(
                {
                    if (cursor.isBlank()) {
                        selectedKeywords.clear()
                    }
                    addSelectedKeywords(keywordType, it.topAdsListKeyword.data.keywords)
                    onSuccess(getDeepCopyOfSelectedKeywords(), it.topAdsListKeyword.data.pagination.cursor)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    private fun getDeepCopyOfSelectedKeywords(): List<GetKeywordResponse.KeywordsItem> {
        val list  = ArrayList<GetKeywordResponse.KeywordsItem>()
        selectedKeywords.forEach {
            list.add(it.copy())
        }
        return list
    }

    private fun addSelectedKeywords(keywordType: String, keywords: List<GetKeywordResponse.KeywordsItem>) {
        if (keywordType == KEYWORD_POSITIVE) {
            val positiveKeywordList = keywords.filter { it.type == KEYWORD_TYPE_PHRASE || it.type == KEYWORD_TYPE_EXACT }
            selectedKeywords.addAll(positiveKeywordList)
        } else {
            val positiveKeywordList = keywords.filter { it.type == KEYWORD_TYPE_NEGATIVE_PHRASE || it.type == KEYWORD_TYPE_NEGATIVE_EXACT }
            selectedKeywords.addAll(positiveKeywordList)
        }
    }

    fun getSelectedKeywords() = selectedKeywords

}