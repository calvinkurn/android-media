package com.tokopedia.shop_widget.buy_more_save_more.data.response

import com.google.gson.annotations.SerializedName

data class GetOfferingInfoByShopIdResponse(
    @SerializedName("GetOfferingInfoByShopId")
    val offeringInfoByShopId: OfferingInfoByShopId = OfferingInfoByShopId()
) {
    data class OfferingInfoByShopId(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("list_offering")
        val listOffering: List<GetOfferingInfoByShopIDResponseListOffering> = listOf()
    )

    data class ResponseHeader(
        @SerializedName("success")
        val success: Boolean = true,
        @SerializedName("errorCode")
        val errorCode: Long = 0,
        @SerializedName("processTime")
        val processTime: String = ""
    )

    data class GetOfferingInfoByShopIDResponseListOffering(
        @SerializedName("offer_id")
        val offerId: Long = 0,
        @SerializedName("offer_name")
        val offerName: String = "",
        @SerializedName("offer_cta_text")
        val offerCtaText: String = "",
        @SerializedName("redirect_offer_url")
        val redirectOfferUrl: String = "",
        @SerializedName("redirect_offer_applink")
        val redirectOfferApplink: String = "",
        @SerializedName("offer_banner")
        val offerBanner: GetOfferingInfoByShopIDResponseBanner = GetOfferingInfoByShopIDResponseBanner()
    )

    data class GetOfferingInfoByShopIDResponseBanner(
        @SerializedName("banner_image")
        val bannerImage: String = ""
    )
}
