package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultTypeFactory

data class MerchantSearchResultUiModel(
    val id: String,
    val merchant: Merchant
) : Visitable<TokofoodSearchResultTypeFactory> {

    override fun type(typeFactory: TokofoodSearchResultTypeFactory): Int {
        return typeFactory.type(this)
    }

}