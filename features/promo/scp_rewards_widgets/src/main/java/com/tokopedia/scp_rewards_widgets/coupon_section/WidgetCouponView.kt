package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
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

        if (benefitSectionModel.benefitList.isNullOrEmpty()) {
            // TODO: handle empty case 
        }

        if (benefitSectionModel.benefitList?.size == 1) {
            // TODO: handle single coupon
        } else {
            // TODO: handle multiple coupons 
        }
    }
}
