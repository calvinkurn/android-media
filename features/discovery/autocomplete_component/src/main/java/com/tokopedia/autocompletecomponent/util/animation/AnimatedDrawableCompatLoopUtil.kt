package com.tokopedia.autocompletecomponent.util.animation

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

internal class AnimatedDrawableCompatLoopUtil : AnimatedDrawableLoopUtil {
    override fun startLoopAnimation(drawable: Drawable?) {
        if (drawable is AnimatedVectorDrawableCompat) {
            drawable.startLoopAnimation()
        }
    }

    override fun stopLoopAnimation(drawable: Drawable?) {
        if (drawable is AnimatedVectorDrawableCompat) {
            drawable.stopLoopAnimation()
        }
    }

    private val animatableCompatAnimationCallback = object : Animatable2Compat.AnimationCallback() {
        private val animationHandler = Handler(Looper.getMainLooper())
        override fun onAnimationEnd(drawable: Drawable?) {
            if (drawable is AnimatedVectorDrawableCompat) {
                animationHandler.post { drawable.start() }
            }
        }
    }

    private fun AnimatedVectorDrawableCompat.startLoopAnimation() {
        registerAnimationCallback(animatableCompatAnimationCallback)
        start()
    }

    private fun AnimatedVectorDrawableCompat.stopLoopAnimation() {
        unregisterAnimationCallback(animatableCompatAnimationCallback)
        clearAnimationCallbacks()
        stop()
    }
}
