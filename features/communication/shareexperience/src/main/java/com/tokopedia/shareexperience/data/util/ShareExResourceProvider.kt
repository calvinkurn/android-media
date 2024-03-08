package com.tokopedia.shareexperience.data.util

interface ShareExResourceProvider {
    // Social Media Channels
    fun getWhatsappChannelTitle(): String
    fun getFacebookFeedChannelTitle(): String
    fun getFacebookStoryChannelTitle(): String
    fun getInstagramFeedChannelTitle(): String
    fun getInstagramStoryChannelTitle(): String
    fun getInstagramDirectMessageChannelTitle(): String
    fun getLineChannelTitle(): String
    fun getXTwitterChannelTitle(): String
    fun getTelegramChannelTitle(): String

    // Default Channels
    fun getCopyLinkChannelTitle(): String
    fun getSMSChannelTitle(): String
    fun getEmailChannelTitle(): String
    fun getOthersChannelTitle(): String
}
