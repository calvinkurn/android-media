package com.tokopedia.mvc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMerchantPromoListResponse(
    @SerializedName("merchantPromotionGetPromoList")
    @Expose
    val merchantPromotionGetPromoList: MerchantPromotionGetPromoList = MerchantPromotionGetPromoList()
) {

    data class MerchantPromotionGetPromoList(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
    ){
        data class Data(
            @SerializedName("pages")
            val pages: List<Page> = listOf()
        ){
            data class Page(
                @SerializedName("page_id")
                val pageId: String = "",
                @SerializedName("page_name")
                val pageName: String = "",
                @SerializedName("cta_link")
                val ctaLink: String = ""
            )
        }
    }
}
