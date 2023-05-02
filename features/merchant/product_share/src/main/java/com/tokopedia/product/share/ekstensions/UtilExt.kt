package com.tokopedia.product.share.ekstensions

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.R

fun ProductData.getTextDescriptionNonDisc(context: Context, shortUrl: String): String {
    return context.getString(R.string.product_share_text_description_no_discount, priceText) + "\n$shortUrl"
}

fun ProductData.getTextDescriptionDisc(context: Context, shortUrl: String, disc: Float): String {
    return context.getString(R.string.product_share_text_description_discount, "$disc%", priceText) + "\n$shortUrl"
}

fun ProductData.getShareContent(shortUrl: String): String {
    if (!TextUtils.isEmpty(productShareDescription)) {
        if (!productShareDescription.contains(PLACEHOLDER_LINK)) {
            productShareDescription = productShareDescription + "\n" + PLACEHOLDER_LINK;
        }
        return FindAndReplaceHelper.findAndReplacePlaceHolders(productShareDescription,
                PLACEHOLDER_PRICE, if (priceText != null) priceText else "",
                PLACEHOLDER_SHOP_NAME, if (shopName != null) shopName else "",
                PLACEHOLDER_NAME, if (productName != null) productName else "",
                PLACEHOLDER_LINK, if (shortUrl != null) shortUrl else "",
                PLACEHOLDER_NEW_LINE, "\r\n")
    } else {
        var content = ""
        if (!productName.isNullOrEmpty()) {
            content = "Temukan $productName seharga $priceText hanya di Tokopedia!\n"
        }
        return "$content$shortUrl"
    }
}
