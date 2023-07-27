package com.tokopedia.promousage.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.OffsetEdgeTreatment
import com.google.android.material.shape.ShapeAppearanceModel
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
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

class VoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: PromoUsageVoucherViewBinding? = null

    init {
        binding = PromoUsageVoucherViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.cardView?.cardElevation = 4f
        binding?.cardView?.elevation = 4f
    }

    fun bind(promo: PromoItem) {
        binding?.run {
            val promoItemInfos = promo.promoItemInfos.filter { it.type == PromoItemInfo.TYPE_PROMO_INFO }
            if (promoItemInfos.size == 1) {
                tpgPromoInfo.text = promoItemInfos.first().title
            } else {
                // TODO: Handle multiple promo info
            }

            val cardDetail = promo.cardDetails[0]
            imgPromoIcon.loadImage(cardDetail.iconUrl)
            imgPromoBackground.loadImage(cardDetail.backgroundUrl)

            if (promo.state is PromoItemState.Ineligible || promo.state is PromoItemState.Disabled) {
                imgPromoBackground.grayscale()
                imgPromoIcon.grayscale()
            } else {
                imgPromoBackground.removeGrayscale()
                imgPromoIcon.removeGrayscale()
            }

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

            when (promo.benefitDetail.benefitType) {
                PromoItemBenefitDetail.BENEFIT_TYPE_CASHBACK -> handleCashback(promo)
                PromoItemBenefitDetail.BENEFIT_TYPE_DISCOUNT -> handleDiscount(promo)
                PromoItemBenefitDetail.BENEFIT_TYPE_FREE_SHIPPING -> handleFreeShipping(promo)
            }

            tpgPromoCode.text = if (promo.isAttempted) promo.code else ""
            tpgPromoCode.isVisible = promo.isAttempted

            if (promo.state is PromoItemState.Ineligible) {
                val clashingInfo = promo.clashingInfos.firstOrNull {
                    promo.currentClashingPromoCodes.contains(it.code)
                }
                clashingInfo?.let {
                    tpgIneligibleReason.text = it.message
                    tpgIneligibleReason.isVisible = true
                }
            } else {
                tpgIneligibleReason.isVisible = false
            }

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

    private fun handleCashback(promo: PromoItem) {
        val voucherTypeTextColorResId = when (promo.state) {
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
        val voucherTypeTextColorResId = when (promo.state) {
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
        val voucherTypeTextColorResId = when (promo.state) {
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

    private fun TextView.setVoucherAmountTextColor(promo: PromoItem) {
        val voucherAmountTextColorResId = when (promo.state) {
            is PromoItemState.Disabled -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            is PromoItemState.Ineligible -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
        }
        setTextColorCompat(voucherAmountTextColorResId)
    }

    private fun createVoucherShape(cardViewHeightPx: Int, isVoucherSelected: Boolean): Drawable {
        val cardCornerRadius = 8.toPx().toFloat()
        val notchRadius = 18

        val stokeWidth = 1

        val distance = cardViewHeightPx - 48.toPx()
        val offsetY = distance / 4

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
}
