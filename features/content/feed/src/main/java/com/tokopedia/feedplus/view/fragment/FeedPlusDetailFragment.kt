package com.tokopedia.feedplus.view.fragment

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
import com.tokopedia.feedcomponent.analytics.tracker.FeedTrackerData
import com.tokopedia.feedcomponent.bottomsheets.ProductActionBottomSheet
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.view.share.FeedProductTagSharingHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.DetailFeedAdapter
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedDetailAnalytics.Companion.feedDetailAnalytics
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.presenter.FeedDetailViewModel
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.feedplus.view.util.EndlessScrollRecycleListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailProductModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR

/**
 * A simple [Fragment] subclass.
 */

private const val ARGS_DETAIL_ID = "DETAIL_ID"
private const val REQUEST_OPEN_PDP = 111
private const val TYPE = "text/plain"
private const val PLACEHOLDER_LINK = "{{branchlink}}"
private const val TITLE_OTHER = "Lainnya"

class FeedPlusDetailFragment : BaseDaggerFragment(), FeedPlusDetailListener, ShareCallback {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerviewScrollListener: EndlessScrollRecycleListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DetailFeedAdapter
    private lateinit var pagingHandler: PagingHandler
    private lateinit var feedViewModel: FeedViewModel
    private var detailId: String = ""
    private var shopId: String = ""
    private var shopName: String = ""
    private var postType: String = ""
    private var saleType: String = ""
    private var saleStatus: String = ""
    private var contentSlotValue: String = ""
    private var isFollowed: Boolean = false
    private var productList = mutableListOf<FeedXProduct>()
    private var activityId: String = ""
    private lateinit var shareData: LinkerData
    private var lastScrollPosition: Int = -1

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
                        message = linkerError?.errorMessage ?: getString(R.string.default_request_error_unknown),
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
        if (shopId.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_SHOP_ID)?.let {
                    shopId = it
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
        if (contentSlotValue.isEmpty()) {
            arguments?.run {
                getString(FeedPlusDetailActivity.PARAM_CONTENT_SLOT_VALUE)?.let {
                    contentSlotValue = it
                }
            }
        }
        arguments?.run {
            getBoolean(FeedPlusDetailActivity.PARAM_IS_FOLLOWED)?.let {
                isFollowed = it
            }
        }

        layoutManager = object : LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false){
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


    private fun onRecyclerViewListener() : EndlessScrollRecycleListener {
        return object : EndlessScrollRecycleListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (!adapter.isLoading && presenter.cursor.isNotEmpty()) {
                        pagingHandler.nextPage()
                        presenter.getFeedDetail(detailId, pagingHandler.page, shopId, activityId)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_plus_detail_nav, container, false)
        view.run {
            recyclerView = findViewById(R.id.detail_list)
            progressBar = findViewById(R.id.progress_bar)
        }
        prepareView()
        return view
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
        presenter.getFeedDetail(detailId, pagingHandler.page, shopId, activityId)

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
                        onSuccessGetFeedDetail(it.feedDetailList, it.cursor)
                    }

                    is FeedDetailViewState.Error -> {
                        onErrorGetFeedDetail(it.error)
                    }
                }
            })

            getPagingLiveData().observe(viewLifecycleOwner, Observer {
                setHasNextPage(it)
            })
        }
    }
    private fun onSuccessGetFeedDetail(
            products: List<FeedXProduct>,
            cursor: String) {
        setUpShopDataHeader()
        productList.addAll(products)
        val ret = mapPostTag(products)
        adapter.addList(ret)
        pagingHandler.setHasNext(ret.size > 1 && cursor.isNotEmpty())
        adapter.notifyDataSetChanged()
    }
    private fun onErrorGetFeedDetail(error: Throwable) {
        dismissLoading()
        NetworkErrorHelper.showEmptyState(activity, view, ErrorHandler.getErrorMessage(context, error)) {
            presenter.getFeedDetail(detailId, pagingHandler.page, shopId, activityId)
        }
    }

    private fun onEmptyFeedDetail() {
        adapter.showEmpty()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    override fun onGoToShopDetail(activityId: String?, shopId: Int) {
        arguments?.run {
            goToShopDetail(shopId)
            analytics.eventFeedViewShop(
                    screenName, shopId.toString(), getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                    + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP)
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
        val finalID = if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId
        feedAnalytics.eventClickBottomSheetMenu(
            FeedTrackerData(
                postId = finalID,
                media = FeedXMedia(),
                postType = item.postType,
                isFollowed = item.isFollowed,
                shopId = item.shopId,
                mediaType = "",
                positionInFeed = item.positionInFeed,
                contentSlotValue = contentSlotValue,
                trackerId = "",
                campaignStatus = item.saleStatus,
                product = item.product,
                productId = item.product.id,
                mediaIndex = 0
            )
        )
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val desc =
            context.getString(feedComponentR.string.feed_detail_share_default_text)
                ?.let {
                    String.format(
                        it, item.product.name, item.shopName, item.priceFmt
                    )
                }
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
                    item.playChannelId
            )
        }
        sheet.addToWIshListCB = {
            addToWishList(finalID, item.id, item.postType, item.isFollowed, item.shopId, item.playChannelId)
        }
    }

    override fun onAddToWishlistButtonClicked(item: FeedDetailProductModel, productPosition: Int) {
        val campaignTrackerValue =
            if (item.isFollowed && item.saleStatus.isNotEmpty()) {
                if (item.isUpcoming)
                   CAMPAIGN_UPCOMING_TRACKER_SUFFIX
                else
                    CAMPAIGN_ONGOING_TRACKER_SUFFIX
            }
            else ""
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
            }
            else ""

        onTagSheetItemBuy(
            item.postId.toString(),
            item.positionInFeed,
            item.product,
            item.shopId,
            item.postType,
            item.isFollowed,
            item.shopName,
            item.playChannelId,
            campaignTrackerValue
        )
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
                media = FeedXMedia(),
                mediaType =  "",
                product = FeedXProduct(),
                campaignStatus = "",
                contentSlotValue = contentSlotValue,
                mediaIndex = 0,
                positionInFeed = 0,
                trackerId = ""
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
            campaignStatusValue: String = ""
    ) {
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
                    contentScore = contentSlotValue
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
                    contentScore = contentSlotValue
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
                contentScore = contentSlotValue
            )
        }
        if (userSession.isLoggedIn) {
            feedViewModel.addtoCartProduct(postTagItem, shopId, type, isFollowed, activityId)
        } else {
            onGoToLogin()
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
            campaignStatusValue: String = "") {

        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId
        if (campaignStatusValue.isEmpty())
            feedAnalytics.eventAddToWishlistClicked(
                finalId,
                productId,
                type,
                isFollowed,
                shopId,
                "",
                contentScore = contentSlotValue
            )
        else
            feedAnalytics.sendClickAddToWishlistAsgcProductDetail(
                postId,
                shopId,
                productId,
                campaignStatusValue,
                contentSlotValue
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
                feedAnalytics.eventOnTagSheetItemBuyClicked (
                    FeedTrackerData(
                        postId = activityId,
                        postType = type,
                        isFollowed = isFollowed,
                        shopId = shopId,
                        campaignStatus = getTrackerCampaignStatusSuffix(),
                        contentSlotValue = contentSlotValue,
                        mediaIndex = 0 ,
                        media = FeedXMedia(),
                        mediaType = "",
                        positionInFeed = positionInFeed,
                        product = FeedXProduct(),
                        productId = "",
                        trackerId = ""
                    ))
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

    private fun getTrackerCampaignStatusSuffix() = if(saleStatus == FeedDetailProductModel.UPCOMING) CAMPAIGN_UPCOMING_TRACKER_SUFFIX else CAMPAIGN_ONGOING_TRACKER_SUFFIX
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
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
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


    private fun setUpShopDataHeader() {
        (activity as FeedPlusDetailActivity).getShopInfoLayout()?.run {

            product_detail_back_icon?.setOnClickListener { activity?.finish() }
            show()
        }
    }

    private fun goToShopDetail(shopId: Int) {
        if (activity != null && activity?.applicationContext != null) {
            val intent = RouteManager.getIntent(activity,
                    ApplinkConst.SHOP, shopId.toString())
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

    override fun onGoToProductDetail(feedDetailProductModel: FeedDetailProductModel, adapterPosition: Int) {
        if (activity != null && activity?.applicationContext != null && arguments != null) {

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
                if (feedDetailProductModel.isUpcoming) CAMPAIGN_UPCOMING_TRACKER_SUFFIX else CAMPAIGN_ONGOING_TRACKER_SUFFIX

            )
            activity?.startActivityForResult(
                getProductIntent(feedDetailProductModel.id),
                REQUEST_OPEN_PDP
            )
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    private fun setHasNextPage(hasNextPage: Boolean) {
        pagingHandler.setHasNext(hasNextPage)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_DETAIL_ID, detailId)
    }

    var impressionProductList : ArrayList<FeedXProduct>? = null

    private fun trackImpression(postTagItemList: List<FeedXProduct>) {
        if (impressionProductList == null) {
            impressionProductList = ArrayList()
            if (lastScrollPosition < postTagItemList.size && lastScrollPosition >= 0 ) {
                impressionProductList?.addAll(postTagItemList.slice(0..lastScrollPosition))
            }
        } else {
            impressionProductList = ArrayList()
        }
        if (impressionProductList?.size!! > 0) {
            feedAnalytics.eventImpressionProductBottomSheet(
                activityId,
                impressionProductList!!,
                shopId,
                postType,
                isFollowed,
                true,
                "",
                 trackerId = if (saleType.isNotEmpty()) "13439" else ""
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
            feedDetailAnalytics.eventShareCategory(shareParam[0], shareParam[1].toString() + "-" + KEY_OTHER)
        }
    }
    private fun mapPostTag(postTagItemList: List<FeedXProduct>): MutableList<FeedDetailProductModel> {
        var postDescription = ""
        var adClickUrl = ""
        val desc = context?.getString(feedComponentR.string.feed_share_default_text)
        val itemList: MutableList<FeedDetailProductModel> = ArrayList()
        for (postTagItem in postTagItemList) {
            if (postTagItem.isTopads){
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
                    shopId = shopId,
                    postType = postType,
                    isFollowed = isFollowed,
                    description = postDescription,
                    isTopads = postTagItem.isTopads,
                    adClickUrl = adClickUrl,
                    saleStatus = saleStatus,
                    saleType = saleType
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
