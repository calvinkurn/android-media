package com.tokopedia.mvcwidget.views

import android.view.View
import com.tokopedia.promoui.common.dpToPx

fun View.slideUpFromMiddle(duration: Long = 500, visibility: Int = View.VISIBLE, completion: (() -> Unit)? = null) {
        animate()
            .alpha(1f)
            .translationY(-height.toFloat() - dpToPx(10))
            .setStartDelay(100L)
            .setDuration(duration)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
    }

    fun View.slideUpFromBottom(duration: Long = 500, completion: (() -> Unit)? = null) {
        y = height.toFloat()
        animate()
            .alpha(1f)
            .setDuration(duration)
            .translationY(0f)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
    }
