package com.tokopedia.feedplus.oldFeed.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.analytics.tracker.FeedMerchantVoucherAnalyticTracker
import com.tokopedia.feedcomponent.analytics.tracker.FeedTrackerData
import com.tokopedia.feedcomponent.bottomsheets.FeedFollowersOnlyBottomSheet
import com.tokopedia.feedcomponent.bottomsheets.ProductActionBottomSheet
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.view.share.FeedProductTagSharingHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.oldFeed.view.activity.FeedPlusDetailActivity
import com.tokopedia.feedplus.oldFeed.view.activity.FeedPlusDetailActivity.Companion.PARAM_IS_FOLLOWED
import com.tokopedia.feedplus.oldFeed.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory
import com.tokopedia.feedplus.oldFeed.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl
import com.tokopedia.feedplus.oldFeed.view.adapter.viewholder.feeddetail.DetailFeedAdapter
import com.tokopedia.feedplus.oldFeed.view.adapter.viewholder.feeddetail.FeedDetailViewHolder
import com.tokopedia.feedplus.oldFeed.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.oldFeed.view.analytics.FeedDetailAnalytics.Companion.feedDetailAnalytics
import com.tokopedia.feedplus.oldFeed.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.oldFeed.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.oldFeed.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.oldFeed.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.oldFeed.view.presenter.FeedDetailViewModel
import com.tokopedia.feedplus.oldFeed.view.presenter.FeedViewModel
import com.tokopedia.feedplus.oldFeed.view.subscriber.FeedDetailViewState
import com.tokopedia.feedplus.oldFeed.view.viewmodel.feeddetail.FeedDetailProductModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR

/**
 * A simple [Fragment] subclass.
 */

private const val ARGS_DETAIL_ID = "DETAIL_ID"
private const val REQUEST_OPEN_PDP = 111
private const val TYPE = "text/plain"

@Suppress("LateinitUsage")
class FeedPlusDetailFragment : BaseDaggerFragment(), FeedPlusDetailListener, ShareCallback,
    FeedFollowersOnlyBottomSheet.Listener {
    private lateinit var mvcWidget: MvcView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerviewScrollListener: com.tokopedia.feedplus.oldFeed.view.util.EndlessScrollRecycleListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DetailFeedAdapter
    private lateinit var pagingHandler: PagingHandler
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var campaignData: FeedXCampaign
    private var detailId: String = ""
    private var authorId: String = ""
    private var authorType: String = ""
    private var shopName: String = ""
    private var postType: String = ""
    private var saleType: String = ""
    private var saleStatus: String = ""
    private var contentScore: String = ""
    private var isFollowed: Boolean = false
    private var hasVoucher: Boolean = false
    private var productList = mutableListOf<FeedXProduct>()
    private var activityId: String = ""
    private lateinit var shareData: LinkerData
    private var lastScrollPosition: Int = -1

    private var customMvcTracker: FeedMerchantVoucherAnalyticTracker =
        FeedMerchantVoucherAnalyticTracker()

    companion object {
        const val KEY_OTHER = "lainnya"
        private const val CAMPAIGN_UPCOMING_TRACKER_SUFFIX = "pre"
        private const val CAMPAIGN_ONGOING_TRACKER_SUFFIX = "ongoing"

        fun createInstance(bundle: Bundle): FeedPlusDetailFragment {
            val feedPlusDetailNavFragment = FeedPlusDetailFragment()
            feedPlusDetailNavFragment.arguments = bundle
            return feedPlusDetailNavFragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var presenter: FeedDetailViewModel
    private var feedFollowersOnlyBottomSheet: FeedFollowersOnlyBottomSheet? = null

    @Inject
    lateinit var analytics: FeedAnalytics

    @Inject
    lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var userSession: UserSessionInterface

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            presenter = viewModelProvider.get(FeedDetailViewModel::class.java)
            feedViewModel = viewModelProvider.get(FeedViewModel::class.java)
        }
        initVar(savedInstanceState)

        if (::presenter.isInitialized) {
            presenter.fetchMerchantVoucherSummary(authorId)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        feedViewModel.run {
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
                            it.throwable.message
                                ?: getString(R.string.default_request_error_unknown),
                            Toaster.TYPE_ERROR
                        )
                    }
                }
            })

            followKolResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess && data.isFollow) {
                            showToast(
                                getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_success_toaster_text),
                                Toaster.TYPE_NORMAL,
                                getString(com.tokopedia.feedcomponent.R.string.feed_asgc_campaign_toaster_action_text)
                            )
                            onResponseAfterFollowFromBottomSheet(true)

                        }
                    }
                    is Fail -> {
                        onResponseAfterFollowFromBottomSheet(false)
                        val message = it.throwable.message
                            ?: getString(R.string.default_request_error_unknown)
                        showToast(message, Toaster.TYPE_ERROR)
                    }
                }
            })

            toggleFavoriteShopResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            showToast(
                                getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_success_toaster_text),
                                Toaster.TYPE_NORMAL,
                                getString(com.tokopedia.feedcomponent.R.string.feed_asgc_campaign_toaster_action_text)
                            )
                            onResponseAfterFollowFromBottomSheet(true)
                            if (feedFollowersOnlyBottomSheet?.isAdded == true && feedFollowersOnlyBottomSheet?.isVisible == true) {
                                feedFollowersOnlyBottomSheet?.dismiss()
                            }
                        }
                    }
                    is Fail -> {
                        onResponseAfterFollowFromBottomSheet(false)
                        val message = it.throwable.message
                            ?: getString(R.string.default_request_error_unknown)
                        showToast(message, Toaster.TYPE_ERROR)
                    }
                }
            })

        }
    }

    private fun initVar(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            getString(ARGS_DETAIL_ID)?.let {
                detailId = it
            }
        }
        if (detailId.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.EXTRA_DETAIL_ID)?.let {
                    detailId = it
                }
            }
        }
        if (authorId.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_AUTHOR_ID)?.let {
                    authorId = it
                }
            }
        }
        if (authorType.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_AUTHOR_TYPE)?.let {
                    authorType = it
                }
            }
        }
        if (activityId.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_ACTIVITY_ID)?.let {
                    activityId = it
                }
            }
        }
        if (postType.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_POST_TYPE)?.let {
                    postType = it
                }
            }
        }
        if (shopName.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_SHOP_NAME)?.let {
                    shopName = it
                }
            }
        }
        if (saleType.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_SALE_TYPE)?.let {
                    saleType = it
                }
            }
        }
        if (saleStatus.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_SALE_STATUS)?.let {
                    saleStatus = it
                }
            }
        }
        if (contentScore.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_CONTENT_SLOT_VALUE)?.let {
                    contentScore = it
                }
            }
        }
        arguments?.run {
            getBoolean(PARAM_IS_FOLLOWED).let {
                isFollowed = it
            }
        }

        arguments?.run {
            getBoolean(FeedPlusDetailActivity.PARAM_HAS_VOUCHER).let {
                hasVoucher = it
            }
        }

        layoutManager =
            object : LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) {
                override fun onLayoutCompleted(state: RecyclerView.State?) {
                    super.onLayoutCompleted(state)
                    var index = layoutManager.findLastVisibleItemPosition()
                    if (index > lastScrollPosition)
                        lastScrollPosition = index
                }
            }

        recyclerviewScrollListener = onRecyclerViewListener()
        val typeFactory: FeedPlusDetailTypeFactory = FeedPlusDetailTypeFactoryImpl(this)
        adapter = DetailFeedAdapter(typeFactory)
        pagingHandler = PagingHandler()
    }


    private fun onRecyclerViewListener(): com.tokopedia.feedplus.oldFeed.view.util.EndlessScrollRecycleListener {
        return object : com.tokopedia.feedplus.oldFeed.view.util.EndlessScrollRecycleListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!adapter.isLoading && presenter.cursor.isNotEmpty()) {
                    pagingHandler.nextPage()
                    presenter.getFeedDetail(detailId, pagingHandler.page, authorId, activityId)
                }
            }

            override fun onScroll(lastVisiblePosition: Int) {
            }


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var index = layoutManager.findLastVisibleItemPosition()
                    if (index > lastScrollPosition)
                        lastScrollPosition = index
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_plus_detail_nav, container, false)
        view.run {
            mvcWidget = findViewById(R.id.merchant_voucher_widget_plus_detail)
            recyclerView = findViewById(R.id.detail_list)
            progressBar = findViewById(R.id.progress_bar)
        }
        prepareView()
        return view
    }

    private fun onShopFollowRequestedFromBottomSheet() {
        requireLogin {
            if (authorType == FollowCta.AUTHOR_USER || authorType == FollowCta.AUTHOR_UGC) {
                feedViewModel.doFollowKol(authorId, 0)
            } else if (authorType == FollowCta.AUTHOR_SHOP) {
                feedViewModel.doToggleFavoriteShop(
                    rowNumber = 0,
                    adapterPosition = 0,
                    shopId = authorId,
                    follow = false
                )
            }
        }
    }

    private fun prepareView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(recyclerviewScrollListener)

    }

    override fun getScreenName(): String {
        return FeedTrackingEventLabel.SCREEN_FEED_DETAIL
    }

    override fun initInjector() {
        DaggerFeedPlusComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        presenter.getFeedDetail(detailId, pagingHandler.page, authorId, activityId)

        setUpShopDataHeader()
    }

    private fun setUpObservers() {
        presenter.run {
            getFeedDetailLiveData().observe(viewLifecycleOwner, Observer {
                when (it) {
                    is FeedDetailViewState.LoadingState -> {
                        if (it.loadingMore) {
                            if (it.isLoading) {
                                showLoadingMore()
                            } else {
                                dismissLoadingMore()
                            }
                        } else {
                            if (it.isLoading) {
                                showLoading()
                            } else {
                                dismissLoading()
                            }
                        }
                    }

                    is FeedDetailViewState.SuccessWithNoData -> {
                        onEmptyFeedDetail()
                    }

                    is FeedDetailViewState.Success -> {
                        onSuccessGetFeedDetail(it.feedXGetActivityProductsResponse)
                    }

                    is FeedDetailViewState.Error -> {
                        onErrorGetFeedDetail(it.error)
                    }
                }
            })

            getPagingLiveData().observe(viewLifecycleOwner, Observer {
                setHasNextPage(it)
            })

            merchantVoucherSummary.observe(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        if (it.data.animatedInfoList?.isNotEmpty() == true) {

                            customMvcTracker.activityId = activityId
                            customMvcTracker.status = getTrackerCampaignStatusSuffix()
                            customMvcTracker.hasVoucher = hasVoucher
                            customMvcTracker.contentScore = contentScore


                            mvcWidget.setData(
                                mvcData = MvcData(
                                    it.data.animatedInfoList
                                ),
                                shopId = authorId,
                                source = MvcSource.FEED_PRODUCT_DETAIL,
                                mvcTrackerImpl = customMvcTracker
                            )
                            mvcWidget.show()
                        } else {
                            mvcWidget.hide()
                        }
                    }
                    is Fail -> {
                        mvcWidget.hide()
                    }
                }
            }
        }
    }

    private fun onSuccessGetFeedDetail(
        data: FeedXGetActivityProductsResponse
    ) {
        setUpShopDataHeader()
        isFollowed = data.isFollowed
        campaignData = data.campaign
        productList.addAll(data.products)
        saleType = campaignData.name
        saleStatus = campaignData.status
        val ret = mapPostTag(data.products)
        adapter.addList(ret)
        pagingHandler.setHasNext(ret.size > 1 && data.nextCursor.isNotEmpty())
        adapter.notifyDataSetChanged()
        if (shouldShowFollowerBottomSheet())
            showFollowerBottomSheet()
    }

    private fun onErrorGetFeedDetail(error: Throwable) {
        dismissLoading()
        NetworkErrorHelper.showEmptyState(
            activity,
            view,
            ErrorHandler.getErrorMessage(context, error)
        ) {
            presenter.getFeedDetail(detailId, pagingHandler.page, authorId, activityId)
        }
    }

    private fun shouldShowFollowerBottomSheet() =
        ::campaignData.isInitialized && campaignData.isRilisanSpl && !isFollowed

    private fun onEmptyFeedDetail() {
        adapter.showEmpty()
    }

    private fun showFollowerBottomSheet() {

        feedFollowersOnlyBottomSheet =
            FeedFollowersOnlyBottomSheet.getOrCreate(childFragmentManager)

        if (feedFollowersOnlyBottomSheet?.isAdded == false && feedFollowersOnlyBottomSheet?.isVisible == false)
            feedFollowersOnlyBottomSheet?.show(childFragmentManager, this, status = saleStatus)
    }

    private fun onResponseAfterFollowFromBottomSheet(isFollowSuccess: Boolean) {
        isFollowed = isFollowSuccess
        activity?.setResult(Activity.RESULT_OK, getReturnIntent(isFollowSuccess))
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    override fun onGoToShopDetail(activityId: String?, shopId: Int) {
        arguments?.run {
            goToShopDetail(shopId)
            analytics.eventFeedViewShop(
                screenName,
                shopId.toString(),
                getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                    + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP
            )
            activityId?.let { feedAnalytics.eventClickFeedDetailAvatar(it, shopId.toString()) }
        }
    }

    override fun onBackPressed() {
        activity?.onBackPressed()
    }

    override fun onBottomSheetMenuClicked(
        item: FeedDetailProductModel,
        context: Context,
        shopId: String
    ) {
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId
        feedAnalytics.eventClickBottomSheetMenu(
            FeedTrackerData(
                postId = finalID,
                postType = item.postType,
                isFollowed = item.isFollowed,
                shopId = item.shopId,
                positionInFeed = item.positionInFeed,
                contentSlotValue = contentScore,
                campaignStatus = item.saleStatus,
                product = item.product,
                productId = item.product.id,
                isProductDetailPage = true,
                authorType = authorType
            )
        )
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        sheet.show((context as FragmentActivity).supportFragmentManager, "")
        sheet.shareProductCB = {
            onShareProduct(item, finalID)
        }
        sheet.addToCartCB = {
            onTagSheetItemBuy(
                finalID,
                item.positionInFeed,
                item.product,
                item.shopId,
                item.postType,
                item.isFollowed,
                item.shopName,
                item.playChannelId,
                authorType = authorType
            )
        }
        sheet.addToWIshListCB = {
            addToWishList(
                finalID,
                item.id,
                item.postType,
                item.isFollowed,
                item.shopId,
                item.playChannelId
            )
        }
    }

    override fun onAddToWishlistButtonClicked(item: FeedDetailProductModel, productPosition: Int) {
        val campaignTrackerValue =
            if (item.isFollowed && item.saleStatus.isNotEmpty()) {
                if (item.isUpcoming)
                    CAMPAIGN_UPCOMING_TRACKER_SUFFIX
                else
                    CAMPAIGN_ONGOING_TRACKER_SUFFIX
            } else ""
        addToWishList(
            item.postId,
            item.id,
            item.postType,
            item.isFollowed,
            item.shopId,
            item.playChannelId,
            productPosition,
            campaignTrackerValue
        )
    }

    override fun onAddToCartButtonClicked(item: FeedDetailProductModel) {
        val campaignTrackerValue =
            if (item.isFollowed && item.saleStatus.isNotEmpty()) {
                if (item.isUpcoming)
                    CAMPAIGN_UPCOMING_TRACKER_SUFFIX
                else
                    CAMPAIGN_ONGOING_TRACKER_SUFFIX
            } else ""

        if (shouldShowFollowerBottomSheet()) {
            showFollowerBottomSheet()
        } else {
            onTagSheetItemBuy(
                item.postId.toString(),
                item.positionInFeed,
                item.product,
                item.shopId,
                item.postType,
                item.isFollowed,
                item.shopName,
                item.playChannelId,
                campaignTrackerValue,
                authorType = authorType
            )
        }
    }

    private fun onShareProduct(
        item: FeedDetailProductModel,
        activityId: String
    ) {
        feedAnalytics.eventonShareProductClicked(
            FeedTrackerData(
                postId = activityId,
                productId = item.id,
                postType = item.postType,
                isFollowed = item.isFollowed,
                shopId = item.shopId,
                contentSlotValue = contentScore,
                isProductDetailPage = true,
                authorType = authorType
            )
        )

        val linkerBuilder = LinkerData.Builder.getLinkerBuilder()
            .setId(item.id)
            .setName(item.text)
            .setDescription(item.description)
            .setImgUri(item.imgUrl)
            .setUri(item.weblink)
            .setDeepLink(item.applink)
            .setType(LinkerData.FEED_TYPE)
            .setDesktopUrl(item.weblink)

        shareData = linkerBuilder.build()

        feedProductTagSharingHelper.show(
            productTagShareModel = FeedProductTagSharingHelper.Model(
                title = item.text,
                imageUrl = item.imgUrl,
                productName = item.product.name,
                shopName = item.shopName,
                priceFmt = item.priceFmt
            ),
            shareData = shareData
        )
    }

    private fun onTagSheetItemBuy(
        activityId: String,
        positionInFeed: Int,
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        shopName: String,
        playChannelId: String,
        campaignStatusValue: String = "",
        authorType: String
    ) {
        //send tracker data
        if (campaignStatusValue.isEmpty()) {
            if (type == TYPE_FEED_X_CARD_PLAY)
                feedAnalytics.eventAddToCartFeedVOD(
                    playChannelId,
                    postTagItem.id,
                    postTagItem.name,
                    postTagItem.price.toString(),
                    1,
                    shopId,
                    postTagItem.authorName,
                    type,
                    isFollowed,
                    "",
                    contentScore = contentScore,
                    hasVoucher = hasVoucher,
                    authorType = authorType
                )
            else
                feedAnalytics.eventAddToCartFeedVOD(
                    activityId,
                    postTagItem.id,
                    postTagItem.name,
                    postTagItem.price.toString(),
                    1,
                    shopId,
                    shopName,
                    type,
                    isFollowed,
                    "",
                    contentScore = contentScore,
                    hasVoucher = hasVoucher,
                    authorType = authorType
                )
        } else {
            feedAnalytics.sendClickAddToCartAsgcProductDetail(
                activityId,
                shopId,
                postTagItem.id,
                campaignStatusValue,
                postTagItem.name,
                postTagItem.price.toString(),
                1,
                shopName,
                contentScore = contentScore,
                hasVoucher = hasVoucher,
                authorType = authorType
            )
        }

        requireLogin {
            feedViewModel.addtoCartProduct(postTagItem, shopId, type, isFollowed, activityId)
        }
    }

    private fun requireLogin(action: () -> Unit) {
        if (!userSession.isLoggedIn) {
            onGoToLogin()
        } else {
            action.invoke()
        }
    }

    private fun addToWishList(
        postId: String,
        productId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        playChannelId: String,
        productItemPostion: Int = 0,
        campaignStatusValue: String = "",
        hasVoucher: Boolean = false
    ) {

        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId
        if (campaignStatusValue.isEmpty())
            feedAnalytics.eventAddToWishlistClicked(
                finalId,
                productId,
                type,
                isFollowed,
                shopId,
                "",
                contentScore = contentScore,
                hasVoucher = hasVoucher
            )
        else
            feedAnalytics.sendClickAddToWishlistAsgcProductDetail(
                postId,
                shopId,
                productId,
                campaignStatusValue,
                contentScore,
                hasVoucher
            )

        context?.let {
            feedViewModel.addWishlistV2(
                postId,
                productId,
                shopId,
                0,
                productItemPostion,
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
        rowNumber: Int,
        positionInFeed: Int,
        result: AddToWishlistV2Response.Data.WishlistAddV2
    ) {
        Toaster.build(
            requireView(),
            getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist),
            View.OnClickListener {
                feedAnalytics.eventOnTagSheetItemBuyClicked(
                    FeedTrackerData(
                        postId = activityId,
                        postType = type,
                        isFollowed = isFollowed,
                        shopId = shopId,
                        campaignStatus = getTrackerCampaignStatusSuffix(),
                        contentSlotValue = contentScore,
                        positionInFeed = positionInFeed,
                        authorType = authorType
                    )
                )
                RouteManager.route(context, ApplinkConst.WISHLIST)
            }).show()
        adapter.notifyItemChanged(rowNumber, FeedDetailViewHolder.PAYLOAD_CLICK_WISHLIST)
    }

    private fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            requireActivity().startActivityForResult(intent, FeedPlusFragment.REQUEST_LOGIN)
        }
    }

    private fun getTrackerCampaignStatusSuffix() = if (saleStatus.isNotEmpty()) {
        if (saleStatus == FeedDetailProductModel.UPCOMING)
            CAMPAIGN_UPCOMING_TRACKER_SUFFIX
        else
            CAMPAIGN_ONGOING_TRACKER_SUFFIX
    } else String.EMPTY

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    private fun onGoToLink(link: String) {
        context?.let {
            if (!TextUtils.isEmpty(link)) {
                if (RouteManager.isSupportApplink(it, link)) {
                    RouteManager.route(it, link)
                } else {
                    RouteManager.route(
                        it,
                        String.format(Locale.US, "%s?url=%s", ApplinkConst.WEBVIEW, link)
                    )
                }
            }
        }
    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false)
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }

    override fun onFollowClickedFromFollowBottomSheet(position: Int) {
        onShopFollowRequestedFromBottomSheet()
    }

    private fun setUpShopDataHeader() {
        (activity as? FeedPlusDetailActivity)?.getShopInfoLayout()?.run {
            product_detail_back_icon?.setOnClickListener {
                (activity as? FeedPlusDetailActivity)?.onBackPressed()
            }
            show()
        }
    }

    private fun goToShopDetail(shopId: Int) {
        if (activity != null && activity?.applicationContext != null) {
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConst.SHOP, shopId.toString()
            )
            startActivity(intent)
        }
    }

    private fun showLoading() {
        adapter.showLoading()
    }

    private fun dismissLoading() {
        adapter.dismissLoading()
    }

    private fun showLoadingMore() {
        adapter.showLoadingMore()
    }

    private fun dismissLoadingMore() {
        adapter.dismissLoadingMore()
    }

    override fun onGoToProductDetail(
        feedDetailProductModel: FeedDetailProductModel,
        adapterPosition: Int
    ) {
        if (activity != null && activity?.applicationContext != null && arguments != null) {
            val campaignStatus = if (feedDetailProductModel.saleType.isNotEmpty()) {
                if (feedDetailProductModel.isUpcoming) CAMPAIGN_UPCOMING_TRACKER_SUFFIX else CAMPAIGN_ONGOING_TRACKER_SUFFIX
            } else String.EMPTY

            analytics.eventDetailProductClick(
                ProductEcommerce(
                    feedDetailProductModel.id,
                    feedDetailProductModel.text,
                    if (feedDetailProductModel.isDiscount) feedDetailProductModel.priceDiscount else feedDetailProductModel.price,
                    adapterPosition
                ),
                userSession.userId,
                feedDetailProductModel.shopId,
                feedDetailProductModel.postId,
                feedDetailProductModel.postType,
                feedDetailProductModel.isFollowed,
                if (feedDetailProductModel.saleType.isNotEmpty()) "13440" else "",
                campaignStatus,
                contentScore
            )
            goToProductDetail(feedDetailProductModel.id)
        }
    }

    private fun goToProductDetail(productId: String) {
        getProductIntent(productId).let { intent ->
            activity?.startActivityForResult(
                intent,
                REQUEST_OPEN_PDP
            )
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null && productId.isNotBlank()) {
            RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
        } else {
            null
        }
    }

    private fun getReturnIntent(isFollowSuccessFromBottomSheet: Boolean): Intent {
        val intent = Intent()
        arguments?.let {
            intent.putExtras(it)
        }
        intent.putExtra(PARAM_IS_FOLLOWED, isFollowSuccessFromBottomSheet)
        return intent
    }


    private fun setHasNextPage(hasNextPage: Boolean) {
        pagingHandler.setHasNext(hasNextPage)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_DETAIL_ID, detailId)
    }

    var impressionProductList: MutableList<FeedXProduct>? = null

    private fun trackImpression(postTagItemList: List<FeedXProduct>) {
        if (impressionProductList == null) {
            impressionProductList = mutableListOf()
            if (lastScrollPosition < postTagItemList.size && lastScrollPosition >= 0) {
                impressionProductList?.addAll(postTagItemList.slice(0..lastScrollPosition))
            }
        } else {
            impressionProductList = mutableListOf()
        }
        if (impressionProductList?.size!! > 0) {
            feedAnalytics.eventImpressionProductBottomSheet(
                activityId,
                impressionProductList!!,
                authorId,
                postType,
                isFollowed,
                true,
                "",
                trackerId = if (saleType.isNotEmpty()) "13439" else "",
                authorType = authorType
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (productList.isNotEmpty())
            trackImpression(productList)
    }

    override fun onError(linkerError: LinkerError?) {}

    override fun urlCreated(linkerShareData: LinkerShareResult?) {
        val intent = getIntent()
        activity?.startActivity(Intent.createChooser(intent, shareData.name))
        sendTracker()
    }

    private fun getIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, shareData.name)
            putExtra(Intent.EXTRA_SUBJECT, shareData.name)
            putExtra(Intent.EXTRA_TEXT, shareData.description + "\n" + shareData.uri)
        }
    }

    private fun sendTracker() {
        if (shareData.type == LinkerData.CATEGORY_TYPE) {
            shareCategory(shareData)
        } else {
            feedDetailAnalytics.sendAnalyticsToGtm(shareData.type)
        }
    }

    private fun shareCategory(data: LinkerData) {
        val shareParam = data.getSplittedDescription(",")
        if (shareParam.size == 2) {
            feedDetailAnalytics.eventShareCategory(
                shareParam[0],
                shareParam[1].toString() + "-" + KEY_OTHER
            )
        }
    }

    private fun mapPostTag(postTagItemList: List<FeedXProduct>): MutableList<FeedDetailProductModel> {
        var postDescription = ""
        var adClickUrl = ""
        val desc = context?.getString(feedComponentR.string.feed_share_default_text)
        val itemList: MutableList<FeedDetailProductModel> = ArrayList()
        for (postTagItem in postTagItemList) {
            if (postTagItem.isTopads) {
                postDescription = desc?.replace("%s", postTagItem.authorName).toString()
                adClickUrl = postTagItem.adClickUrl
            }
            val item = FeedDetailProductModel(
                id = postTagItem.id,
                text = postTagItem.name,
                imgUrl = postTagItem.coverURL,
                price = postTagItem.price.toString(),
                priceDiscount = postTagItem.priceDiscount.toString(),
                priceFmt = postTagItem.priceFmt,
                isDiscount = postTagItem.isDiscount,
                discountFmt = postTagItem.discountFmt,
                type = "product",
                applink = postTagItem.appLink,
                weblink = postTagItem.webLink,
                product = postTagItem,
                isFreeShipping = postTagItem.isBebasOngkir,
                freeShipping = postTagItem.bebasOngkirStatus,
                freeShippingURL = postTagItem.bebasOngkirURL,
                originalPriceFmt = postTagItem.priceOriginalFmt,
                priceDiscountFmt = postTagItem.priceDiscountFmt,
                totalSold = postTagItem.totalSold,
                rating = postTagItem.star,
                mods = postTagItem.mods,
                shopName = shopName,
                shopId = authorId,
                postType = postType,
                isFollowed = isFollowed,
                description = postDescription,
                isTopads = postTagItem.isTopads,
                adClickUrl = adClickUrl,
                saleStatus = campaignData.status,
                saleType = campaignData.name
            )
            item.feedType = "product"
            item.postId = activityId
            itemList.add(item)
        }
        return itemList
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }
}
