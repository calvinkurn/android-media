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
    val value: SortValue,
) : Parcelable {
    companion object {
        val supportedSortList: List<SortUiModel> = listOf(
            SortUiModel(
                key = "UPDATE_TIME",
                text = "Terbaru",
                value = SortValue.Descending,
            ),
            SortUiModel(
                key = "PRICE",
                text = "Harga Terendah",
                value = SortValue.Ascending,
            ),
            SortUiModel(
                key = "PRICE",
                text = "Harga Tertinggi",
                value = SortValue.Descending,
            ),
        )

        val Empty: SortUiModel
            get() = supportedSortList.first()
    }
}

enum class SortValue(val value: String) {
    Ascending("ASC"),
    Descending("DESC"),
}
