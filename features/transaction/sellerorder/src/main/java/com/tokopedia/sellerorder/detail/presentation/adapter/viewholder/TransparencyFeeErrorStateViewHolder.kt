package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils.getGlobalErrorType
import com.tokopedia.sellerorder.databinding.ItemTransparencyFeeErrorStateBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLoadingUiModel

class TransparencyFeeErrorStateViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<TransparencyFeeErrorStateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_transparency_fee_error_state
    }

    private val binding = ItemTransparencyFeeErrorStateBinding.bind(itemView)
    override fun bind(element: TransparencyFeeErrorStateUiModel) {
        binding.errorSateTransparancyFee.run {
            setType(element.throwable.getGlobalErrorType())
            setActionClickListener {
                listener.onErrorActionClicked()
            }
        }
    }

    interface Listener {
        fun onErrorActionClicked()
    }
}
