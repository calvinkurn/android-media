package com.tokopedia.utils.htmltags

import android.text.Editable

class NumberedList(private val listTagHelper: ListTagHelper) : IListTags {

    private var index = 1
    override fun openTag(text: Editable) {
        listTagHelper.addNewLine(text)
        listTagHelper.insertInvisibleMarkerAtStart(text, NumberedListMark(index))
        index++
    }

    override fun closeTag(text: Editable) {
        listTagHelper.addNewLine(text)
        listTagHelper.getMarkerPosition<NumberedListMark>(text)?.let { marker ->
            listTagHelper.setSpanFromMarker(text, marker, SpanLeadingListItem(50, "${marker.index}."))
        }
    }
}