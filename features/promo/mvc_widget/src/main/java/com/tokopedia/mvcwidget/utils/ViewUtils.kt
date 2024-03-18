package com.tokopedia.mvcwidget.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal fun Context.getUnifyColorFromHex(hex: String): Int {
    return runCatching {
        stringToUnifyColor(this, hex).unifyColor!!
    }.getOrElse {
        runCatching { Color.parseColor(hex) }.getOrDefault(
            ContextCompat.getColor(
                this,
                unifyprinciplesR.color.Unify_NN950
            )
        )
    }
}

internal fun Context.parseColorSafely(hex: String): Int {
    return runCatching { Color.parseColor(hex) }.getOrDefault(
        ContextCompat.getColor(
            this,
            unifyprinciplesR.color.Unify_RN50
        )
    )
}

internal fun TextView.setAttribute(text: String, color: Int, typeFace: String) {
    this.text = text
    setTextColor(color)
    typeface = when (typeFace) {
        "bold" -> Typeface.DEFAULT_BOLD
        else -> Typeface.DEFAULT
    }
}
