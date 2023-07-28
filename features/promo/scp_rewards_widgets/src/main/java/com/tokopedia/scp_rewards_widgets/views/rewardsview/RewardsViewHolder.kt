package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.databinding.RewardsViewLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel

class RewardsViewHolder(itemView: View) : AbstractViewHolder<MedalRewardsModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.rewards_view_layout
    }

    private var binding: RewardsViewLayoutBinding? = null

    init {
        binding = RewardsViewLayoutBinding.bind(itemView)
    }

    override fun bind(data: MedalRewardsModel?) {
        data?.let {
            loadCouponImage(it)
            loadCouponDate(it)
        }
    }

    private fun loadCouponImage(data: MedalRewardsModel) {
        binding?.couponView?.setImageUrl(data.imageUrl)
        if (!data.isActive) {
            binding?.couponView?.setCouponToLockedState()
        } else {
            binding?.couponView?.setCouponToActiveState()
        }
    }

    private fun loadCouponDate(data: MedalRewardsModel) {
        binding?.rewardExpiryInfo?.apply {
            text = data.statusDescription
            val textColorToken = when (data.status) {
                CouponState.ACTIVE -> com.tokopedia.unifyprinciples.R.color.Unify_NN950
                CouponState.EXPIRED -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                CouponState.INACTIVE -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
                else -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
            }
            setTextColor(ContextCompat.getColor(context, textColorToken))
        }
    }
}
