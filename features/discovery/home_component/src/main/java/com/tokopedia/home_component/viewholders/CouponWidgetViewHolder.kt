package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentCouponWidgetBinding
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetAdapter
import com.tokopedia.home_component.viewholders.coupon.CouponWidgetListener
import com.tokopedia.home_component.viewholders.layoutmanager.DynamicGridLayoutManager
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class CouponWidgetViewHolder constructor(
    view: View,
    recyclerViewPool: RecyclerView.RecycledViewPool?,
    private val listener: CouponWidgetListener
) : AbstractViewHolder<CouponWidgetDataModel>(view) {

    private val binding: HomeComponentCouponWidgetBinding? by viewBinding()

    init {
        if (recyclerViewPool != null) {
            binding?.lstCoupon?.setRecycledViewPool(recyclerViewPool)
        }
    }

    override fun bind(element: CouponWidgetDataModel?) {
        if (element == null || element.coupons.isEmpty()) return

        onWidgetImpressionListener(element)
        setupHeaderAndBackgroundWidget(element)
        setupDynamicCouponWidgetAdapter(element)
    }

    private fun setupDynamicCouponWidgetAdapter(element: CouponWidgetDataModel) {
        if (binding?.lstCoupon?.adapter == null) {
            val adapter = CouponWidgetAdapter(listener = listener)
            binding?.lstCoupon?.layoutManager = DynamicGridLayoutManager(itemView.context)
            binding?.lstCoupon?.addItemDecoration(DynamicGridLayoutManager.Decorator())
            binding?.lstCoupon?.adapter = adapter

            adapter.setData(element)
        } else {
            val adapter = binding?.lstCoupon?.adapter as? CouponWidgetAdapter
            adapter?.setData(element)
        }
    }

    private fun setupHeaderAndBackgroundWidget(element: CouponWidgetDataModel) {
        binding?.headerView?.bind(element.header())
        binding?.root?.setGradientBackground(element.backgroundGradientColor)
    }

    private fun onWidgetImpressionListener(element: CouponWidgetDataModel) {
        itemView.addOnImpressionListener(element, object : ViewHintListener {
            override fun onViewHint() {
                listener.impressionTrack(
                    model = element.channelModel,
                    position = bindingAdapterPosition,
                    coupons = element.coupons
                )
            }
        })
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_coupon_widget
    }
}
