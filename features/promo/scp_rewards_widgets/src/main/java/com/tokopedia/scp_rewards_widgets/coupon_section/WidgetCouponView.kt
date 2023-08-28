package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCouponViewBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitSectionModel

class WidgetCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetCouponViewBinding.inflate(LayoutInflater.from(context), this)

    fun renderCoupons(benefitSectionModel: MedalBenefitSectionModel, onErrorAction: () -> Unit) {
        binding.tvTitle.text = benefitSectionModel.title
        setBackgroundColor(parseColor(benefitSectionModel.backgroundColor) ?: Color.WHITE)

        val benefitList = benefitSectionModel.benefitList
        when {
            benefitList.isNullOrEmpty() -> {
                binding.ivErrorState.visible()
                // TODO: handle error case
            }
            benefitList.size == 1 -> {
                val benefit = benefitList.first()
                if (benefit.status == CouponState.EMPTY) {
                    binding.cardEmptyCoupon.visible()
                    binding.cardEmptyCoupon.setData(
                        MedalBenefitModel(
                            title = "We will refill your bonus \uD83D\uDC4D",
                            statusDescription = "Stay tuned for upcoming updates!",
                            medaliImageURL = "",
                            cta = CtaButton(text = "See Bonus History (12)")
                        )
                    ) {
                        // TODO: open bonus history page
                    }
                } else {
                    binding.cardCoupon.visible()
                    binding.cardCoupon.setData(benefitSectionModel.benefitList.first()) {
                        // TODO: auto apply coupon
                    }
                }
            }
            benefitList.size > 1 -> {
                // TODO: handle multiple coupons
                binding.cardCoupon.setData(benefitSectionModel.benefitList.first()) {}
            }
        }
    }
}
