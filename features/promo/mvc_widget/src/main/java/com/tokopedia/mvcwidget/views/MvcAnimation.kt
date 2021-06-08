package com.tokopedia.mvcwidget.views

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.tokopedia.promoui.common.dpToPx

fun View.slideUpFromMiddle(duration: Long = 600, visibility: Int = View.VISIBLE, completion: (() -> Unit)? = null) {
        animate()
            .alpha(1f)
            .translationY(-height.toFloat()/3)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())

    }

    fun View.slideUpFromBottom(duration: Long = 600, completion: (() -> Unit)? = null) {
        y = height.toFloat()/3
        animate()
            .alpha(1f)
            .setDuration(duration)
            .translationY(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                completion?.let {
                    it()
                }
            }
    }
