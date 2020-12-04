package com.tokopedia.homenav.mainnav.data.pojo.order

data class Order(
    val metadata: Metadata,
    val orderUUID: String,
    val searchableText: String,
    val status: String,
    val userID: String,
    val verticalCategory: String,
    val verticalID: String,
    val verticalStatus: String
)