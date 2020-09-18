package com.tokopedia.searchbar.helper

import android.view.animation.Interpolator
import androidx.annotation.NonNull
import com.tokopedia.searchbar.helper.EasingProvider.get

class EasingInterpolator(@param:NonNull val ease: Ease) : Interpolator {

    override fun getInterpolation(input: Float): Float {
        return get(ease, input)
    }
}