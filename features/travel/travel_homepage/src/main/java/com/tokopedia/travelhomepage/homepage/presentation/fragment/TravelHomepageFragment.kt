package com.tokopedia.travelhomepage.homepage.presentation.fragment

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
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.common.util.TravelHomepageGqlQuery
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingUtil
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.di.TravelHomepageComponent
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageAdapter
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageTypeFactory
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import com.tokopedia.travelhomepage.homepage.presentation.viewmodel.TravelHomepageViewModel
import kotlinx.android.synthetic.main.travel_homepage_fragment.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment : BaseListFragment<TravelHomepageItemModel,
        TravelHomepageTypeFactory>(), OnItemBindListener, TravelHomepageActionListener,
        CoroutineScope {

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

        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.destination_toolbar_transition_range)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.travel_homepage_fragment, container, false)
    }

    override fun createAdapterInstance(): BaseListAdapter<TravelHomepageItemModel, TravelHomepageTypeFactory> {
        val baseListAdapter = TravelHomepageAdapter(adapterTypeFactory)
        baseListAdapter.setOnAdapterInteractionListener(this)
        baseListAdapter.setHasStableIds(true)
        return baseListAdapter
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        travel_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        calculateToolbarView(0)

        (getRecyclerView(view) as? VerticalRecyclerView)?.clearItemDecoration()

        getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                getRecyclerView(view)?.computeVerticalScrollOffset()?.let {
                    calculateToolbarView(it)
                }
            }
        })
    }

    private fun hideStatusBar() {
        travel_homepage_container.fitsSystemWindows = false
        travel_homepage_container.requestApplyInsets()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = travel_homepage_container.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            travel_homepage_container.systemUiVisibility = flags
            context?.let {
                activity?.window?.statusBarColor = androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }

        if (Build.VERSION.SDK_INT in 19..20) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

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

    var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        travelHomepageViewModel.travelItemListLiveData.observe(viewLifecycleOwner, Observer {
            if(job.isActive) {
                job.cancel()
            }
            job = launch {
                isLoadingInitialData = true
                renderList(it, false)
                delay(750L)
            }
        })

        travelHomepageViewModel.isAllError.observe(viewLifecycleOwner, Observer {
            it?.let { isAllError ->
                if (isAllError) {
                    clearAllData()
                    NetworkErrorHelper.showEmptyState(context, view) { loadDataFromCloud() }
                }
            }
        })
    }

    override fun onItemClicked(t: TravelHomepageItemModel) {
        // do nothing
    }

    override fun loadData(page: Int) {
        travelHomepageViewModel.getListFromCloud(TravelHomepageGqlQuery.LAYOUT_SUBHOMEPAGE,
                swipeToRefresh?.isRefreshing ?: false)
    }

    private fun loadDataFromCloud() {
        isLoadingInitialData = true
        clearAllData()
        showLoading()
        showSwipeLoading()
        travelHomepageViewModel.getListFromCloud(TravelHomepageGqlQuery.LAYOUT_SUBHOMEPAGE, true)
    }

    override fun initInjector() {
        getComponent(TravelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onBannerItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.SliderBannerResponse::class.java)
    }

    override fun onCategoryItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.CategoryResponse::class.java)
    }

    override fun onDestinationItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.DestinationResponse::class.java)
    }

    override fun onLegoBannerItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)
    }

    override fun onProductCardItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
    }

    override fun onHomepageSectionItemBind(travelLayoutSubhomepage: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        travelHomepageViewModel.getTravelUnifiedData(TravelHomepageGqlQuery.DYNAMIC_SUBHOMEPAGE,
                travelLayoutSubhomepage, true, TypeUnifiedSubhomepageResponse.HomepageSectionResponse::class.java)
    }

    override fun onItemClick(appUrl: String, webUrl: String) {
        context?.run {
            when {
                RouteManager.isSupportApplink(this, appUrl) -> RouteManager.route(this, appUrl)
                DeeplinkMapper.getRegisteredNavigation(this, appUrl).isNotEmpty() -> RouteManager.route(this, DeeplinkMapper.getRegisteredNavigation(this, appUrl))
                webUrl.isNotEmpty() -> RouteManager.route(this, webUrl)
                else -> { /* do nothing */
                }
            }
        }
    }

    override fun onViewSliderBanner(banner: TravelCollectiveBannerModel.Banner, position: Int) {
        travelHomepageTrackingUtil.travelHomepageImpressionBanner(banner, position)
    }

    override fun onClickSliderBannerItem(banner: TravelCollectiveBannerModel.Banner, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickBanner(banner, position)
    }

    override fun onClickSeeAllSliderBanner() {
        travelHomepageTrackingUtil.travelHomepageClickAllBanner()
    }

    override fun onClickDynamicIcon(category: TravelHomepageCategoryListModel.Category, position: Int) {
        travelHomepageTrackingUtil.travelHomepageClickCategory(category, position)
    }

    override fun onClickDynamicBannerItem(destination: TravelHomepageDestinationModel.Destination, position: Int, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageClickPopularDestination(destination, position, componentPosition, sectionTitle)
    }

    override fun onViewDynamicBanners(destinations: List<TravelHomepageDestinationModel.Destination>, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageDynamicBannerImpression(destinations, componentPosition, sectionTitle)
    }

    override fun onViewProductCards(list: List<ProductGridCardItemModel>, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelProductCardImpression(list, componentPosition, sectionTitle)
    }

    override fun onClickProductCard(item: ProductGridCardItemModel, position: Int, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelProductCardClick(item, position, componentPosition, sectionTitle)
    }

    override fun onClickSeeAllProductCards(componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageClickSeeAllProductCard(componentPosition, sectionTitle)
    }

    override fun onViewLegoBanner(list: List<LegoBannerItemModel>, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageLegoImpression(list, componentPosition, sectionTitle)
    }

    override fun onClickLegoBanner(item: LegoBannerItemModel, position: Int, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageLegoClick(item, position, componentPosition, sectionTitle)
    }

    override fun onViewProductSlider(list: List<TravelHomepageSectionModel.Item>, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelProductCardSliderImpression(list, componentPosition, sectionTitle)
    }

    override fun onClickProductSliderItem(item: TravelHomepageSectionModel.Item, position: Int, componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelSliderProductCardClick(item, position, componentPosition, sectionTitle)
    }

    override fun onClickSeeAllProductSlider(componentPosition: Int, sectionTitle: String) {
        travelHomepageTrackingUtil.travelHomepageClickSeeAllSliderProductCard(componentPosition, sectionTitle)
    }

    companion object {
        fun getInstance(): TravelHomepageFragment = TravelHomepageFragment()
    }
}