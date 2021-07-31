package com.tokopedia.play.view.uimodel.action

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayViewerNewAction

object InteractivePreStartFinishedAction : PlayViewerNewAction()
object InteractiveOngoingFinishedAction : PlayViewerNewAction()

data class InteractiveWinnerBadgeClickedAction(val height: Int) : PlayViewerNewAction()

object InteractiveTapTapAction : PlayViewerNewAction()

object ClickCloseLeaderboardSheetAction : PlayViewerNewAction()

object ClickFollowAction : PlayViewerNewAction()
object ClickPartnerNameAction : PlayViewerNewAction()

object ClickFollowInteractiveAction : PlayViewerNewAction()
object ClickRetryInteractiveAction : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()