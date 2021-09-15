package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import android.os.Parcelable
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RecentPurchaseTypeFactory
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.parcel.Parcelize

data class RepurchaseSortFilterUiModel(
    val id: String,
    val sortFilterList: List<RepurchaseSortFilter>
): Visitable<RecentPurchaseTypeFactory> {

    override fun type(typeFactory: RecentPurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class RepurchaseSortFilter(
        @StringRes val title: Int,
        @StringRes val qtyFormat: Int? = null,
        val selectedItem: SelectedSortFilter? = null,
        val chipType: String = ChipsUnify.TYPE_NORMAL,
        val type: RepurchaseSortFilterType
    )

    @Parcelize
    data class SelectedSortFilter(
        val id: List<String> = emptyList(),
        val title: List<String> = emptyList()
    ) : Parcelable

    enum class RepurchaseSortFilterType {
        SORT,
        DATE_FILTER,
        CATEGORY_FILTER
    }
}