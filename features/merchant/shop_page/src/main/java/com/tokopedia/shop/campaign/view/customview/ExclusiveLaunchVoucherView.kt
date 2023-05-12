package com.tokopedia.shop.campaign.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.databinding.CustomViewExclusiveLaunchVoucherBinding
import com.tokopedia.shop.R

class ExclusiveLaunchVoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding : CustomViewExclusiveLaunchVoucherBinding? = null

    init {
        binding = CustomViewExclusiveLaunchVoucherBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setVoucherName(benefit: Long, benefitMax: Long) {
        binding?.tpgRemainingQuota?.text = context.getString(R.string.shop_page_placeholder_benefit_name, benefit.toString(), benefitMax.toString())
    }

    fun setMinimumPurchase(minimumPurchase: Long) {
        val unit = if(minimumPurchase > 1_000_000) "jt" else "rb"
        val formattedMinimumPurchase = "$minimumPurchase $unit"
        binding?.tpgRemainingQuota?.text = context.getString(R.string.shop_page_placeholder_minimal_purchase, formattedMinimumPurchase)
    }

    fun setRemainingQuota(remainingQuota: Int) {
        if (remainingQuota.isMoreThanZero()) {
            binding?.tpgRemainingQuota?.visible()
            binding?.tpgRemainingQuota?.text = context.getString(R.string.shop_page_placeholder_remaining_quota, remainingQuota)
        } else {
            binding?.tpgRemainingQuota?.gone()
        }
    }

    fun setOnPrimaryCtaClick(block: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { block() }
    }

    fun setPrimaryCta(ctaText: String, onClick: () -> Unit) {
        binding?.tpgClaim?.setOnClickListener { onClick() }
        binding?.tpgClaim?.text = ctaText
    }

    fun destroy() {
        binding = null
    }
}
