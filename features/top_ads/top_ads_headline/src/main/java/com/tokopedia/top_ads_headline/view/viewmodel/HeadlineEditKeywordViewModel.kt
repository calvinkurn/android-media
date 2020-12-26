package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.topads.common.data.internal.ParamObject.EDIT_HEADLINE_PAGE
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import javax.inject.Inject

class HeadlineEditKeywordViewModel @Inject constructor(
        private val getAdKeywordUseCase: GetAdKeywordUseCase
) : ViewModel() {

    fun getAdKeyword(shopId: String, groupId: Int, cursor: String, onSuccess: (List<GetKeywordResponse.KeywordsItem>, cursor: String) -> Unit) {
        getAdKeywordUseCase.setParams(groupId, cursor, shopId, EDIT_HEADLINE_PAGE)
        getAdKeywordUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsListKeyword.data.keywords, it.topAdsListKeyword.data.pagination.cursor)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

}