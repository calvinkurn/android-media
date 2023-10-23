package com.tokopedia.scp_rewards_widgets.coupon_list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.LayoutCouponListHistoryEmptyBinding
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryEmptyModel

class CouponListHistoryEmptyViewHolder(itemView: View) :
    AbstractViewHolder<CouponListHistoryEmptyModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_coupon_list_history_empty
    }

    private var binding: LayoutCouponListHistoryEmptyBinding =
        LayoutCouponListHistoryEmptyBinding.bind(itemView)

    override fun bind(item: CouponListHistoryEmptyModel) {
        binding.apply {
            tvErrorTitle.text = item.title
            btnAction.text = item.ctaText
            btnAction.setOnClickListener {
                item.onCtaClick()
            }
        }
    }
}
