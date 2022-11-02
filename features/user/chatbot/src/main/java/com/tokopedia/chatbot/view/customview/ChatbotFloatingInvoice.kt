package com.tokopedia.chatbot.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ChatbotFloatingInvoice(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private lateinit var invoiceImage: ImageUnify
    private lateinit var invoiceName: Typography
    private lateinit var cancelInvoice: ImageUnify
    private lateinit var invoiceLabel: Label
    private lateinit var invoice: ConstraintLayout

    var sendButtonListener : ChatbotSendButtonListener? = null
    var invoiceListener : InvoiceListener? = null

    init {
        initViewBindings()
        bindClickListeners()
    }

    private fun bindClickListeners() {
        cancelInvoice.setOnClickListener {
            sendButtonListener?.enableSendButton()
            invoiceListener?.isInvoiceRemoved(true)
            invoice.hide()
        }
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            invoice = findViewById(R.id.cl_chat_bubble)
            invoiceName = findViewById(R.id.invoice_name)
            cancelInvoice = findViewById(R.id.invoice_cancel)
            invoiceLabel = findViewById(R.id.invoice_status)
            invoiceImage = findViewById(R.id.invoice_image)
        }
    }

    fun setUpInvoiceData(invoiceTitle: String?, invoiceIconURL: String?, labelType : Int, labelText : String?) {
        if (invoiceTitle != null)
            invoiceName.text = invoiceTitle
        invoiceLabel.text = labelText
        invoiceLabel.setLabelType(labelType)
        if (invoiceIconURL != null)
            ImageHandler.loadImage(
                context,
                invoiceImage,
                invoiceIconURL,
                R.drawable.ic_retry_image_send
            )
    }

    companion object {
        val LAYOUT = R.layout.attached_invoice_float_chat_item
    }

    interface InvoiceListener {
        fun isInvoiceRemoved(isRemoved : Boolean)
    }

}