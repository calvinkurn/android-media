package com.tokopedia.tokofood.feature.merchant.presentation.model

data class MerchantShareComponent(
    val id: String = "",
    val pageName: String = "",
    val previewTitle: String = "",
    val previewThumbnail: String = "",
    val txtDesc: String = "",
    val shareLinkUrl: String = "",
    val shareLinkDeepLink: String = "",
    val ogTitle: String = "",
    val ogDesc: String = "",
    val ogImage: String = ""
)