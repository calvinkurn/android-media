package com.tokopedia.play.broadcaster.ui.model.livetovod

data class TickerBottomSheetUiModel(
    val page: String,
    val type: TickerBottomSheetPageType,
    val imageURL: String,
    val bottomText: BottomText,
    val mainText: List<MainText>,
) {
    data class BottomText(
        val action: List<Action>,
        val description: String,
    )

    data class MainText(
        val action: List<Action>,
        val description: String,
        val title: String,
    )

    data class Action(
        val index: String,
        val text: String,
        val link: String,
    )

    companion object {
        val Empty = TickerBottomSheetUiModel(
            page = TickerBottomSheetPageType.UNKNOWN.value,
            type = TickerBottomSheetPageType.UNKNOWN,
            imageURL = "",
            bottomText = BottomText(
                action = listOf(),
                description = "",
            ),
            mainText = listOf(
                MainText(
                    action = listOf(),
                    title = "",
                    description = "",
                ),
            ),
        )
    }
}

enum class TickerBottomSheetPageType(val value: String) {
    BOTTOM_SHEET("test_bottomsheet"),
    TICKER("test_ticker"),
    UNKNOWN(""),
}

