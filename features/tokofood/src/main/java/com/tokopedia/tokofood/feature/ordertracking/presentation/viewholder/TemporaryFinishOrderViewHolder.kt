package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.constants.ImageUrl
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingTemporaryFinishBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TemporaryFinishOrderUiModel

class TemporaryFinishOrderViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<TemporaryFinishOrderUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_temporary_finish
    }

    private val binding = ItemTokofoodOrderTrackingTemporaryFinishBinding.bind(itemView)

    override fun bind(element: TemporaryFinishOrderUiModel) {
        with(binding) {
            setTemporaryFinishImage(ImageUrl.OrderTracking.TEMPORARY_FINISH_URL)
        }
        autoRefreshTempFinishOrder(element.orderDetailResultUiModel)
    }

    private fun ItemTokofoodOrderTrackingTemporaryFinishBinding.setTemporaryFinishImage(imageUrl: String) {
        ivTemporaryFinish.setImageUrl(imageUrl)
    }

    private fun autoRefreshTempFinishOrder(orderDetailResultUiModel: OrderDetailResultUiModel) {
        listener.onAutoRefreshTempFinishOrder(orderDetailResultUiModel)
    }

    interface Listener {
        fun onAutoRefreshTempFinishOrder(orderDetailResultUiModel: OrderDetailResultUiModel)
    }
}