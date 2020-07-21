package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.data.model.P2Error

data class ProductPurchaseProtectionInfo(
        @SerializedName("program")
        @Expose
        var ppItemDetailPage: PPItemDetailPage = PPItemDetailPage(),

        @SerializedName("error")
        @Expose
        var ppError: P2Error = P2Error()
) {
    data class Response(
            @SerializedName("ppGetItemDetailPage")
            @Expose
            var response: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo()
    )
}
