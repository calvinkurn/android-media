package com.tokopedia.buy_more_get_more.olp.domain.entity

class SharingDataByOfferIdUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val offerData: OfferData = OfferData()
) {
    data class ResponseHeader(
        val success: Boolean = true,
        val errorCode: Long = 0,
        val processTime: String = ""
    )

    data class OfferData(
        val imageUrl: String = "",
        val title: String = "",
        val description: String = "",
        val deeplink: String = "",
        val tag: String = "",
        val pageType: String = "",
        val campaignName: String = ""
    )
}
