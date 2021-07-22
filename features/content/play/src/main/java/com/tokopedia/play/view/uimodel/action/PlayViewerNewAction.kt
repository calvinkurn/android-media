package com.tokopedia.play.view.uimodel.action

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayViewerNewAction

/**
 * Interactive
 */
object InteractivePreStartFinishedAction : PlayViewerNewAction()
object InteractiveOngoingFinishedAction : PlayViewerNewAction()

data class InteractiveWinnerBadgeClickedAction(val height: Int) : PlayViewerNewAction()

object InteractiveTapTapAction : PlayViewerNewAction()

object ClickFollowInteractiveAction : PlayViewerNewAction()
object ClickRetryInteractiveAction : PlayViewerNewAction()

object ClickCloseLeaderboardSheetAction : PlayViewerNewAction()

/**
 * Partner
 */
object ClickFollowAction : PlayViewerNewAction()
object ClickPartnerNameAction : PlayViewerNewAction()

/**
 * Like
 */
object ClickLikeAction : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()