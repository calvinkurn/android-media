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
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageVoucherViewBinding
import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.PromoState
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
        binding = PromoUsageVoucherViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        binding?.cardView?.cardElevation = 4f
        binding?.cardView?.elevation = 4f
    }

    fun bind(promo: Promo) {
        binding?.run {
            // TODO: Get from Promo.promoInfos[] with TYPE_PROMO_INFO
            tpgVoucherTnc.text = promo.promoInfos[0].title

            val cardDetail = promo.cardDetails[0]
            imgVoucherIcon.loadImage(cardDetail.iconUrl)
            imgSupergraphic.loadImage(cardDetail.backgroundUrl)

            if (promo.state.isIneligible() || promo.state.isDisabled()) {
                imgSupergraphic.grayscale()
                imgVoucherIcon.grayscale()
            } else {
                imgSupergraphic.removeGrayscale()
                imgVoucherIcon.removeGrayscale()
            }

            imgCheckmark.isVisible = promo.state.isSelected()

            // TODO: Get from Promo.expiryInfo
            if (promo.expiryInfo.isNotBlank()) {
                tpgVoucherExpiredDate.text = HtmlLinkHelper(context, promo.expiryInfo).spannedString
                tpgVoucherExpiredDate.background = if (promo.state.isSelected()) {
                    ContextCompat.getDrawable(
                        tpgVoucherExpiredDate.context,
                        R.drawable.promo_usage_shape_voucher_selected
                    )
                } else {
                    ContextCompat.getDrawable(
                        tpgVoucherExpiredDate.context,
                        R.drawable.promo_usage_shape_voucher_expired_date
                    )
                }
                tpgVoucherExpiredDate.isVisible = true
            } else {
                tpgVoucherExpiredDate.isVisible = false
            }

            when(promo.benefitType) {
                Promo.BENEFIT_TYPE_CASHBACK -> handleCashback(promo)
                Promo.BENEFIT_TYPE_DISCOUNT -> handleDiscount(promo)
                Promo.BENEFIT_TYPE_FREE_SHIPPING -> handleFreeShipping(promo)
            }

            tpgVoucherCode.text = if (promo.isAttempted) promo.code else ""
            tpgVoucherCode.isVisible = promo.isAttempted

            if (promo.state.isIneligible()) {
                val clashingInfo = promo.clashingInfos.firstOrNull {
                    promo.currentClashingPromoCode.contains(it.code)
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
                    isVoucherSelected = promo.state.isSelected()
                )
            }

            if (promo.state is PromoState.Loading) {
                tpgVoucherAmount.gone()
                tpgVoucherTnc.gone()
                topShimmer.visible()
                middleShimmer.visible()
                bottomShimmer.visible()
            } else {
                tpgVoucherAmount.visible()
                tpgVoucherTnc.visible()
                topShimmer.gone()
                middleShimmer.gone()
                bottomShimmer.gone()
            }

        }
    }


    private fun handleCashback(promo: Promo) {
        val voucherTypeTextColorResId = when {
            promo.state.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            promo.state.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            promo.state.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_cashback)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_gopay_coins, promo.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(promo)
        }
    }
    private fun handleFreeShipping(promo: Promo) {
        val voucherTypeTextColorResId = when {
            promo.state.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            promo.state.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            promo.state.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_free_shipping)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_voucher_amount, promo.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(promo)
        }
    }
    private fun handleDiscount(promo: Promo) {
        val voucherTypeTextColorResId = when {
            promo.state.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            promo.state.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            promo.state.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_discount)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_voucher_amount, promo.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(promo)
        }

    }


    private fun TextView.setVoucherAmountTextColor(promo: Promo) {
        val voucherAmountTextColorResId = when {
            promo.state.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            promo.state.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
        }

        setTextColorCompat(voucherAmountTextColorResId)
    }

    private fun PromoState.isSelected(): Boolean {
        return this == PromoState.Selected
    }

    private fun PromoState.isDisabled(): Boolean {
        return this == PromoState.Disabled
    }

    private fun PromoState.isIneligible(): Boolean {
        return this is PromoState.Ineligible
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
