package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductPurchaseProtectionInfo(
        @SerializedName("program")
        @Expose
        var ppItemDetailPage: PPItemDetailPage = PPItemDetailPage()
) {
    data class Response(
            @SerializedName("ppGetItemDetailPage")
            @Expose
            var response: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo()
    )
}
