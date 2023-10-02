package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.coupon_list.CouponListViewTypeFactory

data class CouponListHistoryEmptyModel(
    val title: String,
    val ctaText: String,
    val onCtaClick: () -> Unit = {},
) : Visitable<CouponListViewTypeFactory> {
    override fun type(typeFactory: CouponListViewTypeFactory): Int = typeFactory.type(this)
}
