package com.tokopedia.dilayanitokopedia.common.util

import com.google.gson.annotations.SerializedName

data class PageInfo(

    @SerializedName("Path")
    val path: String? = "",

    @SerializedName("Name")
    var name: String? = "",

    @SerializedName("Type")
    val type: String? = "",

    @SerializedName("search_applink")
    val searchApplink: String? = "",

    @SerializedName("Identifier")
    val identifier: String? = "",

    @SerializedName("Id")
    val id: Long = 0,

//        @SerializedName("share_config", alternate = ["share"])
//        val share: Share? = null,

    @SerializedName("campaign_code")
    val campaignCode: String? = null,

    @SerializedName("search_title")
    val searchTitle: String = "Cari di Tokopedia",

    @SerializedName("show_choose_address")
    val showChooseAddress: Boolean = false,

    @SerializedName("tokonow_has_mini_cart_active")
    val tokonowMiniCartActive: Boolean = false,

//        var additionalInfo: AdditionalInfo? = null,

    var redirectionUrl: String? = null,

    var isAdult: Int = 0,

    var origin: Int = 0
)
