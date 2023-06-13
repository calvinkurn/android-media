package com.tokopedia.tokofood.feature.search.common.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.common.presentation.adapter.TokofoodSearchTypeFactory

class TokofoodSearchErrorStateUiModel(val globalErrorType: Int): Visitable<TokofoodSearchTypeFactory> {

    override fun type(typeFactory: TokofoodSearchTypeFactory): Int {
        return typeFactory.type(this)
    }

}