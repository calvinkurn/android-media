package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils.getGlobalErrorType
import com.tokopedia.buyerorderdetail.databinding.PartialOrderFulfillmentErrorBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel

class PofErrorViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<PofErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_order_fulfillment_error
    }

    private val binding = PartialOrderFulfillmentErrorBinding.bind(itemView)

    override fun bind(element: PofErrorUiModel) {
        with(binding) {
            globalErrorPartialOrderFulfillment.run {
                setType(element.throwable.getGlobalErrorType())
                setActionClickListener {
                    listener.onErrorActionClicked()
                }
            }
        }
    }

    interface Listener {
        fun onErrorActionClicked()
    }
}
