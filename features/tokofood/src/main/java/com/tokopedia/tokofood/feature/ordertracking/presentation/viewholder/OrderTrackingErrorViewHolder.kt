package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingErrorBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.io.IOException

class OrderTrackingErrorViewHolder(itemView: View) :
    AbstractViewHolder<OrderTrackingErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_error
    }

    private val binding by viewBinding<ItemTokofoodOrderTrackingErrorBinding>()

    override fun bind(element: OrderTrackingErrorUiModel) {
        binding?.globalErrorOrderTracking?.run {
            setType(if (element.throwable is IOException) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR)
            setActionClickListener {
                //TODO
            }
        }
    }
}