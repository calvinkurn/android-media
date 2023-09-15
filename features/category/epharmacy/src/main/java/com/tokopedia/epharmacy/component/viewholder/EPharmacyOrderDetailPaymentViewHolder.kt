package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailPaymentDataModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class EPharmacyOrderDetailPaymentViewHolder(
    val view: View
) : AbstractViewHolder<EPharmacyOrderDetailPaymentDataModel>(view) {

    private val paymentModeValue = view.findViewById<Typography>(R.id.payment_mode_value)
    private val totalPrice = view.findViewById<Typography>(R.id.ep_total_harga_value)
    private val totalPayment = view.findViewById<Typography>(R.id.ep_total_bayar_value)

    companion object {
        val LAYOUT = R.layout.epharmacy_payment_detail_view_item
    }

    override fun bind(data: EPharmacyOrderDetailPaymentDataModel) {
        paymentModeValue.text = data.paymentMethod
        totalPrice.text = data.totalPrice
        totalPayment.text = data.totalPayment
    }
}
