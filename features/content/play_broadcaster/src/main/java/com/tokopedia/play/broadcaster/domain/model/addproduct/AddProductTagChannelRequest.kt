package com.tokopedia.play.broadcaster.domain.model.addproduct

data class AddProductTagChannelRequest(
    val channelId: String,
    val productIds: List<String>,
)
