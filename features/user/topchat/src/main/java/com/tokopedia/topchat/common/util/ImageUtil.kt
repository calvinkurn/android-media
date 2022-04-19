package com.tokopedia.topchat.common.util

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

object ImageUtil {
    private const val TYPING_LIGHT_MODE = "https://images.tokopedia.net/img/android/user/typing_motion_lightmode.gif"
    private const val TYPING_DARK_MODE = "https://images.tokopedia.net/img/android/user/typing_motion_darkmode.gif"

    fun loadGif(imageUnify: ImageUnify, gifUrl: String){
        Glide.with(imageUnify.context)
                .asGif()
                .load(gifUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageUnify)
    }

    fun setTypingAnimation(imageUnify: ImageUnify) {
        val animationType = getAnimationType()
        when(animationType) {
            "avd" -> setAVDTypingAnimation(imageUnify)
            "gif" -> setGifTypingAnimation(imageUnify)
        }
    }

    private fun setAVDTypingAnimation(imageUnify: ImageUnify) {
        val animatedVector = AnimatedVectorDrawableCompat.create(
                imageUnify.context, com.tokopedia.chat_common.R.drawable.topchat_typing_motion
        )
        imageUnify.setImageDrawable(animatedVector)
    }

    fun startAVDTypingAnimation(imageUnify: ImageUnify) {
        val animationType = getAnimationType()
        if(animationType == "avd") {
            try {
                setAnimationCallback(imageUnify.drawable as AnimatedVectorDrawableCompat)
                (imageUnify.drawable as Animatable).start()
            } catch (ignored: Throwable) {}
        }
    }

    private fun setAnimationCallback(animatedVector: AnimatedVectorDrawableCompat) {
        animatedVector.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            private val animHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                animHandler.post(animatedVector::start)
            }
        })
    }

    fun stopAVDTypingAnimation(imageUnify: ImageUnify) {
        val animationType = getAnimationType()
        if(animationType == "avd") {
            try {
                (imageUnify.drawable as AnimatedVectorDrawableCompat).clearAnimationCallbacks()
                (imageUnify.drawable as Animatable).stop()
            } catch (ignored: Throwable) {}
        }
    }

    private fun setGifTypingAnimation(imageUnify: ImageUnify) {
        if(imageUnify.context.isDarkMode()) {
            loadGif(imageUnify, TYPING_DARK_MODE)
        } else {
            loadGif(imageUnify, TYPING_LIGHT_MODE)
        }
    }

    private fun getAnimationType(): String {
        return "avd"
    }
}