package com.tokopedia.universal_sharing.view.bottomsheet

enum class UiState {
    DEFAULT, CHIP_LIST, CHIP_SELECTED
}

data class UniversalShareState(
    val state: UiState,
    val chipList: List<Map<Int, String>>,
    val chipSelectedId: Int
) {

    companion object {
        fun default(): UniversalShareState {
            return UniversalShareState(
                state = UiState.DEFAULT,
                chipList = emptyList(),
                chipSelectedId = -1
            )
        }
    }
}
