package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils.getGlobalErrorType
import com.tokopedia.buyerorderdetail.databinding.OwocErrorStateBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel

class OwocErrorStateViewHolder(
    view: View,
    private val listener: Listener
) : AbstractViewHolder<PofErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.owoc_error_state
    }

    private val binding = OwocErrorStateBinding.bind(itemView)

    override fun bind(element: PofErrorUiModel) {
        with(binding) {
            globalErrorStateOwoc.run {
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

