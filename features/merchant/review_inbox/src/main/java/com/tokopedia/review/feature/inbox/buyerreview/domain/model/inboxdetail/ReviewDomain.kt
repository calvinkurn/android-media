package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/19/17.
 */
data class ReviewDomain(
    @SerializedName("reviewList") @Expose val data: List<ReviewItemDomain> = listOf(),
    @SerializedName("reputationIdStr") @Expose val reputationId: String = "",
    @SerializedName("userData") @Expose val userData: UserDataDomain = UserDataDomain(),
    @SerializedName("shopData") @Expose val shopData: ShopDataDomain = ShopDataDomain(),
    @SerializedName("invoiceRefNum") @Expose val invoiceRefNum: String = "",
    @SerializedName("invoiceTime") @Expose val invoiceTime: String = "",
    @SerializedName("orderId") @Expose val orderId: String = ""
)