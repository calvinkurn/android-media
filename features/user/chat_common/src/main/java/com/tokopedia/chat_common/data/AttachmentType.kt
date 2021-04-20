package com.tokopedia.chat_common.data

/**
 * @author by nisie on 27/11/18.
 */
class AttachmentType {

    object Companion {
        //COMMON
        const val TYPE_IMAGE_ANNOUNCEMENT = "1"
        const val TYPE_IMAGE_UPLOAD = "2"
        const val TYPE_PRODUCT_ATTACHMENT = "3"
        const val TYPE_VOUCHER_ATTACHMENT = 11
        const val TYPE_STICKER = 21
        const val TYPE_REVIEW_REMINDER = "24"

        //TOPCHAT
        const val TYPE_IMAGE_DUAL_ANNOUNCEMENT = "4"
        const val TYPE_IMAGE_CAROUSEL = "991"
        const val TYPE_MESSAGE = "0"

        //CHATBOT
        const val TYPE_INVOICES_SELECTION = "6"
        const val TYPE_INVOICE_SEND = "7"
        const val TYPE_QUICK_REPLY = "8"
        const val TYPE_CHAT_BALLOON_ACTION = "9"
        const val TYPE_QUICK_REPLY_SEND = "10"
        const val TYPE_VOUCHER = "11"
        const val TYPE_QUOTATION = "19"

        const val TYPE_CHAT_RATING = "-1"
    }
}