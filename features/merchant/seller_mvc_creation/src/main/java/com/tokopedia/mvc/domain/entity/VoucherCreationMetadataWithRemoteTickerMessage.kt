package com.tokopedia.mvc.domain.entity

data class VoucherCreationMetadataWithRemoteTickerMessage(
    val creationMetadata: VoucherCreationMetadata,
    val tickerWording: String
)
