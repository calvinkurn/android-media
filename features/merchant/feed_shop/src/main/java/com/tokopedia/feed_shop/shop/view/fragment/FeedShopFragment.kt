package com.tokopedia.feed_shop.shop.view.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feed_shop.R
import com.tokopedia.feed_shop.analytics.ShopAnalytics
import com.tokopedia.feed_shop.databinding.FragmentFeedShopBinding
import com.tokopedia.feed_shop.di.DaggerFeedShopComponent
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.util.PostMenuListener
import com.tokopedia.kolcommon.util.createBottomMenu
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.util.goToInformationWebview
import com.tokopedia.seller_migration_common.presentation.util.goToSellerApp
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.feed_shop.shop.domain.WhitelistDomain
import com.tokopedia.feed_shop.shop.view.InterfaceShopPageFab
import com.tokopedia.feed_shop.shop.view.adapter.factory.FeedShopFactoryImpl
import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopUiModel
import com.tokopedia.feed_shop.shop.view.model.WhitelistUiModel
import com.tokopedia.shop.common.view.interfaces.HasSharedViewModel
import com.tokopedia.shop.common.view.interfaces.ISharedViewModel
import com.tokopedia.shop.common.view.model.ShopPageFabConfig
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.universal_sharing.R as universalSharingR

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        KolPostViewHolderListener, KolPostLikeListener,
        DynamicPostViewHolder.DynamicPostListener,
        BannerAdapter.BannerItemListener,
        TopadsShopViewHolder.TopadsShopListener,
        RecommendationCardAdapter.RecommendationCardListener,
        CardTitleView.CardTitleListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener,
        FeedMultipleImageView.FeedMultipleImageViewListener,
        HighlightAdapter.HighlightListener,
        FeedShopContract.View, TopAdsBannerViewHolder.TopAdsBannerListener,
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
                goToCreatePost(getSellerApplink())
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        shopFeedTabSharedViewModel.feedTabClearCache.observe(viewLifecycleOwner, Observer {
            if (it) {
                clearCache()
            }
        })
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
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
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
        return (adapter.list != null
                && adapter.list.isNotEmpty()
                && adapter.list.size > 1
                && adapter.list[0] !is EmptyModel)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return FeedShopFactoryImpl(this,
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
                this,
                this,
                userSession)
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
        presenter.getFeedFirstPage(shopId, true,whitelistDomain.authors.isEmpty())
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String, whitelistDomain: WhitelistDomain) {
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
            if (visitable is DynamicPostViewModel) {
                val trackingPostModel = visitable.trackingPostModel
                if (visitable.postTag.items.isNotEmpty()) {
                    postTagAnalytics.trackViewPostTagFeedShop(
                            visitable.id,
                            visitable.postTag.items,
                            visitable.header.followCta.authorType,
                            trackingPostModel)
                }
            }
        }
    }

    override fun onSuccessGetFeedNotLoginFirstPage(element: List<Visitable<*>>, lastCursor: String) {
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

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
        view?.let {
            Toaster.make(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                presenter.deletePost(id, rowNumber)
            })
        }
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction) {
        if (adapter.data.size > rowNumber
                && adapter.data[rowNumber] != null
                && adapter.data[rowNumber] is DynamicPostViewModel) {
            val model = adapter.data[rowNumber] as DynamicPostViewModel
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

    override fun onGoToKolProfile(rowNumber: Int, userId: String, postId: Int) {
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String) {
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String) {
        onGoToLink(url ?: "")
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {
    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {
    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.unlikeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
    }

    override fun onGoToKolComment(
        rowNumber: Int,
        id: Int,
        hasMultipleContent: Boolean,
        activityType: String,
    ) {
        RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT,
                mapOf(
                    COMMENT_ARGS_POSITION to rowNumber.toString()
                )
            ),
            id.toString()
        ).run { startActivityForResult(this, KOL_COMMENT_CODE) }
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String, activityType: String) {
    }

    override fun onAvatarClick(
        positionInFeed: Int,
        redirectUrl: String,
        activityId: Int,
        activityName: String,
        followCta: FollowCta,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        isCaption: Boolean
    ) {}


    override fun onHeaderActionClick(
        positionInFeed: Int,
        id: String,
        type: String,
        isFollow: Boolean,
        postType: String,
        isVideo: Boolean,
        isBottomSheetMenuOnFeed: Boolean
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

    override fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean, isFollowed: Boolean, id: String, authorType: String, postType: String, mediaType: String, caption: String, playChannelId: String) {
        context?.let {
            val menus =
                    createBottomMenu(it, deletable, reportable, editable, object : PostMenuListener {
                        override fun onDeleteClicked() {
                            createDeleteDialog(positionInFeed, postId)?.show()
                        }

                        override fun onReportClick() {
                            goToContentReport(postId)
                        }

                        override fun onEditClick() {
                            goToEditPostShop(postId)
                        }
                    })
            menus.show()
        }
    }

    private fun goToEditPostShop(postId: Int) {
        context?.let { RouteManager.route(it, ApplinkConstInternalContent.SHOP_POST_EDIT, postId.toString()) }
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean, postType: String, isFollowed: Boolean, type: Boolean, shopId: String, mediaType: String?, playChannelId: String) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int, authorType: String, type: String, isFollowed: Boolean, mediaType: String, shopId: String, playChannelId: String, isVideo: Boolean) {
        onGoToKolComment(positionInFeed, id, false, "")
    }

    override fun onShareClick(
            positionInFeed: Int,
            id: Int,
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
            playChannelId: String
    ) {
        activity?.let {
            ShareBottomSheets.newInstance(object : ShareBottomSheets.OnShareItemClickListener {
                override fun onShareItemClicked(packageName: String) {

                }
            }, "", imageUrl, url, description, title)
        }.also {
            fragmentManager?.run {
                it?.show(this)
            }
        }
    }

    override fun onStatsClick(title: String, activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int) {
        //Not Used
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int) {
        onGoToLink(redirectUrl)
    }

    override fun onPostTagItemBSClick(positionInFeed: Int, redirectUrl: String, postTagItem: FeedXProduct, itemPosition: Int, mediaType: String) {
    }

    override fun onFullScreenCLick(feedXCard: FeedXCard,positionInFeed: Int, redirectUrl: String, currentTime: Long, shouldTrack: Boolean, isFullScreen: Boolean) {

    }
    override fun addVODView(feedXCard: FeedXCard,playChannelId: String, rowNumber: Int, time:Long, hitTrackerApi: Boolean) {

    }

    override fun onPostTagBubbleClick(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            adClickUrl: String
    ) {
    }

    override fun onPostTagItemBSImpression(activityId: String, postTagItemList: List<FeedXProduct>, type: String, shopId: String, isFollowed: Boolean, mediaType: String) {
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean) {
        for (tracking in trackList) {
            if (isClick) {
                presenter.trackPostClickUrl(tracking.clickURL)
            } else {
                presenter.trackPostClickUrl(tracking.viewURL)
            }
        }
    }

    override fun onTopAdsImpression(url: String, shopId: String, shopName: String, imageUrl: String) {
        presenter.doTopAdsTracker(url, shopId, shopName, imageUrl, false)
    }

    override fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel) {

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
        if (positionInFeed < adapter.dataSize && adapter.data[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostViewModel
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

    override fun userCarouselImpression(
        activityId: String,
        media: FeedXMedia,
        positionInFeed: Int,
        postType: String,
        isFollowed: Boolean,
        shopId: String,
        postPosition: Int,
        cpmData: CpmData,
        products: List<Product>
    ) {
    }

    override fun userGridPostImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String
    ) {
    }

    override fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
        if (adapter.data[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostViewModel
            feedAnalytics.eventShopPageClickPost(
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.mediaUrl,
                    positionInFeed)
        }
    }

    override fun onMediaGridClick(positionInFeed: Int, contentPosition: Int,
                                  redirectLink: String, isSingleItem: Boolean) {
        val model = adapter.data[positionInFeed] as? DynamicPostViewModel
        if (!isSingleItem && model != null) {
            RouteManager.route(
                    requireContext(),
                    UriUtil.buildUriAppendParam(
                            ApplinkConstInternalContent.MEDIA_PREVIEW,
                            mapOf(
                                    MEDIA_PREVIEW_INDEX to contentPosition.toString()
                            )
                    ),
                    model.id.toString()
            )
        }
        if (adapter.data[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostViewModel
            feedAnalytics.eventShopPageClickPost(
                    trackingPostModel.postId.toString(),
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.mediaUrl,
                    positionInFeed)
        }
    }

    override fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: com.tokopedia.topads.sdk.domain.model.Shop) {
        if (adapter.list[positionInFeed] is TopadsShopUiModel) {
            val (_, dataList, _, _) = adapter.list[positionInFeed] as TopadsShopUiModel
            if (adapterPosition != RecyclerView.NO_POSITION) {
                presenter.doTopAdsTracker(dataList[adapterPosition].shopClickUrl, shop.id, shop.name, dataList[adapterPosition].shop.imageShop.xsEcs, true)
            }
        }
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: com.tokopedia.topads.sdk.domain.model.Data) {
    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String, postType: String, authorId: String) {
        onGoToLink(redirectLink)
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean) {
    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(
                    requireContext(),
                    redirectUrl
            )
        }
    }

    override fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String) {
        if (isVoted) {
            onGoToLink(redirectLink)
        }
    }

    override fun onGridItemClick(positionInFeed: Int, contentPosition: Int, productPosition: String, redirectLink: String, type: String, isFollowed: Boolean, shopId: String, feedXProducts: List<FeedXProduct>, index: Int) {
        onGoToLink(redirectLink)
    }

    override fun userProductImpression(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        shopId: String,
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
        onGoToLink(redirectUrl)
        if (adapter.data[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.data[positionInFeed] as DynamicPostViewModel
            feedAnalytics.eventShopPageClickPost(
                    trackingPostModel.postId.toString(),
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.mediaUrl,
                    positionInFeed
            )
        }
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
    }

    override fun onAddToCartSuccess() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
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

    override fun onReadMoreClicked(
            postId: String,
            shopId: String,
            type: String,
            isFollowed: Boolean,
            mediaType: String
    ) {
    }

    override fun onImageClicked(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
    }

    override fun onTagClicked(postId: Int, products: List<FeedXProduct>, listener: DynamicPostViewHolder.DynamicPostListener, id: String, type: String, isFollowed: Boolean, mediaType: String, positionInFeed: Int, playChannelId: String, shopName: String) {
    }

    override fun onBottomSheetMenuClicked(
        item: ProductPostTagViewModelNew,
        context: Context,
        shopId: String
    ) {
    }

    override fun muteUnmuteVideo(postId: String, mute: Boolean, id: String, isFollowed: Boolean, isVOD: Boolean, mediaType: String) {
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

    private fun getSellerApplink(): String {
        var applink = ApplinkConst.CONTENT_CREATE_POST
        if (whitelistDomain.authors.size != 0) {
            for (author in whitelistDomain.authors) {
                if (author.type.equals(Author.TYPE_SHOP)) {
                    applink = author.link
                }
            }
        }
        return applink
    }

    fun updateShopInfo(shopInfo: ShopInfo) {
        shopId = shopInfo.shopCore.shopID
        loadInitialData()
    }

    private fun goToCreatePost() {
        goToCreatePost(getSellerApplink())
    }

    private fun goToCreatePost(link: String) {
        startActivityForResult(
                RouteManager.getIntent(
                        requireContext(),
                        link
                ),
                CREATE_POST
        )
    }

    private fun onGoToLink(url: String) {
        if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, url)
        } else {
            RouteManager.route(
                    activity,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
            )
        }
    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): DialogUnify? {
        return context?.let{
            val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(com.tokopedia.kolcommon.R.string.kol_delete_post))
            dialog.setDescription(getString(com.tokopedia.kolcommon.R.string.kol_delete_post_desc))
            dialog.setPrimaryCTAText(getString(com.tokopedia.kolcommon.R.string.kol_title_delete))
            dialog.setSecondaryCTAText(getString(com.tokopedia.kolcommon.R.string.kol_title_cancel))
            dialog.setPrimaryCTAClickListener {
                presenter.deletePost(id, rowNumber)
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener { dialog.dismiss() }
            dialog
        }
    }

    private fun goToContentReport(contentId: Int) {
        if (context != null) {
            if (userSession.isLoggedIn) {
                val intent = RouteManager.getIntent(
                        requireContext(),
                        ApplinkConstInternalContent.CONTENT_REPORT,
                        contentId.toString()
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
            Toaster.make(it, getString(com.tokopedia.feedcomponent.R.string.feed_content_reported),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL,
                    getString(universalSharingR.string.label_close), View.OnClickListener { })
        }
    }

    private fun onErrorReportContent(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(universalSharingR.string.label_close), View.OnClickListener { })
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
            Toaster.make(requireView(), errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.title_try_again), it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getSharedViewModel(): Any {
        return ((activity as HasSharedViewModel).getSharedViewModel())
    }

    fun clearCache() {
        if(::presenter.isInitialized)
            presenter.clearCache()
    }

    private fun trackGotoSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(userSession.userId.orEmpty(), SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

    private fun trackGotoPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(userSession.userId.orEmpty(), SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

    override fun onTopAdsViewImpression(bannerId: String, imageUrl: String) {

    }

    private val submitPostReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) {
                    return
                }

                if (intent.action == BROADCAST_SUBMIT_POST
                        && intent.extras?.getBoolean(SUBMIT_POST_SUCCESS) == true) {
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

    override fun onClickSekSekarang(postId: String, shopId: String, type: String, isFollowed: Boolean, positionInFeed: Int, feedXCard: FeedXCard) {
    }
}