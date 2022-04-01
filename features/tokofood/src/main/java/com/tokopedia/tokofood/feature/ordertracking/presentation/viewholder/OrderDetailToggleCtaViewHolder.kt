package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingOrderDetailToggleBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel

class OrderDetailToggleCtaViewHolder(
    view: View,
    private val orderTrackingListener: OrderTrackingListener
) : AbstractViewHolder<OrderDetailToggleCtaUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_order_detail_toggle

        const val PAYLOAD_UPDATE_TOGGLE = 859
        const val NO_ROTATION = 0F
        const val REVERSE_ROTATION = 180F
    }

    private val binding = ItemTokofoodOrderTrackingOrderDetailToggleBinding.bind(itemView)

    override fun bind(element: OrderDetailToggleCtaUiModel) {
        with(binding) {
            setOrderDetailToggleTitle(element.isExpand)
            setOrderDetailToggleIcon(element.isExpand)
        }
    }

    private fun ItemTokofoodOrderTrackingOrderDetailToggleBinding.setOrderDetailToggleTitle(isExpand: Boolean) {
        tvOrderDetailToggleTitle.text = if (isExpand)
            getString(R.string.order_detail_collapse_cta_label) else getString(R.string.order_detail_expand_cta_label)

        tvOrderDetailToggleTitle.setOnClickListener {
            setToggleCtaListener(isExpand)
        }
    }

    private fun ItemTokofoodOrderTrackingOrderDetailToggleBinding.setOrderDetailToggleIcon(isExpand: Boolean) {
        icOrderDetailToggle.rotation = if (isExpand) REVERSE_ROTATION else NO_ROTATION
        icOrderDetailToggle.setOnClickListener {
            setToggleCtaListener(isExpand)
        }
    }

    private fun setToggleCtaListener(isExpand: Boolean) {
        if (isExpand) {
            orderTrackingListener.onToggleClicked(false)
        } else {
            orderTrackingListener.onToggleClicked(true)
        }
    }

    override fun bind(element: OrderDetailToggleCtaUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if (payloads.isNullOrEmpty() || element == null) return

        when (payloads.getOrNull(Int.ZERO) as? Int) {
            PAYLOAD_UPDATE_TOGGLE -> {
                binding.run {
                    setOrderDetailToggleTitle(element.isExpand)
                    setOrderDetailToggleIcon(element.isExpand)
                }
            }
        }
    }

    interface OrderDetailToggleCtaListener {
        fun onToggleClicked(isExpandable: Boolean)
    }
}