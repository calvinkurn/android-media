package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.RewardsViewErrorLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.RewardsErrorModel

class CouponErrorViewHolder(itemView:View) : AbstractViewHolder<RewardsErrorModel>(itemView) {

    companion object{
        val LAYOUT = R.layout.rewards_view_error_layout
    }

    private var binding:RewardsViewErrorLayoutBinding?=null

    init {
        binding = RewardsViewErrorLayoutBinding.bind(itemView)
    }

    override fun bind(data: RewardsErrorModel?) {
        data?.let {
           binding?.errorImage?.setImageUrl(it.imageUrl)
        }
    }
}
