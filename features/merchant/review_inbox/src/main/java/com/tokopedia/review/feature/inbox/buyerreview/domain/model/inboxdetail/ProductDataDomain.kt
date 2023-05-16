package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ProductDataDomain constructor(
    @SerializedName("productID") @Expose val productId: String,
    @SerializedName("productName") @Expose val productName: String,
    @SerializedName("productImageURL") @Expose val productImageUrl: String,
    @SerializedName("productStatus") @Expose val productStatus: Int
)
