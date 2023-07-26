package com.tokopedia.play.broadcaster.ui.model.livetovod

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.content.common.util.buildSpannedString
import com.tokopedia.content.common.util.setSpanOnText

data class TickerBottomSheetUiModel(
    val type: TickerBottomSheetType,
    val page: TickerBottomSheetPage,
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
            page = TickerBottomSheetPage.UNKNOWN,
            type = TickerBottomSheetType.UNKNOWN,
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
            page = TickerBottomSheetPage.UNKNOWN,
            type = TickerBottomSheetType.UNKNOWN,
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

enum class TickerBottomSheetPage(val value: String) {
    LIVE_PREPARATION("live_preparation"),
    LIVE_REPORT("live_report"),
    UNKNOWN(""),
}

enum class TickerBottomSheetType {
    BOTTOM_SHEET,
    TICKER,
    UNKNOWN,
}

fun generateSpanText(
    fullText: String,
    action: List<TickerBottomSheetUiModel.Action>,
    color: ForegroundColorSpan,
    onTextCLick: (appLink: String) -> Unit,
): CharSequence {
    if (action.isEmpty()) return fullText

    var newFullText = fullText

    val mappedText = buildSpannedString {
        action.forEach { data ->
            newFullText = newFullText.replaceFirst(data.item, data.text)
        }
        append(newFullText)
        action.forEach { data ->
            setSpanOnText(
                data.text,
                StyleSpan(Typeface.BOLD),
                color,
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        onTextCLick.invoke(data.link)
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                },
            )
        }
    }

    return mappedText
}

fun generateHtmlSpanText(
    fullText: String,
    action: List<TickerBottomSheetUiModel.Action>,
): String {
    if (action.isEmpty()) return fullText

    var newFullText = fullText
    val mappedText = action.map { data ->
        if (!newFullText.contains(data.item)) return fullText

        newFullText = newFullText.replaceFirst(
            data.item, "<a href=\"${data.link}\">${data.text}</a>"
        )
        newFullText
    }.last()

    val finalText = buildString {
        append(mappedText)
    }

    return finalText
}
