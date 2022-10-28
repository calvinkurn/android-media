package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ProductDataDomain constructor(
    @SerializedName("productIdStr") @Expose val productId: String,
    @SerializedName("name") @Expose val productName: String,
    @SerializedName("imageUrl") @Expose val productImageUrl: String,
    @SerializedName("url") @Expose val productPageUrl: String,
    @SerializedName("shopIdStr") @Expose val shopId: String,
    @SerializedName("status") @Expose val productStatus: Int
)