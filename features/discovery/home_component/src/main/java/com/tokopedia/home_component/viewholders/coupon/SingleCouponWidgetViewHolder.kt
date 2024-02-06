package com.tokopedia.home_component.viewholders.coupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutCouponWidgetSingleBinding
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.utils.view.binding.viewBinding

class SingleCouponWidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: LayoutCouponWidgetSingleBinding? by viewBinding()

    fun onBind(model: CouponWidgetDataItemModel) {
        binding?.couponView?.setModel(model.coupon as AutomateCouponModel.List)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_coupon_widget_single

        fun create(parent: ViewGroup): SingleCouponWidgetViewHolder {
            return SingleCouponWidgetViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            )
        }
    }
}
