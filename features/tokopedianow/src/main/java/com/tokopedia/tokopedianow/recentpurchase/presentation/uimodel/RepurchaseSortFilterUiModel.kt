package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory
import com.tokopedia.unifycomponents.ChipsUnify

data class RepurchaseSortFilterUiModel(
    val sortFilterList: List<RepurchaseSortFilter>
): Visitable<RecentPurchaseTypeFactory> {

    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class RepurchaseSortFilter(
        @StringRes val title: Int,
        val selectedItems: ArrayList<String> = arrayListOf(),
        val chipType: String = ChipsUnify.TYPE_NORMAL,
        val filterType: RepurchaseSortFilterType
    )

    enum class RepurchaseSortFilterType {
        SORT,
        DATE_FILTER,
        CATEGORY_FILTER
    }
}