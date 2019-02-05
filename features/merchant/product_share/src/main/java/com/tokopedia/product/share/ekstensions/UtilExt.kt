package com.tokopedia.product.share.ekstensions

import android.text.TextUtils
import com.tokopedia.product.share.ProductData
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper



fun ProductData.getShareContent(shortUrl: String):String{
    if(!TextUtils.isEmpty(productShareDescription)){
        if(!productShareDescription.contains(PLACEHOLDER_LINK)){
            productShareDescription = productShareDescription + "\n" + PLACEHOLDER_LINK;
        }
        return FindAndReplaceHelper.findAndReplacePlaceHolders(productShareDescription,
                PLACEHOLDER_PRICE, if (priceText != null) priceText else "",
                PLACEHOLDER_SHOP_NAME, if (shopName != null) shopName else "",
                PLACEHOLDER_NAME, if (productName != null) productName else "",
                PLACEHOLDER_LINK, if(shortUrl != null) shortUrl else "",
                PLACEHOLDER_NEW_LINE, "\r\n")
    }
    else {
        var content = ""
        if (!productName.isNullOrEmpty()){
            content = "Temukan $productName seharga $priceText hanya di Tokopedia!\n"
        }
        return "$content$shortUrl"
    }
}