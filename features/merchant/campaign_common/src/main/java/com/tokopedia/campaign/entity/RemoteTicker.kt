package com.tokopedia.campaign.entity

data class RemoteTicker(
    val title: String,
    val description: String,
    val type: String,
    val actionLabel: String,
    val actionType: String,
    val actionAppUrl: String
)
