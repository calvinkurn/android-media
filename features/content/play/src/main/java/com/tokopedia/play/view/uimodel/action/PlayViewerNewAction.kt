package com.tokopedia.play.view.uimodel.action

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayViewerNewAction

object InteractivePreStartFinishedAction : PlayViewerNewAction()
object InteractiveOngoingFinishedAction : PlayViewerNewAction()

object InteractiveWinnerBadgeClickedAction : PlayViewerNewAction()