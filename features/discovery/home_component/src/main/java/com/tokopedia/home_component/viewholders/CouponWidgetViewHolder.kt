package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentCouponWidgetBinding
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetAdapter
import com.tokopedia.home_component.viewholders.layoutmanager.DynamicGridLayoutManager
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.utils.view.binding.viewBinding

class CouponWidgetViewHolder constructor(
    view: View,
    recyclerViewPool: RecyclerView.RecycledViewPool?
) : AbstractViewHolder<CouponWidgetDataModel>(view) {

    private val binding: HomeComponentCouponWidgetBinding? by viewBinding()

    private val mockData = listOf(
        CouponWidgetDataItemModel(shopName = "Loren Ipsum"),
        CouponWidgetDataItemModel(shopName = "Foo"),
        CouponWidgetDataItemModel(shopName = "Bar"),
    )

    init {
        if (recyclerViewPool != null) {
            binding?.lstCoupon?.setRecycledViewPool(recyclerViewPool)
        }
    }

    override fun bind(element: CouponWidgetDataModel?) {
        if (binding?.lstCoupon?.adapter == null) {
            val adapter = CouponWidgetAdapter()

            binding?.lstCoupon?.layoutManager = DynamicGridLayoutManager(itemView.context)
            binding?.lstCoupon?.adapter = adapter

            adapter.setData(mockData)
        } else {
            val adapter = binding?.lstCoupon?.adapter as? CouponWidgetAdapter
            adapter?.setData(mockData)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_coupon_widget
    }
}
