package com.tokopedia.play.broadcaster.analytic.interactive


/**
 * Created by mzennis on 22/07/21.
 */
interface PlayBroadcastInteractiveAnalytic {

    fun onImpressInteractiveTool(channelId: String)

    fun onClickInteractiveTool(channelId: String)

    fun onStartInteractive(channelId: String, interactiveId: String, interactiveTitle: String, durationInMs: Long)

    fun onImpressWinnerIcon(channelId: String, interactiveId: String, interactiveTitle: String)

    fun onClickWinnerIcon(channelId: String, interactiveId: String, interactiveTitle: String)

    fun onClickChatWinnerIcon(channelId: String, interactiveId: String, interactiveTitle: String)
}