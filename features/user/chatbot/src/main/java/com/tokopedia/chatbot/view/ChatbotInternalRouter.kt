package com.tokopedia.chatbot.view

import android.content.Context
import android.content.Intent
import com.tokopedia.chatbot.attachinvoice.view.activity.AttachInvoiceActivity

/**
 * @author by nisie on 07/12/18.
 */
open class ChatbotInternalRouter {

    object Companion {

        const val TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE"

        fun getAttachInvoiceIntent(context: Context, userId: String, messageId: Int): Intent {
            return AttachInvoiceActivity.createInstance(context, userId, messageId)
        }
    }
}