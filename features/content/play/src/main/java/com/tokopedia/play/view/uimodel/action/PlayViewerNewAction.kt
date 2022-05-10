package com.tokopedia.play.view.uimodel.action

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayViewerNewAction {

    object GiveawayUpcomingEnded : PlayViewerNewAction()
    object GiveawayOngoingEnded : PlayViewerNewAction()
    object TapGiveaway : PlayViewerNewAction()

    data class ClickQuizOptionAction(val item: QuizChoicesUiModel): PlayViewerNewAction()
    object QuizEnded : PlayViewerNewAction()

    object StartPlayingInteractive : PlayViewerNewAction()
    object StopPlayingInteractive : PlayViewerNewAction()
    object FollowInteractive : PlayViewerNewAction()

    object Follow : PlayViewerNewAction()
}

/**
 * Interactive
 */
data class InteractiveWinnerBadgeClickedAction(val height: Int) : PlayViewerNewAction()
data class InteractiveGameResultBadgeClickedAction(val height: Int) : PlayViewerNewAction()

object ClickRetryInteractiveAction : PlayViewerNewAction()

object ClickCloseLeaderboardSheetAction : PlayViewerNewAction()

object RefreshLeaderboard: PlayViewerNewAction()

/**
 * Partner
 */
object ClickFollowAction : PlayViewerNewAction()
data class ClickPartnerNameAction(val appLink: String) : PlayViewerNewAction()

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
data class BuyProductVariantAction(val id: String, val sectionInfo: ProductSectionUiModel.Section) : PlayViewerNewAction()
data class AtcProductAction(val sectionInfo: ProductSectionUiModel.Section, val product: PlayProductUiModel.Product) : PlayViewerNewAction()
data class AtcProductVariantAction(val id: String, val sectionInfo: ProductSectionUiModel.Section) : PlayViewerNewAction()
data class SelectVariantOptionAction(val option: VariantOptionWithAttribute) : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()

object OpenKebabAction: PlayViewerNewAction()
object OpenUserReport: PlayViewerNewAction()
data class OpenFooterUserReport(val appLink: String): PlayViewerNewAction()

data class SendUpcomingReminder(val section: ProductSectionUiModel.Section): PlayViewerNewAction()