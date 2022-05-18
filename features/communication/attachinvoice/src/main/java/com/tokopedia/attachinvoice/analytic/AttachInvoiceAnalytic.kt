package com.tokopedia.attachinvoice.analytic

import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class AttachInvoiceAnalytic @Inject constructor() {

    object Name {
        const val CHAT_DETAIL = "clickChatDetail"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
    }

    object Action {
        const val CLICK_ATTACH_INVOICE = "click lampirkan invoice"
    }

    // #AIC3
    fun trackOnAttachInvoice(invoice: Invoice) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        Name.CHAT_DETAIL,
                        Category.CHAT_DETAIL,
                        Action.CLICK_ATTACH_INVOICE,
                        invoice.id.toString()
                )
        )
    }
}