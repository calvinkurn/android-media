package com.tokopedia.sellerorder.filter.presentation.model

import com.tokopedia.sellerorder.filter.presentation.adapter.TypeFactorySomFilterAdapter

class SomFilterDateUiModel(val nameFilter: String, var date: String = ""): BaseSomFilter {
    override fun type(typeFactory: TypeFactorySomFilterAdapter): Int {
        return typeFactory.type(this)
    }
}