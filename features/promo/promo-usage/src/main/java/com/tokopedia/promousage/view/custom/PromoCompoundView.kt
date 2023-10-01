package com.tokopedia.promousage.view.custom

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
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
import com.tokopedia.kotlin.extensions.view.invisible
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
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.util.IconHelper
import com.tokopedia.promousage.util.extension.toSpannableHtmlString
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.promousage.R as promousageR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PromoCompoundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
//        private const val ALPHA_20 = 50
//        private const val ALPHA_10 = 25
//        private const val ALPHA_0 = 0

        private const val COLOR_STRING_BLUE = "blue"
        private const val COLOR_STRING_GREEN = "green"
        private const val COLOR_STRING_ORANGE = "orange"
        private const val COLOR_STRING_RED = "red"
    }

    private var scaleAnimator: ValueAnimator = ValueAnimator.ofFloat()
    private var overlayAnimator: ValueAnimator = ValueAnimator.ofFloat()

//    private var viewOverlay: ViewGroupOverlay? = null
//    private var overlayDrawable: ColorDrawable? = null
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
//        viewOverlay = this.overlay
//        overlayDrawable =
//            ColorDrawable(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN300))
//                .apply { alpha = 0 }
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
                    val selectedResColorId = unifyprinciplesR.color.Unify_GN500
                    tpgPromoBenefitType.setTextColorCompat(selectedResColorId)
                }

                is PromoItemState.Disabled -> {
                    val disabledResColorId = unifyprinciplesR.color.Unify_NN600
                    tpgPromoBenefitType.setTextColorCompat(disabledResColorId)
                }

                is PromoItemState.Ineligible -> {
                    val ineligibleResColorId = unifyprinciplesR.color.Unify_NN600
                    tpgPromoBenefitType.setTextColorCompat(ineligibleResColorId)
                }

                else -> {
                    val defaultTextColor = when (promo.benefitDetail.benefitType) {
                        PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK -> unifyprinciplesR.color.Unify_BN500
                        PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT -> unifyprinciplesR.color.Unify_YN500
                        PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING -> unifyprinciplesR.color.Unify_GN500
                        else -> unifyprinciplesR.color.Unify_NN950
                    }
                    tpgPromoBenefitType.setTextColorCompat(defaultTextColor)
                    val cardDetail = promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_INITIAL }
                    if (cardDetail != null && cardDetail.color.isNotBlank()) {
                        val textColor =
                            ContextCompat.getColor(context, getColorRes(cardDetail.color))
                        tpgPromoBenefitType.setTextColor(textColor)
                    }
                }
            }
            tpgPromoBenefitType.text = promo.benefitTypeStr
            tpgPromoBenefitType.visible()
        }
    }

    private fun getColorRes(colorString: String): Int {
        return when (colorString) {
            COLOR_STRING_BLUE -> unifyprinciplesR.color.Unify_BN500
            COLOR_STRING_ORANGE -> unifyprinciplesR.color.Unify_YN500
            COLOR_STRING_GREEN -> unifyprinciplesR.color.Unify_GN500
            COLOR_STRING_RED -> unifyprinciplesR.color.Unify_RN500
            else -> unifyprinciplesR.color.Unify_NN950
        }
    }

    private fun renderPromoBenefitAmount(promo: PromoItem) {
        binding?.run {
            val defaultTextColorResId = when (promo.state) {
                is PromoItemState.Disabled -> unifyprinciplesR.color.Unify_NN600
                is PromoItemState.Ineligible -> unifyprinciplesR.color.Unify_NN600
                else -> unifyprinciplesR.color.Unify_NN950
            }
            tpgPromoBenefitAmount.setTextColorCompat(defaultTextColorResId)
            val benefitAmountStr = if (promo.useSecondaryPromo) {
                promo.secondaryPromo.benefitAmountStr
            } else {
                promo.benefitAmountStr
            }
            tpgPromoBenefitAmount.text = benefitAmountStr.toSpannableHtmlString(context)
            if (promo.state !is PromoItemState.Loading) {
                tpgPromoBenefitAmount.visible()
            } else {
                tpgPromoBenefitAmount.invisible()
            }
        }
    }

    private fun renderPromoInfo(promo: PromoItem) {
        binding?.run {
            val promoItemInfos = when (promo.state) {
                is PromoItemState.Ineligible -> {
                    listOf(
                        PromoItemInfo(
                            type = PromoItemInfo.TYPE_PROMO_INFO,
                            icon = PromoItemInfo.ICON_NONE,
                            title = if (promo.useSecondaryPromo) {
                                promo.secondaryPromo.message
                            } else {
                                promo.message
                            }
                        )
                    ).plus(
                        if (promo.useSecondaryPromo) {
                            promo.secondaryPromo.promoItemInfos
                        } else {
                            promo.promoItemInfos
                        }.filter { it.type == PromoItemInfo.TYPE_PROMO_INFO }
                    )
                }

                else -> {
                    if (promo.useSecondaryPromo) {
                        promo.secondaryPromo.promoItemInfos
                    } else {
                        promo.promoItemInfos
                    }.filter { it.type == PromoItemInfo.TYPE_PROMO_INFO }
                }
            }
            llPromoInfo.removeAllViews()
            promoItemInfos.forEach { promoInfo ->
                val promoInfoChildView = PromoUsageItemSubPromoInfoBinding
                    .inflate(LayoutInflater.from(context))
                if (IconHelper.shouldShowIcon(promoInfo.icon)) {
                    if (IconHelper.isCustomIcon(promoInfo.icon)) {
                        promoInfoChildView.iuPromoInfo
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    IconHelper.getIcon(promoInfo.icon)
                                )
                            )
                    } else {
                        promoInfoChildView.iuPromoInfo
                            .setImage(IconHelper.getIcon(promoInfo.icon))
                    }
                } else {
                    promoInfoChildView.iuPromoInfo.gone()
                }
                promoInfoChildView.tpgPromoInfo.text =
                    promoInfo.title.toSpannableHtmlString(context)
                llPromoInfo.addView(promoInfoChildView.root)
            }
            if (promo.state !is PromoItemState.Loading &&
                llPromoInfo.childCount.isMoreThanZero()
            ) {
                llPromoInfo.visible()
            } else {
                llPromoInfo.invisible()
            }
        }
    }

    private fun renderPromoBackground(promo: PromoItem) {
        binding?.run {
            val cardDetail = when (promo.state) {
                is PromoItemState.Selected -> {
                    promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_SELECTED }
                }

                is PromoItemState.Disabled -> {
                    promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_CLASHED }
                }

                else -> {
                    promo.cardDetails
                        .firstOrNull { it.state == PromoItemCardDetail.TYPE_INITIAL }
                }
            }
            if (cardDetail != null) {
                if (cardDetail.iconUrl.isNotBlank()) {
                    imgPromoIcon.loadImage(cardDetail.iconUrl)
                    imgPromoIcon.visible()
                } else {
                    imgPromoIcon.invisible()
                }
                if (cardDetail.backgroundUrl.isNotBlank()) {
                    imgPromoBackground.loadImage(cardDetail.backgroundUrl)
                    imgPromoBackground.visible()
                } else {
                    imgPromoBackground.invisible()
                }
                if (promo.state is PromoItemState.Selected) {
                    cardView.updateToSelectedState()
                } else {
                    cardView.updateToNormalState()
                }
            } else {
                imgPromoIcon.invisible()
                imgPromoBackground.invisible()
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
            val expiryText = if (promo.isAttempted) {
                context?.getString(promousageR.string.promo_usage_promo_code_label)
            } else {
                if (promo.useSecondaryPromo) {
                    promo.secondaryPromo
                        .expiryInfo.toSpannableHtmlString(tpgPromoExpiryInfo.context)
                } else {
                    promo.expiryInfo
                        .toSpannableHtmlString(tpgPromoExpiryInfo.context)
                }
            }
            tpgPromoExpiryInfo.text = expiryText
            if (!expiryText.isNullOrBlank()) {
                tpgPromoExpiryInfo.visible()
            } else {
                tpgPromoExpiryInfo.invisible()
            }
        }
    }

    private fun renderAdditionalInfo(promo: PromoItem) {
        binding?.run {
            when (promo.state) {
                is PromoItemState.Disabled -> {
                    if (promo.state.message.isNotBlank()) {
                        tpgAdditionalInfoMessage.text = promo.state.message
                            .toSpannableHtmlString(tpgAdditionalInfoMessage.context)
                        tpgAdditionalInfoMessage
                            .setTextColorCompat(unifyprinciplesR.color.Unify_YN600)
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
                    val isPromoGopayLater = if (promo.useSecondaryPromo) {
                        promo.secondaryPromo.couponType.firstOrNull {
                            it == PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL
                        } != null
                    } else {
                        promo.couponType.firstOrNull {
                            it == PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL
                        } != null
                    }
                    val isPromoCtaRegisterGopayLater = if (promo.useSecondaryPromo) {
                        promo.secondaryPromo.cta.type == PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL
                    } else {
                        promo.cta.type == PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL
                    }
                    val isPromoCtaValid = if (promo.useSecondaryPromo) {
                        promo.secondaryPromo.cta.text.isNotBlank() &&
                            promo.secondaryPromo.cta.appLink.isNotBlank()
                    } else {
                        promo.cta.text.isNotBlank() && promo.cta.appLink.isNotBlank()
                    }
                    if (isPromoGopayLater && isPromoCtaRegisterGopayLater && isPromoCtaValid) {
                        tpgAdditionalInfoMessage.text =
                            promo.cta.text.toSpannableHtmlString(tpgAdditionalInfoMessage.context)
                        tpgAdditionalInfoMessage
                            .setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
                        iuAdditionalInfoIcon.visible()
                        clAdditionalInfo.background = ContextCompat
                            .getDrawable(
                                context,
                                R.drawable.promo_usage_shape_promo_bottom_info_actionable
                            )
                        clAdditionalInfo.visible()
                    } else {
                        clAdditionalInfo.gone()
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
            tpgPromoCode.text = if (promo.useSecondaryPromo) {
                promo.secondaryPromo.code
            } else {
                promo.code
            }
            tpgPromoCode.isVisible = promo.isAttempted && promo.code.isNotBlank()
        }
    }

    private fun renderPromoQuota(promo: PromoItem) {
        binding?.run {
            val remainingPromoCount = if (promo.useSecondaryPromo) {
                promo.secondaryPromo.remainingPromoCount
            } else {
                promo.remainingPromoCount
            }
            if (remainingPromoCount > 1) {
                layoutRemainingQuotaRibbon
                    .setBackgroundResource(R.drawable.promo_usage_ic_quota_ribbon)
                layoutRemainingQuotaRibbon.visible()
                tpgRemainingQuota.text = context?.getString(
                    R.string.promo_usage_label_remaining_quota,
                    remainingPromoCount
                )
                updatePromoBenefitTypeMargin(16.toPx())
                updateCardViewMargin(5.toPx(), 8.toPx())
                updateAdditionalInfoMargin(5.toPx(), (-4).toPx())
            } else {
                layoutRemainingQuotaRibbon.gone()
                updatePromoBenefitTypeMargin(12.toPx())
                updateCardViewMargin(0.toPx(), 0.toPx())
                updateAdditionalInfoMargin(0.toPx(), (-4).toPx())
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

    private fun updateAdditionalInfoMargin(marginStart: Int, marginTop: Int) {
        binding?.run {
            val layoutParams = clAdditionalInfo.layoutParams as? RelativeLayout.LayoutParams
            layoutParams?.setMargins(marginStart, marginTop, 0, 0)

            clAdditionalInfo.layoutParams = layoutParams
            clAdditionalInfo.requestLayout()
        }
    }

//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        setupOverlay(w, h)
//    }

//    private fun setupOverlay(width: Int, height: Int) {
//        overlayDrawable?.bounds = Rect(0, 0, width, height)
//        viewOverlay?.clear()
//        overlayDrawable?.let {
//            viewOverlay?.add(it)
//        }
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                longPressHandler.removeCallbacks(onLongPress)
                Handler().postDelayed(
                    {
//                        animateOverlay(
//                            ALPHA_20,
//                            ALPHA_0, UnifyMotion.T2, UnifyMotion.EASE_OUT
//                        )
                        animateScaling(0.95f, 1.01f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                        scaleAnimator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationCancel(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationStart(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationEnd(animator: Animator) {
                                animateScaling(1.01f, 1f, UnifyMotion.T1, UnifyMotion.EASE_IN_OUT)
                            }
                        })

                        overlayAnimator.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationCancel(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationStart(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationEnd(animator: Animator) {
//                                animateOverlay(
//                                    ALPHA_10,
//                                    ALPHA_0, UnifyMotion.T1, UnifyMotion.LINEAR
//                                )
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
//                animateOverlay(
//                    ALPHA_0,
//                    ALPHA_20, UnifyMotion.T1, UnifyMotion.EASE_OUT
//                )
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

//    private fun animateOverlay(
//        start: Int,
//        end: Int,
//        duration: Long,
//        interpolator: TimeInterpolator
//    ) {
//        overlayAnimator = ValueAnimator.ofFloat()
//        overlayAnimator.setIntValues(start, end)
//        overlayAnimator.removeAllListeners()
//        overlayAnimator.removeAllUpdateListeners()
//        overlayAnimator.addUpdateListener {
//            overlayDrawable?.alpha = it.animatedValue as Int
//        }
//        overlayAnimator.duration = duration
//        overlayAnimator.interpolator = interpolator
//        overlayAnimator.start()
//    }
}
