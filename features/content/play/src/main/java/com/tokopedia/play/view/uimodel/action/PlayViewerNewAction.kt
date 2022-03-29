package com.tokopedia.play.view.uimodel.action

import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.universal_sharing.view.model.ShareModel

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

object RefreshLeaderboard: PlayViewerNewAction()

/**
 * Partner
 */
object ClickFollowAction : PlayViewerNewAction()
object ClickPartnerNameAction : PlayViewerNewAction()

/**
 * Like
 */
object ClickLikeAction : PlayViewerNewAction()

/**
 * Share
 */
object ClickShareAction : PlayViewerNewAction()
object CopyLinkAction: PlayViewerNewAction()

/**
 * Swipe
 */
object SetChannelActiveAction : PlayViewerNewAction()

/**
 * Sharing Experience
 */
object ShowShareExperienceAction: PlayViewerNewAction()
data class ClickSharingOptionAction(val shareModel: ShareModel): PlayViewerNewAction()
object CloseSharingOptionAction: PlayViewerNewAction()
object ScreenshotTakenAction: PlayViewerNewAction()
data class SharePermissionAction(val label: String): PlayViewerNewAction()

/**
 * Product
 */
object RetryGetTagItemsAction : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()

data class OpenKebabAction (val height: Int): PlayViewerNewAction()
object OpenUserReport: PlayViewerNewAction()
data class OpenFooterUserReport(val appLink: String): PlayViewerNewAction()

data class SendUpcomingReminder(val section: ProductSectionUiModel.Section): PlayViewerNewAction()