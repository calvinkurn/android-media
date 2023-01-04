package com.tokopedia.dilayanitokopedia.home.domain.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by irpan on 30/11/22.
 */
data class Shop(
    @SerializedName("applink")
    @Expose
    val applink: String = "",

    @SerializedName("city")
    @Expose
    val city: String = "",

    @SerializedName("domain")
    @Expose
    val domain: String = "",

    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("reputation")
    @Expose
    val reputation: String = "",

    @SerializedName("url")
    @Expose
    val url: String = ""
)
