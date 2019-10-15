package com.tokopedia.promotionstarget.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.ui.adapter.CouponData
import com.tokopedia.promotionstarget.ui.views.CouponView

class CouponListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons
    }

    var couponView: CouponView = itemView.findViewById(R.id.couponView)

    fun setData(data: CouponData) {

    }
}