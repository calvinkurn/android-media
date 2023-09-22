package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.epharmacy.utils.EPharmacyUtils
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
    private val buyerOrderDetailIndicator = view.findViewById<View>(R.id.ep_buyer_order_indicator)

    companion object {
        val LAYOUT = R.layout.epharmacy_order_detail_header
    }

    override fun bind(data: EPharmacyOrderDetailHeaderDataModel) {
        setUpTicker(data.title, data.tickerMessage, data.tickerType.orZero())
        setUpInvoiceData(data.invoiceTitle,data.chatDate)
        setUpValidity(data.validUntil)
        setupIndicatorColor(data.indicatorColor.orEmpty())
    }

    private fun setUpInvoiceData(invoiceTitle: String?, chatDate: String?) {
        invoiceNumber.text = invoiceTitle
        purchaseDateValue.text = chatDate
    }

    private fun setUpValidity(validUntil: String?) {
        validUntilValue.displayTextOrHide(validUntil.orEmpty())
        if(validUntil?.isBlank() == true){
            this.validUntil.hide()
        }
    }

    private fun setUpTicker(title: String?, tickerMessage: String?, tickerType: Int) {
        this.title.text = title
        ticker.tickerTitle = tickerMessage
        ticker.tickerType = tickerType
    }

    private fun setupIndicatorColor(indicatorColor: String) {
        buyerOrderDetailIndicator?.background = EPharmacyUtils.getColoredIndicator(itemView.context, indicatorColor)
    }
}
