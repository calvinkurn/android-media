package com.tokopedia.play.view.uimodel.event

import androidx.annotation.StringRes
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created by jegul on 29/06/21
 */
sealed class PlayViewerNewUiEvent

data class ShowWinningDialogEvent(val userImageUrl: String, val dialogTitle: String, val dialogSubtitle: String, val gameType: GameUiModel) : PlayViewerNewUiEvent()

data class ShowCoachMarkWinnerEvent(val title: String, val subtitle: UiString) : PlayViewerNewUiEvent()
object HideCoachMarkWinnerEvent : PlayViewerNewUiEvent()

data class OpenPageEvent(val applink: String, val params: List<String> = emptyList(), val requestCode: Int? = null, val pipMode: Boolean = false) : PlayViewerNewUiEvent()

data class ShowInfoEvent(val message: UiString) : PlayViewerNewUiEvent()
data class ShowErrorEvent(val error: Throwable, val errMessage: UiString? = null) : PlayViewerNewUiEvent()

data class CopyToClipboardEvent(val content: String) : PlayViewerNewUiEvent()

data class LoginEvent(val afterSuccess: () -> Unit) : PlayViewerNewUiEvent()

/**
 * Real Time Notification
 */
data class ShowRealTimeNotificationEvent(
        val notification: RealTimeNotificationUiModel,
) : PlayViewerNewUiEvent()

/**
 * Multiple Likes
 */
data class AnimateLikeEvent(val fromIsLiked: Boolean) : PlayViewerNewUiEvent()
object RemindToLikeEvent : PlayViewerNewUiEvent()
sealed class ShowLikeBubbleEvent : PlayViewerNewUiEvent() {

    abstract val count: Long
    abstract val reduceOpacity: Boolean

    data class Single(
        override val count: Long,
        override val reduceOpacity: Boolean,
        val config: PlayLikeBubbleConfig,
    ) : ShowLikeBubbleEvent()

    data class Burst(
        override val count: Long,
        override val reduceOpacity: Boolean,
        val config: PlayLikeBubbleConfig,
    ) : ShowLikeBubbleEvent()
}
data class PreloadLikeBubbleIconEvent(val urls: Set<String>) : PlayViewerNewUiEvent()

/**
 * Sharing Experience
 */
data class SaveTemporarySharingImage(val imageUrl: String): PlayViewerNewUiEvent()
data class OpenSharingOptionEvent(val title: String, val coverUrl: String, val userId: String, val channelId: String) : PlayViewerNewUiEvent()
data class OpenSelectedSharingOptionEvent(val linkerShareResult: LinkerShareResult?, val shareModel: ShareModel, val shareString: String): PlayViewerNewUiEvent()
object CloseShareExperienceBottomSheet: PlayViewerNewUiEvent()
object ErrorGenerateShareLink: PlayViewerNewUiEvent()

/**
 * Status
 */
data class BuySuccessEvent(
    val product: PlayProductUiModel.Product,
    val isVariant: Boolean,
    val cartId: String,
    val sectionInfo: ProductSectionUiModel.Section?,
    val isProductFeatured: Boolean,
) : PlayViewerNewUiEvent()
data class AtcSuccessEvent(
    val product: PlayProductUiModel.Product,
    val isVariant: Boolean,
    val cartId: String,
    val sectionInfo: ProductSectionUiModel.Section?,
    val isProductFeatured: Boolean,
) : PlayViewerNewUiEvent()
data class OCCSuccessEvent(
    val product: PlayProductUiModel.Product,
    val isVariant: Boolean,
    val cartId: String,
    val sectionInfo: ProductSectionUiModel.Section?,
    val isProductFeatured: Boolean,
) : PlayViewerNewUiEvent()

//---------------------

sealed class UiString {

    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}

data class AllowedWhenInactiveEvent(
    val event: PlayViewerNewUiEvent
) : PlayViewerNewUiEvent() {

    init {
        require(event !is AllowedWhenInactiveEvent)
    }
}


/**
 * Interactive
 * */
data class QuizAnsweredEvent(val isTrue: Boolean) : PlayViewerNewUiEvent()

object OpenKebabEvent : PlayViewerNewUiEvent()
object OpenUserReportEvent : PlayViewerNewUiEvent()

/**
 * CampaignReminder
 */
data class ChangeCampaignReminderSuccess(val isReminded: Boolean, val message: String) : PlayViewerNewUiEvent()
data class ChangeCampaignReminderFailed(val error: Throwable) : PlayViewerNewUiEvent()
