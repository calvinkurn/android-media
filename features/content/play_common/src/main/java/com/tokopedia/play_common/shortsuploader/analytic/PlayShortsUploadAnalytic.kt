package com.tokopedia.play_common.shortsuploader.analytic

/**
 * Created By : Jonathan Darwin on December 13, 2022
 */
interface PlayShortsUploadAnalytic {

    fun clickRedirectToChannelRoom(
        authorId: String,
        authorType: String,
        channelId: String
    )

    fun clickRetryUpload(
        authorId: String,
        authorType: String,
        channelId: String
    )
}
