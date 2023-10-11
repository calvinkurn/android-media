package com.tokopedia.product.manage.feature.list.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductArchivalInfo(
    @SerializedName("ProductarchivalGetProductArchiveInfo")
    @Expose
    val productarchivalGetProductArchiveInfo: ProductarchivalGetProductArchiveInfo
) {
    data class ProductarchivalGetProductArchiveInfo(
        @SerializedName("archiveTime")
        @Expose
        val archiveTime: String,
        @SerializedName("helpPageURL")
        @Expose
        val helpPageURL: String,
        @SerializedName("reason")
        @Expose
        val reason: String,
        @SerializedName("sellerEduArticleURL")
        @Expose
        val sellerEduArticleURL: String,
        @SerializedName("status")
        @Expose
        val status: Int
    )
}
