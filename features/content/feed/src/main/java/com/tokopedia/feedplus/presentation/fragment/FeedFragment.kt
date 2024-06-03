package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.btm.BtmApi
import com.tokopedia.analytics.btm.Tokopedia
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_ID
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_NAME
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_WIDGET_ID
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_REFRESH_FOR_RELEVANT_POST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.navigation.people.UserProfileActivityResult
import com.tokopedia.content.common.report_content.bottomsheet.ContentReportBottomSheet
import com.tokopedia.content.common.report_content.bottomsheet.ContentSubmitReportBottomSheet
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feed.common.comment.ContentCommentFactory
import com.tokopedia.feed.common.comment.PageSource
import com.tokopedia.feed.common.comment.analytic.ContentCommentAnalytics
import com.tokopedia.feed.common.comment.analytic.ContentCommentAnalyticsModel
import com.tokopedia.feed.common.comment.ui.ContentCommentBottomSheet
import com.tokopedia.feed.component.product.FeedProductPaging
import com.tokopedia.feed.component.product.FeedTaggedProductBottomSheet
import com.tokopedia.feedcomponent.bottomsheets.FeedFollowersOnlyBottomSheet
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.FeedVideoCache
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.analytics.FeedAnalytics
import com.tokopedia.feedplus.analytics.FeedFollowRecommendationAnalytics
import com.tokopedia.feedplus.analytics.FeedMVCAnalytics
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_TOP_ADS
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedInjector
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.domain.mapper.MapperProductsToXProducts
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.adapter.util.FeedPostLayoutManager
import com.tokopedia.feedplus.presentation.callback.FeedUiActionListener
import com.tokopedia.feedplus.presentation.callback.FeedUiListener
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_TYPE_FOR_YOU
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedContentUiModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedPostEvent
import com.tokopedia.feedplus.presentation.model.FeedProductActionModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedTopAdsTrackerDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.PostSourceModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType
import com.tokopedia.feedplus.presentation.uiview.PRODUCT_COUNT_ONE
import com.tokopedia.feedplus.presentation.util.FeedContentManager
import com.tokopedia.feedplus.presentation.util.OverscrollEdgeEffectFactory
import com.tokopedia.feedplus.presentation.util.VideoPlayerManager
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.shop.common.util.ShopPageActivityResult
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.resources.common.R as resourcescommonR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment :
    TkpdBaseV4Fragment(),
    FeedListener,
    ContentThreeDotsMenuBottomSheet.Listener,
    FeedTaggedProductBottomSheet.Listener,
    FeedFollowersOnlyBottomSheet.Listener,
    FeedUiActionListener {

    private var _binding: FragmentFeedImmersiveBinding? = null
    private val binding: FragmentFeedImmersiveBinding
        get() = _binding!!

    private var data: FeedDataModel? = null
    private val adapter: FeedContentAdapter by lazy {
        FeedContentAdapter(
            FeedAdapterTypeFactory(
                this,
                viewLifecycleOwner,
                binding.rvFeedPost,
                trackerModelMapper,
                feedFollowRecommendationListener,
                dispatchers
            )
        ) {
            if (feedPostViewModel.shouldShowNoMoreContent || !feedPostViewModel.hasNext) return@FeedContentAdapter
            adapter.showLoading()
            feedPostViewModel.fetchFeedPosts(data?.type ?: "", postSource = postSourceModel)
        }
    }
    private var currentTrackerData: FeedTrackerDataModel? = null
    private var isCdp: Boolean = false

    private val atcVariantViewModel by lazyThreadSafetyNone {
        ViewModelProvider(requireActivity())[AtcVariantSharedViewModel::class.java]
    }

    private val muteAnimatedVector by lazyThreadSafetyNone {
        AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.ic_feed_mute_animated)
    }

    private val unmuteAnimatedVector by lazyThreadSafetyNone {
        AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.ic_feed_unmute_animated)
    }

    private val videoPlayerManager by lazy { VideoPlayerManager(requireContext()) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var feedFactory: FeedAnalytics.Factory

    private var feedAnalytics: FeedAnalytics? = null

    @Inject
    lateinit var commentAnalytics: ContentCommentAnalytics.Creator

    @Inject
    lateinit var feedFollowRecommendationAnalytics: FeedFollowRecommendationAnalytics

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var viewModelAssistedFactory: FeedMainViewModel.Factory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var commentFactory: ContentCommentFactory.Creator

    private var mDataSource: DataSource? = null

//    init {
//        BtmApi.registerBtmPageOnCreate(this, Tokopedia.Feed)
//    }

    private val feedMainViewModel: FeedMainViewModel by viewModels(
        ownerProducer = { parentFragment ?: this },
        factoryProducer = {
            FeedMainViewModel.provideFactory(viewModelAssistedFactory, ActiveTabSource.Empty)
        }
    )

    private val feedPostViewModel: FeedPostViewModel by viewModels(
        ownerProducer = { mDataSource?.getViewModelStoreOwner(data?.type.orEmpty()) ?: this },
        factoryProducer = { viewModelFactory }
    )

    private val feedMvcAnalytics = FeedMVCAnalytics()

    private val feedEntrySource: MapperFeedModelToTrackerDataModel.FeedEntrySource by lazyThreadSafetyNone {
        val widgetId = arguments?.getString(UF_EXTRA_FEED_WIDGET_ID).ifNullOrBlank { ENTRY_POINT_DEFAULT }
        val source = arguments?.getString(ARGUMENT_ENTRY_POINT).ifNullOrBlank { ENTRY_POINT_DEFAULT }
        val entryPoint = arguments?.getString(UF_EXTRA_FEED_ENTRY_POINT).ifNullOrBlank { source }

        MapperFeedModelToTrackerDataModel.FeedEntrySource(
            widgetId = widgetId,
            entryPoint = entryPoint
        )
    }

    private val tabType: String
        get() {
            isCdp = arguments?.getBoolean(ARGUMENT_IS_CDP, false) ?: false
            return if (isCdp) FeedBaseFragment.TAB_TYPE_CDP else data?.type.orEmpty()
        }

    private val trackerModelMapper: MapperFeedModelToTrackerDataModel by lazy {
        MapperFeedModelToTrackerDataModel(
            tabType,
            feedEntrySource
        )
    }
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context)
    }

    private var commentEntrySource: ContentCommentBottomSheet.EntrySource? = null

    private val reportPostLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val feedMenuSheet =
                childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet
            if (feedMenuSheet != null && userSession.isLoggedIn) {
                val item = adapter.currentList[getCurrentPosition()]?.data
                val isVideo =
                    item is FeedCardVideoContentModel || item is FeedCardLivePreviewContentModel

                feedMenuSheet.showReportLayoutWhenLaporkanClicked(isVideo = isVideo, action = {
                    ContentReportBottomSheet.getOrCreate(
                        childFragmentManager,
                        requireActivity().classLoader
                    )
                        .apply {
                            updateList(feedPostViewModel.userReportList)
                        }
                        .show(childFragmentManager, ContentReportBottomSheet.TAG)
                })
            }
        }

    private val followLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (!userSession.isLoggedIn) return@registerForActivityResult
            feedPostViewModel.processSuspendedFollow()
        }

    private val likeLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (!userSession.isLoggedIn) return@registerForActivityResult
            feedPostViewModel.processSuspendedLike()
        }

    private val addToCartLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (!userSession.isLoggedIn) return@registerForActivityResult
            feedPostViewModel.processSuspendedAddProductToCart()
        }

    private val buyLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (!userSession.isLoggedIn) return@registerForActivityResult
            feedPostViewModel.processSuspendedBuyProduct()
        }

    private val userProfileResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { intent ->
                val userId = UserProfileActivityResult.getUserId(intent)
                val isFollow = UserProfileActivityResult.isFollow(intent)

                feedPostViewModel.updateFollowStatus(userId, isFollow)
            }
        }

    private val shopPageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { intent ->
                val shopId = ShopPageActivityResult.getShopId(intent)
                val isFollow = ShopPageActivityResult.isFollow(intent)

                feedPostViewModel.updateFollowStatus(shopId, isFollow)
            }
        }

    private val openCartPageLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (!userSession.isLoggedIn) return@registerForActivityResult
            openCartPage()
        }

    private val cartResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            feedPostViewModel.fetchCartCount()
        }

    private var feedFollowersOnlyBottomSheet: FeedFollowersOnlyBottomSheet? = null

    private val layoutManager by lazy {
        FeedPostLayoutManager(
            context,
            object : FeedPostLayoutManager.Listener {
                override fun onScrolling(
                    layoutManager: LinearLayoutManager,
                    isOverScroll: Boolean
                ) {
                    if (isOverScroll) {
                        adapter.stopScrolling()
                        return
                    }

                    if (binding.rvFeedPost.scrollState == RecyclerView.SCROLL_STATE_IDLE) return
                    adapter.onScrolling()
                }

                override fun onLayoutCompleted(layoutManager: LinearLayoutManager) {
                    val currentPosition = layoutManager.findFirstVisibleItemPosition()
                    updateBottomActionView(currentPosition)
                }
            }
        )
    }

    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            // update item state and send tracker
            if (newState == SCROLL_STATE_IDLE) {
                feedAnalytics?.eventSwipeUpDownContent(
                    trackerModelMapper.tabType,
                    feedEntrySource.entryPoint,
                    feedEntrySource.widgetId
                )

                val position = getCurrentPosition()
                updateBottomActionView(position)

                adapter.select(position)
                feedPostViewModel.saveScrollPosition(position)
            }
        }
    }
    private val snapHelper = PagerSnapHelper()

    private var mAuthor: FeedAuthorModel? = null
    private var mProducts: List<FeedCardProductModel>? = null
    private var mHasVoucher: Boolean = false
    private var mCampaign: FeedCardCampaignModel? = null

    private var postSourceModel: PostSourceModel? = null

    private var mHasNotScrolled = true

    private val feedFollowRecommendationListener = object : FeedFollowRecommendationListener {

        override fun onImpressProfile(profile: FeedFollowRecommendationModel.Profile) {
            feedFollowRecommendationAnalytics.eventImpressProfileRecommendation(
                trackerModelMapper.transformProfileRecommendationToTrackerModel(profile)
            )
        }

        override fun onClickFollow(profile: FeedFollowRecommendationModel.Profile) {
            feedFollowRecommendationAnalytics.eventClickFollowProfileRecommendation(
                trackerModelMapper.transformProfileRecommendationToTrackerModel(profile)
            )

            if (profile.isFollowed) {
                feedPostViewModel.doUnfollowProfileRecommendation(
                    profile.id,
                    profile.encryptedId,
                    profile.isShop
                )
            } else {
                feedPostViewModel.doFollowProfileRecommendation(
                    profile.id,
                    profile.encryptedId,
                    profile.isShop
                )
            }
        }

        override fun onCloseProfileRecommendation(profile: FeedFollowRecommendationModel.Profile) {
            feedFollowRecommendationAnalytics.eventClickRemoveProfileRecommendation(
                trackerModelMapper.transformProfileRecommendationToTrackerModel(profile)
            )

            feedPostViewModel.removeProfileRecommendation(profile)
        }

        override fun onClickProfileRecommendation(profile: FeedFollowRecommendationModel.Profile) {
            feedFollowRecommendationAnalytics.eventClickProfileRecommendation(
                trackerModelMapper.transformProfileRecommendationToTrackerModel(profile)
            )

            goToProfilePage(profile.applink, profile.isShop)
        }

        override fun reloadProfileRecommendation() {
            feedPostViewModel.fetchFollowRecommendation(getCurrentPosition())
        }

        override fun onLoadNextProfileRecommendation() {
            feedPostViewModel.fetchFollowRecommendation(getCurrentPosition())
        }

        override fun onClickViewOtherContent() {
            feedMainViewModel.setActiveTab(FeedBaseFragment.TAB_TYPE_FOR_YOU)
        }

        override fun onSwipeProfileRecommendation() {
            feedFollowRecommendationAnalytics.eventSwipeProfileRecommendation(
                tabType = trackerModelMapper.tabType,
                entryPoint = feedEntrySource.entryPoint
            )
        }

        override fun onErrorPlayingVideo() {
            showToast(
                getString(feedplusR.string.feed_load_follow_recommendation_failed),
                Toaster.TYPE_ERROR
            )
        }

        override fun isMuted(): Boolean = FeedContentManager.muteState.value.orFalse()
    }

    private var mUiListener: FeedUiListener? = null

    fun setUiListener(uiListener: FeedUiListener) {
        mUiListener = uiListener
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    ContentCommentBottomSheet::class.java.name -> ContentCommentBottomSheet(
                        commentFactory,
                        router
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        BtmApi.registerBtmPageOnCreate(this, Tokopedia.Feed)

        childFragmentManager.addFragmentOnAttachListener(::onAttachChildFragment)

        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA)
            isCdp = it.getBoolean(ARGUMENT_IS_CDP, false)
        } ?: savedInstanceState?.let {
            data = it.getParcelable(ARGUMENT_DATA)
            isCdp = it.getBoolean(ARGUMENT_IS_CDP, false)
        }

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if (checkResume(isOnResume = true)) {
                            handleResume()
                            setDataEligibleForOnboarding()
                        }
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        pauseCurrentVideo()

                        feedFollowRecommendationAnalytics.sendAll()
                    }

                    else -> {}
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(ARGUMENT_DATA, data)
        outState.putBoolean(ARGUMENT_IS_CDP, isCdp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedImmersiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        if (feedPostViewModel.shouldFetchInitialPost()) {
            fetchInitialPost()
        }

        initView()
        observePostData()
        observeReport()
        observeDelete()
        observeFollow()
        observeUnfollow()
        observeLikeContent()
        observeResumePage()
        observeAddProductToCart()
        observeBuyProduct()
        observeReminder()
        observeFeedRecommendationResult()

        observeEvent()
        observeMuteUnmute()
        initAnalytic()
    }

    override fun onDestroyView() {
        dismissFeedProductBottomSheet()
        dismissFeedMenuBottomSheet()
        dismissAtcVariantBottomSheet()
        dismissShareBottomSheet()
        dismissCommentBottomSheet()
        super.onDestroyView()
        _binding = null

        videoPlayerManager.releaseAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        FeedVideoCache.cleanUp(requireContext())
        mUiListener = null
        mDataSource = null
    }

    private fun initInjector() {
        FeedInjector.get(requireActivity()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onMenuClicked(
        id: String,
        menuItems: List<ContentMenuItem>,
        trackerModel: FeedTrackerDataModel
    ) {
        currentTrackerData = trackerModel
        feedAnalytics?.eventClickThreeDots(trackerModel)
        activity?.let {
            val newMenuItems = updateThreeDotsMenuItems(menuItems)

            val feedMenuSheet =
                ContentThreeDotsMenuBottomSheet.getOrCreateFragment(
                    childFragmentManager,
                    it.classLoader
                )
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(newMenuItems, id)
            feedMenuSheet.show(childFragmentManager, TAG_FEED_MENU_BOTTOMSHEET)

            if (trackerModel.type == FeedXCard.TYPE_FEED_X_CARD_PLAY || trackerModel.type == FeedXCard.TYPE_FEED_PLAY_CHANNEL || trackerModel.type == FeedXCard.TYPE_FEED_PLAY_SHORT_VIDEO || trackerModel.type == FeedXCard.TYPE_FEED_PLAY_LIVE) {
                feedPostViewModel.getReport()
            }
        }
    }

    override fun onProfileClicked(appLink: String, type: AuthorType) {
        goToProfilePage(appLink, type.isShop)
    }

    override fun onMenuItemClick(contentMenuItem: ContentMenuItem, contentId: String) {
        when (contentMenuItem.type) {
            ContentMenuIdentifier.Report -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    val item = adapter.currentList[getCurrentPosition()]?.data
                    val isVideo =
                        item is FeedCardVideoContentModel || item is FeedCardLivePreviewContentModel
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.showReportLayoutWhenLaporkanClicked(
                        isVideo = isVideo,
                        action = {
                            ContentReportBottomSheet.getOrCreate(
                                childFragmentManager,
                                requireActivity().classLoader
                            )
                                .apply {
                                    updateList(feedPostViewModel.userReportList)
                                }
                                .show(childFragmentManager, ContentReportBottomSheet.TAG)
                        }
                    )
                    currentTrackerData?.let {
                        feedAnalytics?.eventClickReportContent(it)
                    }
                }
            }

            ContentMenuIdentifier.WatchMode -> {
                val position = getCurrentPosition()
                if (position >= ZERO) {
                    adapter.showClearView(position)
                }
                currentTrackerData?.let {
                    feedAnalytics?.eventClickWatchMode(it)
                    currentTrackerData = null
                }
            }

            ContentMenuIdentifier.Edit -> {
                val intent = RouteManager.getIntent(context, INTERNAL_AFFILIATE_CREATE_POST_V2)
                intent.putExtra(PARAM_AUTHOR_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
                intent.putExtra(
                    CreatePostViewModel.TAG,
                    CreatePostViewModel().apply {
                        caption = contentMenuItem.contentData?.caption.orEmpty()
                        postId = contentMenuItem.contentData?.postId.orEmpty()
                        editAuthorId = contentMenuItem.contentData?.authorId.orEmpty()
                    }
                )

                startActivity(intent)
            }

            ContentMenuIdentifier.Delete -> {
                context?.let {
                    DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(feedplusR.string.dialog_delete_post_title))
                        setDescription(getString(feedplusR.string.dialog_delete_post_subtitle))
                        setPrimaryCTAText(getString(feedplusR.string.feed_delete))
                        setPrimaryCTAClickListener {
                            feedPostViewModel.doDeletePost(
                                contentMenuItem.contentData?.postId.orEmpty(),
                                contentMenuItem.contentData?.rowNumber.orZero()
                            )
                            dismiss()
                        }
                        setSecondaryCTAText(getString(resourcescommonR.string.general_label_cancel))
                        setSecondaryCTAClickListener {
                            dismiss()
                        }
                        show()
                    }
                }
            }

            ContentMenuIdentifier.SeePerformance,
            ContentMenuIdentifier.LearnVideoInsight -> {
                RouteManager.route(requireContext(), contentMenuItem.appLink)
            }

            ContentMenuIdentifier.Mute -> {
                binding.ivFeedMuteUnmute.setImageDrawable(muteAnimatedVector)
                muteAnimatedVector?.start()

                feedMainViewModel.muteSound()
            }

            ContentMenuIdentifier.Unmute -> {
                binding.ivFeedMuteUnmute.setImageDrawable(unmuteAnimatedVector)
                unmuteAnimatedVector?.start()

                feedMainViewModel.unmuteSound()
            }
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param) {
        feedPostViewModel.reportContent(feedReportRequestParamModel)
        currentTrackerData?.let {
            feedAnalytics?.eventClickReasonReportContent(
                it,
                feedReportRequestParamModel.reason
            )
        }
    }

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        // add analytics(if any)
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCTAText: String,
        secondaryCTAText: String,
        primaryAction: () -> Unit,
        secondaryAction: () -> Unit = {}
    ) {
        activity?.let {
            val dialog =
                DialogUnify(context = it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCTAText)
                setPrimaryCTAClickListener {
                    primaryAction()
                    dismiss()
                }
                setSecondaryCTAText(secondaryCTAText)
                setSecondaryCTAClickListener {
                    secondaryAction()
                    dismiss()
                }
            }.show()
        }
    }

    private fun getVideoTimeStamp(): Long {
        val currentIndex = getCurrentPosition()
        if (currentIndex >= adapter.itemCount) return 0
        val item = adapter.currentList[currentIndex]?.data ?: return 0

        return when (item) {
            is FeedCardVideoContentModel -> videoPlayerManager.getPlayerById(item.id)
                ?.getExoPlayer()?.currentPosition.orZero()

            is FeedCardLivePreviewContentModel -> videoPlayerManager.getPlayerById(item.id)
                ?.getExoPlayer()?.currentPosition.orZero()

            else -> 0
        }
    }

    override fun onSharePostClicked(data: FeedShareModel, trackerModel: FeedTrackerDataModel) {
        val shareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(getShareListener(data, trackerModel))
        }
        shareBottomSheet.setUtmCampaignData(
            pageName = "Feed Page",
            userId = userSession.userId,
            pageId = data.contentId,
            feature = "share"
        )
        shareBottomSheet.setMetaData(
            tnTitle = String.format(
                requireContext().getString(feedplusR.string.feed_share_title),
                data.author.name
            ),
            tnImage = data.mediaUrl
        )
        if (!shareBottomSheet.isVisible) {
            feedAnalytics?.sendClickShareButtonEvent(trackerModel)
            shareBottomSheet.show(
                fragmentManager = childFragmentManager,
                fragment = this,
                screenshotDetector = null
            )
        }
    }

    override fun onFollowClicked(
        id: String,
        encryptedId: String,
        isShop: Boolean,
        trackerData: FeedTrackerDataModel?
    ) {
        trackerData?.let {
            feedAnalytics?.eventClickFollowButton(it)
        }
        if (userSession.isLoggedIn) {
            feedPostViewModel.doFollow(id, encryptedId, isShop)
        } else {
            feedPostViewModel.suspendFollow(id, encryptedId, isShop)
            followLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }
    }

    override fun changeTab() {
        feedMainViewModel.setActiveTab(FeedBaseFragment.TAB_TYPE_FOR_YOU)
    }

    override fun reload() {
        feedPostViewModel.fetchFeedPosts(data?.type ?: "", postSource = postSourceModel)
        adapter.removeErrorNetwork()
        showLoading()
        adapter.showLoading()
    }

    override fun getVideoPlayer(id: String): FeedExoPlayer {
        return videoPlayerManager.occupy(id)
    }

    override fun isAllowedToPlayVideo(): Boolean {
        return viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    override fun detachPlayer(player: FeedExoPlayer) {
        videoPlayerManager.detach(player)
    }

    override fun onPauseVideoPost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics?.eventClickPauseVideo(trackerModel)
    }

    override fun onTapHoldSeekbarVideoPost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics?.eventHoldSeekBarVideo(trackerModel)
    }

    override fun onWatchPostVideo(
        model: FeedCardVideoContentModel,
        trackerModel: FeedTrackerDataModel
    ) {
        feedPostViewModel.trackPerformance(model.playChannelId, model.products.map(FeedCardProductModel::id), BroadcasterReportTrackViewerUseCase.Companion.Event.Visit)
    }

    override fun onSwipeMultiplePost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics?.eventSwipeLeftRightMultiplePost(trackerModel)
    }

    override fun onAuthorNameClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics?.eventClickAuthorName(it)
        }
    }

    override fun onAuthorProfilePictureClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics?.eventClickAuthorProfilePicture(it)
        }
    }

    override fun onCaptionClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics?.eventClickContentCaption(it)
        }
    }

    override fun onLivePreviewClicked(
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int,
        productId: String,
        authorName: String
    ) {
        trackerModel?.let {
            feedAnalytics?.eventClickLivePreview(it, productId, authorName, positionInFeed)
        }
    }

    override fun onPostImpression(
        trackerModel: FeedTrackerDataModel?,
        activityId: String,
        positionInFeed: Int
    ) {
        trackerModel?.let {
            feedAnalytics?.eventWatchVideoPost(trackerModel)
            feedAnalytics?.eventPostImpression(
                it,
                activityId,
                positionInFeed
            )
        }
    }

    override fun onProductTagButtonClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int
    ) {
        currentTrackerData = trackerModel

        val action: () -> Unit = {
            openProductTagBottomSheet(
                activityId = postId,
                author = author,
                hasVoucher = hasVoucher,
                products = products,
                trackerData = trackerModel,
                campaign = campaign
            )
            trackerModel?.let {
                feedAnalytics?.eventClickProductTag(it)
            }
        }

        if (!checkForFollowerBottomSheet(
                trackerModel?.activityId ?: "",
                positionInFeed,
                campaign.status,
                campaign.isExclusiveForMember,
                action
            )
        ) {
            action()
        }
    }

    override fun onProductTagViewClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int,
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int
    ) {
        currentTrackerData = trackerModel

        val action: () -> Unit = {
            if (products.size == PRODUCT_COUNT_ONE) {
                val appLink = products.firstOrNull()?.applink
                if (appLink?.isNotEmpty() == true) {
                    trackerModel?.let {
                        feedAnalytics?.eventClickProductLabel(it, products)
                    }
                    activity?.let {
                        RouteManager.route(it, appLink)
                    }
                }
            } else {
                trackerModel?.let {
                    feedAnalytics?.eventClickContentProductLabel(it)
                }
                openProductTagBottomSheet(
                    activityId = postId,
                    author = author,
                    hasVoucher = hasVoucher,
                    products = products,
                    trackerData = trackerModel,
                    campaign = campaign
                )
            }
        }

        if (!checkForFollowerBottomSheet(
                trackerModel?.activityId ?: "",
                positionInFeed,
                campaign.status,
                campaign.isExclusiveForMember,
                action
            )
        ) {
            action()
        }
    }

    override fun onASGCGeneralClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?
    ) {
        openProductTagBottomSheet(
            activityId = postId,
            author = author,
            hasVoucher = hasVoucher,
            products = products,
            trackerData = trackerModel,
            campaign = campaign
        )
    }

    override fun onReminderClicked(
        campaignId: Long,
        setReminder: Boolean,
        trackerModel: FeedTrackerDataModel?,
        type: FeedCampaignRibbonType
    ) {
        trackerModel?.let {
            if (setReminder) {
                feedAnalytics?.eventClickRemindMe(it)
            } else {
                feedAnalytics?.eventClickActiveRemindMe(it)
            }
        }

        feedPostViewModel.setUnsetReminder(campaignId, setReminder, type)
    }

    override fun onTopAdsImpression(topadsTrackerData: FeedTopAdsTrackerDataModel) {
        with(topadsTrackerData) {
            topAdsUrlHitter.hitImpressionUrl(
                this::class.java.simpleName,
                adViewUrl,
                id,
                uri,
                fullEcs
            )
        }
    }

    override fun onTopAdsClick(topadsTrackerData: FeedTopAdsTrackerDataModel) {
        with(topadsTrackerData) {
            topAdsUrlHitter.hitClickUrl(
                this::class.java.simpleName,
                adClickUrl,
                id,
                uri,
                fullEcs
            )
        }
    }

    override fun onOngoingCampaignClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?,
        campaignName: String,
        positionInFeed: Int
    ) {
        currentTrackerData = trackerModel
        val action: () -> Unit = {
            trackerModel?.let {
                feedAnalytics?.eventClickCampaignRibbon(
                    it,
                    campaignName,
                    positionInFeed
                )
                openProductTagBottomSheet(
                    activityId = postId,
                    author = author,
                    hasVoucher = hasVoucher,
                    products = products,
                    trackerData = it,
                    campaign = campaign
                )
            }
        }

        if (!checkForFollowerBottomSheet(
                trackerModel?.activityId ?: "",
                positionInFeed,
                campaign.status,
                campaign.isExclusiveForMember,
                action
            )
        ) {
            action()
        }
    }

    override fun onFollowClickedFromFollowBottomSheet(position: Int) {
        if (adapter.itemCount > position) {
            adapter.currentList[position]?.let {
                var author: FeedAuthorModel? = null
                when (it.data) {
                    is FeedCardImageContentModel -> author = it.data.author
                    is FeedCardVideoContentModel -> author = it.data.author
                }
                author?.let {
                    if (userSession.isLoggedIn) {
                        feedPostViewModel.doFollow(
                            author.id,
                            author.encryptedUserId,
                            author.type.isShop
                        )
                    } else {
                        feedPostViewModel.suspendFollow(
                            author.id,
                            author.encryptedUserId,
                            author.type.isShop
                        )
                        followLoginResult.launch(
                            RouteManager.getIntent(
                                context,
                                ApplinkConst.LOGIN
                            )
                        )
                    }
                }
            }
        }

        feedFollowersOnlyBottomSheet?.dismiss()
    }

    override fun isMuted(): Boolean = FeedContentManager.muteState.value.orFalse()

    override fun checkLiveStatus(channelId: String) {
        feedPostViewModel.updateChannelStatus(channelId)
    }

    fun setDataSource(dataSource: FeedFragment.DataSource?) {
        mDataSource = dataSource
    }

    private fun onAttachChildFragment(fragmentManager: FragmentManager, childFragment: Fragment) {
        when (childFragment) {
            is ContentCommentBottomSheet -> {
                val eventLabel = currentTrackerData?.let {
                    feedAnalytics?.getEventLabel(it)
                } ?: ""
                childFragment.setEntrySource(commentEntrySource)
                childFragment.setAnalytic(
                    commentAnalytics.create(
                        PageSource.Feed(currentTrackerData?.activityId.orEmpty()), // PostId
                        model = ContentCommentAnalyticsModel(
                            eventCategory = FeedAnalytics.CATEGORY_UNIFIED_FEED,
                            eventLabel = eventLabel
                        )
                    )
                )
            }

            is ContentReportBottomSheet -> {
                childFragment.setListener(
                    object : ContentReportBottomSheet.Listener {
                        override fun onCloseButtonClicked() {
                            childFragment.dismiss()
                        }

                        override fun onItemReportClick(item: PlayUserReportReasoningUiModel.Reasoning) {
                            ContentSubmitReportBottomSheet.getOrCreate(
                                childFragmentManager,
                                requireActivity().classLoader
                            ).apply {
                                setData(item)
                            }.show(childFragmentManager, ContentSubmitReportBottomSheet.TAG)
                            feedPostViewModel.selectReport(item)
                        }

                        override fun onFooterClicked() {
                            RouteManager.route(
                                requireContext(),
                                getString(contentcommonR.string.content_user_report_footer_weblink)
                            )
                        }
                    }
                )
            }

            is ContentSubmitReportBottomSheet -> childFragment.setListener(
                object : ContentSubmitReportBottomSheet.Listener {
                    override fun onBackButtonListener() {
                        childFragment.dismiss()
                    }

                    override fun onFooterClicked() {
                        RouteManager.route(
                            requireContext(),
                            getString(contentcommonR.string.content_user_report_footer_weblink)
                        )
                    }

                    override fun onSubmitReport(desc: String) {
                        val currentIndex = layoutManager.findFirstVisibleItemPosition()
                        val item = adapter.currentList[currentIndex]?.data ?: return
                        if (item !is FeedCardVideoContentModel) return

                        showDialog(
                            title = getString(contentcommonR.string.play_user_report_verification_dialog_title),
                            description = getString(contentcommonR.string.play_user_report_verification_dialog_desc),
                            primaryCTAText = getString(contentcommonR.string.play_user_report_verification_dialog_btn_ok),
                            secondaryCTAText = getString(feedplusR.string.feed_cancel),
                            primaryAction = {
                                feedPostViewModel.submitReport(desc, getVideoTimeStamp(), item)
                            }
                        )
                    }
                }
            )

            is FeedTaggedProductBottomSheet -> {
                childFragment.listener = this
                childFragment.tracker = feedMvcAnalytics
            }
        }
    }

    private fun observeReport() {
        feedPostViewModel.reportResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    currentTrackerData?.let { trackerData ->
                        feedAnalytics?.eventViewSuccessReportContent(trackerData)
                        currentTrackerData = null
                    }
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.apply {
                        setFinalView()
                    }
                }

                is Fail -> {}
            }
        }

        feedPostViewModel.isReported.observe(viewLifecycleOwner) {
            val menuSheet =
                childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet
            when (it) {
                is Success -> {
                    (childFragmentManager.findFragmentByTag(ContentSubmitReportBottomSheet.TAG) as? ContentSubmitReportBottomSheet)?.dismiss()
                    (childFragmentManager.findFragmentByTag(ContentReportBottomSheet.TAG) as? ContentReportBottomSheet)?.dismiss()
                    menuSheet?.dismiss()
                    showToast(
                        message = getString(feedplusR.string.feed_success_report_video),
                        type = Toaster.TYPE_NORMAL
                    )
                }

                is Fail -> {
                    val view = menuSheet?.view ?: return@observe
                    Toaster.build(
                        view,
                        ErrorHandler.getErrorMessage(requireContext(), it.throwable),
                        type = Toaster.TYPE_ERROR,
                        duration = Toaster.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun observeDelete() {
        feedPostViewModel.deletePostResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedplusR.string.toast_delete_post_success),
                        Toaster.TYPE_NORMAL
                    )
                }

                is Fail -> {
                    showToast(
                        getString(feedplusR.string.toast_delete_post_failed),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun initView() {
        binding.let {
            it.swipeRefreshFeedLayout.setOnRefreshListener {
                feedPostViewModel.fetchFeedPosts(
                    data?.type ?: "",
                    isNewData = true,
                    postSource = postSourceModel
                )
            }

            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.removeOnScrollListener(contentScrollListener)
            it.rvFeedPost.addOnScrollListener(contentScrollListener)
            it.rvFeedPost.itemAnimator = null
            it.rvFeedPost.edgeEffectFactory = OverscrollEdgeEffectFactory()
        }
    }

    private fun observePostData() {
        feedPostViewModel.feedHome.observe(viewLifecycleOwner) {
            binding.swipeRefreshFeedLayout.isRefreshing = false
            adapter.hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.items.isEmpty()) {
                        adapter.setList(
                            listOf(
                                FeedNoContentModel.getNoContentInstance(requireContext())
                            )
                        )
                        mUiListener?.onContentFailed()
                    } else {
                        adapter.setList(it.data.items) {
                            if (_binding == null || !mHasNotScrolled) return@setList

                            mHasNotScrolled = false
                            val position = feedPostViewModel.getScrollPosition().orZero()

                            binding.rvFeedPost.scrollToPosition(position)
                            if (checkResume()) adapter.select(position)
                        }
                        context?.let { ctx ->
                            if (feedPostViewModel.shouldShowNoMoreContent && !isCdp) {
                                adapter.addElement(FeedNoContentModel.getNoMoreContentInstance(ctx))
                            }
                        }
                        feedPostViewModel.fetchTopAdsData()
                    }

                    setDataEligibleForOnboarding()
                    hideLoading()
                }

                is Fail -> {
                    mUiListener?.onContentFailed()
                    hideLoading()
                    adapter.showErrorNetwork()
                }

                else -> {
                    adapter.setList(emptyList())
                }
            }
        }
    }

    private fun preCacheVideo(urls: List<String>) {
        val cacheManager = FeedVideoCache.getInstance(requireContext())
        urls.forEach { url ->
            lifecycleScope.launch(dispatchers.io) {
                if (cacheManager.isCached(url)) return@launch
                cacheManager.cache(requireContext(), url)
            }
        }
    }

    private fun observeFollow() {
        feedPostViewModel.followResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedplusR.string.feed_message_success_follow, it.data),
                        Toaster.TYPE_NORMAL
                    )
                }

                is Fail -> {
                    showToast(
                        getString(feedplusR.string.feed_message_failed_follow),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observeUnfollow() {
        feedPostViewModel.unfollowResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedplusR.string.feed_message_success_unfollow, it.data),
                        Toaster.TYPE_NORMAL
                    )
                }

                is Fail -> {
                    showToast(
                        getString(feedplusR.string.feed_message_failed_unfollow),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observeLikeContent() {
        feedPostViewModel.getLikeKolResp.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is FeedResult.Failure -> {
                    val error = it.error
                    val errorMessage = if (error is CustomUiMessageThrowable) {
                        requireContext().getString(error.errorMessageId)
                    } else {
                        ErrorHandler.getErrorMessage(requireContext(), it.error)
                    }
                    showToast(errorMessage, Toaster.TYPE_ERROR)
                }

                else -> {}
            }
        }
    }

    private fun observeResumePage() {
        feedMainViewModel.isPageResumed.observe(viewLifecycleOwner) { isResumed ->
            if (isResumed == null) return@observe

            if (isResumed && checkResume(isPageResumed = true)) {
                handleResume()
            } else {
                pauseCurrentVideo()

                feedFollowRecommendationAnalytics.sendAll()
            }
        }
    }

    private fun observeAddProductToCart() {
        feedPostViewModel.observeAddProductToCart.observe(viewLifecycleOwner) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)
            when (it) {
                is Success -> {
                    currentTrackerData?.let { data ->
                        if (it.data.source == FeedProductActionModel.Source.CardHighlight) {
                            feedAnalytics?.atcFromProductHighlight(
                                trackerModel = data,
                                product = it.data
                            )
                        } else {
                            feedAnalytics?.eventClickBuyButton(
                                trackerData = data,
                                productInfo = it.data
                            )
                        }
                    }

                    if (productBottomSheet != null) {
                        productBottomSheet.doShowToaster(
                            message = getString(feedplusR.string.feeds_add_to_cart_success_text),
                            actionText = getString(feedplusR.string.feeds_add_to_cart_toaster_action_text),
                            actionClickListener = {
                                currentTrackerData?.let { trackerData ->
                                    feedAnalytics?.eventClickViewCart(trackerData)
                                }
                                goToCartPage()
                            }
                        )
                    } else {
                        showToast(
                            message = getString(feedplusR.string.feeds_add_to_cart_success_text),
                            actionText = getString(feedplusR.string.feeds_add_to_cart_toaster_action_text),
                            actionClickListener = {
                                currentTrackerData?.let { trackerData ->
                                    feedAnalytics?.eventClickViewCart(trackerData)
                                }
                                goToCartPage()
                            }
                        )
                    }
                }

                is Fail -> {
                    if (productBottomSheet != null) {
                        productBottomSheet.doShowToaster(
                            message = it.throwable.localizedMessage.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        )
                    } else {
                        showToast(
                            message = it.throwable.localizedMessage.orEmpty(),
                            type = Toaster.TYPE_ERROR
                        )
                    }
                }
            }
        }
    }

    private fun observeBuyProduct() {
        feedPostViewModel.observeBuyProduct.observe(viewLifecycleOwner) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)
            when (it) {
                is Success -> {
                    currentTrackerData?.let { data ->
                        feedAnalytics?.eventClickCartButton(
                            trackerData = data,
                            productInfo = it.data
                        )
                    }
                    goToCartPage()
                }

                is Fail -> productBottomSheet?.doShowToaster(
                    message = it.throwable.localizedMessage.orEmpty(),
                    type = Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedMainViewModel.uiEvent.collect { event ->
                    if (event == null) return@collect

                    when (event) {
                        is FeedMainEvent.ScrollToTop -> {
                            if (event.tabKey != data?.key) return@collect
                            binding.rvFeedPost.scrollToPosition(0)
                            if (checkResume()) adapter.select(0)
                        }

                        else -> {}
                    }

                    feedMainViewModel.consumeEvent(event)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                feedPostViewModel.uiEvent.collect { event ->
                    if (event == null) return@collect

                    when (event) {
                        is FeedPostEvent.PreCacheVideos -> {
                            preCacheVideo(event.videoUrls)
                        }
                    }

                    feedPostViewModel.consumeEvent(event)
                }
            }
        }
    }

    private fun observeMuteUnmute() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                FeedContentManager.muteState.collectLatest { isMuted ->
                    if (isMuted == null) return@collectLatest
                    updateCurrentVideoVolume(isMuted)
                }
            }
        }
    }

    private fun pauseCurrentVideo() {
        val currentIndex = getCurrentPosition()
        if (currentIndex < ZERO || currentIndex >= adapter.itemCount) return
        val item = adapter.currentList[currentIndex]?.data ?: return

        when (item) {
            is FeedCardVideoContentModel -> {
                pauseVideo(item.id)
                adapter.pauseVideoProductIconAnimation(currentIndex)
            }

            is FeedCardLivePreviewContentModel -> pauseVideo(item.id)
            is FeedFollowRecommendationModel -> adapter.pauseFollowRecommendationVideo(currentIndex)
            else -> {}
        }
    }

    private fun resumeCurrentVideo() {
        val currentIndex = getCurrentPosition()
        if (currentIndex < ZERO || currentIndex >= adapter.itemCount) return
        val item = adapter.currentList[currentIndex]?.data ?: return

        when (item) {
            is FeedCardVideoContentModel -> {
                resumeVideo(item.id)
                adapter.resumeVideoProductIconAnimation(currentIndex)
            }

            is FeedCardLivePreviewContentModel -> resumeVideo(item.id)
            is FeedFollowRecommendationModel -> adapter.resumeFollowRecommendationVideo(currentIndex)
            else -> {}
        }
    }

    private fun pauseVideo(id: String) {
        videoPlayerManager.pause(id, shouldReset = true)
    }

    private fun resumeVideo(id: String) {
        videoPlayerManager.resume(id)
    }

    private fun updateCurrentVideoVolume(isMuted: Boolean) {
        val currentIndex = getCurrentPosition()
        if (currentIndex < ZERO || currentIndex >= adapter.itemCount) return
        val item = adapter.currentList[currentIndex]?.data ?: return

        when (item) {
            is FeedCardVideoContentModel -> videoPlayerManager.setMuteStatus(item.id, isMuted)
            is FeedCardLivePreviewContentModel -> videoPlayerManager.setMuteStatus(item.id, isMuted)
            else -> {}
        }
    }

    private fun updateThreeDotsMenuItems(menuItems: List<ContentMenuItem>): List<ContentMenuItem> =
        menuItems.map {
            val isMuted = FeedContentManager.muteState.value.orFalse()

            when (it.type) {
                ContentMenuIdentifier.Mute, ContentMenuIdentifier.Unmute -> it.copy(
                    iconUnify = if (isMuted) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE,
                    type = if (isMuted) ContentMenuIdentifier.Unmute else ContentMenuIdentifier.Mute,
                    name = if (isMuted) feedplusR.string.feed_unmute_label else feedplusR.string.feed_mute_label
                )

                else -> it
            }
        }

    private fun checkResume(
        isOnResume: Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
        isPageResumed: Boolean = feedMainViewModel.isPageResumed.value != false
    ): Boolean {
        return isPageResumed && isOnResume
    }

    private fun getCurrentPosition(): Int {
        val snappedView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return binding.rvFeedPost.getChildAdapterPosition(snappedView)
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            reportPostLoginResult.launch(intent)
        }
    }

    override fun onLikePostCLicked(
        id: String,
        rowNumber: Int,
        trackerModel: FeedTrackerDataModel,
        isDoubleClick: Boolean
    ) {
        if (isDoubleClick) {
            feedAnalytics?.eventDoubleClickLikeButton(trackerModel)
        } else {
            feedAnalytics?.eventClickLikeButton(trackerModel)
        }

        if (userSession.isLoggedIn) {
            feedPostViewModel.likeContent(
                contentId = id,
                rowNumber = rowNumber
            )
        } else {
            feedPostViewModel.suspendLikeContent(id, rowNumber)
            likeLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }
    }

    override fun onCommentClick(
        trackerModel: FeedTrackerDataModel?,
        contentId: String,
        isPlayContent: Boolean,
        rowNumber: Int
    ) {
        trackerModel?.let {
            currentTrackerData = trackerModel
            feedAnalytics?.eventClickComment(it)
            commentEntrySource = object : ContentCommentBottomSheet.EntrySource {
                override fun getPageSource(): PageSource = PageSource.Feed(it.activityId)
                override fun onCommentDismissed() {
                    feedPostViewModel.updateCommentsCount(contentId, isPlayContent)
                }
            }

            val sheet = ContentCommentBottomSheet.getOrCreate(
                childFragmentManager,
                requireActivity().classLoader
            )
            if (!sheet.isAdded) sheet.show(childFragmentManager) else sheet.dismiss()
        }
    }

    private fun openProductTagBottomSheet(
        activityId: String,
        author: FeedAuthorModel,
        products: List<FeedCardProductModel>,
        hasVoucher: Boolean,
        campaign: FeedCardCampaignModel,
        trackerData: FeedTrackerDataModel?
    ) {
        openFeedTaggedProductBottomSheet(
            activityId = activityId,
            author = author,
            products = products,
            hasVoucher = hasVoucher,
            trackerData = trackerData,
            campaign = campaign
        )
    }

    private fun getMerchantVoucher(shopId: String) {
        feedPostViewModel.getMerchantVoucher(shopId)
    }

    private fun showToast(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_LONG,
        actionText: String? = null,
        actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        if (view == null) return
        if (message.isEmpty()) return

        Toaster.build(
            requireView(),
            message,
            type = type,
            duration = duration,
            actionText = actionText.orEmpty(),
            clickListener = actionClickListener
        ).show()
    }

    private fun showLoading() {
        mUiListener?.onContentLoading()
        binding.feedLoading.show()
        binding.swipeRefreshFeedLayout.hide()
    }

    private fun hideLoading() {
        binding.feedLoading.hide()
        binding.swipeRefreshFeedLayout.show()
    }

    private fun checkForFollowerBottomSheet(
        id: String,
        positionInFeed: Int,
        campaignStatus: String,
        isExclusiveForMember: Boolean,
        onDismiss: () -> Unit
    ): Boolean {
        val shouldShowFollowBottomSheet = isExclusiveForMember && !feedPostViewModel.isFollowing(id)
        if (shouldShowFollowBottomSheet) {
            showFollowerBottomSheet(positionInFeed, campaignStatus, onDismiss)
        }
        return shouldShowFollowBottomSheet
    }

    private fun showFollowerBottomSheet(
        positionInFeed: Int,
        campaignStatus: String,
        onDismiss: () -> Unit
    ) {
        feedFollowersOnlyBottomSheet =
            FeedFollowersOnlyBottomSheet.getOrCreate(childFragmentManager)
        feedFollowersOnlyBottomSheet?.setOnDismissListener(onDismiss)

        if (feedFollowersOnlyBottomSheet?.isAdded == false && feedFollowersOnlyBottomSheet?.isVisible == false) {
            feedFollowersOnlyBottomSheet?.show(
                childFragmentManager,
                this,
                positionInFeed,
                status = campaignStatus
            )
        }
    }

    private fun goToCartPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    override fun onProductCardClicked(product: ContentTaggedProductUiModel, itemPosition: Int) {
        if (product.appLink.isEmpty()) return

        currentTrackerData?.let { data ->
            feedAnalytics?.eventClickProductInProductListBottomSheet(
                trackerData = data,
                productName = product.title,
                productId = product.id,
                productPrice = product.finalPrice,
                shopName = product.shop.name,
                index = itemPosition
            )
        }

        RouteManager.route(requireContext(), product.appLink)
    }

    override fun onAddToCartProductButtonClicked(
        product: ContentTaggedProductUiModel,
        itemPosition: Int
    ) {
        if (!checkForFollowerBottomSheet(
                currentTrackerData?.activityId ?: "",
                itemPosition,
                when (product.campaign.status) {
                    is ContentTaggedProductUiModel.CampaignStatus.Upcoming -> FeedCardCampaignModel.UPCOMING
                    is ContentTaggedProductUiModel.CampaignStatus.Ongoing -> FeedCardCampaignModel.ONGOING
                    else -> FeedCardCampaignModel.NO
                },
                product.campaign.isExclusiveForMember
            ) {}
        ) {
            checkAddToCartAction(product, FeedProductActionModel.Source.BottomSheet)
        }
    }

    private fun checkAddToCartAction(product: ContentTaggedProductUiModel, source: FeedProductActionModel.Source) {
        when {
            userSession.isLoggedIn -> {
                if (product.showGlobalVariant) {
                    checkAvailableTracker(product, source)
                    dismissFeedProductBottomSheet()
                    openVariantBottomSheet(product)
                } else {
                    feedPostViewModel.addProductToCart(product, source)
                }
            }

            !userSession.isLoggedIn -> {
                if (product.showGlobalVariant) {
                    RouteManager.route(context, ApplinkConst.LOGIN)
                } else {
                    feedPostViewModel.suspendAddProductToCart(product, source)
                    addToCartLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
                }
            }

            else -> return
        }
    }

    private fun saveFeedTaggedProductArgs(
        author: FeedAuthorModel?,
        products: List<FeedCardProductModel>?,
        hasVoucher: Boolean,
        trackerData: FeedTrackerDataModel?,
        campaign: FeedCardCampaignModel?
    ) {
        mAuthor = author
        mProducts = products
        mHasVoucher = hasVoucher
        currentTrackerData = trackerData
        mCampaign = campaign
    }

    private fun openFeedTaggedProductBottomSheet(
        activityId: String,
        author: FeedAuthorModel?,
        products: List<FeedCardProductModel>,
        hasVoucher: Boolean,
        trackerData: FeedTrackerDataModel?,
        campaign: FeedCardCampaignModel
    ) {
        var isTopAds = false
        val sourceType = convertToSourceType(trackerData?.type.orEmpty())
        val taggedProductList = products.map {
            MapperProductsToXProducts.transform(it, campaign, sourceType)
        }

        if (products.isEmpty()) return

        val productBottomSheet = FeedTaggedProductBottomSheet()

        fun trackOpenProductTagBottomSheet(data: FeedTrackerDataModel) {
            feedMvcAnalytics.trackerData = data
            currentTrackerData = data
            feedAnalytics?.eventViewProductListBottomSheets(data, products)

            productBottomSheet.setOnDismissListener {
                feedAnalytics?.eventClickCloseProductListBottomSheet(data)
            }
        }

        if (trackerData != null) {
            isTopAds = trackerData.type == TYPE_FEED_TOP_ADS
            trackOpenProductTagBottomSheet(trackerData)
        }
        saveFeedTaggedProductArgs(author, products, hasVoucher, trackerData, campaign)

        feedPostViewModel.fetchFeedProduct(
            activityId,
            if (isTopAds) taggedProductList else emptyList(),
            sourceType,
            trackerData?.mediaType.orEmpty()
        )

        feedPostViewModel.fetchCartCount()

        productBottomSheet.show(
            activityId = activityId,
            shopId = author?.id ?: "",
            manager = childFragmentManager,
            tag = TAG_FEED_PRODUCT_BOTTOM_SHEET,
            sourceType = sourceType
        )
        if (hasVoucher && author?.type?.isShop == true) {
            getMerchantVoucher(author.id)
        } else {
            feedPostViewModel.clearMerchantVoucher()
        }
    }

    override fun onFeedProductNextPage(activityId: String, sourceType: ContentTaggedProductUiModel.SourceType) {
        feedPostViewModel.fetchFeedProduct(
            activityId = activityId,
            sourceType = sourceType,
            isNextPage = true,
            mediaType = currentTrackerData?.mediaType.orEmpty()
        )
    }

    private fun convertToSourceType(type: String): ContentTaggedProductUiModel.SourceType =
        when (type) {
            FeedXCard.TYPE_FEED_ASGC_RESTOCK, FeedXCard.TYPE_FEED_ASGC_NEW_PRODUCTS, FeedXCard.TYPE_FEED_ASGC_SHOP_DISCOUNT,
            FeedXCard.TYPE_FEED_ASGC_SHOP_FLASH_SALE, FeedXCard.TYPE_FEED_ASGC_SPECIAL_RELEASE, TYPE_FEED_TOP_ADS -> ContentTaggedProductUiModel.SourceType.NonOrganic

            else -> ContentTaggedProductUiModel.SourceType.Organic
        }

    private fun openVariantBottomSheet(product: ContentTaggedProductUiModel) {
        atcVariantViewModel.setAtcBottomSheetParams(
            ProductVariantBottomSheetParams(
                trackerCdListName = currentTrackerData?.activityId.orEmpty(),
                productId = product.id,
                shopId = product.shop.id,
                showQtyEditor = false,
                isTokoNow = false,
                pageSource = VariantPageSource.FEED_PAGESOURCE.source,
                dismissAfterTransaction = false
            )
        )

        val atcVariantBottomSheet = AtcVariantBottomSheet()
        atcVariantBottomSheet.showNow(childFragmentManager, VARIANT_BOTTOM_SHEET_TAG)
        atcVariantBottomSheet.setOnDismissListener {
            openFeedTaggedProductBottomSheet(
                activityId = currentTrackerData?.activityId.orEmpty(),
                author = mAuthor,
                products = mProducts ?: emptyList(),
                hasVoucher = mHasVoucher,
                trackerData = currentTrackerData,
                campaign = mCampaign ?: FeedCardCampaignModel()
            )
        }
    }

    override fun onBuyProductButtonClicked(
        product: ContentTaggedProductUiModel,
        itemPosition: Int
    ) {
        if (!checkForFollowerBottomSheet(
                currentTrackerData?.activityId ?: "",
                itemPosition,
                when (product.campaign.status) {
                    is ContentTaggedProductUiModel.CampaignStatus.Upcoming -> FeedCardCampaignModel.UPCOMING
                    is ContentTaggedProductUiModel.CampaignStatus.Ongoing -> FeedCardCampaignModel.ONGOING
                    else -> FeedCardCampaignModel.NO
                },
                product.campaign.isExclusiveForMember
            ) {}
        ) {
            if (userSession.isLoggedIn) {
                if (product.showGlobalVariant) {
                    dismissFeedProductBottomSheet()
                    openVariantBottomSheet(product)
                } else {
                    feedPostViewModel.buyProduct(product, FeedProductActionModel.Source.BottomSheet)
                }
            } else {
                feedPostViewModel.suspendBuyProduct(product, FeedProductActionModel.Source.BottomSheet)
                buyLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
            }
        }
    }

    override val mvcLiveData: LiveData<Result<TokopointsCatalogMVCSummary>?>
        get() = feedPostViewModel.merchantVoucherLiveData

    override val productListLiveData: Flow<FeedProductPaging>
        get() = feedPostViewModel.feedTagProductList

    override val cartCount: StateFlow<Int>
        get() = feedPostViewModel.cartCount

    override fun sendMvcImpressionTracker(mvcList: List<AnimatedInfos?>) {
        if (currentTrackerData != null) {
            feedAnalytics?.eventMvcWidgetImpression(
                currentTrackerData!!,
                mvcList
            )
            feedMvcAnalytics.voucherList = mvcList
        }
    }

    override fun onCartClicked() {
        if (!userSession.isLoggedIn) {
            router.route(requireContext(), openCartPageLoginResult, ApplinkConst.LOGIN)
        } else {
            openCartPage()
        }
    }

    private fun openCartPage() {
        router.route(requireContext(), cartResult, ApplinkConst.CART)
    }

    private fun observeReminder() {
        feedPostViewModel.reminderResult.observe(viewLifecycleOwner) {
            val message = when (it) {
                is Success -> {
                    val type = when (it.data.reminderType) {
                        FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> getString(feedplusR.string.feed_flash_sale)
                        FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> getString(feedplusR.string.feed_special_release)
                        else -> ""
                    }

                    // if set reminder
                    if (it.data.isSetReminder) {
                        getString(feedplusR.string.feed_reminder_set_success, type)
                    } else {
                        // if unset reminder
                        getString(feedplusR.string.feed_reminder_unset_success)
                    }
                }

                is Fail -> it.throwable.message
                else -> ""
            }

            showToast(
                message = message.orEmpty(),
                type = if (it is Success) Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR,
                actionText = getString(feedplusR.string.feed_cta_ok_toaster)
            )
        }
    }

    private fun observeFeedRecommendationResult() {
        feedPostViewModel.followRecommendationResult.observe(viewLifecycleOwner) {
            when (it) {
                is Fail -> {
                    showToast(
                        getString(feedplusR.string.feed_load_follow_recommendation_failed),
                        Toaster.TYPE_ERROR
                    )
                }

                else -> {}
            }
        }
    }

    /**
     * Share
     */
    private fun getShareListener(
        data: FeedShareModel,
        trackerModel: FeedTrackerDataModel
    ) = object : ShareBottomsheetListener {
        override fun onShareOptionClicked(shareModel: ShareModel) {
            dismissShareBottomSheet()
            feedAnalytics?.sendClickSharingChannelEvent(
                shareModel.socialMediaName.orEmpty(),
                trackerModel
            )
            generateShareLinkUrl(data, shareModel, trackerModel)
        }

        override fun onCloseOptionClicked() {
            feedAnalytics?.sendClickCloseButtonOnShareBottomSheetEvent(trackerModel)
        }
    }

    private fun generateShareLinkUrl(
        data: FeedShareModel,
        shareModel: ShareModel,
        trackerModel: FeedTrackerDataModel
    ) {
        val shareData = LinkerData.Builder.getLinkerBuilder()
            .setId(data.contentId)
            .setName(
                String.format(
                    getString(feedplusR.string.feed_share_title),
                    data.author.name
                )
            )
            .setDescription(
                String.format(
                    getString(feedplusR.string.feed_share_desc_text),
                    data.author.name
                )
            )
            .setDesktopUrl(data.webLink)
            .setType(LinkerData.FEED_TYPE)
            .setImgUri(data.mediaUrl)
            .setDeepLink(data.appLink)
            .setUri(data.webLink)
            .build()

        val linkerShareData = DataMapper().getLinkerShareData(shareData)
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        val shareString =
                            if (shareData.description.contains("%s")) {
                                String.format(
                                    shareData.description,
                                    linkerShareData?.shareUri.orEmpty()
                                )
                            } else {
                                "${shareData.description}\n${linkerShareData?.shareUri.orEmpty()}"
                            }

                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareData,
                            activity,
                            view,
                            shareString,
                            onSuccessCopyLink = {
                                showToast(
                                    message = getString(feedplusR.string.feed_copy_link_success_message),
                                    actionText = getString(contentcommonR.string.feed_ok),
                                    actionClickListener = {
                                        feedAnalytics?.sendClickOkeShareToasterEvent(trackerModel)
                                    }
                                )
                            }
                        )
                        dismissShareBottomSheet()
                    }

                    override fun onError(linkerError: LinkerError?) {}
                }
            )
        )
    }

    private fun goToProfilePage(appLink: String, isShop: Boolean) {
        if (isShop) {
            router.route(requireContext(), shopPageResult, appLink)
        } else {
            router.route(requireContext(), userProfileResult, appLink)
        }
    }

    private fun dismissFeedProductBottomSheet() {
        (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)?.dismiss()
    }

    private fun dismissFeedMenuBottomSheet() {
        (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.dismiss()
    }

    private fun dismissAtcVariantBottomSheet() {
        (childFragmentManager.findFragmentByTag(VARIANT_BOTTOM_SHEET_TAG) as? AtcVariantBottomSheet)?.dismiss()
    }

    private fun dismissShareBottomSheet() {
        (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)?.dismiss()
    }

    private fun dismissCommentBottomSheet() {
        ContentCommentBottomSheet.getOrCreate(childFragmentManager, requireActivity().classLoader).dismiss()
    }

    private fun updateBottomActionView(position: Int) {
        val currentItem =
            if (position != RecyclerView.NO_POSITION && adapter.currentList.size > position) {
                adapter.currentList[position]
            } else {
                null
            }

        val uiListener = mUiListener ?: return

        if (currentItem == null || !isCdp) return

        val feedContentUiModel = (currentItem.data as? FeedContentUiModel) ?: return

        val trackerModel = when (currentItem.data) {
            is FeedCardImageContentModel -> {
                trackerModelMapper.transformImageContentToTrackerModel(currentItem.data)
            }

            is FeedCardVideoContentModel -> {
                trackerModelMapper.transformVideoContentToTrackerModel(currentItem.data)
            }

            is FeedCardLivePreviewContentModel -> {
                trackerModelMapper.transformLiveContentToTrackerModel(currentItem.data)
            }

            else -> {
                null
            }
        }

        uiListener.onContentLoaded(
            feedContentUiModel,
            trackerModel,
            this,
            position
        )
    }

    private fun setDataEligibleForOnboarding() {
        val selectedTab = feedMainViewModel.selectedTab
        if (selectedTab?.isSelected == true && selectedTab.type == data?.type) {
            feedMainViewModel.setDataEligibleForOnboarding(
                feedPostViewModel.determinePostDataEligibilityForOnboarding(data?.isFollowingTab == true)
            )
        }
    }

    private fun initAnalytic() {
        feedAnalytics = feedFactory.create(userSession, feedEntrySource)
    }
    private fun handleResume() {
        updateArgumentsFromParentFragment()

        val isRefreshForRelevantPost = arguments?.getString(UF_EXTRA_REFRESH_FOR_RELEVANT_POST).toBoolean()

        if (isRefreshForRelevantPost) {
            if (data?.type != TAB_TYPE_FOR_YOU) {
                removeRefreshForRelevantPostArgument(isRemoveParent = false)
                feedMainViewModel.setActiveTab(TAB_TYPE_FOR_YOU)
            } else {
                removeRefreshForRelevantPostArgument(isRemoveParent = true)
                fetchInitialPost()
            }
        } else {
            resumeCurrentVideo()
        }
    }

    private fun updateArgumentsFromParentFragment() {
        parentFragment?.arguments?.let { parentArguments ->
            arguments?.putAll(parentArguments)
        }
    }

    private fun removeRefreshForRelevantPostArgument(isRemoveParent: Boolean) {
        arguments?.remove(UF_EXTRA_REFRESH_FOR_RELEVANT_POST)

        if (isRemoveParent) {
            parentFragment?.arguments?.remove(UF_EXTRA_REFRESH_FOR_RELEVANT_POST)
        }
    }

    private fun fetchInitialPost() {
        postSourceModel = arguments?.getString(UF_EXTRA_FEED_SOURCE_ID)?.let { sourceId ->
            PostSourceModel(
                id = sourceId,
                source = if (isCdp) {
                    FeedBaseFragment.TAB_TYPE_CDP
                } else {
                    arguments?.getString(UF_EXTRA_FEED_SOURCE_NAME)
                },
                entryPoint = feedEntrySource.entryPoint
            )
        }

        feedPostViewModel.fetchFeedPosts(
            data?.type ?: "",
            isNewData = true,
            postSource = postSourceModel
        )
    }

    override fun impressHighlightCard(
        product: FeedCardProductModel,
        trackerModel: FeedTrackerDataModel?
    ) {
        if (trackerModel != null) {
            currentTrackerData = trackerModel
            feedAnalytics?.impressProductHighlight(product, trackerModel)
        }
    }

    override fun addToCartHighlight(product: FeedCardProductModel, campaign: FeedCardCampaignModel, position: Int) {
        val taggedProduct = MapperProductsToXProducts.transform(product, campaign, ContentTaggedProductUiModel.SourceType.Organic)

        if (!checkForFollowerBottomSheet(
                currentTrackerData?.activityId ?: "",
                position,
                when (taggedProduct.campaign.status) {
                    is ContentTaggedProductUiModel.CampaignStatus.Upcoming -> FeedCardCampaignModel.UPCOMING
                    is ContentTaggedProductUiModel.CampaignStatus.Ongoing -> FeedCardCampaignModel.ONGOING
                    else -> FeedCardCampaignModel.NO
                },
                taggedProduct.campaign.isExclusiveForMember
            ) {}
        ) {
            checkAddToCartAction(taggedProduct, FeedProductActionModel.Source.CardHighlight)
        }
    }

    private fun checkAvailableTracker(product: ContentTaggedProductUiModel, source: FeedProductActionModel.Source) {
        currentTrackerData?.let { data ->
            if (source == FeedProductActionModel.Source.CardHighlight) {
                feedAnalytics?.atcFromProductHighlightWithVariant(
                    trackerModel = data,
                    product = FeedProductActionModel(
                        cartId = "",
                        product = product,
                        source = source
                    )
                )
            }
        }
    }

    override fun onHighlightClick(product: FeedCardProductModel, position: Int) {
        currentTrackerData?.let { data -> feedAnalytics?.sendClickProductHighlight(product, data) }
        RouteManager.route(requireContext(), product.applink)
    }

    override fun onHighlightClose(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let { data -> feedAnalytics?.closeProductHighlight(data) }
    }

    companion object {
        private const val VARIANT_BOTTOM_SHEET_TAG = "atc variant bs"

        private const val ARGUMENT_DATA = "ARGUMENT_DATA"
        private const val ARGUMENT_ENTRY_POINT = "ARGUMENT_ENTRY_POINT"
        private const val ARGUMENT_IS_CDP = "ARGUMENT_IS_CDP"

        private const val ZERO = 0

        private const val TAG_FEED_MENU_BOTTOMSHEET = "TAG_FEED_MENU_BOTTOMSHEET"
        private const val TAG_FEED_PRODUCT_BOTTOM_SHEET = "TAG_FEED_PRODUCT_BOTTOMSHEET"

        private const val PARAM_AUTHOR_TYPE = "author_type"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"

        const val ENTRY_POINT_APPLINK = "applink"
        private const val ENTRY_POINT_DEFAULT = "0"

        fun createFeedFragment(
            data: FeedDataModel,
            extras: Bundle,
            entryPoint: String,
            isCdp: Boolean = false
        ): FeedFragment = FeedFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA, data)
                putAll(extras)
                putString(ARGUMENT_ENTRY_POINT, entryPoint)
                putBoolean(ARGUMENT_IS_CDP, isCdp)
            }
        }
    }

    interface DataSource {
        fun getViewModelStoreOwner(type: String): ViewModelStoreOwner
    }
}
