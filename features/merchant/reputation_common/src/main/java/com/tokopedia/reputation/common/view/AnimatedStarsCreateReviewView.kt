package com.tokopedia.reputation.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.reputation.common.R

class AnimatedStarsCreateReviewView @JvmOverloads constructor(
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
        normalAnimation = AnimatedVectorDrawableCompat.create(context, R.drawable.animate_star)
        reverseAnimation = AnimatedVectorDrawableCompat.create(context, R.drawable.animate_star_reverse)
        setImageDrawable(normalAnimation)
    }

    fun resetStars() {
        showingNormalAnim = true
        setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_star_create_review))
    }

    fun morph() {
        val drawable = if (showingNormalAnim) normalAnimation else reverseAnimation
        setImageDrawable(drawable)
        drawable?.start()
        showingNormalAnim = !showingNormalAnim
    }

    fun toggleStar() {
        val drawable = if (showingNormalAnim) {
            MethodChecker.getDrawable(context, R.drawable.filled_star_create_review)
        } else {
            MethodChecker.getDrawable(context, R.drawable.empty_star_create_review)
        }
        setImageDrawable(drawable)
        showingNormalAnim = !showingNormalAnim
    }
}
