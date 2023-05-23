package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultTypeFactory

class MerchantSearchOOCUiModel(val type: Int) : Visitable<TokofoodSearchResultTypeFactory> {

    override fun type(typeFactory: TokofoodSearchResultTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val NO_ADDRESS_REVAMP = 1
        const val NO_PINPOINT = 2
        const val OUT_OF_COVERAGE = 3
    }
}
