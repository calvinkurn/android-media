package com.tokopedia.developer_options.deeplink.presentation.uimodel

data class DeepLinkUiModel(
    val appLink: String,
    val deepLinkVariable: String,
    val destActivity: String
)