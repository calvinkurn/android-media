package com.tokopedia.tokofood.feature.search.srp.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.srp.domain.response.TokofoodSearchMerchantItem
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.TokofoodSearchResultTypeFactory

data class MerchantSearchResultUiModel(
    val id: String,
    val merchant: TokofoodSearchMerchantItem,
    val branchApplink: String?
) : Visitable<TokofoodSearchResultTypeFactory> {

    override fun type(typeFactory: TokofoodSearchResultTypeFactory): Int {
        return typeFactory.type(this)
    }

}