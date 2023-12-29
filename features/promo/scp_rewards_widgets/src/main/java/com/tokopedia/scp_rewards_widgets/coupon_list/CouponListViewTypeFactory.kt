package com.tokopedia.scp_rewards_widgets.coupon_list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.scp_rewards_widgets.model.CouponListActiveEmptyModel
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryEmptyModel
import com.tokopedia.scp_rewards_widgets.model.CouponListHistoryErrorModel

class CouponListViewTypeFactory(private val onApplyClick: (MedalBenefitModel, Int) -> Unit = { _, _ -> },
                                private val onCardTap: ((MedalBenefitModel) -> Unit)? = null) : BaseAdapterTypeFactory() {

    fun type(model: MedalBenefitModel) = CouponListViewHolder.LAYOUT
    fun type(model: CouponListActiveEmptyModel) = CouponListActiveEmptyViewHolder.LAYOUT
    fun type(model: CouponListHistoryEmptyModel) = CouponListHistoryEmptyViewHolder.LAYOUT
    fun type(model: CouponListHistoryErrorModel) = CouponListHistoryErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CouponListViewHolder.LAYOUT -> CouponListViewHolder(parent, onApplyClick, onCardTap)
            CouponListActiveEmptyViewHolder.LAYOUT -> CouponListActiveEmptyViewHolder(parent)
            CouponListHistoryEmptyViewHolder.LAYOUT -> CouponListHistoryEmptyViewHolder(parent)
            CouponListHistoryErrorViewHolder.LAYOUT -> CouponListHistoryErrorViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
