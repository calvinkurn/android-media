package com.tokopedia.vouchercreation.product.share.domain.entity

data class ShareComponentParam(
    val title : String,
    val thumbnailImageUrl : String,
    val description : String,
    val redirectionUrl : String,
    val deeplinkPath : String,
    val utmMedium : String,
    val utmCampaign : String,
    val outgoingTitle: String,
    val outgoingDescription: String,
    val outgoingImageUrl: String
)