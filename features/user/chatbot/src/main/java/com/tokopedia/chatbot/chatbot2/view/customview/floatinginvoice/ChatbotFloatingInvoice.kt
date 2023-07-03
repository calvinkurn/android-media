package com.tokopedia.chatbot.chatbot2.view.customview.floatinginvoice

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotSendButtonListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ChatbotFloatingInvoice(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var invoiceImage: ImageUnify? = null
    private var invoiceName: Typography? = null
    private var cancelInvoice: ImageUnify? = null
    private var invoiceLabel: Label? = null
    private var invoice: ConstraintLayout? = null

    var sendButtonListener: ChatbotSendButtonListener? = null
    var invoiceListener: InvoiceListener? = null

    init {
        initViewBindings()
        bindClickListeners()
    }

    private fun bindClickListeners() {
        cancelInvoice?.setOnClickListener {
            sendButtonListener?.enableSendButton()
            invoiceListener?.isInvoiceRemoved(true)
            invoice?.hide()
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

    fun setUpInvoiceData(invoiceTitle: String?, invoiceIconURL: String?, labelType: Int, labelText: String?) {
        if (invoiceTitle != null) {
            invoiceName?.text = invoiceTitle
        }
        invoiceLabel?.text = labelText
        invoiceLabel?.setLabelType(labelType)
        if (invoiceIconURL != null) {
            ImageHandler.loadImage(
                context,
                invoiceImage,
                invoiceIconURL,
                R.drawable.ic_retry_image_send
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.customview_chatbot_floating_invoice
    }

    interface InvoiceListener {
        fun isInvoiceRemoved(isRemoved: Boolean)
    }
}
