package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ShopDataDomain constructor(
    @SerializedName("shopIdStr") @Expose val shopId: String = "",
    @SerializedName("domain") @Expose val domain: String = "",
    @SerializedName("shopName") @Expose val shopName: String = "",
)