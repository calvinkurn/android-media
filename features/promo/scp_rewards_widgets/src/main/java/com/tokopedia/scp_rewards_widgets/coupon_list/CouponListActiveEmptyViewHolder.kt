package com.tokopedia.scp_rewards_widgets.coupon_list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.LayoutCouponListActiveEmptyBinding
import com.tokopedia.scp_rewards_widgets.model.CouponListActiveEmptyModel

class CouponListActiveEmptyViewHolder(itemView: View) :
    AbstractViewHolder<CouponListActiveEmptyModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_coupon_list_active_empty
    }

    private var binding: LayoutCouponListActiveEmptyBinding =
        LayoutCouponListActiveEmptyBinding.bind(itemView)

    override fun bind(item: CouponListActiveEmptyModel) {
        binding.apply {
            tvErrorTitle.text = item.title
            tvErrorSubtitle.text = item.subtitle
            ivEmptyImage.setImageDrawable(item.icon)
        }
    }
}
