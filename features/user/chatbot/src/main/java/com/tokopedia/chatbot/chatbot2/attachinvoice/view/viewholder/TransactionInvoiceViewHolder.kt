package com.tokopedia.chatbot.chatbot2.attachinvoice.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.model.TransactionInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.util.helper.InvoiceStatusLabelHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class TransactionInvoiceViewHolder(itemView: View, private val listener: TransactionInvoiceViewHolderListener) :
    AbstractViewHolder<TransactionInvoiceUiModel>(itemView) {

    private val tvInvoiceDate: Typography = itemView.findViewById(R.id.tv_invoice_date)
    private val tvStatus: Label = itemView.findViewById(R.id.tv_status)
    private val tvInvoiceName: Typography = itemView.findViewById(R.id.tv_invoice_name)
    private val tvInvoiceDesc: Typography = itemView.findViewById(R.id.tv_invoice_desc)
    private val tvPrice: Typography = itemView.findViewById(R.id.tv_price)
    private val tvPricePrefix: Typography = itemView.findViewById(R.id.tv_price_prefix)
    private val ivThumbnail: ImageUnify = itemView.findViewById(R.id.iv_thumbnail)

    override fun bind(invoice: TransactionInvoiceUiModel) {
        ImageHandler.loadImageRounded2(itemView.context, ivThumbnail, invoice.imageUrl)
        setStatus(invoice)
        tvInvoiceName.text = invoice.title
        tvInvoiceDesc.text = invoice.description
        tvInvoiceDesc.showWithCondition(!invoice.description.isNullOrEmpty())
        setPrice(invoice.amount)
        tvInvoiceDate.text = invoice.createdTime
    }

    private fun setStatus(invoice: TransactionInvoiceUiModel) {
        if (invoice.status.isNotEmpty()) {
            var labelType: Int = if (invoice.color.isEmpty()) {
                InvoiceStatusLabelHelper.getLabelTypeWithStatusId(invoice.statusId)
            } else {
                InvoiceStatusLabelHelper.getLabelType(invoice.color)
            }
            tvStatus.text = invoice.status
            tvStatus.setLabelType(labelType)
            tvStatus.show()
        } else {
            tvStatus.invisible()
        }
    }

    private fun setPrice(totalAmount: String?) {
        if (totalAmount.isNullOrEmpty()) {
            tvPricePrefix.hide()
            tvPrice.hide()
        } else {
            tvPricePrefix.show()
            tvPrice.text = totalAmount
            tvPrice.show()
        }
    }

    companion object {
        var LAYOUT = R.layout.item_chatbot_invoice_transaction_card
    }
}

interface TransactionInvoiceViewHolderListener {
    fun onItemClick(invoice: TransactionInvoiceUiModel)
}
