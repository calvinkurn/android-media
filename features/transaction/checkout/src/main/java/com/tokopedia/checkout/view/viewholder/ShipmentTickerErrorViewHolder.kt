package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker

class ShipmentTickerErrorViewHolder(itemView: View, val listener: ShipmentAdapterActionListener): RecyclerView.ViewHolder(itemView) {

    internal var data: ShipmentTickerErrorModel = ShipmentTickerErrorModel()
    private val ticker: Ticker? = itemView.findViewById(R.id.shipment_ticker_error)

    fun bind(shipmentTickerErrorModel: ShipmentTickerErrorModel) {
        data = shipmentTickerErrorModel
        if (isEmptyTicker()) {
            ticker?.gone()
        } else {
            ticker?.setTextDescription(shipmentTickerErrorModel.errorMessage)
            ticker?.visible()
            ticker?.post {
                ticker?.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                ticker?.requestLayout()
            }
            if (!shipmentTickerErrorModel.hasShown) {
                listener.onViewTickerPaymentError(shipmentTickerErrorModel.errorMessage)
                shipmentTickerErrorModel.hasShown = true
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