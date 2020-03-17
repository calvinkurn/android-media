package com.tokopedia.feedplus.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactoryImpl
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.DetailFeedAdapter
import com.tokopedia.feedplus.view.analytics.FeedAnalytics
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.listener.FeedPlusDetail
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.track.TrackApp
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.android.synthetic.main.fragment_feed_plus_detail_nav.*
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */

private const val ARGS_DETAIL_ID = "DETAIL_ID"
private const val REQUEST_OPEN_PDP = 111

class FeedPlusDetailNavFragment : BaseDaggerFragment(), FeedPlusDetail.View, WishListActionListener, ShareCallback {
    private lateinit var recyclerView: RecyclerView
    private lateinit var shareButton: ImageButton
    private lateinit var seeShopButon: Typography
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DetailFeedAdapter
    private lateinit var pagingHandler: PagingHandler
    private var detailId: String = ""
    private lateinit var shareData: LinkerData

    private val TYPE = "text/plain"
    private val PLACEHOLDER_LINK = "{{branchlink}}"
    private val KEY_OTHER = "lainnya"
    private val TITLE_OTHER = "Lainnya"

    companion object{
        fun createInstance(bundle:Bundle): FeedPlusDetailNavFragment {
            val feedPlusDetailNavFragment = FeedPlusDetailNavFragment()
            feedPlusDetailNavFragment.arguments = bundle
            return feedPlusDetailNavFragment
        }
    }

    internal interface Event {
        companion object {
            const val CATEGORY_PAGE = "clickKategori"
            const val CLICK_APP_SHARE_WHEN_REFERRAL_OFF = "clickAppShare"
            const val CLICK_APP_SHARE_REFERRAL = "clickReferral"
            const val PRODUCT_DETAIL_PAGE = "clickPDP"
        }
    }

    internal interface EventLabel {
        companion object {
            const val SHARE_TO = "Share - "
        }
    }

    internal interface Category {
        companion object {
            const val CATEGORY_PAGE = "Category Page"
            const val REFERRAL = "Referral"
            const val APPSHARE = "App share"
            const val PRODUCT_DETAIL = "Product Detail Page"
        }
    }

    internal interface Action {
        companion object {
            const val CATEGORY_SHARE = "Bottom Navigation - Share"
            const val SELECT_CHANNEL = "select channel"
            const val CLICK = "Click"
        }
    }

    internal interface MoEngage {
        companion object {
            const val CHANNEL = "channel"
        }
    }

    internal interface EventMoEngage {
        companion object {
            const val REFERRAL_SHARE_EVENT = "Share_Event"
        }
    }

    @Inject
    lateinit var presenter: FeedPlusDetail.Presenter

    @Inject
    lateinit var analytics: FeedAnalytics

    @Inject
    lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var userSession: UserSessionInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar(savedInstanceState)
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
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        val typeFactory: FeedPlusDetailTypeFactory = FeedPlusDetailTypeFactoryImpl(this)
        adapter = DetailFeedAdapter(typeFactory)
        GraphqlClient.init(context!!)
        pagingHandler = PagingHandler()
    }


    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!adapter.isLoading && pagingHandler.CheckNextPage()) {
                    pagingHandler.nextPage()
                    presenter.getFeedDetail(detailId, pagingHandler.page)
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
            shareButton = findViewById(R.id.share_button)
            seeShopButon = findViewById(R.id.see_shop)
            progressBar = findViewById(R.id.progress_bar)
        }
        prepareView()
        presenter.attachView(this, this)
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
        presenter.getFeedDetail(detailId, pagingHandler.page)
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    private fun onShareClicked(url: String,
                               title: String,
                               imageUrl: String,
                               description: String): View.OnClickListener? {
        return View.OnClickListener { v: View? ->
            if (activity != null) {
                shareData = LinkerData.Builder.getLinkerBuilder().setId(detailId)
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
        }
    }

    override fun onWishlistClicked(adapterPosition: Int, productId: Int, isWishlist: Boolean) {
        if (arguments != null) {
            if (!isWishlist) {
                presenter.addToWishlist(adapterPosition, productId.toString())
                analytics.eventFeedClickProduct(
                        screenName, productId.toString(),
                        arguments!!.getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.ADD_TO_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST)
            } else {
                presenter.removeFromWishlist(adapterPosition, productId.toString())
                analytics.eventFeedClickProduct(
                        screenName, productId.toString(),
                        arguments!!.getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                                + FeedTrackingEventLabel.Click.REMOVE_WISHLIST +
                                FeedTrackingEventLabel.PAGE_PRODUCT_LIST)
            }
        }
    }

    override fun onGoToShopDetail(activityId: String?, shopId: Int) {
        if (arguments != null) {
            goToShopDetail(shopId)
            analytics.eventFeedViewShop(
                    screenName, shopId.toString(), arguments!!.getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                    + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP)
            activityId?.let { feedAnalytics.eventClickFeedDetailAvatar(it, shopId.toString()) }
        }
    }

    override fun onErrorGetFeedDetail(errorMessage: String?) {
        dismissLoading()
        footer.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, view, errorMessage
        ) { presenter.getFeedDetail(detailId, pagingHandler.page) }
    }

    override fun onEmptyFeedDetail() {
        adapter.showEmpty()
        footer.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (activity != null) activity!!.onBackPressed()
    }

    override fun onSuccessGetFeedDetail(
            header: FeedDetailHeaderViewModel,
            listDetail: ArrayList<Visitable<*>>,
            hasNextPage: Boolean) {
        footer.visibility = View.VISIBLE
        if (pagingHandler.page == 1) {
            adapter.add(header)
        }
        adapter.addList(listDetail)
        shareButton.setOnClickListener(onShareClicked(
                header.shareLinkURL,
                header.shopName,
                header.shopAvatar,
                header.shareLinkDescription))
        seeShopButon.setOnClickListener(onGoToShopDetailFromButton(header.shopId))
        pagingHandler.setHasNext(listDetail.size > 1 && hasNextPage)
        adapter.notifyDataSetChanged()
        trackImpression(listDetail)
    }

    private fun onGoToShopDetailFromButton(shopId: Int): View.OnClickListener? {
        return View.OnClickListener { v: View? ->
            if (arguments != null) {
                goToShopDetail(shopId)
                analytics.eventFeedClickShop(
                        screenName, shopId.toString(), arguments!!.getString(
                        FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                        + FeedTrackingEventLabel.Click.VISIT_SHOP)
            }
        }
    }


    private fun goToShopDetail(shopId: Int) {
        if (activity != null && activity!!.applicationContext != null) {
            val intent = RouteManager.getIntent(activity,
                    ApplinkConst.SHOP, shopId.toString())
            startActivity(intent)
        }
    }

    override fun showLoading() {
        footer.visibility = View.GONE
        adapter.showLoading()
    }

    override fun dismissLoading() {
        footer.visibility = View.VISIBLE
        adapter.dismissLoading()
    }

    override fun showLoadingMore() {
        adapter.showLoadingMore()
    }

    override fun dismissLoadingMore() {
        adapter.dismissLoadingMore()
    }

    override fun showLoadingProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is FeedDetailViewModel) {
                val feedDetailViewModel = adapter.list[i] as FeedDetailViewModel
                if (productID == feedDetailViewModel.productId.toString()) {
                    feedDetailViewModel.isWishlist = true
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.feed_msg_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is FeedDetailViewModel) {
                val feedDetailViewModel = adapter.list[i] as FeedDetailViewModel
                if (productID == feedDetailViewModel.productId.toString()) {
                    feedDetailViewModel.isWishlist = false
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.feed_msg_remove_wishlist))
    }

    override fun onGoToProductDetail(feedDetailViewModel: FeedDetailViewModel, adapterPosition: Int) {
        if (activity != null && activity!!.applicationContext != null && arguments != null) {
            activity!!.startActivityForResult(
                    getProductIntent(feedDetailViewModel.productId.toString()),
                    REQUEST_OPEN_PDP
            )
            analytics.eventDetailProductClick(
                    ProductEcommerce(feedDetailViewModel.productId.toString(),
                            feedDetailViewModel.name,
                            feedDetailViewModel.price,
                            adapterPosition),
                    getUserIdInt()
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

    override fun getColor(resId: Int): Int {
        return MethodChecker.getColor(activity, resId)
    }

    override fun setHasNextPage(hasNextPage: Boolean) {
        pagingHandler.setHasNext(hasNextPage)
    }

    private fun dismissLoadingProgress() {
        progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARGS_DETAIL_ID, detailId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OPEN_PDP && data != null && data.extras != null && data.extras.getInt(FeedPlusDetailFragment.WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            val position = data.extras.getInt(FeedPlusDetailFragment.WISHLIST_STATUS_UPDATED_POSITION, -1)
            val isWishlist = data.extras.getBoolean(FeedPlusDetailFragment.WIHSLIST_STATUS_IS_WISHLIST, false)
            updateWishlistFromPDP(position, isWishlist)
        }
    }

    private fun updateWishlistFromPDP(position: Int, isWishlist: Boolean) {
        if (adapter != null && adapter.list != null && !adapter.list.isEmpty()
                && position < adapter.list.size && adapter.list[position] != null && adapter.list[position] is FeedDetailViewModel) {
            (adapter.list[position] as FeedDetailViewModel).isWishlist = isWishlist
            adapter.notifyItemChanged(position)
        }
    }

    private fun trackImpression(listDetail: ArrayList<Visitable<*>>) {
        val productList = ArrayList<ProductEcommerce>()
        for (position in listDetail.indices) {
            if (listDetail[position] is FeedDetailViewModel) {
                val model = listDetail[position] as FeedDetailViewModel
                productList.add(ProductEcommerce(model.productId.toString(),
                        model.name,
                        model.price,
                        position
                ))
            }
        }
        analytics.eventDetailProductImpression(productList, getUserIdInt())
    }

    private fun getUserIdInt(): Int {
        return try {
            Integer.valueOf(userSession.getUserId())
        } catch (ignored: NumberFormatException) {
            0
        }
    }

    override fun urlCreated(linkerShareData: LinkerShareResult) {
        val intent = getIntent(linkerShareData.shareContents, linkerShareData.url)
        if (null != activity) {
            activity!!.startActivity(Intent.createChooser(intent, TITLE_OTHER))
            sendTracker()
        }
    }

    override fun onError(linkerError: LinkerError?) {}

    private fun getIntent(contains: String, url: String): Intent {
        var contains: String? = contains
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = TYPE
        var title: String? = ""
        if (shareData != null) {
            title = shareData.name
        }
        if (!TextUtils.isEmpty(shareData.custmMsg) && shareData.custmMsg.contains(PLACEHOLDER_LINK)) {
            contains = FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.custmMsg, PLACEHOLDER_LINK, url)
        }
        mIntent.putExtra(Intent.EXTRA_TITLE, title)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        mIntent.putExtra(Intent.EXTRA_TEXT, contains)
        return mIntent
    }

    private fun sendTracker() {
        if (shareData.type == LinkerData.CATEGORY_TYPE) {
            shareCategory(shareData)
        } else {
            sendAnalyticsToGtm(shareData.type)
        }
    }

    private fun shareCategory(data: LinkerData) {
        val shareParam = data.getSplittedDescription(",")
        if (shareParam.size == 2) {
            eventShareCategory(shareParam[0], shareParam[1].toString() + "-" + KEY_OTHER)
        }
    }

    fun eventShareCategory(parentCat: String, label: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                FeedPlusDetailFragment.Event.CATEGORY_PAGE,
                FeedPlusDetailFragment.Category.CATEGORY_PAGE + "-" + parentCat,
                FeedPlusDetailFragment.Action.CATEGORY_SHARE,
                label)
    }

    private fun sendAnalyticsToGtm(type: String) {
        when (type) {
            LinkerData.REFERRAL_TYPE -> {
                sendEventReferralAndShare(
                        FeedPlusDetailFragment.Action.SELECT_CHANNEL,
                        KEY_OTHER
                )
                sendMoEngageReferralShareEvent(KEY_OTHER)
            }
            LinkerData.APP_SHARE_TYPE -> sendEventAppShareWhenReferralOff(
                    FeedPlusDetailFragment.Action.SELECT_CHANNEL,
                    KEY_OTHER
            )
            else -> sendEventShare(KEY_OTHER)
        }
    }

    fun sendMoEngageReferralShareEvent(channel: String?) {
        val value = DataLayer.mapOf(
                FeedPlusDetailFragment.MoEngage.CHANNEL, channel
        )
        TrackApp.getInstance().moEngage.sendTrackEvent(value, FeedPlusDetailFragment.EventMoEngage.REFERRAL_SHARE_EVENT)
    }

    private fun sendEventReferralAndShare(action: String, label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = FeedPlusDetailFragment.Event.CLICK_APP_SHARE_REFERRAL
        eventTracking["eventCategory"] = FeedPlusDetailFragment.Category.REFERRAL
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
        TrackApp.getInstance().gtm.sendGeneralEvent(eventTracking)
    }

    fun sendEventAppShareWhenReferralOff(action: String, label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = FeedPlusDetailFragment.Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF
        eventTracking["eventCategory"] = FeedPlusDetailFragment.Category.APPSHARE
        eventTracking["eventAction"] = action
        eventTracking["eventLabel"] = label
        TrackApp.getInstance().gtm.sendGeneralEvent(eventTracking)
    }

    fun sendEventShare(label: String) {
        val eventTracking: MutableMap<String, Any> = HashMap()
        eventTracking["event"] = FeedPlusDetailFragment.Event.PRODUCT_DETAIL_PAGE
        eventTracking["eventCategory"] = FeedPlusDetailFragment.Category.PRODUCT_DETAIL
        eventTracking["eventAction"] = FeedPlusDetailFragment.Action.CLICK
        eventTracking["eventLabel"] = FeedPlusDetailFragment.EventLabel.SHARE_TO + label
        TrackApp.getInstance().gtm.sendGeneralEvent(eventTracking)
    }
}
