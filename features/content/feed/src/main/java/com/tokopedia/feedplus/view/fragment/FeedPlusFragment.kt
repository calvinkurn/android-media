package com.tokopedia.feedplus.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tagmanager.DataLayer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
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
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
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
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
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
import com.tokopedia.feedplus.KEY_FEED
import com.tokopedia.feedplus.KEY_FEED_FIRSTPAGE_CURSOR
import com.tokopedia.feedplus.KEY_FEED_FIRSTPAGE_LAST_CURSOR
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.activity.FollowRecomActivity
import com.tokopedia.feedplus.view.activity.FeedOnboardingActivity
import com.tokopedia.feedplus.view.adapter.EntryPointAdapter
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingViewHolder
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.di.FeedPlusComponent
import com.tokopedia.feedplus.view.listener.FeedPlus
import com.tokopedia.feedplus.view.presenter.FeedOnboardingViewModel
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager
import com.tokopedia.feedplus.view.viewmodel.RetryModel
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.SubmitInterestResponseViewModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.interest_pick_common.view.adapter.OnboardingAdapter
import com.tokopedia.kolcommon.util.PostMenuListener
import com.tokopedia.kolcommon.util.createBottomMenu
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel
import kotlinx.android.synthetic.main.fragment_feed_plus.*
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusFragment : BaseDaggerFragment(),
        FeedPlus.View,
        FeedPlus.View.Polling,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener,
        KolPostViewHolderListener,
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
        HighlightAdapter.HighlightListener,
        OnboardingAdapter.InterestPickItemListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var mainContent: View
    private lateinit var newFeed: View
    private lateinit var newFeedReceiver: BroadcastReceiver

    private lateinit var adapter: FeedPlusAdapter
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var infoBottomSheet: TopAdsInfoBottomSheet
    private lateinit var createPostBottomSheet: CloseableBottomSheetDialog

    private var layoutManager: LinearLayoutManager? = null
    private var loginIdInt: Int = 0
    private var isLoadedOnce: Boolean = false
    private var afterPost: Boolean = false
    private var afterRefresh: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var feedOnboardingPresenter: FeedOnboardingViewModel

    @Inject
    @get:RestrictTo(RestrictTo.Scope.TESTS)
    @set:RestrictTo(RestrictTo.Scope.TESTS)
    lateinit var presenter: FeedPlusPresenter

    @Inject
    internal lateinit var analytics: FeedAnalytics

    @Inject
    internal lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    internal lateinit var userSession: UserSessionInterface

    private val userIdInt: Int
        get() {
            try {
                return Integer.valueOf(userSession.userId)
            } catch (ignored: NumberFormatException) {
                return 0
            }

        }

    companion object {

        private const val OPEN_DETAIL = 54
        private const val OPEN_KOL_COMMENT = 101
        private const val OPEN_KOL_PROFILE = 13
        private const val OPEN_CONTENT_REPORT = 1310
        private const val CREATE_POST = 888
        private const val OPEN_INTERESTPICK_DETAIL = 1234
        private const val OPEN_INTERESTPICK_RECOM_PROFILE = 1235
        private const val DEFAULT_VALUE = -1
        const val REQUEST_LOGIN = 345

        private val TAG = FeedPlusFragment::class.java.simpleName
        private const val ARGS_ROW_NUMBER = "row_number"
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val FEED_TRACE = "mp_feed"
        private const val AFTER_POST = "after_post"
        private const val TRUE = "true"
        private const val FEED_DETAIL = "feedcommunicationdetail"
        private const val BROADCAST_FEED = "BROADCAST_FEED"
        private const val PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED"
        private const val PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED"
        private const val REMOTE_CONFIG_ENABLE_INTEREST_PICK = "mainapp_enable_interest_pick"

        //Profile Param and Args
        private const val PARAM_IS_FOLLOWING = "is_following"

        private const val IS_FOLLOWING_TRUE = 1
        private const val IS_FOLLOWING_FALSE = 0

        //region Content Report Param
        private const val CONTENT_REPORT_RESULT_SUCCESS = "result_success"
        private const val CONTENT_REPORT_RESULT_ERROR_MSG = "error_msg"
        //endregion

        //region Media Preview Param
        private const val MEDIA_PREVIEW_INDEX = "media_index"
        //endregion

        //region Kol Comment Param
        private const val COMMENT_ARGS_POSITION = "ARGS_POSITION"
        private const val COMMENT_ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT"
        private const val COMMENT_ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG"
        //endregion

        fun newInstance(bundle: Bundle?): FeedPlusFragment {
            val fragment = FeedPlusFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    object MoEngage {
        const val LOGIN_STATUS = "logged_in_status"
        const val IS_FEED_EMPTY = "is_feed_empty"
    }

    object EventMoEngage {
        const val OPEN_FEED = "Feed_Screen_Launched"
    }

    override fun getScreenName(): String {
        return FeedTrackingEventLabel.SCREEN_UNIFY_HOME_FEED
    }

    override fun initInjector() {
        DaggerFeedPlusComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activity != null) GraphqlClient.init(activity!!)
        performanceMonitoring = PerformanceMonitoring.start(FEED_TRACE)
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            feedOnboardingPresenter = viewModelProvider.get(FeedOnboardingViewModel::class.java)
        }
        initVar()
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feedOnboardingPresenter.onboardingResp.observe(this, Observer {
            hideAdapterLoading()
            when (it) {
                is Success -> onSuccessGetOnboardingData(it.data)
                is Fail -> fetchFirstPage()
            }
        })

        feedOnboardingPresenter.submitInterestPickResp.observe(this, Observer {
            view?.hideLoadingTransparent()
            when (it) {
                is Success -> onSuccessSubmitInterestPickData(it.data)
                is Fail  -> onErrorSubmitInterestPickData(it.throwable)
            }
        })
    }

    private fun initVar() {
        val typeFactory = FeedPlusTypeFactoryImpl(this, userSession, this)
        adapter = FeedPlusAdapter(typeFactory)
        adapter.setOnLoadListener { totalCount ->
            val size = adapter.getlist().size
            val lastIndex = size - 1
            if (adapter.getlist()[0] !is EmptyModel && adapter.getlist()[lastIndex] !is RetryModel)
                presenter.fetchNextPage()
        }

        val loginIdString = userSession.userId
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
        adapter.itemTreshold = 1
        layoutManager = NpaLinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)
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
                            position = layoutManager?.findLastVisibleItemPosition() ?: 0
                        } else if (layoutManager?.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager?.findFirstCompletelyVisibleItemPosition() ?: 0
                        } else if (layoutManager?.findLastCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager?.findLastCompletelyVisibleItemPosition() ?: 0
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
        return layoutManager?.findLastVisibleItemPosition()  == layoutManager?.findFirstVisibleItemPosition()
    }

    override fun onRefresh() {
        newFeed.visibility = View.GONE
        feedOnboardingPresenter.getOnboardingData(GetDynamicFeedUseCase.SOURCE_FEEDS, true)
        afterRefresh = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()

        if (layoutManager != null) {
            layoutManager = null
        }
    }

    override fun setLastCursorOnFirstPage(lastCursor: String) {
        activity?.applicationContext?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            cache.putString(KEY_FEED_FIRSTPAGE_LAST_CURSOR, lastCursor)
            cache.applyEditor()
        }
    }

    override fun setFirstPageCursor(firstPageCursor: String) {
        activity?.applicationContext?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            cache.putString(KEY_FEED_FIRSTPAGE_CURSOR, firstPageCursor)
            cache.applyEditor()
        }
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

    override fun onSuccessGetFeedFirstPage(listFeed: MutableList<Visitable<*>>) {
        parentFragment?.let {
            (it as FeedPlusContainerFragment).showCreatePostOnBoarding()
        }
        swipe_refresh_layout.isEnabled = true
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
        if (context != null && isEnableInterestPick()) {
            RouteManager.route(context, ApplinkConst.INTEREST_PICK)
        }
    }

    private fun isEnableInterestPick(): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(requireContext())
        return remoteConfig.getBoolean(REMOTE_CONFIG_ENABLE_INTEREST_PICK, true)
    }

    override fun onErrorGetFeedFirstPage(errorMessage: String) {
        finishLoading()
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(activity, mainContent, errorMessage
            ) { fetchFirstPage() }
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }

    }

    override fun onSearchShopButtonClicked() {
        if (context != null) {
            val INIT_STATE_FRAGMENT_FAVORITE = 2
            val EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT"
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.HOME)
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


    override fun onSuccessGetFeed(listFeed: MutableList<Visitable<*>>) {
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
                val serverErrorMsg = data.getStringExtra(COMMENT_ARGS_SERVER_ERROR_MSG)
                if (!TextUtils.isEmpty(serverErrorMsg)) {
                    ToasterError
                            .make(view, serverErrorMsg, BaseToaster.LENGTH_LONG)
                            .setAction(R.string.cta_refresh_feed) { v -> onRefresh() }.show()
                } else {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(COMMENT_ARGS_POSITION, DEFAULT_VALUE),
                            data.getIntExtra(COMMENT_ARGS_TOTAL_COMMENT, 0)
                    )
                }
            }
            CREATE_POST -> {
            }
            OPEN_CONTENT_REPORT -> if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(CONTENT_REPORT_RESULT_SUCCESS, false)) {
                    onSuccessReportContent()
                } else {
                    onErrorReportContent(
                            data.getStringExtra(CONTENT_REPORT_RESULT_ERROR_MSG)
                    )
                }
            }
            OPEN_INTERESTPICK_DETAIL -> {
                val selectedIdList = data.getIntegerArrayListExtra(FeedOnboardingFragment.EXTRA_SELECTED_IDS)
                adapter.getlist().firstOrNull { it is OnboardingViewModel }?.let {
                    (it as? OnboardingViewModel)?.dataList?.forEach {
                        it.isSelected = selectedIdList.contains(it.id)
                    }
                }
                adapter.notifyItemChanged(0, OnboardingViewHolder.PAYLOAD_UPDATE_ADAPTER)

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
        if (::recyclerView.isInitialized) {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun triggerClearNewFeedNotification() {
        if (context?.applicationContext != null) {
            val intent = Intent(BROADCAST_FEED)
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, true)
            LocalBroadcastManager.getInstance(requireContext().applicationContext).sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        registerNewFeedReceiver()
        if (userVisibleHint && ::presenter.isInitialized) {
            loadData(userVisibleHint)
        }
    }

    override fun onPause() {
        super.onPause()
        unRegisterNewFeedReceiver()
        feedAnalytics.sendPendingAnalytics()
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
        if (isVisibleToUser && isAdded && activity != null && ::presenter.isInitialized) {
            if (!isLoadedOnce) {
                feedOnboardingPresenter.getOnboardingData(GetDynamicFeedUseCase.SOURCE_FEEDS, false)
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
        //Not Used - 04/11/2019
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String) {
        onGoToLink(applink)
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String) {
        onGoToLink(url)
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {

    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String, activityType: String, position: String) {

    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            presenter.followKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            presenter.unfollowKol(id, rowNumber)
        } else {
            onGoToLogin()
        }

    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.LIKE)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                    activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.unlikeKol(id, rowNumber)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.UNLIKE)
        } else {
            onGoToLogin()
        }
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        RouteManager.getIntent(
                requireContext(),
                UriUtil.buildUriAppendParam(
                        ApplinkConstInternalContent.COMMENT,
                        mapOf(
                                COMMENT_ARGS_POSITION to rowNumber.toString()
                        )
                ),
                id.toString()
        ).run { startActivityForResult(this, OPEN_KOL_COMMENT) }
        trackCardPostElementClick(rowNumber, FeedAnalytics.Element.COMMENT)
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String,
                               activityType: String) {

    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): Dialog {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.feed_delete_post))
        dialog.setDesc(getString(R.string.feed_after_delete_cant))
        dialog.setBtnOk(getString(R.string.button_delete))
        dialog.setBtnCancel(getString(R.string.cancel))
        dialog.setOnOkClickListener {
            presenter.deletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener { dialog.dismiss() }
        return dialog
    }

    override fun onGoToLink(link: String) {
        context?.let {
            if (!TextUtils.isEmpty(link)) {
                if (RouteManager.isSupportApplink(it, link)) {
                    RouteManager.route(it, link)
                } else {
                    RouteManager.route(
                            it,
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                    )
                }
            }
        }
    }

    override fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String) {
        presenter.sendVote(rowNumber, pollId, optionId)
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

    override fun onErrorFollowKol(errorMessage: String, id: Int, status: Int, rowNumber: Int) {
        Toaster.make(view!!, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_try_again), View.OnClickListener {
            if (status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                presenter.unfollowKol(id, rowNumber)
            else
                presenter.followKol(id, rowNumber )
        })
    }

    override fun onSuccessFollowUnfollowKol(rowNumber: Int) {
        if (adapter.getlist()[rowNumber] is DynamicPostViewModel) {
            val (_, _, header) = adapter.getlist()[rowNumber] as DynamicPostViewModel
            header.followCta.isFollow = !header.followCta.isFollow
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW)
        }
    }

    override fun onErrorLikeDislikeKolPost(errorMessage: String) {
        Toaster.make(view!!, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)

    }

    override fun onSuccessLikeDislikeKolPost(rowNumber: Int) {
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

    override fun sendMoEngageOpenFeedEvent() {
        val isEmptyFeed = !hasFeed()
        val value = DataLayer.mapOf(
                MoEngage.LOGIN_STATUS, userSession.isLoggedIn,
                MoEngage.IS_FEED_EMPTY, isEmptyFeed
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, EventMoEngage.OPEN_FEED)
    }

    override fun onStop() {
        super.onStop()
        activity?.run {
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
                presenter.unfollowKolFromRecommendation(userIdInt, positionInFeed, adapterPosition)
            } else {
                presenter.followKolFromRecommendation(userIdInt, positionInFeed, adapterPosition)
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

    override fun onTitleCtaClick(redirectUrl: String, adapterPosition: Int) {
        onGoToLink(redirectUrl)
    }

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)

        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.AVATAR)
    }

    override fun onHeaderActionClick(positionInFeed: Int, id: String, type: String,
                                     isFollow: Boolean) {
        if (userSession.isLoggedIn) {
            if (type == FollowCta.AUTHOR_USER) {
                val userIdInt = id.toIntOrZero()

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
            val menus = createBottomMenu(requireContext(), deletable, reportable, false, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(positionInFeed, postId).show()
                }

                override fun onReportClick() {
                    if (userSession.isLoggedIn) {
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
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        onGoToKolComment(positionInFeed, id, false, "")
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

    override fun onStatsClick(title: String, activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int) {
        //Not used
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
                RouteManager.route(
                        requireContext(),
                        UriUtil.buildUriAppendParam(
                                ApplinkConstInternalContent.MEDIA_PREVIEW,
                                mapOf(
                                        MEDIA_PREVIEW_INDEX to contentPosition.toString()
                                )
                        ),
                        id.toString()
                )
            }
        }
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean) {
        for (track in trackList) {
            if (isClick) {
                presenter.trackAffiliate(track.clickURL)
            } else {
                presenter.trackAffiliate(track.viewURL)
            }
        }
    }

    override fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel) {

    }

    override fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem, authorType: String) {
        val shop = postTagItem.shop.firstOrNull()
        feedAnalytics.eventFeedAddToCart(
                postTagItem.id,
                postTagItem.text,
                postTagItem.price,
                1,
                shop?.shopId?.toIntOrZero() ?: -1,
                "",
                authorType
        )
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

    override fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel) {
        feedAnalytics.eventTimelineClickHashtag(
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                hashtagText
        )
    }

    override fun onReadMoreClicked(trackingPostModel: TrackingPostModel) {
        feedAnalytics.eventTimelineClickReadMore(
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType
        )
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
            RouteManager.route(
                    requireContext(),
                    ApplinkConstInternalContent.VIDEO_DETAIL,
                    postId
            )
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    override fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    override fun onSuccessDeletePost(rowNumber: Int) {
        adapter.getlist().removeAt(rowNumber)
        adapter.notifyItemRemoved(rowNumber)
        val snackbar = ToasterNormal.make(view,
                getString(R.string.feed_post_deleted),
                BaseToaster.LENGTH_LONG
        )
        snackbar.setAction(R.string.af_title_ok) { snackbar.dismiss() }.show()
        if (adapter.getlist().isEmpty()) {
            showRefresh()
            onRefresh()
        }
    }

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
        ToasterError.make(view, errorMessage, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again) {
                    presenter.deletePost(id, rowNumber)
                }
                .show()
    }

    override fun onInterestPickItemClicked(item: InterestPickDataViewModel) {
        feedAnalytics.eventClickFeedInterestPick(item.name)
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<InterestPickDataViewModel>) {
        feedAnalytics.eventClickFeedInterestPickSeeAll()
        activity?.let {
            val bundle = Bundle()
            bundle.putIntegerArrayList(FeedOnboardingFragment.EXTRA_SELECTED_IDS, ArrayList(selectedItemList.map { it.id }))
            startActivityForResult(FeedOnboardingActivity.getCallingIntent(it, bundle), OPEN_INTERESTPICK_DETAIL)
        }
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<InterestPickDataViewModel>) {
        view?.showLoadingTransparent()
        feedAnalytics.eventClickFeedCheckAccount(selectedItemList.size.toString())
        feedOnboardingPresenter.submitInterestPickData(selectedItemList, FeedOnboardingViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK, OPEN_INTERESTPICK_RECOM_PROFILE)
    }

    private fun fetchFirstPage() {
        val firstPageCursor: String = if (afterRefresh) getFirstPageCursor() else ""
        presenter.fetchFirstPage(firstPageCursor)
        afterRefresh = false
    }

    private fun getFirstPageCursor(): String {
        context?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            return cache.getString(KEY_FEED_FIRSTPAGE_CURSOR, "")
        }
        return ""
    }

    private fun onSuccessGetOnboardingData(data: OnboardingViewModel) {
        if (!data.isEnableOnboarding) {
            fetchFirstPage()
        } else {
            finishLoading()
            clearData()
            val feedOnboardingData: MutableList<InterestPickDataViewModel> = mutableListOf()
            feedOnboardingData.addAll(data.dataList)
            data.dataList = feedOnboardingData
            adapter.addItem(data)
            parentFragment?.let {
                (it as FeedPlusContainerFragment).hideAllFab(true)
            }
            swipe_refresh_layout.isRefreshing = false
            swipe_refresh_layout.isEnabled = false
        }
    }

    private fun onSuccessSubmitInterestPickData(data : SubmitInterestResponseViewModel) {
        context?.let {
            when (data.source) {
                FeedOnboardingViewModel.PARAM_SOURCE_SEE_ALL_CLICK -> {
                    startActivityForResult(FeedOnboardingActivity.getCallingIntent(it, Bundle()), OPEN_INTERESTPICK_DETAIL)
                }
                FeedOnboardingViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK -> {
                    startActivityForResult(FollowRecomActivity.createIntent(it, data.idList.toIntArray()), OPEN_INTERESTPICK_RECOM_PROFILE)
                }

            }
        }
    }

    private fun onErrorSubmitInterestPickData(throwable: Throwable) {
        view?.let{
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG
            )
        }

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
            val intent = RouteManager.getIntent(
                    requireContext(),
                    ApplinkConstInternalContent.CONTENT_REPORT,
                    contentId.toString()
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

    private fun trackFeedImpression(listFeed: MutableList<Visitable<*>>) {
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

            } else if (visitable is BannerViewModel) {
                val (itemViewModels) = visitable
                val trackingBannerModels = ArrayList<TrackingBannerModel>()
                for ((_, _, _, trackingBannerModel, tracking) in itemViewModels) {
                    trackingBannerModels.add(trackingBannerModel)
                }
                analytics.eventBannerImpression(trackingBannerModels, userId)
            } else if (visitable is FeedRecommendationViewModel) {
                val (_, cards) = visitable
                val trackingList = ArrayList<TrackingRecommendationModel>()
                for ((_, _, _, _, _, _, _, _, _, _, trackingRecommendationModel, tracking) in cards) {
                    trackingList.add(trackingRecommendationModel)
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
}
