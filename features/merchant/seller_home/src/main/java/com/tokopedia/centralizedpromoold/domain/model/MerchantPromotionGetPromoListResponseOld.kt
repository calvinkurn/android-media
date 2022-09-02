package com.tokopedia.centralizedpromoold.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class MerchantPromotionGetPromoListResponseOld(
    @Expose
    @SerializedName("merchantPromotionGetPromoList")
    val merchantPromotionGetPromoList: MerchantPromotionGetPromoList = MerchantPromotionGetPromoList()
)

data class MerchantPromotionGetPromoList(
    @Expose
    @SerializedName("header")
    val header: Header = Header(),
    @Expose
    @SerializedName("data")
    val data: MerchantPromotionGetPromoListData = MerchantPromotionGetPromoListData()
)

data class MerchantPromotionGetPromoListData(
    @Expose
    @SerializedName("pages")
    val pages: List<MerchantPromotionGetPromoListPageOld> = listOf()
)

data class MerchantPromotionGetPromoListPageOld(
    @Expose
    @SerializedName("page_id")
    val pageId: String = "",
    @Expose
    @SerializedName("is_eligible")
    val isEligible: Int = 0
) {

    companion object {
        private const val IS_ELIGIBLE = 1
    }

    fun getIsEligible(): Boolean = isEligible == IS_ELIGIBLE
}