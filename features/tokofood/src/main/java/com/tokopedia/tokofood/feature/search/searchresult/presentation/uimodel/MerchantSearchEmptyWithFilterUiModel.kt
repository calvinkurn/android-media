package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultTypeFactory

object MerchantSearchEmptyWithFilterUiModel: Visitable<TokofoodSearchResultTypeFactory> {

    override fun type(typeFactory: TokofoodSearchResultTypeFactory): Int =
        typeFactory.type(this)

}