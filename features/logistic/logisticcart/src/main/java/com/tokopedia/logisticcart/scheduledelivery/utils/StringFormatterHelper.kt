package com.tokopedia.logisticcart.scheduledelivery.utils

object StringFormatterHelper {

    private const val HTML_BOLD_FORMAT = "<b>%s</b>"
    private const val HTML_STRIKETHROUGH_FORMAT = "<s>%s</s>"

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
}
