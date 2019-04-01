package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductPurchaseProtectionInfo{

    @SerializedName("ppGetItemDetailPage")
    @Expose
    var ppItemDetailPage: PPItemDetailPage? = null

}
