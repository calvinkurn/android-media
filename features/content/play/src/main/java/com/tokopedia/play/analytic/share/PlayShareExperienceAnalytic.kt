package com.tokopedia.play.analytic.share

/**
 * Created By : Jonathan Darwin on December 15, 2021
 */
interface PlayShareExperienceAnalytic {

    fun clickShareButton(channelId: String, channelType: String)

    fun closeShareBottomSheet(channelId: String, channelType: String, isScreenshot: Boolean)

    fun clickSharingOption(channelId: String, channelType: String, sharingOption: String?, isScreenshot: Boolean)

    fun impressShareBottomSheet(channelId: String, channelType: String)

    fun clickSharePermission(channelId: String, channelType: String, label: String)

    fun takeScreenshotForSharing(channelId: String, channelType: String)
}