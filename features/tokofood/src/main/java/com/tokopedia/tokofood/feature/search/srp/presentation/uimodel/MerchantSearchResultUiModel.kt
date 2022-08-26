package com.tokopedia.tokofood.feature.search.srp.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.search.srp.presentation.adapter.TokofoodSearchResultTypeFactory

data class MerchantSearchResultUiModel(
    val id: String,
    val merchant: Merchant,
    val branchApplink: String?
) : Visitable<TokofoodSearchResultTypeFactory> {

    override fun type(typeFactory: TokofoodSearchResultTypeFactory): Int {
        return typeFactory.type(this)
    }

}