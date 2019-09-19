package com.tokopedia.navigation.domain.pojo

/**
 * @author : Steven 11/04/19
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationCenterDetail(
        @Expose
        @SerializedName("notifcenter_detail")
        var pojo: NotificationUpdatePojo = NotificationUpdatePojo()
)
data class NotificationUpdatePojo(
        @Expose
        @SerializedName("paging")
        var paging: Paging = Paging(),
        @Expose
        @SerializedName("list")
        var list: List<NotificationUpdateItem> = arrayListOf(),
        @SerializedName("user_info")
        @Expose
        var userInfo: UserInfo = UserInfo()
)

data class Paging(
        @Expose
        @SerializedName("has_next")
        val hasNext: Boolean = false,
        @Expose
        @SerializedName("has_prev")
        val hasPrev: Boolean = false
)

data class UserInfo(
        @Expose
        @SerializedName("user_id")
        val userId: String = "",
        @Expose
        @SerializedName("shop_id")
        val shopId: String = "",
        @Expose
        @SerializedName("email")
        val email: String = "",
        @Expose
        @SerializedName("fullname")
        val fullName: String = ""
) {
        fun hasShop() : Boolean {
                return shopId != "0"
        }
}

