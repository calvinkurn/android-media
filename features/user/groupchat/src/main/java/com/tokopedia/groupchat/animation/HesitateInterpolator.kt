package com.tokopedia.groupchat.animation

import android.view.animation.Interpolator

/**
 * @author : Steven 24/01/19
 */
class HesitateInterpolator(var direction: Int = 1) : Interpolator {

    override fun getInterpolation(t: Float): Float {
        return Math.sin(t * Math.PI / 2).toFloat() * 2 * direction
    }
}