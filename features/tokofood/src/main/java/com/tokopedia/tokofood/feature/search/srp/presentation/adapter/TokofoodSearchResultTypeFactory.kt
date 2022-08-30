package com.tokopedia.tokofood.feature.search.srp.presentation.adapter

import com.tokopedia.tokofood.feature.search.srp.presentation.uimodel.MerchantSearchResultUiModel

interface TokofoodSearchResultTypeFactory {
    fun type(uiModel: MerchantSearchResultUiModel): Int
}