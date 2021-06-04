package com.tokopedia.feedplus.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.feedplus.view.analytics.FeedDetailAnalytics.Companion.feedDetailAnalytics
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel
import com.tokopedia.feedplus.view.analytics.ProductEcommerce
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.presenter.FeedDetailViewModel
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kolcommon.util.TimeConverter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.feed_detail_header.view.*
import java.util.*
import javax.inject.Inject

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
    private lateinit var shareButton: ImageButton
    private lateinit var seeShopButton: Typography
    private lateinit var footer: View
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DetailFeedAdapter
    private lateinit var pagingHandler: PagingHandler
    private var detailId: String = ""
    private lateinit var shareData: LinkerData

    companion object {
        const val KEY_OTHER = "lainnya"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            presenter = viewModelProvider.get(FeedDetailViewModel::class.java)
        }
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
        GraphqlClient.init(requireContext())
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
            progressBar = findViewById(R.id.progress_bar)
        }
        (activity as FeedPlusDetailActivity).getFooterLayout()?.run {
            footer = this
            shareButton = findViewById(R.id.share_button)
            seeShopButton = findViewById(R.id.see_shop)
        }
        prepareView()
        return view
    }


    private fun prepareView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
        footer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val height = footer.measuredHeight
        recyclerView.setPadding(0,0,0,height)
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
        presenter.getFeedDetail(detailId, pagingHandler.page)
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
                        onSuccessGetFeedDetail(it.headerModel, it.feedDetailList as ArrayList<Visitable<*>>, it.hasNextPage)
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

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    private fun onShareClicked(url: String,
                               title: String,
                               imageUrl: String,
                               description: String): View.OnClickListener? {
        return View.OnClickListener {
            activity?.let {
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

    override fun onGoToShopDetail(activityId: String?, shopId: Int) {
        arguments?.run {
            goToShopDetail(shopId)
            analytics.eventFeedViewShop(
                    screenName, shopId.toString(), getString(FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                    + FeedTrackingEventLabel.View.PRODUCTLIST_SHOP)
            activityId?.let { feedAnalytics.eventClickFeedDetailAvatar(it, shopId.toString()) }
        }
    }

    private fun onErrorGetFeedDetail(error: Throwable) {
        dismissLoading()
        footer.hide()
        NetworkErrorHelper.showEmptyState(activity, view, ErrorHandler.getErrorMessage(context, error)) {
            presenter.getFeedDetail(detailId, pagingHandler.page)
        }
    }

    private fun onEmptyFeedDetail() {
        adapter.showEmpty()
        footer.hide()
    }

    override fun onBackPressed() {
        activity?.onBackPressed()
    }

    private fun onSuccessGetFeedDetail(
            header: FeedDetailHeaderModel,
            listDetail: ArrayList<Visitable<*>>,
            hasNextPage: Boolean) {
        footer.show()
        setUpShopDataHeader(header)
        adapter.addList(listDetail)
        shareButton.setOnClickListener(onShareClicked(
                header.shareLinkURL,
                header.shopName,
                header.shopAvatar,
                header.shareLinkDescription))
        seeShopButton.setOnClickListener(onGoToShopDetailFromButton(header.shopId))
        pagingHandler.setHasNext(listDetail.size > 1 && hasNextPage)
        adapter.notifyDataSetChanged()
        trackImpression(listDetail)
    }

    private fun setUpShopDataHeader(header: FeedDetailHeaderModel) {
        (activity as FeedPlusDetailActivity).getShopInfoLayout()?.run {
            val shopNameString = MethodChecker.fromHtml(header.shopName).toString()
            ImageHandler.LoadImage(shopAvatar, header.shopAvatar)
            officialStore.setImageUrl(header.badgeUrl)
            shopName.text = shopNameString
            shopName.movementMethod = LinkMovementMethod.getInstance()
            if (header.actionText.isNotEmpty()) {
                shopSlogan.text = String.format(
                        getString(com.tokopedia.feedcomponent.R.string.feed_header_time_format),
                        TimeConverter.generateTime(shopSlogan.context, header.time),
                        header.actionText)
            } else {
                shopSlogan.text = TimeConverter.generateTime(shopSlogan.context, header.time)
            }
            shopAvatar.setOnClickListener { onGoToShopDetail(header.activityId, header.shopId) }
            this.setOnClickListener { onGoToShopDetail(header.activityId, header.shopId) }
            show()
        }
    }

    private fun onGoToShopDetailFromButton(shopId: Int): View.OnClickListener? {
        return View.OnClickListener {
            arguments?.run {
                goToShopDetail(shopId)
                analytics.eventFeedClickShop(
                        screenName, shopId.toString(), getString(
                        FeedPlusDetailActivity.EXTRA_ANALYTICS_PAGE_ROW_NUMBER, "")
                        + FeedTrackingEventLabel.Click.VISIT_SHOP)
            }
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
        footer.hide()
        adapter.showLoading()
    }

    private fun dismissLoading() {
        footer.show()
        adapter.dismissLoading()
    }

    private fun showLoadingMore() {
        adapter.showLoadingMore()
    }

    private fun dismissLoadingMore() {
        adapter.dismissLoadingMore()
    }

    override fun onGoToProductDetail(feedDetailViewModel: FeedDetailItemModel, adapterPosition: Int) {
        if (activity != null && activity?.applicationContext != null && arguments != null) {
            activity?.startActivityForResult(
                    getProductIntent(feedDetailViewModel.productId.toString()),
                    REQUEST_OPEN_PDP
            )
            analytics.eventDetailProductClick(
                    ProductEcommerce(feedDetailViewModel.productId.toString(),
                            feedDetailViewModel.name,
                            feedDetailViewModel.price,
                            adapterPosition),
                    userSession.userId?.toIntOrNull() ?: 0
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

    private fun trackImpression(listDetail: ArrayList<Visitable<*>>) {
        val productList = ArrayList<ProductEcommerce>()
        for (position in listDetail.indices) {
            if (listDetail[position] is FeedDetailItemModel) {
                val model = listDetail[position] as FeedDetailItemModel
                productList.add(ProductEcommerce(model.productId.toString(),
                        model.name,
                        model.price,
                        position
                ))
            }
        }
        analytics.eventDetailProductImpression(productList, userSession.userId?.toIntOrNull() ?: 0)
    }

    override fun urlCreated(linkerShareData: LinkerShareResult) {
        val intent = getIntent(linkerShareData.shareContents, linkerShareData.url)
        activity?.startActivity(Intent.createChooser(intent, TITLE_OTHER))
        sendTracker()
    }

    override fun onError(linkerError: LinkerError?) {}

    private fun getIntent(contains: String, url: String): Intent {
        var str: String? = contains
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = TYPE
        val title: String? = shareData.name
        if (!TextUtils.isEmpty(shareData.custmMsg) && shareData.custmMsg.contains(PLACEHOLDER_LINK)) {
            str = FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.custmMsg, PLACEHOLDER_LINK, url)
        }
        mIntent.putExtra(Intent.EXTRA_TITLE, title)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        mIntent.putExtra(Intent.EXTRA_TEXT, str)
        return mIntent
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

}
