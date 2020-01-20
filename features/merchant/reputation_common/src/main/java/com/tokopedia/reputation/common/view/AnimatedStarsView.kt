package com.tokopedia.reputation.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.reputation.common.R

class AnimatedStarsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var reverseAnimation: AnimatedVectorDrawableCompat? = null
    private var normalAnimation: AnimatedVectorDrawableCompat? = null
    private var showingNormalAnim: Boolean = false

    init {
        init()
    }

    fun init() {
        showingNormalAnim = true
        normalAnimation = AnimatedVectorDrawableCompat.create(context, R.drawable.animated_stars)
        reverseAnimation = AnimatedVectorDrawableCompat.create(context, R.drawable.animated_reverse)
        setImageDrawable(normalAnimation)
    }

    fun resetStars() {
        showingNormalAnim = true
        setImageDrawable(AnimatedVectorDrawableCompat.create(context, R.drawable.empty_star))
    }

    fun morph() {
        val drawable = if (showingNormalAnim) normalAnimation else reverseAnimation
        setImageDrawable(drawable)
        drawable?.start()
        showingNormalAnim = !showingNormalAnim
    }
}