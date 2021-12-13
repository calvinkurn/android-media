package com.tokopedia.campaignlist.common.util

class FakeResourceProvider : ResourceProvider {

    companion object {
        private const val EMPTY_STRING = ""
    }

    override fun getString(resId: Int): String {
        return EMPTY_STRING
    }

    override fun getShareTitle(): String {
        return EMPTY_STRING
    }

    override fun getShareCampaignDescriptionWording(): String {
        return EMPTY_STRING
    }

    override fun getShareOngoingCampaignDescriptionWording(): String {
        return EMPTY_STRING
    }

    override fun getShareOgTitle(): String {
        return EMPTY_STRING
    }

    override fun getShareOgDescription(): String {
        return EMPTY_STRING
    }

    override fun getShareOngoingOgDescription(): String {
        return EMPTY_STRING
    }

}