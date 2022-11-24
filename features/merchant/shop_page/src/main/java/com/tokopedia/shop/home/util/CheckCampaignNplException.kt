package com.tokopedia.shop.home.util

data class CheckCampaignNplException(
    override val cause: Throwable?,
    override val message: String?,
    val campaignId: String = ""
) : Throwable()
