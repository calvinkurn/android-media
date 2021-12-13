package com.tokopedia.campaignlist.common.util

interface ResourceProvider {
    fun getString(resId: Int): String?
    fun getShareTitle(): String?
    fun getShareCampaignDescriptionWording(): String?
    fun getShareOngoingCampaignDescriptionWording(): String?
    fun getShareOgTitle(): String?
    fun getShareOgDescription(): String?
    fun getShareOngoingOgDescription(): String?
}