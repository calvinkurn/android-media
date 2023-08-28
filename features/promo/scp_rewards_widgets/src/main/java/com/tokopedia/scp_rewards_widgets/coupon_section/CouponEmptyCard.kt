package com.tokopedia.scp_rewards_widgets.coupon_section

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.tokopedia.scp_rewards_widgets.databinding.ItemCouponEmptyLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel

@SuppressLint("RestrictedApi")
class CouponEmptyCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private var binding = ItemCouponEmptyLayoutBinding.inflate(LayoutInflater.from(context), this)

    fun setData(data: MedalBenefitModel, onCtaClick: (String?) -> Unit) {
        with(binding) {
            tvTitle.text = data.title
            tvDescription.text = data.statusDescription
            ivIcon.setImageUrl(data.medaliImageURL.orEmpty())
            btnSeeBonusHistory.text = data.cta?.text
            btnSeeBonusHistory.setOnClickListener {
                onCtaClick(data.cta?.appLink)
            }
        }
    }
}
