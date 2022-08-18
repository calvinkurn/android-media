package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetReviewCredibilityFooterBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityFooterUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityFooterUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ReviewCredibilityFooterWidget @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityFooterBinding>(
    context, attributeSet, defStyleAttr
) {
    override val binding: WidgetReviewCredibilityFooterBinding =
        WidgetReviewCredibilityFooterBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val footerBinding by lazy(LazyThreadSafetyMode.NONE) {
        binding.layoutReviewCredibilityFooter
    }

    private val footerLoadingBinding by lazy(LazyThreadSafetyMode.NONE) {
        binding.layoutReviewCredibilityFooterLoading
    }

    private var listener: Listener? = null

    private fun hideWidget() {
        animateHide(onAnimationEnd = {
            listener?.onFooterTransitionEnd()
        })
    }

    private fun showLoading() {
        footerBinding.root.gone()
        footerLoadingBinding.root.show()
        animateShow(onAnimationEnd = {
            listener?.onFooterTransitionEnd()
        })
    }

    private fun showData(data: ReviewCredibilityFooterUiModel) {
        runTransitions(createShowDataTransition())
        showUIData(data)
        animateShow(onAnimationEnd = {
            listener?.onFooterTransitionEnd()
        })
    }

    private fun showUIData(data: ReviewCredibilityFooterUiModel) {
        setupFooter(data.description)
        setupCta(data.button)
        footerLoadingBinding.root.gone()
        footerBinding.root.show()
    }

    private fun setupFooter(description: StringRes) {
        footerBinding.tvReviewCredibilityFooterDescription.apply {
            text = context?.let {
                HtmlLinkHelper(it, description.getStringValueWithDefaultParam(context)).spannedString
            } ?: description.getStringValueWithDefaultParam(context)
            movementMethod = object : LinkMovementMethod() {
                override fun onTouchEvent(
                    widget: TextView, buffer: Spannable, event: MotionEvent
                ): Boolean {
                    val action = event.action

                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                        var x = event.x
                        var y = event.y.toInt()

                        x -= widget.totalPaddingLeft
                        y -= widget.totalPaddingTop

                        x += widget.scrollX
                        y += widget.scrollY

                        val layout = widget.layout
                        val line = layout.getLineForVertical(y)
                        val off = layout.getOffsetForHorizontal(line, x)

                        val link = buffer.getSpans(off, off, URLSpan::class.java)
                        if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                            return listener?.onClickFooterCTA(link.first().url).orFalse()
                        }
                    }
                    return super.onTouchEvent(widget, buffer, event)
                }
            }
        }
    }

    private fun setupCta(button: ReviewCredibilityFooterUiModel.Button) {
        footerBinding.btnReviewCredibilityFooterCta.apply {
            text = button.text
            setOnClickListener { listener?.onClickCTAButton(button.appLink, button.text) }
        }
    }

    private fun createShowDataTransition(): Transition {
        return Fade().setInterpolator(
            PathInterpolatorCompat.create(
                CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2
            )
        ).setDuration(ANIMATION_DURATION)
    }

    private fun runTransitions(transition: Transition) {
        TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    fun updateUiState(uiState: ReviewCredibilityFooterUiState) {
        when (uiState) {
            is ReviewCredibilityFooterUiState.Hidden -> hideWidget()
            is ReviewCredibilityFooterUiState.Loading -> showLoading()
            is ReviewCredibilityFooterUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onClickFooterCTA(appLink: String): Boolean
        fun onClickCTAButton(appLink: String, buttonText: String)
        fun onFooterTransitionEnd()
    }
}