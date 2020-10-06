package com.tokopedia.utils.contentdescription

import android.widget.TextView

object TextAndContentDescriptionUtil {

    @JvmStatic
    fun setTextAndContentDescription(textView :TextView?, text :String, contentDescriptionTemplate :String) {
        if (textView != null) {
            textView.text = text
            textView.contentDescription = "$contentDescriptionTemplate $text"
        }
    }
}
