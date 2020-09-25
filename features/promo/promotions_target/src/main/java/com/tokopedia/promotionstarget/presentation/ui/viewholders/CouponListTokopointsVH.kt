package com.tokopedia.promotionstarget.presentation.ui.viewholders

import android.view.View
import android.widget.TextView
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetail

class CouponListTokopointsVH(itemView: View) : CouponListVH(itemView) {
    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons
    }

    private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

    fun setTpCouponData(data: TokopointsCouponDetail) {
        tvStatus.text = data.statusStr
    }
}