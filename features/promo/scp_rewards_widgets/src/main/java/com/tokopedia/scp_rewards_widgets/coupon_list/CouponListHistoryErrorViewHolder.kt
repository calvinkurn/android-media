package com.tokopedia.scp_rewards_widgets.coupon_list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.LayoutCouponListHistoryErrorBinding
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryErrorModel

class CouponListHistoryErrorViewHolder(itemView: View) :
    AbstractViewHolder<CouponListHistoryErrorModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_coupon_list_history_error
    }

    private var binding: LayoutCouponListHistoryErrorBinding =
        LayoutCouponListHistoryErrorBinding.bind(itemView)

    override fun bind(item: CouponListHistoryErrorModel) {
        binding.ivErrorImage.setImageDrawable(item.image)
    }
}
