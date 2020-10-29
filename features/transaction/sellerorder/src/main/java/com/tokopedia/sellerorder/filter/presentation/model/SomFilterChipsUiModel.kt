package com.tokopedia.sellerorder.filter.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SomFilterChipsUiModel(
        val idStatus: List<Int> = listOf(),
        val id: Int = 0,
        val name: String = "", //equal order_status
        val key: String = "", //equal key of status_list
        var isSelected: Boolean = false,
        val amount: Int = 0,
        val childStatus: List<ChildStatusUiModel> = listOf()
): Parcelable {
    @Parcelize
    data class ChildStatusUiModel(
            val childId: List<Int> = listOf(),
            val key: String = "",
            val text: String = "",
            val isChecked: Boolean = false
    ): Parcelable
}