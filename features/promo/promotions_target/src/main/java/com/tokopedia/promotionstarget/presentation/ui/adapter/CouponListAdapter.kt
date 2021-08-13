package com.tokopedia.promotionstarget.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promotionstarget.data.coupon.CouponUiData
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetail
import com.tokopedia.promotionstarget.presentation.ui.viewholders.CouponListTokopointsVH
import com.tokopedia.promotionstarget.presentation.ui.viewholders.CouponListVH

class CouponListAdapter(val couponList: ArrayList<CouponUiData>) : RecyclerView.Adapter<CouponListVH>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CouponListVH {
        val vh: CouponListVH
        when (viewType) {
            CouponListVH.getLayout() -> {
                vh = CouponListVH(LayoutInflater.from(viewGroup.context).inflate(viewType, viewGroup, false))
            }
            else -> {
                vh = CouponListTokopointsVH(LayoutInflater.from(viewGroup.context).inflate(viewType, viewGroup, false))
            }
        }
        if (couponList.size > 1) {
            vh.setDynamicWidth()
        }
        return vh
    }

    override fun getItemCount() = couponList.size
    override fun getItemViewType(position: Int): Int {
        if (couponList[position] is GetCouponDetail) {
            return CouponListVH.getLayout()
        } else
            return CouponListTokopointsVH.getLayout()
    }

    override fun onBindViewHolder(vh: CouponListVH, position: Int) {
        when(couponList[position]){
            is GetCouponDetail-> vh.setData(couponList[position] as GetCouponDetail)
            is TokopointsCouponDetail-> (vh as CouponListTokopointsVH).setTpCouponData(couponList[position] as TokopointsCouponDetail)
        }
    }
}