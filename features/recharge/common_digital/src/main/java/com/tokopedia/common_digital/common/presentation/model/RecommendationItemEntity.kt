package com.tokopedia.common_digital.common.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 14/11/18.
 */
class RecommendationItemEntity {

    @SerializedName("iconUrl")
    @Expose
    val iconUrl: String = ""

    @SerializedName("title")
    @Expose
    val title: String = ""

    @SerializedName("clientNumber")
    @Expose
    val clientNumber: String = ""

    @SerializedName("appLink")
    @Expose
    val applink: String = ""

    @SerializedName("webLink")
    @Expose
    val webLink: String = ""

    @SerializedName("categoryId")
    @Expose
    val categoryId: Int = 0

    @SerializedName("categoryName")
    @Expose
    val categoryName: String = ""

    @SerializedName("productId")
    @Expose
    val productId: Int = 0

    @SerializedName("productName")
    @Expose
    val productName: String = ""

    @SerializedName("productPrice")
    @Expose
    val productPrice: Float = 0f

    @SerializedName("type")
    @Expose
    val type: String = ""

    @SerializedName("position")
    @Expose
    val position: Int = 0

    @SerializedName("tag")
    @Expose
    val tag: String = ""

    @SerializedName("tagType")
    @Expose
    val tagType: Int = 0

    @SerializedName("description")
    @Expose
    val description: String = ""

}
