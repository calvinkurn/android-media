package com.tokopedia.promousage.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.promousage.util.extension.removeGrayscale
import com.tokopedia.promousage.util.extension.setHyperlinkText
import com.tokopedia.unifycomponents.toPx

class VoucherCompoundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: PromoUsageVoucherViewBinding? = null
    private var onHyperlinkTextClick : (String) -> Unit = {}

    init {
        binding = PromoUsageVoucherViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(voucher: Voucher) {
        binding?.run {
            handleVoucherType(voucher)
            handleVoucherState(voucher.voucherState)
            handleVoucherSource(voucher.voucherSource)
            handleVoucherQuota(voucher.remainingQuota, voucher.showRemainingQuotaRibbon)

            tpgVoucherTnc.text = voucher.termAndCondition

            tpgVoucherExpiredDate.text = context?.getString(
                R.string.promo_voucher_placeholder_expired_date,
                voucher.expiredDate
            )
            tpgVoucherExpiredDate.text = MethodChecker.fromHtml(
                context?.getString(
                    R.string.promo_voucher_placeholder_expired_date,
                    voucher.expiredDate
                )
            )

            imgSupergraphic.loadImage(voucher.superGraphicImageUrl)
            imgVoucherIcon.loadImage(voucher.iconImageUrl)
        }
    }

    private fun handleVoucherQuota(remainingQuota: Int, showRemainingQuotaRibbon: Boolean) {
        if (showRemainingQuotaRibbon) {
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

    private fun handleVoucherSource(voucherSource: VoucherSource) {
        binding?.run {
            tpgVoucherCode.text = voucherSource.voucherCodeOrEmpty()
            tpgVoucherCode.isVisible = voucherSource is VoucherSource.UserInput
        }
    }

    private fun handleVoucherType(voucher: Voucher) {
        when (voucher.voucherType) {
            VoucherType.CASHBACK -> handleCashback(voucher)
            VoucherType.FREE_SHIPPING -> handleFreeShipping(voucher)
            VoucherType.DISCOUNT -> handleDiscount(voucher)
        }
    }

    private fun handleVoucherState(voucherState: VoucherState) {
        binding?.run {
            when (voucherState) {
                VoucherState.Loading -> {
                    imgCheckmark.gone()
                    imgSupergraphic.removeGrayscale()
                    imgVoucherIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    showLoadingAppearance()
                }

                VoucherState.Normal -> {
                    imgCheckmark.gone()
                    imgSupergraphic.removeGrayscale()
                    imgVoucherIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()
                }
                is VoucherState.Ineligible -> {
                    imgCheckmark.gone()
                    imgSupergraphic.grayscale()
                    imgVoucherIcon.grayscale()

                    tpgAdditionalInformation.text = voucherState.ineligibleReason
                    tpgAdditionalInformation.visible()
                    layoutActionable.gone()

                    hideLoadingAppearance()
                }
                is VoucherState.Actionable -> {
                    imgCheckmark.gone()
                    imgSupergraphic.removeGrayscale()
                    imgVoucherIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.visible()
                    tpgActionableText.text = voucherState.actionableText
                    tpgActionableText.setHyperlinkText(
                        fullText =  voucherState.actionableText,
                        hyperlinkSubstring = voucherState.hyperlinkSubString,
                        onHyperlinkClick = { onHyperlinkTextClick(voucherState.appLink) }
                    )

                    hideLoadingAppearance()
                }
                is VoucherState.Disabled -> {
                    imgCheckmark.gone()
                    imgSupergraphic.grayscale()
                    imgVoucherIcon.grayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()
                }
                is VoucherState.Selected -> {
                    imgCheckmark.visible()
                    imgSupergraphic.removeGrayscale()
                    imgVoucherIcon.removeGrayscale()

                    tpgAdditionalInformation.gone()
                    layoutActionable.gone()

                    hideLoadingAppearance()
                }

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

            tpgVoucherAmount.text = context?.getString(
                R.string.promo_voucher_placeholder_gopay_coins,
                voucher.benefitAmount.splitByThousand()
            )
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

            tpgVoucherAmount.text = context?.getString(
                R.string.promo_voucher_placeholder_voucher_amount,
                voucher.benefitAmount.splitByThousand()
            )
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

            tpgVoucherAmount.text = context?.getString(
                R.string.promo_voucher_placeholder_voucher_amount,
                voucher.benefitAmount.splitByThousand()
            )
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

    private fun showLoadingAppearance() {
        binding?.run {
            tpgVoucherAmount.gone()
            tpgVoucherTnc.gone()
            topShimmer.visible()
            middleShimmer.visible()
            bottomShimmer.visible()
        }

    }

    private fun hideLoadingAppearance() {
        binding?.run {
            tpgVoucherAmount.visible()
            tpgVoucherTnc.visible()
            topShimmer.gone()
            middleShimmer.gone()
            bottomShimmer.gone()
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

    fun setOnHyperlinkTextClick(onHyperlinkTextClick : (String) -> Unit) {
        this.onHyperlinkTextClick = onHyperlinkTextClick
    }

    private fun updateVoucherTypeMarginTop(marginTop: Int) {
        val layoutParams = binding?.tpgVoucherType?.layoutParams as? ConstraintLayout.LayoutParams
        layoutParams?.setMargins(0, marginTop, 0, 0)

        binding?.tpgVoucherType?.layoutParams = layoutParams
        binding?.tpgVoucherType?.requestLayout()
    }

    private fun updateCardViewMargin(marginStart: Int, marginTop: Int) {
        val layoutParams = binding?.cardView?.layoutParams as? RelativeLayout.LayoutParams
        layoutParams?.setMargins(marginStart, marginTop, 0, 0)

        binding?.cardView?.layoutParams = layoutParams
        binding?.cardView?.requestLayout()
    }

}
