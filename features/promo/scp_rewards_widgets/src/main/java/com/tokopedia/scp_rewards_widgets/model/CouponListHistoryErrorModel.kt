package com.tokopedia.scp_rewards_widgets.model

import android.graphics.drawable.Drawable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.coupon_list.CouponListViewTypeFactory

data class CouponListHistoryErrorModel(val image: Drawable?) : Visitable<CouponListViewTypeFactory> {
    override fun type(typeFactory: CouponListViewTypeFactory): Int = typeFactory.type(this)
}
