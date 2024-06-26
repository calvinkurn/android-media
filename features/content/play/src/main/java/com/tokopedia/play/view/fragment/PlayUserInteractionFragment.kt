package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.feed.common.comment.PageSource
import com.tokopedia.feed.common.comment.analytic.ContentCommentAnalytics
import com.tokopedia.feed.common.comment.analytic.ContentCommentAnalyticsModel
import com.tokopedia.feed.common.comment.ui.ContentCommentBottomSheet
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.channel.analytic.PlayChannelAnalyticManager
import com.tokopedia.play.channel.ui.component.CommentIconUiComponent
import com.tokopedia.play.channel.ui.component.KebabIconUiComponent
import com.tokopedia.play.channel.ui.component.ProductCarouselUiComponent
import com.tokopedia.play.databinding.FragmentPlayInteractionBinding
import com.tokopedia.play.extensions.hasAlpha
import com.tokopedia.play.extensions.isAnyBottomSheetsShown
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isFullAlpha
import com.tokopedia.play.extensions.isFullSolid
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.explorewidget.PlayChannelRecommendationFragment
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.isEnableShareExPlay
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.util.measureWithTimeout
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.bottomsheet.PlayFollowBottomSheet
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayFullscreenManager
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.custom.dialog.InteractiveWinningDialogFragment
import com.tokopedia.play.view.dialog.PlayExploreWidgetFragment
import com.tokopedia.play.view.dialog.interactive.giveaway.InteractiveDialogFragment
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatHeightMapKey
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatHeightMapValue
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.PlayChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.PlayVideoBoundsProvider
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.VideoBoundsProvider
import com.tokopedia.play.view.measurement.layout.DynamicLayoutManager
import com.tokopedia.play.view.measurement.layout.PlayDynamicLayoutManager
import com.tokopedia.play.view.storage.multiplelikes.MultipleLikesIconCacheStorage
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.ScreenOrientation2
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.type.isCompact
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.action.ClickLikeAction
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.ClickShareAction
import com.tokopedia.play.view.uimodel.action.ClickSharingOptionAction
import com.tokopedia.play.view.uimodel.action.CloseSharingOptionAction
import com.tokopedia.play.view.uimodel.action.CommentVisibilityAction
import com.tokopedia.play.view.uimodel.action.InteractiveGameResultBadgeClickedAction
import com.tokopedia.play.view.uimodel.action.OpenFooterUserReport
import com.tokopedia.play.view.uimodel.action.OpenKebabAction
import com.tokopedia.play.view.uimodel.action.OpenPageResultAction
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.action.RetryGetTagItemsAction
import com.tokopedia.play.view.uimodel.action.ScreenshotTakenAction
import com.tokopedia.play.view.uimodel.action.SendWarehouseId
import com.tokopedia.play.view.uimodel.action.SharePermissionAction
import com.tokopedia.play.view.uimodel.action.ShowVariantAction
import com.tokopedia.play.view.uimodel.event.AnimateLikeEvent
import com.tokopedia.play.view.uimodel.event.CloseShareExperienceBottomSheet
import com.tokopedia.play.view.uimodel.event.CommentVisibilityEvent
import com.tokopedia.play.view.uimodel.event.CopyToClipboardEvent
import com.tokopedia.play.view.uimodel.event.ErrorGenerateShareLink
import com.tokopedia.play.view.uimodel.event.FailedFollow
import com.tokopedia.play.view.uimodel.event.HideCoachMarkWinnerEvent
import com.tokopedia.play.view.uimodel.event.LoginEvent
import com.tokopedia.play.view.uimodel.event.OpenKebabEvent
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.event.OpenSelectedSharingOptionEvent
import com.tokopedia.play.view.uimodel.event.OpenSharingOptionEvent
import com.tokopedia.play.view.uimodel.event.PreloadLikeBubbleIconEvent
import com.tokopedia.play.view.uimodel.event.RemindToLikeEvent
import com.tokopedia.play.view.uimodel.event.ShowCoachMarkWinnerEvent
import com.tokopedia.play.view.uimodel.event.ShowErrorEvent
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.event.ShowLikeBubbleEvent
import com.tokopedia.play.view.uimodel.event.ShowRealTimeNotificationEvent
import com.tokopedia.play.view.uimodel.event.ShowWinningDialogEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayerType
import com.tokopedia.play.view.uimodel.recom.interactive.InteractiveStateUiModel
import com.tokopedia.play.view.uimodel.recom.isGeneral
import com.tokopedia.play.view.uimodel.recom.shouldShow
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.view.uimodel.state.AddressWidgetUiState
import com.tokopedia.play.view.uimodel.state.EngagementUiState
import com.tokopedia.play.view.uimodel.state.PlayLikeUiState
import com.tokopedia.play.view.uimodel.state.PlayRtnUiState
import com.tokopedia.play.view.uimodel.state.PlayTitleUiState
import com.tokopedia.play.view.uimodel.state.PlayTotalViewUiState
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.uimodel.state.PlayWinnerBadgeUiState
import com.tokopedia.play.view.viewcomponent.ChatListViewComponent
import com.tokopedia.play.view.viewcomponent.ChooseAddressViewComponent
import com.tokopedia.play.view.viewcomponent.EmptyViewComponent
import com.tokopedia.play.view.viewcomponent.EndLiveInfoViewComponent
import com.tokopedia.play.view.viewcomponent.EngagementCarouselViewComponent
import com.tokopedia.play.view.viewcomponent.ExploreWidgetViewComponent
import com.tokopedia.play.view.viewcomponent.GradientBackgroundViewComponent
import com.tokopedia.play.view.viewcomponent.ImmersiveBoxViewComponent
import com.tokopedia.play.view.viewcomponent.LikeBubbleViewComponent
import com.tokopedia.play.view.viewcomponent.LikeCountViewComponent
import com.tokopedia.play.view.viewcomponent.LikeViewComponent
import com.tokopedia.play.view.viewcomponent.PinnedViewComponent
import com.tokopedia.play.view.viewcomponent.PlayButtonViewComponent
import com.tokopedia.play.view.viewcomponent.ProductSeeMoreViewComponent
import com.tokopedia.play.view.viewcomponent.QuickReplyViewComponent
import com.tokopedia.play.view.viewcomponent.SendChatViewComponent
import com.tokopedia.play.view.viewcomponent.ShareExperienceViewComponent
import com.tokopedia.play.view.viewcomponent.StatsInfoViewComponent
import com.tokopedia.play.view.viewcomponent.ToolbarRoomViewComponent
import com.tokopedia.play.view.viewcomponent.VideoControlViewComponent
import com.tokopedia.play.view.viewcomponent.VideoSettingsViewComponent
import com.tokopedia.play.view.viewcomponent.interactive.InteractiveGameResultViewComponent
import com.tokopedia.play.view.viewcomponent.partnerinfo.PartnerInfoViewComponent
import com.tokopedia.play.view.viewcomponent.realtimenotif.RealTimeNotificationViewComponent
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.util.ActivityResultHelper
import com.tokopedia.play_common.util.PerformanceClassConfig
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.awaitMeasured
import com.tokopedia.play_common.util.extension.dismissToaster
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.recreateView
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg.Companion.CHANNEL_KEY
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg.Companion.SHARE_ID_KEY
import com.tokopedia.shareexperience.ui.util.ShareExInitializer
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.play_common.R as play_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by jegul on 29/11/19
 */
class PlayUserInteractionFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val pipAnalytic: PlayPiPAnalytic,
    private val analytic: PlayAnalytic,
    private val multipleLikesIconCacheStorage: MultipleLikesIconCacheStorage,
    private val performanceClassConfig: PerformanceClassConfig,
    private val newAnalytic: PlayNewAnalytic,
    private val analyticManager: PlayChannelAnalyticManager,
    private val router: Router,
    private val commentAnalytics: ContentCommentAnalytics.Creator,
    private val abTestPlatform: AbTestPlatform
) :
    TkpdBaseV4Fragment(),
    PlayMoreActionBottomSheet.Listener,
    PlayFragmentContract,
    ToolbarRoomViewComponent.Listener,
    PartnerInfoViewComponent.Listener,
    VideoControlViewComponent.Listener,
    LikeViewComponent.Listener,
    ShareExperienceViewComponent.Listener,
    SendChatViewComponent.Listener,
    QuickReplyViewComponent.Listener,
    PinnedViewComponent.Listener,
    VideoSettingsViewComponent.Listener,
    ImmersiveBoxViewComponent.Listener,
    PlayButtonViewComponent.Listener,
    ProductSeeMoreViewComponent.Listener,
    InteractiveGameResultViewComponent.Listener,
    ChooseAddressViewComponent.Listener,
    EngagementCarouselViewComponent.Listener,
    ExploreWidgetViewComponent.Listener {
    private val viewSize by viewComponent { EmptyViewComponent(it, R.id.view_size) }
    private val gradientBackgroundView by viewComponent { GradientBackgroundViewComponent(it) }
    private val toolbarView by viewComponent { ToolbarRoomViewComponent(it, R.id.view_toolbar_room, this) }
    private val partnerInfoView by viewComponentOrNull { PartnerInfoViewComponent(it, this) }
    private val statsInfoView by viewComponent { StatsInfoViewComponent(it, R.id.view_stats_info) }
    private val videoControlView by viewComponent { VideoControlViewComponent(it, R.id.pcv_video, this) }
    private val likeView by viewComponent { LikeViewComponent(it, this) }
    private val likeCountView by viewComponent { LikeCountViewComponent(it) }
    private val shareExperienceView by viewComponentOrNull { ShareExperienceViewComponent(it, R.id.view_share_experience, childFragmentManager, this, this, requireContext()) }
    private val sendChatView by viewComponentOrNull { SendChatViewComponent(it, R.id.view_send_chat, this) }
    private val quickReplyView by viewComponentOrNull { QuickReplyViewComponent(it, R.id.rv_quick_reply, this) }
    private val chatListView by viewComponentOrNull { ChatListViewComponent(it, R.id.scrollable_host_chat) }
    private val pinnedView by viewComponentOrNull { PinnedViewComponent(it, R.id.view_pinned, this) }
    private val videoSettingsView by viewComponent { VideoSettingsViewComponent(it, R.id.view_video_settings, this) }
    private val immersiveBoxView by viewComponent { ImmersiveBoxViewComponent(it, R.id.v_immersive_box, this) }
    private val playButtonView by viewComponent { PlayButtonViewComponent(it, R.id.view_play_button, this) }
    private val endLiveInfoView by viewComponent { EndLiveInfoViewComponent(it, R.id.view_end_live_info) }

    private val rtnView by viewComponentOrNull { RealTimeNotificationViewComponent(it) }
    private val likeBubbleView by viewComponent {
        LikeBubbleViewComponent(
            it,
            R.id.view_like_bubble,
            viewLifecycleOwner.lifecycleScope,
            multipleLikesIconCacheStorage,
            performanceClassConfig
        )
    }
    private val productSeeMoreView by viewComponentOrNull(isEagerInit = true) { ProductSeeMoreViewComponent(it, R.id.view_product_see_more, this) }
    private val chooseAddressView by viewComponentOrNull { ChooseAddressViewComponent(it, this, childFragmentManager) }
    private val engagementCarouselView by viewComponentOrNull {
        EngagementCarouselViewComponent(
            listener = this,
            resId = R.id.scrollable_host_engagement,
            scope = viewLifecycleOwner.lifecycleScope,
            container = it
        )
    }

    /**
     * Interactive
     */
    private val interactiveResultView by viewComponentOrNull(isEagerInit = true) { InteractiveGameResultViewComponent(it, this, viewLifecycleOwner.lifecycleScope) }

    private val exploreView by viewComponentOrNull { ExploreWidgetViewComponent(it, this) }

    private val glQuick by viewComponentOrNull(isEagerInit = true) { EmptyViewComponent(it, R.id.gl_quick_reply) }

    private val activityResultHelper by lifecycleBound({
        ActivityResultHelper(this)
    })

    private val offset8 by lazy { requireContext().resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl3) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val container: View
        get() = requireView()

    private val bottomSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_BOTTOMSHEET_HEIGHT).toInt()

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private val orientationListener: PlayOrientationListener
        get() = requireActivity() as PlayOrientationListener

    private val playNavigation: PlayNavigation
        get() = requireActivity() as PlayNavigation

    private val playFullscreenManager: PlayFullscreenManager
        get() = requireActivity() as PlayFullscreenManager

    private val orientation: ScreenOrientation2
        get() = ScreenOrientation2.get(requireActivity())

    private val screenOrientationDataSource = object : ScreenOrientationDataSource {
        override fun getScreenOrientation(): ScreenOrientation {
            return orientation.orientation
        }
    }

    private var isOpened = false
    private var portraitInsets: WindowInsets? = null
    private var hasInvalidateChat = false

    private var videoBoundsProvider: VideoBoundsProvider? = null
    private var dynamicLayoutManager: DynamicLayoutManager? = null
    private var chatListHeightManager: ChatListHeightManager? = null

    private val chatListHeightMap = mutableMapOf<ChatHeightMapKey, ChatHeightMapValue>()

    private var mMaxTopChatMode: Int? = null
    private var toasterBottomMargin = 0

    private lateinit var onStatsInfoGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    private var localCache: LocalCacheModel = LocalCacheModel()

    private var commentAnalyticsModel: ContentCommentAnalyticsModel? = null

    /**
     * Animation
     */
    private val fadeInAnimation by viewLifecycleBound({ PlayFadeInAnimation(FADE_DURATION) })
    private val fadeOutAnimation by viewLifecycleBound({ PlayFadeOutAnimation(FADE_DURATION) })
    private val fadeInFadeOutAnimation by viewLifecycleBound(
        { PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY) }
    )
    private val delayFadeOutAnimation by viewLifecycleBound(
        { PlayDelayFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY) }
    )
    private val fadeAnimationList by viewLifecycleBound(
        { arrayOf(fadeInAnimation, fadeOutAnimation, fadeInFadeOutAnimation, delayFadeOutAnimation) },
        whenLifecycle {
            onDestroy { cancelAllAnimations() }
        }
    )

    private val interactiveDialogDataSource = object : InteractiveDialogFragment.DataSource {
        override fun getViewModelProvider(): ViewModelProvider {
            return getPlayViewModelProvider()
        }
    }

    private val commentEntrySource = object : ContentCommentBottomSheet.EntrySource {
        override fun getPageSource(): PageSource = PageSource.Play(channelId)
        override fun onCommentDismissed() {
            playViewModel.submitAction(CommentVisibilityAction(isOpen = false))
        }
    }

    override fun getScreenName(): String = "Play User Interaction"

    private val components = mutableListOf<UiComponent<PlayViewerNewUiState>>()

    private val eventBus = EventBus<Any>()

    private var shareExInitializer: ShareExInitializer? = null

    private var _binding: FragmentPlayInteractionBinding? = null
    private val binding: FragmentPlayInteractionBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = getPlayViewModelProvider().get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayInteractionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_interaction, container, false)
        _binding = FragmentPlayInteractionBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupInsets(view)
        setupObserve()

        invalidateSystemUiVisibility()
        initAddress()
    }

    override fun onStart() {
        super.onStart()
        viewSize.rootView.requestApplyInsetsWhenAttached()
        initAddress()
    }

    override fun onPause() {
        super.onPause()
        isOpened = false
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        analytic.clickWatchMode()
        triggerImmersive(container.isFullSolid)
        bottomSheet.dismiss()
    }

    override fun onPipClicked(bottomSheet: PlayMoreActionBottomSheet) {
        playViewModel.requestWatchInPiP()
        pipAnalytic.clickPiPIcon(
            channelId = channelId,
            shopId = playViewModel.partnerId,
            channelType = playViewModel.channelType
        )
        bottomSheet.dismiss()
    }

    override fun onSeePerformanceClicked(bottomSheet: PlayMoreActionBottomSheet) {
        router.route(context, playViewModel.performanceSummaryPageLink)
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            activityResultHelper.processResult(requestCode)
        }

        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else if (requestCode == REQUEST_CODE_ADDRESS_LIST && resultCode == Activity.RESULT_OK) {
            chooseAddressView?.hideBottomSheet()
            initAddress()
        } else {
            playViewModel.submitAction(
                OpenPageResultAction(
                    isSuccess = resultCode == Activity.RESULT_OK,
                    requestCode = requestCode
                )
            )
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        shareExperienceView?.handleRequestPermissionResult(requestCode, grantResults)
    }

    override fun onResume() {
        super.onResume()
        isOpened = true
        invalidateSystemUiVisibility()
        initAddress()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreateView()
    }

    override fun onDestroyView() {
        videoBoundsProvider = null
        dynamicLayoutManager = null
        chatListHeightManager = null

        hasInvalidateChat = false

        super.onDestroyView()
        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is InteractiveDialogFragment -> {
                childFragment.setDataSource(interactiveDialogDataSource)
            }
            is ContentCommentBottomSheet -> {
                childFragment.setEntrySource(commentEntrySource)
                commentAnalyticsModel?.let {
                    childFragment.setAnalytic(
                        commentAnalytics.create(
                            PageSource.Play(channelId),
                            model = it
                        )
                    )
                }
            }
            is PlayChannelRecommendationFragment -> {
                childFragment.setFactory(object : PlayChannelRecommendationFragment.Factory {
                    override fun getViewModelFactory(): ViewModelProvider = getPlayViewModelProvider()
                })
            }
        }
    }

    private fun getPlayViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            requireParentFragment(),
            (requireParentFragment() as PlayFragment).viewModelProviderFactory
        )
    }

    //region ComponentListener
    /**
     * Toolbar View Component Listener
     */
    override fun onBackButtonClicked(view: ToolbarRoomViewComponent) {
        doLeaveRoom()
    }

    override fun onShareIconClick(view: ShareExperienceViewComponent) {
        playViewModel.submitAction(ClickShareAction)
    }

    override fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel) {
        playViewModel.submitAction(ClickSharingOptionAction(shareModel, view.isScreenshotBottomSheet))
    }

    override fun onShareOptionClosed(view: ShareExperienceViewComponent) {
        playViewModel.submitAction(CloseSharingOptionAction(view.isScreenshotBottomSheet))
    }

    override fun onScreenshotTaken(view: ShareExperienceViewComponent) {
        playViewModel.submitAction(ScreenshotTakenAction)
    }

    override fun onSharePermissionAction(
        view: ShareExperienceViewComponent,
        label: String
    ) {
        playViewModel.submitAction(SharePermissionAction(label))
    }

    override fun onShareIconImpressed(view: ShareExperienceViewComponent) {
        // No tracker for live
    }

    override fun onPartnerInfoClicked(view: PartnerInfoViewComponent, applink: String) {
        playViewModel.submitAction(ClickPartnerNameAction(applink))
    }

    override fun onFollowImpressed(view: PartnerInfoViewComponent) {
        // No tracker for live
    }

    override fun onFollowButtonClicked(view: PartnerInfoViewComponent) {
        playViewModel.submitAction(PlayViewerNewAction.Follow)
    }

    /**
     * VideoControl View Component Listener
     */
    override fun onStartSeeking(view: VideoControlViewComponent) {
        onScrubStarted()
        eventBus.emit(Event.OnScrubStarted)
    }

    override fun onEndSeeking(view: VideoControlViewComponent) {
        onScrubEnded()
        eventBus.emit(Event.OnScrubEnded)
    }

    /**
     * Like View Component Listener
     */
    override fun onLikeClicked(view: LikeViewComponent) {
        playViewModel.submitAction(ClickLikeAction)
    }

    /**
     * SendChat View Component Listener
     */
    override fun onChatFormClicked(view: SendChatViewComponent) {
        doClickChatBox()
    }

    override fun onSendChatClicked(view: SendChatViewComponent, message: String) {
        analytic.clickSendChat()
        doSendChat(message)
    }

    /**
     * QuickReply View Component Listener
     */
    override fun onQuickReplyClicked(view: QuickReplyViewComponent, replyString: String) {
        analytic.clickQuickReply()
        doSendChat(replyString)
    }

    /**
     * Pinned View Component Listener
     */
    override fun onPinnedMessageActionClicked(view: PinnedViewComponent, appLink: String, message: String) {
        analytic.clickPinnedMessage(message, appLink)
        openPageByApplink(appLink, pipMode = true)
    }

    /**
     * VideoSettings View Component Listener
     */
    override fun onEnterFullscreen(view: VideoSettingsViewComponent) {
        analytic.clickCtaFullScreenFromPortraitToLandscape()
        enterFullscreen()
    }

    override fun onExitFullscreen(view: VideoSettingsViewComponent) {
        exitFullscreen()
    }

    /**
     * ImmersiveBox View Component Listener
     */
    override fun onImmersiveBoxClicked(view: ImmersiveBoxViewComponent, currentAlpha: Float) {
        if (playViewModel.hasNoMedia) return

        analytic.clickWatchArea(
            screenOrientation = orientation.orientation
        )
        triggerImmersive(currentAlpha == VISIBLE_ALPHA)
    }

    /**
     * PlayButton View Component Listener
     */
    override fun onButtonClicked(view: PlayButtonViewComponent) {
        playViewModel.startCurrentVideo()
    }

    /**
     * Product See More Listener
     */
    override fun onProductSeeMoreClick(view: ProductSeeMoreViewComponent) {
        openProductSheet()
        analytic.clickFeaturedProductSeeMore()
    }

    fun maxTopOnChatMode(maxTopPosition: Int) {
        mMaxTopChatMode = maxTopPosition
        if (!playViewModel.bottomInsets.isKeyboardShown) return
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
            invalidateChatListBounds(maxTopPosition = maxTopPosition)
        }
    }

    fun onStartAnimateInsets(isHidingInsets: Boolean) {
        view?.alpha = 0f
    }

    // TODO("Find better logic to improve this code")
    fun onFinishAnimateInsets(isHidingInsets: Boolean) {
        if (view == null) return
        /**
         * The first one is to handle fast changes when insets transition from show to hide
         */
        if (isHidingInsets) {
            viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
                invalidateChatListBounds(shouldForceInvalidate = true)
            }
        }
        view?.alpha = VISIBLE_ALPHA
        /**
         * The second one is to handle edge cases when somehow any interaction has changed while insets is shown
         */
        if (isHidingInsets) {
            viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
                invalidateChatListBounds(shouldForceInvalidate = true)
            }
        }
    }

    private fun setupView(view: View) {
        val productFeaturedBinding = binding.viewProductFeatured
        if (productFeaturedBinding != null) {
            components.add(
                ProductCarouselUiComponent(
                    binding = productFeaturedBinding,
                    bus = eventBus,
                    scope = viewLifecycleOwner.lifecycleScope,
                    dispatchers = dispatchers
                )
            )
        }

        val kebabIconBinding = binding.viewKebabMenu
        if (kebabIconBinding != null) {
            components.add(
                KebabIconUiComponent(
                    binding = kebabIconBinding,
                    bus = eventBus
                )
            )
        }

        val commentBinding = binding.grComment
        if (commentBinding != null) {
            components.add(
                CommentIconUiComponent(
                    group = commentBinding,
                    bus = eventBus
                )
            )
        }

        fun setupLandscapeView() {
            container.setOnTouchListener(PlayClickTouchListener(INTERACTION_TOUCH_CLICK_TOLERANCE))
            container.setOnClickListener {
                if (playViewModel.isFreezeOrBanned) return@setOnClickListener
                if (container.hasAlpha) triggerImmersive(it.isFullSolid)
            }
        }

        fun setupPortraitView() {
            container.setOnClickListener {
                if (playViewModel.isFreezeOrBanned) return@setOnClickListener
                if (!playViewModel.videoOrientation.isHorizontal && container.hasAlpha) triggerImmersive(it.isFullSolid)
            }
        }

        handleVideoHorizontalTopBounds()

        videoSettingsView.setFullscreen(orientation.isLandscape)

        if (orientation.isLandscape) {
            setupLandscapeView()
        } else {
            setupPortraitView()
        }

        setupFeaturedProductsFadingEdge(view)
    }

    private fun setupInsets(view: View) {
        /**
         * This is a temporary workaround for when insets not working as intended inside viewpager
         */
        val layoutParams = viewSize.rootView.layoutParams as? ViewGroup.MarginLayoutParams
        val initialBottomMargin = layoutParams?.bottomMargin ?: 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val rootInsets = activity?.window?.decorView?.rootWindowInsets
                if (portraitInsets == null && orientation.isPortrait) portraitInsets = rootInsets
                val insets = if (orientation.isPortrait) portraitInsets else rootInsets
                if (insets != null) {
                    layoutParams?.updateMargins(top = insets.systemWindowInsetTop, bottom = initialBottomMargin + insets.systemWindowInsetBottom)
                } else {
                    error("Insets not supported")
                }
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }

        viewSize.rootView.doOnApplyWindowInsets { v, insets, _, recordedMargin ->
            val skipTop = !isOpened && insets.systemWindowInsetTop == 0
            val skipBottom = !isOpened && insets.systemWindowInsetBottom == 0

            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams

            /**
             * Reduce margin by the first added value
             */
            val margin = recordedMargin.copy(bottom = initialBottomMargin)

            var isMarginChanged = false

            val newTopMargin = insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin && !skipTop) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                isMarginChanged = true
            }

            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin && !skipBottom) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                isMarginChanged = true
            }

            val newLeftMargin = insets.systemWindowInsetLeft
            if (marginLayoutParams.leftMargin != newLeftMargin) {
                marginLayoutParams.updateMargins(left = newLeftMargin)
                isMarginChanged = true
            }

            val newRightMargin = insets.systemWindowInsetRight
            if (marginLayoutParams.rightMargin != newRightMargin) {
                marginLayoutParams.updateMargins(right = newRightMargin)
                isMarginChanged = true
            }

            if (isMarginChanged) v.parent.requestLayout()
        }
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeVideoProperty()
        observeChannelInfo()
        observePinnedMessage()
        observeBottomInsetsState()

        observeUiState()
        observeUiEvent()
        observeViewEvent()

        observeChats()

        observeLoggedInInteractionEvent()

        observeAnalytic()

        components.forEach { lifecycle.addObserver(it) }
    }

    private fun invalidateSystemUiVisibility() {
        when {
            playViewModel.isFreezeOrBanned -> playFullscreenManager.onExitFullscreen()
            orientation.isLandscape && orientation.isCompact -> playFullscreenManager.onEnterFullscreen()
            !playViewModel.videoOrientation.isHorizontal && container.isFullAlpha -> playFullscreenManager.onEnterFullscreen()
            else -> playFullscreenManager.onExitFullscreen()
        }
    }

    private fun handleVideoHorizontalTopBounds() {
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            val toolbarMeasure = asyncCatchError(block = {
                measureWithTimeout { toolbarView.rootView.awaitMeasured() }
            }, onError = {})
            val statsInfoMeasure = asyncCatchError(block = {
                measureWithTimeout { statsInfoView.rootView.awaitMeasured() }
            }, onError = {})

            awaitAll(toolbarMeasure, statsInfoMeasure)
            playFragment.onFirstTopBoundsCalculated()
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner) { meta ->
            changeLayoutBasedOnVideoOrientation(meta.videoStream.orientation)
            triggerImmersive(false)

            viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
                playFragment.setCurrentVideoTopBounds(meta.videoStream.orientation, getVideoTopBounds(meta.videoStream.orientation))
                if (playViewModel.channelType.isLive) invalidateChatListBounds(videoOrientation = meta.videoStream.orientation, videoPlayer = meta.videoPlayer)
            }

            videoSettingsViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            gradientBackgroundViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            playButtonViewOnStateChanged(videoPlayer = meta.videoPlayer)

            pinnedViewOnStateChanged()

            changeLayoutBasedOnVideoType(meta.videoPlayer, playViewModel.channelType)
            if (meta.videoPlayer is PlayVideoPlayerUiModel.General.Complete) videoControlView.setPlayer(meta.videoPlayer.exoPlayer)
        }
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(
            viewLifecycleOwner,
            DistinctObserver {
                if (it.state == PlayViewerVideoState.Waiting ||
                    (it.state is PlayViewerVideoState.Buffer && it.state.bufferSource == BufferSource.Broadcaster)
                ) {
                    triggerImmersive(false)
                } else if (it.state == PlayViewerVideoState.Play) {
                    analytic.clickPlayVideo()
                } else if (it.state == PlayViewerVideoState.End) showInteractionIfWatchMode()

                playButtonViewOnStateChanged(state = it.state)
            }
        )
    }

    private fun observeChannelInfo() {
        playViewModel.observableChannelInfo.observe(
            viewLifecycleOwner,
            DistinctObserver {
                triggerStartMonitoring()
                statsInfoViewOnStateChanged(channelType = it.channelType)
                videoControlViewOnStateChanged(channelType = it.channelType)
                sendChatViewOnStateChanged(channelType = it.channelType)
                chatListViewOnStateChanged(channelType = it.channelType)

                pinnedViewOnStateChanged()
            }
        )
    }

    private fun observeChats() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.chats.collectLatest {
                chatListView?.setChatList(it)
            }
        }
    }

    private fun observePinnedMessage() {
        playViewModel.observablePinnedMessage.observe(
            viewLifecycleOwner,
            DistinctObserver {
                pinnedViewOnStateChanged(pinnedModel = it, shouldTriggerChatHeightCalculation = true)
            }
        )
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(
            viewLifecycleOwner,
            DistinctObserver { map ->
                if (playViewModel.videoOrientation.isVertical) triggerImmersive(false)

                val keyboardState = map[BottomInsetsType.Keyboard]
                if (keyboardState != null && !keyboardState.isPreviousStateSame) {
                    when (keyboardState) {
                        is BottomInsetsState.Shown -> {
                            pushParentPlayByKeyboardHeight(keyboardState.estimatedInsetsHeight)
                        }
                        else -> {
                            // no-op
                        }
                    }
                }

                if (map.isKeyboardShown) dismissToaster()

                gradientBackgroundViewOnStateChanged(bottomInsets = map)
                toolbarViewOnStateChanged(bottomInsets = map)
                partnerInfoViewOnStateChanged(bottomInsets = map)
                statsInfoViewOnStateChanged(bottomInsets = map)
                videoControlViewOnStateChanged(bottomInsets = map)
                sendChatViewOnStateChanged(bottomInsets = map)
                chatListViewOnStateChanged(bottomInsets = map)
                pinnedViewOnStateChanged(bottomInsets = map)
                videoSettingsViewOnStateChanged(bottomInsets = map)
                immersiveBoxViewOnStateChanged(bottomInsets = map)
            }
        )
    }
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                components.forEach { it.render(cachedState) }

                val state = cachedState.value
                val prevState = cachedState.prevValue

                renderToolbarView(state.title)
                renderShareView(state.channel, state.bottomInsets, state.status)
                renderPartnerInfoView(prevState?.partner, state.partner)
                renderLikeView(prevState?.like, state.like)
                renderLikeBubbleView(state.like)
                renderStatsInfoView(state.totalView)
                renderRealTimeNotificationView(state.rtn)
                renderViewAllProductView(state.tagItems, state.bottomInsets, state.address, state.status)
                renderQuickReplyView(prevState?.quickReply, state.quickReply, prevState?.bottomInsets, state.bottomInsets, state.channel)
                renderAddressWidget(state.address)

                renderInteractiveDialog(prevState?.interactive, state.interactive)
                renderWinnerBadge(state = state.winnerBadge)

                handleStatus(cachedState)
                handleAutoSwipe(cachedState)
                renderEngagement(prevState?.engagement, state.engagement)
                renderExploreView(cachedState)

                if (prevState?.tagItems?.product != state.tagItems.product &&
                    prevState?.tagItems?.voucher != state.tagItems.voucher
                ) {
                    viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) { invalidateChatListBounds() }
                }

                if (cachedState.isChanged { it.followPopUp }) renderFollowPopUp(state.followPopUp)
            }
        }
    }

    private fun renderEngagement(prevState: EngagementUiState?, currState: EngagementUiState) {
        if (prevState?.shouldShow != currState.shouldShow) {
            engagementCarouselView?.rootView?.showWithCondition(currState.shouldShow)
        }
        if (prevState?.data != currState.data) {
            engagementCarouselView?.setData(currState.data)
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowWinningDialogEvent -> {
                        if (container.alpha != VISIBLE_ALPHA) return@collect
                        getInteractiveWinningDialog().apply {
                            setData(imageUrl = event.userImageUrl, title = event.dialogTitle, subtitle = event.dialogSubtitle, game = event.gameType)
                        }.showNow(childFragmentManager)
                    }
                    is OpenPageEvent -> {
                        openPageByApplink(
                            applink = event.applink,
                            params = event.params.toTypedArray(),
                            requestCode = event.requestCode,
                            pipMode = event.pipMode
                        )
                    }
                    is LoginEvent -> {
                        val reqCode = activityResultHelper.generateRequestCode(event.afterSuccess)
                        openApplink(
                            ApplinkConst.LOGIN,
                            requestCode = reqCode
                        )
                    }
                    is ShowInfoEvent -> {
                        if (PlayExploreWidgetFragment.get(childFragmentManager) != null) {
                            return@collect
                        }

                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message)
                        )
                    }
                    is ShowErrorEvent -> {
                        if (InteractiveDialogFragment.get(childFragmentManager) != null) {
                            return@collect
                        }

                        val errMessage = if (event.errMessage == null) {
                            ErrorHandler.getErrorMessage(
                                context,
                                event.error,
                                ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                        } else {
                            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                                context,
                                event.error,
                                ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                            getString(
                                play_commonR.string.play_custom_error_handler_msg,
                                getTextFromUiString(event.errMessage),
                                errCode
                            )
                        }

                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = errMessage
                        )
                    }
                    is CopyToClipboardEvent -> copyToClipboard(event.content)
                    is ShowRealTimeNotificationEvent -> {
                        rtnView?.queueNotification(event.notification)
                    }
                    is AnimateLikeEvent -> {
                        likeView.playLikeAnimation()
                    }
                    is ShowLikeBubbleEvent -> {
                        if (event is ShowLikeBubbleEvent.Burst) {
                            likeBubbleView.shotBurst(event.count, event.reduceOpacity, event.config)
                        } else if (event is ShowLikeBubbleEvent.Single) {
                            likeBubbleView.shot(event.count, event.reduceOpacity, event.config)
                        }
                    }
                    RemindToLikeEvent -> likeView.playReminderAnimation()
                    is PreloadLikeBubbleIconEvent -> likeBubbleView.preloadIcons(event.urls)
                    is OpenSharingOptionEvent -> {
                        if (abTestPlatform.isEnableShareExPlay()) {
                            if (shareExInitializer == null) {
                                shareExInitializer = ShareExInitializer(requireContext())
                            }
                            val label = "$SHARE_ID_KEY - ${event.channelId} - ${event.partnerId} - ${event.channelType}"
                            val labelActionClick = "$CHANNEL_KEY - $SHARE_ID_KEY - ${event.channelId} - ${event.partnerId} - ${event.channelType}"
                            shareExInitializer?.openShareBottomSheet(
                                bottomSheetArg = ShareExBottomSheetArg.Builder(
                                    pageTypeEnum = ShareExPageTypeEnum.PLAY,
                                    defaultUrl = "tokopedia://play/$channelId",
                                    trackerArg = ShareExTrackerArg(
                                        utmCampaign = "Content-$SHARE_ID_KEY-play-${event.channelId}",
                                        labelActionClickShareIcon = label,
                                        labelActionCloseIcon = label,
                                        labelActionClickChannel = labelActionClick,
                                        labelImpressionBottomSheet = label
                                    )
                                )
                                    .withContentId(event.channelId)
                                    .withOrigin("play")
                                    .build()
                            )
                        } else {
                            shareExperienceView?.showSharingOptions(
                                event.title,
                                event.coverUrl,
                                event.userId,
                                event.channelId
                            )
                        }
                    }
                    is OpenSelectedSharingOptionEvent -> {
                        SharingUtil.executeShareIntent(event.shareModel, event.linkerShareResult, activity, view, event.shareString)
                    }
                    CloseShareExperienceBottomSheet -> shareExperienceView?.dismiss()
                    ErrorGenerateShareLink -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getString(R.string.play_sharing_error_generate_link),
                            actionText = getString(R.string.play_sharing_refresh)
                        )
                    }
                    OpenKebabEvent -> {
                        playViewModel.onShowKebabMenuSheet()
                        showMoreActionBottomSheet()
                    }
                    is ShowCoachMarkWinnerEvent -> {
                        if (interactiveResultView?.isHidden() == true || container.alpha != VISIBLE_ALPHA) return@collect
                        interactiveResultView?.showCoachMark(event.title, getTextFromUiString(event.subtitle))
                    }
                    HideCoachMarkWinnerEvent -> {
                        interactiveResultView?.hideCoachMark()
                    }
                    FailedFollow -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = getString(R.string.play_failed_follow),
                            actionText = getString(play_commonR.string.play_interactive_retry),
                            clickListener = {
                                newAnalytic.clickRetryToasterPopUp(channelId, channelType = playViewModel.channelType.value)
                                playViewModel.submitAction(PlayViewerNewAction.FollowInteractive)
                            }
                        )
                    }
                    is CommentVisibilityEvent -> {
                        val sheet = ContentCommentBottomSheet.getOrCreate(
                            childFragmentManager,
                            requireActivity().classLoader
                        )
                        commentAnalyticsModel =
                            ContentCommentAnalyticsModel(
                                eventCategory = "groupchat room",
                                eventLabel = "$channelId - ${playViewModel.partnerId}"
                            )
                        if (event.isOpen) sheet.show(childFragmentManager) else sheet.dismiss()
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        }
    }

    private fun observeViewEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            eventBus.subscribe().collect { event ->
                when (event) {
                    is ProductCarouselUiComponent.Event -> onProductCarouselEvent(event)
                    is KebabIconUiComponent.Event -> onKebabIconEvent(event)
                    is CommentIconUiComponent.Event -> onCommentIconEvent(event)
                }
            }
        }
    }

    private fun observeAnalytic() {
        analyticManager.observe(
            viewLifecycleOwner.lifecycleScope,
            eventBus,
            playViewModel.uiState,
            playViewModel.uiEvent,
            viewLifecycleOwner.lifecycle
        )
    }
    //endregion

    private fun setupFeaturedProductsFadingEdge(view: View) {
        view.doOnLayout {
            eventBus.emit(
                Event.OnFadingEdgeMeasured(
                    widthFromEnd = (FADING_EDGE_PRODUCT_FEATURED_WIDTH_MULTIPLIER * it.width).toInt()
                )
            )
        }
    }

    private fun triggerImmersive(shouldImmersive: Boolean) {
        cancelAllAnimations()

        fun triggerFullImmersive(shouldImmersive: Boolean, fadeOutAfterFadeIn: Boolean) {
            val animation = when {
                shouldImmersive -> fadeOutAnimation
                fadeOutAfterFadeIn -> fadeInFadeOutAnimation
                else -> fadeInAnimation
            }

            animation.start(container)
        }

        fun isBroadcasterLoading(): Boolean {
            val videoState = playViewModel.viewerVideoState
            return (
                videoState == PlayViewerVideoState.Waiting ||
                    (videoState is PlayViewerVideoState.Buffer && videoState.bufferSource == BufferSource.Broadcaster)
                )
        }

        when {
            playViewModel.isFreezeOrBanned || isBroadcasterLoading() -> {
                container.alpha = VISIBLE_ALPHA
                playFullscreenManager.onExitFullscreen()
            }
            orientation.isLandscape && orientation.isCompact -> triggerFullImmersive(shouldImmersive, true)
            playViewModel.videoOrientation.isHorizontal -> handleVideoHorizontalImmersive(shouldImmersive)
            playViewModel.videoOrientation.isVertical -> {
                if (shouldImmersive) {
                    interactiveResultView?.hideCoachMark()
                    playFullscreenManager.onEnterFullscreen()
                } else {
                    playFullscreenManager.onExitFullscreen()
                }
                triggerFullImmersive(shouldImmersive, false)
            }
        }
    }

    private fun triggerStartMonitoring() {
        playFragment.startRenderMonitoring()

        if (!this::onStatsInfoGlobalLayoutListener.isInitialized) {
            onStatsInfoGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (isAdded) {
                        playFragment.stopRenderMonitoring()
                        statsInfoView.rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
            statsInfoView.rootView.viewTreeObserver.addOnGlobalLayoutListener(onStatsInfoGlobalLayoutListener)
        }
    }

    private fun onScrubStarted() {
        pinnedView?.setTransparent(true)

        if (!orientation.isLandscape || !orientation.isCompact) return

        cancelAllAnimations()
        fadeInAnimation.start(container)
    }

    private fun onScrubEnded() {
        pinnedView?.setTransparent(false)

        if (!orientation.isLandscape || !orientation.isCompact) return

        cancelAllAnimations()
        delayFadeOutAnimation.start(container)
    }

    private fun enterFullscreen() {
        orientationListener.changeOrientation(ScreenOrientation.Landscape, isTilting = false)
    }

    private fun exitFullscreen() {
        orientationListener.changeOrientation(ScreenOrientation.Portrait, isTilting = false)
    }

    private fun doLeaveRoom() {
        playNavigation.onBackPressed(isSystemBack = false)
    }

    // TODO("This action is duplicated with the one in PlayBottomSheetFragment, find a way to prevent duplication")
    private fun doOpenProductDetail(product: PlayProductUiModel.Product, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            analytic.clickProduct(product, ProductSectionUiModel.Section.Empty, position)
            openPageByApplink(product.applink, pipMode = true)
        }
    }

    private fun handleVideoHorizontalImmersive(shouldImmersive: Boolean) {
        if (shouldImmersive) {
            videoSettingsView.fadeOut()
            immersiveBoxView.fadeOut()
        } else {
            videoSettingsView.fadeIn()
            immersiveBoxView.fadeIn()
        }
    }

    private fun showMoreActionBottomSheet() {
        if (!bottomSheet.isVisible) getBottomSheetInstance().show(childFragmentManager)
    }

    private fun doClickChatBox() {
        interactiveResultView?.hideCoachMark()
        viewModel.doInteractionEvent(InteractionEvent.SendChat)
    }

    private fun doSendChat(message: String) {
        playViewModel.sendChat(message)
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun handleInteractionEvent(event: InteractionEvent) {
        when (event) {
            InteractionEvent.SendChat -> shouldComposeChat()
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.position)
            else -> {
                // no-op
            }
        }
    }

    private fun shouldComposeChat() {
        sendChatView?.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && playViewModel.isPiPAllowed && !playViewModel.isFreezeOrBanned) {
            playViewModel.requestPiPBrowsingPage(
                OpenApplinkUiModel(applink = applink, params = params.toList(), requestCode, shouldFinish)
            )
        } else {
            openApplink(applink, *params, requestCode = requestCode, shouldFinish = shouldFinish)
        }
    }

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            router.route(context, applink, *params)
        } else {
            val intent = router.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun getBottomSheetInstance(): PlayMoreActionBottomSheet {
        if (!::bottomSheet.isInitialized) {
            bottomSheet = childFragmentManager.fragmentFactory.instantiate(requireActivity().classLoader, PlayMoreActionBottomSheet::class.java.name) as PlayMoreActionBottomSheet
            bottomSheet.mListener = this
            bottomSheet.setShowListener { bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        }

        return bottomSheet
    }

    fun hideBottomSheet() {
        val bottomSheet = getBottomSheetInstance()
        if (bottomSheet.isVisible) {
            bottomSheet.dismiss()
        }
    }

    private fun showInteractionIfWatchMode() {
        view?.performClick()
    }

    private fun openProductSheet() {
        interactiveResultView?.hideCoachMark()
        playViewModel.onShowProductSheet(bottomSheetMaxHeight)
    }

    private fun pushParentPlayByKeyboardHeight(estimatedKeyboardHeight: Int) {
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
            playFragment.onBottomInsetsViewShown(
                getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight)
            )

            if (playViewModel.videoOrientation.isVertical && orientation.isPortrait) {
                chatListView?.setMaxHeight(
                    requireContext().resources.getDimensionPixelSize(
                        R.dimen.play_chat_vertical_max_height
                    ).toFloat()
                )
            }
        }
    }

    private fun cancelAllAnimations() {
        fadeAnimationList.forEach { it.cancel() }
    }

    private suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return getVideoBoundsProvider().getVideoTopBounds(videoOrientation)
    }

    private suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int): Int {
        return try {
            getVideoBoundsProvider().getVideoBottomBoundsOnKeyboardShown(
                requireView(),
                estimatedKeyboardHeight,
                playViewModel.videoOrientation
            )
        } catch (e: Throwable) { getScreenHeight() }
    }

    private suspend fun invalidateChatListBounds(
        videoOrientation: VideoOrientation = playViewModel.videoOrientation,
        videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        maxTopPosition: Int = mMaxTopChatMode ?: 0,
        shouldForceInvalidate: Boolean = false
    ) {
        if (!playViewModel.channelType.isLive) return

        val hasQuickReply = playViewModel.quickReply.quickReplyList.isNotEmpty()

        val hasProductFeatured = binding.viewProductFeatured?.root?.isVisible == true

        if (bottomInsets.isKeyboardShown) {
            getChatListHeightManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
        } else {
            getChatListHeightManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer, shouldForceInvalidate, hasProductFeatured)
        }
    }

    private fun changeLayoutBasedOnVideoOrientation(videoOrientation: VideoOrientation) {
        getDynamicLayoutManager().onVideoOrientationChanged(videoOrientation)
    }

    private fun changeLayoutBasedOnVideoType(videoPlayer: PlayVideoPlayerUiModel, channelType: PlayChannelType) {
        getDynamicLayoutManager().onVideoPlayerChanged(videoPlayer, channelType)
    }

    private fun getDynamicLayoutManager(): DynamicLayoutManager = synchronized(this) {
        if (dynamicLayoutManager == null) {
            dynamicLayoutManager = PlayDynamicLayoutManager(container as ViewGroup, screenOrientationDataSource)
        }
        return dynamicLayoutManager!!
    }

    private fun getVideoBoundsProvider(): VideoBoundsProvider = synchronized(this) {
        if (videoBoundsProvider == null) {
            videoBoundsProvider = PlayVideoBoundsProvider(container as ViewGroup, screenOrientationDataSource)
        }
        return videoBoundsProvider!!
    }

    private fun getChatListHeightManager(): ChatListHeightManager = synchronized(this) {
        if (chatListHeightManager == null) {
            chatListHeightManager = PlayChatListHeightManager(requireView() as ViewGroup, screenOrientationDataSource, chatListHeightMap)
        }
        return chatListHeightManager!!
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-room", content))
    }

    private fun doShowToaster(
        toasterType: Int = Toaster.TYPE_NORMAL,
        message: String,
        actionText: String = "",
        clickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        if (toasterBottomMargin == 0) {
            val likeAreaView = likeView.rootView
            val likeAreaBottomMargin = (likeAreaView.layoutParams as? ViewGroup.MarginLayoutParams)
                ?.bottomMargin ?: 0
            toasterBottomMargin = likeAreaView.height + likeAreaBottomMargin
        }
        Toaster.toasterCustomBottomHeight = toasterBottomMargin
        Toaster.build(
            container,
            message,
            type = toasterType,
            actionText = actionText,
            clickListener = clickListener
        ).show()
    }

    private fun isAllowAutoSwipe(status: Boolean) =
        status && !playViewModel.bottomInsets.isAnyShown &&
            playNavigation.canNavigateNextPage() &&
            !playViewModel.isAnyBottomSheetsShown &&
            playViewModel.uiState.value.status.channelStatus.statusSource == PlayStatusSource.Socket &&
            lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

    private fun doAutoSwipe() {
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            delay(AUTO_SWIPE_DELAY)
            playNavigation.navigateToNextPage()
        }
    }

    //region OnStateChanged
    private fun playButtonViewOnStateChanged(
        channelType: PlayChannelType = playViewModel.channelType,
        videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
        state: PlayViewerVideoState = playViewModel.observableVideoProperty.value?.state ?: PlayViewerVideoState.Unknown
    ) {
        if (!channelType.isVod || (videoPlayer is PlayVideoPlayerUiModel.General.Complete && videoPlayer.playerType != PlayerType.Client)) {
            playButtonView.hide()
            return
        }
        when (state) {
            PlayViewerVideoState.Pause -> playButtonView.showPlayButton()
            PlayViewerVideoState.End -> {
                if (playViewModel.bottomInsets.isAnyShown || !playNavigation.canNavigateNextPage()) playButtonView.showRepeatButton()
            }
            else -> playButtonView.hide()
        }
    }

    private fun videoControlViewOnStateChanged(
        channelType: PlayChannelType = playViewModel.channelType,
        videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            videoControlView.setPlayer(null)
            videoControlView.hide()
        } else if (channelType.isVod && videoPlayer.isGeneral() && !bottomInsets.isAnyShown) {
            videoControlView.show()
        } else {
            videoControlView.hide()
        }
    }

    private fun chatListViewOnStateChanged(
        channelType: PlayChannelType = playViewModel.channelType,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive && !bottomInsets.isAnyBottomSheetsShown) chatListView?.show() else chatListView?.hide()
    }

    private fun videoSettingsViewOnStateChanged(
        videoOrientation: VideoOrientation = playViewModel.videoOrientation,
        videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            videoSettingsView.hide()
        } else if (
            videoOrientation.isHorizontal &&
            videoPlayer.isGeneral() &&
            orientation.isCompact &&
            !bottomInsets.isAnyShown
        ) {
            videoSettingsView.show()
        } else {
            videoSettingsView.hide()
        }
    }

    private fun pinnedViewOnStateChanged(
        pinnedModel: PinnedMessageUiModel? = playViewModel.observablePinnedMessage.value,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
        shouldTriggerChatHeightCalculation: Boolean = false
    ) {
        if (isFreezeOrBanned) {
            pinnedView?.hide()
            return
        }

        if (pinnedModel != null) {
            pinnedView?.setPinnedMessage(pinnedModel)
            if (pinnedModel.shouldShow && !bottomInsets.isAnyShown) {
                pinnedView?.show()
            } else {
                pinnedView?.hide()
            }
        } else {
            pinnedView?.hide()
        }

        if (shouldTriggerChatHeightCalculation) viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) { invalidateChatListBounds(shouldForceInvalidate = true) }
    }

    private fun gradientBackgroundViewOnStateChanged(
        videoOrientation: VideoOrientation = playViewModel.videoOrientation,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (bottomInsets.isAnyShown ||
            (videoOrientation.isHorizontal && orientation.isPortrait)
        ) {
            gradientBackgroundView.hide()
        } else if (isFreezeOrBanned) {
            gradientBackgroundView.showTop()
        } else {
            gradientBackgroundView.show()
        }
    }

    private fun toolbarViewOnStateChanged(
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
        isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            toolbarView.show()
        } else if (!bottomInsets.isAnyShown && (orientation.isPortrait || !orientation.isCompact)) {
            toolbarView.show()
        } else {
            toolbarView.hide()
        }
    }

    private fun partnerInfoViewOnStateChanged(
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (!bottomInsets.isAnyShown && (orientation.isPortrait || !orientation.isCompact)) {
            partnerInfoView?.show()
        } else {
            partnerInfoView?.hide()
        }
    }

    private fun statsInfoViewOnStateChanged(
        channelType: PlayChannelType = playViewModel.channelType,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        statsInfoView.setLiveBadgeVisibility(channelType.isLive)

        if (!bottomInsets.isAnyShown && (orientation.isPortrait || !orientation.isCompact)) {
            statsInfoView.show()
        } else {
            statsInfoView.hide()
        }
    }

    private fun sendChatViewOnStateChanged(
        channelType: PlayChannelType = playViewModel.channelType,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive &&
            bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
            bottomInsets[BottomInsetsType.CouponSheet]?.isShown == false &&
            bottomInsets[BottomInsetsType.LeaderboardSheet]?.isShown == false
        ) {
            sendChatView?.show()
        } else {
            sendChatView?.invisible()
        }

        sendChatView?.focusChatForm(channelType.isLive && bottomInsets[BottomInsetsType.Keyboard] is BottomInsetsState.Shown)
    }

    private fun immersiveBoxViewOnStateChanged(
        bottomInsets: Map<BottomInsetsType, BottomInsetsState>
    ) {
        if (bottomInsets.isAnyShown) immersiveBoxView.hide() else immersiveBoxView.show()
    }

    private fun endLiveInfoViewOnStateChanged(
        event: PlayStatusUiModel
    ) {
        when (event.channelStatus.statusType) {
            PlayStatusType.Freeze -> {
                endLiveInfoView.setInfo(title = event.config.freezeModel.title)
                endLiveInfoView.show()
            }
            PlayStatusType.Archived -> {
                endLiveInfoView.setInfo(title = getString(R.string.play_archived_title))
                endLiveInfoView.showToaster(text = getString(R.string.play_archived_description))
                endLiveInfoView.show()
            }
            else -> endLiveInfoView.hide()
        }
    }

    private fun renderInteractiveDialog(
        prevState: InteractiveStateUiModel?,
        state: InteractiveStateUiModel
    ) {
        if (prevState == null ||
            prevState.isPlaying == state.isPlaying
        ) {
            return
        }

        if (state.isPlaying) {
            InteractiveDialogFragment.getOrCreate(
                childFragmentManager,
                requireActivity().classLoader
            ).showNow(childFragmentManager)
        } else if (InteractiveDialogFragment.get(childFragmentManager)?.isAdded == true) {
            InteractiveDialogFragment.getOrCreate(
                childFragmentManager,
                requireActivity().classLoader
            ).dismiss()
        }
    }

    private fun renderWinnerBadge(state: PlayWinnerBadgeUiState) {
        if (state.shouldShow) {
            interactiveResultView?.show()
            if (playViewModel.gameData is GameUiModel.Unknown) return
            newAnalytic.impressWinnerBadge(shopId = playViewModel.partnerId.toString(), interactiveId = playViewModel.gameData.id, channelId = playViewModel.channelId)
        } else {
            interactiveResultView?.hide()
        }
    }

    private fun renderToolbarView(title: PlayTitleUiState) {
        toolbarView.setTitle(title.title)
    }

    private fun renderShareView(
        channel: PlayChannelDetailUiModel,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
        status: PlayStatusUiModel
    ) {
        if (channel.shareInfo.shouldShow &&
            !bottomInsets.isAnyShown &&
            status.channelStatus.statusType.isActive
        ) {
            shareExperienceView?.show()
        } else {
            shareExperienceView?.hide()
        }
    }

    private fun renderPartnerInfoView(
        prevState: PlayPartnerInfo?,
        state: PlayPartnerInfo
    ) {
        if (prevState == state) return
        partnerInfoView?.setInfo(state)
    }

    private fun renderLikeView(
        prevState: PlayLikeUiState?,
        likeState: PlayLikeUiState
    ) {
        if (prevState?.canLike != likeState.canLike) likeView.setEnabled(isEnabled = likeState.canLike)

        if (prevState?.isLiked != likeState.isLiked) {
            likeView.setIsLiked(likeState.isLiked)
        }

        likeCountView.setTotalLikes(likeState.totalLike)

        if (likeState.shouldShow) {
            likeView.show()
            likeCountView.show()
        } else {
            likeView.hide()
            likeCountView.hide()
        }
    }

    private fun renderLikeBubbleView(likeState: PlayLikeUiState) {
        if (likeState.canShowBubble) {
            likeBubbleView.show()
        } else {
            likeBubbleView.hide()
        }
    }

    private fun renderStatsInfoView(totalView: PlayTotalViewUiState) {
        statsInfoView.setTotalViews(totalView.viewCountStr)
    }

    private fun renderRealTimeNotificationView(rtn: PlayRtnUiState) {
        rtnView?.setLifespan(rtn.lifespanInMs)
        if (rtn.shouldShow) {
            rtnView?.show()
        } else {
            rtnView?.invisible()
        }
    }

    private fun renderViewAllProductView(
        tagItem: TagItemUiModel,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
        address: AddressWidgetUiState,
        status: PlayStatusUiModel
    ) {
        val productListSize = tagItem.product.productSectionList.filterIsInstance<ProductSectionUiModel.Section>().sumOf {
            it.productList.size
        }

        if (!bottomInsets.isAnyShown && !address.shouldShow && productListSize.isMoreThanZero() && status.channelStatus.statusType.isActive) {
            productSeeMoreView?.show()
        } else {
            productSeeMoreView?.hide()
        }

        productSeeMoreView?.setTotalProduct(productListSize)
    }

    private fun renderQuickReplyView(
        prevQuickReply: PlayQuickReplyInfoUiModel?,
        quickReply: PlayQuickReplyInfoUiModel,
        prevBottomInsets: Map<BottomInsetsType, BottomInsetsState>?,
        bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
        channelDetail: PlayChannelDetailUiModel
    ) {
        if (prevQuickReply != quickReply) {
            quickReplyView?.setQuickReply(quickReply)
        }

        if (bottomInsets.isKeyboardShown &&
            channelDetail.channelInfo.channelType.isLive
        ) {
            quickReplyView?.showIfNotEmpty()
        } else {
            quickReplyView?.hide()
        }

        if (prevBottomInsets?.isKeyboardShown != bottomInsets.isKeyboardShown) {
            changeQuickReplyConstraint(isShown = bottomInsets.isKeyboardShown)
        }
    }

    private fun renderAddressWidget(addressUiState: AddressWidgetUiState) {
        chooseAddressView?.rootView?.showWithCondition(addressUiState.shouldShow)
    }

    private fun handleStatus(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged { it.status.channelStatus.statusType }) return

        val status = state.value.status
        getBottomSheetInstance().setState(status.channelStatus.statusType.isFreeze)

        if (status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isBanned || status.channelStatus.statusType.isArchive) {
            likeCountView.hide()
            likeView.hide()
            quickReplyView?.hide()
            chatListView?.hide()
            pinnedView?.hide()
            immersiveBoxView.hide()
            playButtonView.hide()
            shareExperienceView?.hide()

            videoControlViewOnStateChanged(isFreezeOrBanned = true)

            sendChatView?.focusChatForm(false)
            sendChatView?.invisible()

            videoSettingsViewOnStateChanged(isFreezeOrBanned = true)
            toolbarViewOnStateChanged(isFreezeOrBanned = true)
            pinnedViewOnStateChanged(isFreezeOrBanned = true)

            /**
             * Non view component
             */
            hideBottomSheet()
            cancelAllAnimations()

            triggerImmersive(false)
        }

        endLiveInfoViewOnStateChanged(event = status)
    }

    private fun handleAutoSwipe(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged(
                { it.combinedState.videoProperty },
                { it.status },
                { it.channel.channelInfo.channelType.isLive }
            )
        ) {
            return
        }

        val isLive = state.value.channel.channelInfo.channelType.isLive

        if (isAllowAutoSwipe(
                if (isLive) {
                    !state.value.status.channelStatus.statusType.isActive
                } else {
                    !state.value.status.channelStatus.statusType.isActive ||
                        state.value.combinedState.videoProperty.state == PlayViewerVideoState.End
                }
            )
        ) {
            doAutoSwipe()
        }
    }

    private fun getTextFromUiString(uiString: UiString): String {
        return when (uiString) {
            is UiString.Text -> uiString.text
            is UiString.Resource -> getString(uiString.resource)
        }
    }

    /**
     * Change constraint
     */
    private fun changeQuickReplyConstraint(isShown: Boolean) {
        /**
         * This can be solved by a simple barrier, but the barrier only work on testapp, not customerapp
         * and I don't know why arghhh
         */
        val quickReplyViewId = quickReplyView?.id ?: return
        val glScreen = this.glQuick ?: return
        view?.changeConstraint {
            if (isShown) {
                sendChatView?.let {
                    connect(quickReplyViewId, ConstraintSet.BOTTOM, it.id, ConstraintSet.TOP, offset8)
                }
            } else {
                connect(quickReplyViewId, ConstraintSet.BOTTOM, glScreen.id, ConstraintSet.TOP)
            }
        }
    }

    /**
     * Dialog
     */
    private fun getInteractiveWinningDialog(): InteractiveWinningDialogFragment {
        val existing = InteractiveWinningDialogFragment.get(childFragmentManager)
        return existing ?: childFragmentManager.fragmentFactory.instantiate(requireActivity().classLoader, InteractiveWinningDialogFragment::class.java.name) as InteractiveWinningDialogFragment
    }

    /***
     * Choose Address
     */

    private fun initAddress() {
        if (ChooseAddressUtils.isLocalizingAddressHasUpdated(context = requireContext(), localizingAddressStateData = localCache)) {
            localCache = ChooseAddressUtils.getLocalizingAddressData(context = requireContext())
            val warehouseId = localCache.warehouses.find {
                it.service_type == localCache.service_type
            }?.warehouse_id ?: 0

            playViewModel.submitAction(SendWarehouseId(isOOC = localCache.isOutOfCoverage(), id = warehouseId.toString()))
        }
    }

    override fun onAddressUpdated(view: ChooseAddressViewComponent) {
        initAddress()
        playViewModel.submitAction(RetryGetTagItemsAction)
    }

    override fun onInfoClicked(view: ChooseAddressViewComponent) {
        newAnalytic.clickInfoAddressWidgetNow()
        playViewModel.submitAction(
            OpenFooterUserReport(
                TokopediaUrl.getInstance().WEB +
                    getString(R.string.play_tokonow_info_weblink)
            )
        )
    }

    override fun onImpressedAddressWidget(view: ChooseAddressViewComponent) {
        newAnalytic.impressAddressWidgetNow()
    }

    override fun onImpressedBtnChoose(view: ChooseAddressViewComponent) {
        newAnalytic.impressChooseAddressNow()
    }

    override fun onBtnChooseClicked(view: ChooseAddressViewComponent) {
        newAnalytic.clickChooseAddressNow()
    }

    override fun onGameResultClicked(view: InteractiveGameResultViewComponent) {
        playViewModel.submitAction(InteractiveGameResultBadgeClickedAction(bottomSheetMaxHeight))
        newAnalytic.clickWinnerBadge(
            shopId = playViewModel.partnerId.toString(),
            interactiveId = playViewModel.gameData.id,
            gameType = playViewModel.gameData,
            channelId = playViewModel.channelId,
            channelType = playViewModel.channelType
        )
    }

    /**
     * View Event
     */
    private fun onProductCarouselEvent(event: ProductCarouselUiComponent.Event) {
        when (event) {
            is ProductCarouselUiComponent.Event.OnTransactionClicked -> {
                if (event.product.isVariantAvailable) {
                    playViewModel.submitAction(ShowVariantAction(event.product, true))
                } else {
                    playViewModel.submitAction(
                        when (event.action) {
                            ProductAction.Buy -> PlayViewerNewAction.BuyProduct(
                                event.product,
                                isProductFeatured = true
                            )
                            ProductAction.AddToCart -> PlayViewerNewAction.AtcProduct(
                                event.product,
                                isProductFeatured = true
                            )
                            ProductAction.OCC -> PlayViewerNewAction.OCCProduct(
                                event.product,
                                isProductFeatured = true
                            )
                        }
                    )
                }
            }
            is ProductCarouselUiComponent.Event.OnClicked -> {
                if (event.product.applink == null) return
                openPageByApplink(
                    event.product.applink,
                    pipMode = true
                )
            }
            else -> {}
        }
    }

    private fun onKebabIconEvent(event: KebabIconUiComponent.Event) {
        when (event) {
            KebabIconUiComponent.Event.OnClicked -> {
                playViewModel.submitAction(OpenKebabAction)
            }
            else -> {
                // no-op
            }
        }
    }

    private fun renderFollowPopUp(shouldShow: Boolean) {
        if (shouldShow) {
            PlayFollowBottomSheet.getOrCreate(
                childFragmentManager,
                classLoader = requireActivity().classLoader
            )
                .show(childFragmentManager)
        }
    }

    /**
     * Engagement Listener
     */
    override fun onWidgetGameEnded(
        view: EngagementCarouselViewComponent,
        engagement: EngagementUiModel.Game
    ) {
        when (engagement.game) {
            is GameUiModel.Giveaway -> {
                handleGiveaway(game = engagement.game)
            }
            is GameUiModel.Quiz -> {
                handleQuiz(game = engagement.game)
            }
            else -> {
                // no-op
            }
        }
    }

    /**
     * When game ended
     */
    private fun handleGiveaway(game: GameUiModel.Giveaway) {
        when (game.status) {
            is GameUiModel.Giveaway.Status.Upcoming -> {
                playViewModel.submitAction(PlayViewerNewAction.GiveawayUpcomingEnded)
            }
            is GameUiModel.Giveaway.Status.Ongoing ->
                playViewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
            else -> {
                // no-op
            }
        }
    }

    /**
     * When game ended
     */
    private fun handleQuiz(game: GameUiModel.Quiz) {
        when (game.status) {
            is GameUiModel.Quiz.Status.Ongoing ->
                playViewModel.submitAction(PlayViewerNewAction.QuizEnded)
            else -> {
                // no-op
            }
        }
    }

    override fun onWidgetClicked(
        view: EngagementCarouselViewComponent,
        engagement: EngagementUiModel
    ) {
        when (engagement) {
            is EngagementUiModel.Promo -> {
                playViewModel.showCouponSheet(bottomSheetMaxHeight)
                newAnalytic.clickVoucherWidget(engagement.info.id)
            }
            is EngagementUiModel.Game -> {
                playViewModel.submitAction(PlayViewerNewAction.StartPlayingInteractive)
                newAnalytic.clickActiveInteractive(
                    interactiveId = playViewModel.gameData.id,
                    shopId = playViewModel.partnerId.toString(),
                    gameType = playViewModel.gameData,
                    channelId = playViewModel.channelId
                )
            }
        }
    }

    override fun onWidgetSwipe(view: EngagementCarouselViewComponent, id: String) {
        newAnalytic.swipeWidget(id)
    }

    override fun onWidgetImpressed(
        view: EngagementCarouselViewComponent,
        engagement: EngagementUiModel
    ) {
        when (engagement) {
            is EngagementUiModel.Promo -> {
                newAnalytic.impressVoucherWidget(engagement.info.id)
            }
            is EngagementUiModel.Game -> {
                newAnalytic.impressActiveInteractive(shopId = playViewModel.partnerId.toString(), interactiveId = engagement.game.id, channelId = playViewModel.channelId)
            }
        }
    }

    /**
     * Explore Widget
     */

    private fun renderExploreView(state: CachedState<PlayViewerNewUiState>) {
        if (state.isChanged { it.exploreWidget }) {
            exploreView?.setupVisibility(state.value.exploreWidget.shouldShow)
            exploreView?.setText(state.value.exploreWidget.config.categoryWidgetConfig.categoryName)
        }
    }

    override fun onExploreClicked(viewComponent: ExploreWidgetViewComponent) {
        eventBus.emit(ExploreWidgetViewComponent.Event.OnClicked(playViewModel.widgetInfo))
        PlayChannelRecommendationFragment
            .getOrCreate(childFragmentManager, requireActivity().classLoader)
            .showNow(childFragmentManager)
    }

    override fun onExploreWidgetIconImpressed(viewComponent: ExploreWidgetViewComponent) {
        eventBus.emit(ExploreWidgetViewComponent.Event.OnImpressed(playViewModel.widgetInfo))
    }

    private fun onCommentIconEvent(event: CommentIconUiComponent.Event) {
        when (event) {
            CommentIconUiComponent.Event.OnCommentClicked -> {
                playViewModel.submitAction(CommentVisibilityAction(isOpen = true))
                analytic.clickCommentIcon(playViewModel.partnerId.toString())
            }
        }
    }

    companion object {
        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192
        private const val REQUEST_CODE_ADDRESS_LIST = 399

        private const val PERCENT_BOTTOMSHEET_HEIGHT = 0.6

        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L

        private const val AUTO_SWIPE_DELAY = 500L

        private const val FADING_EDGE_PRODUCT_FEATURED_WIDTH_MULTIPLIER = 0.125f
    }

    sealed interface Event {
        data class OnFadingEdgeMeasured(val widthFromEnd: Int) : Event

        object OnScrubStarted : Event
        object OnScrubEnded : Event
    }
}
