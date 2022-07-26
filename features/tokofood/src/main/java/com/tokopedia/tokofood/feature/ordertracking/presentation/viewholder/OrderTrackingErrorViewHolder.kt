package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.TokofoodExt.getPostPurchaseGlobalErrorType
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingErrorBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel

class OrderTrackingErrorViewHolder(itemView: View, private val listener: Listener) :
    AbstractViewHolder<OrderTrackingErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_error
    }

    private val binding = ItemTokofoodOrderTrackingErrorBinding.bind(itemView)

    override fun bind(element: OrderTrackingErrorUiModel) {
        with(binding) {
            globalErrorOrderTracking.run {
                setType(element.throwable.getPostPurchaseGlobalErrorType())
                setPrimaryActionText()
                setActionClickListener {
                    listener.onErrorActionClicked()
                }
            }
        }
    }

    private fun GlobalError.setPrimaryActionText() {
        errorAction.text = getString(com.tokopedia.tokofood.R.string.global_error_order_tracking_primary_action_text)
    }

    interface Listener {
        fun onErrorActionClicked()
    }
}