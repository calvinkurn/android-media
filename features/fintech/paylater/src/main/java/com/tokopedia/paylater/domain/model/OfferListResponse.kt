package com.tokopedia.paylater.domain.model

data class OfferListResponse(
        val payLaterOfferName: String? = "",
        val offerItemList: List<OfferDescriptionItem> = listOf(OfferDescriptionItem())
)