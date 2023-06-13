package com.tokopedia.campaign.utils.extension

import android.graphics.Color
import android.widget.ImageView

private const val DIMMED_80_PERCENT_GREY = "#CC40444E"

fun ImageView.dimmed(hexColor : String = DIMMED_80_PERCENT_GREY) {
    this.setColorFilter(Color.parseColor(hexColor))
}

fun ImageView.resetDimmedBackground() {
    this.colorFilter = null
}
