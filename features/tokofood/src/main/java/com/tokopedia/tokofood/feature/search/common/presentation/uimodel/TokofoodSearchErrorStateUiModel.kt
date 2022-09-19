package com.tokopedia.tokofood.feature.search.common.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.common.presentation.adapter.SearchTypeFactory

class TokofoodSearchErrorStateUiModel(val globalErrorType: Int): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }

}