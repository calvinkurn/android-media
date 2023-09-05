package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentTickerErrorBinding
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

class ShipmentTickerErrorViewHolder(private val binding: ItemShipmentTickerErrorBinding) : RecyclerView.ViewHolder(binding.root) {

    internal var data: ShipmentTickerErrorModel = ShipmentTickerErrorModel()

    fun bind(shipmentTickerErrorModel: ShipmentTickerErrorModel) {
        data = shipmentTickerErrorModel
        if (isEmptyTicker()) {
            binding.shipmentTickerError.gone()
        } else {
            binding.shipmentTickerError.setTextDescription(shipmentTickerErrorModel.errorMessage)
            binding.shipmentTickerError.visible()
            binding.shipmentTickerError.post {
                binding.shipmentTickerError.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                binding.shipmentTickerError.requestLayout()
            }
        }
    }

    internal fun isEmptyTicker(): Boolean {
        return data.errorMessage.isEmpty()
    }

    companion object {
        @JvmStatic
        val ITEM_VIEW_SHIPMENT_TICKER_ERROR = R.layout.item_shipment_ticker_error
    }
}
