package com.tokopedia.play.view.event

import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    data class Init(val screenOrientation: ScreenOrientation, val stateHelper: StateHelperUiModel) : ScreenStateEvent()

    /**
     * Setter
     */
    data class SetVideo(val videoPlayer: VideoPlayerUiModel) : ScreenStateEvent()
    data class SetPartnerInfo(val partnerInfo: PartnerInfoUiModel): ScreenStateEvent()
    data class SetTotalCart(val cartUiModel: CartUiModel): ScreenStateEvent()
    data class SetTotalViews(val totalView: TotalViewUiModel): ScreenStateEvent()
    data class SetTotalLikes(val totalLikes: TotalLikeUiModel): ScreenStateEvent()
    data class SetPinned(val pinned: PinnedUiModel, val stateHelper: StateHelperUiModel) : ScreenStateEvent()
    data class SetQuickReply(val quickReply: QuickReplyUiModel) : ScreenStateEvent()
    data class SetProductSheet(val productResult: PlayResult<ProductSheetUiModel>) : ScreenStateEvent()
    data class SetVariantSheet(val variantResult: PlayResult<VariantSheetUiModel>) : ScreenStateEvent()
    data class SetVariantToaster(val toasterType: Int, val message: String, val actionText: String, val actionClickListener: View.OnClickListener) : ScreenStateEvent()
    data class SetChatList(val chatList: List<PlayChatUiModel>) : ScreenStateEvent()
    /**
     * Chat
     */
    data class IncomingChat(val chat: PlayChatUiModel) : ScreenStateEvent()
    object ComposeChat : ScreenStateEvent()
    /**
     * Like
     */
    data class LikeContent(val likeState: LikeStateUiModel, val isFirstTime: Boolean) : ScreenStateEvent()
    /**
     * Follow
     */
    data class FollowPartner(val shouldFollow: Boolean) : ScreenStateEvent()
    /**
     * Bottom Insets (Keyboard & BottomSheet)
     */
    data class BottomInsetsChanged(
            val insetsViewMap: Map<BottomInsetsType, BottomInsetsState>,
            val isAnyShown: Boolean,
            val isAnyHidden: Boolean,
            val stateHelper: StateHelperUiModel
    ) : ScreenStateEvent()
    /**
     * Video
     */
    data class VideoPropertyChanged(val videoProp: VideoPropertyUiModel, val stateHelper: StateHelperUiModel) : ScreenStateEvent()
    data class VideoStreamChanged(val videoStream: VideoStreamUiModel, val stateHelper: StateHelperUiModel) : ScreenStateEvent()
    /**
     * Room Event
     */
    data class OnNewPlayRoomEvent(val event: PlayRoomEvent) : ScreenStateEvent()
    /**
     * Immersive
     */
    data class ImmersiveStateChanged(val shouldImmersive: Boolean) : ScreenStateEvent()
    /**
     * Global Error
     */
    object ShowGlobalError : ScreenStateEvent()

    /**
     * Orientation Changed
     */
    data class OrientationChanged(val orientation: ScreenOrientation, val stateHelper: StateHelperUiModel) : ScreenStateEvent()

    /**
     * Analytics
     */
    data class YouTubeAnalyticsRequired(val analyticsBlock: (YouTubePlayer) -> Unit) : ScreenStateEvent()

    object OnNoMoreAction : ScreenStateEvent()
    data class ShowOneTapOnboarding(val stateHelper: StateHelperUiModel) : ScreenStateEvent()
}