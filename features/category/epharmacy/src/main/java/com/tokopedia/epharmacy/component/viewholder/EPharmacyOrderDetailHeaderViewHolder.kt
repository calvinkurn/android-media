package com.tokopedia.epharmacy.component.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class EPharmacyOrderDetailHeaderViewHolder(
    val view: View,
    private val ePharmacyListener: EPharmacyListener?
) : AbstractViewHolder<EPharmacyOrderDetailHeaderDataModel>(view) {

    private val title = view.findViewById<Typography>(R.id.ep_order_status_description)
    private val ticker = view.findViewById<Ticker>(R.id.ep_order_ticker)
    private val invoiceNumberText = view.findViewById<Typography>(R.id.ep_invoice_number)
    private val copyInvoiceNumber = view.findViewById<IconUnify>(R.id.ep_invoice_copy)
    private val viewInvoice = view.findViewById<Typography>(R.id.ep_lihat_invoice)
    private val purchaseDate = view.findViewById<Typography>(R.id.ep_purchase_date)
    private val purchaseDateValue = view.findViewById<Typography>(R.id.ep_purchase_date_value)
    private val validUntil = view.findViewById<Typography>(R.id.ep_time_valid_until)
    private val validUntilValue = view.findViewById<Typography>(R.id.ep_time_valid_until_value)
    private val chatStartDate = view.findViewById<Typography>(R.id.ep_chat_start_date)
    private val chatStartDateValue = view.findViewById<Typography>(R.id.ep_chat_start_date_value)
    private val buyerOrderDetailIndicator = view.findViewById<View>(R.id.ep_buyer_order_indicator)

    companion object {
        val LAYOUT = R.layout.epharmacy_order_detail_header
        const val CLIPBOARD_TAG = "Invoice Number"
    }

    override fun bind(data: EPharmacyOrderDetailHeaderDataModel) {
        setUpTicker(data.title, data.tickerMessage, data.tickerType.orZero())
        setUpInvoiceData(data.invoiceNumber, data.paymentDate)
        setUpInvoiceClipBoard(data.invoiceNumber)
        setUpInvoiceLink(data.invoiceLink)
        setUpValidity(data.validUntil)
        setUpChatStartDate(data.chatStartDate)
        setupIndicatorColor(data.indicatorColor.orEmpty())
    }

    private fun setUpInvoiceClipBoard(invoiceNumber: String?) {
        if (invoiceNumber.isNullOrEmpty()) {
            copyInvoiceNumber.hide()
        }
        copyInvoiceNumber.setOnClickListener {
            (view.context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        CLIPBOARD_TAG,
                        invoiceNumber
                    )
                )
            }
        }
    }

    private fun setUpInvoiceLink(invoiceLink: String?) {
        if (invoiceLink.isNullOrBlank()) {
            viewInvoice.hide()
        }
        viewInvoice.setOnClickListener {
            if (invoiceLink?.isNotBlank().orFalse()) {
                ePharmacyListener?.onLihatInvoiceClicked(invoiceLink)
            }
        }
    }

    private fun setUpInvoiceData(invoiceNumber: String?, paymentDate: String?) {
        invoiceNumberText.displayTextOrHide(invoiceNumber.orEmpty())
        purchaseDateValue.displayTextOrHide(paymentDate.orEmpty())
        if (paymentDate?.isNotBlank().orFalse()) {
            purchaseDate.show()
        }
    }

    private fun setUpValidity(validUntil: String?) {
        validUntilValue.displayTextOrHide(validUntil.orEmpty())
        if (validUntil?.isNotBlank().orFalse()) {
            this.validUntil.show()
        }
    }

    private fun setUpChatStartDate(chatStartDate: String?) {
        chatStartDateValue.displayTextOrHide(chatStartDate.orEmpty())
        if (chatStartDate?.isNotBlank().orFalse()) {
            this.chatStartDate.show()
        }
    }

    private fun setUpTicker(title: String?, tickerMessage: String?, tickerType: Int) {
        this.title.text = title
        if (tickerMessage?.isNotBlank().orFalse()) {
            ticker.show()
            ticker.setHtmlDescription(tickerMessage.orEmpty())
            ticker.tickerType = tickerType
        } else {
            ticker.hide()
        }
    }

    private fun setupIndicatorColor(indicatorColor: String) {
        buyerOrderDetailIndicator?.background = EPharmacyUtils.getColoredIndicator(itemView.context, indicatorColor)
    }
}
