package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Talk (
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("createTime")
        @Expose
        val createTime: String = "",

        @SerializedName("createTimeFmt")
        @Expose
        val createTimeFmt: String = "",

        @SerializedName("isModerator")
        @Expose
        val isModerator: Int = 0,

        @SerializedName("isOwner")
        @Expose
        val isOwner: Int = 0,

        @SerializedName("isSeller")
        @Expose
        val isSeller: Int = 0,

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("rawMessage")
        @Expose
        val rawMessage: String = "",

        @SerializedName("shopId")
        @Expose
        val shopId: String = "",

        @SerializedName("shopImage")
        @Expose
        val shopImage: String = "",

        @SerializedName("shopName")
        @Expose
        val shopName: String = "",

        @SerializedName("totalComment")
        @Expose
        val totalComment: Int = 0,

        @SerializedName("userId")
        @Expose
        val userId: String = "",

        @SerializedName("userImage")
        @Expose
        val userImage: String = "",

        @SerializedName("userName")
        @Expose
        val userName: String = "",

        @SerializedName("userUrl")
        @Expose
        val userUrl: String = "",

        @SerializedName("commentList")
        @Expose
        val commentList: List<Talk> = listOf()
)
