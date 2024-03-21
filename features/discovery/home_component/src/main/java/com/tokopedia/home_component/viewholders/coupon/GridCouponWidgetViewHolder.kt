package com.tokopedia.home_component.viewholders.coupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutCouponWidgetGridBinding
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.utils.view.binding.viewBinding

class GridCouponWidgetViewHolder constructor(
    view: View,
    private val listener: CouponWidgetListener
) : RecyclerView.ViewHolder(view) {

    private val binding: LayoutCouponWidgetGridBinding? by viewBinding()

    fun onBind(oldWidgetData: CouponWidgetDataModel, model: CouponWidgetDataItemModel) {
        val state = ButtonStateHandler(oldWidgetData, model, bindingAdapterPosition, listener)

        binding?.couponView?.setState(state)
        binding?.couponView?.setModel(model.coupon as AutomateCouponModel.Grid)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_coupon_widget_grid

        fun create(parent: ViewGroup, listener: CouponWidgetListener): GridCouponWidgetViewHolder {
            val parentView = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return GridCouponWidgetViewHolder(parentView, listener)
        }
    }
}
