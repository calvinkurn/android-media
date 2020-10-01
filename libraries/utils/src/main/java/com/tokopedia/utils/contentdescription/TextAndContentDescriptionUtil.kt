package com.tokopedia.utils.contentdescription

import android.widget.TextView

object TextAndContentDescriptionUtil {

    @JvmStatic
    fun setTextAndContentDescription(textView :TextView, text :String, contentDescriptionTemplate :String) {
        textView.text = text;
        textView.contentDescription = text+contentDescriptionTemplate
    }
}
