package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCouponViewBinding
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
                // TODO: handle empty case
            }
            benefitList.size == 1 -> {
                binding.cardCoupon.setData(benefitSectionModel.benefitList!!.first()) {}
            }
            benefitList.size > 1 -> {
                // TODO: handle multiple coupons
            }
        }
    }
}
