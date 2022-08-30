package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingOrderDetailToggleBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel

class OrderDetailToggleCtaViewHolder(
    view: View,
    private val orderTrackingListener: OrderTrackingListener
) : CustomPayloadViewHolder<OrderDetailToggleCtaUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_order_detail_toggle

        const val NO_ROTATION = 0F
        const val REVERSE_ROTATION = 180F
    }

    private val binding = ItemTokofoodOrderTrackingOrderDetailToggleBinding.bind(itemView)

    override fun bind(element: OrderDetailToggleCtaUiModel) {
        with(binding) {
            setOrderDetailToggleTitle(element)
            setOrderDetailToggleIcon(element)
        }
    }

    private fun ItemTokofoodOrderTrackingOrderDetailToggleBinding.setOrderDetailToggleTitle(
        element: OrderDetailToggleCtaUiModel
    ) {
        tvOrderDetailToggleTitle.text = if (element.isExpand)
            getString(R.string.order_detail_collapse_cta_label) else getString(R.string.order_detail_expand_cta_label)

        tvOrderDetailToggleTitle.setOnClickListener {
            setToggleCtaListener(element)
        }
    }

    private fun ItemTokofoodOrderTrackingOrderDetailToggleBinding.setOrderDetailToggleIcon(
        element: OrderDetailToggleCtaUiModel
    ) {
        icOrderDetailToggle.rotation = if (element.isExpand) REVERSE_ROTATION else NO_ROTATION
        icOrderDetailToggle.setOnClickListener {
            setToggleCtaListener(element)
        }
    }

    private fun setToggleCtaListener(item: OrderDetailToggleCtaUiModel) {
        if (item.isExpand) {
            orderTrackingListener.onToggleClicked(item, false)
        } else {
            orderTrackingListener.onToggleClicked(item, true)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is OrderDetailToggleCtaUiModel && newItem is OrderDetailToggleCtaUiModel) {
                if (oldItem != newItem) {
                    binding.run {
                        setOrderDetailToggleTitle(newItem)
                        setOrderDetailToggleIcon(newItem)
                    }
                }
            }
        }
    }

    interface OrderDetailToggleCtaListener {
        fun onToggleClicked(
            orderDetailToggleCta: OrderDetailToggleCtaUiModel,
            isExpandable: Boolean
        )
    }
}