package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter

import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel

interface TokofoodSearchResultTypeFactory {
    fun type(uiModel: MerchantSearchResultUiModel): Int
}