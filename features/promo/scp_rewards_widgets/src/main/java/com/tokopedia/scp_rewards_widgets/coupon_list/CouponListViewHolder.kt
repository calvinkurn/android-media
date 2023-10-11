package com.tokopedia.scp_rewards_widgets.coupon_list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemCouponListBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel

class CouponListViewHolder(
        itemView: View,
        private val onApplyClick: (MedalBenefitModel, Int) -> Unit = { _, _ -> },
        private val onCardTap: ((MedalBenefitModel) -> Unit)? = null)
    : AbstractViewHolder<MedalBenefitModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_coupon_list
    }

    private var binding: ItemCouponListBinding = ItemCouponListBinding.bind(itemView)

    override fun bind(item: MedalBenefitModel) {
        binding.viewCouponCard.apply {
            setData(item, onApplyClick = {
                onApplyClick(it, bindingAdapterPosition)
            }, onCardTap)
            showHideInfo(false)
        }
    }
}
