package com.tokopedia.chatbot.attachinvoice.view.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel

/**
 * Created by Hendri on 22/03/18.
 */

class InvoiceViewHolder(itemView: View) : AbstractViewHolder<InvoiceViewModel>(itemView) {
    private val invoiceNo: TextView
    private val invoiceDate: TextView
    private val productName: TextView
    private val productDesc: TextView
    private val invoiceStatus: TextView
    private val totalAmount: TextView
    private val productImage: ImageView

    init {
        invoiceNo = itemView.findViewById(R.id.attach_invoice_item_invoice_no)
        invoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date)
        productName = itemView.findViewById(R.id.attach_invoice_item_product_name)
        productDesc = itemView.findViewById(R.id.attach_invoice_item_product_desc)
        invoiceStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status)
        totalAmount = itemView.findViewById(R.id.attach_invoice_item_invoice_total)
        productImage = itemView.findViewById(R.id.attach_invoice_item_product_image)
    }


    override fun bind(element: InvoiceViewModel) {
        invoiceNo.text = element.invoiceNumber
        if (element.productTopImage != null && !TextUtils.isEmpty(element.productTopImage)) {
            productImage.visibility = View.VISIBLE
            ImageHandler.loadImageAndCache(productImage, element.productTopImage)
        } else {
            productImage.visibility = View.GONE
        }
        invoiceDate.text = element.date
        productName.text = element.productTopName
        productDesc.text = element.description
        invoiceStatus.text = element.status
        totalAmount.text = element.total
    }

    companion object {
        var LAYOUT = R.layout.item_invoice_attach
    }
}
