package com.tokopedia.chat_common.util

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
    @RequiresApi(Build.VERSION_CODES.M)
    fun animateTypingImage(start: Boolean, anim: AnimatedVectorDrawable) {
        toggleAnimation(start, anim as Animatable)
        anim.registerAnimationCallback(setAnimatable2Callback(anim))
    }

    fun animateTypingImage(start: Boolean, anim: AnimatedVectorDrawableCompat) {
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAnimatable2Callback(anim: Animatable2): Animatable2.AnimationCallback {
        return object: Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                anim.start()
            }
        }
    }

    private fun setAnimatable2CompatCallback(anim: Animatable2Compat): Animatable2Compat.AnimationCallback {
        return object: Animatable2Compat.AnimationCallback() {
            private val animHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                animHandler.post(anim::start)
            }
        }
    }
}