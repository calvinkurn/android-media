package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_RELEVANT_POST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.analytic.ContentCommentAnalytics
import com.tokopedia.content.common.comment.analytic.ContentCommentAnalyticsModel
import com.tokopedia.content.common.comment.ui.ContentCommentBottomSheet
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feed.component.product.FeedTaggedProductBottomSheet
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.bottomsheets.FeedFollowersOnlyBottomSheet
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.analytics.FeedAnalytics
import com.tokopedia.feedplus.analytics.FeedMVCAnalytics
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.domain.mapper.MapperProductsToXProducts
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.VideoPlayerManager
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment :
    BaseDaggerFragment(),
    FeedListener,
    ContentThreeDotsMenuBottomSheet.Listener,
    FeedTaggedProductBottomSheet.Listener,
    FeedFollowersOnlyBottomSheet.Listener {

    private var _binding: FragmentFeedImmersiveBinding? = null
    private val binding: FragmentFeedImmersiveBinding
        get() = _binding!!

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var currentTrackerData: FeedTrackerDataModel? = null

    private val atcVariantViewModel by lazyThreadSafetyNone {
        ViewModelProvider(requireActivity())[AtcVariantSharedViewModel::class.java]
    }

    private val videoPlayerManager by lazy { VideoPlayerManager(requireContext()) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var feedAnalytics: FeedAnalytics

    @Inject
    lateinit var commentAnalytics: ContentCommentAnalytics.Creator

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private val feedMainViewModel: FeedMainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val feedPostViewModel: FeedPostViewModel by viewModels { viewModelFactory }

    private val feedMvcAnalytics = FeedMVCAnalytics()
    private val trackerModelMapper: MapperFeedModelToTrackerDataModel by lazy {
        MapperFeedModelToTrackerDataModel(
            data?.type ?: "",
            arguments?.getString(ARGUMENT_ENTRY_POINT) ?: ""
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
                feedMenuSheet.showReportLayoutWhenLaporkanClicked()
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

    private var feedFollowersOnlyBottomSheet: FeedFollowersOnlyBottomSheet? = null

    private val layoutManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }
    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == SCROLL_STATE_IDLE &&
                layoutManager.findFirstVisibleItemPosition() >= (adapter?.itemCount.orZero() - MINIMUM_ENDLESS_CALL) &&
                !feedPostViewModel.shouldShowNoMoreContent
            ) {
                adapter?.showLoading()
                feedPostViewModel.fetchFeedPosts(data?.type ?: "")
            }

            // update item state and send tracker
            if (newState == SCROLL_STATE_IDLE) {
                feedAnalytics.eventSwipeUpDownContent(
                    trackerModelMapper.tabType,
                    trackerModelMapper.entryPoint
                )

                val position = layoutManager.findFirstVisibleItemPosition()

                if (position > ZERO) {
                    notifyItemNotSelected(position - ONE)
                }
                if (position < (adapter?.list?.size.orZero())) {
                    notifyItemNotSelected(position + ONE)
                }

                notifyItemSelected(position)
            }
        }
    }

    private var mAuthor: FeedAuthorModel? = null
    private var mProducts: List<FeedCardProductModel>? = null
    private var mHasVoucher: Boolean = false
    private var mCampaign: FeedCardCampaignModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.fragmentFactory = fragmentFactory

        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        } ?: savedInstanceState?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        }

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if (checkResume(isOnResume = true)) resumeCurrentVideo()
                    }
                    Lifecycle.Event.ON_PAUSE -> pauseCurrentVideo()
                    else -> {}
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(ARGUMENT_DATA, data)
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

        feedPostViewModel.fetchFeedPosts(
            data?.type ?: "",
            isNewData = true,
            postId = arguments?.getString(UF_EXTRA_FEED_RELEVANT_POST)
        )

        initView()
        observePostData()
        observeReport()
        observeDelete()
        observeFollow()
        observeLikeContent()
        observeResumePage()
        observeMerchantVoucher()
        observeAddProductToCart()
        observeBuyProduct()
        observeReminder()

        observeEvent()
    }

    override fun onDestroyView() {
        _binding = null
        dismissFeedProductBottomSheet()
        dismissFeedMenuBottomSheet()
        dismissAtcVariantBottomSheet()
        dismissShareBottomSheet()
        super.onDestroyView()

        videoPlayerManager.releaseAll()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onMenuClicked(
        id: String,
        menuItems: List<FeedMenuItem>,
        trackerModel: FeedTrackerDataModel
    ) {
        currentTrackerData = trackerModel
        feedAnalytics.eventClickThreeDots(trackerModel)
        activity?.let {
            val feedMenuSheet =
                ContentThreeDotsMenuBottomSheet.getFragment(
                    childFragmentManager,
                    it.classLoader
                )
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(menuItems, id)
            feedMenuSheet.show(childFragmentManager, TAG_FEED_MENU_BOTTOMSHEET)
        }
    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.Report -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.showReportLayoutWhenLaporkanClicked()

                    currentTrackerData?.let {
                        feedAnalytics.eventClickReportContent(it)
                    }
                }
            }

            FeedMenuIdentifier.WatchMode -> {
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position >= ZERO) {
                    adapter?.showClearView(layoutManager.findFirstCompletelyVisibleItemPosition())
                }
                currentTrackerData?.let {
                    feedAnalytics.eventClickWatchMode(it)
                    currentTrackerData = null
                }
            }

            FeedMenuIdentifier.Edit -> {
                val intent = RouteManager.getIntent(context, INTERNAL_AFFILIATE_CREATE_POST_V2)
                intent.putExtra(PARAM_AUTHOR_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
                intent.putExtra(
                    CreatePostViewModel.TAG,
                    CreatePostViewModel().apply {
                        caption = feedMenuItem.contentData?.caption.orEmpty()
                        postId = feedMenuItem.contentData?.postId.orEmpty()
                        editAuthorId = feedMenuItem.contentData?.authorId.orEmpty()
                    }
                )

                startActivity(intent)
            }

            FeedMenuIdentifier.Delete -> {
                context?.let {
                    DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(feedR.string.dialog_delete_post_title))
                        setDescription(getString(feedR.string.dialog_delete_post_subtitle))
                        setPrimaryCTAText(getString(feedR.string.feed_delete))
                        setPrimaryCTAClickListener {
                            feedPostViewModel.doDeletePost(
                                feedMenuItem.contentData?.postId.orEmpty(),
                                feedMenuItem.contentData?.rowNumber.orZero()
                            )
                            dismiss()
                        }
                        setSecondaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
                        setSecondaryCTAClickListener {
                            dismiss()
                        }
                        show()
                    }
                }
            }

            FeedMenuIdentifier.SeePerformance,
            FeedMenuIdentifier.LearnVideoInsight -> {
                RouteManager.route(requireContext(), feedMenuItem.appLink)
            }
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param) {
        feedMainViewModel.reportContent(feedReportRequestParamModel)
        currentTrackerData?.let {
            feedAnalytics.eventClickReasonReportContent(
                it,
                feedReportRequestParamModel.reason
            )
        }
    }

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        // add analytics(if any)
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
                requireContext().getString(feedR.string.feed_share_title),
                data.author.name
            ),
            tnImage = data.mediaUrl
        )
        if (!shareBottomSheet.isVisible) {
            feedAnalytics.sendClickShareButtonEvent(trackerModel)
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
            feedAnalytics.eventClickFollowButton(it)
        }
        if (userSession.isLoggedIn) {
            feedPostViewModel.doFollow(id, encryptedId, isShop)
        } else {
            feedPostViewModel.suspendFollow(id, encryptedId, isShop)
            followLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }
    }

    override fun changeTab() {
        feedMainViewModel.changeCurrentTabByType(FeedBaseFragment.TAB_TYPE_FOR_YOU)
    }

    override fun reload() {
        feedPostViewModel.fetchFeedPosts(data?.type ?: "")
        adapter?.removeErrorNetwork()
        showLoading()
        adapter?.showLoading()
    }

    override fun getVideoPlayer(id: String): FeedExoPlayer {
        return videoPlayerManager.occupy(id)
    }

    override fun detachPlayer(player: FeedExoPlayer) {
        videoPlayerManager.detach(player)
    }

    override fun onPauseVideoPost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics.eventClickPauseVideo(trackerModel)
    }

    override fun onTapHoldSeekbarVideoPost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics.eventHoldSeekBarVideo(trackerModel)
    }

    override fun onWatchPostVideo(
        model: FeedCardVideoContentModel,
        trackerModel: FeedTrackerDataModel
    ) {
        feedAnalytics.eventWatchVideoPost()
        feedPostViewModel.trackVisitChannel(model)
        feedPostViewModel.trackChannelPerformance(model)
    }

    override fun onSwipeMultiplePost(trackerModel: FeedTrackerDataModel) {
        feedAnalytics.eventSwipeLeftRightMultiplePost(trackerModel)
    }

    override fun onAuthorNameClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics.eventClickAuthorName(it)
        }
    }

    override fun onAuthorProfilePictureClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics.eventClickAuthorProfilePicture(it)
        }
    }

    override fun onCaptionClicked(trackerModel: FeedTrackerDataModel?) {
        trackerModel?.let {
            feedAnalytics.eventClickContentCaption(it)
        }
    }

    override fun onLivePreviewClicked(
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int,
        productId: String,
        authorName: String
    ) {
        trackerModel?.let {
            feedAnalytics.eventClickLivePreview(it, productId, authorName, positionInFeed)
        }
    }

    override fun onPostImpression(
        trackerModel: FeedTrackerDataModel?,
        activityId: String,
        positionInFeed: Int
    ) {
        trackerModel?.let {
            feedAnalytics.eventPostImpression(
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
                author = author,
                hasVoucher = hasVoucher,
                products = products,
                trackerData = trackerModel,
                campaign = campaign
            )
            trackerModel?.let {
                feedAnalytics.eventClickProductTag(it)
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
            trackerModel?.let {
                feedAnalytics.eventClickProductLabel(it, products)
                feedAnalytics.eventClickContentProductLabel(it)
            }
            if (products.size == FeedProductTagView.PRODUCT_COUNT_ONE) {
                val appLink = products.firstOrNull()?.applink
                if (appLink?.isNotEmpty() == true) {
                    activity?.let {
                        RouteManager.route(it, appLink)
                    }
                }
            } else {
                openProductTagBottomSheet(
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
                feedAnalytics.eventClickRemindMe(it)
            } else {
                feedAnalytics.eventClickActiveRemindMe(it)
            }
        }

        feedPostViewModel.setUnsetReminder(campaignId, setReminder, type)
    }

    override fun onTopAdsImpression(
        adViewUrl: String,
        id: String,
        shopId: String,
        uri: String,
        fullEcs: String?,
        position: Int
    ) {
        topAdsUrlHitter.hitImpressionUrl(
            this::class.java.simpleName,
            adViewUrl,
            id,
            uri,
            fullEcs
        )
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
                feedAnalytics.eventClickCampaignRibbon(
                    it,
                    campaignName,
                    positionInFeed
                )
                openProductTagBottomSheet(
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
        if (adapter?.list?.size.orZero() > position) {
            adapter?.list?.get(position)?.let {
                var author: FeedAuthorModel? = null
                when (it) {
                    is FeedCardImageContentModel -> author = it.author
                    is FeedCardVideoContentModel -> author = it.author
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

    private fun observeReport() {
        feedMainViewModel.reportResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    currentTrackerData?.let { trackerData ->
                        feedAnalytics.eventViewSuccessReportContent(trackerData)
                        currentTrackerData = null
                    }
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.apply {
                        setFinalView()
                    }
                }
                is Fail -> {}
            }
        }
    }

    private fun observeDelete() {
        feedPostViewModel.deletePostResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedR.string.toast_delete_post_success),
                        Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToast(
                        getString(feedR.string.toast_delete_post_failed),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun initView() {
        binding.let {
            it.swipeRefreshFeedLayout.setOnRefreshListener {
                feedPostViewModel.fetchFeedPosts(data?.type ?: "", isNewData = true)
            }

            adapter = FeedPostAdapter(
                FeedAdapterTypeFactory(this, binding.rvFeedPost, trackerModelMapper)
            )
            if (adapter!!.itemCount == 0) {
                showLoading()
            }

            PagerSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.removeOnScrollListener(contentScrollListener)
            it.rvFeedPost.addOnScrollListener(contentScrollListener)
        }
    }

    private fun observePostData() {
        feedPostViewModel.feedHome.observe(viewLifecycleOwner) {
            binding.swipeRefreshFeedLayout.isRefreshing = false
            hideLoading()
            adapter?.hideLoading()
            when (it) {
                is Success -> {
                    if (it.data.items.isEmpty()) {
                        context?.let { ctx ->
                            adapter?.setElements(
                                listOf(
                                    FeedNoContentModel.getNoContentInstance(ctx)
                                )
                            )
                        }
                    } else {
                        adapter?.updateList(it.data.items)
                        context?.let { ctx ->
                            if (feedPostViewModel.shouldShowNoMoreContent) {
                                adapter?.addElement(FeedNoContentModel.getNoMoreContentInstance(ctx))
                            }
                        }
                        feedPostViewModel.fetchTopAdsData()
                        if (it.data.pagination.totalData == it.data.items.size) {
                            view?.post { notifyItemSelected(0) }
                        }
                    }
                    feedMainViewModel.onPostDataLoaded(it.data.items.isNotEmpty())
                }
                is Fail -> {
                    adapter?.showErrorNetwork()
                }
                else -> {}
            }
        }
    }

    private fun observeFollow() {
        feedPostViewModel.followResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToast(
                        getString(feedR.string.feed_message_success_follow, it.data),
                        Toaster.TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToast(
                        getString(feedR.string.feed_message_failed_follow),
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
                resumeCurrentVideo()
            } else {
                pauseCurrentVideo()
            }
        }
    }

    private fun observeAddProductToCart() {
        feedPostViewModel.observeAddProductToCart.observe(viewLifecycleOwner) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)
            when (it) {
                is Success -> productBottomSheet?.doShowToaster(
                    message = getString(feedR.string.feeds_add_to_cart_success_text),
                    actionText = getString(feedR.string.feeds_add_to_cart_toaster_action_text),
                    actionClickListener = {
                        currentTrackerData?.let { trackerData ->
                            feedAnalytics.eventClickViewCart(trackerData)
                        }
                        goToCartPage()
                    }
                )
                is Fail -> productBottomSheet?.doShowToaster(
                    message = it.throwable.localizedMessage.orEmpty(),
                    type = Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun observeBuyProduct() {
        feedPostViewModel.observeBuyProduct.observe(viewLifecycleOwner) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)
            when (it) {
                is Success -> goToCartPage()
                is Fail -> productBottomSheet?.doShowToaster(
                    message = it.throwable.localizedMessage.orEmpty(),
                    type = Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun observeMerchantVoucher() {
        feedPostViewModel.merchantVoucherLiveData.observe(viewLifecycleOwner) { result ->
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOM_SHEET) as? FeedTaggedProductBottomSheet)
            when (result) {
                is Success -> {
                    if (!result.data.animatedInfoList.isNullOrEmpty() && currentTrackerData != null) {
                        feedAnalytics.eventMvcWidgetImpression(
                            currentTrackerData!!,
                            result.data.animatedInfoList!!
                        )
                        feedMvcAnalytics.voucherList = result.data.animatedInfoList!!
                    }
                    productBottomSheet?.showMerchantVoucherWidget(
                        result.data,
                        feedMvcAnalytics
                    )
                }
                is Fail -> productBottomSheet?.hideMerchantVoucherWidget()
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
                            binding.rvFeedPost.smoothScrollToPosition(0)
                        }
                        else -> {}
                    }

                    feedMainViewModel.consumeEvent(event)
                }
            }
        }
    }

    private fun pauseCurrentVideo() {
        val currentIndex = layoutManager.findFirstVisibleItemPosition()
        if (currentIndex < ZERO || currentIndex >= (adapter?.list?.size ?: 0)) return
        val item = adapter?.list?.get(currentIndex) ?: return

        when (item) {
            is FeedCardVideoContentModel -> pauseVideo(item.id)
            is FeedCardLivePreviewContentModel -> pauseVideo(item.id)
            else -> {}
        }
    }

    private fun resumeCurrentVideo() {
        val currentIndex = layoutManager.findFirstVisibleItemPosition()
        if (currentIndex < ZERO || currentIndex >= (adapter?.list?.size ?: 0)) return
        val item = adapter?.list?.get(currentIndex) ?: return

        when (item) {
            is FeedCardVideoContentModel -> resumeVideo(item.id)
            is FeedCardLivePreviewContentModel -> resumeVideo(item.id)
            else -> {}
        }
    }

    private fun pauseVideo(id: String) {
        videoPlayerManager.pause(id, shouldReset = true)
    }

    private fun resumeVideo(id: String) {
        videoPlayerManager.resume(id)
    }

    private fun checkResume(
        isOnResume: Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
        isPageResumed: Boolean = feedMainViewModel.isPageResumed.value != false
    ): Boolean {
        return isPageResumed && isOnResume
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
            feedAnalytics.eventDoubleClickLikeButton(trackerModel)
        } else {
            feedAnalytics.eventClickLikeButton(trackerModel)
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
            feedAnalytics.eventClickComment(it)
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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ContentCommentBottomSheet -> {
                val eventLabel = currentTrackerData?.let {
                    feedAnalytics.getEventLabel(it)
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
        }
    }

    private fun openProductTagBottomSheet(
        author: FeedAuthorModel,
        products: List<FeedCardProductModel>,
        hasVoucher: Boolean,
        trackerData: FeedTrackerDataModel?,
        campaign: FeedCardCampaignModel
    ) {
        openFeedTaggedProductBottomSheet(
            author = author,
            products = products,
            hasVoucher = hasVoucher,
            trackerData = trackerData,
            campaign = campaign,
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
        binding.feedLoading.show()
        binding.swipeRefreshFeedLayout.hide()
    }

    private fun hideLoading() {
        binding.feedLoading.hide()
        binding.swipeRefreshFeedLayout.show()
    }

    private fun notifyItemSelected(position: Int) {
        adapter?.notifyItemChanged(position, FEED_POST_SELECTED)
    }

    private fun notifyItemNotSelected(position: Int) {
        adapter?.notifyItemChanged(position, FEED_POST_NOT_SELECTED)
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

    override fun onProductCardClicked(product: FeedTaggedProductUiModel, itemPosition: Int) {
        if (product.appLink.isEmpty()) return

        currentTrackerData?.let { data ->
            feedAnalytics.eventClickProductInProductListBottomSheet(
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
        product: FeedTaggedProductUiModel,
        itemPosition: Int
    ) {
        if (!checkForFollowerBottomSheet(
                currentTrackerData?.activityId ?: "",
                itemPosition,
                when (product.campaign.status) {
                    is FeedTaggedProductUiModel.CampaignStatus.Upcoming -> FeedCardCampaignModel.UPCOMING
                    is FeedTaggedProductUiModel.CampaignStatus.Ongoing -> FeedCardCampaignModel.ONGOING
                    else -> FeedCardCampaignModel.NO
                },
                product.campaign.isExclusiveForMember
            ) {}
        ) {
            currentTrackerData?.let { data ->
                feedAnalytics.eventClickCartButton(
                    trackerData = data,
                    productName = product.title,
                    productId = product.id,
                    productPrice = product.finalPrice,
                    shopId = product.shop.id,
                    shopName = product.shop.name,
                    index = itemPosition
                )
            }

            checkAddToCartAction(product)
        }
    }

    private fun checkAddToCartAction(product: FeedTaggedProductUiModel) {
        when {
            userSession.isLoggedIn -> {
                if (product.showGlobalVariant) {
                    dismissFeedProductBottomSheet()
                    openVariantBottomSheet(product)
                }
                else feedPostViewModel.addProductToCart(product)
            }
            !userSession.isLoggedIn -> {
                if (product.showGlobalVariant) RouteManager.route(context, ApplinkConst.LOGIN)
                else {
                    feedPostViewModel.suspendAddProductToCart(product)
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
        author: FeedAuthorModel?,
        products: List<FeedCardProductModel>?,
        hasVoucher: Boolean,
        trackerData: FeedTrackerDataModel?,
        campaign: FeedCardCampaignModel?
    ) {
        if (products.isNullOrEmpty() || campaign == null) return

        val productBottomSheet = FeedTaggedProductBottomSheet().apply {
            setCustomListener(this@FeedFragment)
        }

        fun trackOpenProductTagBottomSheet(data: FeedTrackerDataModel) {
            feedMvcAnalytics.trackerData = data
            currentTrackerData = data
            feedAnalytics.eventViewProductListBottomSheets(data, products)

            productBottomSheet.setOnDismissListener {
                feedAnalytics.eventClickCloseProductListBottomSheet(data)
            }
        }

        if (trackerData != null) trackOpenProductTagBottomSheet(trackerData)
        saveFeedTaggedProductArgs(author, products, hasVoucher, trackerData, campaign)

        val mappedProducts = products.map { MapperProductsToXProducts.transform(it, campaign) }
        productBottomSheet.show(
            taggedProducts = mappedProducts,
            manager = childFragmentManager,
            tag = TAG_FEED_PRODUCT_BOTTOM_SHEET
        )
        if (hasVoucher && author?.type?.isShop == true) getMerchantVoucher(author.id)
    }

    private fun openVariantBottomSheet(product: FeedTaggedProductUiModel) {
        atcVariantViewModel.setAtcBottomSheetParams(
            ProductVariantBottomSheetParams(
                trackerCdListName = currentTrackerData?.activityId.orEmpty(),
                productId = product.id,
                shopId = product.shop.id,
                showQtyEditor = false,
                isTokoNow = false,
                pageSource = VariantPageSource.FEED_PAGESOURCE.source,
                dismissAfterTransaction = false,
            )
        )

        val atcVariantBottomSheet = AtcVariantBottomSheet()
        atcVariantBottomSheet.showNow(childFragmentManager, VARIANT_BOTTOM_SHEET_TAG)
        atcVariantBottomSheet.setOnDismissListener {
            openFeedTaggedProductBottomSheet(
                author = mAuthor,
                products = mProducts,
                hasVoucher = mHasVoucher,
                trackerData = currentTrackerData,
                campaign = mCampaign,
            )
        }
    }

    override fun onBuyProductButtonClicked(product: FeedTaggedProductUiModel, itemPosition: Int) {
        if (!checkForFollowerBottomSheet(
                currentTrackerData?.activityId ?: "",
                itemPosition,
                when (product.campaign.status) {
                    is FeedTaggedProductUiModel.CampaignStatus.Upcoming -> FeedCardCampaignModel.UPCOMING
                    is FeedTaggedProductUiModel.CampaignStatus.Ongoing -> FeedCardCampaignModel.ONGOING
                    else -> FeedCardCampaignModel.NO
                },
                product.campaign.isExclusiveForMember
            ) {}
        ) {
            currentTrackerData?.let { data ->
                feedAnalytics.eventClickBuyButton(
                    trackerData = data,
                    productName = product.title,
                    productId = product.id,
                    productPrice = product.finalPrice,
                    shopId = product.shop.id,
                    shopName = product.shop.name,
                    index = itemPosition
                )
            }

            if (userSession.isLoggedIn) {
                feedPostViewModel.buyProduct(product)
            } else {
                feedPostViewModel.suspendBuyProduct(product)
                buyLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
            }
        }
    }

    private fun observeReminder() {
        feedPostViewModel.reminderResult.observe(viewLifecycleOwner) {
            val message = when (it) {
                is Success -> {
                    val type = when (it.data.reminderType) {
                        FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> getString(feedR.string.feed_flash_sale)
                        FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> getString(feedR.string.feed_special_release)
                        else -> ""
                    }

                    // if set reminder
                    if (it.data.isSetReminder) {
                        getString(feedR.string.feed_reminder_set_success, type)
                    } else {
                        // if unset reminder
                        getString(feedR.string.feed_reminder_unset_success)
                    }
                }
                is Fail -> it.throwable.message
                else -> ""
            }

            showToast(
                message = message.orEmpty(),
                type = if (it is Success) Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR,
                actionText = getString(feedR.string.feed_cta_ok_toaster)
            )
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
            feedAnalytics.sendClickSharingChannelEvent(
                shareModel.socialMediaName.orEmpty(),
                trackerModel
            )
            generateShareLinkUrl(data, shareModel, trackerModel)
        }

        override fun onCloseOptionClicked() {
            feedAnalytics.sendClickCloseButtonOnShareBottomSheetEvent(trackerModel)
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
                    getString(feedR.string.feed_share_title),
                    data.author.name
                )
            )
            .setDescription(
                String.format(
                    getString(feedR.string.feed_share_desc_text),
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
                                    message = getString(feedR.string.feed_copy_link_success_message),
                                    actionText = getString(com.tokopedia.content.common.R.string.feed_ok),
                                    actionClickListener = {
                                        feedAnalytics.sendClickOkeShareToasterEvent(trackerModel)
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

    companion object {
        private const val VARIANT_BOTTOM_SHEET_TAG = "atc variant bs"

        private const val ARGUMENT_DATA = "ARGUMENT_DATA"
        private const val ARGUMENT_ENTRY_POINT = "ARGUMENT_ENTRY_POINT"

        private const val MINIMUM_ENDLESS_CALL = 3

        private const val ZERO = 0
        private const val ONE = 1

        private const val TAG_FEED_MENU_BOTTOMSHEET = "TAG_FEED_MENU_BOTTOMSHEET"
        private const val TAG_FEED_PRODUCT_BOTTOM_SHEET = "TAG_FEED_PRODUCT_BOTTOMSHEET"

        private const val PARAM_AUTHOR_TYPE = "author_type"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"

        fun createFeedFragment(
            data: FeedDataModel,
            extras: Bundle,
            entryPoint: String
        ): FeedFragment = FeedFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ARGUMENT_DATA, data)
                putAll(extras)
                putString(ARGUMENT_ENTRY_POINT, entryPoint)
            }
        }
    }
}
