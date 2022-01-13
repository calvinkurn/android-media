package com.tokopedia.feedplus.view.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
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
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.createpost.common.view.customview.PostProgressUpdateView
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.bottomsheets.*
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.mapper.*
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder.Companion.PAYLOAD_ANIMATE_LIKE
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.*
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
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
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
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
import io.embrace.android.embracesdk.Embrace
import kotlinx.android.synthetic.main.fragment_feed_plus.*
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by nisie on 5/15/17.
 */

const val CLICK_FOLLOW_TOPADS = "click - follow - topads"
const val IMPRESSION_CARD_TOPADS = "impression - card - topads"
const val IMPRESSION_PRODUCT_TOPADS = "impression - product - topads"
const val CLICK_CEK_SEKARANG = "click - cek sekarang - topads"
const val CLICK_SHOP_TOPADS = "click - shop - topads"
const val CLICK_PRODUCT_TOPADS = "click - product - topads"
const val TYPE_CONTENT_PREVIEW_PAGE = "content-preview-page"

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
    PlayWidgetListener, TopAdsHeadlineListener,
    ShareCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var mainContent: View
    private lateinit var newFeed: View
    private lateinit var newFeedReceiver: BroadcastReceiver
    private lateinit var postUpdateSwipe: PostProgressUpdateView.PostUpdateSwipe
    private lateinit var adapter: FeedPlusAdapter
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var infoBottomSheet: TopAdsInfoBottomSheet

    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator

    private var layoutManager: LinearLayoutManager? = null
    private var loginIdInt: Int = 0
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
    private lateinit var productTagBS: ProductItemInfoBottomSheet
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
        private const val POST_TYPE = "post_type"
        private const val SHOP_NAME = "shop_name"


        private val TAG = FeedPlusFragment::class.java.simpleName
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val FEED_TRACE = "mp_feed"
        private const val AFTER_POST = "after_post"
        private const val TRUE = "true"
        private const val FEED_DETAIL = "feedcommunicationdetail"
        private const val BROADCAST_FEED = "BROADCAST_FEED"
        private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"
        private const val PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED"
        private const val PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED"
        private const val REMOTE_CONFIG_ENABLE_INTEREST_PICK = "mainapp_enable_interest_pick"
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
        Embrace.getInstance().startEvent(FEED_TRACE, null, false)
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
            })

            followKolResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessFollowUnfollowKol(data.rowNumber)
                            if (!data.isFollow) {
                                showToast(
                                    getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast),
                                    Toaster.TYPE_NORMAL
                                )
                            }
                        } else {
                            if (data.isFollow)
                                data.errorMessage =
                                    getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_fail_toast)
                            else
                                data.errorMessage =
                                    getString(com.tokopedia.feedcomponent.R.string.feed_component_follow_fail_toast)
                            onErrorFollowUnfollowKol(data)
                        }
                    }
                    is Fail -> {
                        val message = it.throwable.message
                            ?: getString(R.string.default_request_error_unknown)
                        showToast(message, Toaster.TYPE_ERROR)
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
                        when (it.throwable.cause) {
                            is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                view?.let {
                                    showNoInterNetDialog(it.context)
                                }
                            }
                            else -> {
                                val message = getString(R.string.default_request_error_unknown)
                                showToast(message, Toaster.TYPE_ERROR)
                            }
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
                        val message = getString(R.string.default_request_error_unknown)
                        showToast(message, Toaster.TYPE_ERROR)
                    }
                }
            })

            atcResp.observe(lifecycleOwner, Observer {
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
                                        feedAnalytics.eventOnTagSheetItemBuyClicked(
                                            data.activityId,
                                            data.postType,
                                            data.isFollowed,
                                            data.shopId
                                        )
                                        onAddToCartSuccess()
                                    }).show()
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
                            getString(R.string.default_request_error_unknown),
                            Toaster.TYPE_ERROR
                        )
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
                                val message = it.throwable.message
                                    ?: getString(R.string.default_request_error_unknown)
                                showToast(message, Toaster.TYPE_ERROR)
                            }
                        }

                    }
                }
            })

            trackAffiliateResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Fail -> {
                        val message = it.throwable.localizedMessage ?: ""
                        showToast(message, Toaster.TYPE_ERROR)
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

            viewTrackResponse.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        onSuccessAddViewVODPost(it.data.rowNumber)
                    }
                }
            })

            reportResponse.observe(lifecycleOwner, Observer {
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
                if (intent != null && intent.action != null) {
                    if (intent.action == BROADCAST_FEED) {
                        val isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false)
                        if (intent.extras == null || intent.extras?.isEmpty == true)
                            isFeedPageShown = true
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        layoutManager = NpaLinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

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
                                var position = getCurrentPosition()
                                FeedScrollListenerNew.onFeedScrolled(
                                    recyclerView,
                                    adapter.getList()
                                )
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
                showSnackbar(data.getStringExtra("message")?: "")
            OPEN_KOL_COMMENT -> if (resultCode == Activity.RESULT_OK) {
                val serverErrorMsg = data.getStringExtra(COMMENT_ARGS_SERVER_ERROR_MSG)
                if (!TextUtils.isEmpty(serverErrorMsg)) {
                    view?.let {
                        Toaster.build(
                            it,
                            serverErrorMsg?: "",
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(R.string.cta_refresh_feed),
                            View.OnClickListener { onRefresh() })
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
                        data.getStringExtra(CONTENT_REPORT_RESULT_ERROR_MSG) ?: ""
                    )
                }
            }
            OPEN_INTERESTPICK_DETAIL -> {
                val selectedIdList =
                    data.getIntegerArrayListExtra(FeedOnboardingFragment.EXTRA_SELECTED_IDS)
                adapter.getlist().firstOrNull { it is OnboardingViewModel }?.let {
                    (it as? OnboardingViewModel)?.dataList?.forEach { interestPickDataViewModel ->
                        interestPickDataViewModel.isSelected =
                            selectedIdList?.contains(interestPickDataViewModel.id) == true
                    }
                }
                adapter.notifyItemChanged(0, OnboardingViewHolder.PAYLOAD_UPDATE_ADAPTER)
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
                        if (changeLike != item.feedXCard.like.isLiked)
                            onSuccessLikeDislikeKolPost(positionInFeed)
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
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY
            )
        )
        analytics.eventTrackingEnhancedEcommerce(
            FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt)
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
                Integer.valueOf(shop.adId),
                FeedEnhancedTracking.Promotion
                    .createContentNameTopadsShop(),
                shop.adRefKey,
                position,
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                Integer.valueOf(shop.adId),
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY
            )
        )

        analytics.eventTrackingEnhancedEcommerce(
            FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt)
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
            feedAnalytics.userVisitsFeed(userSession.isLoggedIn)
        }
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        registerNewFeedReceiver()
        if (userVisibleHint) {
            loadData(userVisibleHint)
        }
    }

    override fun onPause() {
        if (isFeedPageShown)
            isUserEventTrackerDoneOnResume = true
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        unRegisterNewFeedReceiver()
        analytics.sendPendingAnalytics()
        feedAnalytics.sendPendingAnalytics()
    }

    private fun resetImagePostWhenFragmentNotVisible(){
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val firstPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
        val lastPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
        for (i in firstPosition..lastPosition) {
            val item = getCardViewModel(adapter.getList(), i)
            val topadsItem = getTopadsCardViewModel(adapter.getList(),i)
            if (isImageCard(adapter.getList(), i)) {
                if (item != null) {
                    Objects.requireNonNull(adapter)
                        .notifyItemChanged(i, DynamicPostNewViewHolder.PAYLOAD_POST_VISIBLE)
                }
            }
            else if (isTopadsImageCard(adapter.getList(), i)) {
                if (topadsItem != null) {
                    Objects.requireNonNull(adapter)
                            .notifyItemChanged(i, TopAdsHeadlineV2ViewHolder.PAYLOAD_POST_VISIBLE)
                }
            }
        }
    }
    private fun getCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            return (list[position] as DynamicPostUiModel).feedXCard.media.firstOrNull()
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
        return null
    }

    private fun isImageCard(list: List<Visitable<*>>, position: Int): Boolean {

        if (position >= 0 && list.size > position && list[position] is DynamicPostUiModel) {
            val item = (list[position] as DynamicPostUiModel).feedXCard
            return (item.typename == TYPE_FEED_X_CARD_POST
                    && (item.media.isNotEmpty()
                    && (item.media.find { it.type == TYPE_IMAGE } != null)))
        }
        return false
    }

    private fun getTopadsCardViewModel(list: List<Visitable<*>>, position: Int): FeedXMedia? {
        try {
            return (list[position] as TopadsHeadLineV2Model).feedXCard.media.firstOrNull()
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
        }
        return null
    }

    private fun isTopadsImageCard(list: List<Visitable<*>>, position: Int): Boolean {

        if (position >= 0 && list.size > position && list[position] is TopadsHeadLineV2Model) {
            val item = (list[position] as TopadsHeadLineV2Model).feedXCard
            return (item.typename == TYPE_TOPADS_HEADLINE_NEW
                    && (item.media.isNotEmpty()
                    && (item.media.find { it.type == TYPE_IMAGE } != null)))
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

    override fun trackContentClick(
        hasMultipleContent: Boolean,
        activityId: String,
        activityType: String,
        position: String
    ) {

    }

    override fun trackTooltipClick(
        hasMultipleContent: Boolean,
        activityId: String,
        activityType: String,
        position: String
    ) {

    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            feedViewModel.doFollowKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnfollowKol(id, rowNumber)
        } else {
            onGoToLogin()
        }

    }

    override fun onLikeKolClicked(
        rowNumber: Int, id: Int, hasMultipleContent: Boolean,
        activityType: String
    ) {
        if (userSession.isLoggedIn) {
            feedViewModel.doLikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    override fun onUnlikeKolClicked(
        rowNumber: Int, id: Int, hasMultipleContent: Boolean,
        activityType: String
    ) {
        if (userSession.isLoggedIn) {
            feedViewModel.doUnlikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

    override fun onGoToKolComment(
        rowNumber: Int, id: Int, hasMultipleContent: Boolean,
        activityType: String
    ) {
        //not used
    }

    fun gotToKolComment(
        rowNumber: Int, id: Int, authorType: String,
        isVideo: Boolean, isFollowed: Boolean, type: String
    ) {
        val intent = RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT_NEW,
                mapOf(
                    COMMENT_ARGS_POSITION to rowNumber.toString()
                )
            ),
            id.toString()
        )
        intent.putExtra(ARGS_AUTHOR_TYPE, authorType)
        intent.putExtra(ARGS_VIDEO, isVideo)
        intent.putExtra(ARGS_POST_TYPE, type)
        intent.putExtra(ARGS_IS_POST_FOLLOWED, isFollowed)
        startActivityForResult(intent, OPEN_KOL_COMMENT)
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
        onLikeClick(positionInFeed, id, isLiked)
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            RouteManager.getIntent(
                requireContext(),
                UriUtil.buildUriAppendParam(
                    ApplinkConstInternalContent.COMMENT,
                    mapOf(
                        COMMENT_ARGS_POSITION to positionInFeed.toString(),
                        COMMENT_ARGS_POSITION_COLUMN to columnNumber.toString()
                    )
                ),
                id.toString()
            ).run { startActivityForResult(this, KOL_COMMENT_CODE) }
        } else {
            onGoToLogin()
        }
    }

    override fun onEditClicked(
        hasMultipleContent: Boolean, activityId: String,
        activityType: String
    ) {

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
        isViewResumed: Boolean = if (view == null) false else viewLifecycleOwner.lifecycle.currentState.isAtLeast(
            Lifecycle.State.RESUMED
        ),
        isUserVisibleHint: Boolean = userVisibleHint,
        isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) playWidgetCoordinator.onResume()
            else playWidgetCoordinator.onPause()
        }
    }

    private fun createDeleteDialog(
        rowNumber: Int,
        id: Int,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        isVideo: Boolean
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
                id.toString(),
                shopId,
                type,
                isFollowed,
                isVideo
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
            item.feedXCard.comments.count = item.feedXCard.comments.count + totalNewComment
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

    override fun onBannerItemClick(
        positionInFeed: Int, adapterPosition: Int,
        redirectUrl: String
    ) {
        onGoToLink(redirectUrl)

        if (adapter.getlist()[positionInFeed] is BannerViewModel) {
            val (itemViewModels) = adapter.getlist()[positionInFeed] as BannerViewModel
            trackBannerClick(
                adapterPosition,
                itemViewModels[adapterPosition].trackingBannerModel
            )
        }
    }

    override fun onRecommendationAvatarClick(
        positionInFeed: Int, adapterPosition: Int,
        redirectLink: String, postType: String, authorId: String
    ) {
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

    override fun onRecommendationActionClick(
        positionInFeed: Int, adapterPosition: Int,
        id: String, type: String,
        isFollow: Boolean
    ) {
        if (type == FollowCta.AUTHOR_USER) {
            val userIdInt = id.toIntOrZero()

            if (isFollow) {
                feedViewModel.doUnfollowKolFromRecommendation(
                    userIdInt,
                    positionInFeed,
                    adapterPosition
                )
            } else {
                feedViewModel.doFollowKolFromRecommendation(
                    userIdInt,
                    positionInFeed,
                    adapterPosition
                )
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
        if (adapter.getlist()[positionInFeed] is TopadsShopUiModel) {
            val (_, _, _, trackingList) = adapter.getlist()[positionInFeed] as TopadsShopUiModel
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
        activityId: Int,
        activityName: String,
        followCta: FollowCta,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        isVideo: Boolean,
        isCaption: Boolean
    ) {
        onGoToLink(redirectUrl)
        feedAnalytics.eventClickFeedAvatar(
            activityId.toString(),
            type,
            isFollowed,
            shopId,
            isVideo,
            isCaption
        )
    }

    override fun onHeaderActionClick(
        positionInFeed: Int, id: String, type: String,
        isFollow: Boolean, postType: String, isVideo: Boolean
    ) {
        if (userSession.isLoggedIn) {
            if (type == FollowCta.AUTHOR_USER) {
                val userIdInt = id.toIntOrZero()
                if (isFollow) {
                    onUnfollowKolClicked(positionInFeed, userIdInt)
                } else {
                    onFollowKolClicked(positionInFeed, userIdInt)
                }

            } else if (type == FollowCta.AUTHOR_SHOP) {
                feedViewModel.doToggleFavoriteShop(positionInFeed, 0, id, isFollow)
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

    override fun onMenuClick(
        positionInFeed: Int,
        postId: Int,
        reportable: Boolean,
        deletable: Boolean,
        editable: Boolean,
        isFollowed: Boolean,
        authorId: String,
        authorType: String,
        postType: String,
        isVideo: Boolean,
        caption: String,
        playChannelId: String
    ) {
        if (context != null) {
            val finalId = if (postType == TYPE_FEED_X_CARD_PLAY) playChannelId else postId.toString()
            feedAnalytics.evenClickMenu(finalId, postType, isFollowed, authorId, isVideo)
            val sheet = MenuOptionsBottomSheet.newInstance(
                reportable, isFollowed,
                deletable
            )
            sheet.show((context as FragmentActivity).supportFragmentManager, "")
            sheet.onReport = {
                feedAnalytics.eventClickThreeDotsOption(
                    finalId,
                    "laporkan",
                    postType,
                    isFollowed,
                    authorId,
                    isVideo
                )
                if (userSession.isLoggedIn) {
                    context?.let {
                        reportBottomSheet = ReportBottomSheet.newInstance(
                            postId,
                            context = object : ReportBottomSheet.OnReportOptionsClick {
                                override fun onReportAction(
                                    reasonType: String,
                                    reasonDesc: String
                                ) {
                                    feedViewModel.sendReport(
                                        positionInFeed,
                                        postId,
                                        reasonType,
                                        reasonDesc,
                                        "content"
                                    )
                                }
                            })
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
                    finalId, "unfollow",
                    postType,
                    isFollowed, authorId, isVideo
                )
                if (userSession.isLoggedIn)
                    onHeaderActionClick(
                        positionInFeed,
                        authorId,
                        authorType,
                        isFollowed,
                        isVideo = isVideo
                    ) else onGoToLogin()
            }
            sheet.onDelete = {
                createDeleteDialog(positionInFeed, postId, authorId, postType, isFollowed, isVideo)
                feedAnalytics.clickDeleteThreeDotsPage(
                    finalId, authorId,
                    postType,
                    isFollowed, isVideo
                )

            }
            sheet.onDismiss = {
                feedAnalytics.eventClickGreyAreaThreeDots(
                    finalId,
                    postType,
                    isFollowed, authorId
                )
            }
            sheet.onEdit = {
                openEditPostPage(caption, postId.toString())
            }

            sheet.onClosedClicked = {
                feedAnalytics.eventCloseThreeDotBS(
                    finalId,
                    postType,
                    isFollowed, authorId
                )

            }
        }
    }
    private fun openEditPostPage(caption: String, postId: String) {
        var createPostViewModel = CreatePostViewModel()
        createPostViewModel.caption = caption
        createPostViewModel.postId = postId

        val intent = RouteManager.getIntent(context,INTERNAL_AFFILIATE_CREATE_POST_V2)
        intent.putExtra("author_type",TYPE_CONTENT_PREVIEW_PAGE)
        intent.putExtra(CreatePostViewModel.TAG,createPostViewModel)
        startActivity(intent)
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean, postType: String, isFollowed: Boolean, type: Boolean, shopId: String, isVideo: Boolean, playChannelId: String) {
        feedAnalytics.eventClickLikeButton(
            if(postType == TYPE_FEED_X_CARD_PLAY) playChannelId else id.toString(),
            type,
            isLiked,
            postType,
            isFollowed,
            shopId,
            isVideo
        )
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int, authorType: String, type: String, isFollowed: Boolean, isVideo: Boolean, shopId: String, playChannelId: String, isClickIcon: Boolean) {
        if (isClickIcon)
        feedAnalytics.eventClickOpenComment(if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else id.toString(), type, isFollowed, authorType, isVideo)
        else if (type == TYPE_FEED_X_CARD_PLAY && !isClickIcon)
        feedAnalytics.eventClickLihatSemuaComment(playChannelId, type, isFollowed, authorType, isVideo)

        gotToKolComment(positionInFeed, id, authorType, isVideo, isFollowed, type)
    }

    override fun onShareClick(
        positionInFeed: Int, id: Int, title: String,
        description: String, url: String,
        imageUrl: String,
        typeASGC: Boolean,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        video: Boolean,
        isTopads: Boolean,
        playChannelId: String
    ) {
        val typeVOD = type == TYPE_FEED_X_CARD_PLAY
        activity?.let {
            val urlString = when {
                typeASGC -> {
                    String.format(getString(R.string.feed_share_asgc_weblink), id.toString())
                }
                typeVOD -> {
                    String.format(getString(R.string.feed_vod_share_weblink), playChannelId)
                }
                else -> {
                    String.format(getString(R.string.feed_share_weblink), id.toString())
                }
            }

            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
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
        if (type == TYPE_FEED_X_CARD_PLAY)
        feedAnalytics.eventClickOpenShare(playChannelId, type, isFollowed, shopId, video)
        else
        feedAnalytics.eventClickOpenShare(id.toString(), type, isFollowed, shopId, video)
    }

    override fun onStatsClick(
        title: String,
        activityId: String,
        productIds: List<String>,
        likeCount: Int,
        commentCount: Int
    ) {
        //Not used
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

    override fun onPostTagItemBSClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int
    ) {

        if (adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            if (item.feedXCard.tags.isNotEmpty())
            feedAnalytics.eventClickBSitem(
                if (item.feedXCard.typename == TYPE_FEED_X_CARD_PLAY) item.feedXCard.playChannelID else item.feedXCard.id,
                item.feedXCard.tags,
                itemPosition,
                item.feedXCard.typename,
                item.feedXCard.followers.isFollowed,
                item.feedXCard.author.id
            )
        }

        if (adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            if (item.feedXCard.tags.isNotEmpty())
            feedAnalytics.eventClickBSitem(
                    item.feedXCard.id,
                    item.feedXCard.tags,
                    itemPosition,
                    item.feedXCard.typename,
                    item.feedXCard.followers.isFollowed,
                    item.feedXCard.author.id
            )
           sendTopadsUrlClick(getAdClickUrl(positionInFeed))
        }
        onGoToLink(redirectUrl)
    }

    override fun onFullScreenCLick(feedXCard: FeedXCard, positionInFeed: Int, redirectUrl: String, currentTime: Long, shouldTrack: Boolean, isFullScreenButton: Boolean) {
        if (isFullScreenButton)
        feedAnalytics.eventClickFullScreenIconVOD(feedXCard.playChannelID, feedXCard.typename, feedXCard.followers.isFollowed, feedXCard.author.id)
        else
        feedAnalytics.eventClicklanjutMenontonVOD(feedXCard.playChannelID, feedXCard.typename, feedXCard.followers.isFollowed, feedXCard.author.id)
        val finalApplink = if (!shouldTrack) {
             Uri.parse(redirectUrl)
                    .buildUpon()
                    .appendQueryParameter(START_TIME, currentTime.toString())
                    .appendQueryParameter(SHOULD_TRACK, shouldTrack.toString())
                    .build().toString()
        }
        else{
            Uri.parse(redirectUrl)
                    .buildUpon()
                    .appendQueryParameter(START_TIME, currentTime.toString())
                    .appendQueryParameter(SOURCE_TYPE, VOD_POST)
                    .build().toString()
        }

        onGoToLink(finalApplink)
    }

    override fun addVODView(feedXCard: FeedXCard, playChannelId: String, rowNumber: Int, time: Long, hitTrackerApi: Boolean) {
        if (!hitTrackerApi)
        feedAnalytics.eventAddView(playChannelId,feedXCard.typename,feedXCard.followers.isFollowed,feedXCard.author.id,time)
        if (hitTrackerApi)
        feedViewModel.trackVisitChannel(playChannelId, rowNumber)
    }

    override fun onPostTagBubbleClick(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            adClickUrl: String
    ) {
        if (adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
            val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
            feedAnalytics.eventClickPostTagitem(
                item.feedXCard.id,
                postTagItem,
                0,
                item.feedXCard.typename,
                item.feedXCard.followers.isFollowed,
                item.feedXCard.author.id
            )
        }

        if (adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            val isFollowed = item.cpmModel?.data?.firstOrNull()?.cpm?.cpmShop?.isFollowed
            val id  = item.cpmModel?.data?.get(0)?.id?:""
            if (isFollowed != null) {
                feedAnalytics.eventClickPostTagitem(
                        id,
                        postTagItem,
                        0,
                        TYPE_TOPADS_HEADLINE_NEW,
                        isFollowed,
                        id
                )
            }
            sendTopadsUrlClick(adClickUrl)
        }
        onGoToLink(redirectUrl)
    }
    override fun onPostTagItemBSImpression(
        activityId: String,
        products: List<FeedXProduct>,
        type: String,
        shopId: String,
        isFollowed: Boolean
    ) {
        feedAnalytics.eventImpressionProductBottomSheet(
                activityId,
                products,
                shopId,
                type,
                isFollowed,
                false
        )
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
                positionInFeed
            )
        }
    }

    override fun userCarouselImpression(
            activityId: String,
            media: FeedXMedia,
            positionInFeed: Int,
            type: String,
            isFollowed: Boolean,
            shopId: String,
            postPosition: Int,
            cpmData: CpmData,
            products: List<Product>
    ) {
        if (type == TYPE_TOPADS_HEADLINE_NEW) {
            onTopAdsProductItemListsner(positionInFeed, products[positionInFeed], cpmData)
            TopAdsGtmTracker.eventTopAdsHeadlineShopView(postPosition, cpmData, "", userSession.userId)
        }
        feedAnalytics.eventImpression(
            activityId,
            media,
            positionInFeed,
            type,
            isFollowed,
            shopId,
            (postPosition + 1).toString()
        )
    }

    override fun userGridPostImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String
    ) {
        feedAnalytics.eventImpressionPostASGC(activityId, positionInFeed, "", shopId)
    }

    override fun userProductImpression(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        shopId: String,
        productList: List<FeedXProduct>
    ) {
        feedAnalytics.eventImpressionProduct(
            activityId,
            productId,
            productList,
            shopId
        )
    }

    override fun onImageClick(
        positionInFeed: Int, contentPosition: Int,
        redirectLink: String
    ) {
        onGoToLink(redirectLink)

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onMediaGridClick(
        positionInFeed: Int,
        contentPosition: Int,
        redirectLink: String,
        isSingleItem: Boolean
    ) {
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
        onGoToLink(item.applink)
    }

    override fun onPostTagItemBuyClicked(
        positionInFeed: Int,
        postTagItem: PostTagItem,
        authorType: String
    ) {
    }

    private fun onTagSheetItemBuy(
        activityId: String,
        positionInFeed: Int,
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        playChannelId: String,
        shopName : String
    ) {
        if (type == TYPE_FEED_X_CARD_PLAY || type == TYPE_TOPADS_HEADLINE_NEW)
            feedAnalytics.eventAddToCartFeedVOD(if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else activityId, postTagItem.id, postTagItem.name, postTagItem.priceFmt, 1, shopId, shopName, type, isFollowed)
        else
            feedAnalytics.eventAddToCartFeedVOD(activityId, postTagItem.id, postTagItem.name, postTagItem.price.toString(), 1, shopId, shopName, type, isFollowed)
        if (userSession.isLoggedIn) {
            if (::productTagBS.isInitialized) {
                productTagBS.dismissedByClosing = true
                productTagBS.dismiss()
            }
            feedViewModel.doAtc(postTagItem, shopId, type, isFollowed, activityId)
        } else {
            onGoToLogin()
        }
    }

    override fun onYoutubeThumbnailClick(
        positionInFeed: Int, contentPosition: Int,
        youtubeId: String
    ) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(context, redirectUrl)
        }

        if (adapter.getlist()[positionInFeed] is DynamicPostViewModel) {
            val (_, _, _, _, _, _, _, _, trackingPostModel) = adapter.getlist()[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(positionInFeed, trackingPostModel)
        }
    }

    override fun onPollOptionClick(
        positionInFeed: Int, contentPosition: Int, option: Int,
        pollId: String, optionId: String, isVoted: Boolean,
        redirectLink: String
    ) {
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

    override fun onReadMoreClicked(
        postId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        isVideo: Boolean
    ) {
        feedAnalytics.eventClickReadMoreNew(postId, shopId, type, isFollowed, isVideo)
    }

    override fun onImageClicked(
        activityId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String
    ) {
        feedAnalytics.eventImageClicked(activityId, type, isFollowed, shopId)
    }

    override fun onTagClicked(
            postId: Int,
            products: List<FeedXProduct>,
            listener: DynamicPostViewHolder.DynamicPostListener,
            id: String,
            type: String,
            isFollowed: Boolean,
            isVideo: Boolean,
            positionInFeed: Int,
            playChannelId: String,
            shopName: String
    ) {
        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId.toString()
        productTagBS = ProductItemInfoBottomSheet()
        feedAnalytics.eventTagClicked(finalId, type, isFollowed, id, isVideo)
        productTagBS.show(
            childFragmentManager,
            products,
            listener,
            postId,
            id,
            type,
            isFollowed,
            positionInFeed,
            playChannelId,
            shopName = shopName
        )
        productTagBS.closeClicked = {
            feedAnalytics.eventClickCloseProductInfoSheet(finalId, type, isFollowed, id)
        }
        productTagBS.disMissed = {
            feedAnalytics.eventClickGreyArea(finalId, type, isFollowed, id)
        }
    }

    private fun addToWishList(
        postId: String,
        productId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        playChannelId: String
    ) {
        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId

        feedAnalytics.eventAddToWishlistClicked(
            finalId,
            productId,
            type,
            isFollowed,
            shopId
        )
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        feedViewModel.addWishlist(
            postId,
            productId,
            shopId,
            0,
            type,
            isFollowed,
            ::onWishListFail,
            ::onWishListSuccess
        )
    }

    private fun onWishListFail(s: String) {
        showToast(s, Toaster.TYPE_ERROR)
    }

    private fun onWishListSuccess(
        activityId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean
    ) {
        Toaster.build(
            requireView(),
            getString(R.string.feed_added_to_wishlist),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            getString(R.string.feed_go_to_wishlist),
            View.OnClickListener {
                feedAnalytics.eventOnTagSheetItemBuyClicked(activityId, type, isFollowed, shopId)
                RouteManager.route(context, ApplinkConst.WISHLIST)
            }).show()
    }

    private fun onShareProduct(
        id: Int,
        title: String,
        description: String,
        url: String,
        imageUrl: String,
        activityId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        isTopads:Boolean = false
    ) {
        feedAnalytics.eventonShareProductClicked(
            activityId,
            id.toString(),
            type,
            isFollowed, shopId
        )
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        val urlString: String = if (isTopads) {
            shareBottomSheetProduct = true
            String.format(getString(R.string.feed_share_pdp), id.toString())
        } else{
            shareBottomSheetProduct = false
            url
        }
        activity?.let {
            val linkerBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                .setName(title)
                .setDescription(description)
                .setImgUri(imageUrl)
                .setUri(url)
                .setDeepLink(url)
                .setType(LinkerData.FEED_TYPE)
                .setDesktopUrl(urlString)

            if (isTopads) {
                linkerBuilder.setOgImageUrl(imageUrl)
            }
            shareData = linkerBuilder.build()
            val linkerShareData = DataMapper().getLinkerShareData(shareData)
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    this
                )
            )
        }

    }

    override fun onBottomSheetMenuClicked(
        item: ProductPostTagViewModelNew,
        context: Context,
        shopId: String
    ) {
        val finalID = if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()
        feedAnalytics.eventClickBottomSheetMenu(
            finalID,
            item.postType,
            item.isFollowed,
            item.shopId
        )
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        sheet.show((context as FragmentActivity).supportFragmentManager, "")
        sheet.shareProductCB = {
            onShareProduct(
                item.id.toIntOrZero(),
                item.text,
                item.description,
                item.weblink,
                item.imgUrl,
                finalID,
                item.postType,
                item.isFollowed,
                item.shopId,
                item.isTopads
            )
        }
        sheet.addToCartCB = {
            onTagSheetItemBuy(
                finalID,
                item.positionInFeed,
                item.product,
                item.shopId,
                item.postType,
                item.isFollowed,
                item.playChannelId,
                item.shopName
            )
        }
        sheet.addToWIshListCB = {
            addToWishList(finalID, item.id, item.postType, item.isFollowed, item.shopId, item.playChannelId)
        }
    }

    override fun muteUnmuteVideo(postId: String, mute: Boolean, id: String, isFollowed: Boolean, isVOD: Boolean) {
        if (isVOD)
        feedAnalytics.clickSoundVOD(postId, mute, id, isFollowed)
        else
        feedAnalytics.clickMuteButton(postId, mute, id, isFollowed)

    }

    override fun onImpressionTracking(feedXCard: FeedXCard, positionInFeed: Int) {
        feedAnalytics.eventPostImpression(
            feedXCard.id,
            feedXCard.media.first(),
            positionInFeed,
            feedXCard.type,
            feedXCard.followers.isFollowed,
            feedXCard.author.id,
            (feedXCard.media.firstOrNull()?.type != TYPE_IMAGE)
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
            false
        )
    }

    override fun onGridItemClick(positionInFeed: Int, activityId: Int, productId: String, redirectLink: String, type: String, isFollowed: Boolean, shopId: String, products: List<FeedXProduct>, index: Int) {
        onGoToLinkASGCProductDetail(redirectLink, shopId, activityId.toString(), isFollowed, type, products)

        if (redirectLink.contains(FEED_DETAIL)) {
            feedAnalytics.eventGridMoreProductCLicked(
                activityId.toString(), type, isFollowed, shopId
            )
        } else {
            if (adapter.getlist()[positionInFeed] is DynamicPostUiModel) {
                val item = (adapter.getlist()[positionInFeed] as DynamicPostUiModel)
                feedAnalytics.eventGridProductItemClicked(
                    activityId.toString(),
                    productId,
                    type,
                    isFollowed,
                    shopId,
                    products[index],
                    index
                )
            }


        }
    }

    override fun onVideoPlayerClicked(
        positionInFeed: Int,
        contentPosition: Int,
        postId: String,
        redirectUrl: String,
        authorId: String,
        authorType: String,
        isFollowed: Boolean
    ) {
        if (activity != null) {
            feedAnalytics.clickOnVideo(postId, authorId, isFollowed)
            val videoDetailIntent =
                RouteManager.getIntent(context, ApplinkConstInternalContent.VIDEO_DETAIL, postId)
            videoDetailIntent.putExtra(PARAM_CALL_SOURCE, PARAM_FEED)
            videoDetailIntent.putExtra(PARAM_POST_POSITION, positionInFeed)
            videoDetailIntent.putExtra(PARAM_VIDEO_INDEX, contentPosition)
            videoDetailIntent.putExtra(PARAM_VIDEO_AUTHOR_TYPE, authorType)
            videoDetailIntent.putExtra(PARAM_POST_TYPE, "sgc")
            videoDetailIntent.putExtra(PARAM_IS_POST_FOLLOWED, isFollowed)
            startActivityForResult(videoDetailIntent, OPEN_VIDEO_DETAIL)
        }
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
        feedAnalytics.eventWatchVideo(
            feedXCard.id,
            feedXCard.author.id,
            feedXCard.followers.isFollowed,
            duration
        )
    }

    override fun onInterestPickItemClicked(item: InterestPickDataViewModel) {
        feedAnalytics.eventClickFeedInterestPick(item.name)
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<InterestPickDataViewModel>) {
        feedAnalytics.eventClickFeedInterestPickSeeAll()
        activity?.let { fragmentActivity ->
            val bundle = Bundle()
            bundle.putIntegerArrayList(
                FeedOnboardingFragment.EXTRA_SELECTED_IDS,
                ArrayList(selectedItemList.map { it.id })
            )
            startActivityForResult(
                FeedOnboardingActivity.getCallingIntent(
                    fragmentActivity,
                    bundle
                ), OPEN_INTERESTPICK_DETAIL
            )
        }
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<InterestPickDataViewModel>) {
        view?.showLoadingTransparent()
        feedAnalytics.eventClickFeedCheckAccount(selectedItemList.size.toString())
        feedViewModel.submitInterestPickData(
            selectedItemList,
            FeedViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK,
            OPEN_INTERESTPICK_RECOM_PROFILE
        )
    }

    private fun fetchFirstPage() {
        showRefresh()
        adapter.showShimmer()
        feedViewModel.getFeedFirstPage()
        afterRefresh = false
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
                (it as FeedPlusContainerFragment).hideAllFab()
            }
            swipe_refresh_layout.isRefreshing = false
            swipe_refresh_layout.isEnabled = false
        }
    }

    private fun onSuccessSubmitInterestPickData(data: SubmitInterestResponseViewModel) {
        context?.let {
            when (data.source) {
                FeedViewModel.PARAM_SOURCE_SEE_ALL_CLICK -> {
                    startActivityForResult(
                        FeedOnboardingActivity.getCallingIntent(it, Bundle()),
                        OPEN_INTERESTPICK_DETAIL
                    )
                }
                FeedViewModel.PARAM_SOURCE_RECOM_PROFILE_CLICK -> {
                    startActivityForResult(
                        FollowRecomActivity.createIntent(
                            it,
                            data.idList.toIntArray()
                        ), OPEN_INTERESTPICK_RECOM_PROFILE
                    )
                }

            }
        }
    }

    private fun onErrorSubmitInterestPickData(throwable: Throwable) {
        Toaster.build(
            requireView(),
            ErrorHandler.getErrorMessage(activity, throwable),
            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR
        )
    }

    private fun onSuccessGetFirstFeed(firstPageDomainModel: DynamicFeedFirstPageDomainModel) {
        if (!firstPageDomainModel.shouldOverwrite) {
            adapter.updateList(firstPageDomainModel.dynamicFeedDomainModel.postList)
            return
        }
        if (isRefreshForPostCOntentCreation) {
            isRefreshForPostCOntentCreation = false
            adapter.addListItemAtTop(firstPageDomainModel.dynamicFeedDomainModel.postList[1])
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
        stopTracePerformanceMon()
    }

    private fun onErrorGetFirstFeed(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        finishLoading()
        val errorMessage = ErrorHandler.getErrorMessage(context, e)
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(
                activity, mainContent, errorMessage
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
        if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostUiModel) {
            val item = (adapter.getlist()[rowNumber] as DynamicPostUiModel)
            item.feedXCard.followers.isFollowed = !item.feedXCard.followers.isFollowed
            if (item.feedXCard.followers.isFollowed)
                item.feedXCard.followers.transitionFollow = true
            if (!item.feedXCard.followers.isFollowed)
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
                if (data.status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                    feedViewModel.doUnfollowKol(data.id, data.rowNumber)
                else
                    feedViewModel.doFollowKol(data.id, data.rowNumber)
            })
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
                }
                catch (ignored: NumberFormatException) {
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

    private fun onErrorLikeDislikeKolPost(errorMessage: String) {
        showToast(errorMessage, Toaster.TYPE_ERROR)
    }

    private fun onSuccessFollowKolFromRecommendation(data: FollowKolViewModel) {
        if (adapter.getlist()[data.rowNumber] is FeedRecommendationViewModel) {
            val (_, cards) = adapter.getlist()[data.rowNumber] as FeedRecommendationViewModel
            cards[data.position].cta.isFollow = data.isFollow
            adapter.notifyItemChanged(
                data.rowNumber,
                DynamicPostNewViewHolder.PAYLOAD_ANIMATE_FOLLOW
            )
        }
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
                getString(com.tokopedia.affiliatecommon.R.string.af_title_ok)
            ).show()
        }
        if (adapter.getlist().isEmpty()) {
            showRefresh()
            onRefresh()
        }
    }

    private fun onErrorDeletePost(data: DeletePostViewModel) {
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again),
            View.OnClickListener {
                feedViewModel.doDeletePost(data.id, data.rowNumber)
            })
    }

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    private fun onSuccessToggleFavoriteShop(rowNumber: Int, adapterPosition: Int) {
        if (rowNumber < adapter.getlist().size) {

            if (adapter.getlist().size > rowNumber && adapter.getlist()[rowNumber] is DynamicPostUiModel) {
                val item = (adapter.getlist()[rowNumber] as DynamicPostUiModel)
                item.feedXCard.followers.isFollowed = !item.feedXCard.followers.isFollowed

                feedAnalytics.eventClickFollowitem(
                    if (item.feedXCard.typename == TYPE_FEED_X_CARD_PLAY) item.feedXCard.playChannelID else item.feedXCard.id,
                    adapterPosition,
                    item.feedXCard.typename,
                    !item.feedXCard.followers.isFollowed,
                    item.feedXCard.author.id
                )

                if (item.feedXCard.followers.isFollowed)
                    item.feedXCard.followers.transitionFollow = true

                adapter.notifyItemChanged(
                    rowNumber,
                    DynamicPostNewViewHolder.PAYLOAD_ANIMATE_FOLLOW
                )
            }

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
                item.feedXCard.followers.isFollowed = !item.feedXCard.followers.isFollowed

                feedAnalytics.eventClickFollowitem(
                        item.feedXCard.id,
                        adapterPosition,
                        item.feedXCard.typename,
                        !item.feedXCard.followers.isFollowed,
                        item.feedXCard.author.id
                )

                if (item.feedXCard.followers.isFollowed)
                    item.feedXCard.followers.transitionFollow = true

                adapter.notifyItemChanged(
                        rowNumber,
                        TopAdsHeadlineV2ViewHolder.PAYLOAD_ANIMATE_FOLLOW
                )
            }
        }
    }

    private fun onErrorToggleFavoriteShop(data: FavoriteShopViewModel) {
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
            })
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
        Embrace.getInstance().endEvent(FEED_TRACE)
    }

    private fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String) {

    }

    private fun onGoToLinkASGCProductDetail(link: String, shopId: String, activityId: String, isFollowed: Boolean,type: String, products: List<FeedXProduct>) {
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
                    if (activity != null)
                        requireActivity().startActivity(intent)
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
            shopId, FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE
        )
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
        return (adapter.getlist()
            .isNotEmpty() && adapter.getlist().size > 1 && adapter.getlist()[0] !is EmptyModel)
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
                        trackingPostModel
                    )
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
            } else if (visitable is TopadsShopUiModel) {
                val (_, _, _, trackingList, _) = visitable
                analytics.eventTopadsRecommendationImpression(
                    trackingList,
                    userId
                )
            }
        }
    }

    private fun trackPostContentImpression(
        postViewModel: DynamicPostViewModel,
        trackingPostModel: TrackingPostModel,
        userId: Int, feedPosition: Int
    ) {
        if (postViewModel.contentList.isEmpty()) {
            return
        }

        when {
            postViewModel.contentList[0] is GridPostViewModel -> {
                val (itemList) = postViewModel.contentList[0] as GridPostViewModel
                val productList = ArrayList<ProductEcommerce>()
                for (position in 0 until itemList.size) {
                    val (id, text, price) = itemList[position]
                    productList.add(
                        ProductEcommerce(
                            id,
                            text,
                            price,
                            position
                        )
                    )
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

    private fun trackRecommendationFollowClick(
        trackingRecommendationModel: TrackingRecommendationModel,
        action: String
    ) {
        analytics.eventFollowRecommendation(
            action,
            trackingRecommendationModel.authorType,
            trackingRecommendationModel.authorId.toString()
        )
    }

    private fun trackBannerClick(
        adapterPosition: Int,
        trackingBannerModel: TrackingBannerModel
    ) {
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

    override fun onClickSekSekarang(postId: String, shopId: String, type: String, isFollowed: Boolean, positionInFeed: Int, feedXCard: FeedXCard) {
        if (type == TYPE_TOPADS_HEADLINE_NEW) {
            sendTopadsUrlClick(getAdClickUrl(positionInFeed = positionInFeed))
            feedAnalytics?.clickSekSekarang(postId, shopId, type, isFollowed)
        } else {
            feedAnalytics.eventGridMoreProductCLicked(
                    postId, type, isFollowed, shopId
            )
            val intent = RouteManager.getIntent(context, feedXCard.appLink)
            intent.putParcelableArrayListExtra(PRODUCT_LIST, ArrayList(feedXCard.products))
            intent.putExtra(IS_FOLLOWED, isFollowed)
            intent.putExtra(PARAM_SHOP_ID, shopId)
            intent.putExtra(SHOP_NAME, feedXCard.author.name)
            intent.putExtra(PARAM_ACTIVITY_ID, postId)
            intent.putExtra(POST_TYPE, type)
            requireActivity().startActivity(intent)
        }
    }

    override fun onTopAdsHeadlineImpression(position: Int, cpmModel: CpmModel, isNewVariant:Boolean) {
        val eventLabel = "${cpmModel.data[0].id} - ${cpmModel.data[0].cpm.cpmShop.id}"
        val eventAction = IMPRESSION_CARD_TOPADS

        if (isNewVariant){
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

    override fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData, isNewVariant:Boolean) {
        if (!isNewVariant) {
            RouteManager.route(context, applink)
        } else{
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

    private fun getIntent(shareUrl:String=""): Intent {
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
        if (actionText?.isEmpty() == false)
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()

        }
    }

    private fun showNoInterNetDialog(context: Context) {
        val sheet = FeedNetworkErrorBottomSheet.newInstance()
        sheet.show((context as FragmentActivity).supportFragmentManager, "")

    }

    override fun hideTopadsView(position: Int) {
        if (recyclerView.isComputingLayout ) {

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

    private fun sendTopadsUrlClick(adClickUrl: String,id:String="",uri: String="",fullEcs: String?="") {
        topAdsUrlHitter.hitClickUrl(
                this::class.java.simpleName,
                adClickUrl,
                id,
                uri,
                fullEcs,
                ""
        )
    }

    private fun sendTopadsImpression(adViewUrl: String, id: String, uri: String, fullEcs: String?){
        topAdsUrlHitter.hitImpressionUrl(
                this::class.java.simpleName,
                adViewUrl,
                id,
                uri,
                fullEcs
        )
    }

    private fun getAdClickUrl(positionInFeed: Int): String{
        var adClickUrl=""
        if (adapter.getlist()[positionInFeed] is TopadsHeadLineV2Model) {
            val item = (adapter.getlist()[positionInFeed] as TopadsHeadLineV2Model)
            adClickUrl = item.cpmModel?.data?.firstOrNull()?.adClickUrl?:""
        }
        return adClickUrl
    }
    }
