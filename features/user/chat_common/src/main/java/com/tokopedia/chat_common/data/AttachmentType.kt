package com.tokopedia.chat_common.data

/**
 * @author by nisie on 27/11/18.
 */
open class AttachmentType {

    object Companion {
        val TYPE_CHAT_RATING = "-1"
        val TYPE_IMAGE_ANNOUNCEMENT = "1"
        public val TYPE_IMAGE_UPLOAD = "2"
        val TYPE_PRODUCT_ATTACHMENT = "3"
        val TYPE_IMAGE_DUAL_ANNOUNCEMENT = "4"
        val TYPE_INVOICES_SELECTION = "6"
        val TYPE_INVOICE_SEND = "7"
        val TYPE_QUICK_REPLY = "8"
        val TYPE_CHAT_BALLOON_ACTION = "9"
    }
}