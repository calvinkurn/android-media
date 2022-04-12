package com.tokopedia.tokopedianow.repurchase.presentation.uimodel

import android.os.Parcelable
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment.Companion.ALL_DATE_TRANSACTION_POSITION
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseTypeFactory
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.parcel.Parcelize

data class RepurchaseSortFilterUiModel(
    val id: String,
    val sortFilterList: List<RepurchaseSortFilter>
): Visitable<RepurchaseTypeFactory> {

    override fun type(typeFactory: RepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class RepurchaseSortFilter(
        @StringRes val title: Int,
        @StringRes val titleFormat: Int? = null,
        val selectedItem: SelectedSortFilter? = null,
        val selectedDateFilter: SelectedDateFilter? = null,
        val chipType: String = ChipsUnify.TYPE_NORMAL,
        val sort: Int = FREQUENTLY_BOUGHT,
        val type: RepurchaseSortFilterType
    )

    @Parcelize
    data class SelectedSortFilter(
        val id: List<String> = emptyList(),
        val title: List<String> = emptyList()
    ) : Parcelable

    @Parcelize
    data class SelectedDateFilter(
        var position: Int = ALL_DATE_TRANSACTION_POSITION,
        var startDate: String = "",
        var endDate: String = "",
    ) : Parcelable

    enum class RepurchaseSortFilterType {
        SORT,
        DATE_FILTER,
        CATEGORY_FILTER
    }
}