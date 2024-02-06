package com.tokopedia.home_component.viewholders.coupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel

class GridCouponWidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun onBind(model: CouponWidgetDataItemModel) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_coupon_widget_grid

        fun create(parent: ViewGroup): GridCouponWidgetViewHolder {
            return GridCouponWidgetViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            )
        }
    }
}
