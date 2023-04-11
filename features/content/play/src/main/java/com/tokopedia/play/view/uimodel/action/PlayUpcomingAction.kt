package com.tokopedia.play.view.uimodel.action

import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
sealed class PlayUpcomingAction

/**
 * Upcoming
 */
object ClickUpcomingButton: PlayUpcomingAction()
object UpcomingTimerFinish: PlayUpcomingAction()

/**
 * Upcoming Interaction
 */
object ClickFollowUpcomingAction: PlayUpcomingAction()
data class ClickPartnerNameUpcomingAction(val appLink: String): PlayUpcomingAction()
object ClickShareUpcomingAction: PlayUpcomingAction()

/**
 * Share Experience
 */
data class ClickSharingOptionUpcomingAction(val shareModel: ShareModel): PlayUpcomingAction()
object ShowShareExperienceUpcomingAction: PlayUpcomingAction()
object ScreenshotTakenUpcomingAction: PlayUpcomingAction()
object CopyLinkUpcomingAction: PlayUpcomingAction()

data class OpenUpcomingPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayUpcomingAction()

/**
 * Description
 */
object ExpandDescriptionUpcomingAction : PlayUpcomingAction()
object TapCover : PlayUpcomingAction()

