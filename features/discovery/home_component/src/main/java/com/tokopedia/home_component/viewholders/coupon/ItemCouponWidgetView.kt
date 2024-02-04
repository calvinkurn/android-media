package com.tokopedia.home_component.viewholders.coupon

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel

object ItemCouponWidgetView {

    class Adapter constructor(
        private val data: MutableList<CouponWidgetDataItemModel> = mutableListOf()
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(data[position])
        }

        override fun getItemCount() = data.size

        @SuppressLint("NotifyDataSetChanged")
        fun setData(coupons: List<CouponWidgetDataItemModel>) {
            data.clear()
            data.addAll(coupons)

            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun onBind(model: CouponWidgetDataItemModel) {}

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.layout_coupon_widget

            fun create(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
                )
            }
        }
    }
}
