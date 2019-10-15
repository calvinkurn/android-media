package com.tokopedia.promotionstarget.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.promotionstarget.ui.CouponListVH

class CouponListAdapter(val couponList: ArrayList<CouponData>) : RecyclerView.Adapter<CouponListVH>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CouponListVH {
        return CouponListVH(LayoutInflater.from(viewGroup.context).inflate(CouponListVH.getLayout(), viewGroup, false))
    }

    override fun getItemCount() = couponList.size

    override fun onBindViewHolder(vh: CouponListVH, position: Int) {
        vh.setData(couponList[position])
    }
}

data class CouponData(val id: Int)