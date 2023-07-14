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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageVoucherViewBinding
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.VoucherSource
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.domain.entity.VoucherType
import com.tokopedia.promousage.util.extension.grayscale
import com.tokopedia.promousage.util.extension.onDrawn
import com.tokopedia.promousage.util.extension.removeGrayscale
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

    fun bind(voucher: Voucher) {
        binding?.run {
            tpgVoucherTnc.text = voucher.termAndCondition

            tpgVoucherExpiredDate.text = context?.getString(R.string.promo_voucher_placeholder_expired_date, voucher.expiredDate)

            imgSupergraphic.loadImage(voucher.superGraphicImageUrl)
            imgVoucherIcon.loadImage(voucher.iconImageUrl)

            if (voucher.voucherState.isIneligible() || voucher.voucherState.isDisabled()) {
                imgSupergraphic.grayscale()
                imgVoucherIcon.grayscale()
            } else {
                imgSupergraphic.removeGrayscale()
                imgVoucherIcon.removeGrayscale()
            }

            imgCheckmark.isVisible = voucher.voucherState.isSelected()

            tpgVoucherExpiredDate.text = MethodChecker.fromHtml(context?.getString(R.string.promo_voucher_placeholder_expired_date, voucher.expiredDate))
            tpgVoucherExpiredDate.background = if (voucher.voucherState.isSelected()) {
                ContextCompat.getDrawable(tpgVoucherExpiredDate.context, R.drawable.promo_usage_shape_voucher_selected)
            } else {
                ContextCompat.getDrawable(tpgVoucherExpiredDate.context, R.drawable.promo_usage_shape_voucher_expired_date)
            }

            when(voucher.voucherType) {
                VoucherType.CASHBACK -> handleCashback(voucher)
                VoucherType.FREE_SHIPPING -> handleFreeShipping(voucher)
                VoucherType.DISCOUNT -> handleDiscount(voucher)
            }

            tpgVoucherCode.text = voucher.voucherSource.voucherCodeOrEmpty()
            tpgVoucherCode.isVisible = voucher.voucherSource is VoucherSource.UserInput

            tpgIneligibleReason.text = voucher.voucherState.ineligibleMessageOrEmpty()
            tpgIneligibleReason.isVisible = voucher.voucherState is VoucherState.Ineligible

            cardView.onDrawn { cardViewHeightPx ->
                cardView.background = createVoucherShape(
                    cardViewHeightPx = cardViewHeightPx,
                    isVoucherSelected = voucher.voucherState.isSelected()
                )
            }

            if (voucher.voucherState is VoucherState.Loading) {
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


    private fun handleCashback(voucher: Voucher) {
        val voucherTypeTextColorResId = when {
            voucher.voucherState.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            voucher.voucherState.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            voucher.voucherState.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_cashback)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_gopay_coins, voucher.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(voucher)
        }
    }
    private fun handleFreeShipping(voucher: Voucher) {
        val voucherTypeTextColorResId = when {
            voucher.voucherState.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            voucher.voucherState.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            voucher.voucherState.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_free_shipping)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_voucher_amount, voucher.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(voucher)
        }
    }
    private fun handleDiscount(voucher: Voucher) {
        val voucherTypeTextColorResId = when {
            voucher.voucherState.isSelected() -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            voucher.voucherState.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            voucher.voucherState.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
        }

        binding?.run {
            tpgVoucherType.text = context?.getString(R.string.promo_voucher_discount)
            tpgVoucherType.setTextColorCompat(voucherTypeTextColorResId)

            tpgVoucherAmount.text = context?.getString(R.string.promo_voucher_placeholder_voucher_amount, voucher.benefitAmount.splitByThousand())
            tpgVoucherAmount.setVoucherAmountTextColor(voucher)
        }

    }


    private fun TextView.setVoucherAmountTextColor(voucher: Voucher) {
        val voucherAmountTextColorResId = when {
            voucher.voucherState.isDisabled() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            voucher.voucherState.isIneligible() -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
        }

        setTextColorCompat(voucherAmountTextColorResId)
    }

    private fun VoucherState.ineligibleMessageOrEmpty(): String {
        return if (this is VoucherState.Ineligible) {
            this.ineligibleReason
        } else {
            ""
        }
    }

    private fun VoucherSource.voucherCodeOrEmpty(): String {
        return if (this is VoucherSource.UserInput) {
            this.voucherCode
        } else {
            ""
        }
    }

    private fun VoucherState.isSelected(): Boolean {
        return this == VoucherState.Selected
    }

    private fun VoucherState.isDisabled(): Boolean {
        return this == VoucherState.Disabled
    }

    private fun VoucherState.isIneligible(): Boolean {
        return this is VoucherState.Ineligible
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
