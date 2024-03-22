package com.tokopedia.buyerorderdetail.presentation.partialview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper

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
        WidgetBrcCsatContentBinding.inflate(LayoutInflater.from(context), null, false)
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
            is WidgetBrcCsatUiState.Hidden -> onHidden()
            is WidgetBrcCsatUiState.Loading -> onLoading()
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

    private fun onLoading() {
        animateHide()
    }

    private fun WidgetBrcCsatContentBinding.setup(orderID: String, helpUrl: String) {
        setupSmileys(orderID)
        setupHelpText(helpUrl)
    }

    private fun WidgetBrcCsatContentBinding.setupSmileys(orderID: String) {
        ivBomBrcCsatSmiley1.setOnClickListener { onSmileyClicked(orderID, 1) }
        ivBomBrcCsatSmiley2.setOnClickListener { onSmileyClicked(orderID, 2) }
        ivBomBrcCsatSmiley3.setOnClickListener { onSmileyClicked(orderID, 3) }
        ivBomBrcCsatSmiley4.setOnClickListener { onSmileyClicked(orderID, 4) }
        ivBomBrcCsatSmiley5.setOnClickListener { onSmileyClicked(orderID, 5) }
    }

    private fun onSmileyClicked(orderID: String, feedback: Int) {
        _navigator?.goToBrcCsatForm(orderID, feedback)
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
}
