package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailPaymentDataModel
import com.tokopedia.unifyprinciples.Typography

class EPharmacyOrderDetailPaymentViewHolder(
    val view: View,
    private val ePharmacyListener: EPharmacyListener?
) : AbstractViewHolder<EPharmacyOrderDetailPaymentDataModel>(view) {

    private val paymentModeValue = view.findViewById<Typography>(R.id.payment_mode_value)
    private val totalPrice = view.findViewById<Typography>(R.id.ep_total_harga_value)
    private val totalPayment = view.findViewById<Typography>(R.id.ep_total_bayar_value)
    private val helpLabel = view.findViewById<Typography>(R.id.pusat_bantuan_title)
    private val helpCaption = view.findViewById<Typography>(R.id.pusat_bantuan_description)
    private val helpView = view.findViewById<View>(R.id.help_view)

    companion object {
        val LAYOUT = R.layout.epharmacy_payment_detail_view_item
    }

    override fun bind(data: EPharmacyOrderDetailPaymentDataModel) {
        paymentModeValue.text = data.paymentMethod
        totalPrice.text = data.totalPrice
        totalPayment.text = data.totalPayment
        helpLabel.text = data.helpButton?.label
        helpCaption.text = data.helpButton?.caption
        helpView.setOnClickListener {
            if (data.helpButton?.appUrl.orEmpty().isNotBlank()) {
                ePharmacyListener?.onHelpButtonClicked(data.helpButton?.appUrl)
            }
        }
    }
}
