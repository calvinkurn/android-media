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

    companion object {
        private const val THOUSAND: Long = 1_000
        private const val MILLION: Long = 1_000_000
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

    fun setMinimumPurchase(minimumPurchase: Long) {
        val formattedMinimumPurchase = if (minimumPurchase >= MILLION) {
            val million = (minimumPurchase / MILLION).toInt()
            context.getString(
                R.string.shop_page_placeholder_minimal_purchase_million,
                million.toString()
            )
        } else if (minimumPurchase >= THOUSAND) {
            val thousand = (minimumPurchase / THOUSAND).toInt()
            context.getString(
                R.string.shop_page_placeholder_minimal_purchase_thousand,
                thousand.toString()
            )
        } else {
            context.getString(
                R.string.shop_page_placeholder_minimal_purchase,
                minimumPurchase.toString()
            )
        }
        binding?.tpgMinPurchase?.text = formattedMinimumPurchase
    }

    fun setRemainingQuota(remainingQuota: Int) {
        binding?.tpgRemainingQuota?.text =
            context.getString(R.string.shop_page_placeholder_remaining_quota, remainingQuota)
    }

    fun setOnPrimaryCtaClick(block: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { block() }
    }

    fun setPrimaryCta(ctaText: String, onClick: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { onClick() }
        binding?.tpgClaim?.text = ctaText
    }

    fun useDarkBackground() {
        binding?.run {
            rootLayout.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_dark)
            tpgBenefitName.setTextColorCompat(R.color.Unify_Static_White)
            tpgMinPurchase.setTextColorCompat(R.color.Unify_Static_White)
            tpgRemainingQuota.setTextColorCompat(R.color.Unify_RN500)
            tpgClaim.setTextColorCompat(R.color.Unify_NN950)
        }
    }

    fun useLightBackground() {
        binding?.run {
            rootLayout.setBackgroundResource(R.drawable.bg_exclusive_launch_voucher_light)
            tpgBenefitName.setTextColorCompat(R.color.Unify_NN950)
            tpgMinPurchase.setTextColorCompat(R.color.Unify_NN950)
            tpgRemainingQuota.setTextColorCompat(R.color.Unify_RN500)
            tpgClaim.setTextColorCompat(R.color.Unify_GN500)
        }
    }
}
