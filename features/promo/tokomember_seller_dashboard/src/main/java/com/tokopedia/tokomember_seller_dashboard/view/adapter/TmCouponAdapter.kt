package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmCouponVh

class TmCouponAdapter(
    var vouchersItemList: ArrayList<VouchersItem>,
    val fragmentManager: FragmentManager,
    val tmCouponActions: TmCouponActions,
) :
    RecyclerView.Adapter<TmCouponVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmCouponVh {
        return TmCouponVh(
            LayoutInflater.from(parent.context).inflate(R.layout.tm_coupon_list_item,parent,false), fragmentManager
        )
    }

    override fun onBindViewHolder(holder: TmCouponVh, position: Int) {
        holder.bind(vouchersItemList[position], tmCouponActions)
    }

    override fun getItemCount() = vouchersItemList.size

}