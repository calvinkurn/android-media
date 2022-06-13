package com.tokopedia.play.analytic.share

/**
 * Created By : Jonathan Darwin on December 15, 2021
 */
interface PlayShareExperienceAnalytic {

    fun clickShareButton(channelId: String, partnerId: Long?, channelType: String)

    fun closeShareBottomSheet(channelId: String, partnerId: Long?, channelType: String, isScreenshot: Boolean)

    fun clickSharingOption(channelId: String, partnerId: Long?, channelType: String, sharingOption: String?, isScreenshot: Boolean)

    fun impressShareBottomSheet(channelId: String, partnerId: Long?, channelType: String)

    fun clickSharePermission(channelId: String, partnerId: Long?, channelType: String, label: String)

    fun takeScreenshotForSharing(channelId: String, partnerId: Long?, channelType: String)
}