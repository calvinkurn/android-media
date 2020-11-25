package com.tokopedia.promotionstarget.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.presentation.ui.viewholders.CouponListVH

class CouponListAdapter(val couponList: ArrayList<GetCouponDetail>) : RecyclerView.Adapter<CouponListVH>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CouponListVH {
        val vh = CouponListVH(LayoutInflater.from(viewGroup.context).inflate(CouponListVH.getLayout(), viewGroup, false))
        if (couponList.size > 1) {
            vh.setDynamicWidth()
        }
        return vh
    }

    override fun getItemCount() = couponList.size

    override fun onBindViewHolder(vh: CouponListVH, position: Int) {
        vh.setData(couponList[position])
    }
}

