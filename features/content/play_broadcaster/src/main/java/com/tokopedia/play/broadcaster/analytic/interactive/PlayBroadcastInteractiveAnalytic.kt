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

    fun onClickChatWinnerIcon(channelId: String, channelTitle: String)

    fun onClickGameIconButton(channelId: String, channelTitle: String)

    fun onClickGameOption(channelId: String, channelTitle: String, gameType: String)

    fun onClickContinueGiveaway(channelId: String, channelTitle: String)

    fun onImpressGameIconButton(channelId: String, channelTitle: String)

    fun onClickBackGiveaway(channelId: String, channelTitle: String)

    fun onClickGameResult(channelId: String, channelTitle: String)

    fun onClickCloseGameResultBottomsheet(channelId: String, channelTitle: String)

    fun onClickCloseGameResultReport(channelId: String, channelTitle: String)

    fun onImpressFailedLeaderboard(channelId: String, channelTitle: String)
}