package com.tokopedia.feedplus.view.fragment

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.mapper.TopAdsHeadlineActivityCounter
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.util.util.copy
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
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.profilerecommendation.view.activity.FollowRecomActivity
import com.tokopedia.feedplus.view.activity.FeedOnboardingActivity
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl
import com.tokopedia.feedplus.view.adapter.viewholder.EmptyFeedBeforeLoginViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.RetryViewHolder
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.analytics.widget.FeedPlayWidgetAnalyticListener
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED_FIRST_PAGE_CURSOR
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED_FIRST_PAGE_LAST_CURSOR
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.di.FeedPlusComponent
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.feedplus.view.viewmodel.RetryModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.interest_pick_common.view.adapter.InterestPickAdapter
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.SubmitInterestResponseViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.util.PostMenuListener
import com.tokopedia.kolcommon.util.createBottomMenu
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
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
import kotlinx.android.synthetic.main.fragment_feed_plus.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusFragment : BaseDaggerFragment(),
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
        InterestPickAdapter.InterestPickItemListener,
        EmptyFeedBeforeLoginViewHolder.EmptyFeedBeforeLoginListener,
        RetryViewHolder.RetryViewHolderListener,
        EmptyFeedViewHolder.EmptyFeedListener,
        FeedPlusAdapter.OnLoadListener, TopAdsBannerViewHolder.TopAdsBannerListener,
        PlayWidgetListener, TopAdsHeadlineViewHolder.TopAdsHeadlineListener,
        ShareCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var mainContent: View
    private lateinit var newFeed: View
    private lateinit var newFeedReceiver: BroadcastReceiver

    private lateinit var adapter: FeedPlusAdapter
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var infoBottomSheet: TopAdsInfoBottomSheet

    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator

    private var layoutManager: LinearLayoutManager? = null
    private var loginIdInt: Int = 0
    private var isLoadedOnce: Boolean = false
    private var afterPost: Boolean = false
    private var afterRefresh: Boolean = false

    private var isUserEventTrackerDoneTrack = false

    private lateinit var shareData: LinkerData

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedViewModel: FeedViewModel

    @Inject
    internal lateinit var analytics: FeedAnalytics

    @Inject
    internal lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var feedPlayWidgetAnalytic: FeedPlayWidgetAnalyticListener

    @Inject
    lateinit var playWidgetImpressionValidator: DefaultImpressionValidator

    @Inject
    internal lateinit var userSession: UserSessionInterface

    private val userIdInt: Int
        get() {
            return userSession.userId.toIntOrZero()
        }

    companion object {

        private const val OPEN_DETAIL = 54
        private const val OPEN_KOL_COMMENT = 101
        private const val OPEN_CONTENT_REPORT = 1310
        private const val CREATE_POST = 888
        private const val OPEN_INTERESTPICK_DETAIL = 1234
        private const val OPEN_INTERESTPICK_RECOM_PROFILE = 1235
        private const val DEFAULT_VALUE = -1
        private const val OPEN_PLAY_CHANNEL = 1858
        const val REQUEST_LOGIN = 345

        private val TAG = FeedPlusFragment::class.java.simpleName
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val FEED_TRACE = "mp_feed"
        private const val AFTER_POST = "after_post"
        private const val TRUE = "true"
        private const val FEED_DETAIL = "feedcommunicationdetail"
        private const val BROADCAST_FEED = "BROADCAST_FEED"
        private const val PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED"
        private const val PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED"
        private const val REMOTE_CONFIG_ENABLE_INTEREST_PICK = "mainapp_enable_interest_pick"

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

        private const val EXTRA_PLAY_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_PLAY_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

        fun newInstance(bundle: Bundle?): FeedPlusFragment {
            val fragment = FeedPlusFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val PERFORMANCE_FEED_PAGE_NAME = "feed"
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
        if (activity != null) GraphqlClient.init(requireActivity())
        performanceMonitoring = PerformanceMonitoring.start(FEED_TRACE)
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            feedViewModel = viewModelProvider.get(FeedViewModel::class.java)
        }
        initVar()
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        feedViewModel.run {
            onboardingResp.observe(lifecycleOwner, Observer {
                hideAdapterLoading()
                when (it) {
                    is Success -> onSuccessGetOnboardingData(it.data)
                    is Fail -> fetchFirstPage()
                }
            })

            submitInterestPickResp.observe(lifecycleOwner, Observer {
                view?.hideLoadingTransparent()
                when (it) {
                    is Success -> onSuccessSubmitInterestPickData(it.data)
                    is Fail -> onErrorSubmitInterestPickData(it.throwable)
                }
            })

            getFeedFirstPageResp.observe(lifecycleOwner, Observer {
                finishLoading()
                when (it) {
                    is Success -> onSuccessGetFirstFeed(it.data)
                    is Fail -> onErrorGetFirstFeed(it.throwable)
                }
            })

            getFeedNextPageResp.observe(lifecycleOwner, Observer {
                hideAdapterLoading()
                when (it) {
                    is Success -> onSuccessGetNextFeed(it.data)
                    is Fail -> onErrorGetNextFeed(it.throwable)
                }
            })

            doFavoriteShopResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val stringBuilder = StringBuilder()
                        val data = it.data
                        if (data.isSuccess) {
                            stringBuilder.append(MethodChecker.fromHtml(data.promotedShopViewModel.shop.name)).append(" ")
                            if (data.promotedShopViewModel.isFavorit) {
                                stringBuilder.append(getString(R.string.shop_success_unfollow))
                            } else {
                                stringBuilder.append(getString(R.string.shop_success_follow))
                            }
                        } else {
                            stringBuilder.append(getString(com.tokopedia.abstraction.R.string.msg_network_error))
                        }
                        data.resultString = stringBuilder.toString()
                        onSuccessFavoriteUnfavoriteShop(data)
                    }
                    is Fail -> onErrorFavoriteUnfavoriteShop(it.throwable)
                }
            })

            followKolResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessFollowUnfollowKol(data.rowNumber)
                        } else {
                            data.errorMessage = getString(R.string.default_request_error_unknown)
                            onErrorFollowUnfollowKol(data)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            likeKolResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessLikeDislikeKolPost(data.rowNumber)
                        } else {
                            data.errorMessage = getString(R.string.default_request_error_unknown)
                            onErrorLikeDislikeKolPost(data.errorMessage)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            followKolRecomResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessFollowKolFromRecommendation(data)
                        } else {
                            data.errorMessage = getString(R.string.default_request_error_unknown)
                            onErrorFollowUnfollowKol(data)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            deletePostResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessDeletePost(data.rowNumber)
                        } else {
                            data.errorMessage = getString(R.string.default_request_error_unknown)
                            onErrorDeletePost(data)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            atcResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        when {
                            data.isSuccess -> {
                                onAddToCartSuccess()
                            }
                            data.errorMsg.isNotEmpty() -> {
                                view?.run {
                                    Toaster.build(this, data.errorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                                }
                            }
                            else -> {
                                onAddToCartFailed(data.applink)
                            }
                        }
                    }
                    is Fail -> {
                        Timber.e(it.throwable)
                        view?.run {
                            Toaster.build(this, getString(R.string.default_request_error_unknown), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            })

            toggleFavoriteShopResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessToggleFavoriteShop(data.rowNumber, data.adapterPosition)
                        } else {
                            data.errorMessage = ErrorHandler.getErrorMessage(context, RuntimeException())
                            onErrorToggleFavoriteShop(data)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            trackAffiliateResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Fail -> {
                        val message = it.throwable.localizedMessage
                        view?.run {
                            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                        }
                    }
                }
            })

            playWidgetModel.observe(lifecycleOwner, Observer {
                when (it) {
                    is Fail -> adapter.removePlayWidget()
                    is Success -> {
                        if (!it.data.isAutoRefresh) playWidgetImpressionValidator.invalidate()
                        adapter.updatePlayWidget(it.data)
                    }
                }
            })
        }
    }

    private fun initVar() {
        playWidgetCoordinator = PlayWidgetCoordinator().apply {
            setListener(this@FeedPlusFragment)
            setAnalyticListener(DefaultPlayWidgetInListAnalyticListener(feedPlayWidgetAnalytic))
            setImpressionHelper(ImpressionHelper(validator = playWidgetImpressionValidator))
        }
        val typeFactory = FeedPlusTypeFactoryImpl(this, userSession, this, playWidgetCoordinator)
        adapter = FeedPlusAdapter(typeFactory, this)

        val loginIdString = userSession.userId
        loginIdInt = loginIdString.toIntOrZero()

        newFeedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent != null && intent.action != null && intent.action == BROADCAST_FEED) {
                    val isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false)
                    if (isHaveNewFeed) {
                        onRefresh()
                        scrollToTop()
                        triggerNewFeedNotification()
                    }
                }
            }
        }
        registerNewFeedReceiver()

        arguments?.run {
            afterPost = TextUtils.equals(getString(AFTER_POST, ""), TRUE)
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun reInitInjector(component: FeedPlusComponent) {
        component.inject(this)
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun resetToFirstTime() {
        isLoadedOnce = false
    }

    override fun onLoad(totalCount: Int) {
        val size = adapter.getlist().size
        val lastIndex = size - 1
        if (adapter.getlist()[0] !is EmptyModel && adapter.getlist()[lastIndex] !is RetryModel)
            feedViewModel.getFeedNextPage()
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
        newFeed.setOnClickListener {
            scrollToTop()
            sendNewFeedClickEvent()
            showRefresh()
            onRefresh()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (hasFeed()) {
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                var position = 0
                                val item: Visitable<*>
                                when {
                                    itemIsFullScreen() -> {
                                        position = layoutManager?.findLastVisibleItemPosition() ?: 0
                                    }
                                    layoutManager?.findFirstCompletelyVisibleItemPosition() != -1 -> {
                                        position = layoutManager?.findFirstCompletelyVisibleItemPosition()
                                                ?: 0
                                    }
                                    layoutManager?.findLastCompletelyVisibleItemPosition() != -1 -> {
                                        position = layoutManager?.findLastCompletelyVisibleItemPosition()
                                                ?: 0
                                    }
                                }

                                item = adapter.getlist()[position]

                                if (item is DynamicPostViewModel) {
                                    if (!TextUtils.isEmpty(item.footer.buttonCta.appLink)) {
                                        adapter.notifyItemChanged(position, DynamicPostViewHolder.PAYLOAD_ANIMATE_FOOTER)
                                    }
                                }
                                FeedScrollListener.onFeedScrolled(recyclerView, adapter.getlist())
                            }
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }

            }

        })
    }

    private fun sendNewFeedClickEvent() {
        analytics.eventNewPostClick()
    }

    private fun itemIsFullScreen(): Boolean {
        return layoutManager?.findLastVisibleItemPosition() == layoutManager?.findFirstVisibleItemPosition()
    }

    override fun onRefresh() {
        newFeed.visibility = View.GONE
        feedViewModel.getOnboardingData(GetDynamicFeedUseCase.SOURCE_FEEDS)
        afterRefresh = true
        TopAdsHeadlineActivityCounter.page = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
        if (layoutManager != null) {
            layoutManager = null
        }
        TopAdsHeadlineActivityCounter.page = 1
    }

    override fun onInfoClicked() {
        infoBottomSheet.show()
    }

    override fun onRetryClicked() {
        adapter.removeRetry()
        adapter.showLoading()
        adapter.setEndlessScrollListener()
        feedViewModel.getFeedNextPage()
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
                    view?.let {
                        Toaster.make(it, serverErrorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.cta_refresh_feed), View.OnClickListener { onRefresh() })
                    }
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
                    (it as? OnboardingViewModel)?.dataList?.forEach { interestPickDataViewModel ->
                        interestPickDataViewModel.isSelected = selectedIdList.contains(interestPickDataViewModel.id)
                    }
                }
                adapter.notifyItemChanged(0, OnboardingViewHolder.PAYLOAD_UPDATE_ADAPTER)
            }
            OPEN_PLAY_CHANNEL -> {
                val channelId = data.getStringExtra(EXTRA_PLAY_CHANNEL_ID)
                val totalView = data.getStringExtra(EXTRA_PLAY_TOTAL_VIEW)
                updatePlayWidgetTotalView(channelId, totalView)
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
        feedViewModel.doFavoriteShop(dataShop, position)
        analytics.eventFeedClickShop(screenName,
                dataShop.shop.id,
                FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE)

    }

    fun onParentFragmentHiddenChanged(hidden: Boolean) {
        playWidgetOnVisibilityChanged(
                isParentHidden = hidden
        )
    }

    fun scrollToTop() {
        if (::recyclerView.isInitialized) {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun triggerNewFeedNotification() {
        if (context?.applicationContext != null) {
            val intent = Intent(BROADCAST_FEED)
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, true)
            LocalBroadcastManager.getInstance(requireContext().applicationContext).sendBroadcast(intent)
        }
    }

    private fun triggerClearNewFeedNotification() {
        if (context?.applicationContext != null) {
            val intent = Intent(BROADCAST_FEED)
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, false)
            LocalBroadcastManager.getInstance(requireContext().applicationContext).sendBroadcast(intent)
        }
    }

    override fun onResume() {
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        registerNewFeedReceiver()
        if (userVisibleHint) {
            loadData(userVisibleHint)
        }
    }

    override fun onPause() {
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        unRegisterNewFeedReceiver()
        analytics.sendPendingAnalytics()
        feedAnalytics.sendPendingAnalytics()
    }

    private fun registerNewFeedReceiver() {
        if (activity != null && requireActivity().applicationContext != null) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_FEED)

            LocalBroadcastManager
                    .getInstance(requireActivity().applicationContext)
                    .registerReceiver(newFeedReceiver, intentFilter)
        }
    }

    private fun unRegisterNewFeedReceiver() {
        if (activity != null && requireActivity().applicationContext != null) {
            LocalBroadcastManager
                    .getInstance(requireActivity().applicationContext)
                    .unregisterReceiver(newFeedReceiver)
        }
    }

    private fun loadData(isVisibleToUser: Boolean) {
        activity?.let {
            if (isVisibleToUser && isAdded) {
                if (!isLoadedOnce) {
                    feedViewModel.getOnboardingData(GetDynamicFeedUseCase.SOURCE_FEEDS)
                    isLoadedOnce = !isLoadedOnce
                }
                if (afterPost) {
                    showAfterPostToaster()
                    afterPost = false
                }
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadData(isVisibleToUser)
        playWidgetOnVisibilityChanged(
                isUserVisibleHint = isVisibleToUser
        )
    }

    override fun onSearchShopButtonClicked() {
        if (context != null) {
            val valueExtraInitFragment = 2
            val keyExtraInitFragment = "EXTRA_INIT_FRAGMENT"
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.HOME)
            intent.putExtra(keyExtraInitFragment, valueExtraInitFragment)
            startActivity(intent)
        }
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
            feedViewModel.doFollowKol(id, rowNumber)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.FOLLOW)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnfollowKol(id, rowNumber)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.UNFOLLOW)
        } else {
            onGoToLogin()
        }

    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        if (userSession.isLoggedIn) {
            feedViewModel.doLikeKol(id, rowNumber)
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.LIKE)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                    activityType: String) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnlikeKol(id, rowNumber)
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

    /**
     * Play Widget
     */
    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        feedViewModel.doAutoRefreshPlayWidget()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, OPEN_PLAY_CHANNEL)
    }

    private fun playWidgetOnVisibilityChanged(
            isViewResumed: Boolean = if (view == null) false else viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
            isUserVisibleHint: Boolean = userVisibleHint,
            isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) playWidgetCoordinator.onResume()
            else playWidgetCoordinator.onPause()
        }
    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): Dialog {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.feed_delete_post))
        dialog.setDesc(getString(R.string.feed_after_delete_cant))
        dialog.setBtnOk(getString(R.string.feed_delete))
        dialog.setBtnCancel(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
        dialog.setOnOkClickListener {
            feedViewModel.doDeletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener { dialog.dismiss() }
        return dialog
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        val newList: MutableList<DynamicPostViewModel> = adapter.getlist().copy()
        if (rowNumber != DEFAULT_VALUE
                && newList.size > rowNumber) {
            val (_, _, _, _, footer) = newList[rowNumber]
            val comment = footer.comment
            try {
                val commentValue = Integer.valueOf(comment.fmt) + totalNewComment
                comment.fmt = commentValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            comment.value = comment.value + totalNewComment
            adapter.updateList(newList)
        }
    }

    private fun onSuccessReportContent() {
        view?.let {
            Toaster.make(it, getString(R.string.feed_content_reported), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.design.R.string.label_close))
        }
    }

    private fun onErrorReportContent(errorMsg: String) {
        view?.let {
            Toaster.make(it, errorMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.label_close))
        }
    }

    override fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            requireActivity().startActivityForResult(intent, REQUEST_LOGIN)
        }
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
                                             redirectLink: String, postType: String, authorId: String) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[positionInFeed] as FeedRecommendationViewModel
            val (templateType, activityName, _, _, authorName, authorType, modelAuthorId, cardPosition) = cards[adapterPosition].trackingRecommendationModel
            analytics.eventRecommendationClick(
                    templateType,
                    activityName,
                    authorName,
                    authorType,
                    modelAuthorId,
                    cardPosition,
                    userIdInt
            )
        }
        if (postType == FollowCta.AUTHOR_USER || postType == FollowCta.AUTHOR_SHOP)
            feedAnalytics.eventClickFeedProfileRecommendation(authorId, postType)
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int,
                                             id: String, type: String,
                                             isFollow: Boolean) {
        if (type == FollowCta.AUTHOR_USER) {
            val userIdInt = id.toIntOrZero()

            if (isFollow) {
                feedViewModel.doUnfollowKolFromRecommendation(userIdInt, positionInFeed, adapterPosition)
            } else {
                feedViewModel.doFollowKolFromRecommendation(userIdInt, positionInFeed, adapterPosition)
            }

        } else if (type == FollowCta.AUTHOR_SHOP) {
            feedViewModel.doToggleFavoriteShop(positionInFeed, adapterPosition, id)
        }

        if (adapter.getlist()[positionInFeed] is FeedRecommendationViewModel) {

            val (_, cards) = adapter.getlist()[positionInFeed] as FeedRecommendationViewModel
            trackRecommendationFollowClick(
                    cards[adapterPosition].trackingRecommendationModel,
                    if (isFollow) FeedAnalytics.Element.UNFOLLOW else FeedAnalytics.Element.FOLLOW
            )
        }
    }

    private fun visitShopPageWithAnalytics(positionInFeed: Int, shop: Shop) {
        goToShopPage(shop.id)
        if (adapter.getlist()[positionInFeed] is TopadsShopViewModel) {
            val (_, dataList, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopViewModel
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
            feedAnalytics.eventClickTopadsPromoted(shop.id)
        }
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: Shop) {
        trackShopClickImpression(positionInFeed, adapterPosition, shop)
        visitShopPageWithAnalytics(positionInFeed, shop)
    }

    override fun onTopAdsImpression(url: String, shopId: String, shopName: String, imageUrl: String) {
        feedViewModel.doTopAdsTracker(url, shopId, shopName, imageUrl, false)
    }

    private fun trackShopClickImpression(positionInFeed: Int, adapterPosition: Int, shop: Shop) {
        if (adapter.getlist()[positionInFeed] is TopadsShopViewModel) {
            val (_, dataList, _, _) = adapter.getlist()[positionInFeed] as TopadsShopViewModel
            if (adapterPosition != RecyclerView.NO_POSITION) {
                feedViewModel.doTopAdsTracker(dataList[adapterPosition].shopClickUrl, shop.id, shop.name, dataList[adapterPosition].shop.imageShop.xsEcs, true)
            }
        }
    }

    private fun trackUrlEvent(url: String) {
        feedViewModel.doTrackAffiliate(url)
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: Data) {
        feedViewModel.doToggleFavoriteShop(positionInFeed, adapterPosition, data.shop.id)

        if (adapter.getlist()[positionInFeed] is TopadsShopViewModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopViewModel

            for (tracking in trackingList) {
                if (TextUtils.equals(tracking.authorName, data.shop.name)) {
                    if (data.isFavorit) {
                        trackRecommendationFollowClick(tracking, FeedAnalytics.Element.UNFOLLOW)
                    } else {
                        trackShopClickImpression(positionInFeed, adapterPosition, data.shop)
                        trackRecommendationFollowClick(tracking, FeedAnalytics.Element.FOLLOW)
                    }

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

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String, activityId: Int, activityName: String, followCta: FollowCta) {
        onGoToLink(redirectUrl)
        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.AVATAR)
        feedAnalytics.eventClickFeedAvatar(activityId.toString(), activityName, followCta.authorID, followCta.authorType)
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
                feedViewModel.doToggleFavoriteShop(positionInFeed, 0, id)
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
        activity?.let {
            shareData = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                    .setName(title)
                    .setDescription(description)
                    .setImgUri(imageUrl)
                    .setUri(url)
                    .setType(LinkerData.FEED_TYPE)
                    .build()
            val linkerShareData = DataMapper().getLinkerShareData(shareData)
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    this
            ))
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

    override fun userImagePostImpression(positionInFeed: Int, contentPosition: Int) {
        if (adapter.getList()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            feedAnalytics.eventImageImpressionPost(
                    FeedAnalyticTracker.Screen.FEED,
                    trackingPostModel.postId.toString(),
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.mediaUrl,
                    trackingPostModel.recomId,
                    positionInFeed)
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
                trackUrlEvent(track.clickURL)
            } else {
                trackUrlEvent(track.viewURL)
            }
        }
    }

    override fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel) {

    }

    override fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem, authorType: String) {
        if (userSession.isLoggedIn) {
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
            feedViewModel.doAtc(postTagItem)
        } else {
            onGoToLogin()
        }
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
                analytics.eventGoToFeedDetail(trackingPostModel.postId, trackingPostModel.recomId)
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
                        userIdInt,
                        trackingPostModel.recomId
                )
            }
        }
    }

    override fun onVideoPlayerClicked(positionInFeed: Int,
                                      contentPosition: Int,
                                      postId: String,
                                      redirectUrl: String) {
        if (activity != null) {
            onGoToLink(redirectUrl)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onInterestPickItemClicked(item: InterestPickDataViewModel) {
        feedAnalytics.eventClickFeedInterestPick(item.name)
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<InterestPickDataViewModel>) {
        feedAnalytics.eventClickFeedInterestPickSeeAll()
        activity?.let { fragmentActivity ->
            val bundle = Bundle()
            bundle.putIntegerArrayList(FeedOnboardingFragment.EXTRA_SELECTED_IDS, ArrayList(selectedItemList.map { it.id }))
            startActivityForResult(FeedOnboardingActivity.getCallingIntent(fragmentActivity, bundle), OPEN_INTERESTPICK_DETAIL)
        }
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<InterestPickDataViewModel>) {
        view?.showLoadingTransparent()
        feedAnalytics.eventClickFeedCheckAccount(selectedItemList.size.toString())
        feedViewModel.submitInterestPickData(selectedItemList, FeedViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK, OPEN_INTERESTPICK_RECOM_PROFILE)
    }

    private fun fetchFirstPage() {
        val firstPageCursor: String = if (afterRefresh) getFirstPageCursor() else ""
        showRefresh()
        feedViewModel.getFeedFirstPage(firstPageCursor)
        afterRefresh = false
    }

    private fun getFirstPageCursor(): String {
        context?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            return cache.getString(KEY_FEED_FIRST_PAGE_CURSOR, "")
        }
        return ""
    }

    private fun onSuccessGetOnboardingData(data: OnboardingViewModel) {
        if (!data.isEnableOnboarding) {
            fetchFirstPage()
        } else {
            finishLoading()
            clearData()
            val feedOnBoardingData: MutableList<InterestPickDataViewModel> = mutableListOf()
            feedOnBoardingData.addAll(data.dataList)
            data.dataList = feedOnBoardingData
            adapter.addItem(data)
            parentFragment?.let {
                (it as FeedPlusContainerFragment).hideAllFab(true)
            }
            swipe_refresh_layout.isRefreshing = false
            swipe_refresh_layout.isEnabled = false
        }
    }

    private fun onSuccessSubmitInterestPickData(data: SubmitInterestResponseViewModel) {
        context?.let {
            when (data.source) {
                FeedViewModel.PARAM_SOURCE_SEE_ALL_CLICK -> {
                    startActivityForResult(FeedOnboardingActivity.getCallingIntent(it, Bundle()), OPEN_INTERESTPICK_DETAIL)
                }
                FeedViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK -> {
                    startActivityForResult(FollowRecomActivity.createIntent(it, data.idList.toIntArray()), OPEN_INTERESTPICK_RECOM_PROFILE)
                }

            }
        }
    }

    private fun onErrorSubmitInterestPickData(throwable: Throwable) {
        view?.let {
            Toaster.make(it,
                    ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }

    }

    private fun onSuccessGetFirstFeed(firstPageDomainModel: DynamicFeedFirstPageDomainModel) {
        if (!firstPageDomainModel.shouldOverwrite) {
            adapter.updateList(firstPageDomainModel.dynamicFeedDomainModel.postList)
            return
        }

        clearData()
        val model = firstPageDomainModel.dynamicFeedDomainModel
        if (model.postList.isNotEmpty()) {
            setLastCursorOnFirstPage(model.cursor)
            setFirstPageCursor(model.firstPageCursor)
            parentFragment?.let {
                (it as FeedPlusContainerFragment).showCreatePostOnBoarding()
            }
            swipe_refresh_layout.isEnabled = true
            trackFeedImpression(model.postList)
            adapter.clearData()
            adapter.updateList(model.postList)
            triggerClearNewFeedNotification()
            if (model.hasNext) {
                setEndlessScroll()
            } else {
                unsetEndlessScroll()
            }
        } else {
            onShowEmpty()
        }

        if (firstPageDomainModel.isInterestWhitelist) {
            showInterestPick()
        }

        sendMoEngageOpenFeedEvent()
        sendFeedPlusScreenTracking()
        stopTracePerformanceMon()
    }

    private fun onErrorGetFirstFeed(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        finishLoading()
        val errorMessage = ErrorHandler.getErrorMessage(context, e)
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(activity, mainContent, errorMessage
            ) { fetchFirstPage() }
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }
        stopTracePerformanceMon()
    }

    private fun onSuccessGetNextFeed(model: DynamicFeedDomainModel) {
        hideAdapterLoading()
        if (model.hasNext.not()) {
            unsetEndlessScroll()
        }
        if (model.postList.isNotEmpty()) {
            trackFeedImpression(model.postList)

            adapter.removeEmpty()
            adapter.addList(model.postList)
        }
    }

    private fun onErrorGetNextFeed(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        unsetEndlessScroll()
        onShowRetryGetFeed()
        hideAdapterLoading()
    }

    private fun onSuccessFavoriteUnfavoriteShop(model: FeedPromotedShopViewModel) {
        showSnackbar(model.resultString)
        if (hasFeed())
            updateFavorite()
        else
            updateFavoriteFromEmpty(model.promotedShopViewModel.shop.id)
    }

    private fun onErrorFavoriteUnfavoriteShop(e: Throwable) {
        NetworkErrorHelper.showSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
    }

    private fun onSuccessFollowUnfollowKol(rowNumber: Int) {
        val newList: MutableList<DynamicPostViewModel> = adapter.getlist().copy()
        val (_, _, header) = newList[rowNumber]
        header.followCta.isFollow = !header.followCta.isFollow
        adapter.updateList(newList)
    }

    private fun onErrorFollowUnfollowKol(data: FollowKolViewModel) {
        view?.let {
            Toaster.make(it, data.errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                if (data.status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                    feedViewModel.doUnfollowKol(data.id, data.rowNumber)
                else
                    feedViewModel.doFollowKol(data.id, data.rowNumber)
            })
        }
    }

    private fun onSuccessLikeDislikeKolPost(rowNumber: Int) {
        val newList = adapter.getlist()
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostViewModel) {
            val (_, _, _, _, footer) = newList[rowNumber] as DynamicPostViewModel
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

    private fun onErrorLikeDislikeKolPost(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun onSuccessFollowKolFromRecommendation(data: FollowKolViewModel) {
        if (adapter.getlist()[data.rowNumber] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[data.rowNumber] as FeedRecommendationViewModel
            cards[data.position].cta.isFollow = data.isFollow
            adapter.notifyItemChanged(data.rowNumber, data.position)
        }
    }

    private fun onSuccessDeletePost(rowNumber: Int) {
        val newList: MutableList<DynamicPostViewModel> = adapter.getlist().copy()
        newList.removeAt(rowNumber)
        adapter.updateList(newList)
        view?.let {
            Toaster.make(it, getString(R.string.feed_post_deleted), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.affiliatecommon.R.string.af_title_ok), View.OnClickListener {
                Toaster.snackBar.dismiss()
            })
        }
        if (adapter.getlist().isEmpty()) {
            showRefresh()
            onRefresh()
        }
    }

    private fun onErrorDeletePost(data: DeletePostViewModel) {
        view?.let {
            Toaster.make(it, data.errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                feedViewModel.doDeletePost(data.id, data.rowNumber)
            })
        }
    }

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    private fun onSuccessToggleFavoriteShop(rowNumber: Int, adapterPosition: Int) {
        if (rowNumber < adapter.getlist().size) {
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
            if (adapter.getlist()[rowNumber] is TopadsHeadlineUiModel) {
                val topadsHeadlineUiModel = adapter.getlist()[rowNumber] as TopadsHeadlineUiModel
                topadsHeadlineUiModel.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed?.let {
                    topadsHeadlineUiModel.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed = !it
                }
                adapter.notifyItemChanged(rowNumber)
            }
        }
    }

    private fun onErrorToggleFavoriteShop(data: FavoriteShopViewModel) {
        adapter.notifyItemChanged(data.rowNumber, data.adapterPosition)
        view?.let {
            Toaster.make(it, data.errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                feedViewModel.doToggleFavoriteShop(data.rowNumber, data.adapterPosition, data.shopId)
            })
        }
    }

    private fun sendMoEngageOpenFeedEvent() {
        val isEmptyFeed = !hasFeed()
        val value = DataLayer.mapOf(
                MoEngage.LOGIN_STATUS, userSession.isLoggedIn,
                MoEngage.IS_FEED_EMPTY, isEmptyFeed
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, EventMoEngage.OPEN_FEED)
    }

    private fun stopTracePerformanceMon() {
        performanceMonitoring.stopTrace()
    }

    private fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String) {

    }

    private fun onGoToLink(link: String) {
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

    private fun setLastCursorOnFirstPage(lastCursor: String) {
        activity?.applicationContext?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            cache.putString(KEY_FEED_FIRST_PAGE_LAST_CURSOR, lastCursor)
            cache.applyEditor()
        }
    }

    private fun setFirstPageCursor(firstPageCursor: String) {
        activity?.applicationContext?.let {
            val cache = LocalCacheHandler(it, KEY_FEED)
            cache.putString(KEY_FEED_FIRST_PAGE_CURSOR, firstPageCursor)
            cache.applyEditor()
        }
    }

    private fun sendFeedPlusScreenTracking() {
        if (!isUserEventTrackerDoneTrack) {
            val isEmptyFeed = !hasFeed()

            feedAnalytics.eventOpenFeedPlusFragment(
                    userSession.isLoggedIn,
                    isEmptyFeed
            )

            isUserEventTrackerDoneTrack = true
        }
    }

    private fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }

    private fun showRefresh() {
        if (!swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = true
        }
    }

    private fun updateFavorite() {

    }

    private fun updateFavoriteFromEmpty(shopId: String) {
        onRefresh()
        analytics.eventFeedClickShop(screenName,
                shopId, FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE)
    }

    private fun finishLoading() {
        swipeToRefresh.isRefreshing = false
    }

    private fun showInterestPick() {
        if (context != null && isEnableInterestPick()) {
            RouteManager.route(context, ApplinkConst.INTEREST_PICK)
        }
    }

    private fun isEnableInterestPick(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(requireContext())
        return remoteConfig.getBoolean(REMOTE_CONFIG_ENABLE_INTEREST_PICK, true)
    }

    private fun onShowRetryGetFeed() {
        adapter.showRetry()
    }

    private fun hideAdapterLoading() {
        adapter.removeLoading()
    }

    private fun onShowEmpty() {
        adapter.unsetEndlessScrollListener()
        adapter.showEmpty()
    }

    private fun clearData() {
        adapter.clearData()
    }

    private fun setEndlessScroll() {
        adapter.setEndlessScrollListener()
    }

    private fun unsetEndlessScroll() {
        adapter.unsetEndlessScrollListener()
    }

    private fun hasFeed(): Boolean {
        return (adapter.getlist().isNotEmpty() && adapter.getlist().size > 1 && adapter.getlist()[0] !is EmptyModel)
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
            requireActivity().startActivity(getProductIntent(productId))
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
            requireActivity().startActivity(intent)
        }
    }

    private fun trackFeedImpression(listFeed: List<Visitable<*>>) {
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

                if (visitable.postTag.items.isNotEmpty()) {
                    postTagAnalytics.trackViewPostTagFeed(
                            visitable.id,
                            visitable.postTag.items,
                            visitable.header.followCta.authorType,
                            trackingPostModel)
                }

            } else if (visitable is BannerViewModel) {
                val (itemViewModels) = visitable
                val trackingBannerModels = ArrayList<TrackingBannerModel>()
                for ((_, _, _, trackingBannerModel, _) in itemViewModels) {
                    trackingBannerModels.add(trackingBannerModel)
                }
                analytics.eventBannerImpression(trackingBannerModels, userId)
            } else if (visitable is FeedRecommendationViewModel) {
                val (_, cards) = visitable
                val trackingList = ArrayList<TrackingRecommendationModel>()
                for ((_, _, _, _, _, _, _, _, _, _, trackingRecommendationModel, _) in cards) {
                    trackingList.add(trackingRecommendationModel)
                }
                analytics.eventRecommendationImpression(
                        trackingList,
                        userId
                )
            } else if (visitable is TopadsShopViewModel) {
                val (_, _, _, trackingList, _) = visitable
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

        when {
            postViewModel.contentList[0] is GridPostViewModel -> {
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
                        userIdInt,
                        trackingPostModel.recomId
                )
            }
            postViewModel.contentList[0] is PollContentViewModel -> {
                val (pollId) = postViewModel.contentList[0] as PollContentViewModel
                analytics.eventVoteImpression(
                        trackingPostModel.activityName,
                        trackingPostModel.mediaType,
                        pollId,
                        trackingPostModel.postId,
                        userId
                )
            }
            else -> {
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
                        feedPosition,
                        trackingPostModel.recomId
                )
            }
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
                positionInFeed,
                trackingPostModel.recomId
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
        if (adapter.getlist().size > positionInFeed && positionInFeed >= 0 && adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val trackingPostModel = (adapter.getlist()[positionInFeed] as DynamicPostViewModel).trackingPostModel

            analytics.eventCardPostElementClick(
                    element,
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    trackingPostModel.postId.toString(),
                    trackingPostModel.recomId
            )
        }
    }

    private fun showAfterPostToaster() {
        if (context != null) {
            Toast.makeText(context, R.string.feed_after_post, Toast.LENGTH_LONG).show()
        }
    }

    override fun onTopAdsViewImpression(bannerId: String, imageUrl: String) {
        analytics.eventTopadsRecommendationImpression(
                listOf(TrackingRecommendationModel(authorId = bannerId.toIntOrZero())),
                userIdInt
        )
        feedViewModel.doTopAdsTracker(imageUrl, "", "", "", false)
    }

    private fun updatePlayWidgetTotalView(channelId: String?, totalView: String?) {
        feedViewModel.updatePlayWidgetTotalView(channelId, totalView)
    }

    override fun onFollowClick(positionInFeed: Int, shopId: String) {
        feedViewModel.doToggleFavoriteShop(positionInFeed, 0, shopId)
    }

    override fun urlCreated(linkerShareData: LinkerShareResult?) {
        ShareBottomSheets.newInstance(object : ShareBottomSheets.OnShareItemClickListener {
            override fun onShareItemClicked(packageName: String) {

            }
        }, "", shareData.imgUri, linkerShareData?.url
                ?: "", shareData.description, shareData.name).also {
            fragmentManager?.run {
                it.show(this)
            }
        }
    }

    override fun onError(linkerError: LinkerError?) {

    }
}
