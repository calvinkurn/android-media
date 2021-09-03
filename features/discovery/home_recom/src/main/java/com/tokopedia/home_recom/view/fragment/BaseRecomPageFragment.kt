package com.tokopedia.home_recom.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.util.RecomPageConstant.RV_SPAN_COUNT
import com.tokopedia.home_recom.util.RecomPageUiUpdater
import com.tokopedia.home_recom.view.adapter.RecomPageAdapter
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by yfsx on 30/08/21.
 */
abstract class BaseRecomPageFragment<T : Visitable<*>, F : AdapterTypeFactory> : BaseDaggerFragment() {

    var homeRecommendationComponent: HomeRecommendationComponent? = null
    var productAdapter: RecomPageAdapter? = null
    var navToolbar: NavToolbar? = null
    var chooseAddressWidget: ChooseAddressWidget? = null

    private var recyclerView: RecyclerView? = null
    private lateinit var root: ConstraintLayout
    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private var isNewNavigation = false

    private val ROLLANCE_EXP_NAME = RollenceKey.NAVIGATION_EXP_TOP_NAV
    private val ROLLANCE_VARIANT_OLD = RollenceKey.NAVIGATION_VARIANT_OLD
    private val ROLLANCE_VARIANT_REVAMP = RollenceKey.NAVIGATION_VARIANT_REVAMP

    private var errorToaster: Snackbar? = null

    protected abstract fun createAdapterInstance(): RecomPageAdapter

    protected abstract fun loadInitData(forceRefresh: Boolean = false)

    protected abstract fun loadMoreData(pageNumber: Int = 1)

    protected abstract fun observeData()

    protected abstract fun onCreateExtended(savedInstanceState: Bundle?)

    protected abstract fun onChooseAddressImplemented(): ChooseAddressWidget.ChooseAddressWidgetListener

    companion object {
        private const val ERROR_COBA_LAGI = "Coba Lagi"
    }

    open fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadInitData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        setHasOptionsMenu(true)
        productAdapter = createAdapterInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base_recom_page, container, false)
        navToolbar = view.findViewById(R.id.navToolbar)
        root = view.findViewById(R.id.root)
        chooseAddressWidget = view.findViewById(R.id.widget_choose_address)
        initNavBar()
        initChooseAddress()
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
            productAdapter?.submitList(visitables)
        }
    }

    fun submitList(visitables: List<HomeRecommendationDataModel>) {
        recyclerView?.post {
            productAdapter?.submitList(visitables)
        }
    }

    fun showLoading() {
//        productAdapter?.showLoading()
    }

    fun renderPageError(errorModel: RecommendationErrorDataModel) {
        context?.let { ctx ->
            productAdapter?.showError(errorModel)
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

    private fun getEndlessLayoutManagerListener(): EndlessLayoutManagerListener? {
        return null
    }

    protected open fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerView!!.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
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

    private fun hideSwipeLoading() {
        swipeToRefresh?.let {
            it.isEnabled = true
            it.isRefreshing = false
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = StaggeredGridLayoutManager(RV_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.itemAnimator = null
        showLoading()

        recyclerView?.adapter = productAdapter
    }

    private fun initNavBar() {
        getRollenceNavigationValue()
        var iconBuilder = IconBuilder()
        if (isNewNavigation) {
            iconBuilder = IconBuilder()
                    .addIcon(IconList.ID_CART) {}
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
        }
        navToolbar?.setIcon(iconBuilder)
        navToolbar?.setToolbarTitle("tes")
    }

    private fun initChooseAddress() {
        chooseAddressWidget?.bindChooseAddress(onChooseAddressImplemented())
    }

    private fun getRollenceNavigationValue() {
        try {
            val rollanceNavType = RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLANCE_EXP_NAME, ROLLANCE_VARIANT_OLD)
            this.isNewNavigation = rollanceNavType.equals(ROLLANCE_VARIANT_REVAMP, ignoreCase = true)
        } catch (e: java.lang.Exception) {
            this.isNewNavigation = false
        }
    }
}