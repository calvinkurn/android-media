package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.constants.Constants.CouponState
import com.tokopedia.scp_rewards_widgets.databinding.RewardsViewLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel

class RewardsViewHolder(itemView:View) : AbstractViewHolder<MedalRewardsModel>(itemView) {
    companion object{
        val LAYOUT = R.layout.rewards_view_layout
    }

    private var binding:RewardsViewLayoutBinding? = null

    init {
        binding = RewardsViewLayoutBinding.bind(itemView)
    }

    override fun bind(data: MedalRewardsModel?) {
        data?.let {
            loadCouponImage(it)
            loadCouponDate(it)
        }
    }

    private fun loadCouponImage(data: MedalRewardsModel){
        binding?.couponView?.setImageUrl(data.imageUrl)
        if(!data.isActive){
            binding?.couponView?.setCouponToLockedState()
        }
    }

    private fun loadCouponDate(data: MedalRewardsModel){
        binding?.rewardExpiryInfo?.apply {
            text = data.statusDescription
            val textColorToken = when(data.status){
                CouponState.ACTIVE -> R.color.NN_950
                CouponState.EXPIRED -> R.color.RN_500
                CouponState.INACTIVE -> R.color.NN_600
                else -> R.color.RN_500
            }
            setTextColor(ContextCompat.getColor(context,textColorToken))
        }
    }
}
