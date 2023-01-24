package com.tokopedia.feedplus.view.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.analytics.tracker.FeedMerchantVoucherAnalyticTracker
import com.tokopedia.feedcomponent.analytics.tracker.FeedTrackerData
import com.tokopedia.feedcomponent.bottomsheets.*
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.mapper.*
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.cordinator.ShopRecomImpressCoordinator
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder.Companion.PAYLOAD_ANIMATE_LIKE
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineListener
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineV2ViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.feedcomponent.view.base.FeedPlusTabParentFragment
import com.tokopedia.feedcomponent.view.share.FeedProductTagSharingHelper
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.shoprecom.FeedShopRecomWidgetAnalytic
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED_FIRST_PAGE_CURSOR
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.KEY_FEED_FIRST_PAGE_LAST_CURSOR
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.di.FeedPlusComponent
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetFeedsAnalyticModel
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.android.synthetic.main.fragment_feed_plus.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import com.tokopedia.wishlist_common.R as Rwishlist

/**
 * @author by nisie on 5/15/17.
 */
@Suppress("LateinitUsage")
class FeedPlusFragment :
    BaseDaggerFragment(),
    SwipeRefreshLayout.OnRefreshListener,
    TopAdsItemClickListener,
    TopAdsInfoClickListener,
    TopadsShopViewHolder.TopadsShopListener,
    CardTitleView.CardTitleListener,
    DynamicPostViewHolder.DynamicPostListener,
    ImagePostViewHolder.ImagePostListener,
    YoutubeViewHolder.YoutubePostListener,
    PollAdapter.PollOptionListener,
    GridPostAdapter.GridItemListener,
    VideoViewHolder.VideoViewListener,
    FeedMultipleImageView.FeedMultipleImageViewListener,
    FeedPlusAdapter.OnLoadListener,
    TopAdsBannerViewHolder.TopAdsBannerListener,
    PlayWidgetListener,
    TopAdsHeadlineListener,
    ShareCallback,
    ProductItemInfoBottomSheet.Listener,
    FeedFollowersOnlyBottomSheet.Listener,
    ShopRecomWidgetCallback,
    FeedPlusTabParentFragment {

    @Suppress("LateinitUsage")
    private lateinit var feedGlobalError: GlobalError

    @Suppress("LateinitUsage")
    private lateinit var feedContainer: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var mainContent: View
    private lateinit var newFeed: View
    private lateinit var newFeedReceiver: BroadcastReceiver
    private lateinit var dynamicPostReceiver: BroadcastReceiver
    private lateinit var adapter: FeedPlusAdapter
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var infoBottomSheet: TopAdsInfoBottomSheet

    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator

    private var layoutManager: LinearLayoutManager? = null
    private var isLoadedOnce: Boolean = false
    private var afterPost: Boolean = false
    private var afterRefresh: Boolean = false

    private var isUserEventTrackerDoneOnResume = false
    private var isFeedPageShown = false
    private var isRefreshForPostCOntentCreation = false

    private lateinit var shareData: LinkerData
    private lateinit var reportBottomSheet: ReportBottomSheet
    private var shareBottomSheetProduct = false
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context)
    }

    private var mContainerListener: FeedPlusContainerListener? = null

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
    lateinit var playWidgetImpressionValidator: DefaultImpressionValidator

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var feedFloatingButtonManager: FeedFloatingButtonManager

    @Inject
    lateinit var feedShopRecomWidgetAnalytics: FeedShopRecomWidgetAnalytic

    @Inject
    lateinit var shopRecomImpression: ShopRecomImpressCoordinator

    private var customMvcTracker: FeedMerchantVoucherAnalyticTracker =
        FeedMerchantVoucherAnalyticTracker()

    private lateinit var productTagBS: ProductItemInfoBottomSheet
    private var feedFollowersOnlyBottomSheet: FeedFollowersOnlyBottomSheet? = null

    private val userIdLong: Long
        get() {
            return userSession.userId.toLongOrZero()
        }

    private val feedProductTagSharingHelper by lazy(LazyThreadSafetyMode.NONE) {
        FeedProductTagSharingHelper(
            fragmentManager = childFragmentManager,
            fragment = this,
            listener = object : FeedProductTagSharingHelper.Listener {
                override fun onErrorCreatingUrl(linkerError: LinkerError?) {
                    showToast(
                        message = linkerError?.errorMessage
                            ?: getString(R.string.default_request_error_unknown),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        )
    }

    companion object {

        private const val OPEN_DETAIL = 54
        private const val OPEN_KOL_COMMENT = 101
        private const val OPEN_FEED_DETAIL = 1011
        private const val OPEN_CONTENT_REPORT = 1310
        private const val DEFAULT_INVALID_POSITION_VALUE = -1
        private const val AUTHOR_USER_TYPE_VALUE = 1
        private const val AUTHOR_SHOP_TYPE_VALUE = 2
        private const val OPEN_PLAY_CHANNEL = 1858
        private const val OPEN_VIDEO_DETAIL = 1311
        const val REQUEST_LOGIN = 345
        private const val KOL_COMMENT_CODE = 13
        private const val TYPE = "text/plain"
        private const val START_TIME = "start_time"
        private const val SHOULD_TRACK = "should_track"
        private const val SOURCE_TYPE = "source_type"
        private const val VOD_POST = "VOD_POST"
        private const val PRODUCT_LIST = "product_list"
        private const val IS_FOLLOWED = "is_followed"
        private const val HAS_VOUCHER = "has_voucher"
        private const val POST_TYPE = "post_type"
        private const val SHOP_NAME = "shop_name"
        private const val PARAM_SALE_TYPE = "sale_type"
        private const val PARAM_SALE_STATUS = "sale_status"
        private const val PARAM_AUTHOR_TYPE = "author_type"
        const val PARAM_CONTENT_SLOT_VALUE = "content_slot_value"

        private const val TYPE_LONG_VIDEO: String = "long-video"

        private const val YOUTUBE_URL = "{youtube_url}"
        private const val FEED_TRACE = "mp_feed"
        private const val AFTER_POST = "after_post"
        private const val TRUE = "true"
        private const val FEED_DETAIL = "feedcommunicationdetail"
        private const val BROADCAST_FEED = "BROADCAST_FEED"
        private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"
        private const val PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED"
        private const val PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED"
        private const val PARAM_POST_POSITION = "position"
        private const val PARAM_COMMENT_COUNT = "comment_count"
        private const val PARAM_LIKE_COUNT = "like_count"

        private const val PARAM_CALL_SOURCE = "call_source"
        private const val PARAM_FEED = "feed"

        private const val PARAM_VIDEO_INDEX = "video_index"
        private const val PARAM_VIDEO_AUTHOR_TYPE = "video_author_type"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_ACTIVITY_ID = "activity_id"
        const val PARAM_POST_TYPE = "POST_TYPE"
        const val PARAM_IS_POST_FOLLOWED = "IS_FOLLOWED"
        const val PARAM_START_TIME = "START_TIME"

        //region Content Report Param
        private const val CONTENT_REPORT_RESULT_SUCCESS = "result_success"
        private const val CONTENT_REPORT_RESULT_ERROR_MSG = "error_msg"
        //endregion

        //region Media Preview Param
        private const val MEDIA_PREVIEW_INDEX = "media_index"
        //endregion

        //region Kol Comment Param
        private const val ARGS_AUTHOR_TYPE = "ARGS_AUTHOR_TYPE"
        private const val ARGS_VIDEO = "ARGS_VIDEO"
        const val ARGS_POST_TYPE = "POST_TYPE"
        const val ARGS_IS_POST_FOLLOWED = "IS_FOLLOWED"
        private const val COMMENT_ARGS_POSITION = "ARGS_POSITION"
        private const val COMMENT_ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT"
        private const val COMMENT_ARGS_POSITION_COLUMN = "ARGS_POSITION_COLUMN"
        private const val COMMENT_ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG"
        //endregion

        private const val EXTRA_PLAY_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_PLAY_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

        const val CLICK_FOLLOW_TOPADS = "click - follow - topads"
        const val IMPRESSION_CARD_TOPADS = "impression - card - topads"
        const val IMPRESSION_PRODUCT_TOPADS = "impression - product - topads"
        const val CLICK_CEK_SEKARANG = "click - cek sekarang - topads"
        const val CLICK_SHOP_TOPADS = "click - shop - topads"
        const val CLICK_PRODUCT_TOPADS = "click - product - topads"
        const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"

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
        performanceMonitoring = PerformanceMonitoring.start(FEED_TRACE)
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            feedViewModel = viewModelProvider.get(FeedViewModel::class.java)
        }
        initVar()
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        feedViewModel.run {
            getFeedFirstPageResp.observe(
                lifecycleOwner,
                Observer {
                    finishLoading()
                    when (it) {
                        is Success -> onSuccessGetFirstFeed(it.data)
                        is Fail -> {
                            when (it.throwable) {
                                is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                    view?.let {
                                        showNoInterNetDialog(it.context)
                                    }
                                }
                                else -> {
                                    onErrorGetFirstFeed(it.throwable)
                                }
                            }
                        }
                    }
                }
            )

            getFeedNextPageResp.observe(
                lifecycleOwner,
                Observer {
                    hideAdapterLoading()
                    when (it) {
                        is Success -> onSuccessGetNextFeed(it.data)
                        is Fail -> onErrorGetNextFeed(it.throwable)
                    }
                }
            )

            doFavoriteShopResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val stringBuilder = StringBuilder()
                            val data = it.data
                            if (data.isSuccess) {
                                stringBuilder.append(MethodChecker.fromHtml(data.promotedShopViewModel.shop.name))
                                    .append(" ")
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
                }
            )

            followKolResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val data = it.data
                            if (data.isSuccess) {
                                if (data.isFollowedFromFollowRestrictionBottomSheet && data.isFollow) {
                                    if (::productTagBS.isInitialized) {
                                        productTagBS.showToasterOnBottomSheetOnSuccessFollow(
                                            getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_success_toaster_text),
                                            Toaster.TYPE_NORMAL,
                                            getString(com.tokopedia.feedcomponent.R.string.feed_asgc_campaign_toaster_action_text)
                                        )
                                        if (feedFollowersOnlyBottomSheet?.isAdded == true && feedFollowersOnlyBottomSheet?.isVisible == true) {
                                            feedFollowersOnlyBottomSheet?.dismiss()
                                        }
                                    }
                                } else {
                                    val messageRes =
                                        if (data.isFollow) com.tokopedia.feedcomponent.R.string.feed_component_follow_success_toast else com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast
                                    showToast(
                                        getString(messageRes),
                                        Toaster.TYPE_NORMAL
                                    )
                                }
                                onSuccessFollowUnfollowKol(data.rowNumber)
                            } else {
                                if (data.isFollow) {
                                    data.errorMessage =
                                        getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_fail_toast)
                                } else {
                                    data.errorMessage =
                                        getString(com.tokopedia.feedcomponent.R.string.feed_component_follow_fail_toast)
                                }
                                onErrorFollowUnfollowKol(data)
                            }
                        }
                        is Fail -> {
                            val message = it.throwable.message
                                ?: getString(R.string.default_request_error_unknown)
                            showToast(message, Toaster.TYPE_ERROR)
                        }
                    }
                }
            )

            likeKolResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val data = it.data
                            if (data.isSuccess) {
                                onSuccessLikeDislikeKolPost(data.rowNumber)
                            } else {
                                data.errorMessage = getString(R.string.feed_like_error_message)
                                onErrorLikeDislikeKolPost(data.errorMessage)
                            }
                        }
                        is Fail -> {
                            when (it.throwable.cause) {
                                is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                    view?.let {
                                        showNoInterNetDialog(it.context)
                                    }
                                }
                                else -> {
                                    val message = getString(R.string.feed_like_error_message)
                                    showToast(message, Toaster.TYPE_ERROR)
                                }
                            }
                        }
                    }
                }
            )

            deletePostResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val data = it.data
                            if (data.isSuccess) {
                                onSuccessDeletePost(data.rowNumber)
                            } else {
                                data.errorMessage =
                                    getString(R.string.default_request_error_unknown)
                                onErrorDeletePost(data)
                            }
                        }
                        is Fail -> {
                            val message = getString(R.string.default_request_error_unknown)
                            showToast(message, Toaster.TYPE_ERROR)
                        }
                    }
                }
            )

            atcResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val data = it.data
                            when {
                                data.isSuccess -> {
                                    Toaster.build(
                                        requireView(),
                                        getString(R.string.feed_added_to_cart),
                                        Toaster.LENGTH_LONG,
                                        Toaster.TYPE_NORMAL,
                                        getString(R.string.feed_go_to_cart),
                                        View.OnClickListener {
                                            onAddToCartSuccess()
                                        }
                                    ).show()
                                }
                                data.errorMsg.isNotEmpty() -> {
                                    showToast(data.errorMsg, Toaster.TYPE_ERROR)
                                }
                                else -> {
                                    onAddToCartFailed(data.applink)
                                }
                            }
                        }
                        is Fail -> {
                            Timber.e(it.throwable)
                            showToast(
                                it.throwable.message
                                    ?: getString(R.string.default_request_error_unknown),
                                Toaster.TYPE_ERROR
                            )
                        }
                    }
                }
            )

            toggleFavoriteShopResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val data = it.data
                            if (data.isSuccess) {
                                if (data.isFollowedFromFollowRestrictionBottomSheet) {
                                    if (::productTagBS.isInitialized) {
                                        productTagBS.showToasterOnBottomSheetOnSuccessFollow(
                                            getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_success_toaster_text),
                                            Toaster.TYPE_NORMAL,
                                            getString(com.tokopedia.feedcomponent.R.string.feed_asgc_campaign_toaster_action_text)
                                        )
                                        if (feedFollowersOnlyBottomSheet?.isAdded == true && feedFollowersOnlyBottomSheet?.isVisible == true) {
                                            feedFollowersOnlyBottomSheet?.dismiss()
                                        }
                                    }
                                }
                                onSuccessToggleFavoriteShop(data)
                            } else {
                                data.errorMessage =
                                    ErrorHandler.getErrorMessage(context, RuntimeException())
                                onErrorToggleFavoriteShop(data)
                            }
                        }
                        is Fail -> {
                            when (it.throwable.cause) {
                                is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                    view?.let {
                                        showNoInterNetDialog(it.context)
                                    }
                                }
                                else -> {
                                    it.throwable.let { throwable ->
                                        val message = if (throwable is CustomUiMessageThrowable) {
                                            context?.getString(throwable.errorMessageId)
                                        } else {
                                            it.throwable.message
                                                ?: getString(R.string.default_request_error_unknown)
                                        }
                                        message?.let { errormessage ->
                                            showToast(errormessage, Toaster.TYPE_ERROR)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )

            trackAffiliateResp.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Fail -> {
                            val message = it.throwable.localizedMessage ?: ""
                            showToast(message, Toaster.TYPE_ERROR)
                        }
                    }
                }
            )

            playWidgetModel.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Fail -> adapter.removePlayWidget()
                        is Success -> {
                            if (!it.data.isAutoRefresh) playWidgetImpressionValidator.invalidate()
                            adapter.updatePlayWidget(it.data)
                        }
                    }
                }
            )

            asgcReminderButtonStatus.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Fail -> {
                            showToast(
                                ErrorHandler.getErrorMessage(context, it.throwable),
                                Toaster.TYPE_ERROR
                            )
                        }
                        is Success -> {
                            onSuccessFetchStatusCampaignReminderButton(it.data, true)
                        }
                    }
                }
            )

            feedWidgetLatestData.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onSuccessFetchLatestFeedWidgetData(it.data.feedXCard, it.data.rowNumber)
                        }
                    }
                }
            )

            asgcReminderButtonInitialStatus.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onSuccessFetchStatusCampaignReminderButton(it.data)
                        }
                    }
                }
            )

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                shopRecom.collectLatest {
                    if (it.shopRecomUiModel.items.isEmpty()) {
                        adapter.removeShopRecomWidget()
                        return@collectLatest
                    }
                    if (it.onError.isNotEmpty()) {
                        showToast(
                            message = it.onError,
                            type = Toaster.TYPE_ERROR
                        )
                    }
                    adapter.updateShopRecomWidget(it)
                }
            }

            viewTrackResponse.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onSuccessAddViewVODPost(it.data.rowNumber)
                        }
                    }
                }
            )
            longVideoViewTrackResponse.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onSuccessAddViewVODPost(it.data.rowNumber)
                        }
                    }
                }
            )

            reportResponse.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Fail -> {
                            when (it.throwable) {
                                is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                    view?.let {
                                        reportBottomSheet.dismiss()
                                        showNoInterNetDialog(it.context)
                                    }
                                }
                                else -> {
                                    val message = it.throwable.localizedMessage ?: ""
                                    showToast(message, Toaster.TYPE_ERROR)
                                }
                            }
                        }
                        is Success -> {
                            reportBottomSheet.setFinalView()
                            onSuccessDeletePost(it.data.rowNumber)
                        }
                    }
                }
            )

            shopIdsFollowStatusToUpdateData.observe(
                lifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            val indexNeedToBeChanged = mutableSetOf<Int>()

                            adapter.getlist().mapIndexed { index, item ->
                                if (item is ShopRecomWidgetModel) {
                                    val newItems = item.shopRecomUiModel.items.toList()
                                    var isChanged = false

                                    newItems.map { recomItem ->
                                        it.data[recomItem.id.toString()]?.let { followStatus ->
                                            recomItem.state =
                                                if (followStatus) ShopRecomFollowState.FOLLOW else ShopRecomFollowState.UNFOLLOW
                                            indexNeedToBeChanged.add(index)
                                            isChanged = true
                                        }
                                    }

                                    if (isChanged) {
                                        val newItem = item.copy(
                                            item.shopRecomUiModel.copy(
                                                isShown = item.shopRecomUiModel.isShown,
                                                nextCursor = item.shopRecomUiModel.nextCursor,
                                                title = item.shopRecomUiModel.title,
                                                loadNextPage = item.shopRecomUiModel.loadNextPage,
                                                items = newItems,
                                                isRefresh = item.shopRecomUiModel.isRefresh
                                            ),
                                            ""
                                        )
                                        adapter.updateShopRecomWidget(newItem)
                                    }

                                } else {
                                    val shopId = when (item) {
                                        is DynamicPostModel -> {
                                            item.header.followCta.authorID
                                        }
                                        is DynamicPostUiModel -> {
                                            item.feedXCard.author.id
                                        }
                                        else -> ""
                                    }
                                    if (shopId != "") {
                                        it.data[shopId]?.let { followStatus ->
                                            when (item) {
                                                is DynamicPostModel -> {
                                                    item.header.followCta.isFollow = followStatus
                                                    indexNeedToBeChanged.add(index)
                                                }
                                                is DynamicPostUiModel -> {
                                                    item.feedXCard.followers.isFollowed =
                                                        followStatus
                                                    indexNeedToBeChanged.add(index)
                                                }
                                                else -> {
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (indexNeedToBeChanged.isNotEmpty()) {
                                val indexSorted = indexNeedToBeChanged.sorted()
                                val count = indexSorted[indexSorted.size - 1] - indexSorted[0]
                                adapter.notifyItemRangeChanged(
                                    indexSorted[0],
                                    count,
                                    DynamicPostNewViewHolder.PAYLOAD_ANIMATE_FOLLOW
                                )
                            }

                            feedViewModel.clearFollowIdToUpdate()
                        }
                        is Fail -> {}
                    }
                }
            )
        }
    }

    private fun initVar() {
        playWidgetCoordinator =
            PlayWidgetCoordinator(this, autoHandleLifecycleMethod = false).apply {
                setListener(this@FeedPlusFragment)
                setAnalyticModel(PlayWidgetFeedsAnalyticModel())
                setImpressionHelper(ImpressionHelper(validator = playWidgetImpressionValidator))
            }
        val typeFactory = FeedPlusTypeFactoryImpl(this, userSession, playWidgetCoordinator)
        adapter = FeedPlusAdapter(typeFactory, this)

        newFeedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    if (intent.action == BROADCAST_FEED) {
                        val isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false)
                        if (intent.extras == null || intent.extras?.isEmpty == true) {
                            isFeedPageShown = true
                        }
                        if (isHaveNewFeed) {
                            newFeed.visible()
                            triggerNewFeedNotification()
                        }
                    } else if (intent.action == BROADCAST_VISIBLITY) {
                        resetImagePostWhenFragmentNotVisible()
                        isFeedPageShown = false
                    }
                }
            }
        }
        dynamicPostReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.action?.let {
                    updateDynamicPostListVisibility(it)
                }
            }
        }
        registerNewFeedReceiver()

        arguments?.run {
            afterPost = TextUtils.equals(getString(AFTER_POST, ""), TRUE)
        }
    }

    fun updateDynamicPostListVisibility(action: String) = when (action) {
        BROADCAST_VISIBLITY, BROADCAST_FEED -> {
            // Change the adapter value so the next time item is bind, it will reflect this change.
            adapter.broadcastValueForDynamicPost = action
            try {
                // loop all current child in recyclerview and change all dynamic video
                // correspond to broadcast action. This is behaves the same way as notifyItemChange
                val childCount = recyclerView.childCount
                var i = 0
                while (i < childCount) {
                    val holder: RecyclerView.ViewHolder =
                        recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
                    if (holder is DynamicPostNewViewHolder) {
                        holder.setPostDynamicView(action)
                    }
                    ++i
                }
            } catch (ignored: Exception) {
            }
        }
        else -> {}
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
        val isNotEmpty = adapter.getlist().isNotEmpty()
        if (isNotEmpty && adapter.getlist()[0] !is EmptyModel) feedViewModel.getFeedNextPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false)
        feedGlobalError = parentView.findViewById(R.id.feed_plus_global_error)
        feedContainer = parentView.findViewById(R.id.feed_plus_container)
        recyclerView = parentView.findViewById(R.id.recycler_view)
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_layout)
        mainContent = parentView.findViewById(R.id.main)
        newFeed = parentView.findViewById(R.id.layout_new_feed)
        prepareView()
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRefresh()

        recyclerView.addOnScrollListener(feedFloatingButtonManager.scrollListener)
        feedFloatingButtonManager.setDelayForExpandFab(recyclerView)
    }

    private fun prepareView() {
        adapter.itemTreshold = 1
        layoutManager =
            NpaLinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        swipeToRefresh.setOnRefreshListener(this)
        context?.let {
            infoBottomSheet = TopAdsInfoBottomSheet.newInstance(it)
        }
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
                    if (hasFeed() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        FeedScrollListenerNew.onFeedScrolled(
                            recyclerView,
                            adapter.getList()
                        )
                    }
                } catch (e: Exception) {
                }
            }
        })

        feedFloatingButtonManager.setInitialData(parentFragment)
    }

    private fun sendNewFeedClickEvent() {
        analytics.eventNewPostClick()
    }

    private fun itemIsFullScreen(): Boolean {
        return layoutManager?.findLastVisibleItemPosition() == layoutManager?.findFirstVisibleItemPosition()
    }

    override fun onRefresh() {
        newFeed.visibility = View.GONE
        hideAdapterLoading()
        fetchFirstPage()
        afterRefresh = true
        TopAdsHeadlineActivityCounter.page = 1

        mContainerListener?.onChildRefresh()
    }

    fun onRefreshForNewPostUpdated() {
        feedViewModel.getFeedFirstPage()
        isRefreshForPostCOntentCreation = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
        if (layoutManager != null) {
            layoutManager = null
        }
        if (::productTagBS.isInitialized) {
            productTagBS.onDestroy()
        }

        TopAdsHeadlineActivityCounter.page = 1
        Toaster.onCTAClick = View.OnClickListener { }
        feedFloatingButtonManager.cancel()
        recyclerView.clearOnScrollListeners()
    }

    override fun onInfoClicked() {
        infoBottomSheet.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {
            OPEN_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                showSnackbar(data.getStringExtra("message") ?: "")
            }
            OPEN_KOL_COMMENT -> if (resultCode == Activity.RESULT_OK) {
                val serverErrorMsg = data.getStringExtra(COMMENT_ARGS_SERVER_ERROR_MSG)
                if (!TextUtils.isEmpty(serverErrorMsg)) {
                    view?.let {
                        Toaster.build(
                            it,
                            serverErrorMsg ?: "",
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(R.string.cta_refresh_feed),
                            View.OnClickListener { onRefresh() }
                        )
                    }
                } else {
                    onSuccessAddDeleteKolComment(
                        data.getIntExtra(COMMENT_ARGS_POSITION, DEFAULT_INVALID_POSITION_VALUE),
                        data.getIntExtra(COMMENT_ARGS_TOTAL_COMMENT, 0)
                    )
                }
            }
            OPEN_FEED_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(IS_FOLLOWED, false)) {
                    val authorType = data.getStringExtra(PARAM_AUTHOR_TYPE)
                    val rowNumber =
                        data.getIntExtra(PARAM_POST_POSITION, DEFAULT_INVALID_POSITION_VALUE)
                    if (rowNumber in 0 until adapter.getList().size) {
                        if (authorType == FollowCta.AUTHOR_USER || authorType == FollowCta.AUTHOR_UGC) {
                            onSuccessFollowUnfollowKol(rowNumber)
                        } else if (authorType == FollowCta.AUTHOR_SHOP) {
                            onSuccessToggleFavoriteShop(
                                FavoriteShopModel(
                                    rowNumber = rowNumber
                                )
                            )
                        }
                    }
                }
            }
            OPEN_CONTENT_REPORT -> if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(CONTENT_REPORT_RESULT_SUCCESS, false)) {
                    onSuccessReportContent()
                } else {
                    onErrorReportContent(
                        data.getStringExtra(CONTENT_REPORT_RESULT_ERROR_MSG) ?: ""
                    )
                }
            }
            OPEN_PLAY_CHANNEL -> {
                val channelId = data.getStringExtra(EXTRA_PLAY_CHANNEL_ID)
                val totalView = data.getStringExtra(EXTRA_PLAY_TOTAL_VIEW)
                updatePlayWidgetTotalView(channelId, totalView)
            }
            OPEN_VIDEO_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    val positionInFeed = data.getIntExtra(PARAM_POST_POSITION, 0)
                    val totalComment = data.getIntExtra(PARAM_COMMENT_COUNT, 0)
                    val changeLike = data.getBooleanExtra(PARAM_LIKE_COUNT, true)
                    val newList = adapter.getlist()
                    if (newList.size > positionInFeed && newList[positionInFeed] is DynamicPostUiModel) {
                        val item = (newList[positionInFeed] as DynamicPostUiModel)
                        item.feedXCard.comments.count = totalComment
                        item.feedXCard.comments.countFmt = totalComment.toString()
                        if (changeLike != item.feedXCard.like.isLiked) {
                            onSuccessLikeDislikeKolPost(positionInFeed)
                        }
                    }
                    adapter.notifyItemChanged(
                        positionInFeed,
                        DynamicPostNewViewHolder.PAYLOAD_COMMENT
                    )
                }
            }
            else -> {
            }
        }
    }

    override fun onProductItemClicked(position: Int, product: Product) {
        goToProductDetail(product.id)

        analytics.eventFeedClickProduct(
            screenName,
            product.id,
            FeedTrackingEventLabel.Click.TOP_ADS_PRODUCT
        )

        val listTopAds = ArrayList<FeedEnhancedTracking.Promotion>()

        listTopAds.add(
            FeedEnhancedTracking.Promotion(
                id = product.adId,
                name = FeedEnhancedTracking.Promotion
                    .createContentNameTopadsProduct(),
                creative = if (TextUtils.isEmpty(product.adRefKey)) {
                    FeedEnhancedTracking.Promotion.TRACKING_NONE
                } else {
                    product.adRefKey
                },
                position = position,
                creativeUrl = product.category.toString(),
                promoId = product.id,
                promoCode = FeedEnhancedTracking.Promotion.TRACKING_EMPTY
            )
        )
        analytics.eventTrackingEnhancedEcommerce(
            FeedEnhancedTracking.getClickTracking(listTopAds, userIdLong) as HashMap<String, Any>
        )
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {
        goToShopPage(shop.id)
        analytics.eventFeedClickShop(
            screenName,
            shop.id,
            FeedTrackingEventLabel.Click.TOP_ADS_SHOP
        )

        val listTopAds = ArrayList<FeedEnhancedTracking.Promotion>()

        listTopAds.add(
            FeedEnhancedTracking.Promotion(
                id = shop.adId,
                name = FeedEnhancedTracking.Promotion
                    .createContentNameTopadsShop(),
                creative = shop.adRefKey,
                position = position,
                category = FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                promoId = shop.adId,
                promoCode = FeedEnhancedTracking.Promotion.TRACKING_EMPTY
            )
        )

        analytics.eventTrackingEnhancedEcommerce(
            FeedEnhancedTracking.getClickTracking(listTopAds, userIdLong) as HashMap<String, Any>
        )
    }

    override fun onAddFavorite(position: Int, dataShop: Data) {
        feedViewModel.doFavoriteShop(dataShop, position)
        analytics.eventFeedClickShop(
            screenName,
            dataShop.shop.id,
            FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE
        )
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
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .sendBroadcast(intent)
        }
    }

    private fun triggerClearNewFeedNotification() {
        if (context?.applicationContext != null) {
            val intent = Intent(BROADCAST_FEED)
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, false)
            LocalBroadcastManager.getInstance(requireContext().applicationContext)
                .sendBroadcast(intent)
        }
    }

    override fun onResume() {
        if (isUserEventTrackerDoneOnResume) {
            isUserEventTrackerDoneOnResume = false
        }
        feedViewModel.updateFollowStatus()
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        registerNewFeedReceiver()
        registerDynamicPostReceiver()
        if (userVisibleHint) {
            loadData(userVisibleHint)
        }
    }

    private fun registerDynamicPostReceiver() {
        if (activity != null) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_VISIBLITY)
            intentFilter.addAction(BROADCAST_FEED)
            LocalBroadcastManager
                .getInstance(requireActivity())
                .registerReceiver(dynamicPostReceiver, intentFilter)
        }
    }

    fun updateFeedVisibilityVariable(isFeedShown: Boolean) {
        this.isFeedPageShown = isFeedShown
        resetVODWhenFeedTabChanged()
    }

    override fun onPause() {
        if (isFeedPageShown) {
            isUserEventTrackerDoneOnResume = true
        }
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        unRegisterNewFeedReceiver()
        unRegisterDynamicPostReceiver()
        analytics.sendPendingAnalytics()
        feedAnalytics.sendPendingAnalytics()
        shopRecomImpression.sendTracker { feedShopRecomWidgetAnalytics.sendPendingAnalytics() }
    }

    private fun resetImagePostWhenFragmentNotVisible() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
        val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
        for (i in firstPosition..lastPosition) {
            val item = getCardViewModel(adapter.getList(), i)
            val topadsItem = getTopadsCardViewModel(adapter.getList(), i)
            if (isImageCard(adapter.getList(), i)) {
                if (item != null) {
                    Objects.requireNonNull(adapter)
                        .notifyItemChanged(i, DynamicPostNewViewHolder.PAYLOAD_POST_VISIBLE)
                }
            } else if (isTopadsImageCard(adapter.getList(), i)) {
                if (topadsItem != null) {
                    Objects.requireNonNull(adapter)
                        .notifyItemChanged(i, TopAdsHeadlineV2ViewHolder.PAYLOAD_POST_VISIBLE)
                }
            }
        }
    }

    private fun resetVODWhenFeedTabChanged() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
        val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
        for (i in firstPosition..lastPosition) {
            val item = getCardViewModel(adapter.getList(), i)
            if (isVOD(adapter.getList(), i)) {
                if (item != null) {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
                    if (viewHolder is DynamicPostNewViewHolder) {
                        (viewHolder as DynamicPostNewViewHolder).setPostDynamicView(if (!isFeedPageShown) BROADCAST_VISIBLITY else "")
                    }
                }
            }
        }
    }

    private fun getCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            if (position in 0 until (list.size) && list[position] is DynamicPostUiModel) {
                return (list[position] as DynamicPostUiModel).feedXCard.media.firstOrNull()
            }
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
        return null
    }

    private fun isImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        if (position in 0 until (list.size) && list[position] is DynamicPostUiModel) {
            val item = (list[position] as DynamicPostUiModel).feedXCard
            return (
                item.typename == TYPE_FEED_X_CARD_POST &&
                    (
                        item.media.isNotEmpty() &&
                            (item.media.find { it.type == TYPE_IMAGE } != null)
                        )
                )
        }
        return false
    }

    private fun isVOD(list: List<Visitable<*>>, position: Int): Boolean {
        if (position in 0 until (list.size) && list[position] is DynamicPostUiModel) {
            val item = (list[position] as DynamicPostUiModel).feedXCard
            return (item.typename == TYPE_FEED_X_CARD_PLAY)
        }
        return false
    }

    private fun getTopadsCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            if (position in 0 until (list.size) && list[position] is TopadsHeadLineV2Model) {
                return (list[position] as TopadsHeadLineV2Model).feedXCard.media.firstOrNull()
            }
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
        return null
    }

    private fun isTopadsImageCard(list: List<Visitable<*>>, position: Int): Boolean {
        if (position in 0 until (list.size) && list[position] is TopadsHeadLineV2Model) {
            val item = (list[position] as TopadsHeadLineV2Model).feedXCard
            return (
                item.typename == TYPE_TOPADS_HEADLINE_NEW &&
                    (
                        item.media.isNotEmpty() &&
                            (item.media.find { it.type == TYPE_IMAGE } != null)
                        )
                )
        }
        return false
    }

    private fun registerNewFeedReceiver() {
        if (activity != null && requireActivity().applicationContext != null) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_FEED)
            intentFilter.addAction(BROADCAST_VISIBLITY)

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

    private fun unRegisterDynamicPostReceiver() {
        if (activity != null) {
            LocalBroadcastManager
                .getInstance(requireActivity().applicationContext)
                .unregisterReceiver(dynamicPostReceiver)
        }
    }

    private fun loadData(isVisibleToUser: Boolean) {
        activity?.let {
            if (isVisibleToUser && isAdded) {
                if (!isLoadedOnce) {
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

    private fun getCurrentPosition(): Int {
        val position: Int
        when {
            itemIsFullScreen() -> {
                position = layoutManager?.findLastVisibleItemPosition() ?: 0
            }
            layoutManager?.findFirstCompletelyVisibleItemPosition() != -1 -> {
                position =
                    layoutManager?.findFirstCompletelyVisibleItemPosition()
                        ?: 0
            }
            layoutManager?.findLastCompletelyVisibleItemPosition() != -1 -> {
                position =
                    layoutManager?.findLastCompletelyVisibleItemPosition()
                        ?: 0
            }
            else -> {
                position = layoutManager?.findLastVisibleItemPosition() ?: 0
            }
        }
        return position
    }

    private fun onFollowKolClicked(
        rowNumber: Int,
        id: String,
        isFollowedFromFollowRestrictionBottomSheet: Boolean
    ) {
        if (userSession.isLoggedIn) {
            feedViewModel.doFollowKol(id, rowNumber, isFollowedFromFollowRestrictionBottomSheet)
        } else {
            onGoToLogin()
        }
    }

    private fun onUnfollowKolClicked(rowNumber: Int, id: String) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnfollowKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    private fun onLikeKolClicked(rowNumber: Int, id: Long) {
        if (userSession.isLoggedIn) {
            feedViewModel.doLikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    private fun onUnlikeKolClicked(
        rowNumber: Int,
        id: Long
    ) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnlikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    private fun goToKolComment(
        rowNumber: Int,
        id: String,
        authorType: String,
        isVideo: Boolean,
        isFollowed: Boolean,
        type: String
    ) {
        val intent = RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT_NEW,
                mapOf(
                    COMMENT_ARGS_POSITION to rowNumber.toString()
                )
            ),
            id
        )
        intent.putExtra(ARGS_AUTHOR_TYPE, authorType)
        intent.putExtra(ARGS_VIDEO, isVideo)
        intent.putExtra(ARGS_POST_TYPE, type)
        intent.putExtra(ARGS_IS_POST_FOLLOWED, isFollowed)
        startActivityForResult(intent, OPEN_KOL_COMMENT)
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
        isViewResumed: Boolean = if (view == null) {
            false
        } else {
            viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                Lifecycle.State.RESUMED
            )
        },
        isUserVisibleHint: Boolean = userVisibleHint,
        isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) {
                playWidgetCoordinator.onResume()
            } else {
                playWidgetCoordinator.onPause()
            }
        }
    }

    private fun createDeleteDialog(
        rowNumber: Int,
        id: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        isVideo: Boolean,
        authorType: String
    ) {
        val dialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.feed_delete_post))
        dialog.setDescription(getString(R.string.feed_after_delete_cant))
        dialog.setPrimaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
        dialog.setSecondaryCTAText(getString(R.string.feed_delete))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            feedAnalytics.clickDeleteConfirmThreeDotsPage(
                id,
                shopId,
                type,
                isFollowed,
                isVideo,
                authorType
            )
            feedViewModel.doDeletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        val newList = adapter.getlist()
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostUiModel) {
            val item = (newList[rowNumber] as DynamicPostUiModel)
            item.feedXCard.comments.count = totalNewComment
            item.feedXCard.comments.countFmt =
                (item.feedXCard.comments.count).toString()
        }
        adapter.notifyItemChanged(rowNumber, DynamicPostNewViewHolder.PAYLOAD_COMMENT)
    }

    private fun onSuccessReportContent() {
        view?.let {
            showToast(
                getString(R.string.feed_content_reported),
                Toaster.TYPE_NORMAL,
                getString(R.string.label_close)
            )
        }
    }

    private fun onErrorReportContent(errorMsg: String) {
        showToast(errorMsg, Toaster.TYPE_ERROR, getString(R.string.label_close))
    }

    private fun onGoToLogin() {
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

    private fun visitShopPageWithAnalytics(positionInFeed: Int, shop: Shop) {
        goToShopPage(shop.id)
        if (adapter.getlist()[positionInFeed] is TopadsShopUiModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopUiModel
            for ((templateType, _, _, _, authorName, _, authorId, cardPosition, adId) in trackingList) {
                if (TextUtils.equals(authorName, shop.name)) {
                    analytics.eventTopadsRecommendationClick(
                        templateType,
                        adId,
                        authorId,
                        cardPosition,
                        userIdLong
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

    override fun onTopAdsImpression(
        url: String,
        shopId: String,
        shopName: String,
        imageUrl: String
    ) {
        feedViewModel.doTopAdsTracker(url, shopId, shopName, imageUrl, false)
    }

    private fun trackShopClickImpression(positionInFeed: Int, adapterPosition: Int, shop: Shop) {
        if (adapter.getlist()[positionInFeed] is TopadsShopUiModel) {
            val (_, dataList, _, _) = adapter.getlist()[positionInFeed] as TopadsShopUiModel
            if (adapterPosition != RecyclerView.NO_POSITION) {
                feedViewModel.doTopAdsTracker(
                    dataList[adapterPosition].shopClickUrl,
                    shop.id,
                    shop.name,
                    dataList[adapterPosition].shop.imageShop.xsEcs,
                    true
                )
            }
        }
    }

    private fun trackUrlEvent(url: String) {
        feedViewModel.doTrackAffiliate(url)
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: Data) {
        feedViewModel.doToggleFavoriteShop(positionInFeed, adapterPosition, data.shop.id)

        if (adapter.getlist()[positionInFeed] is TopadsShopUiModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopUiModel

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
        onGoToLink(redirectUrl)
        val trackerId = if (isCaption) {
            getTrackerIdForCampaignSaleTracker(
                positionInFeed,
                trackerIdAsgc = "17981",
                trackerIdAsgcRecom = "40070"
            )
        } else {
            getTrackerIdForCampaignSaleTracker(
                positionInFeed,
                trackerIdAsgc = "13438",
                trackerIdAsgcRecom = "13422"
            )
        }
        val feedTrackerData = getFeedTrackerDataModelFromPosition(
            positionInFeed,
            trackerId = trackerId
        )
        feedTrackerData?.let {
            feedAnalytics.eventClickFeedAvatar(
                it,
                isCaption
            )
        }
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
        if (userSession.isLoggedIn) {
            if (type == FollowCta.AUTHOR_USER || type == FollowCta.AUTHOR_UGC) {
                if (isFollow) {
                    onUnfollowKolClicked(positionInFeed, id)
                } else {
                    onFollowKolClicked(
                        positionInFeed,
                        id,
                        isFollowedFromFollowRestrictionBottomSheet
                    )
                }
            } else if (type == FollowCta.AUTHOR_SHOP) {
                feedViewModel.doToggleFavoriteShop(
                    positionInFeed,
                    0,
                    id,
                    isFollow,
                    isFollowedFromFollowRestrictionBottomSheet
                )
            }

            if (adapter.getlist()[positionInFeed] is DynamicPostModel) {
                val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
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

    override fun onMenuClick(
        positionInFeed: Int,
        postId: String,
        reportable: Boolean,
        deletable: Boolean,
        editable: Boolean,
        isFollowed: Boolean,
        authorId: String,
        authorType: String,
        postType: String,
        mediaType: String,
        caption: String,
        playChannelId: String
    ) {
        if (context != null) {
            val finalId =
                if (postType == TYPE_FEED_X_CARD_PLAY) playChannelId else postId
            val trackerId =
                getTrackerIdForCampaignSaleTracker(
                    positionInFeed,
                    trackerIdAsgc = "13452",
                    trackerIdAsgcRecom = "40065"
                )
            getFeedTrackerDataModelFromPosition(positionInFeed, trackerId)?.let {
                feedAnalytics.evenClickMenu(
                    it
                )
            }

            val sheet = MenuOptionsBottomSheet.newInstance(
                reportable,
                isFollowed,
                deletable,
                isEditable = postType != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
            )
            sheet.show((context as FragmentActivity).supportFragmentManager, "")
            sheet.onReport = {
                val trackerIdReport =
                    getTrackerIdForCampaignSaleTracker(
                        positionInFeed,
                        trackerIdAsgc = "17985",
                        trackerIdAsgcRecom = "40066"
                    )
                feedAnalytics.eventClickThreeDotsOption(
                    finalId,
                    "laporkan",
                    postType,
                    isFollowed,
                    authorId,
                    mediaType,
                    trackerId = trackerIdReport,
                    campaignStatus = getTrackerLabelSuffixFromPosition(positionInFeed),
                    authorType
                )
                if (userSession.isLoggedIn) {
                    context?.let {
                        reportBottomSheet = ReportBottomSheet.newInstance(
                            context = object : ReportBottomSheet.OnReportOptionsClick {
                                override fun onReportAction(
                                    reasonType: String,
                                    reasonDesc: String
                                ) {
                                    feedViewModel.sendReport(
                                        positionInFeed,
                                        postId,
                                        reasonType,
                                        reasonDesc
                                    )
                                }
                            }
                        )
                        reportBottomSheet.show(
                            (context as FragmentActivity).supportFragmentManager,
                            ""
                        )
                    }
                } else {
                    onGoToLogin()
                }
            }
            sheet.onFollow = {
                feedAnalytics.eventClickThreeDotsOption(
                    finalId,
                    "unfollow",
                    postType,
                    isFollowed,
                    authorId,
                    mediaType,
                    trackerId = "17986",
                    campaignStatus = getTrackerLabelSuffixFromPosition(positionInFeed),
                    authorType = authorType
                )
                if (userSession.isLoggedIn) {
                    onHeaderActionClick(
                        positionInFeed,
                        authorId,
                        authorType,
                        isFollowed,
                        isVideo = mediaType == MediaType.VIDEO,
                        isBottomSheetMenuOnFeed = true
                    )
                } else {
                    onGoToLogin()
                }
            }
            sheet.onDelete = {
                createDeleteDialog(
                    positionInFeed,
                    postId,
                    authorId,
                    postType,
                    isFollowed,
                    isVideo = mediaType == MediaType.VIDEO,
                    authorType
                )
                feedAnalytics.clickDeleteThreeDotsPage(
                    finalId,
                    authorId,
                    postType,
                    isFollowed,
                    mediaType,
                    authorType
                )
            }
            sheet.onDismiss = {
                getFeedTrackerDataModelFromPosition(positionInFeed)?.let {
                    feedAnalytics.eventClickGreyAreaThreeDots(
                        it
                    )
                }
            }
            sheet.onEdit = {
                openEditPostPage(caption, postId, authorId)
            }

            sheet.onClosedClicked = {
                feedAnalytics.eventCloseThreeDotBS(
                    finalId,
                    postType,
                    isFollowed,
                    authorId,
                    authorType
                )
            }
        }
    }

    private fun getTrackerLabelSuffixForCampaignSaleTracker(card: FeedXCard) =
        if (card.campaign.status.isNotEmpty()) {
            if (card.campaign.isUpcoming) {
                "pre"
            } else {
                "ongoing"
            }
        } else {
            ""
        }

    private fun getTrackerLabelSuffixFromPosition(positionInFeed: Int): String {
        val list = adapter.getList()
        if (positionInFeed < list.size && list[positionInFeed] is DynamicPostUiModel) {
            val item = list[positionInFeed] as DynamicPostUiModel
            val card = item.feedXCard
            return getTrackerLabelSuffixForCampaignSaleTracker(card)
        }
        return ""
    }

    private fun getTrackerHasVoucherFromPosition(positionInFeed: Int): Boolean {
        val list = adapter.getList()
        if (positionInFeed < list.size && list[positionInFeed] is DynamicPostUiModel) {
            val item = list[positionInFeed] as DynamicPostUiModel
            val card = item.feedXCard
            return card.hasVoucher
        }
        return false
    }

    private fun getTrackerAuthorTypeFromPosition(positionInFeed: Int): String {
        val list = adapter.getList()
        if (positionInFeed < list.size && list[positionInFeed] is DynamicPostUiModel) {
            val item = list[positionInFeed] as DynamicPostUiModel
            val card = item.feedXCard
            return card.author.type.toString()
        }
        return ""
    }

    private fun getTrackerIdForCampaignSaleTracker(
        positionInFeed: Int,
        trackerIdAsgc: String = "",
        trackerIdAsgcRecom: String = ""
    ): String {
        val list = adapter.getList()
        var trackerId = ""
        if (positionInFeed < list.size && list[positionInFeed] is DynamicPostUiModel) {
            val item = list[positionInFeed] as DynamicPostUiModel
            val card = item.feedXCard
            trackerId = if (card.campaign.isFlashSaleToko || card.campaign.isRilisanSpl) {
                if (card.followers.isFollowed) {
                    trackerIdAsgc
                } else {
                    trackerIdAsgcRecom
                }
            } else {
                ""
            }
        }
        return trackerId
    }

    private fun openEditPostPage(caption: String, postId: String, authorId: String) {
        var createPostViewModel = CreatePostViewModel()
        createPostViewModel.caption = caption
        createPostViewModel.postId = postId
        createPostViewModel.editAuthorId = authorId

        val intent = RouteManager.getIntent(context, INTERNAL_AFFILIATE_CREATE_POST_V2)
        intent.putExtra(PARAM_AUTHOR_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
        intent.putExtra(CreatePostViewModel.TAG, createPostViewModel)
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
        val trackerId = getTrackerIdForCampaignSaleTracker(
            positionInFeed,
            trackerIdAsgc = "13449",
            trackerIdAsgcRecom = "13435"
        )
        feedAnalytics.eventClickLikeButton(
            FeedTrackerData(
                postId = if (postType == TYPE_FEED_X_CARD_PLAY) playChannelId else id.toString(),
                postType = postType,
                isFollowed = isFollowed,
                shopId = shopId,
                mediaType = mediaType ?: "",
                trackerId = trackerId,
                campaignStatus = getTrackerLabelSuffixFromPosition(positionInFeed),
                positionInFeed = positionInFeed,
                contentSlotValue = getContentScoreFromPosition(positionInFeed),
                authorType = authorType,
                hasVoucher = getTrackerHasVoucherFromPosition(positionInFeed)
            ),
            doubleTap = type,
            isLiked = !isLiked
        )
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id)
        } else {
            onLikeKolClicked(positionInFeed, id)
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
        isClickIcon: Boolean
    ) {
        if (isClickIcon) {
            getFeedTrackerDataModelFromPosition(positionInFeed)?.let {
                feedAnalytics.eventClickOpenComment(
                    it
                )
            }
        } else if ((type == TYPE_FEED_X_CARD_PLAY || type == TYPE_FEED_X_CARD_POST) && !isClickIcon) {
            getFeedTrackerDataModelFromPosition(positionInFeed)?.let {
                feedAnalytics.eventClickLihatSemuaComment(
                    it
                )
            }
        }

        goToKolComment(
            positionInFeed,
            id,
            authorType,
            mediaType == MediaType.VIDEO,
            isFollowed,
            type
        )
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
        val typeVOD = type == TYPE_FEED_X_CARD_PLAY
        val trackerid = getTrackerIdForCampaignSaleTracker(
            positionInFeed,
            trackerIdAsgc = "13450",
            trackerIdAsgcRecom = "13436"
        )
        activity?.let {
            val urlString = when {
                typeASGC -> {
                    weblink
                }
                typeVOD -> {
                    String.format(getString(R.string.feed_vod_share_weblink), playChannelId)
                }
                else -> {
                    String.format(getString(R.string.feed_share_weblink), id)
                }
            }

            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(id)
                .setName(title)
                .setDescription(description)
                .setDesktopUrl(urlString)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(imageUrl)
                .setDeepLink(url)

            if (isTopads) {
                shareBottomSheetProduct = true
                shareDataBuilder.apply {
                    setOgImageUrl(imageUrl)
                    setDesktopUrl(url)
                    setUri(url)
                }
            } else {
                shareBottomSheetProduct = false
                shareDataBuilder.apply {
                    setUri(urlString)
                }
            }
            shareData = shareDataBuilder.build()

            val linkerShareData = DataMapper().getLinkerShareData(shareData)
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    this
                )
            )
        }
        if (type == TYPE_FEED_X_CARD_PLAY) {
            getFeedTrackerDataModelFromPosition(positionInFeed)?.let {
                feedAnalytics.eventClickOpenShare(
                    it
                )
            }
        } else {
            getFeedTrackerDataModelFromPosition(positionInFeed, trackerId = trackerid)?.let {
                feedAnalytics.eventClickOpenShare(
                    it
                )
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
        // Not used
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
    }

    override fun onFullScreenCLick(
        feedXCard: FeedXCard,
        positionInFeed: Int,
        redirectUrl: String,
        currentTime: Long,
        shouldTrack: Boolean,
        isFullScreenButton: Boolean
    ) {
        if (isFullScreenButton) {
            feedAnalytics.eventClickFullScreenIconVOD(
                getFeedTrackerDataModel(feedXCard)
            )
        } else {
            feedAnalytics.eventClicklanjutMenontonVOD(
                getFeedTrackerDataModel(feedXCard, positionInFeed)
            )
        }
        val finalApplink = if (!shouldTrack) {
            Uri.parse(redirectUrl)
                .buildUpon()
                .appendQueryParameter(START_TIME, currentTime.toString())
                .appendQueryParameter(SHOULD_TRACK, shouldTrack.toString())
                .build().toString()
        } else {
            Uri.parse(redirectUrl)
                .buildUpon()
                .appendQueryParameter(START_TIME, currentTime.toString())
                .appendQueryParameter(SOURCE_TYPE, VOD_POST)
                .build().toString()
        }

        if (feedXCard.typename != TYPE_FEED_X_CARD_PLAY && feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO) {
            onVideoPlayerClicked(
                positionInFeed,
                0,
                feedXCard.id,
                feedXCard.media[0].mediaUrl,
                feedXCard.author.id,
                "",
                feedXCard.followers.isFollowed,
                currentTime
            )
        } else {
            onGoToLink(finalApplink)
        }
    }

    override fun addVODView(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
        if (hitTrackerApi) {
            if (feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO) {
                feedViewModel.trackLongVideoView(feedXCard.id, rowNumber)
            } else {
                feedViewModel.trackVisitChannel(playChannelId, rowNumber)
            }
        }
    }

    override fun sendWatchVODTracker(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long
    ) {
        feedAnalytics.eventSendWatchVODAnalytics(
            getFeedTrackerDataModel(feedXCard),
            time
        )
    }

    override fun onPostTagBubbleClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        adClickUrl: String
    ) {
        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            val card = item.feedXCard

            feedAnalytics.eventClickPostTagitem(
                getFeedTrackerDataModel(
                    card,
                    positionInFeed = positionInFeed,
                    product = postTagItem
                )
            )
        }

        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            val isFollowed = item.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed
            val id = item.cpmModel?.data?.get(0)?.id ?: ""
            if (isFollowed != null) {
                feedAnalytics.eventClickPostTagitem(
                    FeedTrackerData(
                        postId = id,
                        product = postTagItem,
                        postType = TYPE_TOPADS_HEADLINE_NEW,
                        isFollowed = isFollowed,
                        shopId = id,
                        contentSlotValue = getContentScoreFromPosition(positionInFeed),
                        authorType = item.feedXCard.author.type.toString()
                    )
                )
            }
            sendTopadsUrlClick(adClickUrl)
        }
        onGoToLink(redirectUrl)
    }

    override fun userImagePostImpression(positionInFeed: Int, contentPosition: Int) {
        if (adapter.getlist().size > positionInFeed && adapter.getList()[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
            feedAnalytics.eventImageImpressionPost(
                FeedAnalyticTracker.Screen.FEED,
                trackingPostModel.postId,
                trackingPostModel.activityName,
                trackingPostModel.mediaType,
                trackingPostModel.mediaUrl,
                trackingPostModel.recomId,
                positionInFeed
            )
        }
    }

    override fun userCarouselImpression(feedXCard: FeedXCard, positionInFeed: Int) {
        if (feedXCard.isTopAds) {
            onTopAdsProductItemListsner(
                positionInFeed,
                feedXCard.listProduct[feedXCard.lastCarouselIndex],
                feedXCard.cpmData
            )
            TopAdsGtmTracker.eventTopAdsHeadlineShopView(
                positionInFeed,
                feedXCard.cpmData,
                "",
                userSession.userId
            )
        }
        feedAnalytics.eventImpression(
            getFeedTrackerDataModel(feedXCard)
        )
    }

    override fun userGridPostImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String,
        hasVoucher: Boolean
    ) {
        feedAnalytics.eventImpressionPostASGC(activityId, positionInFeed, "", shopId, hasVoucher)
    }

    override fun userProductImpression(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        shopId: String,
        isFollowed: Boolean,
        productList: List<FeedXProduct>
    ) {
        var isFollowed = true
        var hasVoucher = false
        val list = adapter.getList()
        if (positionInFeed < list.size && list[positionInFeed] is DynamicPostUiModel) {
            val item = list[positionInFeed] as DynamicPostUiModel
            val card = item.feedXCard
            isFollowed = card.followers.isFollowed
            hasVoucher = card.hasVoucher
        }
        feedAnalytics.eventImpressionProduct(
            activityId,
            productId,
            productList,
            shopId,
            isFollowed,
            hasVoucher
        )
    }

    override fun onImageClick(
        positionInFeed: Int,
        contentPosition: Int,
        redirectLink: String
    ) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onMediaGridClick(
        positionInFeed: Int,
        contentPosition: Int,
        redirectLink: String,
        isSingleItem: Boolean
    ) {
        if (adapter.getlist()[positionInFeed] is DynamicPostModel) {
            val (id, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
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

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {
        for (track in trackList) {
            if (isClick) {
                trackUrlEvent(track.clickURL)
            } else {
                trackUrlEvent(track.viewURL)
            }
        }
    }

    override fun onPostTagItemBuyClicked(
        positionInFeed: Int,
        postTagItem: PostTagItem,
        authorType: String
    ) {
    }

    private fun onTagSheetItemBuy(
        item: ProductPostTagModelNew
    ) {
        val activityId = item.postId
        val postTagItem = item.product
        val shopId = item.shopId
        val type = item.postType
        val isFollowed = item.isFollowed
        val playChannelId = item.playChannelId
        val shopName = item.shopName
        val mediaType = item.mediaType
        val isLongVideo = mediaType == TYPE_LONG_VIDEO
        val contentScore = getContentScoreFromPosition(item.positionInFeed)
        val hasVoucher = getTrackerHasVoucherFromPosition(item.positionInFeed)
        val authorType = getTrackerAuthorTypeFromPosition(item.positionInFeed)

        if (item.saleStatus.isEmpty()) {
            if (type == TYPE_FEED_X_CARD_PLAY || type == TYPE_TOPADS_HEADLINE_NEW || isLongVideo) {
                feedAnalytics.eventAddToCartFeedVOD(
                    if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else activityId,
                    postTagItem.id,
                    postTagItem.name,
                    postTagItem.priceFmt,
                    1,
                    shopId,
                    shopName,
                    type,
                    isFollowed,
                    mediaType,
                    contentScore = contentScore,
                    hasVoucher = hasVoucher,
                    authorType = authorType
                )
            } else {
                feedAnalytics.eventAddToCartFeedVOD(
                    activityId,
                    postTagItem.id,
                    postTagItem.name,
                    if (postTagItem.isDiscount) postTagItem.priceDiscount.toString() else postTagItem.price.toString(),
                    1,
                    shopId,
                    shopName,
                    type,
                    isFollowed,
                    mediaType,
                    contentScore = contentScore,
                    hasVoucher = hasVoucher,
                    authorType = authorType
                )
            }
        } else {
            feedAnalytics.sendClickAddToCartAsgcProductTagBottomSheet(
                activityId,
                shopId,
                postTagItem.id,
                if (item.isUpcoming) "pre" else "ongoing",
                postTagItem.name,
                postTagItem.price.toString(),
                1,
                item.shopName,
                contentScore = contentScore,
                hasVoucher = hasVoucher,
                authorType = authorType
            )
        }

        if (userSession.isLoggedIn) {
            if (::productTagBS.isInitialized) {
                productTagBS.dismissedByClosing = true
                productTagBS.dismiss()
            }
            feedViewModel.addtoCartProduct(postTagItem, shopId, type, isFollowed, activityId)
        } else {
            onGoToLogin()
        }
    }

    override fun onYoutubeThumbnailClick(
        positionInFeed: Int,
        contentPosition: Int,
        youtubeId: String
    ) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(context, redirectUrl)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
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
        } else {
            onVoteOptionClicked(positionInFeed, pollId, optionId)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostModel) {
            val (_, _, _, _, _, _, contentList, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostModel
            if (contentList[contentPosition] is PollContentModel) {
                val (_, _, _, _, optionList) = contentList[contentPosition] as PollContentModel
                val (_, option1, imageUrl) = optionList[option]
                analytics.eventVoteClick(
                    trackingPostModel.activityName,
                    trackingPostModel.mediaType,
                    pollId,
                    optionId,
                    option1,
                    imageUrl,
                    trackingPostModel.postId,
                    userIdLong
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

    override fun onReadMoreClicked(card: FeedXCard, positionInFeed: Int) {
        feedAnalytics.eventClickReadMoreNew(getFeedTrackerDataModel(card, positionInFeed))
    }

    override fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int) {
        feedViewModel.checkUpcomingCampaignInitialReminderStatus(card.campaign, positionInFeed)
    }

    override fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int) {
        // send analytics
        if (card.campaign.reminder == FeedASGCUpcomingReminderStatus.On(card.campaign.campaignId)) {
            feedAnalytics.sendClickUnremindCampaignAsgcEvent(
                activityId = card.id,
                shopId = card.author.id,
                contentScore = (card.contentScore).firstOrNull()?.value ?: String.EMPTY
            )
        } else {
            feedAnalytics.sendClickRemindCampaignAsgcEvent(
                activityId = card.id,
                shopId = card.author.id,
                contentScore = (card.contentScore).firstOrNull()?.value ?: String.EMPTY
            )
        }

        if (userSession.isLoggedIn) {
            feedViewModel.setUnsetReminder(card.campaign, positionInFeed)
        } else {
            onGoToLogin()
        }
    }

    override fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int) {
        if (::productTagBS.isInitialized) {
            productTagBS.dismiss()
        }
        feedViewModel.fetchLatestFeedPostWidgetData(card.id, positionInFeed)
    }

    override fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int) {
        if (adapter.getlist().size > positionInFeed && adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            adapter.getlist().removeAt(positionInFeed)
            adapter.notifyItemRemoved(positionInFeed)
        }
    }

    override fun onImageClicked(
        feedXCard: FeedXCard
    ) {
        feedAnalytics.eventImageClicked(getFeedTrackerDataModel(feedXCard))
        if (feedXCard.campaign.isRilisanSpl || feedXCard.campaign.isFlashSaleToko) {
            val product =
                if (feedXCard.lastCarouselIndex in (feedXCard.products.indices)) feedXCard.products[feedXCard.lastCarouselIndex] else null
            product?.let {
                feedAnalytics.eventGridProductItemClicked(
                    getFeedTrackerDataModel(feedXCard, product = it)
                )
            }
        }
    }

    override fun onTagClicked(
        card: FeedXCard,
        products: List<FeedXProduct>,
        listener: DynamicPostViewHolder.DynamicPostListener,
        mediaType: String,
        positionInFeed: Int
    ) {
        if (products.isNotEmpty()) {
            if (!::productTagBS.isInitialized) {
                productTagBS = ProductItemInfoBottomSheet()
            }
            feedAnalytics.eventTagClicked(
                getFeedTrackerDataModel(card, positionInFeed = positionInFeed)
            )

            customMvcTracker.activityId =
                if (card.typename == TYPE_FEED_X_CARD_PLAY) card.playChannelID else card.id
            customMvcTracker.status = getTrackerLabelSuffixForCampaignSaleTracker(card)
            customMvcTracker.hasVoucher = card.hasVoucher
            customMvcTracker.contentScore = (card.contentScore).firstOrNull()?.value ?: String.EMPTY

            productTagBS.show(
                childFragmentManager,
                this,
                ProductBottomSheetData(
                    products = products,
                    postId = card.id,
                    shopId = card.author.id,
                    postType = card.typename,
                    isFollowed = card.followers.isFollowed,
                    positionInFeed = positionInFeed,
                    playChannelId = card.playChannelID,
                    shopName = card.author.name,
                    mediaType = mediaType,
                    saleStatus = card.campaign.status,
                    saleType = card.campaign.name,
                    hasVoucher = card.hasVoucher,
                    authorType = card.author.type.toString()
                ),
                viewModelFactory,
                customMvcTracker = customMvcTracker
            )
            productTagBS.closeClicked = {
                val trackerId = if (card.campaign.isFlashSaleToko || card.campaign.isRilisanSpl) {
                    if (card.followers.isFollowed) {
                        "13441"
                    } else {
                        "13427"
                    }
                } else {
                    ""
                }
                feedAnalytics.eventClickCloseProductInfoSheet(
                    getFeedTrackerDataModel(
                        card,
                        trackerId = trackerId
                    )
                )
            }
            productTagBS.disMissed = {
                val trackerId = if (card.campaign.isFlashSaleToko || card.campaign.isRilisanSpl) {
                    if (card.followers.isFollowed) {
                        "13448"
                    } else {
                        "13434"
                    }
                } else {
                    ""
                }

                feedAnalytics.eventClickGreyArea(
                    getFeedTrackerDataModel(
                        feedXCard = card,
                        trackerId = trackerId
                    )
                )
            }
            if (shouldShowFollowerBottomSheet(card)) {
                showFollowerBottomSheet(positionInFeed, card.campaign.status)
            }
        }
    }

    private fun shouldShowFollowerBottomSheet(card: FeedXCard) =
        card.campaign.isRilisanSpl && !card.followers.isFollowed && card.campaign.isRSFollowersRestrictionOn

    private fun addToWishList(
        postId: String,
        productId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        playChannelId: String,
        mediaType: String,
        productRowNumber: Int = 0,
        positionInFeed: Int,
        trackerid: String = "",
        campaignStatus: String = "",
        hasVoucher: Boolean = false,
        authorType: String = ""
    ) {
        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId
        val contentScore = getContentScoreFromPosition(positionInFeed)

        feedAnalytics.eventAddToWishlistClicked(
            finalId,
            productId,
            type,
            isFollowed,
            shopId,
            mediaType,
            trackerid,
            campaignStatus,
            contentScore = contentScore,
            hasVoucher = hasVoucher,
            authorType = authorType
        )
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        context?.let {
            feedViewModel.addWishlistV2(
                postId,
                productId,
                shopId,
                positionInFeed,
                productRowNumber,
                type,
                isFollowed,
                ::onWishListFail,
                ::onWishListSuccessV2,
                it
            )
        }
    }

    private fun onWishListFail(s: String) {
        showToast(s, Toaster.TYPE_ERROR)
    }

    private fun onWishListSuccessV2(
        activityId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        itemRowNumber: Int,
        positionInFeed: Int,
        result: AddToWishlistV2Response.Data.WishlistAddV2
    ) {
        Toaster.build(
            requireView(),
            getString(Rwishlist.string.on_success_add_to_wishlist_msg),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            getString(Rwishlist.string.cta_success_add_to_wishlist),
            View.OnClickListener {
                getFeedTrackerDataModelFromPosition(positionInFeed)?.let { feedTrackerData ->
                    feedAnalytics.eventOnTagSheetItemBuyClicked(
                        feedTrackerData
                    )
                }
                RouteManager.route(context, ApplinkConst.WISHLIST)
            }
        ).show()
        productTagBS.changeWishlistIconOnWishlistSuccess(itemRowNumber)
    }

    private fun onShareProduct(
        item: ProductPostTagModelNew,
        trackerId: String = ""
    ) {
        getFeedTrackerDataModelFromPosition(item.positionInFeed, trackerId = trackerId)?.let {
            feedAnalytics.eventonShareProductClicked(
                it
            )
        }
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }

        shareBottomSheetProduct = item.isTopads

        val linkerBuilder = LinkerData.Builder.getLinkerBuilder()
            .setId(item.id)
            .setName(item.text)
            .setDescription(item.description)
            .setImgUri(item.imgUrl)
            .setUri(item.weblink)
            .setDeepLink(item.applink)
            .setType(LinkerData.FEED_TYPE)
            .setDesktopUrl(item.weblink)

        if (item.isTopads) {
            linkerBuilder.setOgImageUrl(item.imgUrl)
        }

        shareData = linkerBuilder.build()

        feedProductTagSharingHelper.show(
            productTagShareModel = FeedProductTagSharingHelper.Model.map(item),
            shareData = shareData
        )
    }

    override fun muteUnmuteVideo(
        card: FeedXCard,
        mute: Boolean,
        positionInFeed: Int,
        mediaType: String
    ) {
        if (card.isTypeVOD || card.isTypeLongVideo) {
            feedAnalytics.clickSoundVOD(
                getFeedTrackerDataModel(
                    feedXCard = card,
                    positionInFeed = positionInFeed
                )
            )
        } else {
            feedAnalytics.clickMuteButton(
                getFeedTrackerDataModel(
                    feedXCard = card,
                    positionInFeed = positionInFeed
                )
            )
        }
    }

    override fun onImpressionTracking(feedXCard: FeedXCard, positionInFeed: Int) {
        feedAnalytics.eventPostImpression(
            getFeedTrackerDataModel(feedXCard, positionInFeed)
        )
    }

    override fun onHashtagClickedFeed(hashtagText: String, feedXCard: FeedXCard) {
        feedAnalytics.clickHashTag(
            hashtagText,
            feedXCard.author.id,
            feedXCard.id,
            feedXCard.typename,
            feedXCard.media[0].type != "image",
            feedXCard.followers.isFollowed,
            false,
            (feedXCard.contentScore).firstOrNull()?.value ?: String.EMPTY,
            feedXCard.hasVoucher,
            authorType = feedXCard.author.type.toString()
        )
    }

    override fun onGridItemClick(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        redirectLink: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        hasVoucher: Boolean,
        products: List<FeedXProduct>,
        index: Int
    ) {
        onGoToLinkASGCProductDetail(
            redirectLink,
            shopId,
            activityId,
            isFollowed,
            hasVoucher,
            type,
            products
        )
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
        if (activity != null) {
            feedAnalytics.clickOnVideo(
                postId,
                authorId,
                isFollowed,
                getContentScoreFromPosition(positionInFeed),
                authorType
            )
            val videoDetailIntent =
                RouteManager.getIntent(context, ApplinkConstInternalContent.VIDEO_DETAIL, postId)
            videoDetailIntent.putExtra(PARAM_CALL_SOURCE, PARAM_FEED)
            videoDetailIntent.putExtra(PARAM_POST_POSITION, positionInFeed)
            videoDetailIntent.putExtra(PARAM_VIDEO_INDEX, contentPosition)
            videoDetailIntent.putExtra(PARAM_VIDEO_AUTHOR_TYPE, authorType)
            videoDetailIntent.putExtra(PARAM_POST_TYPE, "sgc")
            videoDetailIntent.putExtra(PARAM_IS_POST_FOLLOWED, isFollowed)
            videoDetailIntent.putExtra(PARAM_START_TIME, startTime)
            startActivityForResult(videoDetailIntent, OPEN_VIDEO_DETAIL)
        }
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
        feedAnalytics.eventWatchVideo(
            getFeedTrackerDataModel(feedXCard),
            duration
        )
    }

    private fun fetchFirstPage() {
        feedContainer.show()
        showRefresh()
        adapter.showShimmer()
        feedViewModel.getFeedFirstPage()
        afterRefresh = false
    }

    private fun checkIfPostsAreSame(model: Visitable<*>?): Boolean {
        val adapterItemAtFirstPosition = adapter.getlist()[1]
        when (model) {
            is DynamicPostUiModel -> {
                if (adapterItemAtFirstPosition is DynamicPostUiModel) {
                    return adapterItemAtFirstPosition.feedXCard.id == model.feedXCard.id
                }
                return false
            }
            is TopadsHeadLineV2Model -> {
                if (adapterItemAtFirstPosition is TopadsHeadLineV2Model) {
                    return adapterItemAtFirstPosition.feedXCard.id == model.feedXCard.id
                }
                return false
            }
            is ShopRecomWidgetModel -> {
                if (adapterItemAtFirstPosition is ShopRecomWidgetModel) {
                    return true
                }
                return false
            }
            else -> return false
        }
    }

    private fun onSuccessGetFirstFeed(firstPageDomainModel: DynamicFeedFirstPageDomainModel) {
        if (!firstPageDomainModel.shouldOverwrite) {
            adapter.updateList(firstPageDomainModel.dynamicFeedDomainModel.postList)
            feedViewModel.updateCurrentFollowState(adapter.getlist())
            return
        }
        if (isRefreshForPostCOntentCreation) {
            isRefreshForPostCOntentCreation = false
            val fetchedPostIsSameAsTopMostPost =
                firstPageDomainModel.dynamicFeedDomainModel.postList.size >= 2 && checkIfPostsAreSame(
                    firstPageDomainModel.dynamicFeedDomainModel.postList[1]
                )
            if (!fetchedPostIsSameAsTopMostPost) {
                adapter.addListItemAtTop(firstPageDomainModel.dynamicFeedDomainModel.postList[1])
            }
            feedViewModel.updateCurrentFollowState(adapter.getlist())
            return
        }

        clearData()
        val model = firstPageDomainModel.dynamicFeedDomainModel
        if (model.postList.isNotEmpty()) {
            setLastCursorOnFirstPage(model.cursor)
            setFirstPageCursor(model.firstPageCursor)
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

        feedViewModel.updateCurrentFollowState(adapter.getlist())

        sendMoEngageOpenFeedEvent()
        stopTracePerformanceMon()
    }

    private fun onErrorGetFirstFeed(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) e.printStackTrace()
        finishLoading()
        showGlobalError()
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
            feedViewModel.updateCurrentFollowState(adapter.getlist())
        }
    }

    private fun onErrorGetNextFeed(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        unsetEndlessScroll()
        hideAdapterLoading()
    }

    private fun onSuccessFavoriteUnfavoriteShop(model: FeedPromotedShopModel) {
        showSnackbar(model.resultString)
        if (hasFeed()) {
            updateFavorite()
        } else {
            updateFavoriteFromEmpty(model.promotedShopViewModel.shop.id)
        }
    }

    private fun onErrorFavoriteUnfavoriteShop(e: Throwable) {
        NetworkErrorHelper.showSnackbar(activity, ErrorHandler.getErrorMessage(context, e))
    }

    private fun onSuccessFollowUnfollowKol(rowNumber: Int) {
        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostUiModel) {
            val item = (adapter.getlist()[rowNumber] as DynamicPostUiModel)
            item.feedXCard.followers.isFollowed = !item.feedXCard.followers.isFollowed
            if (item.feedXCard.followers.isFollowed) {
                item.feedXCard.followers.transitionFollow = true
            }
            adapter.notifyItemChanged(
                rowNumber,
                DynamicPostNewViewHolder.PAYLOAD_ANIMATE_FOLLOW
            )
        }
    }

    private fun onErrorFollowUnfollowKol(data: FollowKolViewModel) {
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again),
            View.OnClickListener {
                if (data.status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW) {
                    feedViewModel.doUnfollowKol(data.id, data.rowNumber)
                } else {
                    feedViewModel.doFollowKol(data.id, data.rowNumber)
                }
            }
        )
    }

    private fun showGlobalError() {
        feedGlobalError.apply {
            visible()
            errorSecondaryAction.hide()
            setActionClickListener {
                fetchFirstPage()
                hide()
            }
        }
        feedContainer.hide()
    }

    private fun onSuccessLikeDislikeKolPost(rowNumber: Int) {
        val newList = adapter.getlist()
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostUiModel) {
            val item = (newList[rowNumber] as DynamicPostUiModel)
            val like = item.feedXCard.like
            like.isLiked = !like.isLiked
            if (like.isLiked) {
                try {
                    val likeValue = Integer.valueOf(like.countFmt) + 1
                    like.countFmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                    Timber.e(ignored)
                }

                like.count = like.count + 1
            } else {
                try {
                    val likeValue = Integer.valueOf(like.countFmt) - 1
                    like.countFmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                    Timber.e(ignored)
                }

                like.count = like.count - 1
            }
            adapter.notifyItemChanged(rowNumber, PAYLOAD_ANIMATE_LIKE)
        }
    }

    private fun onSuccessAddViewVODPost(rowNumber: Int) {
        val newList = adapter.getlist()
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostUiModel) {
            val item = (newList[rowNumber] as DynamicPostUiModel)
            val view = item.feedXCard.views
            try {
                val viewValue = Integer.valueOf(view.countFmt) + 1
                view.countFmt = viewValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            view.count = view.count + 1
        }
    }

    private fun onSuccessFetchStatusCampaignReminderButton(
        data: FeedAsgcCampaignResponseModel,
        shouldShowToaster: Boolean = false
    ) {
        val newList = adapter.getlist()
        val rowNumber = data.rowNumber
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostUiModel) {
            val item = (newList[rowNumber] as DynamicPostUiModel)
            val campaign = item.feedXCard.campaign
            if (campaign.campaignId == data.campaignId) {
                campaign.reminder = data.reminderStatus
            }
            if (shouldShowToaster) {
                showToastOnSuccessReminderSetForFSTorRS(item.feedXCard)
            }

            adapter.notifyItemChanged(
                data.rowNumber,
                DynamicPostNewViewHolder.PAYLOAD_REMINDER_BTN_STATUS_UPDATED
            )
        }
    }

    private fun onSuccessFetchLatestFeedWidgetData(data: FeedXCard, rowNumber: Int) {
        val newList = adapter.getlist()
        if (newList.size > rowNumber && newList[rowNumber] is DynamicPostUiModel) {
            val item = (newList[rowNumber] as DynamicPostUiModel)
            newList[rowNumber] = item.copy(feedXCard = data)
        }
        adapter.notifyItemChanged(
            rowNumber
        )
    }

    private fun showToastOnSuccessReminderSetForFSTorRS(card: FeedXCard) {
        when {
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.On && card.campaign.isFlashSaleToko -> showToast(
                context?.getString(com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_activate_fst_message)
                    ?: "",
                Toaster.TYPE_NORMAL
            )
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.On && card.campaign.isRilisanSpl -> showToast(
                context?.getString(com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_activate_rs_message)
                    ?: "",
                Toaster.TYPE_NORMAL
            )
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.Off -> showToast(
                context?.getString(
                    com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_deactivate_message
                ) ?: "",
                Toaster.TYPE_NORMAL
            )
        }
    }

    private fun onErrorLikeDislikeKolPost(errorMessage: String) {
        showToast(errorMessage, Toaster.TYPE_ERROR)
    }

    private fun onSuccessDeletePost(rowNumber: Int) {
        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostUiModel) {
            adapter.getlist().removeAt(rowNumber)
            adapter.notifyItemRemoved(rowNumber)
            Toaster.build(
                requireView(),
                getString(R.string.feed_post_deleted),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(com.tokopedia.kolcommon.R.string.content_action_ok)
            ).show()
        }
        if (adapter.getlist().isEmpty()) {
            showRefresh()
            onRefresh()
        }
    }

    private fun onErrorDeletePost(data: DeletePostModel) {
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again),
            View.OnClickListener {
                feedViewModel.doDeletePost(data.id, data.rowNumber)
            }
        )
    }

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    private fun onSuccessToggleFavoriteShop(data: FavoriteShopModel) {
        val rowNumber = data.rowNumber
        val adapterPosition = data.adapterPosition
        if (rowNumber < adapter.getlist().size) {
            if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostUiModel) {
                val item = (adapter.getlist()[rowNumber] as DynamicPostUiModel)
                val feedXCardData = item.feedXCard
                feedXCardData.followers.isFollowed = !feedXCardData.followers.isFollowed
                if (!feedXCardData.followers.isFollowed) {
                    showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast),
                        Toaster.TYPE_NORMAL
                    )
                } else if (feedXCardData.followers.isFollowed) {
                    showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_component_follow_success_toast),
                        Toaster.TYPE_NORMAL
                    )
                }

                val trackerId =
                    if (feedXCardData.campaign.isFlashSaleToko || feedXCardData.campaign.isRilisanSpl) {
                        if (feedXCardData.followers.isFollowed) {
                            "13423"
                        } else {
                            ""
                        }
                    } else {
                        ""
                    }

                feedAnalytics.eventClickFollowitem(
                    getFeedTrackerDataModel(feedXCardData, trackerId = trackerId)
                )

                if (item.feedXCard.followers.isFollowed) {
                    item.feedXCard.followers.transitionFollow = true
                }

                adapter.notifyItemChanged(
                    rowNumber,
                    DynamicPostNewViewHolder.PAYLOAD_ANIMATE_FOLLOW
                )
            }

            if (adapter.getlist()[rowNumber] is DynamicPostModel) {
                val (_, _, header) = adapter.getlist()[rowNumber] as DynamicPostModel
                header.followCta.isFollow = !header.followCta.isFollow
                adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW)
            }

            if (adapter.getlist()[rowNumber] is TopadsShopUiModel) {
                val (_, dataList) = adapter.getlist()[rowNumber] as TopadsShopUiModel
                dataList[adapterPosition].isFavorit = !dataList[adapterPosition].isFavorit
                adapter.notifyItemChanged(rowNumber, adapterPosition)
            }
            if (adapter.getlist()[rowNumber] is TopadsHeadlineUiModel) {
                val topadsHeadlineUiModel = adapter.getlist()[rowNumber] as TopadsHeadlineUiModel
                topadsHeadlineUiModel.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed?.let {
                    topadsHeadlineUiModel.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed =
                        !it
                }
                adapter.notifyItemChanged(rowNumber)
            }

            if (adapter.getlist()[rowNumber] is TopadsHeadLineV2Model) {
                val item = (adapter.getlist()[rowNumber] as TopadsHeadLineV2Model)
                val card = item.feedXCard
                card.followers.isFollowed = !card.followers.isFollowed
                val isFollowed = card.followers.isFollowed

                feedAnalytics.eventClickFollowitem(
                    getFeedTrackerDataModel(card)
                )

                if (isFollowed) {
                    card.followers.transitionFollow = true
                }
                if (!isFollowed) {
                    showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast),
                        Toaster.TYPE_NORMAL
                    )
                } else {
                    showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_component_follow_success_toast),
                        Toaster.TYPE_NORMAL
                    )
                }

                adapter.notifyItemChanged(
                    rowNumber,
                    TopAdsHeadlineV2ViewHolder.PAYLOAD_ANIMATE_FOLLOW
                )
            }
        }
    }

    private fun onErrorToggleFavoriteShop(data: FavoriteShopModel) {
        adapter.notifyItemChanged(data.rowNumber, data.adapterPosition)
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again),
            View.OnClickListener {
                feedViewModel.doToggleFavoriteShop(
                    data.rowNumber,
                    data.adapterPosition,
                    data.shopId
                )
            }
        )
    }

    private fun sendMoEngageOpenFeedEvent() {
        val isEmptyFeed = !hasFeed()
        val value = DataLayer.mapOf(
            MoEngage.LOGIN_STATUS,
            userSession.isLoggedIn,
            MoEngage.IS_FEED_EMPTY,
            isEmptyFeed
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, EventMoEngage.OPEN_FEED)
    }

    private fun stopTracePerformanceMon() {
        performanceMonitoring.stopTrace()
    }

    private fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String) {
    }

    private fun onGoToLinkASGCProductDetail(
        link: String,
        shopId: String,
        activityId: String,
        isFollowed: Boolean,
        hasVoucher: Boolean,
        type: String,
        products: List<FeedXProduct>
    ) {
        context?.let {
            if (!TextUtils.isEmpty(link)) {
                if (RouteManager.isSupportApplink(it, link)) {
                    val intent = RouteManager.getIntent(context, link)
                    intent.putParcelableArrayListExtra(PRODUCT_LIST, ArrayList(products))
                    intent.putExtra(IS_FOLLOWED, isFollowed)
                    intent.putExtra(SHOP_NAME, products[0].authorName)
                    intent.putExtra(PARAM_SHOP_ID, shopId)
                    intent.putExtra(PARAM_ACTIVITY_ID, activityId)
                    intent.putExtra(POST_TYPE, type)
                    intent.putExtra(HAS_VOUCHER, hasVoucher)
                    if (activity != null) {
                        requireActivity().startActivity(intent)
                    }
                } else {
                    RouteManager.route(
                        it,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                    )
                }
            }
        }
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
        analytics.eventFeedClickShop(
            screenName,
            shopId,
            FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE
        )
    }

    private fun finishLoading() {
        swipeToRefresh.isRefreshing = false
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
        return (
            adapter.getlist()
                .isNotEmpty() && adapter.getlist().size > 1 && adapter.getlist()[0] !is EmptyModel
            )
    }

    private fun goToProductDetail(productId: String) {
        if (activity != null) {
            requireActivity().startActivity(getProductIntent(productId))
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
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

            if (visitable is DynamicPostUiModel) {
                val trackingPostModel = visitable.trackingPostModel

                if (visitable.feedXCard.tags.isNotEmpty()) {
                    postTagAnalytics.trackViewPostTagFeed(
                        visitable.feedXCard.id,
                        visitable.feedXCard.tags,
                        visitable.feedXCard.author.type,
                        trackingPostModel
                    )
                }
            } else if (visitable is TopadsShopUiModel) {
                val (_, _, _, trackingList, _) = visitable
                analytics.eventTopadsRecommendationImpression(
                    trackingList,
                    userIdLong
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
            userIdLong,
            positionInFeed,
            trackingPostModel.recomId
        )
    }

    private fun trackRecommendationFollowClick(
        trackingRecommendationModel: TrackingRecommendationModel,
        action: String
    ) {
        analytics.eventFollowRecommendation(
            action,
            trackingRecommendationModel.authorType,
            trackingRecommendationModel.authorId
        )
    }

    private fun showAfterPostToaster() {
        if (context != null) {
            Toast.makeText(context, R.string.feed_after_post, Toast.LENGTH_LONG).show()
        }
    }

    override fun onTopAdsViewImpression(bannerId: String, imageUrl: String) {
        analytics.eventTopadsRecommendationImpression(
            listOf(TrackingRecommendationModel(authorId = bannerId)),
            userIdLong
        )
        feedViewModel.doTopAdsTracker(imageUrl, "", "", "", false)
    }

    private fun updatePlayWidgetTotalView(channelId: String?, totalView: String?) {
        feedViewModel.updatePlayWidgetTotalView(channelId, totalView)
    }

    override fun onFollowClick(positionInFeed: Int, shopId: String, adId: String) {
        val eventLabel = "$adId - $shopId"
        val eventAction = CLICK_FOLLOW_TOPADS
        analytics.sendTopAdsHeadlineClickevent(eventAction, eventLabel, userSession.userId)
        feedViewModel.doToggleFavoriteShop(positionInFeed, 0, shopId)
    }

    override fun onFollowClickAds(positionInFeed: Int, shopId: String, adId: String) {
        onFollowClick(positionInFeed, shopId, adId)
        sendTopadsUrlClick(getAdClickUrl(positionInFeed))
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
        if (type == TYPE_TOPADS_HEADLINE_NEW) {
            sendTopadsUrlClick(getAdClickUrl(positionInFeed = positionInFeed))
            feedAnalytics.clickSekSekarang(
                postId,
                shopId,
                type,
                isFollowed,
                feedXCard.author.type.toString()
            )
        } else {
            val trackerId =
                if (feedXCard.campaign.isFlashSaleToko || feedXCard.campaign.isRilisanSpl) {
                    if (feedXCard.followers.isFollowed) {
                        "13451"
                    } else {
                        "13426"
                    }
                } else {
                    if (feedXCard.followers.isFollowed.not()) "40069" else ""
                }
            getFeedTrackerDataModelFromPosition(positionInFeed, trackerId = trackerId)?.let {
                feedAnalytics.eventGridMoreProductCLicked(
                    it
                )
            }
            val authorType =
                if (feedXCard.author.type == AUTHOR_USER_TYPE_VALUE) FollowCta.AUTHOR_USER else if (feedXCard.author.type == AUTHOR_SHOP_TYPE_VALUE) FollowCta.AUTHOR_SHOP else FollowCta.AUTHOR_UGC
            val campaign = feedXCard.campaign

            val intent = RouteManager.getIntent(context, feedXCard.appLinkProductList)
            intent.putExtra(IS_FOLLOWED, isFollowed)
            intent.putExtra(HAS_VOUCHER, hasVoucher)
            intent.putExtra(PARAM_SHOP_ID, shopId)
            intent.putExtra(SHOP_NAME, feedXCard.author.name)
            intent.putExtra(PARAM_ACTIVITY_ID, postId)
            intent.putExtra(POST_TYPE, type)
            intent.putExtra(PARAM_POST_POSITION, positionInFeed)
            intent.putExtra(PARAM_AUTHOR_TYPE, authorType)
            intent.putExtra(PARAM_SALE_TYPE, campaign.name)
            intent.putExtra(PARAM_SALE_STATUS, campaign.status)
            intent.putExtra(
                PARAM_CONTENT_SLOT_VALUE,
                feedXCard.contentScore.firstOrNull()?.value ?: String.EMPTY
            )

            if (shouldShowFollowerBottomSheet(feedXCard)) {
                startActivityForResult(intent, OPEN_FEED_DETAIL)
            } else {
                startActivity(intent)
            }
        }
    }

    override fun onTopAdsHeadlineImpression(
        position: Int,
        cpmModel: CpmModel,
        isNewVariant: Boolean
    ) {
        val eventLabel = "${cpmModel.data[0].id} - ${cpmModel.data[0].cpm.cpmShop.id}"
        val eventAction = IMPRESSION_CARD_TOPADS

        if (isNewVariant) {
            sendTopadsImpression(
                cpmModel.data[0].cpm.cpmImage.fullUrl,
                cpmModel.data[0].cpm.cpmShop.id,
                cpmModel.data[0].cpm.uri,
                cpmModel.data[0].cpm?.cpmImage?.fullEcs
            )
        }
        analytics.sendFeedTopAdsHeadlineAdsImpression(
            eventAction,
            eventLabel,
            cpmModel.data[0].id,
            position,
            userSession.userId
        )
    }

    override fun onTopAdsProductItemListsner(position: Int, product: Product, cpmData: CpmData) {
        val eventLabel = "${cpmData.id} - ${cpmData.cpm.cpmShop.id}"
        val eventAction = IMPRESSION_PRODUCT_TOPADS
        val productList: MutableList<Product> = mutableListOf()
        productList.clear()
        productList.add(product)
        analytics.sendFeedTopAdsHeadlineProductImpression(
            eventAction,
            eventLabel,
            productList,
            position,
            userSession.userId
        )
    }

    override fun onTopAdsHeadlineAdsClick(
        position: Int,
        applink: String?,
        cpmData: CpmData,
        isNewVariant: Boolean
    ) {
        if (!isNewVariant) {
            applink?.let { RouteManager.route(context, it) }
        } else {
            sendTopadsUrlClick(getAdClickUrl(position))
        }
        var eventAction = ""
        val eventLabel = "${cpmData.id} - ${cpmData.cpm.cpmShop.id}"

        if (applink?.contains("shop") == true && position == 0) {
            eventAction = CLICK_CEK_SEKARANG
            val eventLabelcek = "${cpmData.cpm.cpmShop.id}"
            analytics.sendTopAdsHeadlineClickevent(eventAction, eventLabelcek, userSession.userId)
        } else if (applink?.contains("shop") == true && position == 1) {
            eventAction = CLICK_SHOP_TOPADS
            analytics.sendTopAdsHeadlineClickevent(eventAction, eventLabel, userSession.userId)
        } else {
            val productId = applink?.substring(applink.lastIndexOf("/") + 1)
            eventAction = CLICK_PRODUCT_TOPADS
            val clickedProducts: MutableList<Product> = mutableListOf()
            for ((index, productItem) in cpmData.cpm.cpmShop.products.withIndex()) {
                if (productId.equals(productItem.id)) {
                    clickedProducts.clear()
                    clickedProducts.add(productItem)
                    analytics.sendFeedTopAdsHeadlineProductClick(
                        eventAction,
                        eventLabel,
                        clickedProducts,
                        index + 1,
                        userSession.userId
                    )
                }
            }
        }
    }

    override fun urlCreated(linkerShareData: LinkerShareResult?) {
        val intent: Intent = if (shareBottomSheetProduct) {
            getIntent(linkerShareData?.url ?: "")
        } else {
            getIntent()
        }
        activity?.startActivity(Intent.createChooser(intent, shareData.name))
    }

    private fun getIntent(shareUrl: String = ""): Intent {
        val shareUri: String = if (shareUrl.isNotEmpty()) {
            shareUrl
        } else {
            shareData.uri
        }
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, shareData.name)
            putExtra(Intent.EXTRA_SUBJECT, shareData.name)
            putExtra(Intent.EXTRA_TEXT, shareData.description + "\n" + shareUri)
        }
    }

    override fun onError(linkerError: LinkerError?) {}

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }

    private fun showNoInterNetDialog(context: Context) {
        val sheet = FeedNetworkErrorBottomSheet.newInstance(false)
        sheet.show((context as FragmentActivity).supportFragmentManager, "")
    }

    private fun showFollowerBottomSheet(positionInFeed: Int, campaignStatus: String) {
        feedFollowersOnlyBottomSheet =
            FeedFollowersOnlyBottomSheet.getOrCreate(childFragmentManager)

        if (feedFollowersOnlyBottomSheet?.isAdded == false && feedFollowersOnlyBottomSheet?.isVisible == false) {
            feedFollowersOnlyBottomSheet?.show(
                childFragmentManager,
                this,
                positionInFeed,
                status = campaignStatus
            )
        }
    }

    override fun onFollowClickedFromFollowBottomSheet(position: Int) {
        if (adapter.getlist().size > position && adapter.getlist()[position] is DynamicPostUiModel) {
            val item = adapter.getlist()[position] as DynamicPostUiModel
            val card = item.feedXCard
            val authorType =
                if (card.author.type == AUTHOR_USER_TYPE_VALUE) FollowCta.AUTHOR_USER else if (card.author.type == AUTHOR_SHOP_TYPE_VALUE) FollowCta.AUTHOR_SHOP else FollowCta.AUTHOR_UGC

            onHeaderActionClick(
                position,
                card.author.id,
                authorType,
                card.followers.isFollowed,
                card.typename,
                card.isTypeSgcVideo,
                isFollowedFromFollowRestrictionBottomSheet = true
            )
        }
    }

    override fun hideTopadsView(position: Int) {
        recyclerView.isComputingLayout.let {
            if (isAllowedNotify(it, position)) {
                if (adapter.getlist().size > position && adapter.getlist()[position] is TopadsHeadLineV2Model) {
                    adapter.getlist().removeAt(position)
                    recyclerView.post {
                        adapter.notifyItemRemoved(position)
                    }
                }

                if (adapter.getlist().size > position && adapter.getlist()[position] is TopadsHeadlineUiModel) {
                    adapter.getlist().removeAt(position)
                    recyclerView.post {
                        adapter.notifyItemRemoved(position)
                    }
                }
            }
        }
    }

    private fun isAllowedNotify(isComputingLayout: Boolean, position: Int): Boolean {
        return !isComputingLayout && position >= 0
    }

    private fun sendTopadsUrlClick(
        adClickUrl: String,
        id: String = "",
        uri: String = "",
        fullEcs: String? = ""
    ) {
        topAdsUrlHitter.hitClickUrl(
            this::class.java.simpleName,
            adClickUrl,
            id,
            uri,
            fullEcs,
            ""
        )
    }

    private fun sendTopadsImpression(adViewUrl: String, id: String, uri: String, fullEcs: String?) {
        topAdsUrlHitter.hitImpressionUrl(
            this::class.java.simpleName,
            adViewUrl,
            id,
            uri,
            fullEcs
        )
    }

    private fun getAdClickUrl(positionInFeed: Int): String {
        var adClickUrl = ""
        if (adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            adClickUrl = item.cpmModel?.data?.firstOrNull()?.adClickUrl ?: ""
        }
        return adClickUrl
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagModelNew,
        context: Context,
        shopId: String
    ) {
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId
        val trackerIdThreeDotsClick = if (item.isRilisanSpl || item.isFlashSaleToko) {
            if (item.isFollowed) {
                "13444"
            } else {
                "13430"
            }
        } else {
            ""
        }

        val feedTrackerData = getFeedTrackerDataModelFromPosition(
            item.positionInFeed,
            trackerId = trackerIdThreeDotsClick,
            product = item.product
        )
        feedTrackerData?.let {
            feedAnalytics.eventClickBottomSheetMenu(it)
        }
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        if (!sheet.isAdded) {
            sheet.show(childFragmentManager, "")
        }
        sheet.shareProductCB = {
            val shareTrackerId = getTrackerIdForCampaignSaleTracker(
                item.positionInFeed,
                trackerIdAsgcRecom = "13433",
                trackerIdAsgc = "13447"
            )
            onShareProduct(item, shareTrackerId)
        }
        sheet.addToCartCB = {
            onTagSheetItemBuy(
                item
            )
        }
        sheet.addToWIshListCB = {
            addToWishList(
                finalID,
                item.id,
                item.postType,
                item.isFollowed,
                item.shopId,
                item.playChannelId,
                item.mediaType,
                positionInFeed = item.positionInFeed,
                hasVoucher = feedTrackerData?.hasVoucher ?: false,
                authorType = feedTrackerData?.authorType ?: ""
            )
        }
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
        if (postTagItemList.isNotEmpty()) {
            feedAnalytics.eventImpressionProductBottomSheet(
                activityId,
                postTagItemList,
                shopId,
                type,
                isFollowed,
                false,
                mediaType = mediaType,
                hasVoucher = hasVoucher,
                authorType = authorType
            )
        }
    }

    override fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int) {
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()
        val trackerId = if (item.isFlashSaleToko || item.isRilisanSpl) {
            if (item.isFollowed) {
                "13445"
            } else {
                "13431"
            }
        } else {
            ""
        }
        addToWishList(
            finalID,
            item.id,
            item.postType,
            item.isFollowed,
            item.shopId,
            item.playChannelId,
            item.mediaType,
            rowNumber,
            item.positionInFeed,
            trackerId,
            getTrackerLabelSuffixFromPosition(item.positionInFeed),
            getTrackerHasVoucherFromPosition(item.positionInFeed)
        )
    }

    override fun onAddToCartButtonClicked(item: ProductPostTagModelNew) {
        val list = adapter.getList()
        val position = item.positionInFeed
        var card: FeedXCard? = null
        if (position in 0 until list.size && list[position] is DynamicPostUiModel) {
            val item = list[position] as DynamicPostUiModel
            card = item.feedXCard
        }
        if (card != null && shouldShowFollowerBottomSheet(card)) {
            showFollowerBottomSheet(item.positionInFeed, card.campaign.status)
        } else {
            onTagSheetItemBuy(
                item
            )
        }
    }

    override fun onTaggedProductCardClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int,
        mediaType: String
    ) {
        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            val card = item.feedXCard
            if (card.tags.isNotEmpty()) {
                feedAnalytics.eventClickBSitem(
                    getFeedTrackerDataModel(
                        card,
                        trackerId = getTrackerIdForCampaignSaleTracker(
                            positionInFeed,
                            trackerIdAsgc = "13443"
                        ),
                        product = postTagItem
                    ),
                    itemPosition
                )
            }
        }

        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            if (item.feedXCard.tags.isNotEmpty()) {
                feedAnalytics.eventClickBSitem(
                    getFeedTrackerDataModel(item.feedXCard, product = postTagItem),
                    productPosition = itemPosition
                )
            }
            sendTopadsUrlClick(getAdClickUrl(positionInFeed))
        }
        onGoToLink(redirectUrl)
    }

    override fun setContainerListener(listener: FeedPlusContainerListener) {
        this.mContainerListener = listener
    }

    private fun getFeedTrackerDataModel(
        feedXCard: FeedXCard,
        positionInFeed: Int = 0,
        trackerId: String = "",
        product: FeedXProduct = FeedXProduct()
    ) = FeedTrackerData(
        postId = if (feedXCard.typename == TYPE_FEED_X_CARD_PLAY) feedXCard.playChannelID else feedXCard.id,
        media = feedXCard.media.first(),
        postType = feedXCard.typename,
        isFollowed = feedXCard.followers.isFollowed,
        shopId = feedXCard.author.id,
        mediaType = if (feedXCard.lastCarouselIndex in feedXCard.media.indices) {
            feedXCard.media[feedXCard.lastCarouselIndex].type
        } else {
            ""
        },
        positionInFeed = positionInFeed,
        contentSlotValue = (feedXCard.contentScore).firstOrNull()?.value ?: String.EMPTY,
        trackerId = trackerId,
        campaignStatus = getTrackerLabelSuffixForCampaignSaleTracker(feedXCard),
        product = product,
        productId = product.id,
        mediaIndex = feedXCard.lastCarouselIndex,
        hasVoucher = feedXCard.hasVoucher,
        authorType = feedXCard.author.type.toString()
    )

    private fun getFeedTrackerDataModelFromPosition(
        positionInFeed: Int,
        trackerId: String = "",
        product: FeedXProduct = FeedXProduct()
    ): FeedTrackerData? {
        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            val feedXCard = item.feedXCard
            return getFeedTrackerDataModel(feedXCard, positionInFeed, trackerId, product = product)
        }
        return null
    }

    private fun getContentScoreFromPosition(positionInFeed: Int) =
        if (positionInFeed in 0 until adapter.getlist().size && adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val model = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            model.feedXCard.contentScore.firstOrNull()?.value ?: String.EMPTY
        } else {
            ""
        }

    override fun onShopRecomCloseClicked(itemID: Long) {
        feedShopRecomWidgetAnalytics.sendClickXShopRecommendationEvent(itemID.toString())
        feedViewModel.handleClickRemoveButtonShopRecom(itemID)
    }

    override fun onShopRecomFollowClicked(itemID: Long) {
        if (userSession.isLoggedIn) {
            feedShopRecomWidgetAnalytics.sendClickFollowShopRecommendationEvent(itemID.toString())
            feedViewModel.handleClickFollowButtonShopRecom(itemID)
        } else {
            onGoToLogin()
        }
    }

    override fun onShopRecomItemClicked(
        itemID: Long,
        appLink: String,
        imageUrl: String,
        postPosition: Int
    ) {
        feedShopRecomWidgetAnalytics.sendClickShopRecommendationEvent(
            eventLabel = itemID.toString(),
            shopId = itemID.toString(),
            shopsImageUrl = imageUrl,
            postPosition = postPosition
        )
        RouteManager.route(requireContext(), appLink)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        shopRecomImpression.initiateShopImpress(item) { shopImpress ->
            feedShopRecomWidgetAnalytics.sendImpressionShopRecommendationEvent(
                item.id.toString(),
                shopImpress,
                postPosition
            )
        }
    }

    override fun onShopRecomLoadingNextPage(nextCursor: String) {
        feedViewModel.getShopRecomWidget(nextCursor)
    }
}
