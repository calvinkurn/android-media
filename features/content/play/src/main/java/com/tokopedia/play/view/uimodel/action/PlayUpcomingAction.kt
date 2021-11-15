package com.tokopedia.play.view.uimodel.action

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
sealed class PlayUpcomingAction

/**
 * Upcoming
 */
object ImpressUpcomingChannel: PlayUpcomingAction()
object ClickRemindMeUpcomingChannel: PlayUpcomingAction()
object ClickWatchNowUpcomingChannel: PlayUpcomingAction()
object ClickRefreshUpcomingChannel: PlayUpcomingAction()
object UpcomingTimerFinish: PlayUpcomingAction()

/**
 * Upcoming Interaction
 */
object ClickFollowUpcomingAction: PlayUpcomingAction()
object ClickPartnerNameUpcomingAction: PlayUpcomingAction()
object ClickShareUpcomingAction: PlayUpcomingAction()

