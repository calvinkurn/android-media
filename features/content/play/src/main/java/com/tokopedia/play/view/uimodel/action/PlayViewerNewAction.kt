package com.tokopedia.play.view.uimodel.action

import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
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
    object AutoOpenInteractive: PlayViewerNewAction()

    object Follow : PlayViewerNewAction()

    data class BuyProduct(
        val product: PlayProductUiModel.Product,
        val isProductFeatured: Boolean = false,
    ) : PlayViewerNewAction()
    data class AtcProduct(
        val product: PlayProductUiModel.Product,
        val isProductFeatured: Boolean = false,
    ) : PlayViewerNewAction()
    data class OCCProduct(
        val product: PlayProductUiModel.Product,
        val isProductFeatured: Boolean = false,
    ) : PlayViewerNewAction()
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
data class OCCProductAction(val sectionInfo: ProductSectionUiModel.Section, val product: PlayProductUiModel.Product) : PlayViewerNewAction()
data class OCCProductVariantAction(val id: String, val sectionInfo: ProductSectionUiModel.Section) : PlayViewerNewAction()
data class SelectVariantOptionAction(val option: VariantOptionWithAttribute) : PlayViewerNewAction()

data class OpenPageResultAction(val isSuccess: Boolean, val requestCode: Int) : PlayViewerNewAction()

object OpenKebabAction: PlayViewerNewAction()
object OpenUserReport: PlayViewerNewAction()
data class OpenFooterUserReport(val appLink: String): PlayViewerNewAction()

data class SendUpcomingReminder(val section: ProductSectionUiModel.Section): PlayViewerNewAction()

data class SendWarehouseId(val id: String, val isOOC: Boolean) : PlayViewerNewAction()

object DismissFollowPopUp : PlayViewerNewAction()
object OpenCart: PlayViewerNewAction()

/**
 * Explore Widget
 */
object FetchWidgets: PlayViewerNewAction()
data class ClickChipWidget(val item: ChipWidgetUiModel) : PlayViewerNewAction()
object NextPageWidgets : PlayViewerNewAction()
object RefreshWidget : PlayViewerNewAction()
data class UpdateReminder(val channelId : String, val reminderType: PlayWidgetReminderType) : PlayViewerNewAction()
object DismissExploreWidget : PlayViewerNewAction()
object EmptyPageWidget : PlayViewerNewAction()
