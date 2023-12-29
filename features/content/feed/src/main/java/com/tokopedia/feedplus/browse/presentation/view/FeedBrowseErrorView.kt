package com.tokopedia.feedplus.browse.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import com.tokopedia.feedplus.databinding.ViewFeedBrowseCardErrorBinding
import com.tokopedia.unifyprinciples.UnifyMotion

/**
 * Created by meyta.taliti on 31/08/23.
 */
class FeedBrowseErrorView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewFeedBrowseCardErrorBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val rotateAnimation by lazy {
        RotateAnimation(
            0f, 359f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            interpolator = UnifyMotion.LINEAR
            repeatCount = Animation.INFINITE
        }
    }

    fun startAnimating() {
        binding.btnRetry1.startAnimation(rotateAnimation)
        binding.btnRetry2.startAnimation(rotateAnimation)
        binding.btnRetry3.startAnimation(rotateAnimation)
    }

    fun stop() {
        binding.btnRetry1.clearAnimation()
        binding.btnRetry2.clearAnimation()
        binding.btnRetry3.clearAnimation()
    }
}
