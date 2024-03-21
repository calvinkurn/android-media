package com.tokopedia.shareexperience.data.util

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject
import com.tokopedia.shareexperience.R

class ShareExResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ShareExResourceProvider {

    override fun getWhatsappChannelTitle(): String {
        return context.getString(R.string.shareex_channel_whatsapp_title)
    }

    override fun getFacebookFeedChannelTitle(): String {
        return context.getString(R.string.shareex_channel_fb_feed_title)
    }

    override fun getFacebookStoryChannelTitle(): String {
        return context.getString(R.string.shareex_channel_fb_story_title)
    }

    override fun getInstagramFeedChannelTitle(): String {
        return context.getString(R.string.shareex_channel_ig_feed_title)
    }

    override fun getInstagramStoryChannelTitle(): String {
        return context.getString(R.string.shareex_channel_ig_story_title)
    }

    override fun getInstagramDirectMessageChannelTitle(): String {
        return context.getString(R.string.shareex_channel_ig_dm_title)
    }

    override fun getLineChannelTitle(): String {
        return context.getString(R.string.shareex_channel_line_title)
    }

    override fun getXTwitterChannelTitle(): String {
        return context.getString(R.string.shareex_channel_x_twitter_title)
    }

    override fun getTelegramChannelTitle(): String {
        return context.getString(R.string.shareex_channel_telegram_title)
    }

    override fun getCopyLinkChannelTitle(): String {
        return context.getString(R.string.shareex_channel_copy_link_title)
    }

    override fun getSMSChannelTitle(): String {
        return context.getString(R.string.shareex_channel_sms_title)
    }

    @SuppressLint("PII Data Exposure")
    override fun getEmailChannelTitle(): String {
        return context.getString(R.string.shareex_channel_email_title)
    }

    override fun getOthersChannelTitle(): String {
        return context.getString(R.string.shareex_channel_others_title)
    }

}
