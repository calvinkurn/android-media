package com.tokopedia.travel.homepage.presentation.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingUtil
import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.di.TravelHomepageComponent
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageTypeFactory
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import com.tokopedia.travel.homepage.presentation.viewmodel.TravelHomepageViewModel
import kotlinx.android.synthetic.main.travel_homepage_fragment.*
import javax.inject.Inject

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment : BaseListFragment<TravelHomepageItemModel, TravelHomepageTypeFactory>(), OnItemBindListener, OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var travelHomepageViewModel: TravelHomepageViewModel

    @Inject
    lateinit var travelHomepageTrackingUtil: TravelHomepageTrackingUtil

    private var searchBarTransitionRange = 0

    override fun getAdapterTypeFactory(): TravelHomepageTypeFactory = TravelHomepageAdapterTypeFactory(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            travelHomepageViewModel = viewModelProvider.get(TravelHomepageViewModel::class.java)
        }

        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.toolbar_transition_range)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.travel_homepage_fragment, container, false)
        return view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        travel_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        calculateToolbarView(0)

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()
        getRecyclerView(view).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(getRecyclerView(view).computeVerticalScrollOffset())
            }
        })
    }

    private fun hideStatusBar() {
        travel_homepage_container.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            travel_homepage_container.requestApplyInsets()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = travel_homepage_container.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            travel_homepage_container.systemUiVisibility = flags
            activity?.window?.statusBarColor = Color.WHITE
        }

        if (Build.VERSION.SDK_INT in 19..20) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= 19) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun calculateToolbarView(offset: Int) {

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / searchBarTransitionRange * (offset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        if (offsetAlpha >= 255) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            travel_homepage_toolbar.toOnScrolledMode()
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            travel_homepage_toolbar.toInitialMode()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        travelHomepageViewModel.travelItemList.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })

        travelHomepageViewModel.isAllError.observe(this, Observer {
            it?.let { isAllError ->
                if (isAllError) NetworkErrorHelper.showEmptyState(context, view?.rootView, object : NetworkErrorHelper.RetryClickedListener {
                    override fun onRetryClicked() {
                        loadDataFromCloud()
                    }
                })
            }
        })
    }

    override fun onItemClicked(t: TravelHomepageItemModel) {
        // do nothing
    }

    override fun loadData(page: Int) {
        travelHomepageViewModel.getIntialList(swipeToRefresh?.isRefreshing ?: false)
    }

    fun loadDataFromCloud() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        travelHomepageViewModel.getIntialList(true)
    }

    override fun initInjector() {
        getComponent(TravelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onBannerVHItemBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getBanner(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_banner), isFromCloud
                ?: true)
    }

    override fun onCategoryVHBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getCategories(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_category_list), isFromCloud
                ?: true)
    }

    override fun onDestinationVHBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getDestination(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_destination), isFromCloud
                ?: true)
    }

    override fun onOrderListVHBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getOrderList(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_order_list), isFromCloud
                ?: true)
    }

    override fun onRecentSearchVHBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recent_search), isFromCloud
                ?: true)
    }

    override fun onRecommendationVHBind(isFromCloud: Boolean?) {
        travelHomepageViewModel.getRecommendation(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation), isFromCloud
                ?: true)
    }

    override fun onItemClick(appUrl: String, webUrl: String) {
        context?.run {
            when {
                RouteManager.isSupportApplink(this, appUrl) -> RouteManager.route(this, appUrl)
                DeeplinkMapper.getRegisteredNavigation(this, appUrl).isNotEmpty() -> RouteManager.route(this, DeeplinkMapper.getRegisteredNavigation(this, appUrl))
                webUrl.isNotEmpty() -> RouteManager.route(this, webUrl)
                else -> { /* do nothing */ }
            }
        }
    }

    override fun onTrackEventClick(type: Int, position: Int, categoryName: String) {
        when (type) {
            TYPE_ALL_PROMO -> travelHomepageTrackingUtil.travelHomepageClickAllBanner()
            TYPE_ORDER_LIST -> travelHomepageTrackingUtil.travelHomepageClickOrder(position, categoryName)
            TYPE_ALL_ORDER_LIST -> travelHomepageTrackingUtil.travelHomepageClickAllOrder()
            TYPE_RECENT_SEARCH -> travelHomepageTrackingUtil.travelHomepageClickRecentSearch(position, categoryName)
            TYPE_POPULAR_SEARCH -> travelHomepageTrackingUtil.travelHomepageClickPopularSearch(position, categoryName)
            TYPE_ALL_DEALS -> travelHomepageTrackingUtil.travelHomepageClickAllDeals()
        }
    }

    override fun onTrackBannerImpression(banner: TravelHomepageBannerModel.Banner, position: Int) {
        travelHomepageTrackingUtil.travelHomepageImpressionBanner(banner, position)
    }

    override fun onTrackBannerClick(banner: TravelHomepageBannerModel.Banner, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickBanner(banner, position)
    }

    override fun onTrackCategoryClick(category: TravelHomepageCategoryListModel.Category, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickCategory(category, position)
    }

    override fun onTrackDealsClick(deal: TravelHomepageSectionViewModel.Item, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickDeal(deal, position)
    }

    override fun onTrackPopularDestinationClick(destination: TravelHomepageDestinationModel.Destination, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickPopularDestination(destination, position)
    }

    companion object {
        fun getInstance(): TravelHomepageFragment = TravelHomepageFragment()

        const val TYPE_ORDER_LIST = 1
        const val TYPE_RECENT_SEARCH = 2
        const val TYPE_RECOMMENDATION = 3
        const val TYPE_POPULAR_SEARCH = 4
        const val TYPE_ALL_PROMO = 5
        const val TYPE_ALL_ORDER_LIST = 6
        const val TYPE_ALL_DEALS = 7

        const val TYPE_POPULAR_SEARCH_CATEGORY = "popularDestination"

    }
}