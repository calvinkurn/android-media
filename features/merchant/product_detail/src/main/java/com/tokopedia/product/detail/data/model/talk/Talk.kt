package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Talk(
        @SerializedName("create_time")
        @Expose
        val createTime: CreateTime = CreateTime(),

        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("total_comment")
        @Expose
        val totalComment: String = "",

        @SerializedName("user")
        @Expose
        val user: User = User()
)