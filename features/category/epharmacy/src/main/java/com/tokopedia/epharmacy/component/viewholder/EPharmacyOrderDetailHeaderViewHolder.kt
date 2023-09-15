package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class EPharmacyOrderDetailHeaderViewHolder(
    val view: View
) : AbstractViewHolder<EPharmacyOrderDetailHeaderDataModel>(view) {

    private val title = view.findViewById<Typography>(R.id.ep_order_status_description)
    private val ticker = view.findViewById<Ticker>(R.id.ep_order_ticker)
    private val invoiceNumber = view.findViewById<Typography>(R.id.ep_invoice_number)
    private val purchaseDateValue = view.findViewById<Typography>(R.id.ep_purchase_date_value)
    private val validUntil = view.findViewById<Typography>(R.id.ep_time_valid_until)
    private val validUntilValue = view.findViewById<Typography>(R.id.ep_time_valid_until_value)

    companion object {
        val LAYOUT = R.layout.epharmacy_order_detail_header
    }

    override fun bind(data: EPharmacyOrderDetailHeaderDataModel) {
        title.text = data.title
        ticker.tickerTitle = data.tickerMessage
        ticker.tickerType = data.tickerType.orZero()
        invoiceNumber.text = data.invoiceTitle
        purchaseDateValue.text = data.chatDate
        validUntilValue.displayTextOrHide(data.validUntil)
        if(data.validUntil.isEmpty()){
            validUntil.hide()
        }
    }
}
