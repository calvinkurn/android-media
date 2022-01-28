package com.tokopedia.play.broadcaster.ui.model.sort

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
data class SortUiModel(
    val id: Int,
    val text: String,
)

data class SortFilterUiModel(
    val selectedId: Int,
    val sortList: List<SortUiModel>,
) {
    companion object {
        val Empty: SortFilterUiModel
            get() = SortFilterUiModel(
                selectedId = -1,
                sortList = emptyList()
            )
    }
}

