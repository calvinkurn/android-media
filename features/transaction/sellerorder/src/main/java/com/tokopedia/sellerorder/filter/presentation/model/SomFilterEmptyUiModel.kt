package com.tokopedia.sellerorder.filter.presentation.model

import com.tokopedia.sellerorder.filter.presentation.adapter.TypeFactorySomFilterAdapter

class SomFilterEmptyUiModel: BaseSomFilter {
    override fun type(typeFactory: TypeFactorySomFilterAdapter): Int {
        return typeFactory.type(this)
    }
}