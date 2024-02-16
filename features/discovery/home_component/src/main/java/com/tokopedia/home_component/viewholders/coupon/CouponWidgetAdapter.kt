package com.tokopedia.home_component.viewholders.coupon

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel

class CouponWidgetAdapter constructor(
    private val data: MutableList<Pair<CouponWidgetDataModel, CouponWidgetDataItemModel>> = mutableListOf(),
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
        val (oldWidgetData, couponData) = data[position]

        if (holder is SingleCouponWidgetViewHolder) {
            holder.onBind(oldWidgetData, couponData)
        } else if (holder is GridCouponWidgetViewHolder) {
            holder.onBind(oldWidgetData, couponData)
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        if (data.size == 1) return FULL
        if (data.size == 2) return GRID

        return if (position == 0) FULL else GRID
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(widgetData: CouponWidgetDataModel) {
        val newData = mapData(widgetData)

        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

    /**
     * This internal map occurs due we have to send the old widget data.
     *
     * The main purpose is, if the cupon widget shows multiple times in homepage, hence we have to
     * update the button state partially based on coupon data as well as its visitable model.
     */
    private fun mapData(data: CouponWidgetDataModel) =
        data.coupons.map { Pair(data, it) }

    companion object {
        private const val FULL = 0
        private const val GRID = 1
    }
}
