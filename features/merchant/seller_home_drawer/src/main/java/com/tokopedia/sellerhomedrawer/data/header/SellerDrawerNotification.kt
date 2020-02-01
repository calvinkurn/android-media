package com.tokopedia.sellerhomedrawer.data.header

import com.tokopedia.abstraction.common.utils.GlobalConfig


class SellerDrawerNotification {

    companion object {

        val CACHE_INBOX_MESSAGE = "CACHE_INBOX_MESSAGE"
        val CACHE_INBOX_TALK = "CACHE_INBOX_TALK"
        val CACHE_INBOX_REVIEW = "CACHE_INBOX_REVIEW"
        val CACHE_INBOX_TICKET = "CACHE_INBOX_TICKET"
        val CACHE_INBOX_SELLER_INFO = "CACHE_INBOX_SELLER_INFO"
        val CACHE_INBOX_RESOLUTION_CENTER_BUYER = "CACHE_INBOX_RESOLUTION_CENTER_BUYER"
        val CACHE_INBOX_RESOLUTION_CENTER_SELLER = "CACHE_INBOX_RESOLUTION_CENTER_SELLER"

        val CACHE_PURCHASE_ORDER_STATUS = "CACHE_PURCHASE_ORDER_STATUS"
        val CACHE_PURCHASE_PAYMENT_CONF = "CACHE_PURCHASE_PAYMENT_CONF"
        val CACHE_PURCHASE_PAYMENT_CONFIRM = "CACHE_PURCHASE_PAYMENT_CONFIRM"
        val CACHE_PURCHASE_DELIVERY_CONFIRM = "CACHE_PURCHASE_DELIVERY_CONFIRM"
        val CACHE_PURCHASE_REORDER = "CACHE_PURCHASE_REORDER"

        val CACHE_SELLING_NEW_ORDER = "CACHE_SELLING_NEW_ORDER"
        val CACHE_SELLING_SHIPPING_CONFIRMATION = "CACHE_SELLING_SHIPPING_CONFIRMATION"
        val CACHE_SELLING_SHIPPING_STATUS = "CACHE_SELLING_SHIPPING_STATUS"

        val CACHE_TOTAL_NOTIF = "CACHE_TOTAL_NOTIF"
        val CACHE_INCR_NOTIF = "CACHE_INCR_NOTIF"
        val CACHE_TOTAL_CART = "CACHE_INCR_NOTIF"

        /**
         * moved to TransactionConstant
         */
        @Deprecated("")
        val IS_HAS_CART = "IS_HAS_CART"

        val CACHE_PURCHASE_CONFIRMED = "CACHE_PURCHASE_CONFIRMED"
        val CACHE_PURCHASE_PROCESSED = "CACHE_PURCHASE_PROCESSED"
        val CACHE_PURCHASE_SHIPPED = "CACHE_PURCHASE_SHIPPED"
        val CACHE_PURCHASE_DELIVERED = "CACHE_PURCHASE_DELIVERED"
    }
    var inboxMessage: Int = 0
    var inboxTalk: Int = 0
    var inboxReview: Int = 0
    var inboxTicket: Int = 0

    var inboxResCenter: Int = 0
    var purchaseOrderStatus: Int = 0
    var purchasePaymentConfirm: Int = 0
    var purchaseDeliveryConfirm: Int = 0

    var purchaseReorder: Int = 0
    var sellingNewOrder: Int = 0
    var sellingShippingConfirmation: Int = 0
    var sellingShippingStatus: Int = 0
    var totalNotif: Int = 0
        get() = if (GlobalConfig.isSellerApp())
            field - totalPurchaseNotif
        else
            field
    var incrNotif: Int = 0

    var totalCart: Int = 0

    val totalPurchaseNotif: Int
        get() = purchasePaymentConfirm + purchaseDeliveryConfirm +
                purchaseOrderStatus + purchaseReorder

    val isUnread: Boolean
        get() = incrNotif > 0

    val totalInboxNotif: Int
        get() = inboxMessage + inboxTalk + inboxReview + inboxResCenter + inboxTicket

    val totalSellingNotif: Int
        get() = sellingNewOrder + sellingShippingConfirmation + sellingShippingStatus

    init {
        this.inboxMessage = 0
        this.inboxTalk = 0
        this.inboxReview = 0
        this.inboxTicket = 0
        this.inboxResCenter = 0

        this.purchaseOrderStatus = 0
        this.purchasePaymentConfirm = 0
        this.purchaseDeliveryConfirm = 0
        this.purchaseReorder = 0

        this.sellingNewOrder = 0
        this.sellingShippingConfirmation = 0
        this.sellingShippingStatus = 0
    }
}
