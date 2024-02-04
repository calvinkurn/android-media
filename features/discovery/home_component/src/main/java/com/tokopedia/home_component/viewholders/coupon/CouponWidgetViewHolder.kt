package com.tokopedia.home_component.viewholders.coupon

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentCouponWidgetBinding
import com.tokopedia.home_component.viewholders.layoutmanager.CouponGridLayoutManager
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.utils.view.binding.viewBinding

class CouponWidgetViewHolder constructor(
    view: View,
    recyclerViewPool: RecyclerView.RecycledViewPool?
) : AbstractViewHolder<CouponWidgetDataModel>(view) {

    private val binding: HomeComponentCouponWidgetBinding? by viewBinding()

    private val mockData = listOf(
        CouponWidgetDataItemModel("Loren Ipsum"),
        CouponWidgetDataItemModel("Foo"),
        CouponWidgetDataItemModel("Bar"),
    )

    init {
        if (recyclerViewPool != null) {
            binding?.lstCoupon?.setRecycledViewPool(recyclerViewPool)
        }
    }

    override fun bind(element: CouponWidgetDataModel?) {
        if (binding?.lstCoupon?.adapter == null) {
            val adapter = ItemCouponWidgetView.Adapter()

            binding?.lstCoupon?.layoutManager = CouponGridLayoutManager()
            binding?.lstCoupon?.adapter = adapter

            adapter.setData(mockData)
        } else {
            val adapter = binding?.lstCoupon?.adapter as? ItemCouponWidgetView.Adapter
            adapter?.setData(mockData)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_coupon_widget
    }
}
