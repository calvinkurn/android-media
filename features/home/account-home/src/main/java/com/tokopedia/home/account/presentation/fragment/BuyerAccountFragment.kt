package com.tokopedia.home.account.presentation.fragment

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.ToasterError
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.R
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator
import com.tokopedia.home.account.di.component.DaggerBuyerAccountComponent
import com.tokopedia.home.account.presentation.BuyerAccount
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

import kotlinx.android.synthetic.main.fragment_buyer_account.*

import javax.inject.Inject

/**
 * @author okasurya on 7/16/18.
 */
class BuyerAccountFragment : BaseAccountFragment(), BuyerAccount.View, FragmentListener {

    @Inject
    lateinit var presenter: BuyerAccount.Presenter

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private val adapter:BuyerAccountAdapter = BuyerAccountAdapter(AccountTypeFactory(this), arrayListOf())
    private var snackBar: Snackbar? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var fpmBuyer: PerformanceMonitoring? = null
    private var layoutManager: StaggeredGridLayoutManager = StaggeredGridLayoutManager(
            DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fpmBuyer = PerformanceMonitoring.start(FPM_BUYER)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buyer_account, container, false)

        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessRecyclerViewScrollListener?.let {
            recycler_buyer.addOnScrollListener(it)
        }
        recycler_buyer.layoutManager = layoutManager
        recycler_buyer.adapter = adapter

        swipe_refresh_layout.setColorSchemeResources(R.color.tkpd_main_green)

        swipe_refresh_layout.setOnRefreshListener { this.getData() }
        sendBuyerAccountItemImpression()
    }

    private fun sendBuyerAccountItemImpression() {
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_TOKOPOINTS, AccountConstants.Analytics.POSITION_TOKOPOINT
        ))
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_KUPON_SAYA, AccountConstants.Analytics.POSITION_KUPON_SAYA
        ))
        onAccountItemImpression(AccountAnalytics.getAccountPromoImpression(
                AccountConstants.Analytics.CREATIVE_TOKO_MEMBER, AccountConstants.Analytics.POSITION_TOKOMEMBER
        ))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
        }
    }

    override fun onResume() {
        super.onResume()
        scrollToTop()
        context?.let {
            GraphqlClient.init(it)
            getData()
        }
    }

    override fun getScreenName(): String {
        return String.format("/%s/%s",
                AccountConstants.Analytics.USER,
                AccountConstants.Analytics.BELI)
    }

    override fun loadBuyerData(model: BuyerViewModel?) {
        if (model != null) {
            model.items?.let {
                adapter.clearAllElements()
                adapter.setElement(it)

                snackBar?.dismiss()
                snackBar = null
            }
        } else {
            context?.let {
                adapter.clearAllElements()
                adapter.setElement(StaticBuyerModelGenerator.getModel(it, null, remoteConfig))
            }
        }

        fpmBuyer?.run { stopTrace() }
        presenter.getFirstRecomData()
    }

    override fun showLoading() {
        adapter.showLoading()
        scrollToTop()
    }

    override fun hideLoading() {
        adapter.hideLoading()

        if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing)
            swipe_refresh_layout.isRefreshing = false
    }

    override fun showError(message: String) {
        if (view != null && userVisibleHint) {
            snackBar = ToasterError.make(view, message)
            snackBar?.let {
                it.setAction(getString(R.string.title_try_again)) { getData() }
                it.show()
            }
        }

        fpmBuyer?.run { stopTrace() }
    }

    override fun showError(e: Throwable) {
        if (view != null && context != null && userVisibleHint) {
            snackBar = ToasterError.make(view, ErrorHandler.getErrorMessage(context, e))
            snackBar?.let {
                it.setAction(getString(R.string.title_try_again)) { getData() }
                it.show()
            }
        }

        fpmBuyer?.run { stopTrace() }
    }

    override fun showErrorNoConnection() {
        showError(getString(R.string.error_no_internet_connection))
    }

    override fun onScrollToTop() {
        if (recycler_buyer != null) {
            recycler_buyer?.run {
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
        sendProductImpressionTracking(getTrackingQueue(), product, adapterPosition)
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
        }
    }

    override fun hideLoadMoreLoading() {
        adapter.hideLoading()
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
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

    private fun getData() {
        scrollToTop()
        endlessRecyclerViewScrollListener?.resetState()

        context?.let {
            val saldoQuery = GraphqlHelper.loadRawString(it.resources, R.raw
                    .new_query_saldo_balance)
            presenter.getBuyerData(GraphqlHelper.loadRawString(it.resources, R.raw
                    .query_buyer_account_home), saldoQuery)
        }
    }

    private fun initInjector() {
        val component = DaggerBuyerAccountComponent.builder()
                .baseAppComponent(
                        (activity?.application as BaseMainApplication).baseAppComponent
                ).build()

        component.inject(this)
        presenter.attachView(this)
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                presenter.getRecomData(page)
            }
        }
    }

    fun updateWishlist(wishlistStatusFromPdp: Boolean, position: Int) {
        if(adapter.list.get(position) is RecommendationProductViewModel){
            (adapter.list.get(position) as RecommendationProductViewModel).product.isWishlist = wishlistStatusFromPdp
            adapter.notifyItemChanged(position)
        }
    }

    fun scrollToTop() {
        recycler_buyer.scrollToPosition(0)
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
}
