package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingEstimationBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel

class OrderTrackingEstimationViewHolder(itemView: View):
    AbstractViewHolder<OrderTrackingEstimationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_estimation
    }

    private val binding = ItemTokofoodOrderTrackingEstimationBinding.bind(itemView)

    override fun bind(element: OrderTrackingEstimationUiModel) {
        with(binding) {
            tvOrderTrackingEstimation.text = MethodChecker.fromHtml(root.context.getString(
                R.string.order_tracking_estimation_time, element.estimationLabel,
                element.estimationTime
            ))
        }
    }
}