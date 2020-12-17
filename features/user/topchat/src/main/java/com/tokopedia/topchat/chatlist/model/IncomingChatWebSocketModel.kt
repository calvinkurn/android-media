package com.tokopedia.topchat.chatlist.model

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo


/**
 * @author : Steven 2019-08-09
 */
data class IncomingChatWebSocketModel constructor(val msgId: String = "") : BaseIncomingItemWebSocketModel(msgId) {

    var message: String = ""
    var unreadCounter: Int = 0
    var time: String = ""
    var contact: ItemChatAttributesContactPojo? = null

    constructor(messageId: String,
                message: String,
                time: String,
                contact: ItemChatAttributesContactPojo) : this(messageId) {
        this.message = message
        this.time = time
        this.contact = contact
    }

    constructor(
            messageId: String,
            message: String,
            time: String
    ) : this(messageId) {
        this.message = message
        this.time = time
    }

    // fromUid
    override fun getContactId(): String {
        return contact?.contactId.toEmptyStringIfNull()
    }

    // role
    override fun getTag(): String {
        return contact?.tag.toEmptyStringIfNull()
    }

    fun isFromBuyer(userId: String): Boolean {
        return (getTag() == ROLE_BUYER &&
                getContactId() != userId)
    }

    fun isFromSeller(userId: String): Boolean {
        return (getTag() == ROLE_SELLER &&
                getContactId() != userId)
    }

    fun isFromMySelf(@RoleType role: Int, userId: String): Boolean {
        val fromRole = when (getTag()) {
            ROLE_BUYER -> RoleType.BUYER
            ROLE_SELLER -> RoleType.SELLER
            else -> null
        } ?: return false
        return fromRole == role && getContactId() == userId
    }

}