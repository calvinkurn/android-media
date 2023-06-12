package com.tokopedia.feed_shop.shop.view.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feed_shop.R
import com.tokopedia.feed_shop.analytics.ShopAnalytics
import com.tokopedia.feed_shop.databinding.FragmentFeedShopBinding
import com.tokopedia.feed_shop.di.DaggerFeedShopComponent
import com.tokopedia.feed_shop.shop.domain.WhitelistDomain
import com.tokopedia.feed_shop.shop.view.InterfaceShopPageFab
import com.tokopedia.feed_shop.shop.view.adapter.factory.FeedShopFactoryImpl
import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopUiModel
import com.tokopedia.feed_shop.shop.view.model.WhitelistUiModel
import com.tokopedia.feed_shop.shop.view.util.PostMenuListener
import com.tokopedia.feed_shop.shop.view.util.createBottomMenu
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.util.goToInformationWebview
import com.tokopedia.seller_migration_common.presentation.util.goToSellerApp
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.interfaces.HasSharedViewModel
import com.tokopedia.shop.common.view.interfaces.ISharedViewModel
import com.tokopedia.shop.common.view.interfaces.ShopPageSharedListener
import com.tokopedia.shop.common.view.model.ShopPageFabConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import javax.inject.Inject
import com.tokopedia.universal_sharing.R as universalSharingR

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopFragment :
    BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
    KolPostLikeListener,
    DynamicPostViewHolder.DynamicPostListener,
    TopadsShopViewHolder.TopadsShopListener,
    CardTitleView.CardTitleListener,
    ImagePostViewHolder.ImagePostListener,
    YoutubeViewHolder.YoutubePostListener,
    PollAdapter.PollOptionListener,
    GridPostAdapter.GridItemListener,
    VideoViewHolder.VideoViewListener,
    FeedMultipleImageView.FeedMultipleImageViewListener,
    FeedShopContract.View,
    TopAdsBannerViewHolder.TopAdsBannerListener,
    InterfaceShopPageFab {

    override val androidContext: Context
        get() = requireContext()

    private lateinit var createPostUrl: String
    private lateinit var shopId: String
    private var isLoading = false
    private var isForceRefresh = true

    private var whitelistDomain: WhitelistDomain = WhitelistDomain()

    private val fabConfig: ShopPageFabConfig by lazy {
        ShopPageFabConfig(
            items = arrayListOf(FloatingButtonItem(iconUnifyID = IconUnify.ADD, title = "")),
            onMainCircleButtonClicked = {
                goToCreatePost()
                shopAnalytics.eventClickCreatePost()
            }
        )
    }

    @Inject
    lateinit var presenter: FeedShopContract.Presenter

    @Inject
    lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var shopAnalytics: ShopAnalytics

    @Inject
    override lateinit var userSession: UserSessionInterface

    private val viewBinding: FragmentFeedShopBinding? by viewBinding()
    private val shopFeedTabSharedViewModel: ISharedViewModel by lazy {
        ((activity as HasSharedViewModel).getSharedViewModel() as ISharedViewModel)
    }

    companion object {
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val TEXT_PLAIN = "text/plain"
        private const val CREATE_POST = 888
        private const val OPEN_DETAIL = 54
        private const val LOGIN_CODE = 1383
        private const val LOGIN_FOLLOW_CODE = 1384
        private const val OPEN_CONTENT_REPORT = 1130
        private const val KOL_COMMENT_CODE = 13
        private const val PARAM_ENTRY_POINT = "entry_point"
        private const val SHOP_PAGE = "shop_page"
        private const val WEBVIEW_URL_FORMAT = "%s?url=%s"

        private const val PARAM_CREATE_POST_URL: String = "PARAM_CREATE_POST_URL"
        private const val PARAM_SHOP_ID: String = "PARAM_SHOP_ID"

        //region Content Report Param
        private const val CONTENT_REPORT_RESULT_SUCCESS = "result_success"
        private const val CONTENT_REPORT_RESULT_ERROR_MSG = "error_msg"
        //endregion

        //region Media Preview Param
        private const val MEDIA_PREVIEW_INDEX = "media_index"
        //endregion

        //region Kol Comment Param
        private const val COMMENT_ARGS_POSITION = "ARGS_POSITION"
        //endregion

        private const val PDP_APP_LINK_HOST = "product"

        // Edit Post
        private const val KEY_AUTHOR_TYPE = "author_type"
        private const val VALUE_AUTHOR_TYPE = "content-preview-page"

        fun createInstance(shopId: String, createPostUrl: String): FeedShopFragment {
            val fragment = FeedShopFragment()
            val bundle = Bundle()
            bundle.putString(PARAM_SHOP_ID, shopId)
            bundle.putString(PARAM_CREATE_POST_URL, createPostUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        SplitCompat.install(context)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_shop, container, false)
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return viewBinding?.recyclerView
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar()
        super.onViewCreated(view, savedInstanceState)
        isLoadingInitialData = true
        observeSharedLiveData()
    }

    override fun onPause() {
        super.onPause()
        feedAnalytics.sendPendingAnalytics()
        unregisterBroadcastReceiver()
    }

    override fun onDestroy() {
        presenter.detachView()
        shopFeedTabSharedViewModel.feedTabClearCache.removeObservers(this)
        super.onDestroy()
    }

    override fun shouldShowShopPageFab(): Boolean {
        val firstItem = adapter?.data?.firstOrNull()
        return !isSellerMigrationEnabled(context) && shopId == userSession.shopId &&
            whitelistDomain.authors.isNotEmpty() && firstItem != null &&
            firstItem !is EmptyModel && firstItem !is EmptyFeedShopUiModel
    }

    override fun getShopPageFabConfig(): ShopPageFabConfig? {
        return fabConfig
    }

    private fun observeSharedLiveData() {
        shopFeedTabSharedViewModel.feedTabClearCache.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    clearCache()
                }
            }
        )
    }

    private fun initVar() {
        arguments?.let {
            shopId = it.getString(PARAM_SHOP_ID) ?: ""
            createPostUrl = it.getString(PARAM_CREATE_POST_URL) ?: ""
        }
        getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (hasFeed() &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        if (isSellerMigrationEnabled(context) && shopId == userSession.shopId) {
                            showBottomSheetSellerMigration()
                        } else {
                            hideBottomSheetSellerMigration()
                            FeedScrollListener.onFeedScrolled(recyclerView, adapter.list)
                        }
                    } else {
                        hideBottomSheetSellerMigration()
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        })
    }

    private fun hasFeed(): Boolean {
        return (
            adapter.list != null &&
                adapter.list.isNotEmpty() &&
                adapter.list.size > 1 &&
                adapter.list[0] !is EmptyModel
            )
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return FeedShopFactoryImpl(
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            userSession
        )
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerFeedShopComponent.builder()
            .baseAppComponent(
                (requireContext().applicationContext as BaseMainApplication).baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_CONTENT_REPORT -> {
                    if (data!!.getBooleanExtra(CONTENT_REPORT_RESULT_SUCCESS, false)) {
                        onSuccessReportContent()
                    } else {
                        onErrorReportContent(
                            data.getStringExtra(CONTENT_REPORT_RESULT_ERROR_MSG) ?: ""
                        )
                    }
                }
                OPEN_DETAIL -> {
                    showSnackbar(data!!.getStringExtra("message") ?: "")
                }
                LOGIN_CODE -> {
                    loadInitialData()
                }
                CREATE_POST -> {
                }
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint && isLoadingInitialData) {
            loadInitialData()
        }
        registerBroadcastReceiver()
        reshowFloatingActionButton()
    }

    override fun loadData(page: Int) {
        if (shopId.isNotEmpty() && !isLoading) {
            isLoading = true
            if (isLoadingInitialData) {
                presenter.getFeedFirstPage(
                    shopId,
                    isForceRefresh,
                    whitelistDomain.authors.isEmpty()
                )
            } else {
                presenter.getFeed(shopId)
            }
        }
    }

    override fun onSwipeRefresh() {
        hideFloatingActionButton()
        hideSnackBarRetry()
        presenter.clearCache()
        isLoadingInitialData = true
        presenter.getFeedFirstPage(shopId, true, whitelistDomain.authors.isEmpty())
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun onSuccessGetFeedFirstPage(
        element: List<Visitable<*>>,
        lastCursor: String,
        whitelistDomain: WhitelistDomain
    ) {
        val dataList = ArrayList<Visitable<*>>()
        this.whitelistDomain = whitelistDomain
        isForceRefresh = true
        isLoading = false
        if (element.isNotEmpty()) {
            trackFeedShopImpression(element)
            dataList.addAll(element)
            renderList(dataList, lastCursor.isNotEmpty())
        } else {
            if (isSellerMigrationEnabled(context) && shopId == userSession.shopId) {
                hideBottomSheetSellerMigration()
                dataList.add(EmptyFeedShopSellerMigrationUiModel())
            } else {
                dataList.add(getEmptyResultViewModel())
            }
            renderList(dataList)
        }
        reshowFloatingActionButton()
    }

    private fun trackFeedShopImpression(listFeed: List<Visitable<*>>) {
        for (i in listFeed.indices) {
            val visitable = listFeed[i]
            if (visitable is DynamicPostModel) {
                val trackingPostModel = visitable.trackingPostModel
                if (visitable.postTag.items.isNotEmpty()) {
                    postTagAnalytics.trackViewPostTagFeedShop(
                        visitable.id,
                        visitable.postTag.items,
                        visitable.header.followCta.authorType,
                        trackingPostModel
                    )
                }
            }
        }
    }

    override fun onSuccessGetFeedNotLoginFirstPage(
        element: List<Visitable<*>>,
        lastCursor: String
    ) {
        val dataList = ArrayList<Visitable<*>>()
        isLoading = false
        if (element.isNotEmpty()) {
            dataList.addAll(element)
            renderList(dataList, lastCursor.isNotEmpty())
        } else {
            if (isSellerMigrationEnabled(context)) {
                dataList.add(EmptyFeedShopSellerMigrationUiModel())
            } else {
                dataList.add(getEmptyResultViewModel())
            }
            renderList(dataList)
        }
    }

    override fun onWhitelistClicked(element: WhitelistUiModel) {
        goToCreatePost()
    }

    override fun onEmptyFeedButtonClicked() {
        goToCreatePost()
        shopAnalytics.eventClickCreatePost()
    }

    override fun onGotoPlayStoreClicked() {
        goToSellerApp(::trackGotoSellerApp, ::trackGotoPlayStore)
    }

    override fun onGotoLearnMoreClicked(url: String): Boolean {
        return goToInformationWebview(url)
    }

    override fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String) {
        isLoading = false
        updateCursor(lastCursor)
        trackFeedShopImpression(visitables)
        renderList(visitables, lastCursor.isNotEmpty())
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onSuccessFollowKol() {
    }

    override fun onErrorFollowKol(errorMessage: String) {
    }

    override fun onSuccessDeletePost(rowNumber: Int) {
        adapter.list.removeAt(rowNumber)
        if (adapter.list.isEmpty()) {
            adapter.addElement(getEmptyResultViewModel())
            adapter.notifyItemChanged(0)
        } else {
            adapter.notifyItemRemoved(rowNumber)
        }
        reshowFloatingActionButton()
    }

    override fun onErrorDeletePost(errorMessage: String, id: String, rowNumber: Int) {
        view?.let {
            Toaster.make(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again),
                View.OnClickListener {
                    presenter.deletePost(id, rowNumber)
                }
            )
        }
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction) {
        if (adapter.data.size > rowNumber &&
            adapter.data[rowNumber] != null &&
            adapter.data[rowNumber] is DynamicPostModel
        ) {
            val model = adapter.data[rowNumber] as DynamicPostModel
            val like = model.footer.like
            like.isChecked = !model.footer.like.isChecked
            if (like.isChecked) {
                try {
                    val likeValue = Integer.valueOf(like.fmt) + 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value + 1
            } else {
                try {
                    val likeValue = Integer.valueOf(like.fmt) - 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value - 1
            }
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_LIKE)
        }
    }

    override fun onLikeKolError(message: String) = showError(message)

    fun onLikeKolClicked(
        rowNumber: Int,
        id: Long,
        hasMultipleContent: Boolean,
        activityType: String
    ) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    fun onGoToKolComment(
        rowNumber: Int,
        id: String,
        hasMultipleContent: Boolean,
        activityType: String
    ) {
        RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT,
                mapOf(
                    COMMENT_ARGS_POSITION to rowNumber.toString()
                )
            ),
            id
        ).run { startActivityForResult(this, KOL_COMMENT_CODE) }
    }

    override fun onAvatarClick(
        positionInFeed: Int,
        redirectUrl: String,
        activityId: String,
        activityName: String,
        followCta: FollowCta,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        isCaption: Boolean
    ) {
    }

    override fun onHeaderActionClick(
        positionInFeed: Int,
        id: String,
        type: String,
        isFollow: Boolean,
        postType: String,
        isVideo: Boolean,
        isBottomSheetMenuOnFeed: Boolean,
        isFollowedFromFollowRestrictionBottomSheet: Boolean
    ) {
        if (type == FollowCta.AUTHOR_USER) {
            var userIdInt = 0
            try {
                userIdInt = Integer.valueOf(id)
            } catch (ignored: NumberFormatException) {
            }
            if (isFollow) {
                onUnfollowKolClicked(positionInFeed, userIdInt)
            } else {
                onFollowKolClicked(positionInFeed, userIdInt)
            }
        }
    }

    fun onFollowKolClicked(rowNumber: Int, id: Int) {
    }

    fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
    }

    override fun onMenuClick(
        positionInFeed: Int,
        postId: String,
        reportable: Boolean,
        deletable: Boolean,
        editable: Boolean,
        isFollowed: Boolean,
        id: String,
        authorType: String,
        postType: String,
        mediaType: String,
        caption: String,
        playChannelId: String
    ) {
        context?.let {
            val menus =
                createBottomMenu(
                    it,
                    deletable,
                    reportable,
                    editable,
                    object : PostMenuListener {
                        override fun onDeleteClicked() {
                            createDeleteDialog(positionInFeed, postId)?.show()
                        }

                        override fun onReportClick() {
                            goToContentReport(postId)
                        }

                        override fun onEditClick() {
                            goToEditPost(caption, postId.toString(), id)
                        }
                    }
                )
            menus.show()
        }
    }

    private fun goToEditPost(caption: String, postId: String, authorId: String) {
        val createPostViewModel = CreatePostViewModel()
        createPostViewModel.caption = caption
        createPostViewModel.postId = postId
        createPostViewModel.editAuthorId = authorId

        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
        )
            .putExtra(BundleData.KEY_IS_OPEN_FROM, BundleData.VALUE_IS_OPEN_FROM_SHOP_PAGE)
            .putExtra(KEY_AUTHOR_TYPE, VALUE_AUTHOR_TYPE)
            .putExtra(CreatePostViewModel.TAG, createPostViewModel)

        startActivity(intent)
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(
        positionInFeed: Int,
        id: Long,
        isLiked: Boolean,
        postType: String,
        isFollowed: Boolean,
        type: Boolean,
        shopId: String,
        mediaType: String?,
        playChannelId: String,
        authorType: String
    ) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    fun onUnlikeKolClicked(
        rowNumber: Int,
        id: Long,
        hasMultipleContent: Boolean,
        activityType: String
    ) {
        if (userSession.isLoggedIn) {
            presenter.unlikeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    override fun onCommentClick(
        positionInFeed: Int,
        id: String,
        authorType: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        shopId: String,
        playChannelId: String,
        isVideo: Boolean
    ) {
        onGoToKolComment(positionInFeed, id, false, "")
    }

    override fun onShareClick(
        positionInFeed: Int,
        id: String,
        title: String,
        description: String,
        url: String,
        imageUrl: String,
        typeASGC: Boolean,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        isTopads: Boolean,
        playChannelId: String,
        weblink: String
    ) {
        activity?.let {
            ShareBottomSheets.newInstance(
                object : ShareBottomSheets.OnShareItemClickListener {
                    override fun onShareItemClicked(packageName: String) {
                    }
                },
                "",
                imageUrl,
                url,
                description,
                title
            )
        }.also {
            fragmentManager?.run {
                it?.show(this)
            }
        }
    }

    override fun onStatsClick(
        title: String,
        activityId: String,
        productIds: List<String>,
        likeCount: Int,
        commentCount: Int
    ) {
        // Not Used
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onPostTagItemClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: PostTagItem,
        itemPosition: Int
    ) {
        onGoToLink(redirectUrl)
    }

    override fun onFullScreenCLick(
        feedXCard: FeedXCard,
        positionInFeed: Int,
        redirectUrl: String,
        currentTime: Long,
        shouldTrack: Boolean,
        isFullScreen: Boolean
    ) {
    }

    override fun sendWatchVODTracker(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long
    ) {
    }

    override fun addVODView(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
    }

    override fun onPostTagBubbleClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        adClickUrl: String
    ) {
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {
        for (tracking in trackList) {
            if (isClick) {
                presenter.trackPostClickUrl(tracking.clickURL)
            } else {
                presenter.trackPostClickUrl(tracking.viewURL)
            }
        }
    }

    override fun onTopAdsImpression(
        url: String,
        shopId: String,
        shopName: String,
        imageUrl: String
    ) {
        presenter.doTopAdsTracker(url, shopId, shopName, imageUrl, false)
    }

    override fun onPostTagItemBuyClicked(
        positionInFeed: Int,
        postTagItem: PostTagItem,
        authorType: String
    ) {
        presenter.addPostTagItemToCart(postTagItem)
    }

    override fun onActionPopup() {
    }

    override fun onTitleCtaClick(redirectUrl: String, adapterPosition: Int) {
        onGoToLink(redirectUrl)
    }

    override fun userImagePostImpression(positionInFeed: Int, contentPosition: Int) {
        if (positionInFeed < adapter.dataSize && adapter.data[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostModel
            feedAnalytics.eventImageImpressionPost(
                FeedAnalyticTracker.Screen.FEED_SHOP,
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.mediaUrl,
                trackingPostModel.recomId,
                positionInFeed
            )
        }
    }

    override fun userCarouselImpression(feedXCard: FeedXCard, positionInFeed: Int) {
    }

    override fun userGridPostImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String,
        hasVoucher: Boolean
    ) {
    }

    override fun onImageClick(postId: String, positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        if (adapter.data[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostModel
            feedAnalytics.eventShopPageClickPost(
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.mediaUrl,
                positionInFeed
            )
        }

        /** temporary solution by hardcoding the url to cdp **/
//        val cdpAppLink = redirectLink
        val cdpAppLink = UriUtil.buildUri(
            ApplinkConst.INTERNAL_CONTENT_DETAIL,
            postId
        )
        val finalAppLink = getUpdatedApplinkForContentDetailPage(cdpAppLink)
        onGoToLink(finalAppLink)
    }

    /**
     * Here we are appending the source of opening content detail page to the original applink
     * source = shop_page here
     */
    private fun getUpdatedApplinkForContentDetailPage(originalRedirectlink: String) =
        Uri.parse(originalRedirectlink)
            .buildUpon()
            .appendQueryParameter(PARAM_ENTRY_POINT, SHOP_PAGE)
            .build().toString()

    override fun onMediaGridClick(
        positionInFeed: Int,
        contentPosition: Int,
        redirectLink: String,
        isSingleItem: Boolean
    ) {
        /**
         * will be revamped in the future
         */
    }

    override fun onShopItemClicked(
        positionInFeed: Int,
        adapterPosition: Int,
        shop: com.tokopedia.topads.sdk.domain.model.Shop
    ) {
        if (adapter.list[positionInFeed] is TopadsShopUiModel) {
            val (_, dataList, _, _) = adapter.list[positionInFeed] as TopadsShopUiModel
            if (adapterPosition != RecyclerView.NO_POSITION) {
                presenter.doTopAdsTracker(
                    dataList[adapterPosition].shopClickUrl,
                    shop.id,
                    shop.name,
                    dataList[adapterPosition].shop.imageShop.xsEcs,
                    true
                )
            }
        }
    }

    override fun onAddFavorite(
        positionInFeed: Int,
        adapterPosition: Int,
        data: com.tokopedia.topads.sdk.domain.model.Data
    ) {
    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onYoutubeThumbnailClick(
        positionInFeed: Int,
        contentPosition: Int,
        youtubeId: String
    ) {
        /**
         * will be revamped in the future
         */
    }

    override fun onPollOptionClick(
        positionInFeed: Int,
        contentPosition: Int,
        option: Int,
        pollId: String,
        optionId: String,
        isVoted: Boolean,
        redirectLink: String
    ) {
        if (isVoted) {
            onGoToLink(redirectLink)
        }
    }

    override fun onGridItemClick(
        positionInFeed: Int,
        productPosition: String,
        productId: String,
        redirectLink: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        hasVoucher: Boolean,
        feedXProducts: List<FeedXProduct>,
        index: Int
    ) {
        onGoToLink(redirectLink)
    }

    override fun userProductImpression(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        shopId: String,
        isFollowed: Boolean,
        productList: List<FeedXProduct>
    ) {
    }

    override fun onVideoPlayerClicked(
        positionInFeed: Int,
        contentPosition: Int,
        postId: String,
        redirectUrl: String,
        authorId: String,
        authorType: String,
        isFollowed: Boolean,
        startTime: Long
    ) {
        if (adapter.data[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostModel
            feedAnalytics.eventShopPageClickPost(
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.mediaUrl,
                positionInFeed
            )
        }

        /** temporary solution by hardcoding the url to cdp **/
//        val cdpAppLink = redirectLink
        val cdpAppLink = UriUtil.buildUri(
            ApplinkConst.INTERNAL_CONTENT_DETAIL,
            postId
        )
        val finalAppLink = getUpdatedApplinkForContentDetailPage(cdpAppLink)
        onGoToLink(finalAppLink)
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
    }

    override fun onAddToCartSuccess(productId: String) {
        createAffiliateCookieAtcDirectPurchase(productId)
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
    }

    private fun createAffiliateCookieAtcDirectPurchase(productId: String) {
        (activity as? ShopPageSharedListener)?.createAffiliateCookieAtcProduct(
            productId,
            false,
            0
        )
    }

    override fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    override fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel) {
    }

    override fun onReadMoreClicked(trackingPostModel: TrackingPostModel) {
        feedAnalytics.eventShopPageClickReadMore(
            trackingPostModel.postId.toString(),
            trackingPostModel.activityName,
            trackingPostModel.mediaType
        )
    }

    override fun onReadMoreClicked(card: FeedXCard, positionInFeed: Int) {
    }

    override fun onImageClicked(card: FeedXCard) {
    }

    override fun onTagClicked(
        card: FeedXCard,
        products: List<FeedXProduct>,
        listener: DynamicPostViewHolder.DynamicPostListener,
        mediaType: String,
        positionInFeed: Int
    ) {
    }

    override fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int) {
    }

    override fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int) {
    }

    override fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int) {
    }

    override fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int) {
    }

    override fun muteUnmuteVideo(
        card: FeedXCard,
        mute: Boolean,
        positionInFeed: Int,
        mediaType: String
    ) {
    }

    override fun onImpressionTracking(feedXCard: FeedXCard, positionInFeed: Int) {
    }

    override fun onHashtagClickedFeed(hashtagText: String, feedXCard: FeedXCard) {
    }

    private fun showBottomSheetSellerMigration() {
        shopFeedTabSharedViewModel.showSellerMigrationBottomSheet()
    }

    private fun hideBottomSheetSellerMigration() {
        shopFeedTabSharedViewModel.hideSellerMigrationBottomSheet()
    }

    private fun hideFloatingActionButton() {
        shopFeedTabSharedViewModel.hideShopPageFab()
    }

    private fun showFloatingActionButton() {
        shopFeedTabSharedViewModel.showShopPageFab()
    }

    fun updateShopInfo(shopInfo: ShopInfo) {
        shopId = shopInfo.shopCore.shopID
        loadInitialData()
    }

    private fun goToCreatePost() {
        val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2)
            .putExtra(
                BundleData.APPLINK_AFTER_CAMERA_CAPTURE,
                ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
            )
            .putExtra(
                BundleData.MAX_MULTI_SELECT_ALLOWED,
                BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED
            )
            .putExtra(BundleData.TITLE, BundleData.VALUE_POST_SEBAGAI)
            .putExtra(
                BundleData.APPLINK_FOR_GALLERY_PROCEED,
                ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2
            )
            .putExtra(BundleData.KEY_IS_OPEN_FROM, BundleData.VALUE_IS_OPEN_FROM_SHOP_PAGE)

        startActivityForResult(intent, CREATE_POST)

        TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
    }

    private fun onGoToLink(url: String) {
        val updatedUrl = checkShouldAppendPdpAppLinkAffiliate(url)
        if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, updatedUrl)
        } else {
            RouteManager.route(
                activity,
                String.format(Locale.ENGLISH, WEBVIEW_URL_FORMAT, ApplinkConst.WEBVIEW, updatedUrl)
            )
        }
    }

    private fun checkShouldAppendPdpAppLinkAffiliate(url: String): String {
        val uri = Uri.parse(url)
        return if (uri.scheme == ApplinkConst.APPLINK_CUSTOMER_SCHEME) {
            when (uri.host) {
                PDP_APP_LINK_HOST -> {
                    createAffiliateLink(url)
                }
                else -> {
                    url
                }
            }
        } else {
            url
        }
    }

    private fun createAffiliateLink(basePdpAppLink: String): String {
        return (activity as? ShopPageSharedListener)?.createPdpAffiliateLink(basePdpAppLink)
            .orEmpty()
    }

    private fun createDeleteDialog(rowNumber: Int, id: String): DialogUnify? {
        return context?.let {
            val dialog =
                DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.feed_shop_delete_post))
            dialog.setDescription(getString(R.string.feed_shop_delete_post_desc))
            dialog.setPrimaryCTAText(getString(R.string.feed_shop_title_delete))
            dialog.setSecondaryCTAText(getString(R.string.feed_shop_title_cancel))
            dialog.setPrimaryCTAClickListener {
                presenter.deletePost(id, rowNumber)
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener { dialog.dismiss() }
            dialog
        }
    }

    private fun goToContentReport(contentId: String) {
        if (context != null) {
            if (userSession.isLoggedIn) {
                val intent = RouteManager.getIntent(
                    requireContext(),
                    ApplinkConstInternalContent.CONTENT_REPORT,
                    contentId
                )
                startActivityForResult(intent, OPEN_CONTENT_REPORT)
            } else {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        activity?.let {
            startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), LOGIN_CODE)
        }
    }

    private fun getEmptyResultViewModel(): EmptyFeedShopUiModel {
        return EmptyFeedShopUiModel()
    }

    private fun onSuccessReportContent() {
        view?.let {
            Toaster.make(
                it,
                getString(com.tokopedia.content.common.R.string.feed_content_reported),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(universalSharingR.string.label_close),
                View.OnClickListener { }
            )
        }
    }

    private fun onErrorReportContent(errorMessage: String) {
        view?.let {
            Toaster.make(
                it,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(universalSharingR.string.label_close),
                View.OnClickListener { }
            )
        }
    }

    private fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }

    private fun doShare(link: String, formatString: String, shareTitle: String) {
        val shareBody = String.format(formatString, link)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = TEXT_PLAIN
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(
            Intent.createChooser(sharingIntent, shareTitle)
        )
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    private fun showError(errorMessage: String, listener: View.OnClickListener?) {
        listener?.let {
            Toaster.make(
                requireView(),
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again),
                it
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getSharedViewModel(): Any {
        return ((activity as HasSharedViewModel).getSharedViewModel())
    }

    fun clearCache() {
        if (::presenter.isInitialized) {
            presenter.clearCache()
        }
    }

    private fun trackGotoSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(
            userSession.userId.orEmpty(),
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT
        )
    }

    private fun trackGotoPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(
            userSession.userId.orEmpty(),
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT
        )
    }

    override fun onTopAdsViewImpression(bannerId: String, imageUrl: String) {
    }

    private val submitPostReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) {
                    return
                }

                if (intent.action == BROADCAST_SUBMIT_POST &&
                    intent.extras?.getBoolean(SUBMIT_POST_SUCCESS) == true
                ) {
                    onSwipeRefresh()
                }
            }
        }
    }

    private fun registerBroadcastReceiver() {
        context?.applicationContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_SUBMIT_POST)

            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(submitPostReceiver, intentFilter)
        }
    }

    private fun unregisterBroadcastReceiver() {
        context?.applicationContext?.let {
            LocalBroadcastManager
                .getInstance(it)
                .unregisterReceiver(submitPostReceiver)
        }
    }

    private fun reshowFloatingActionButton() {
        if (shouldShowShopPageFab()) {
            setupShopPageFab()
            showFloatingActionButton()
        } else {
            hideFloatingActionButton()
        }
    }

    private fun setupShopPageFab() {
        shopFeedTabSharedViewModel.setupShopPageFab(fabConfig)
    }

    override fun onFollowClickAds(positionInFeed: Int, shopId: String, adId: String) {
    }

    override fun onClickSekSekarang(
        postId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        hasVoucher: Boolean,
        positionInFeed: Int,
        feedXCard: FeedXCard
    ) {
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        if (!isAdded) return
        super.startActivityForResult(intent, requestCode)
    }

    override fun startActivity(intent: Intent?) {
        if (!isAdded) return
        super.startActivity(intent)
    }
}
