package com.tokopedia.home.account.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.ToasterError
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.di.component.BuyerAccountComponent
import com.tokopedia.home.account.di.component.DaggerBuyerAccountComponent
import com.tokopedia.home.account.presentation.BuyerAccount
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.trackingoptimizer.TrackingQueue

import java.util.ArrayList

import javax.inject.Inject

/**
 * @author okasurya on 7/16/18.
 */
class BuyerAccountFragment : BaseAccountFragment(), BuyerAccount.View, FragmentListener {
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var fpmBuyer: PerformanceMonitoring? = null

    lateinit var trackingQueue: TrackingQueue
    private var layoutManager: StaggeredGridLayoutManager = StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    private val adapter:BuyerAccountAdapter = BuyerAccountAdapter(AccountTypeFactory(this), arrayListOf())
    //    private RemoteConfig remoteConfig;

    @Inject
    lateinit var presenter: BuyerAccount.Presenter
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.run {
            trackingQueue = TrackingQueue(this)
        }
        fpmBuyer = PerformanceMonitoring.start(FPM_BUYER)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buyer_account, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_buyer)
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        recyclerView!!.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter
        swipeRefreshLayout!!.setColorSchemeResources(R.color.tkpd_main_green)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { this.getData() })
    }

    override fun onResume() {
        super.onResume()
        scrollToTop()
        if (context != null) {
            GraphqlClient.init(context!!)
            getData()
        }
    }

    private fun getData() {
        scrollToTop()
        endlessRecyclerViewScrollListener!!.resetState()

        val saldoQuery = GraphqlHelper.loadRawString(context!!.resources, R.raw
                .new_query_saldo_balance)
        presenter.getBuyerData(GraphqlHelper.loadRawString(context!!.resources, R.raw
                .query_buyer_account_home), saldoQuery)
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun loadBuyerData(model: BuyerViewModel) {
        if (model.items != null) {
            adapter.run {
                clearAllElements()
                setElement(model.items)
            }
        }
        fpmBuyer?.run {
            stopTrace()
        }
        presenter.getFirstRecomData()
    }

    private fun initInjector() {
        val component = DaggerBuyerAccountComponent.builder()
                .baseAppComponent(
                        (activity!!.application as BaseMainApplication).baseAppComponent
                ).build()

        component.inject(this)
        presenter.attachView(this)
    }

    override fun showLoading() {
        adapter.showLoading()
        scrollToTop()
    }

    override fun hideLoading() {
        adapter.hideLoading()

        if (swipeRefreshLayout != null && swipeRefreshLayout!!.isRefreshing)
            swipeRefreshLayout!!.isRefreshing = false
    }

    override fun showError(message: String) {
        if (view != null) {
            ToasterError.make(view, message)
                    .setAction(getString(R.string.title_try_again)) { view -> getData() }
                    .show()
        }
        fpmBuyer?.run {
            stopTrace()
        }
    }

    override fun showError(e: Throwable) {
        if (view != null && context != null) {
            ToasterError.make(view, ErrorHandler.getErrorMessage(context, e))
                    .setAction(getString(R.string.title_try_again)) { view -> getData() }
                    .show()
        }
        fpmBuyer?.run {
            stopTrace()
        }
    }

    override fun showErroNoConnection() {
        showError(getString(R.string.error_no_internet_connection))
    }

    override fun onScrollToTop() {
        if (recyclerView != null) {
            recyclerView?.run {
                scrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    internal override fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun onProductRecommendationClicked(product: RecommendationItem, adapterPosition: Int, widgetTitle: String) {
        sendProductClickTracking(product, adapterPosition, widgetTitle)
        if (product.isTopAds) ImpresionTask().execute(product.clickUrl)

        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, product.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, adapterPosition)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun onProductRecommendationImpression(product: RecommendationItem, adapterPosition: Int) {
        sendProductImpressionTracking(trackingQueue, product, adapterPosition)
        if (product.isTopAds) {
            ImpresionTask().execute(product.trackerImageUrl)
        }
    }

    override fun onProductRecommendationWishlistClicked(product: RecommendationItem, wishlistStatus: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        sendProductWishlistClickTracking(wishlistStatus)
        if (userSession.isLoggedIn) {
            if (wishlistStatus) {
                presenter.addWishlist(product, callback)
            } else {
                presenter.removeWishlist(product, callback)
            }
        } else {

        }
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                presenter.getRecomData(page)
            }
        }
    }

    override fun hideLoadMoreLoading() {
        adapter.hideLoading()
        endlessRecyclerViewScrollListener!!.updateStateAfterGetData()
    }

    override fun showLoadMoreLoading() {
        adapter.showLoading()
    }

    override fun onRenderRecomAccountBuyer(list: List<Visitable<*>>) {
        adapter.addElement(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
        }
    }

    fun updateWishlist(wishlistStatusFromPdp: Boolean, position: Int) {
        if(adapter.list.get(position) is RecommendationProductViewModel){
            (adapter.list.get(position) as RecommendationProductViewModel).product.isWishlist = wishlistStatusFromPdp
            adapter.notifyItemChanged(position)
        }
    }
    companion object {

        val TAG = BuyerAccountFragment::class.java.simpleName
        private val BUYER_DATA = "buyer_data"
        private val FPM_BUYER = "mp_account_buyer"
        private const val RECOMMENDATION_APP_LINK = "https://tokopedia.com/rekomendasi/%s"
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394

        private val DEFAULT_SPAN_COUNT = 2

        fun newInstance(): Fragment {
            val fragment = BuyerAccountFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    fun scrollToTop() {
        recyclerView?.run {
            scrollToPosition(0)
        }
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }
}
