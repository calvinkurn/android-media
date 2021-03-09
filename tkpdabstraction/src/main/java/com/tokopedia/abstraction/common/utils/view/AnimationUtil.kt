package com.tokopedia.abstraction.common.utils.view

import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

object AnimationUtil {

    /**
     * @param start Boolean toggle start animate / stop animate
     * @param anim AnimatedVectorDrawable to animate & register to callback
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun animateImage(start: Boolean, anim: AnimatedVectorDrawable) {
        toggleAnimation(start, anim as Animatable)
        anim.registerAnimationCallback(setAnimatable2Callback(anim))
    }

    /**
     * @param start Boolean toggle start animate / stop animate
     * @param anim AnimatedVectorDrawableCompat to animate & register to callback
     */
    fun animateImageCompat(start: Boolean, anim: AnimatedVectorDrawableCompat) {
        toggleAnimation(start, anim as Animatable)
        anim.registerAnimationCallback(setAnimatable2CompatCallback(anim))
    }

    private fun toggleAnimation(start: Boolean, anim: Animatable) {
        if(start) {
            anim.start()
        } else {
            anim.stop()
        }
    }

    /**
     * @param anim Animatable2 loop animation when it ends
     * @return Animatable2.AnimationCallback to be registered to Animatable2
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAnimatable2Callback(anim: Animatable2): Animatable2.AnimationCallback {
        return object: Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                anim.start()
            }
        }
    }

    /**
     * @param anim Animatable2Compat loop animation when it ends
     * @return Animatable2Compat.AnimationCallback to be registered to Animatable2
     */
    private fun setAnimatable2CompatCallback(anim: Animatable2Compat): Animatable2Compat.AnimationCallback {
        return object: Animatable2Compat.AnimationCallback() {
            private val animHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                animHandler.post(anim::start)
            }
        }
    }
}