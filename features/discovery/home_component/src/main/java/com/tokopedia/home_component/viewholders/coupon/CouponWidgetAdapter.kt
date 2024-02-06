package com.tokopedia.home_component.viewholders.coupon

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel

class CouponWidgetAdapter constructor(
    private val data: MutableList<CouponWidgetDataItemModel> = mutableListOf(),
    private val listener: CouponWidgetListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FULL -> SingleCouponWidgetViewHolder.create(parent, listener)
            GRID -> GridCouponWidgetViewHolder.create(parent, listener)
            else -> throw Throwable("Unsupported view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SingleCouponWidgetViewHolder) {
            holder.onBind(data[position])
        } else if (holder is GridCouponWidgetViewHolder) {
            holder.onBind(data[position])
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        if (data.size == 1) return FULL
        if (data.size == 2) return GRID

        return if (position == 0) FULL else GRID
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(coupons: List<CouponWidgetDataItemModel>) {
        data.clear()
        data.addAll(coupons)

        notifyDataSetChanged()
    }

    companion object {
        private const val FULL = 0
        private const val GRID = 1
    }
}
