package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter

import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel

interface TokofoodSearchResultTypeFactory {
    fun type(uiModel: MerchantSearchResultUiModel): Int
    fun type(uiModel: MerchantSearchEmptyWithoutFilterUiModel): Int
    fun type(uiModel: MerchantSearchEmptyWithFilterUiModel): Int
    fun type(uiModel: MerchantSearchOOCUiModel): Int
}