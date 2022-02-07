package com.tokopedia.play.broadcaster.ui.model.sort

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
data class SortUiModel(
    val id: Int,
    val text: String,
) {
    companion object {
        val supportedSortList: List<SortUiModel> = listOf(
            SortUiModel(
                id = 2,
                text = "Terbaru",
            ),
            SortUiModel(
                id = 23,
                text = "Paling Sesuai",
            ),
            SortUiModel(
                id = 9,
                text = "Harga Terendah",
            ),
            SortUiModel(
                id = 10,
                text = "Harga Tertinggi",
            ),
            SortUiModel(
                id = 11,
                text = "Ulasan Terbanyak",
            ),
        )

        fun getSortById(id: Int): SortUiModel? {
            return supportedSortList.firstOrNull { it.id == id }
        }

        val Empty: SortUiModel
            get() = supportedSortList.first()
    }
}

