package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.shop.databinding.CustomViewExclusiveLaunchVoucherBinding
import com.tokopedia.shop.R

class ExclusiveLaunchVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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

    fun setRemainingQuota(remainingQuota: String) {
        binding?.tpgRemainingQuota?.text = remainingQuota
    }

    fun setOnPrimaryCtaClick(onClick: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { onClick() }
    }

    fun setPrimaryCta(ctaText: String, onClick: () -> Unit) {
        binding?.tpgClaim?.text = ctaText
        setOnPrimaryCtaClick(onClick)
    }

    fun useDarkBackground() {
        binding?.run {
            rootLayout.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_dark)
            tpgBenefitName.setTextColorCompat(R.color.Unify_Static_White)
            tpgMinPurchase.setTextColorCompat(R.color.Unify_Static_White)
            tpgRemainingQuota.setTextColorCompat(R.color.Unify_RN500)
            tpgClaim.setTextColorCompat(R.color.clr_dms_voucher_claim)
        }
    }

    fun useLightBackground() {
        binding?.run {
            rootLayout.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_light)
            tpgBenefitName.setTextColorCompat(R.color.clr_dms_voucher_title)
            tpgMinPurchase.setTextColorCompat(R.color.clr_dms_voucher_min_purchase)
            tpgRemainingQuota.setTextColorCompat(R.color.Unify_RN500)
            tpgClaim.setTextColorCompat(R.color.Unify_GN500)
        }
    }
}
