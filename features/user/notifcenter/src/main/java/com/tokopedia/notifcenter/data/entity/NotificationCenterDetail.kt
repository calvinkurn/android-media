package com.tokopedia.notifcenter.data.entity

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

data class NotificationCenterSingleDetail(
        @Expose
        @SerializedName("notifcenter_singleList")
        var pojo: NotificationUpdatePojo = NotificationUpdatePojo()
)

data class NotificationUpdatePojo(
        @Expose @SerializedName("paging") var paging: Paging = Paging(),
        @Expose @SerializedName("list") var list: List<NotificationUpdateItem> = arrayListOf(),
        @Expose @SerializedName("options") var options: NotificationOptions = NotificationOptions(),
        @Expose @SerializedName("user_info") var userInfo: UserInfo = UserInfo()
)

data class UserInfo(
        @Expose @SerializedName("user_id") val userId: String = "",
        @Expose @SerializedName("shop_id") val shopId: String = "",
        @Expose @SerializedName("email") val email: String = "",
        @Expose @SerializedName("fullname") val fullName: String = ""
) {
        fun hasShop() : Boolean {
                return shopId != "0"
        }
}

