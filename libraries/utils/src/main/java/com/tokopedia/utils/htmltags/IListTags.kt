package com.tokopedia.utils.htmltags

import android.text.Editable

interface IListTags {
    fun openTag(text: Editable)
    fun closeTag(text: Editable)
}