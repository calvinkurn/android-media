package com.tokopedia.inbox.domain.data.notification


import com.google.gson.annotations.SerializedName
import com.tokopedia.inboxcommon.RoleType

data class InboxCounter(
        @SerializedName("all")
        val all: All = All(),
        @SerializedName("buyer")
        val buyer: Buyer = Buyer(),
        @SerializedName("seller")
        val seller: Seller = Seller(),
        @SerializedName("seller_discussion_unresponded")
        val sellerDiscussionUnresponded: SellerDiscussionUnresponded = SellerDiscussionUnresponded()
) {

    fun getByRole(role: Int): BaseNotification? {
        return when (role) {
            RoleType.BUYER -> buyer
            RoleType.SELLER -> seller
            else -> null
        }
    }

    fun getByRoleOpposite(role: Int): BaseNotification? {
        return when (role) {
            RoleType.BUYER -> seller
            RoleType.SELLER -> buyer
            else -> null
        }
    }

    fun updateTotalDiscussion() {
        seller.talkInt = sellerDiscussionUnresponded.totalInt
    }
}