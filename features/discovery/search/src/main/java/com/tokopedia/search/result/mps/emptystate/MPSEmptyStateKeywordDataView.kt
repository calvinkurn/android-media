package com.tokopedia.search.result.mps.emptystate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory

object MPSEmptyStateKeywordDataView: Visitable<MPSTypeFactory> {

    override fun type(typeFactory: MPSTypeFactory?): Int =
        typeFactory?.type(this) ?: 0
}
