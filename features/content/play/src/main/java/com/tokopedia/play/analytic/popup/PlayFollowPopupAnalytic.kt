package com.tokopedia.play.analytic.popup

/**
 * @author by astidhiyaa on 15/11/22
 */
interface PlayFollowPopupAnalytic {
    fun impressFollowPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    )

    fun clickDismissFollowPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    )

    fun clickFollowCreatorPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    )

    fun clickCreatorPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    )

    fun impressToasterPopUp(channelId: String, channelType: String, isSuccess: Boolean)
    fun clickRetryToasterPopUp(channelId: String, channelType: String)
}
