package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCouponViewBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitSectionModel

class WidgetCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetCouponViewBinding.inflate(LayoutInflater.from(context), this)

    fun renderCoupons(
        benefitSectionModel: MedalBenefitSectionModel,
        onCtaClick: (String?) -> Unit = {},
        onErrorAction: () -> Unit = {}
    ) {
        binding.tvTitle.text = benefitSectionModel.title
        setBackgroundColor(parseColor(benefitSectionModel.backgroundColor) ?: Color.WHITE)

        val benefitList = benefitSectionModel.benefitList
        when {
            benefitList.isNullOrEmpty() -> {
                binding.cardCoupon.hide()
                binding.cardEmptyCoupon.hide()
                binding.ivErrorState.visible()
            }

            // TODO: this condition  should be benefitList.size == 1. Modified for testing currently
            benefitList.size >= 1 -> {
                val benefit = benefitList.first()
                when (benefit.status) {
                    CouponState.EMPTY -> {
                        binding.cardCoupon.hide()
                        binding.ivErrorState.hide()
                        binding.cardEmptyCoupon.visible()
                        binding.cardEmptyCoupon.setData(benefit) {
                            onCtaClick(it)
                        }
                    }
                    CouponState.ERROR -> {
                        binding.cardCoupon.hide()
                        binding.cardEmptyCoupon.hide()
                        binding.ivErrorState.visible()
                        binding.ivErrorState.loadImageOrFallback(
                            benefit.medaliImageURL,
                            com.tokopedia.scp_rewards_widgets.R.drawable.ic_coupon_error
                        )
                    }
                    else -> {
                        binding.ivErrorState.hide()
                        binding.cardEmptyCoupon.hide()
                        binding.cardCoupon.visible()
                        binding.cardCoupon.setData(benefit) {
                            onCtaClick(it)
                        }
                    }
                }
            }

            benefitList.size > 1 -> {
                binding.ivErrorState.hide()
                binding.cardEmptyCoupon.hide()
                binding.cardCoupon.visible()
                // TODO: handle multiple coupons
                binding.cardCoupon.setData(benefitList.first()) {}
            }
        }
    }
}
