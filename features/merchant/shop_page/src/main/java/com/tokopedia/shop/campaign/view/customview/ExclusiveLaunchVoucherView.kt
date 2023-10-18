package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.databinding.CustomViewExclusiveLaunchVoucherBinding
import com.tokopedia.shop.R

class ExclusiveLaunchVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MINIMUM_VOUCHER_QUOTA = 5
    }

    private var binding: CustomViewExclusiveLaunchVoucherBinding? = null


    init {
        binding = CustomViewExclusiveLaunchVoucherBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun setVoucherName(voucherName: String) {
        binding?.tpgBenefitName?.text = voucherName
    }

    fun setMinimumPurchase(text: String) {
        binding?.tpgMinPurchase?.text = text
    }

    fun setRemainingQuota(remainingQuota: Int) {
        when {
            remainingQuota == 0 -> {
                binding?.tpgRemainingQuota?.visible()
                binding?.tpgRemainingQuota?.text = context.getString(R.string.shop_page_no_voucher_quota)
            }
            remainingQuota <= MINIMUM_VOUCHER_QUOTA -> {
                binding?.tpgRemainingQuota?.visible()
                val remainingQuotaText = context.getString(R.string.shop_page_placeholder_remaining_quota, remainingQuota)
                binding?.tpgRemainingQuota?.text = remainingQuotaText
            }
            else ->  binding?.tpgRemainingQuota?.gone()
        }
    }

    fun setOnPrimaryCtaClick(onClick: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { onClick() }
    }

    fun setPrimaryCta(voucherCode: String, isDisabledButton: Boolean) {
        val isVoucherClaimed = voucherCode.isNotEmpty()
        val ctaText = when {
            !isDisabledButton && isVoucherClaimed ->  context?.getString(R.string.shop_page_claimed)
            !isDisabledButton && !isVoucherClaimed ->  context?.getString(R.string.shop_page_claim)
            else -> context?.getString(R.string.shop_page_claimed)
        }

        binding?.tpgClaim?.text = ctaText
        val isCtaClickable = !isDisabledButton && !isVoucherClaimed
        binding?.tpgClaim?.isClickable = isCtaClickable
    }

    fun useDarkBackground(isCtaDisabled: Boolean) {
        binding?.run {
            imgBackground.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_dark)
            tpgBenefitName.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            tpgMinPurchase.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            tpgRemainingQuota.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_RN500)

            if (isCtaDisabled) {
                tpgClaim.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN400)
            } else {
                tpgClaim.setTextColorCompat(R.color.clr_dms_voucher_claim)
            }
        }
    }

    fun useLightBackground(isCtaDisabled: Boolean) {
        binding?.run {
            imgBackground.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_light)
            tpgBenefitName.setTextColorCompat(R.color.clr_dms_voucher_title)
            tpgMinPurchase.setTextColorCompat(R.color.clr_dms_voucher_min_purchase)
            tpgRemainingQuota.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_RN500)

            if (isCtaDisabled) {
                tpgClaim.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN400)
            } else {
                tpgClaim.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }
        }
    }
}
