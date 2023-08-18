package com.tokopedia.stories.widget.animation

import android.animation.TimeInterpolator

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
class LinearEasingInterpolator : TimeInterpolator {

    override fun getInterpolation(input: Float): Float {
        return input
    }
}
