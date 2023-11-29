package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofErrorStateBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofErrorStateUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PofErrorStateViewHolder(
    view: View,
    private val listener: PofAdapterTypeFactory.Listener
) : AbstractViewHolder<PofErrorStateUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_error_state
    }

    private val binding = ItemPofErrorStateBinding.bind(view)

    init {
        setupListener()
    }

    override fun bind(element: PofErrorStateUiModel) {
        setupErrorState(element.throwable)
    }

    override fun bind(element: PofErrorStateUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupListener() {
        binding.root.setActionClickListener { listener.onEvent(UiEvent.ClickRetryOnErrorState) }
    }

    @SuppressLint("SetTextI18n")
    private fun setupErrorState(throwable: Throwable) {
        with(binding.root) {
            setType(
                if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
                    GlobalError.NO_CONNECTION
                } else {
                    GlobalError.SERVER_ERROR
                }
            )
        }
    }
}
