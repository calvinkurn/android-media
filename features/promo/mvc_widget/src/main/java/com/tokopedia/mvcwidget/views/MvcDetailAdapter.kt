package com.tokopedia.mvcwidget.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.views.viewholders.CouponListItemVH
import com.tokopedia.mvcwidget.views.viewholders.FollowViewHolder
import com.tokopedia.mvcwidget.views.viewholders.TickerViewHolder

class MvcDetailAdapter(val data: ArrayList<MvcListItem>, val contract: MvcDetailViewContract) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        val v = when (viewType) {
            R.layout.mvc_list_item_coupon -> CouponListItemVH(view)
            R.layout.mvc_follow_list_item -> FollowViewHolder(view, contract)
            R.layout.mvc_ticker_list_item -> TickerViewHolder(view)
            else -> throw IllegalArgumentException("Unsupported View")
        }
        return v
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is MvcCouponListItem -> R.layout.mvc_list_item_coupon
            is FollowWidget -> R.layout.mvc_follow_list_item
            is TickerText -> R.layout.mvc_ticker_list_item
            else -> super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CouponListItemVH -> {
                holder.setData(data[position] as MvcCouponListItem)
                holder.divider.visibility = if (position == data.size - 1) View.INVISIBLE else View.VISIBLE
            }
            is FollowViewHolder -> {
                holder.setData(data[position] as FollowWidget, contract.getWidgetImpression(),contract.getShopId(), contract.getMvcSource())
            }
            is TickerViewHolder -> {
                holder.setData(data[position] as TickerText)
            }
        }
    }

    fun updateList(newList: List<MvcListItem>) {
        if(newList.isNotEmpty()) {
            data.clear()
            data.addAll(newList)
            notifyDataSetChanged()
        }
    }
}