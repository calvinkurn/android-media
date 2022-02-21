package com.tokopedia.play.view.uimodel.action

import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
sealed class PlayUpcomingAction

/**
 * Upcoming
 */
object ImpressUpcomingChannel: PlayUpcomingAction()
object ClickUpcomingButton: PlayUpcomingAction()
object UpcomingTimerFinish: PlayUpcomingAction()

/**
 * Upcoming Interaction
 */
object ClickFollowUpcomingAction: PlayUpcomingAction()
object ClickPartnerNameUpcomingAction: PlayUpcomingAction()
object ClickShareUpcomingAction: PlayUpcomingAction()

/**
 * Share Experience
 */
data class ClickSharingOptionUpcomingAction(val shareModel: ShareModel): PlayUpcomingAction()
object ShowShareExperienceUpcomingAction: PlayUpcomingAction()
object CloseSharingOptionUpcomingAction: PlayUpcomingAction()
object ScreenshotTakenUpcomingAction: PlayUpcomingAction()
data class SharePermissionUpcomingAction(val label: String): PlayUpcomingAction()
object CopyLinkUpcomingAction: PlayUpcomingAction()

data class OpenUpcomingPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayUpcomingAction()

