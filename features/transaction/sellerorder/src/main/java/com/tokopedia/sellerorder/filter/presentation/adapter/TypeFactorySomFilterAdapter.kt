package com.tokopedia.sellerorder.filter.presentation.adapter

import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

interface TypeFactorySomFilterAdapter {
    fun type(sortUiModel: SomFilterUiModel): Int
}