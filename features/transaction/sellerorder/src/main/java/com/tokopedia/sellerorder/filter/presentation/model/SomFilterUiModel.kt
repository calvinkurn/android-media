package com.tokopedia.sellerorder.filter.presentation.model

import com.tokopedia.sellerorder.filter.presentation.adapter.TypeFactorySomFilterAdapter

data class SomFilterUiModel(
        val nameFilter: String = "",
        val somFilterData: List<SomFilterChipsUiModel> = listOf(),
        val canSelectMany: Boolean = false,
        val isDividerVisible: Boolean = false
): BaseSomFilter {
    override fun type(typeFactory: TypeFactorySomFilterAdapter): Int {
        return typeFactory.type(this)
    }
}