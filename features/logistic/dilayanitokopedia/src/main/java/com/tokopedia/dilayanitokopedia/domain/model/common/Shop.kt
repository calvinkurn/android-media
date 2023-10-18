package com.tokopedia.dilayanitokopedia.domain.model.common

import com.google.gson.annotations.SerializedName

/**
 * Created by irpan on 30/11/22.
 */
data class Shop(
    @SerializedName("applink")
    val applink: String = "",

    @SerializedName("city")
    val city: String = "",

    @SerializedName("domain")
    val domain: String = "",

    @SerializedName("id")
    val id: String = "",

    @SerializedName("imageUrl")
    val imageUrl: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("reputation")
    val reputation: String = "",

    @SerializedName("url")
    val url: String = ""
)
