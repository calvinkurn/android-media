package com.tokopedia.notifcenter.data.entity

/**
 * @author : Steven 11/04/19
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

