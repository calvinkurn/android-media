package com.tokopedia.home_recom.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.util.RecomPageConstant
import com.tokopedia.home_recom.util.RecomPageConstant.PAGE_TITLE_RECOM_DEFAULT
import com.tokopedia.home_recom.util.RecomPageConstant.RV_SPAN_COUNT
import com.tokopedia.home_recom.util.showToastError
import com.tokopedia.home_recom.util.showToastErrorWithAction
import com.tokopedia.home_recom.view.adapter.RecomPageAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by yfsx on 30/08/21.
 */
abstract class BaseRecomPageFragment<T : Visitable<*>, F : AdapterTypeFactory> : BaseDaggerFragment() {

    var homeRecommendationComponent: HomeRecommendationComponent? = null
    var productAdapter: RecomPageAdapter? = null
    var navToolbar: NavToolbar? = null
    var chooseAddressWidget: ChooseAddressWidget? = null
    var miniCartWidget: MiniCartWidget? = null

    private var recyclerView: RecyclerView? = null
    private lateinit var root: ConstraintLayout
    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var trackingQueue: TrackingQueue? = null
    private lateinit var userSession: UserSessionInterface

    private var isNewNavigation = false

    private var errorToaster: Snackbar? = null

    protected abstract fun createAdapterInstance(): RecomPageAdapter

    protected abstract fun loadInitData(forceRefresh: Boolean = false)

    protected abstract fun loadMoreData(pageNumber: Int = 1)

    protected abstract fun observeData()

    protected abstract fun onCreateExtended(savedInstanceState: Bundle?)

    protected abstract fun setDefaultPageTitle(): String

    protected abstract fun onChooseAddressImplemented(): ChooseAddressWidget.ChooseAddressWidgetListener?

    protected abstract fun shouldPageImplementChooseAddress(): Boolean

    protected abstract fun shouldPageImplementMiniCartWidget(): Boolean

    protected abstract fun setShopId(): String

    protected abstract fun setImplementingFragment(): Fragment

    protected abstract fun setMiniCartWidgetListener(): MiniCartWidgetListener

    protected abstract fun setMiniCartPageName(): MiniCartAnalytics.Page

    companion object {
        private const val ERROR_COBA_LAGI = "Coba Lagi"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
    }

    open fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadInitData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
        setHasOptionsMenu(true)
        productAdapter = createAdapterInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base_recom_page, container, false)
        navToolbar = view.findViewById(R.id.navToolbar)
        root = view.findViewById(R.id.root)
        chooseAddressWidget = view.findViewById(R.id.widget_choose_address)
        miniCartWidget = view.findViewById(R.id.widget_minicart)
        initNavBar()
        initChooseAddress()
        initMiniCartWidget()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeLayout(view)
        setupRecyclerView(view)
        onCreateExtended(savedInstanceState)

        enableLoadMore()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    fun submitInitialList(visitables: List<HomeRecommendationDataModel>) {
        hideSwipeLoading()
        recyclerView?.post {
            productAdapter?.clearAllElements()
            productAdapter?.submitList(visitables)
        }
        endlessRecyclerViewScrollListener?.resetState()
        endlessRecyclerViewScrollListener?.setHasNextPage(true)
        scrollToTop()
    }

    fun submitList(visitables: List<HomeRecommendationDataModel>) {
        recyclerView?.post {
            productAdapter?.submitList(visitables)
        }
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(true)
    }

    fun showFirstLoading() {
        productAdapter?.showFirstLoading()
    }

    fun removeFirstLoading() {
        productAdapter?.removeFirstLoading()
    }

    fun appendLoadingForLoadMore() {
        productAdapter?.appendLoadingForLoadMore()
    }

    fun removeLoadingForLoadMore() {
        productAdapter?.removeLoadingForLoadMore()
    }

    fun renderPageError(throwable: Throwable) {
        context?.let { ctx ->
            productAdapter?.showError(RecommendationErrorDataModel(throwable))
            swipeToRefresh?.let {
                it.isEnabled = false
            }
        }
    }

    fun renderEmptyPage(error: RecommendationErrorDataModel = RecommendationErrorDataModel(throwable = Throwable(), type = GlobalError.PAGE_NOT_FOUND)) {
        context?.let { ctx ->
            productAdapter?.showEmpty(error)
            swipeToRefresh?.let {
                it.isEnabled = false
            }
        }
    }

    fun getViewHolderByPosition(position: Int): RecyclerView.ViewHolder? {
        if (position == -1) {
            return null
        }
        return recyclerView?.findViewHolderForAdapterPosition(position)
    }

    fun scrollToPosition(position: Int) {
        if (position >= 0) {
            getRecyclerView()?.post {
                try {
                    getRecyclerView()?.smoothScrollToPosition(position)
                } catch (e: Throwable) {
                }
            }
        }
    }

    fun enableLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            endlessRecyclerViewScrollListener?.setEndlessLayoutManagerListener(getEndlessLayoutManagerListener())
        }
        endlessRecyclerViewScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
    }

    fun resetLoadMore() {
        endlessRecyclerViewScrollListener?.resetState()
    }

    fun disableLoadMore() {
        endlessRecyclerViewScrollListener?.let {
            it.setHasNextPage(false)
            recyclerView?.removeOnScrollListener(it)
        }
    }

    fun hideChooseAddressWidget() {
        chooseAddressWidget?.gone()
    }

    fun showMiniCartWidget() {
        if (getUserSession().isLoggedIn) miniCartWidget?.show() else miniCartWidget?.gone()
    }

    fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    fun updateMiniCart(shopId: String) {
        miniCartWidget?.updateData(listOf(shopId))
    }

    fun goToPDP(productId: String, position: Int) {
        try {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId).run {
                putExtra(PDP_EXTRA_UPDATED_POSITION, position)
                startActivityForResult(this, REQUEST_FROM_PDP)
            }
        } catch (e: Exception) {

        }
    }

    fun hideSwipeLoading() {
        swipeToRefresh?.let {
            it.isEnabled = true
            it.isRefreshing = false
        }
    }

    fun updateMinicartWidgetVisibility(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartWidget?.let {
            if (miniCartSimplifiedData.miniCartItems.isEmpty()) {
                it.gone()
            } else {
                it.updateData(miniCartSimplifiedData)
                it.show()
            }
        }
    }

    fun goToLogin() {
        activity?.let {
            startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    RecomPageConstant.REQUEST_CODE_LOGIN)
        }
    }

    fun isWarehouseIdEmpty(): Boolean {
        val localAddress = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        localAddress?.let {
            if (it.warehouse_id.isNullOrEmpty()) return true
            if (it.warehouse_id == "0") return true
        }
        return false
    }

    fun showEmptyPage() {
        renderEmptyPage()
    }

    fun showErrorFullPage(throwable: Throwable) {
        renderPageError(throwable)
    }

    fun showErrorSnackbarWithRetryLoad(pageNumber: Int, throwable: Throwable) {
        showToastErrorWithAction(throwable, View.OnClickListener {
            loadMoreData(pageNumber)
        })
    }

    fun showErrorSnackbar(throwable: Throwable) {
        showToastError(throwable)
    }

    fun getTrackingQueueObj(): TrackingQueue? {
        if (trackingQueue == null) {
            activity?.let {
                trackingQueue = TrackingQueue(it)
            }
        }
        return trackingQueue
    }

    fun getUserSession(): UserSessionInterface {
        if (!::userSession.isInitialized) {
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }

    private fun getEndlessLayoutManagerListener(): EndlessLayoutManagerListener? {
        return null
    }

    protected open fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerView!!.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (page != 0) appendLoadingForLoadMore()
                loadMoreData(page + 1)
            }
        }
    }

    protected fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun setupSwipeLayout(view: View) {
        swipeToRefresh = view.findViewById<View>(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        swipeToRefresh?.let {
            it.setOnRefreshListener { onSwipeRefresh() }
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = StaggeredGridLayoutManager(RV_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.itemAnimator = null
        showFirstLoading()
        recyclerView?.adapter = productAdapter
    }

    private fun initNavBar() {
        val iconBuilder = IconBuilder()
                    .addIcon(IconList.ID_CART) {}
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.let {
            it.setShowShadowEnabled(true)
            it.setIcon(iconBuilder)
            it.setToolbarTitle(if (setDefaultPageTitle().isNotEmpty()) setDefaultPageTitle() else PAGE_TITLE_RECOM_DEFAULT)
            activity?.let { actv ->
                it.setupToolbarWithStatusBar(activity = actv, applyPadding = false, applyPaddingNegative = true)
            }
            viewLifecycleOwner.lifecycle.addObserver(it)
        }

    }

    private fun initChooseAddress() {
        onChooseAddressImplemented()?.let {
            chooseAddressWidget?.bindChooseAddress(it)
        }
    }

    private fun initMiniCartWidget() {
        if (shouldPageImplementMiniCartWidget()) {
            miniCartWidget?.initialize(
                    shopIds = listOf(setShopId()),
                    fragment = setImplementingFragment(),
                    listener = setMiniCartWidgetListener(),
                    autoInitializeData = false,
                    pageName = setMiniCartPageName()
            )
        }
    }
}