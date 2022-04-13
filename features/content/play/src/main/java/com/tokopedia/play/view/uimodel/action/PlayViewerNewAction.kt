package com.tokopedia.play.view.uimodel.action

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayViewerNewAction {

    object GiveawayUpcomingEnded : PlayViewerNewAction()
    object GiveawayOngoingEnded : PlayViewerNewAction()

    object QuizEnded : PlayViewerNewAction()

    object StartPlayingInteractive : PlayViewerNewAction()
    object StopPlayingInteractive : PlayViewerNewAction()

    object FollowPartner : PlayViewerNewAction()
}

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
data class BuyProductAction(val sectionInfo: ProductSectionUiModel.Section, val product: PlayProductUiModel.Product) : PlayViewerNewAction()
data class BuyProductVariantAction(val id: String) : PlayViewerNewAction()
data class AtcProductAction(val sectionInfo: ProductSectionUiModel.Section, val product: PlayProductUiModel.Product) : PlayViewerNewAction()
data class AtcProductVariantAction(val id: String) : PlayViewerNewAction()
data class SelectVariantOptionAction(val option: VariantOptionWithAttribute) : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()

data class SendUpcomingReminder(val section: ProductSectionUiModel.Section): PlayViewerNewAction()