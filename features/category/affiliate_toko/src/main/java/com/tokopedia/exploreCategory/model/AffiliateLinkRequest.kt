package com.tokopedia.exploreCategory.model

data class AffiliateLinkRequest(
        var type: String,
        var url: String?,
        var identifier: String?,
        var params: ArrayList<String>,
)