package com.tokopedia.product.detail.data.model.review_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 16/02/23"
 * Project name: android-tokopedia-core
 **/

data class ReviewListData(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("applink")
    @Expose
    val appLink: String = "",
    @SerializedName("applinkTitle")
    @Expose
    val appLinkTitle: String = "",
    @SerializedName("data")
    @Expose
    val data: List<Review> = emptyList()
) {

    data class Review(
        @SerializedName("userImage")
        @Expose
        val userImage: String = "",
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("userTitle")
        @Expose
        val userTitle: String = "",
        @SerializedName("userSubtitle")
        @Expose
        val userSubtitle: String = "",
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("applink")
        @Expose
        val appLink: String = ""
    )
}
