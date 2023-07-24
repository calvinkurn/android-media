package com.tokopedia.mvc.domain.entity

import com.tokopedia.campaign.entity.RemoteTicker

data class VoucherCreationMetadataWithRemoteTickerMessage(
    val creationMetadata: VoucherCreationMetadata,
    val tickers: List<RemoteTicker>
)
