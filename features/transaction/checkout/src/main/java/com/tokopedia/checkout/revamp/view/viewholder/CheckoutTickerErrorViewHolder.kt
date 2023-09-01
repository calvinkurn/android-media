package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutTickerErrorBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

class CheckoutTickerErrorViewHolder(private val binding: ItemCheckoutTickerErrorBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: CheckoutTickerErrorModel) {
        if (data.isError) {
            binding.shipmentTickerError.setTextDescription(data.errorMessage)
            binding.shipmentTickerError.visible()
            binding.shipmentTickerError.post {
                binding.shipmentTickerError.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                binding.shipmentTickerError.requestLayout()
            }
        } else {
            binding.shipmentTickerError.gone()
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_ticker_error
    }
}
