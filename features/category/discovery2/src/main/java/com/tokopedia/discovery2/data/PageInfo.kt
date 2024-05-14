package com.tokopedia.discovery2.data

import android.annotation.SuppressLint
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

        @SuppressLint("Invalid Data Type")
        @SerializedName("Id")
        val id: Int = 0,

        @SerializedName("share_config", alternate = ["share"])
        val share: Share? = null,

        @SerializedName("campaign_code")
        val campaignCode: String? = null,

        @SerializedName("search_title")
        val searchTitle: String = "Cari di Tokopedia",

        @SerializedName("show_choose_address")
        val showChooseAddress: Boolean = false,

        @SerializedName("tokonow_has_mini_cart_active")
        val tokonowMiniCartActive : Boolean = false,

        @SerializedName("thematic_header")
        val thematicHeader: ThematicHeader? = null,

        @SerializedName("is_affiliate")
        val isAffiliate: Boolean = false,

        @SerializedName("labels")
        val label: Label = Label(),

        var additionalInfo: AdditionalInfo? = null,

        var redirectionUrl: String? = null,

        var isAdult: Int = 0,

        var origin: Int = 0
) {
    data class Label(
        @SerializedName("tracking_pagename")
        val trackingPageName: String = ""
    ) {
        val enterFromPage: String
            get() = formatEnterFromPage()

        private fun formatEnterFromPage(): String {
            return if (trackingPageName.equals(ADS_LANDING_PAGE_ORIGIN, false)) {
                ADS_LANDING_PAGE_FORMATTED
            } else {
                trackingPageName
            }
        }

        companion object {
            private const val ADS_LANDING_PAGE_ORIGIN = "ads_landing_page"
            private const val ADS_LANDING_PAGE_FORMATTED = "external_promo"
        }
    }
}
