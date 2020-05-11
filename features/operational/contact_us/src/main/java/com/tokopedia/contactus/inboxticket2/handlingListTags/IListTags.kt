package com.tokopedia.contactus.inboxticket2.handlingListTags

import android.text.Editable

interface IListTags {
    fun openTag(text: Editable)
    fun closeTag(text: Editable)
}