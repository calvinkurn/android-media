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
        val item: String,
        val text: String,
        val link: String,
    )

    companion object {
        val Dummy = TickerBottomSheetUiModel(
            mainText = listOf(
                MainText(
                    action = listOf(
                        Action(
                            item = "key item 1",
                            text = "text link",
                            link = "tokopedia.com",
                        )
                    ),
                    title = "Test Title",
                    description = "Test Description",
                )
            ),
            page = "",
            type = TickerBottomSheetPageType.BOTTOM_SHEET,
            imageURL = "tokopedia.com",
            bottomText = BottomText(
                action = listOf(
                    Action(
                        item = "key item 1",
                        text = "text link",
                        link = "tokopedia.com",
                    )
                ),
                description = "Test Description"
            )
        )
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

