package com.tokopedia.buyerorderdetail.presentation.partialview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.WidgetBrcCsatBinding
import com.tokopedia.buyerorderdetail.databinding.WidgetBrcCsatContentBinding
import com.tokopedia.buyerorderdetail.presentation.model.WidgetBrcCsatUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.WidgetBrcCsatUiState
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.UnifyMotion

class WidgetBrcCsat(
    context: Context,
    attrs: AttributeSet?
) : BaseCustomView(context, attrs) {

    companion object {
        const val ANIMATION_DURATION = 300L
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
        private const val HEIGHT_HIDE = 0
        private const val ALPHA_SHOW = 1f
        private const val ALPHA_HIDE = 0f
    }

    private val binding = WidgetBrcCsatBinding.inflate(LayoutInflater.from(context), this, true)
    private val contentBinding by lazyThreadSafetyNone {
        WidgetBrcCsatContentBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        ).apply { setupBackgroundColor() }
    }
    private val contentData by lazyThreadSafetyNone {
        AccordionDataUnify(
            title = context.getString(R.string.bom_brc_csat_title),
            expandableView = contentBinding.root,
            isExpanded = true
        ).apply { setContentPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO) }
    }

    private var _navigator: BuyerOrderDetailNavigator? = null
    private var animator: AnimatorSet? = null

    fun setup(navigator: BuyerOrderDetailNavigator) {
        _navigator = navigator
    }

    fun setup(uiState: WidgetBrcCsatUiState) {
        when (uiState) {
            is WidgetBrcCsatUiState.HasData.Reloading -> onReloading(uiState.data)
            is WidgetBrcCsatUiState.HasData.Showing -> onShowing(uiState.data)
            is WidgetBrcCsatUiState.NoData -> onHidden()
        }
    }

    private fun onReloading(data: WidgetBrcCsatUiModel) {
        with(binding.accordionBomBrcCsat) {
            contentBinding.setup(data.orderID, data.helpUrl)
            onItemClick = { _, expanded -> data.expanded = expanded }
            if (!accordionData.contains(contentData)) addGroup(contentData)
            if (data.expanded) expandAllGroup() else collapseAllGroup()
        }
        animateShow()
    }

    private fun onShowing(data: WidgetBrcCsatUiModel) {
        with(binding.accordionBomBrcCsat) {
            contentBinding.setup(data.orderID, data.helpUrl)
            onItemClick = { _, expanded -> data.expanded = expanded }
            if (!accordionData.contains(contentData)) addGroup(contentData)
            if (data.expanded) expandAllGroup() else collapseAllGroup()
        }
        animateShow()
    }

    private fun onHidden() {
        animateHide()
    }

    private fun WidgetBrcCsatContentBinding.setup(orderID: String, helpUrl: String) {
        setupSmileys(orderID)
        setupHelpText(helpUrl)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun WidgetBrcCsatContentBinding.setupSmileys(orderID: String) {
        cardBomBrcCsatSmiley1.setOnTouchListener(
            SmileyTouchListener(
                orderID = orderID,
                smileyScore = 1,
                cardBomBrcCsatSmiley = cardBomBrcCsatSmiley1,
                ivBomBrcCsatSmiley = ivBomBrcCsatSmiley1,
                navigator = _navigator
            )
        )
        cardBomBrcCsatSmiley2.setOnTouchListener(
            SmileyTouchListener(
                orderID = orderID,
                smileyScore = 2,
                cardBomBrcCsatSmiley = cardBomBrcCsatSmiley2,
                ivBomBrcCsatSmiley = ivBomBrcCsatSmiley2,
                navigator = _navigator
            )
        )
        cardBomBrcCsatSmiley3.setOnTouchListener(
            SmileyTouchListener(
                orderID = orderID,
                smileyScore = 3,
                cardBomBrcCsatSmiley = cardBomBrcCsatSmiley3,
                ivBomBrcCsatSmiley = ivBomBrcCsatSmiley3,
                navigator = _navigator
            )
        )
        cardBomBrcCsatSmiley4.setOnTouchListener(
            SmileyTouchListener(
                orderID = orderID,
                smileyScore = 4,
                cardBomBrcCsatSmiley = cardBomBrcCsatSmiley4,
                ivBomBrcCsatSmiley = ivBomBrcCsatSmiley4,
                navigator = _navigator
            )
        )
        cardBomBrcCsatSmiley5.setOnTouchListener(
            SmileyTouchListener(
                orderID = orderID,
                smileyScore = 5,
                cardBomBrcCsatSmiley = cardBomBrcCsatSmiley5,
                ivBomBrcCsatSmiley = ivBomBrcCsatSmiley5,
                navigator = _navigator
            )
        )
    }

    private fun WidgetBrcCsatContentBinding.setupHelpText(helpUrl: String) {
        tvBomBrcCsatHelp.movementMethod = LinkMovementMethod.getInstance()
        tvBomBrcCsatHelp.text = HtmlLinkHelper(
            context, context.getString(R.string.bom_brc_csat_help, helpUrl)
        ).apply {
            urlList.forEach { it.onClick = { _navigator?.openAppLink(it.linkUrl, false) } }
        }.spannedString ?: String.EMPTY
    }

    private fun calculateWrapHeight(): Int {
        return runCatching {
            val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
            val widthMeasureSpec = (binding.root.parent as? View)?.let { parent ->
                MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY)
            } ?: wrapContentMeasureSpec
            binding.root.measure(widthMeasureSpec, wrapContentMeasureSpec)
            binding.root.measuredHeight
        }.getOrNull().orZero()
    }

    private fun animateHeight(targetHeight: Int, targetAlpha: Float, onAnimateEnd: (Animator) -> Unit = {}) {
        animator?.cancel()
        animator = AnimatorSet().apply {
            duration = ANIMATION_DURATION
            interpolator = PathInterpolatorCompat.create(CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2)
            addListener(onEnd = onAnimateEnd)
            playTogether(createHeightAnimator(targetHeight), createAlphaAnimator(targetAlpha))
            start()
        }
    }

    private fun createHeightAnimator(targetHeight: Int) = ValueAnimator.ofInt(binding.root.height, targetHeight).apply {
        addUpdateListener { newValue -> updateHeight((newValue.animatedValue as Int)) }
    }

    private fun createAlphaAnimator(targetAlpha: Float) = ValueAnimator.ofFloat(binding.root.alpha, targetAlpha).apply {
        addUpdateListener { newValue -> updateAlpha((newValue.animatedValue as Float)) }
    }

    private fun updateHeight(height: Int) {
        val layoutParamsCopy = binding.root.layoutParams
        layoutParamsCopy.height = height
        binding.root.layoutParams = layoutParamsCopy
    }

    private fun updateAlpha(alpha: Float) {
        binding.root.alpha = alpha
    }

    private fun animateShow() {
        animateHeight(calculateWrapHeight(), ALPHA_SHOW) { updateHeight(ViewGroup.LayoutParams.WRAP_CONTENT) }
    }

    private fun animateHide() {
        animateHeight(HEIGHT_HIDE, ALPHA_HIDE)
    }

    private fun WidgetBrcCsatContentBinding.setupBackgroundColor() {
        cardBomBrcCsatSmiley1.setCardUnifyBackgroundColor(Color.TRANSPARENT)
        cardBomBrcCsatSmiley2.setCardUnifyBackgroundColor(Color.TRANSPARENT)
        cardBomBrcCsatSmiley3.setCardUnifyBackgroundColor(Color.TRANSPARENT)
        cardBomBrcCsatSmiley4.setCardUnifyBackgroundColor(Color.TRANSPARENT)
        cardBomBrcCsatSmiley5.setCardUnifyBackgroundColor(Color.TRANSPARENT)
    }

    private class SmileyTouchListener(
        private val orderID: String,
        private val smileyScore: Int,
        private val cardBomBrcCsatSmiley: CardUnify2,
        private val ivBomBrcCsatSmiley: ImageUnify,
        private val navigator: BuyerOrderDetailNavigator?
    ): OnTouchListener {

        companion object {
            private const val CLICKED_ANIMATION_DURATION = 50L
        }

        private var colorChangeAnimation: ValueAnimator? = null

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cardBomBrcCsatSmiley.foreground.setState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled))
                    animateSmileyColor(R.color.buyer_order_detail_dms_brc_csat_smiley_color_click, CLICKED_ANIMATION_DURATION)
                }
                MotionEvent.ACTION_UP -> {
                    cardBomBrcCsatSmiley.foreground.setState(intArrayOf())
                    animateSmileyColor(R.color.buyer_order_detail_dms_brc_csat_smiley_color_default, UnifyMotion.T1) {
                        if (event.intersectWith(cardBomBrcCsatSmiley, Long.ZERO)) {
                            onSmileyClicked(orderID, smileyScore)
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    cardBomBrcCsatSmiley.foreground.setState(intArrayOf())
                    animateSmileyColor(R.color.buyer_order_detail_dms_brc_csat_smiley_color_default, UnifyMotion.T1)
                }
            }
            return true
        }

        private fun animateSmileyColor(@ColorRes color: Int, duration: Long, onAnimateEnd: (Animator) -> Unit = {}) {
            with(ivBomBrcCsatSmiley) {
                colorChangeAnimation?.cancel()
                val defaultCurrentColor = if (color == R.color.buyer_order_detail_dms_brc_csat_smiley_color_default) {
                    ContextCompat.getColor(context, R.color.buyer_order_detail_dms_brc_csat_smiley_color_click)
                } else {
                    ContextCompat.getColor(context, R.color.buyer_order_detail_dms_brc_csat_smiley_color_default)
                }
                val currentColor = colorChangeAnimation?.animatedValue as? Int ?: defaultCurrentColor
                val targetColor = ContextCompat.getColor(context, color)
                colorChangeAnimation = ValueAnimator
                    .ofArgb(currentColor, targetColor)
                    .apply {
                        interpolator = UnifyMotion.EASE_IN_OUT
                        this.duration = duration
                        addUpdateListener { setColorFilter(it.animatedValue as Int) }
                        addListener(onEnd = onAnimateEnd)
                        start()
                    }
            }
        }

        private fun onSmileyClicked(orderID: String, feedback: Int) {
            navigator?.goToBrcCsatForm(orderID, feedback)
        }

        private fun MotionEvent.intersectWith(view: View, extraSizePx: Long): Boolean {
            return if (view.isVisible && view.height.isMoreThanZero() && view.width.isMoreThanZero()) {
                val viewCoordinate = IntArray(2).also {
                    view.getLocationOnScreen(it)
                }
                val viewStartX = (viewCoordinate.first() - extraSizePx).coerceAtLeast(Long.ZERO)
                val viewEndX = viewCoordinate.first() + view.width + extraSizePx
                val viewStartY = (viewCoordinate.last() - extraSizePx).coerceAtLeast(Long.ZERO)
                val viewEndY = viewCoordinate.last() + view.height + extraSizePx
                rawX >= viewStartX && rawX <= viewEndX && rawY >= viewStartY && rawY <= viewEndY
            } else false
        }
    }
}
