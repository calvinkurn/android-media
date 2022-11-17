package com.tokopedia.feedplus.data.pojo

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TagsFeedKol(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("link")
    @Expose
    val link: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: String = "",

    @SerializedName("url")
    @Expose
    val url: String = "",

    @SerializedName("caption")
    @Expose
    val caption: String = "",

    @SerializedName("captionInd")
    @Expose
    val captionInd: String = "",

    @SerializedName("captionEng")
    @Expose
    val captionEng: String = ""
)
