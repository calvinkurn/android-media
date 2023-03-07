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

    fun onClickRefreshGameResult(channelId: String, channelTitle: String)

    fun onImpressSelectGame(channelId: String, channelTitle: String)

    fun onClickBackQuiz(channelId: String, channelTitle: String)

    fun onclickBackSetTimerGiveAway(channelId: String, channelTitle: String)

    fun onClickContinueQuiz(channelId: String, channelTitle: String)

    fun onClickStartQuiz(channelId: String, channelTitle: String)

    fun onClickBackQuizDuration(channelId: String, channelTitle: String)

    fun onImpressOngoingQuizWidget(channelId: String, channelTitle: String, quizId: String, quizTitle:String)

    fun onCLickQuizOptionLive(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onClickCloseOngoingQuizBottomSheet(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onClickBackQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onClickOngoingQuiz(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onImpressOngoingLeaderBoard(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String,
        reward: String
    )

    fun onImpressQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onImpressReportLeaderboard(
        channelId: String,
        channelTitle: String,
        id: String,
        title: String,
        engagementType: String
    )

    fun onImpressReportQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )

    fun onCLickQuizOptionReport(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    )
}
