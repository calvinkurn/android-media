package com.tokopedia.promousage.view.custom

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoCompoundViewBinding
import com.tokopedia.promousage.databinding.PromoUsageItemSubPromoInfoBinding
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemCardDetail
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.util.extension.isGreyscale
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class PromoCompoundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val ALPHA_20 = 50
        private const val ALPHA_10 = 25
        private const val ALPHA_0 = 0
    }

    private var scaleAnimator: ValueAnimator = ValueAnimator.ofFloat()
    private var overlayAnimator: ValueAnimator = ValueAnimator.ofFloat()
    private var overlayDrawable: ColorDrawable? = null
    private val longPressHandler = Handler()
    private var isLongPress = false
    private var onLongPress = Runnable {
        if (parent != null) {
            isLongPress = true
            performLongClick()
        }
    }

    private var binding: PromoUsageItemPromoCompoundViewBinding? = null

    init {
        binding = PromoUsageItemPromoCompoundViewBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(promo: PromoItem) {
        renderLoading(promo)
        renderPromoBenefitType(promo)
        renderPromoBenefitAmount(promo)
        renderPromoQuota(promo)
        renderPromoInfo(promo)
        renderPromoBackground(promo)
        renderPromoCheck(promo)
        renderExpiryInfo(promo)
        renderAdditionalInfo(promo)
        renderPromoCode(promo)
    }

    private fun renderLoading(promo: PromoItem) {
        binding?.run {
            when (promo.state) {
                is PromoItemState.Loading -> {
                    topShimmer.visible()
                    middleShimmer.visible()
                    bottomShimmer.visible()
                }

                else -> {
                    topShimmer.gone()
                    middleShimmer.gone()
                    bottomShimmer.gone()
                }
            }
        }
    }

    private fun renderPromoBenefitType(promo: PromoItem) {
        binding?.run {
            when (promo.state) {
                is PromoItemState.Selected -> {
                    val selectedResColorId = com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    tpgPromoBenefitType.setTextColorCompat(selectedResColorId)
                }

                is PromoItemState.Disabled -> {
                    val disabledResColorId = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    tpgPromoBenefitType.setTextColorCompat(disabledResColorId)
                }

                is PromoItemState.Ineligible -> {
                    val ineligibleResColorId = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    tpgPromoBenefitType.setTextColorCompat(ineligibleResColorId)
                }

                else -> {
                    val defaultTextColor = when (promo.benefitDetail.benefitType) {
                        PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
                        PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
                        PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    }
                    tpgPromoBenefitType.setTextColorCompat(defaultTextColor)
                    val cardDetail = promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_INITIAL }
                    if (cardDetail != null && cardDetail.color.isNotBlank()) {
                        val textColor = Color.parseColor(cardDetail.color)
                        tpgPromoBenefitType.setTextColor(textColor)
                    }
                }
            }
            tpgPromoBenefitType.text = promo.benefitTypeStr
            tpgPromoBenefitType.visible()
        }
    }

    private fun renderPromoBenefitAmount(promo: PromoItem) {
        binding?.run {
            val defaultTextColorResId = when (promo.state) {
                is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
                is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
            }
            tpgPromoBenefitAmount.setTextColorCompat(defaultTextColorResId)
            tpgPromoBenefitAmount.text = promo.benefitAmountStr
            tpgPromoBenefitAmount.isVisible = promo.state !is PromoItemState.Loading
        }
    }

    private fun renderPromoInfo(promo: PromoItem) {
        binding?.run {
            val promoItemInfos =
                promo.promoItemInfos.filter { it.type == PromoItemInfo.TYPE_PROMO_INFO }
            llPromoInfo.removeAllViews()
            promoItemInfos.forEach { promoInfo ->
                val promoInfoChildView = PromoUsageItemSubPromoInfoBinding
                    .inflate(LayoutInflater.from(context))
                promoInfoChildView.tpgPromoInfo.text =
                    HtmlLinkHelper(context, promoInfo.title).spannedString
                llPromoInfo.addView(promoInfoChildView.root)
            }
            llPromoInfo.isVisible = promo.state !is PromoItemState.Loading
                && llPromoInfo.childCount.isMoreThanZero()
        }
    }

    private fun renderPromoBackground(promo: PromoItem) {
        binding?.run {
            val cardDetail = when (promo.state) {
                is PromoItemState.Selected -> {
                    promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_SELECTED }
                }

                else -> {
                    promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_INITIAL }
                }
            }
            if (cardDetail != null) {
                imgPromoIcon.loadImage(cardDetail.iconUrl)
                imgPromoIcon.isGreyscale = promo.state is PromoItemState.Ineligible
                    || promo.state is PromoItemState.Disabled
                imgPromoIcon.visible()
                imgPromoBackground.loadImage(cardDetail.backgroundUrl)
                imgPromoBackground.isGreyscale = promo.state is PromoItemState.Ineligible
                    || promo.state is PromoItemState.Disabled
                imgPromoBackground.visible()
                if (promo.state is PromoItemState.Selected) {
                    cardView.updateToSelectedState()
                } else {
                    cardView.updateToNormalState()
                }
            } else {
                imgPromoIcon.gone()
                imgPromoBackground.gone()
            }
        }
    }

    private fun renderPromoCheck(promo: PromoItem) {
        binding?.run {
            imgCheckmark.isVisible = promo.state is PromoItemState.Selected
        }
    }

    private fun renderExpiryInfo(promo: PromoItem) {
        binding?.run {
            tpgPromoExpiryInfo.text = HtmlLinkHelper(context, promo.expiryInfo).spannedString
            tpgPromoExpiryInfo.visible()
        }
    }

    private fun renderAdditionalInfo(promo: PromoItem) {
        binding?.run {
            when (promo.state) {
                is PromoItemState.Ineligible -> {
                    val clashingInfo = promo.clashingInfos.firstOrNull {
                        promo.currentClashingPromoCodes.contains(it.code)
                    }
                    if (clashingInfo != null && clashingInfo.message.isNotBlank()) {
                        tpgAdditionalInfoMessage.text = clashingInfo.message
                        tpgAdditionalInfoMessage
                            .setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_YN600)
                        iuAdditionalInfoIcon.gone()
                        clAdditionalInfo.background = ContextCompat
                            .getDrawable(
                                context,
                                R.drawable.promo_usage_shape_promo_bottom_info_clashing
                            )
                        clAdditionalInfo.visible()
                    } else if (promo.message.isNotBlank()) {
                        tpgAdditionalInfoMessage.text = promo.message
                        tpgAdditionalInfoMessage
                            .setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_YN600)
                        iuAdditionalInfoIcon.gone()
                        clAdditionalInfo.background = ContextCompat
                            .getDrawable(
                                context,
                                R.drawable.promo_usage_shape_promo_bottom_info_clashing
                            )
                        clAdditionalInfo.visible()
                    } else {
                        clAdditionalInfo.gone()
                    }
                }

                is PromoItemState.Normal -> {
                    if (promo.cta.text.isNotBlank()) {
                        tpgAdditionalInfoMessage.text =
                            HtmlLinkHelper(context, promo.cta.text).spannedString
                        tpgAdditionalInfoMessage
                            .setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                        iuAdditionalInfoIcon.visible()
                        clAdditionalInfo.background = ContextCompat
                            .getDrawable(
                                context,
                                R.drawable.promo_usage_shape_promo_bottom_info_actionable
                            )
                        clAdditionalInfo.visible()
                    }
                }

                else -> {
                    clAdditionalInfo.gone()
                }
            }
        }
    }

    private fun renderPromoCode(promo: PromoItem) {
        binding?.run {
            tpgPromoCode.text = promo.code
            tpgPromoCode.isVisible = promo.isAttempted && promo.code.isNotBlank()
        }
    }

    private fun renderPromoQuota(promo: PromoItem) {
        binding?.run {
            if (promo.remainingPromoCount > 1) {
                layoutRemainingQuotaRibbon.visible()
                tpgRemainingQuota.text = context?.getString(
                    R.string.promo_voucher_placeholder_remaining_quota,
                    promo.remainingPromoCount
                )
                updatePromoBenefitTypeMargin(16.toPx())
                updateCardViewMargin(5.toPx(), 8.toPx())
            } else {
                layoutRemainingQuotaRibbon.gone()
                updatePromoBenefitTypeMargin(12.toPx())
                updateCardViewMargin(0.toPx(), 0.toPx())
            }
        }
    }

    private fun updatePromoBenefitTypeMargin(marginTop: Int) {
        binding?.run {
            val layoutParams =
                tpgPromoBenefitType.layoutParams as? ConstraintLayout.LayoutParams
            layoutParams?.setMargins(0, marginTop, 0, 0)

            tpgPromoBenefitType.layoutParams = layoutParams
            tpgPromoBenefitType.requestLayout()
        }
    }

    private fun updateCardViewMargin(marginStart: Int, marginTop: Int) {
        binding?.run {
            val layoutParams = cardView.layoutParams as? RelativeLayout.LayoutParams
            layoutParams?.setMargins(marginStart, marginTop, 0, 0)

            cardView.layoutParams = layoutParams
            cardView.requestLayout()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                longPressHandler.removeCallbacks(onLongPress)
                Handler().postDelayed(
                    {
                        animateOverlay(
                            ALPHA_20,
                            ALPHA_0, UnifyMotion.T2, UnifyMotion.EASE_OUT
                        )
                        animateScaling(0.95f, 1.01f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                        scaleAnimator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator) {

                            }

                            override fun onAnimationCancel(animator: Animator) {

                            }

                            override fun onAnimationStart(animator: Animator) {

                            }

                            override fun onAnimationEnd(animator: Animator) {
                                animateScaling(1.01f, 1f, UnifyMotion.T1, UnifyMotion.EASE_IN_OUT)
                            }
                        })

                        overlayAnimator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator) {

                            }

                            override fun onAnimationCancel(animator: Animator) {

                            }

                            override fun onAnimationStart(animator: Animator) {

                            }

                            override fun onAnimationEnd(animator: Animator) {
                                animateOverlay(
                                    ALPHA_10,
                                    ALPHA_0, UnifyMotion.T1, UnifyMotion.LINEAR
                                )
                            }
                        })
                    },
                    if (event.eventTime - event.downTime <= UnifyMotion.T1) {
                        UnifyMotion.T1 - (event.eventTime - event.downTime)
                    } else {
                        0.toLong()
                    }
                )

                if (event.action == MotionEvent.ACTION_UP) {
                    if (!isLongPress) {
                        performClick()
                    }
                    isLongPress = false
                }
                return true
            }

            MotionEvent.ACTION_DOWN -> {
                longPressHandler.postDelayed(
                    onLongPress,
                    ViewConfiguration.getLongPressTimeout().toLong()
                )
                animateOverlay(
                    ALPHA_0,
                    ALPHA_20, UnifyMotion.T1, UnifyMotion.EASE_OUT
                )
                animateScaling(1f, 0.95f, UnifyMotion.T1, UnifyMotion.EASE_OUT)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateScaling(
        start: Float,
        end: Float,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        scaleAnimator = ValueAnimator.ofFloat()
        scaleAnimator.setFloatValues(start, end)
        scaleAnimator.removeAllListeners()
        scaleAnimator.removeAllUpdateListeners()
        scaleAnimator.addUpdateListener {
            scaleX = it.animatedValue as Float
            scaleY = it.animatedValue as Float
        }
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = interpolator
        scaleAnimator.start()
    }

    private fun animateOverlay(
        start: Int,
        end: Int,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        overlayAnimator = ValueAnimator.ofFloat()
        overlayAnimator.setIntValues(start, end)
        overlayAnimator.removeAllListeners()
        overlayAnimator.removeAllUpdateListeners()
        overlayAnimator.addUpdateListener {
            overlayDrawable?.alpha = it.animatedValue as Int
        }
        overlayAnimator.duration = duration
        overlayAnimator.interpolator = interpolator
        overlayAnimator.start()
    }
}
