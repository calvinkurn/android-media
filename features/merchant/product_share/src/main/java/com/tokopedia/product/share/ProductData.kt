package com.tokopedia.product.share

import android.net.Uri

data class ProductData(
        var priceText: String = "",
        var cashbacktext: String? = null,
        var productName: String? = "",
        var currencySymbol: String = "Rp",
        var productUrl: String? = "",
        var shopUrl: String? = "",
        var productId: String = "",
        var productImageUrl: String? = ""
) {
    val renderShareUri: String
        get() {
            if (productUrl == null)
                return ""
            val strUri = if (productUrl!!.contains("?")) "$productUrl&" else "$productUrl?"
            return Uri.parse("${strUri}utm_source=${ARG_UTM_SOURCE}&utm_medium=${ARG_UTM_MEDIUM}&utm_campaign=$CAMPAIGN_NAME").toString()
        }

    companion object {
        private const val ARG_UTM_SOURCE = "Android"
        private const val ARG_UTM_MEDIUM = "Share"
        private const val CAMPAIGN_NAME = "Product%20Share"
    }
}