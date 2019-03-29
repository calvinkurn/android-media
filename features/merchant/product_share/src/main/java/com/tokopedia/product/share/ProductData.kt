package com.tokopedia.product.share

import android.net.Uri

data class ProductData(
        var priceText: String = "",
        var cashbacktext: String? = null,
        var productName: String? = "",
        var currencySymbol: String = "Rp",
        var productUrl: String? = "",
        var shopUrl: String? = "",
        var shopName: String = "",
        var productId: String = "",
        var productImageUrl: String? = "",
        var productShareDescription: String = "",
        val PLACEHOLDER_NAME: String = "{{name}}",
        val PLACEHOLDER_LINK: String = "{{branchlink}}",
        val PLACEHOLDER_PRICE: String = "{{price}}",
        val PLACEHOLDER_SHOP_NAME: String = "{{shop_name}}",
        val PLACEHOLDER_NEW_LINE: String = "\\n"
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
        const val PLACEHOLDER_REFERRAL_CODE: String = "{{referral_code}}"

    }
}