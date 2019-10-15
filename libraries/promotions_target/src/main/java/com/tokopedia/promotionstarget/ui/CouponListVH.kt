package com.tokopedia.promotionstarget.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.ui.adapter.CouponData
import com.tokopedia.promotionstarget.ui.views.CouponView

class CouponListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons
        const val WIDTH_RATIO = 1.26
    }

    fun setDynamicWidth(){
        val width = itemView.context.resources.displayMetrics.widthPixels
        val lp = itemView.layoutParams as RecyclerView.LayoutParams
        lp.width = (width / WIDTH_RATIO).toInt()
        itemView.layoutParams = lp
    }

    private var couponView: CouponView = itemView.findViewById(R.id.couponView)

    fun setData(data: CouponData) {

    }
}