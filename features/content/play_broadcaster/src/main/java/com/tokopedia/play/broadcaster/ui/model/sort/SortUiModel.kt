package com.tokopedia.play.broadcaster.ui.model.sort

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
@Parcelize
data class SortUiModel(
    val key: String,
    val text: String,
    val direction: SortDirection,
) : Parcelable {
    companion object {
        val supportedSortList: List<SortUiModel> = listOf(
            SortUiModel(
                key = "UPDATE_TIME",
                text = "Terbaru",
                direction = SortDirection.Descending,
            ),
            SortUiModel(
                key = "PRICE",
                text = "Harga Terendah",
                direction = SortDirection.Ascending,
            ),
            SortUiModel(
                key = "PRICE",
                text = "Harga Tertinggi",
                direction = SortDirection.Descending,
            ),
        )

        val Empty: SortUiModel
            get() = supportedSortList.first()
    }
}

enum class SortDirection(val value: String) {
    Ascending("ASC"),
    Descending("DESC"),
}
