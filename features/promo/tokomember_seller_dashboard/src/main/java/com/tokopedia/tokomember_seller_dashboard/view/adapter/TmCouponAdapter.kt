package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponDetailCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.model.CouponItem
import com.tokopedia.tokomember_seller_dashboard.model.LayoutType
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmCouponVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmMemberLoaderVh

class TmCouponAdapter(
    var vouchersItemList: List<CouponItem>,
    val fragmentManager: FragmentManager,
    val tmCouponActions: TmCouponActions,
    private val callback:TmCouponDetailCallback?,
    private val tmTracker: TmTracker?,
    private val tmCouponListRefreshCallback: TmCouponListRefreshCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            TmCouponVh.LAYOUT -> {
                TmCouponVh(
                    inflater.inflate(R.layout.tm_coupon_list_item,parent,false), fragmentManager
                )
            }
            else -> {
                val layout = inflater.inflate(TmMemberLoaderVh.LAYOUT_TYPE,parent,false)
                TmMemberLoaderVh(layout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TmCouponVh)
        holder.bind(vouchersItemList[position].coupon, tmCouponActions,callback,tmTracker, tmCouponListRefreshCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return when(vouchersItemList[position].layout){
            LayoutType.SHOW_CARD -> TmCouponVh.LAYOUT
            LayoutType.LOADER -> TmMemberLoaderVh.LAYOUT_TYPE
        }
    }

    override fun getItemCount() = vouchersItemList.size

}
