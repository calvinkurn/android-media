package com.tokopedia.feedplus.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_RELEVANT_POST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.report_content.bottomsheet.ContentThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedContentData
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.analytics.FeedAnalytics
import com.tokopedia.feedplus.analytics.FeedMVCAnalytics
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.domain.mapper.MapperProductsToXProducts
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedShareDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.LikeFeedDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.VideoPlayerManager
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_menu_options.*
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
@Suppress("DEPRECATION")
class FeedFragment :
    BaseDaggerFragment(),
    FeedListener,
    ContentThreeDotsMenuBottomSheet.Listener,
    ProductItemInfoBottomSheet.Listener,
    ShareBottomsheetListener {

    private var _binding: FragmentFeedImmersiveBinding? = null
    private val binding: FragmentFeedImmersiveBinding
        get() = _binding!!

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var dissmisByGreyArea = true
    private var shareData: LinkerData? = null
    private var currentTrackerData: FeedTrackerDataModel? = null

    private val videoPlayerManager by lazy { VideoPlayerManager(requireContext()) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var feedAnalytics: FeedAnalytics

    private val feedMainViewModel: FeedMainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val feedPostViewModel: FeedPostViewModel by viewModels { viewModelFactory }

    private val feedMvcAnalytics = FeedMVCAnalytics()
    private val trackerModelMapper: MapperFeedModelToTrackerDataModel by lazy {
        MapperFeedModelToTrackerDataModel(
            data?.type ?: "",
            arguments?.getString(ARGUMENT_ENTRY_POINT) ?: ""
        )
    }

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
            feedPostViewModel.processSuspendedFollow()
        }

    private val likeLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            feedPostViewModel.processSuspendedLike()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        } ?: savedInstanceState?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        }
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
        observeAddToCart()
        observeReport()
        observeDelete()
        observeFollow()
        observeLikeContent()
        observeResumePage()
    }

    override fun onPause() {
        super.onPause()
        pauseCurrentVideo()
    }

    override fun onResume() {
        super.onResume()
        resumeCurrentVideo()
    }

    override fun onDestroyView() {
        _binding = null
        (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOMSHEET) as? ProductItemInfoBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)?.dismiss()
        (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.dismiss()
        super.onDestroyView()

        videoPlayerManager.releaseAll()
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    override fun onMenuClicked(
        id: String,
        editable: Boolean,
        deletable: Boolean,
        reportable: Boolean,
        contentData: FeedContentData,
        trackerModel: FeedTrackerDataModel
    ) {
        currentTrackerData = trackerModel
        feedAnalytics.eventClickThreeDots(trackerModel)
        activity?.let {
            val feedMenuSheet =
                ContentThreeDotsMenuBottomSheet.getFragment(
                    childFragmentManager,
                    it.classLoader,
                    TAG_FEED_MENU_BOTTOMSHEET
                )
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(getMenuItemData(editable, deletable, reportable, contentData), id)
            feedMenuSheet.show(childFragmentManager, TAG_FEED_MENU_BOTTOMSHEET)
        }
    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem, contentId: String) {
        when (feedMenuItem.type) {
            FeedMenuIdentifier.LAPORKAN -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    (childFragmentManager.findFragmentByTag(TAG_FEED_MENU_BOTTOMSHEET) as? ContentThreeDotsMenuBottomSheet)?.showReportLayoutWhenLaporkanClicked()

                    currentTrackerData?.let {
                        feedAnalytics.eventClickReportContent(it)
                    }
                }
            }

            FeedMenuIdentifier.MODE_NONTON -> {
                adapter?.showClearView(binding.rvFeedPost.currentItem)
                currentTrackerData?.let {
                    feedAnalytics.eventClickWatchMode(it)
                    currentTrackerData = null
                }
            }

            FeedMenuIdentifier.EDIT -> {
                val intent = RouteManager.getIntent(context, INTERNAL_AFFILIATE_CREATE_POST_V2)
                intent.putExtra(PARAM_AUTHOR_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
                intent.putExtra(CreatePostViewModel.TAG, CreatePostViewModel().apply {
                    caption = feedMenuItem.contentData?.caption.orEmpty()
                    postId = feedMenuItem.contentData?.postId.orEmpty()
                    editAuthorId = feedMenuItem.contentData?.authorId.orEmpty()
                })

                startActivity(intent)
            }

            FeedMenuIdentifier.DELETE -> {
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
                        }
                        setSecondaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
                        setSecondaryCTAClickListener {
                            dismiss()
                        }
                        show()
                    }

                }
            }

            else -> {}
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel) {
        feedMainViewModel.reportContent(feedReportRequestParamModel)
        currentTrackerData?.let {
            feedAnalytics.eventClickReasonReportContent(
                it,
                feedReportRequestParamModel.reason
            )
        }
    }

    override fun onMenuBottomSheetCloseClick(contentId: String) {
        // implement bottomsheet close tracker
    }

    override fun onSharePostClicked(
        id: String,
        authorName: String,
        applink: String,
        weblink: String,
        imageUrl: String
    ) {
        activity?.let {
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(id)
                .setName(
                    String.format(
                        getString(feedR.string.feed_share_title),
                        authorName
                    )
                )
                .setDescription(
                    String.format(
                        getString(feedR.string.feed_share_desc_text),
                        authorName
                    )
                )
                .setDesktopUrl(weblink)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(imageUrl)
                .setDeepLink(applink)
                .setUri(weblink)

            shareData = shareDataBuilder.build()
            showUniversalShareBottomSheet(
                getFeedShareDataModel(
                    id,
                    authorName,
                    imageUrl
                )
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

    override fun onWatchPostVideo(trackerModel: FeedTrackerDataModel) {
        feedAnalytics.eventWatchVideoPost()
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
        trackerData: FeedTrackerDataModel?
    ) {
        openProductTagBottomSheet(
            postId = postId,
            author = author,
            postType = postType,
            isFollowing = isFollowing,
            campaign = campaign,
            hasVoucher = hasVoucher,
            products = products,
            trackerData = trackerData,
        )
        trackerData?.let {
            feedAnalytics.eventClickProductTag(it)
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
        trackerData: FeedTrackerDataModel?
    ) {
        trackerData?.let {
            feedAnalytics.eventClickProductLabel(it)
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
                postId = postId,
                author = author,
                postType = postType,
                isFollowing = isFollowing,
                campaign = campaign,
                hasVoucher = hasVoucher,
                products = products,
                trackerData = trackerData
            )
        }
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagModelNew,
        context: Context,
        shopId: String
    ) {
        // do nothing
    }

    override fun onTaggedProductCardImpressed(
        activityId: String,
        postTagItemList: List<FeedXProduct>,
        type: String,
        shopId: String,
        isFollowed: Boolean,
        mediaType: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        // do nothing
    }

    override fun onTaggedProductCardClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int,
        mediaType: String
    ) {
        currentTrackerData?.let {
            feedAnalytics.eventClickProductInProductListBottomSheet(
                it,
                postTagItem.productName,
                postTagItem.id,
                postTagItem.price.toDouble(),
                postTagItem.shopName,
                positionInFeed
            )
        }
        RouteManager.route(requireContext(), redirectUrl)
    }

    override fun onAddToCartButtonClicked(item: ProductPostTagModelNew) {
        currentTrackerData?.let {
            feedAnalytics.eventClickCartButton(
                it,
                item.product.productName,
                item.product.id,
                item.product.price.toDouble(),
                item.shopId,
                item.shopName,
                item.positionInFeed
            )
        }
        feedPostViewModel.addToCart(
            productId = item.product.id,
            productName = item.product.name,
            price = item.price,
            shopId = item.shopId
        )
    }

    override fun onBuyButtonClicked(item: ProductPostTagModelNew) {
        currentTrackerData?.let {
            feedAnalytics.eventClickBuyButton(
                it,
                item.product.productName,
                item.product.id,
                item.product.price.toDouble(),
                item.shopId,
                item.shopName,
                item.positionInFeed
            )
        }
//        TODO("Not yet implemented")
    }

    override fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int) {
        // do nothing
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        dissmisByGreyArea = false
        val universalShareBottomSheet =
            (childFragmentManager.findFragmentByTag(UniversalShareBottomSheet.TAG) as? UniversalShareBottomSheet)
        universalShareBottomSheet?.dismiss()

        val linkerShareData = DataMapper().getLinkerShareData(shareData)
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        context?.let {
                            val shareString =
                                if (shareData?.description?.contains("%s") == true) {
                                    shareData?.description?.let { it1 ->
                                        String.format(
                                            it1,
                                            linkerShareData?.shareUri ?: ""
                                        )
                                    }
                                } else {
                                    shareData?.description + "\n" + linkerShareData?.shareUri
                                }

                            if (shareString != null) {
                                SharingUtil.executeShareIntent(
                                    shareModel,
                                    linkerShareData,
                                    activity,
                                    view,
                                    shareString
                                )
                            }

                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        // Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        dissmisByGreyArea = false
    }

    override fun onReminderClicked(
        campaignId: Long,
        setReminder: Boolean,
        trackerModel: FeedTrackerDataModel?
    ) {
        trackerModel?.let {
            if (setReminder) {
                feedAnalytics.eventClickRemindMe(it)
            } else {
                feedAnalytics.eventClickActiveRemindMe(it)
            }
        }

        feedPostViewModel.setUnsetReminder(campaignId, setReminder)

    }

    override fun onTimerFinishUpcoming() {
//        TODO("Not yet implemented")
    }

    override fun onTimerFinishOnGoing() {
//        TODO("Not yet implemented")
    }

    override fun onTopAdsImpression(
        adViewUrl: String,
        id: String,
        shopId: String,
        uri: String,
        fullEcs: String?,
        position: Int
    ) {
//        TODO("Not yet implemented")
    }

    private fun observeAddToCart() {
        feedPostViewModel.atcRespData.observe(
            viewLifecycleOwner
        ) {
            val productBottomSheet =
                (childFragmentManager.findFragmentByTag(TAG_FEED_PRODUCT_BOTTOMSHEET) as? ProductItemInfoBottomSheet)
            when (it) {
                is FeedResult.Success -> {
                    productBottomSheet?.showToastWithAction(
                        getString(feedR.string.feeds_add_to_cart_success_text),
                        Toaster.TYPE_NORMAL,
                        getString(feedR.string.feeds_add_to_cart_toaster_action_text)
                    ) {
                        moveToAddToCartPage()
                    }
                }
                is FeedResult.Failure -> {
                    val errorMessage = it.error.message
                    if (errorMessage != null) {
                        productBottomSheet?.showToasterOnBottomSheetOnSuccessFollow(
                            errorMessage,
                            Toaster.TYPE_ERROR
                        )
                    }
                }
                else -> {}
            }
        }
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

            it.rvFeedPost.adapter = adapter
            it.rvFeedPost.orientation = ViewPager2.ORIENTATION_VERTICAL
            it.rvFeedPost.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == SCROLL_STATE_IDLE && it.rvFeedPost.currentItem >= (adapter!!.itemCount - MINIMUM_ENDLESS_CALL)) {
                        feedPostViewModel.fetchFeedPosts(data?.type ?: "")
                    }
                    if (state == SCROLL_STATE_IDLE) {
                        feedAnalytics.eventSwipeUpDownContent(
                            trackerModelMapper.tabType,
                            trackerModelMapper.entryPoint
                        )
                    }
                }

                override fun onPageSelected(position: Int) {
                    if (position > ZERO) {
                        notifyItemNotSelected(position - ONE)
                    }
                    if (position < (adapter?.itemCount ?: 0)) {
                        notifyItemNotSelected(position + ONE)
                    }

                    notifyItemSelected(position)
                }
            })
        }
    }

    private fun observePostData() {
        feedPostViewModel.feedHome.observe(viewLifecycleOwner) {
            binding.swipeRefreshFeedLayout.isRefreshing = false
            when (it) {
                is Success -> {
                    hideLoading()
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
                    hideLoading()
                    adapter?.showErrorNetwork()
                }
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
                FeedResult.Loading -> {}
                is FeedResult.Success -> {
                    onSuccessLikeResponse(it.data)
                }
                is FeedResult.Failure -> {
                    val error = it.error
                    val errorMessage = if (error is CustomUiMessageThrowable) {
                        requireContext().getString(error.errorMessageId)
                    } else {
                        ErrorHandler.getErrorMessage(requireContext(), it.error)
                    }
                    showToast(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeResumePage() {
        feedMainViewModel.isPageResumed.observe(viewLifecycleOwner) { isResumed ->
            if (isResumed) resumeCurrentVideo()
            else pauseCurrentVideo()
        }
    }

    private fun pauseCurrentVideo() {
        val currentIndex = binding.rvFeedPost.currentItem
        if (currentIndex >= (adapter?.list?.size ?: 0)) return
        val item = adapter?.list?.get(currentIndex) ?: return
        if (item !is FeedCardVideoContentModel) return

        videoPlayerManager.pause(item.id)
    }

    private fun resumeCurrentVideo() {
        val currentIndex = binding.rvFeedPost.currentItem
        if (currentIndex >= (adapter?.list?.size ?: 0)) return
        val item = adapter?.list?.get(currentIndex) ?: return
        if (item !is FeedCardVideoContentModel) return

        videoPlayerManager.resume(item.id)
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            reportPostLoginResult.launch(intent)
        }
    }

    override fun onLikePostCLicked(
        id: String,
        isLiked: Boolean,
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
            val feedLikeAction = FeedLikeAction.getLikeAction(isLiked)
            feedPostViewModel.likeContent(
                contentId = id,
                action = feedLikeAction,
                rowNumber = rowNumber
            )
        } else {
            feedPostViewModel.suspendLikeContent(id, rowNumber)
            likeLoginResult.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        }
    }

    private fun onSuccessLikeResponse(data: LikeFeedDataModel) {
        val newList = adapter?.list
        val rowNumber = data.rowNumber
        if ((newList?.size ?: 0) > data.rowNumber) {
            val item = newList?.get(rowNumber)
            if (item is FeedCardImageContentModel) {
                if (!item.like.isLiked) {
                    try {
                        val likeValue = Integer.valueOf(item.like.countFmt) + 1
                        adapter?.updateLikeUnlikeData(
                            rowNumber,
                            like = item.like.copy(
                                isLiked = item.like.isLiked.not(),
                                countFmt = likeValue.toString(),
                                count = item.like.count + 1
                            )
                        )
                    } catch (ignored: NumberFormatException) {
                        Timber.e(ignored)
                    }
                } else {
                    try {
                        val likeValue = Integer.valueOf(item.like.countFmt) - 1
                        adapter?.updateLikeUnlikeData(
                            rowNumber,
                            like = item.like.copy(
                                isLiked = item.like.isLiked.not(),
                                countFmt = likeValue.toString(),
                                count = item.like.count - 1
                            )
                        )
                    } catch (ignored: NumberFormatException) {
                        Timber.e(ignored)
                    }
                }
                adapter?.notifyItemChanged(
                    rowNumber,
                    FEED_POST_LIKED_UNLIKED
                )
            }
        }
    }

    private fun getFeedShareDataModel(
        id: String,
        authorName: String,
        imageUrl: String
    ): FeedShareDataModel = FeedShareDataModel(
        id = id,
        name = authorName,
        tnTitle = (
            String.format(
                context?.getString(feedR.string.feed_share_title) ?: "",
                authorName
            )
            ),
        tnImage = imageUrl,
        ogUrl = imageUrl
    )

    private fun showUniversalShareBottomSheet(shareModel: FeedShareDataModel) {
        val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@FeedFragment)
            setUtmCampaignData(
                pageName = FeedShareDataModel.PAGE,
                userId = userSession.userId,
                pageId = shareModel.id,
                feature = FeedShareDataModel.FEATURE
            )

            setMetaData(
                tnTitle = shareModel.tnTitle,
                tnImage = shareModel.tnImage
            )
        }
        universalShareBottomSheet.setOnDismissListener {
            if (dissmisByGreyArea) {
                // TODO to be used for analytics
            } else {
                dissmisByGreyArea = true
            }
        }
        universalShareBottomSheet.let {
            if (!it.isAdded) {
                it.show(childFragmentManager, this, null)
            }
        }
    }

    private fun getMenuItemData(
        editable: Boolean,
        deletable: Boolean,
        reportable: Boolean,
        contentData: FeedContentData
    ): List<FeedMenuItem> {
        val items = arrayListOf<FeedMenuItem>()

        if (editable) {
            items.add(
                FeedMenuItem(
                    drawable = getIconUnifyDrawable(
                        requireContext(),
                        IconUnify.EDIT,
                    ),
                    name = FeedMenuIdentifier.EDIT.value,
                    type = FeedMenuIdentifier.EDIT,
                    contentData = contentData
                )
            )
        }
        items.add(
            FeedMenuItem(
                drawable = getIconUnifyDrawable(requireContext(), IconUnify.VISIBILITY),
                name = getString(feedR.string.feed_clear_mode),
                type = FeedMenuIdentifier.MODE_NONTON
            )
        )
        if (reportable) {
            items.add(
                FeedMenuItem(
                    drawable = getIconUnifyDrawable(
                        requireContext(),
                        IconUnify.WARNING,
                        MethodChecker.getColor(
                            context,
                            unifyR.color.Unify_RN500
                        )
                    ),
                    name = getString(feedR.string.feed_report),
                    type = FeedMenuIdentifier.LAPORKAN
                )
            )
        }
        if (deletable) {
            items.add(
                FeedMenuItem(
                    drawable = getIconUnifyDrawable(
                        requireContext(),
                        IconUnify.DELETE,
                        MethodChecker.getColor(
                            context,
                            unifyR.color.Unify_RN500
                        )
                    ),
                    name = FeedMenuIdentifier.DELETE.value,
                    type = FeedMenuIdentifier.DELETE
                )
            )
        }

        return items
    }

    private fun openProductTagBottomSheet(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        products: List<FeedCardProductModel>,
        hasVoucher: Boolean,
        trackerData: FeedTrackerDataModel?
    ) {
        feedMvcAnalytics.trackerData = trackerData
        trackerData?.let {
            currentTrackerData = it
            feedAnalytics.eventViewProductListBottomSheets(
                it,
                products
            )
        }

        val productBottomSheet = ProductItemInfoBottomSheet()
        productBottomSheet.closeClicked = {
            trackerData?.let {
                feedAnalytics.eventClickCloseProductListBottomSheet(it)
            }
        }
        productBottomSheet.show(
            childFragmentManager,
            this,
            ProductBottomSheetData(
                products = MapperProductsToXProducts.transform(products),
                postId = postId,
                shopId = author.id,
                postType = postType,
                isFollowed = isFollowing,
                shopName = author.name,
                saleStatus = campaign.status,
                saleType = campaign.name,
                hasVoucher = hasVoucher,
                authorType = author.type.toString()
            ),
            viewModelFactory = viewModelFactory,
            customMvcTracker = feedMvcAnalytics,
            tag = TAG_FEED_PRODUCT_BOTTOMSHEET
        )
    }

    private fun moveToAddToCartPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
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

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"
        private const val ARGUMENT_ENTRY_POINT = "ARGUMENT_ENTRY_POINT"

        private const val MINIMUM_ENDLESS_CALL = 3

        private const val ZERO = 0
        private const val ONE = 1

        private const val TAG_FEED_MENU_BOTTOMSHEET = "TAG_FEED_MENU_BOTTOMSHEET"
        private const val TAG_FEED_PRODUCT_BOTTOMSHEET = "TAG_FEED_PRODUCT_BOTTOMSHEET"

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
