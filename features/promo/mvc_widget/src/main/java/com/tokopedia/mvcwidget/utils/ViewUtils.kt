package com.tokopedia.mvcwidget.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import com.tokopedia.unifyprinciples.stringToUnifyColor

internal fun Context.getUnifyColorFromHex(hex: String): Int {
    return runCatching {
        stringToUnifyColor(this, hex).unifyColor!!
    }.getOrElse {
        runCatching { Color.parseColor(hex) }.getOrDefault(Color.parseColor("#212121"))
    }
}

internal fun TextView.setAttribute(text: String, color: Int, typeFace: String) {
    this.text = text
    setTextColor(color)
    typeface = when (typeFace) {
        "bold" -> Typeface.DEFAULT_BOLD
        else -> Typeface.DEFAULT
    }
}
