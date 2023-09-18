package com.tokopedia.logisticCommon.util

object StringFormatterHelper {

    private const val HTML_BOLD_FORMAT = "<b>%s</b>"
    private const val HTML_STRIKETHROUGH_FORMAT = "<s>%s</s>"
    private const val HTML_HYPERLINK_FORMAT = "<a href=\"%s\">%s</a>"
    private const val BACKSLASH_FORMAT = "%s\\"
    private const val STRIP_FORMAT = """%s - """

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

    fun StringBuilder.appendWithBackSlashOnCondition(text: String?, condition: Boolean = true, default: String? = null) {
        if (condition && text?.isNotEmpty() == true) {
            append(String.format(BACKSLASH_FORMAT, text))
        } else {
            default?.let { defaultText -> append(String.format(BACKSLASH_FORMAT, defaultText)) }
        }
    }

    fun StringBuilder.appendWithStrip(text: String?, condition: Boolean = true, default: String? = null) {
        if (condition && text?.isNotEmpty() == true) {
            append(String.format(STRIP_FORMAT, text))
        } else {
            default?.let { defaultText -> append(String.format(STRIP_FORMAT, defaultText)) }
        }
    }

    fun StringBuilder.appendWithCondition(text: String?, condition: Boolean = true, default: String? = null) {
        if (condition && text?.isNotEmpty() == true) {
            append(text)
        } else {
            default?.let { defaultText -> append(defaultText) }
        }
    }
}
