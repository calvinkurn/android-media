package com.tokopedia.autocompletecomponent.util.animation

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
internal class AnimatedDrawableMarshmallowOrAboveLoopUtil : AnimatedDrawableLoopUtil {
    override fun startLoopAnimation(drawable: Drawable?) {
        when (drawable) {
            is AnimatedVectorDrawable -> drawable.startLoopAnimation()
        }
    }

    override fun stopLoopAnimation(drawable: Drawable?) {
        when (drawable) {
            is AnimatedVectorDrawable -> drawable.stopLoopAnimation()
        }
    }

    private val animatableAnimationCallback = object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            if (drawable is AnimatedVectorDrawable) {
                drawable.start()
            }
        }
    }

    private fun AnimatedVectorDrawable.startLoopAnimation() {
        registerAnimationCallback(animatableAnimationCallback)
        start()
    }

    private fun AnimatedVectorDrawable.stopLoopAnimation() {
        unregisterAnimationCallback(animatableAnimationCallback)
        clearAnimationCallbacks()
        stop()
    }
}
