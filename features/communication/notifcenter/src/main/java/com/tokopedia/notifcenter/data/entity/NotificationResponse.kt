package com.tokopedia.notifcenter.data.entity


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.tokopedia.inboxcommon.RoleType

data class NotificationResponse(
    @SerializedName("notifications")
    val notifications: Notifications = Notifications()
)

data class Notifications(
    @SerializedName("inbox_counter")
    val inboxCounter: InboxCounter = InboxCounter(),
    @SerializedName("total_cart")
    val totalCart: Int = 0
)

data class InboxCounter(
    @SerializedName("buyer")
    val buyer: Buyer = Buyer(),
    @SerializedName("seller")
    val seller: Seller = Seller()
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
}

abstract class BaseNotification {
    @SerializedName("notifcenter_int")
    var notifcenterInt: Int = 0
}

@Keep
class Buyer : BaseNotification()

@Keep
class Seller : BaseNotification()
