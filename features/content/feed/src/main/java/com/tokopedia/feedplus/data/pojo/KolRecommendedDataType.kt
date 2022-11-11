package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class KolRecommendedDataType(
    @SerializedName("index")
    @Expose
    val index: Int = 0,

    @SerializedName("kols")
    @Expose
    val kols: List<FeedKolRecommendedType> = emptyList(),

    @SerializedName("headerTitle")
    @Expose
    val headerTitle: String = "",

    @SerializedName("exploreLink")
    @Expose
    val exploreLink: String = "",

    @SerializedName("exploreText")
    @Expose
    val exploreText: String = ""
)
