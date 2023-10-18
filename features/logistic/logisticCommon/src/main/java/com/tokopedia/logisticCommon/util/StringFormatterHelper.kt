package com.tokopedia.logisticCommon.util

object StringFormatterHelper {

    private const val HTML_BOLD_FORMAT = "<b>%s</b>"
    private const val HTML_STRIKETHROUGH_FORMAT = "<s>%s</s>"
    private const val HTML_HYPERLINK_FORMAT = "<a href=\"%s\">%s</a>"

    fun StringBuilder.appendHtmlBoldText(text: String) {
        if (text.isNotBlank()) {
            append(String.format(HTML_BOLD_FORMAT, text))
        }
    }

    fun StringBuilder.appendHtmlStrikethroughText(text: String) {
        if (text.isNotBlank()) {
            append(String.format(HTML_STRIKETHROUGH_FORMAT, text))
        }
    }

    fun StringBuilder.appendHyperlinkText(label: String, url: String) {
        if (label.isNotBlank() && url.isNotBlank()) {
            append(String.format(HTML_HYPERLINK_FORMAT, url, label))
        }
    }
    fun StringBuilder.appendEmptySpace() {
        append(" ")
    }
}
