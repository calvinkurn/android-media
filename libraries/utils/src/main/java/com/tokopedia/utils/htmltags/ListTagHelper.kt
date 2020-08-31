package com.tokopedia.utils.htmltags

import android.text.Editable
import android.text.Spannable
import android.text.Spanned

class ListTagHelper {

    fun setSpanFromMarker(text: Spannable, marker: Marker, styleSpan: Any) {
        val markerLocation = text.getSpanStart(marker)

        text.removeSpan(marker)

        val end = text.length
        text.setSpan(styleSpan, markerLocation, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    inline fun <reified T : Marker> getMarkerPosition(text: Editable) =
            text.getSpans(0, text.length, T::class.java).lastOrNull()

    fun addNewLine(text: Editable) {
        if (text.isNotEmpty() && text.last() != '\n') {
            text.append("\n")
        }
    }

    fun insertInvisibleMarkerAtStart(
            text: Editable,
            marker: Marker
    ) {
        val position = text.length
        text.setSpan(marker, position, position, Spanned.SPAN_MARK_MARK)
    }
}