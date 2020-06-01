package com.tokopedia.utils.htmltags

import android.text.Editable

class BulletedList(private val listTagHelper: ListTagHelper) : IListTags {

    override fun openTag(text: Editable) {
        listTagHelper.addNewLine(text)
        listTagHelper.insertInvisibleMarkerAtStart(text, BulletedListMark())
    }

    override fun closeTag(text: Editable) {
        listTagHelper.addNewLine(text)
        listTagHelper.getMarkerPosition<BulletedListMark>(text)?.let { marker ->
            listTagHelper.setSpanFromMarker(text, marker, SpanLeadingListItem(30, "•"))
        }
    }

}