package com.tokopedia.product.detail.view.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.ActivityLifecycleCallbacksAdapter
import com.tokopedia.product.detail.databinding.AnimatedTextLabelBinding
import com.tokopedia.unifyprinciples.Typography

class AnimatedTextLabel : FrameLayout {

    companion object {
        private const val MAX_LIMIT_CHAR = 20
        private const val CHAR_TAKEN_AFTER_ELLIPSIS = 18
    }

    private var _binding: AnimatedTextLabelBinding? = null

    private val binding get() = _binding!!

    private val txtAnimatedLabel by lazy(LazyThreadSafetyMode.NONE) {
        binding.txtAnimatedLabel
    }

    private val containerAnimatedLabel by lazy(LazyThreadSafetyMode.NONE) {
        binding.root
    }

    private var animationHelper: TextLabelAnimator? = null

    private var previousText: String = ""

    private val activityLifeCycle = object: ActivityLifecycleCallbacksAdapter() {
        override fun onActivityDestroyed(activity: Activity) {
            if (context == activity) {
                animationHelper?.clear()
                animationHelper = null
                _binding = null
                unregisterActivityLifecycleCallback()
            }
        }
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        initView()
    }

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrSet,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        registerActivityLifecycleCallback()

        _binding = AnimatedTextLabelBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.animated_text_label, this)
        )

        animationHelper = TextLabelAnimator(containerAnimatedLabel, txtAnimatedLabel)
    }

    private fun registerActivityLifecycleCallback() {
        (context.applicationContext as Application)
            .registerActivityLifecycleCallbacks(activityLifeCycle)
    }

    private fun unregisterActivityLifecycleCallback() {
        (context.applicationContext as Application)
            .unregisterActivityLifecycleCallbacks(activityLifeCycle)
    }

    fun getCurrentText(): String {
        return (txtAnimatedLabel.text ?: "").toString()
    }

    fun setEmptyText() {
        txtAnimatedLabel.text = ""
    }

    fun showView(desc: String) {
        val processText = ellipsisText(desc)

        if (processText.isNotEmpty()) {
            // secondary page and next page
            if (previousText.isNotEmpty() && previousText != processText) {
                updateTextWithAnimation(processText)
            } else {
                // first page
                initialTextWithAnimation(processText)
            }
        } else {
            containerAnimatedLabel.hide()
            previousText = ""
        }
    }

    private fun initialTextWithAnimation(processText: String) {
        txtAnimatedLabel.text = processText

        animationHelper?.animateShow {
            previousText = processText

            animationHelper?.animateAutoHide {
                previousText = ""
            }
        }
    }

    private fun updateTextWithAnimation(processText: String) {
        animationHelper?.animateTextChanged(
            processText,
            onAnimationEnd = {
                animationHelper?.animateAutoHide {
                    previousText = ""
                }
            }
        )
        previousText = processText
    }

    private fun ellipsisText(desc: String): String {
        return if (desc.length > MAX_LIMIT_CHAR) {
            desc.take(CHAR_TAKEN_AFTER_ELLIPSIS) + ".."
        } else {
            desc
        }
    }
}

class TextLabelAnimator(
    private val container: FrameLayout,
    private val txtView: Typography
) {
    companion object {
        private const val IDLE_DURATION = 3000L
        private const val AFTER_SWIPE_DURATION = 100L
        private const val ALPHA_MEDIUM_DURATION = 400L
        private const val ALPHA_SHORT_DURATION = 250L
        private const val ALPHA_MIN = 0f
        private const val ALPHA_MAX = 1f
    }

    private var containerAnimator: Animator? = null
    private var textAnimator: Animator? = null

    fun animateTextChanged(
        text: String,
        onAnimationEnd: () -> Unit
    ) {
        cancelContainerAnimator()
        cancelTextAnimator()

        container.alpha = ALPHA_MAX
        txtView.text = text
        show()

        textAnimator = txtView.createAlphaAnimation(
            start = ALPHA_MIN,
            end = ALPHA_MAX,
            properties = {
                duration = ALPHA_SHORT_DURATION
                startDelay = Int.ZERO.toLong()
                interpolator = AccelerateInterpolator()
            },
            onAnimationEnd = onAnimationEnd
        )

        textAnimator?.start()
    }

    fun animateAutoHide(onAnimationEnd: (() -> Unit)? = null) {
        cancelContainerAnimator()

        containerAnimator = container.createAlphaAnimation(
            start = ALPHA_MAX,
            end = ALPHA_MIN,
            properties = {
                startDelay = IDLE_DURATION
                interpolator = AccelerateInterpolator()
            },
            onAnimationEnd = onAnimationEnd
        )

        containerAnimator?.start()
    }

    fun animateShow(onAnimationEnd: (() -> Unit)? = null) {
        cancelContainerAnimator()
        container.alpha = ALPHA_MIN
        show()

        containerAnimator = container.createAlphaAnimation(
            start = ALPHA_MIN,
            end = ALPHA_MAX,
            properties = {
                startDelay = AFTER_SWIPE_DURATION
                interpolator = DecelerateInterpolator()
            },
            onAnimationEnd = onAnimationEnd
        )

        containerAnimator?.start()
    }

    private fun View.createAlphaAnimation(
        start: Float,
        end: Float,
        properties: ValueAnimator.() -> Unit,
        onAnimationEnd: (() -> Unit)?
    ) = ValueAnimator.ofFloat(start, end).apply {
        duration = ALPHA_MEDIUM_DURATION
        properties.invoke(this)

        addUpdateListener {
            alpha = it.animatedValue as Float
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd?.invoke()
            }
        })
    }

    private fun cancelContainerAnimator() {
        containerAnimator?.removeAllListeners()
        containerAnimator?.cancel()
        containerAnimator = null
    }

    private fun cancelTextAnimator() {
        textAnimator?.removeAllListeners()
        textAnimator?.cancel()
        textAnimator = null
    }

    private fun show() {
        txtView.show()
        container.show()
    }

    fun clear() {
        cancelTextAnimator()
        cancelContainerAnimator()
    }
}
