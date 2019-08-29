package com.tokopedia.feedplus.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.FeedModuleRouter
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.activity.TransparentVideoActivity
import com.tokopedia.feedplus.view.adapter.EntryPointAdapter
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.di.FeedPlusComponent
import com.tokopedia.feedplus.view.listener.FeedPlus
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager
import com.tokopedia.feedplus.view.viewmodel.RetryModel
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.common.util.PostMenuListener
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.video.view.activity.MediaPreviewActivity
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.kolcommon.data.pojo.Author
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel

import java.util.ArrayList

import javax.inject.Inject

import com.tokopedia.affiliatecommon.DISCOVERY_BY_ME
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedplus.FeedPlusConstant.KEY_FEED
import com.tokopedia.feedplus.FeedPlusConstant.KEY_FEED_FIRSTPAGE_LAST_CURSOR
import com.tokopedia.kol.common.util.createBottomMenu
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.IS_LIKE_TRUE
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_IS_LIKED
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_COMMENTS
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_LIKES
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusFragment : BaseDaggerFragment(),
        FeedPlus.View,
        FeedPlus.View.Kol,
        FeedPlus.View.Polling,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener,
        KolPostListener.View.ViewHolder,
        BannerAdapter.BannerItemListener,
        RecommendationCardAdapter.RecommendationCardListener,
        TopadsShopViewHolder.TopadsShopListener,
        CardTitleView.CardTitleListener,
        DynamicPostViewHolder.DynamicPostListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener,
        FeedMultipleImageView.FeedMultipleImageViewListener,
        HighlightAdapter.HighlightListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var mainContent: View
    private lateinit var newFeed: View
    private lateinit var feedModuleRouter: FeedModuleRouter
    private lateinit var newFeedReceiver: BroadcastReceiver

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: FeedPlusAdapter
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var infoBottomSheet: TopAdsInfoBottomSheet
    private lateinit var createPostBottomSheet: CloseableBottomSheetDialog
    private var loginIdInt: Int = 0
    private var isLoadedOnce: Boolean = false
    private var afterPost: Boolean = false

    @Inject
    @get:RestrictTo(RestrictTo.Scope.TESTS)
    @set:RestrictTo(RestrictTo.Scope.TESTS)
    lateinit var presenter: FeedPlusPresenter

    @Inject
    internal lateinit var analytics: FeedAnalytics

    @Inject
    internal lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    internal lateinit var userSession: UserSessionInterface

    val isMainViewVisible: Boolean
        get() = userVisibleHint

    private val userIdInt: Int
        get() {
            try {
                return Integer.valueOf(getUserSession().userId)
            } catch (ignored: NumberFormatException) {
                return 0
            }

        }

    override fun getScreenName(): String {
        return FeedTrackingEventLabel.SCREEN_UNIFY_HOME_FEED
    }

    override fun initInjector() {
        if (activity != null && activity!!.application != null) {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(activity!!
                            .application))
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activity != null) GraphqlClient.init(activity!!)
        performanceMonitoring = PerformanceMonitoring.start(FEED_TRACE)
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initVar() {
        val typeFactory = FeedPlusTypeFactoryImpl(this, analytics, userSession)
        adapter = FeedPlusAdapter(typeFactory)
        adapter.setOnLoadListener { totalCount ->
            val size = adapter.getlist().size
            val lastIndex = size - 1
            if (adapter.getlist()[0] !is EmptyModel && adapter.getlist()[lastIndex] !is RetryModel)
                presenter.fetchNextPage()
        }
        layoutManager = NpaLinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)

        if (activity!!.application is FeedModuleRouter) {
            feedModuleRouter = activity!!.application as FeedModuleRouter
        } else {
            throw IllegalStateException("Application must implement " + FeedModuleRouter::class.java.simpleName)
        }

        val loginIdString = getUserSession().userId
        loginIdInt = if (TextUtils.isEmpty(loginIdString)) 0 else Integer.valueOf(loginIdString)

        newFeedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent != null && intent.action != null && intent.action == BROADCAST_FEED) {
                    val isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false)
                    if (isHaveNewFeed) {
                        onShowNewFeed("")
                    }
                }
            }
        }
        registerNewFeedReceiver()

        if (arguments != null) {
            afterPost = TextUtils.equals(arguments!!.getString(AFTER_POST, ""), TRUE)
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun reInitInjector(component: FeedPlusComponent) {
        component.inject(this)
        presenter.attachView(this)
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun resetToFirstTime() {
        isLoadedOnce = false
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        retainInstance = true
        val parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false)
        recyclerView = parentView.findViewById(R.id.recycler_view)
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_layout)
        mainContent = parentView.findViewById(R.id.main)
        newFeed = parentView.findViewById(R.id.layout_new_feed)

        prepareView()
        presenter.attachView(this)
        return parentView

    }


    private fun prepareView() {
        adapter.itemTreshold = 2
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        swipeToRefresh.setOnRefreshListener(this)
        infoBottomSheet = TopAdsInfoBottomSheet.newInstance(activity)
        newFeed.setOnClickListener { v ->
            scrollToTop()
            showRefresh()
            onRefresh()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position = 0
                        val item: Visitable<*>
                        if (itemIsFullScreen()) {
                            position = layoutManager.findLastVisibleItemPosition()
                        } else if (layoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findFirstCompletelyVisibleItemPosition()
                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findLastCompletelyVisibleItemPosition()
                        }

                        item = adapter.getlist()[position]

                        if (item is DynamicPostViewModel) {
                            if (!TextUtils.isEmpty(item.footer.buttonCta.appLink)) {
                                adapter.notifyItemChanged(position, DynamicPostViewHolder.PAYLOAD_ANIMATE_FOOTER)
                            }
                        }
                        FeedScrollListener.onFeedScrolled(recyclerView, adapter.getlist())
                    }
                } catch (e: IndexOutOfBoundsException) {
                }

            }

        })
    }

    private fun itemIsFullScreen(): Boolean {
        return layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() == 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRefresh() {
        newFeed.visibility = View.GONE
        presenter.refreshPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()

//        if (layoutManager != null)
//            layoutManager = null
    }

    override fun setLastCursorOnFirstPage(lastCursor: String) {
        if (activity != null && activity!!.applicationContext != null) {
            val cache = LocalCacheHandler(
                    activity!!.applicationContext,
                    KEY_FEED
            )
            cache.putString(KEY_FEED_FIRSTPAGE_LAST_CURSOR, lastCursor)
            cache.applyEditor()
        }
    }

    override fun onOpenVideo(videoUrl: String, subtitle: String) {
        val intent = TransparentVideoActivity.getIntent(activity, videoUrl, subtitle)
        startActivity(intent)
    }

    override fun onInfoClicked() {
        infoBottomSheet.show()
    }

    @SuppressLint("Range")
    override fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }

    override fun updateFavorite(adapterPosition: Int) {

    }

    override fun updateFavoriteFromEmpty(shopId: String) {
        onRefresh()
        analytics.eventFeedClickShop(screenName,
                shopId, FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE)

    }

    override fun onSuccessGetFeedFirstPage(listFeed: ArrayList<Visitable<*>>, whitelistViewModel: WhitelistViewModel?) {
        trackFeedImpression(listFeed)

        adapter.setList(listFeed)
        adapter.notifyDataSetChanged()
        triggerClearNewFeedNotification()
    }

    override fun onShowEmpty() {
        adapter.unsetEndlessScrollListener()
        adapter.showEmpty()
        adapter.notifyDataSetChanged()
    }

    override fun clearData() {
        adapter.clearData()
    }

    override fun setEndlessScroll() {
        adapter.setEndlessScrollListener()
    }

    override fun unsetEndlessScroll() {
        adapter.unsetEndlessScrollListener()
    }

    override fun onShowNewFeed(totalData: String) {
        newFeed.visibility = View.VISIBLE
    }

    override fun onHideNewFeed() {
        newFeed.visibility = View.GONE
    }

    override fun finishLoading() {
        swipeToRefresh.isRefreshing = false
    }

    override fun showInterestPick() {
        if (context != null && feedModuleRouter.isEnableInterestPick) {
            RouteManager.route(context, ApplinkConst.INTEREST_PICK)
        }
    }

    override fun onErrorGetFeedFirstPage(errorMessage: String) {
        finishLoading()
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(activity, mainContent, errorMessage
            ) { presenter.refreshPage() }
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }

    }

    override fun onSearchShopButtonClicked() {
        if (context != null) {
            val INIT_STATE_FRAGMENT_FAVORITE = 2
            val EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT"
            val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
            intent.putExtra(EXTRA_INIT_FRAGMENT, INIT_STATE_FRAGMENT_FAVORITE)
            startActivity(intent)
        }
    }

    override fun showRefresh() {
        if (!swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = true
        }
    }

    override fun updateCursor(currentCursor: String) {
        presenter.setCursor(currentCursor)
    }


    override fun onSuccessGetFeed(listFeed: ArrayList<Visitable<*>>) {
        trackFeedImpression(listFeed)

        adapter.removeEmpty()
        val posStart = adapter.itemCount
        adapter.addList(listFeed)
        adapter.notifyItemRangeInserted(posStart, listFeed.size)
    }

    override fun onShowRetryGetFeed() {
        adapter.showRetry()
    }

    override fun hideAdapterLoading() {
        adapter.removeLoading()
    }

    override fun getColor(color: Int): Int {
        return MethodChecker.getColor(activity, color)
    }

    override fun onRetryClicked() {
        adapter.removeRetry()
        adapter.showLoading()
        adapter.setEndlessScrollListener()
        presenter.fetchNextPage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {
            OPEN_DETAIL -> if (resultCode == Activity.RESULT_OK)
                showSnackbar(data.getStringExtra("message"))
            OPEN_KOL_COMMENT -> if (resultCode == Activity.RESULT_OK) {
                val serverErrorMsg = data.getStringExtra(KolCommentFragment.ARGS_SERVER_ERROR_MSG)
                if (!TextUtils.isEmpty(serverErrorMsg)) {
                    ToasterError
                            .make(view, serverErrorMsg, BaseToaster.LENGTH_LONG)
                            .setAction(R.string.cta_refresh_feed) { v -> onRefresh() }.show()
                } else {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, DEFAULT_VALUE),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0)
                    )
                }
            }
            OPEN_KOL_PROFILE -> if (resultCode == Activity.RESULT_OK) {
                onSuccessFollowUnfollowFromProfile(
                        data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                        data.getIntExtra(ProfileActivity.PARAM_IS_FOLLOWING, DEFAULT_VALUE)
                )

                updatePostState(
                        data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                        data.getIntExtra(PARAM_IS_LIKED, DEFAULT_VALUE),
                        data.getIntExtra(PARAM_TOTAL_LIKES, DEFAULT_VALUE),
                        data.getIntExtra(PARAM_TOTAL_COMMENTS, DEFAULT_VALUE)
                )
            }
            CREATE_POST -> {
            }
            OPEN_CONTENT_REPORT -> if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(ContentReportActivity.RESULT_SUCCESS, false)) {
                    onSuccessReportContent()
                } else {
                    onErrorReportContent(
                            data.getStringExtra(ContentReportActivity.RESULT_ERROR_MSG)
                    )
                }
            }
            else -> {
            }
        }
    }

    override fun onProductItemClicked(position: Int, product: Product) {
        goToProductDetail(product.id)

        analytics.eventFeedClickProduct(screenName,
                product.id,
                FeedTrackingEventLabel.Click.TOP_ADS_PRODUCT)

        val listTopAds = ArrayList<FeedEnhancedTracking.Promotion>()

        listTopAds.add(FeedEnhancedTracking.Promotion(
                Integer.valueOf(product.adId),
                FeedEnhancedTracking.Promotion
                        .createContentNameTopadsProduct(),
                if (TextUtils.isEmpty(product.adRefKey))
                    FeedEnhancedTracking.Promotion.TRACKING_NONE
                else
                    product.adRefKey,
                position,
                product.category.toString(),
                Integer.valueOf(product.id),
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY))

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt))
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {
        goToShopPage(shop.id)
        analytics.eventFeedClickShop(screenName,
                shop.id,
                FeedTrackingEventLabel.Click.TOP_ADS_SHOP)

        val listTopAds = ArrayList<FeedEnhancedTracking.Promotion>()

        listTopAds.add(FeedEnhancedTracking.Promotion(
                Integer.valueOf(shop.adId),
                FeedEnhancedTracking.Promotion
                        .createContentNameTopadsShop(),
                shop.adRefKey,
                position,
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                Integer.valueOf(shop.adId),
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY
        ))

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt))
    }

    override fun onAddFavorite(position: Int, dataShop: Data) {
        presenter.favoriteShop(dataShop, position)
        analytics.eventFeedClickShop(screenName,
                dataShop.shop.id,
                FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE)

    }

    fun scrollToTop() {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun triggerClearNewFeedNotification() {
        if (context != null && context!!.applicationContext != null) {
            val intent = Intent(BROADCAST_FEED)
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, true)
            LocalBroadcastManager.getInstance(context!!.applicationContext).sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        registerNewFeedReceiver()
        if (userVisibleHint && presenter != null) {
            loadData(userVisibleHint)
        }
    }

    override fun onPause() {
        super.onPause()
        unRegisterNewFeedReceiver()
    }

    private fun registerNewFeedReceiver() {
        if (activity != null && activity!!.applicationContext != null) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_FEED)

            LocalBroadcastManager
                    .getInstance(activity!!.applicationContext)
                    .registerReceiver(newFeedReceiver, intentFilter)
        }
    }

    private fun unRegisterNewFeedReceiver() {
        if (activity != null && activity!!.applicationContext != null) {
            LocalBroadcastManager
                    .getInstance(activity!!.applicationContext)
                    .unregisterReceiver(newFeedReceiver)
        }
    }

    private fun loadData(isVisibleToUser: Boolean) {
        if (isVisibleToUser && isAdded && activity != null && presenter != null) {
            if (!isLoadedOnce) {
                presenter.fetchFirstPage()
                isLoadedOnce = !isLoadedOnce
            }

            if (afterPost) {
                showAfterPostToaster()
                afterPost = false
            }

            analytics.trackScreen(screenName)
        }
    }

    override fun stopTracePerformanceMon() {
        performanceMonitoring.stopTrace()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadData(isVisibleToUser)
    }

    override fun hasFeed(): Boolean {
        return (adapter.getlist() != null
                && !adapter.getlist().isEmpty()
                && adapter.getlist().size > 1
                && adapter.getlist()[0] !is EmptyModel)
    }

    override fun onGoToKolProfile(rowNumber: Int, userId: String, postId: Int) {
        if (context != null) {
            val profileIntent = ProfileActivity
                    .createIntentFromFeed(context, userId, postId)
                    .putExtra(ARGS_ROW_NUMBER, rowNumber)
            startActivityForResult(profileIntent, OPEN_KOL_PROFILE)
        }
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String) {
        feedModuleRouter.openRedirectUrl(activity, applink)
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String) {
        feedModuleRouter.openRedirectUrl(activity, url)
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {

    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {

    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
        if (getUserSession().isLoggedIn) {
            presenter.followKol(id, rowNumber, this)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
        if (getUserSession().isLoggedIn) {
            presenter.unfollowKol(id, rowNumber, this)
        } else {
            onGoToLogin()
        }

    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        if (getUserSession().isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.LIKE)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                    activityType: String) {
        if (getUserSession().isLoggedIn) {
            presenter.unlikeKol(id, rowNumber, this)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.UNLIKE)
        } else {
            onGoToLogin()
        }
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        val intent = KolCommentActivity.getCallingIntentFromFeed(context, id, rowNumber)
        startActivityForResult(intent, OPEN_KOL_COMMENT)
        trackCardPostElementClick(rowNumber, FeedAnalytics.Element.COMMENT)
    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int) {
        onLikeKolClicked(rowNumber, id, false, "")
    }

    override fun onUnlikeKolClicked(adapterPosition: Int, id: Int) {
        onUnlikeKolClicked(adapterPosition, id, false, "")
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int) {
        onGoToKolComment(rowNumber, id, false, "")
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String,
                               activityType: String) {

    }

    override fun onMenuClicked(rowNumber: Int, element: BaseKolViewModel) {
        if (context != null) {
            val menus = createBottomMenu(context!!, element,
                    object : PostMenuListener {
                        override fun onDeleteClicked() {

                        }

                        override fun onReportClick() {
                            goToContentReport(element.contentId)
                        }

                        override fun onEditClick() {

                        }
                    }

            )
            menus.show()
        }
    }

    override fun onGoToLink(link: String) {
        if (!TextUtils.isEmpty(link)) {
            RouteManager.route(activity, link)
        }
    }

    override fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String) {
        presenter.sendVote(rowNumber, pollId, optionId)
    }

    override fun onErrorFollowKol(errorMessage: String, id: Int, status: Int, rowNumber: Int) {
        ToasterError.make(view, errorMessage, BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_try_again) { v ->
                    if (status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                        presenter.unfollowKol(id, rowNumber, this@FeedPlusFragment)
                    else
                        presenter.followKol(id, rowNumber, this@FeedPlusFragment)
                }
                .show()
    }

    override fun onSuccessFollowUnfollowKol(rowNumber: Int) {
        if (adapter.getlist()[rowNumber] is KolPostViewModel) {
            val kolPostViewModel = adapter.getlist()[rowNumber] as KolPostViewModel
            kolPostViewModel.isFollowed = !kolPostViewModel.isFollowed
            kolPostViewModel.isTemporarilyFollowed = !kolPostViewModel.isTemporarilyFollowed
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW)
        }

        if (adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, header) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            header.followCta.isFollow = !header.followCta.isFollow
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW)
        }
    }

    override fun onErrorLikeDislikeKolPost(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)

    }

    override fun onSuccessLikeDislikeKolPost(rowNumber: Int) {
        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is BaseKolViewModel) {
            val kolViewModel = adapter.getlist()[rowNumber] as BaseKolViewModel
            kolViewModel.isLiked = !kolViewModel.isLiked
            if (kolViewModel.isLiked) {
                kolViewModel.totalLike = (adapter.getlist()[rowNumber] as BaseKolViewModel).totalLike + 1
            } else {
                kolViewModel.totalLike = (adapter.getlist()[rowNumber] as BaseKolViewModel).totalLike - 1
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE)
        }

        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, _, _, footer) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            val like = footer.like
            like.isChecked = !like.isChecked
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

    override fun onFollowKolFromRecommendationClicked(rowNumber: Int, id: Int, position: Int) {
        presenter.followKolFromRecommendation(id, rowNumber, position, this)
    }

    override fun onUnfollowKolFromRecommendationClicked(rowNumber: Int, id: Int, position: Int) {
        presenter.unfollowKolFromRecommendation(id, rowNumber, position, this)
    }

    override fun onSuccessFollowKolFromRecommendation(rowNumber: Int, position: Int, isFollow: Boolean) {
        if (adapter.getlist()[rowNumber] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[rowNumber] as FeedRecommendationViewModel
            cards[position].cta.isFollow = isFollow
            adapter.notifyItemChanged(rowNumber, position)
        }
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size > rowNumber
                && adapter.getlist()[rowNumber] is BaseKolViewModel) {
            val kolViewModel = adapter.getlist()[rowNumber] as BaseKolViewModel
            kolViewModel.totalComment = (adapter.getlist()[rowNumber] as BaseKolViewModel).totalComment + totalNewComment
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_COMMENT)
        }

        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size > rowNumber
                && adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, _, _, footer) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            val comment = footer.comment
            try {
                val commentValue = Integer.valueOf(comment.fmt) + totalNewComment
                comment.fmt = commentValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            comment.value = comment.value + totalNewComment
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_COMMENT)
        }
    }

    private fun onSuccessFollowUnfollowFromProfile(rowNumber: Int, isFollowing: Int) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size > rowNumber
                && adapter.getlist()[rowNumber] is KolPostViewModel) {
            val kolViewModel = adapter.getlist()[rowNumber] as KolPostViewModel

            if (isFollowing != DEFAULT_VALUE) {
                kolViewModel.isFollowed = isFollowing == ProfileActivity.IS_FOLLOWING_TRUE
                kolViewModel.isTemporarilyFollowed = isFollowing == ProfileActivity.IS_FOLLOWING_TRUE
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW)
        }
    }

    private fun updatePostState(rowNumber: Int, isLiked: Int, totalLike: Int, totalComment: Int) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size > rowNumber
                && adapter.getlist()[rowNumber] is BaseKolViewModel) {
            val kolViewModel = adapter.getlist()[rowNumber] as BaseKolViewModel

            if (isLiked != DEFAULT_VALUE) {
                kolViewModel.isLiked = isLiked == IS_LIKE_TRUE
            }

            if (totalLike != DEFAULT_VALUE) {
                kolViewModel.totalLike = totalLike
            }

            if (totalComment != DEFAULT_VALUE) {
                kolViewModel.totalComment = totalComment
            }
            adapter.notifyItemChanged(rowNumber)
        }
    }

    private fun onSuccessReportContent() {
        ToasterNormal
                .make(view,
                        getString(R.string.feed_content_reported),
                        BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { v -> }
                .show()
    }

    private fun onErrorReportContent(errorMsg: String) {
        ToasterError
                .make(view, errorMsg, BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { v -> }
                .show()
    }

    override fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            activity!!.startActivityForResult(intent, REQUEST_LOGIN)
        }
    }

    override fun onSuccessSendVote(rowNumber: Int, optionId: String,
                                   voteStatisticDomainModel: VoteStatisticDomainModel) {
        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, contentList) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            for (basePostViewModel in contentList) {
                if (basePostViewModel is PollContentViewModel) {
                    basePostViewModel.voted = true

                    var totalVoter: Int
                    try {
                        totalVoter = Integer.valueOf(voteStatisticDomainModel.totalParticipants)
                    } catch (ignored: NumberFormatException) {
                        totalVoter = 0
                    }

                    basePostViewModel.totalVoterNumber = totalVoter

                    for (i in 0 until basePostViewModel.optionList.size) {
                        val optionViewModel = basePostViewModel.optionList[i]

                        optionViewModel.selected = if (optionId == optionViewModel.optionId)
                            PollContentOptionViewModel.SELECTED
                        else
                            PollContentOptionViewModel.UNSELECTED

                        var newPercentage = 0
                        try {
                            newPercentage = Integer.valueOf(
                                    voteStatisticDomainModel.listOptions[i].percentage
                            )
                        } catch (ignored: NumberFormatException) {
                        } catch (ignored: IndexOutOfBoundsException) {
                        }

                        optionViewModel.percentage = newPercentage
                    }
                }
            }

            adapter.notifyItemChanged(rowNumber)
        }


    }

    override fun onErrorSendVote(message: String) {
        NetworkErrorHelper.showSnackbar(activity, message)
    }

    override fun onWhitelistClicked(element: WhitelistViewModel) {
        analytics.trackClickCreatePost(userSession.userId)
        showBottomSheetCreatePost(element)
    }

    private fun showBottomSheetCreatePost(element: WhitelistViewModel) {
        if (activity != null) {
            createPostBottomSheet = CloseableBottomSheetDialog.createInstance(context,
                    {

                    }, {

            })
            val customView = createCustomCreatePostBottomSheetView(activity!!.layoutInflater, element)
            createPostBottomSheet.setCustomContentView(customView,
                    getString(R.string.create_post_as), true)
            createPostBottomSheet.show()
        }
    }

    private fun createCustomCreatePostBottomSheetView(layoutInflater: LayoutInflater, element: WhitelistViewModel): View {
        val view = layoutInflater.inflate(R.layout.layout_create_post_bottom_sheet, null)

        if (activity != null) {
            val entryPointRecyclerView = view.findViewById<RecyclerView>(R.id.entry_point_list)
            val adapter = EntryPointAdapter(activity!!,
                    element.whitelist.authors, object: EntryPointAdapter.ActionListener{
                override fun onEntryPointClicked(applink: String) {
                    analytics.trackClickCreatePostAs(applink, userSession.userId,
                            userSession.shopId)
                    startActivityForResult(
                            RouteManager.getIntent(context, applink),
                            CREATE_POST
                    )
                    createPostBottomSheet.dismiss()
                }

            })
            entryPointRecyclerView.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false)
            entryPointRecyclerView.adapter = adapter
        }
        return view
    }

    override fun onSuccessToggleFavoriteShop(rowNumber: Int, adapterPosition: Int) {
        if (adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, header) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            header.followCta.isFollow = !header.followCta.isFollow
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW)
        }

        if (adapter.getlist()[rowNumber] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[rowNumber] as FeedRecommendationViewModel
            cards[adapterPosition].cta.isFollow = !cards[adapterPosition].cta.isFollow
            adapter.notifyItemChanged(rowNumber, adapterPosition)
        }

        if (adapter.getlist()[rowNumber] is TopadsShopViewModel) {
            val (_, dataList) = adapter.getlist()[rowNumber] as TopadsShopViewModel
            dataList[adapterPosition].isFavorit = !dataList[adapterPosition].isFavorit
            adapter.notifyItemChanged(rowNumber, adapterPosition)
        }
    }

    override fun onErrorToggleFavoriteShop(message: String, rowNumber: Int, adapterPosition: Int,
                                           shopId: String) {
        ToasterError.make(view, message, BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_try_again
                ) { v -> presenter.toggleFavoriteShop(rowNumber, adapterPosition, shopId) }
                .show()
    }

    override fun getUserSession(): UserSessionInterface {
        return userSession
    }

    override fun sendMoEngageOpenFeedEvent() {
        feedModuleRouter.sendMoEngageOpenFeedEvent(!hasFeed())
    }

    override fun onStop() {
        super.onStop()
        if (activity != null && activity!!.isFinishing) {
            unRegisterNewFeedReceiver()
        }
    }

    override fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int,
                                   redirectUrl: String) {
        onGoToLink(redirectUrl)

        if (adapter.getlist()[positionInFeed] is BannerViewModel) {
            val (itemViewModels) = adapter.getlist()[positionInFeed] as BannerViewModel
            trackBannerClick(
                    adapterPosition,
                    itemViewModels[adapterPosition].trackingBannerModel
            )
        }
    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int,
                                             redirectLink: String) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[positionInFeed] as FeedRecommendationViewModel
            val (templateType, activityName, _, _, authorName, authorType, authorId, cardPosition) = cards[adapterPosition].trackingRecommendationModel
            analytics.eventRecommendationClick(
                    templateType,
                    activityName,
                    authorName,
                    authorType,
                    authorId,
                    cardPosition,
                    userIdInt
            )
        }
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int,
                                             id: String, type: String,
                                             isFollow: Boolean) {
        if (type == FollowCta.AUTHOR_USER) {
            val userIdInt = id.toIntOrZero()

            if (isFollow) {
                onUnfollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition)
            } else {
                onFollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition)
            }

        } else if (type == FollowCta.AUTHOR_SHOP) {
            presenter.toggleFavoriteShop(positionInFeed, adapterPosition, id)
        }

        if (adapter.getlist()[positionInFeed] is FeedRecommendationViewModel) {

            val (_, cards) = adapter.getlist()[positionInFeed] as FeedRecommendationViewModel
            trackRecommendationFollowClick(
                    cards[adapterPosition].trackingRecommendationModel,
                    if (isFollow) FeedAnalytics.Element.UNFOLLOW else FeedAnalytics.Element.FOLLOW
            )
        }
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: Shop) {
        goToShopPage(shop.id)

        if (adapter.getlist()[positionInFeed] is TopadsShopViewModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopViewModel
            for ((templateType, _, _, _, authorName, _, authorId, cardPosition, adId) in trackingList) {
                if (TextUtils.equals(authorName, shop.name)) {
                    analytics.eventTopadsRecommendationClick(
                            templateType,
                            adId,
                            authorId,
                            cardPosition,
                            userIdInt
                    )
                    break
                }
            }
        }
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: Data) {
        presenter.toggleFavoriteShop(positionInFeed, adapterPosition, data.shop.id)

        if (adapter.getlist()[positionInFeed] is TopadsShopViewModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopViewModel

            for (tracking in trackingList) {
                if (TextUtils.equals(tracking.authorName, data.shop.name)) {
                    trackRecommendationFollowClick(
                            tracking,
                            FeedAnalytics.Element.FOLLOW
                    )
                    break
                }
            }
        }
    }

    override fun onActionPopup() {
        onInfoClicked()
    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onTitleCtaClick(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)

        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.AVATAR)
    }

    override fun onHeaderActionClick(positionInFeed: Int, id: String, type: String,
                                     isFollow: Boolean) {
        if (getUserSession().isLoggedIn) {
            if (type == FollowCta.AUTHOR_USER) {
                val userIdInt = userIdInt

                if (isFollow) {
                    onUnfollowKolClicked(positionInFeed, userIdInt)
                } else {
                    onFollowKolClicked(positionInFeed, userIdInt)
                }

            } else if (type == FollowCta.AUTHOR_SHOP) {
                presenter.toggleFavoriteShop(positionInFeed, id)
            }

            if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
                val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
                analytics.eventFollowCardPost(
                        if (isFollow) FeedAnalytics.Element.UNFOLLOW else FeedAnalytics.Element.FOLLOW,
                        trackingPostModel.activityName,
                        trackingPostModel.postId.toString(),
                        trackingPostModel.mediaType
                )
            }
        } else {
            onGoToLogin()
        }
    }

    override fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean,
                             editable: Boolean) {
        if (context != null) {
            val menus = createBottomMenu(context!!, deletable, reportable, false, object : PostMenuListener {
                override fun onDeleteClicked() {

                }

                override fun onReportClick() {
                    if (getUserSession().isLoggedIn) {
                        goToContentReport(postId)
                    } else {
                        onGoToLogin()
                    }
                }

                override fun onEditClick() {

                }
            })
            menus.show()
        }
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id)
        } else {
            onLikeKolClicked(positionInFeed, id)
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        onGoToKolComment(positionInFeed, id)
    }

    override fun onShareClick(positionInFeed: Int, id: Int, title: String,
                              description: String, url: String,
                              imageUrl: String) {
        if (activity != null) {
            ShareBottomSheets().show(activity!!.supportFragmentManager,
                    ShareBottomSheets.constructShareData("", imageUrl, url, description, title),
                    object : ShareBottomSheets.OnShareItemClickListener {
                        override fun onShareItemClicked(packageName: String) {

                        }
                    })
        }

        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.SHARE)
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.TAG)
    }

    override fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int) {
        onGoToLink(redirectUrl)
        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (id, _, header, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            postTagAnalytics.trackClickPostTagFeed(
                    id,
                    postTagItem,
                    itemPosition,
                    header.followCta.authorType,
                    trackingPostModel
            )
        }
    }

    override fun onImageClick(positionInFeed: Int, contentPosition: Int,
                              redirectLink: String) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onMediaGridClick(positionInFeed: Int, contentPosition: Int, redirectLink: String, isSingleItem: Boolean) {
        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (id, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)

            if (!isSingleItem && activity != null) {
                startActivity(MediaPreviewActivity.createIntent(activity!!, id.toString(), contentPosition))
            }
        }
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean) {
        for (track in trackList) {
            if (isClick) {
                presenter.trackAffiliate(track.clickURL)
            } else {
                presenter.trackAffiliate(track.viewURL)
            }
        }
    }

    override fun onHighlightItemClicked(positionInFeed: Int, redirectUrl: String) {

    }

    override fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem) {
        presenter.addPostTagItemToCart(postTagItem)
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int,
                                         youtubeId: String) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(context, redirectUrl)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int,
                                   pollId: String, optionId: String, isVoted: Boolean,
                                   redirectLink: String) {
        if (isVoted) {
            onGoToLink(redirectLink)
        } else {
            onVoteOptionClicked(positionInFeed, pollId, optionId)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, contentList, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            if (contentList[contentPosition] is PollContentViewModel) {
                val (_, _, _, _, optionList) = contentList[contentPosition] as PollContentViewModel
                val (_, option1, imageUrl) = optionList[option]
                analytics.eventVoteClick(
                        trackingPostModel.activityName,
                        trackingPostModel.mediaType,
                        pollId,
                        optionId,
                        option1,
                        imageUrl,
                        trackingPostModel.postId,
                        userIdInt
                )
            }
        }
    }

    override fun onGridItemClick(positionInFeed: Int, contentPosition: Int, productPosition: Int,
                                 redirectLink: String) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, contentList, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            if (redirectLink.contains(FEED_DETAIL)) {
                analytics.eventGoToFeedDetail(trackingPostModel.postId)
            } else if (contentList[contentPosition] is GridPostViewModel) {
                val (itemList) = contentList[contentPosition] as GridPostViewModel
                val (id, text, price) = itemList[productPosition]
                analytics.eventProductGridClick(
                        ProductEcommerce(id,
                                text,
                                price,
                                productPosition),
                        trackingPostModel.activityName,
                        trackingPostModel.postId,
                        userIdInt
                )
            }
        }
    }

    override fun onVideoPlayerClicked(positionInFeed: Int,
                                      contentPosition: Int,
                                      postId: String) {
        if (activity != null) {
            startActivity(VideoDetailActivity.getInstance(
                    activity!!,
                    postId))
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onAddToCartSuccess() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
    }

    override fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    private fun doShare(body: String, title: String) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body)
        startActivity(
                Intent.createChooser(sharingIntent, title)
        )
    }

    private fun goToContentReport(contentId: Int) {
        if (context != null) {
            val intent = ContentReportActivity.createIntent(
                    context!!,
                    contentId
            )
            startActivityForResult(intent, OPEN_CONTENT_REPORT)
        }
    }

    private fun goToProductDetail(productId: String) {
        if (activity != null) {
            activity!!.startActivity(getProductIntent(productId))
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    private fun goToShopPage(shopId: String) {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP, shopId)
            activity!!.startActivity(intent)
        }
    }

    private fun trackFeedImpression(listFeed: ArrayList<Visitable<*>>) {
        for (i in listFeed.indices) {
            val visitable = listFeed[i]
            val feedPosition = adapter.getlist().size + i
            val userId = userIdInt

            if (visitable is DynamicPostViewModel) {
                val trackingPostModel = visitable.trackingPostModel

                if (visitable.contentList.size > 0) {
                    trackPostContentImpression(
                            visitable,
                            trackingPostModel,
                            userId,
                            feedPosition
                    )
                }

                if (visitable.postTag.items.size != 0) {
                    postTagAnalytics.trackViewPostTagFeed(
                            visitable.id,
                            visitable.postTag.items,
                            visitable.header.followCta.authorType,
                            trackingPostModel)
                }
                onAffiliateTrackClicked(visitable.tracking, false)

            } else if (visitable is BannerViewModel) {
                val (itemViewModels) = visitable
                val trackingBannerModels = ArrayList<TrackingBannerModel>()
                for ((_, _, _, trackingBannerModel, tracking) in itemViewModels) {
                    trackingBannerModels.add(trackingBannerModel)
                    onAffiliateTrackClicked(tracking, false)
                }
                analytics.eventBannerImpression(trackingBannerModels, userId)
            } else if (visitable is FeedRecommendationViewModel) {
                val (_, cards) = visitable
                val trackingList = ArrayList<TrackingRecommendationModel>()
                for ((_, _, _, _, _, _, _, _, _, _, trackingRecommendationModel, tracking) in cards) {
                    trackingList.add(trackingRecommendationModel)
                    onAffiliateTrackClicked(tracking, false)
                }
                analytics.eventRecommendationImpression(
                        trackingList,
                        userId
                )
            } else if (visitable is TopadsShopViewModel) {
                val (_, _, _, trackingList, tracking) = visitable
                analytics.eventTopadsRecommendationImpression(
                        trackingList,
                        userId
                )
                onAffiliateTrackClicked(tracking, false)
            }
        }
    }

    private fun trackPostContentImpression(postViewModel: DynamicPostViewModel,
                                           trackingPostModel: TrackingPostModel,
                                           userId: Int, feedPosition: Int) {
        if (postViewModel.contentList.isEmpty()) {
            return
        }

        if (postViewModel.contentList[0] is GridPostViewModel) {
            val (itemList) = postViewModel.contentList[0] as GridPostViewModel
            val productList = ArrayList<ProductEcommerce>()
            for (position in 0 until itemList.size) {
                val (id, text, price) = itemList[position]
                productList.add(ProductEcommerce(
                        id,
                        text,
                        price,
                        position
                ))
            }
            analytics.eventProductGridImpression(
                    productList,
                    trackingPostModel.activityName,
                    trackingPostModel.postId,
                    userIdInt
            )
        } else if (postViewModel.contentList[0] is PollContentViewModel) {
            val (pollId) = postViewModel.contentList[0] as PollContentViewModel
            analytics.eventVoteImpression(
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    pollId,
                    trackingPostModel.postId,
                    userId
            )
        } else {
            analytics.eventCardPostImpression(
                    trackingPostModel.templateType,
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.redirectUrl,
                    trackingPostModel.mediaUrl,
                    trackingPostModel.authorId,
                    trackingPostModel.totalContent,
                    trackingPostModel.postId,
                    userId,
                    feedPosition
            )
        }
    }

    private fun trackCardPostClick(positionInFeed: Int, trackingPostModel: TrackingPostModel) {
        analytics.eventCardPostClick(
                trackingPostModel.templateType,
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.redirectUrl,
                trackingPostModel.mediaUrl,
                trackingPostModel.authorId,
                trackingPostModel.totalContent,
                trackingPostModel.postId,
                userIdInt,
                positionInFeed
        )
    }

    private fun trackRecommendationFollowClick(trackingRecommendationModel: TrackingRecommendationModel,
                                               action: String) {
        analytics.eventFollowRecommendation(
                action,
                trackingRecommendationModel.authorType,
                trackingRecommendationModel.authorId.toString()
        )
    }

    private fun trackBannerClick(adapterPosition: Int,
                                 trackingBannerModel: TrackingBannerModel) {
        analytics.eventBannerClick(
                trackingBannerModel.templateType,
                trackingBannerModel.activityName,
                trackingBannerModel.mediaType,
                trackingBannerModel.bannerUrl,
                trackingBannerModel.applink,
                trackingBannerModel.totalBanner,
                trackingBannerModel.postId,
                adapterPosition,
                userIdInt
        )
    }

    private fun trackCardPostElementClick(positionInFeed: Int, element: String) {
        if (adapter.getlist().size > positionInFeed && adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val trackingPostModel = (adapter.getlist()[positionInFeed] as DynamicPostViewModel).trackingPostModel

            analytics.eventCardPostElementClick(
                    element,
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.postId.toString()
            )
        }
    }

    private fun showAfterPostToaster() {
        if (context != null) {
            Toast.makeText(context, R.string.feed_after_post, Toast.LENGTH_LONG).show()
        }
    }

    override fun getUserVisibleHint(): Boolean {
        return super.getUserVisibleHint()
    }

    companion object {

        private val OPEN_DETAIL = 54
        private val OPEN_KOL_COMMENT = 101
        private val OPEN_KOL_PROFILE = 13
        private val OPEN_CONTENT_REPORT = 1310
        private val CREATE_POST = 888
        private val DEFAULT_VALUE = -1
        val REQUEST_LOGIN = 345

        private val TAG = FeedPlusFragment::class.java.simpleName
        private val ARGS_ROW_NUMBER = "row_number"
        private val YOUTUBE_URL = "{youtube_url}"
        private val FEED_TRACE = "mp_feed"
        private val AFTER_POST = "after_post"
        private val TRUE = "true"
        private val FEED_DETAIL = "feedcommunicationdetail"
        val BROADCAST_FEED = "BROADCAST_FEED"
        val PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED"
        val PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED"

        fun newInstance(bundle: Bundle?): FeedPlusFragment {
            val fragment = FeedPlusFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
