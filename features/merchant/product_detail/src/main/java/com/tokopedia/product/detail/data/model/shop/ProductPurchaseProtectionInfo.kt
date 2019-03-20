package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.SerializedName

data class ProductPurchaseProtectionInfo(@SerializedName("ppGetItemDetailPage")
                                         var ppItemDetailPage: PPItemDetailPage? = null)
