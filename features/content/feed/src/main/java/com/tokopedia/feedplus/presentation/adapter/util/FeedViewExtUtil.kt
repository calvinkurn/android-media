package com.tokopedia.feedplus.presentation.adapter.util

import android.view.View

/**
 * Created By : Muhammad Furqan on 01/08/23
 */
fun View.animateAlpha(alpha: Float, duration: Long) {
    this.animate().alpha(alpha).duration = duration
}
