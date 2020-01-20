package com.tokopedia.topchat.chatlist.model


/**
 * @author : Steven 2019-08-09
 */
abstract class BaseIncomingItemWebSocketModel(open val messageId: String = "") {

    abstract fun getContactId(): String
    abstract fun getTag(): String

    companion object {
        const val ROLE_BUYER = "User"
        const val ROLE_SELLER = "Shop Owner"
    }
}