package com.tokopedia.promousage.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.OffsetEdgeTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageVoucherViewBinding
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.PromoItemBenefitDetail
import com.tokopedia.promousage.domain.entity.PromoItemInfo
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.util.extension.grayscale
import com.tokopedia.promousage.util.extension.onDrawn
import com.tokopedia.promousage.util.extension.removeGrayscale
import com.tokopedia.promousage.util.extension.setHyperlinkText
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

class VoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val CIRCLE_COT_OUT_MARGIN_BOTTOM_DP = 58
    }

    private var binding: PromoUsageVoucherViewBinding? = null
    private var onHyperlinkTextClick : (String) -> Unit = {}

    init {
        binding = PromoUsageVoucherViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.cardView?.cardElevation = 4f
        binding?.cardView?.elevation = 4f
    }

    fun bind(promo: PromoItem) {
        binding?.run {
            handlePromoType(promo)
            handleVoucherState(promo)
            handleVoucherSource(promo)
            handleVoucherQuota(promo.remainingPromoCount)

            val promoItemInfos = promo.promoItemInfos.filter { it.type == PromoItemInfo.TYPE_PROMO_INFO }
            if (promoItemInfos.size == 1) {
                tpgPromoInfo.text = promoItemInfos.first().title
            } else {
                // TODO: Handle multiple promo info
            }

            val cardDetail = promo.cardDetails[0]
            imgPromoIcon.loadImage(cardDetail.iconUrl)
            imgPromoBackground.loadImage(cardDetail.backgroundUrl)

            imgCheckmark.isVisible = promo.state is PromoItemState.Selected

            if (promo.expiryInfo.isNotBlank()) {
                tpgPromoExpiryInfo.text = HtmlLinkHelper(context, promo.expiryInfo).spannedString
                tpgPromoExpiryInfo.background = if (promo.state is PromoItemState.Selected) {
                    ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_selected
                    )
                } else {
                    ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }
                tpgPromoExpiryInfo.isVisible = true
            } else {
                tpgPromoExpiryInfo.isVisible = false
            }

            if (promo.state is PromoItemState.Ineligible) {
                val clashingInfo = promo.clashingInfos.firstOrNull {
                    promo.currentClashingPromoCodes.contains(it.code)
                }
                clashingInfo?.let {
                    binding?.tpgPromoInfo?.text = it.message
                    binding?.tpgPromoInfo?.isVisible = true
                }
            } else {
                binding?.tpgPromoInfo?.isVisible = false
            }

            tpgPromoExpiryInfo.text = MethodChecker.fromHtml(
                context?.getString(
                    R.string.promo_voucher_placeholder_expired_date,
                    promo.expiryInfo
                )
            )

            cardView.onDrawn { cardViewHeightPx ->
                cardView.background = createVoucherShape(
                    cardViewHeightPx = cardViewHeightPx,
                    isVoucherSelected = promo.state is PromoItemState.Selected
                )
            }

            if (promo.state is PromoItemState.Loading) {
                tpgPromoBenefitAmount.gone()
                tpgPromoInfo.gone()
                topShimmer.visible()
                middleShimmer.visible()
                bottomShimmer.visible()
            } else {
                tpgPromoBenefitAmount.visible()
                tpgPromoInfo.visible()
                topShimmer.gone()
                middleShimmer.gone()
                bottomShimmer.gone()
            }
        }
    }

    private fun handlePromoType(promo: PromoItem) {
        when (promo.benefitDetail.benefitType) {
            PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK -> handleCashback(promo)
            PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING -> handleFreeShipping(promo)
            PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT -> handleDiscount(promo)
        }
    }

    private fun handleCashback(promo: PromoItem) {
        val voucherTypeTextColorResId = when(promo.state) {
            is PromoItemState.Selected -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
        }

        binding?.run {
            tpgPromoBenefitType.text = promo.benefitTypeStr
            tpgPromoBenefitType.setTextColorCompat(voucherTypeTextColorResId)
            tpgPromoBenefitAmount.text = promo.benefitAmountStr
            tpgPromoBenefitAmount.setVoucherAmountTextColor(promo)
        }
    }

    private fun handleFreeShipping(promo: PromoItem) {
        val voucherTypeTextColorResId = when(promo.state) {
            is PromoItemState.Selected -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
        }

        binding?.run {
            tpgPromoBenefitType.text = promo.benefitTypeStr
            tpgPromoBenefitType.setTextColorCompat(voucherTypeTextColorResId)
            tpgPromoBenefitAmount.text = promo.benefitAmountStr
            tpgPromoBenefitAmount.setVoucherAmountTextColor(promo)
        }
    }

    private fun handleDiscount(promo: PromoItem) {
        val voucherTypeTextColorResId = when(promo.state) {
            is PromoItemState.Selected -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
        }

        binding?.run {
            tpgPromoBenefitType.text = promo.benefitTypeStr
            tpgPromoBenefitType.setTextColorCompat(voucherTypeTextColorResId)
            tpgPromoBenefitAmount.text = promo.benefitAmountStr
            tpgPromoBenefitAmount.setVoucherAmountTextColor(promo)
        }
    }

    private fun handleVoucherQuota(remainingQuota: Int) {
        if (remainingQuota > 1) {
            binding?.layoutRemainingQuotaRibbon?.visible()
            binding?.tpgRemainingQuota?.text = context?.getString(
                R.string.promo_voucher_placeholder_remaining_quota,
                remainingQuota
            )

            updateVoucherTypeMarginTop(16.toPx())
            updateCardViewMargin(4.toPx(), 8.toPx())
        } else {
            binding?.layoutRemainingQuotaRibbon?.gone()

            updateVoucherTypeMarginTop(12.toPx())
            updateCardViewMargin(0.toPx(), 0.toPx())
        }
    }

    private fun handleVoucherSource(promo: PromoItem) {
        binding?.run {
            tpgPromoCode.text = if (promo.isAttempted) promo.code else ""
            tpgPromoCode.isVisible = promo.isAttempted
        }
    }

    private fun handleVoucherState(promo: PromoItem) {
        binding?.run {
            when (promo.state) {
                is PromoItemState.Loading -> {
                    imgCheckmark.gone()
                    imgPromoBackground.removeGrayscale()
                    imgPromoIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    showLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }

                is PromoItemState.Normal -> {
                    imgCheckmark.gone()
                    imgPromoBackground.removeGrayscale()
                    imgPromoIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }

                is PromoItemState.Selected -> {
                    imgCheckmark.visible()
                    imgPromoBackground.removeGrayscale()
                    imgPromoIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_selected
                    )
                }

                is PromoItemState.Disabled -> {
                    imgCheckmark.gone()
                    imgPromoBackground.grayscale()
                    imgPromoIcon.grayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }
                
                is PromoItemState.Ineligible -> {
                    imgCheckmark.gone()
                    imgPromoBackground.grayscale()
                    imgPromoIcon.grayscale()

                    tpgAdditionalInformation.text = promo.message
                    tpgAdditionalInformation.visible()
                    layoutActionable.gone()

                    hideLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }
                
                is PromoItemState.Actionable -> {
                    imgCheckmark.gone()
                    imgPromoBackground.removeGrayscale()
                    imgPromoIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.visible()
                    tpgActionableText.text = HtmlLinkHelper(context, promo.state.cta.text).spannedString

                    hideLoadingAppearance()

                    tpgPromoExpiryInfo.background = ContextCompat.getDrawable(
                        tpgPromoExpiryInfo.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }
            }
        }
    }

    private fun TextView.setVoucherAmountTextColor(promo: PromoItem) {
        val voucherAmountTextColorResId = when (promo.state) {
            is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
        }
        setTextColorCompat(voucherAmountTextColorResId)
    }

    private fun showLoadingAppearance() {
        binding?.run {
            tpgPromoBenefitAmount.gone()
            tpgPromoExpiryInfo.gone()
            topShimmer.visible()
            middleShimmer.visible()
            bottomShimmer.visible()
        }

    }

    private fun hideLoadingAppearance() {
        binding?.run {
            tpgPromoBenefitAmount.visible()
            tpgPromoExpiryInfo.visible()
            topShimmer.gone()
            middleShimmer.gone()
            bottomShimmer.gone()
        }
    }

    private fun createVoucherShape(cardViewHeightPx: Int, isVoucherSelected: Boolean): Drawable {
        val cardCornerRadius = 8.toPx().toFloat()
        val notchRadius = 18
        val stokeWidth = 1

        val distanceFromBottom = cardViewHeightPx - CIRCLE_COT_OUT_MARGIN_BOTTOM_DP.toPx()
        val offsetY = distanceFromBottom / 4

        val roundTreatment = BottomAppBarTopEdgeTreatment(0f, 0f, 0f)
        try {
            roundTreatment.fabDiameter = notchRadius * 2f
        } catch (_: Exception) {

        }

        val voucherShapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setLeftEdge(OffsetEdgeTreatment(roundTreatment, offsetY.toFloat()))
            .setRightEdge(OffsetEdgeTreatment(roundTreatment, -1 * offsetY.toFloat()))
            .setAllCornerSizes(cardCornerRadius)
            .build()

        val materialShapeDrawable = MaterialShapeDrawable(voucherShapeAppearanceModel).apply {
            shapeAppearanceModel = voucherShapeAppearanceModel

            strokeWidth = stokeWidth.toPx().toFloat()

            val backgroundColor = if (isVoucherSelected) {
                ContextCompat.getColor(
                    context ?: return@apply,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN50
                )
            } else {
                ContextCompat.getColor(
                    context ?: return@apply,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            }
            setTint(backgroundColor)

            val shapeStrokeColor = if (isVoucherSelected) {
                ContextCompat.getColor(
                    context ?: return@apply,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            } else {
                ContextCompat.getColor(
                    context ?: return@apply,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN200
                )
            }
            strokeColor = ColorStateList.valueOf(shapeStrokeColor)
        }

        return materialShapeDrawable
    }

    fun setOnHyperlinkTextClick(onHyperlinkTextClick : (String) -> Unit) {
        this.onHyperlinkTextClick = onHyperlinkTextClick
    }

    private fun updateVoucherTypeMarginTop(marginTop: Int) {
        val layoutParams = binding?.tpgPromoBenefitType?.layoutParams as? ConstraintLayout.LayoutParams
        layoutParams?.setMargins(0, marginTop, 0, 0)

        binding?.tpgPromoBenefitType?.layoutParams = layoutParams
        binding?.tpgPromoBenefitType?.requestLayout()
    }

    private fun updateCardViewMargin(marginStart: Int, marginTop: Int) {
        val layoutParams = binding?.cardView?.layoutParams as? RelativeLayout.LayoutParams
        layoutParams?.setMargins(marginStart, marginTop, 0, 0)

        binding?.cardView?.layoutParams = layoutParams
        binding?.cardView?.requestLayout()
    }
}
