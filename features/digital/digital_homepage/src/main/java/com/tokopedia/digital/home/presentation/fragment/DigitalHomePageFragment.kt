package com.tokopedia.digital.home.presentation.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.digital.home.APPLINK_HOME_FAV_LIST
import com.tokopedia.digital.home.APPLINK_HOME_MYBILLS
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import com.tokopedia.digital.home.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomePageCategoryDataMapper
import com.tokopedia.digital.home.presentation.Util.DigitalHomeTrackingUtil
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.NEW_USER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageSearchActivity
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageTransactionViewHolder
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel.Companion.FAVORITES_ORDER
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel.Companion.NEW_USER_ZONE_ORDER
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel.Companion.SPOTLIGHT_ORDER
import kotlinx.android.synthetic.main.layout_digital_home.*
import javax.inject.Inject

class DigitalHomePageFragment : BaseListFragment<DigitalHomePageItemModel, DigitalHomePageTypeFactory>(),
        OnItemBindListener,
        DigitalHomePageTransactionViewHolder.TransactionListener,
        SearchInputView.FocusChangeListener {

    @Inject
    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalHomePageViewModel
    private var searchBarTransitionRange = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_digital_home, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageViewModel::class.java)
        }

        searchBarTransitionRange = resources.getDimensionPixelSize(TOOLBAR_TRANSITION_RANGE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        digital_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        digital_homepage_search_view.setFocusChangeListener(this)
        calculateToolbarView(0)

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()
        getRecyclerView(view).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(getRecyclerView(view).computeVerticalScrollOffset())
            }
        })

        digital_homepage_order_list.setOnClickListener {
            onClickOrderList()
        }

        trackingUtil.resetInitialImpressionTracking()
    }

    private fun hideStatusBar() {
        digital_homepage_container.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            digital_homepage_container.requestApplyInsets()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = digital_homepage_container.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            digital_homepage_container.systemUiVisibility = flags
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

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    private fun calculateToolbarView(offset: Int) {

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / searchBarTransitionRange * (offset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        val searchBarContainer = digital_homepage_search_view.findViewById<LinearLayout>(com.tokopedia.common_digital.R.id.search_input_view_container)
        if (offsetAlpha >= 255) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            digital_homepage_toolbar.toOnScrolledMode()
            digital_homepage_order_list.setColorFilter(R.color.Neutral_N200)
            context?.run { searchBarContainer.background =
                        MethodChecker.getDrawable(this, R.drawable.bg_digital_homepage_search_view_background_gray) }
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            digital_homepage_toolbar.toInitialMode()
            digital_homepage_order_list.clearColorFilter()
            context?.run { searchBarContainer.background =
                        MethodChecker.getDrawable(this, R.drawable.bg_digital_homepage_search_view_background) }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun initInjector() {
        getComponent(DigitalHomePageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.digitalHomePageList.observe(this, Observer {
            clearAllData()
            it?.run {
                DigitalHomePageCategoryDataMapper.mapCategoryData(this[DigitalHomePageViewModel.CATEGORY_ORDER])?.let { categoryData ->
                    trackingUtil.eventCategoryImpression(categoryData)
                }
                val list = this.filter { item -> !item.isEmpty }
                renderList(list)
            }
        })

        viewModel.isAllError.observe(this, Observer {
            it?.let { isAllError ->
                if (isAllError) NetworkErrorHelper.showEmptyState(context, view?.rootView, object : NetworkErrorHelper.RetryClickedListener {
                    override fun onRetryClicked() {
                        loadDataFromCloud()
                    }
                })
            }
        })
    }

    fun loadDataFromCloud() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        viewModel.getInitialList(true)
    }

    override fun loadData(page: Int) {
        trackingUtil.resetInitialImpressionTracking()
        viewModel.getInitialList(swipeToRefresh?.isRefreshing ?: false)
    }

    override fun loadInitialData() {
        loadDataFromCloud()
    }

    override fun onBannerItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getBannerList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_banner), loadFromCloud
                ?: true)
    }

    override fun onCategoryItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getCategoryList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_category), loadFromCloud
                ?: true)
    }

    override fun onPromoItemDigitalBind() {
        //nothing to do, api not ready yet
    }

    override fun onFavoritesItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getFavoritesList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section), loadFromCloud
                ?: true)
    }

    override fun onTrustMarkItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getTrustMarkList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section), loadFromCloud
                ?: true)
    }

    override fun onNewUserZoneItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getNewUserZoneList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section), loadFromCloud
                ?: true)
    }

    override fun onSpotlightItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getSpotlightList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section), loadFromCloud
                ?: true)
    }

    override fun onSubscriptionItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getSubscriptionList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section), loadFromCloud
                ?: true)
    }

    override fun onRecommendationItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getRecommendationList(GraphqlHelper.loadRawString(resources, com.tokopedia.common_digital.R.raw.digital_recommendation_list), loadFromCloud
                ?: true)
    }

    override fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        trackingUtil.eventCategoryClick(element, position)
        RouteManager.route(activity, element?.applink)
    }

    override fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int) {
        trackingUtil.eventBannerClick(element, position)
        RouteManager.route(activity, element?.applink)
    }

    override fun onBannerAllItemClicked() {
        trackingUtil.eventClickAllBanners()
        RouteManager.route(activity, ApplinkConst.PROMO_LIST)
    }

    override fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, i: Int, sectionType: String) {
        if (sectionType == SUBSCRIPTION_GUIDE_CLICK) {
            trackingUtil.eventClickSubscriptionGuide()
        } else {
            trackingUtil.eventSectionClick(element, i, sectionType)
        }
        RouteManager.route(activity, element.applink)
    }

    override fun onSectionItemImpression(sectionType: String) {
        val sectionOrder = when(sectionType) {
            BEHAVIORAL_CATEGORY_IMPRESSION -> FAVORITES_ORDER
            NEW_USER_IMPRESSION -> NEW_USER_ZONE_ORDER
            SPOTLIGHT_IMPRESSION -> SPOTLIGHT_ORDER
            else -> -1
        }
        if (sectionOrder >= 0) {
            viewModel.digitalHomePageList.value?.get(sectionOrder)?.let { item ->
                if (item is DigitalHomePageSectionModel && item.isLoaded && item.isSuccess) {
                    item.data?.section?.items?.let { data ->
                        trackingUtil.eventSectionImpression(data, sectionType)
                    }
                }
            }
        }
    }

    override fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, position: Int) {
        trackingUtil.eventBannerImpression(banner, position)
    }

    override fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        // do nothing
    }

    override fun onRecommendationImpression(element: RecommendationItemEntity, position: Int) {
        // do nothing
    }

    override fun getAdapterTypeFactory(): DigitalHomePageTypeFactory {
        return DigitalHomePageTypeFactory(this, this)
    }

    override fun onItemClicked(t: DigitalHomePageItemModel?) {
        // do nothing
    }

    override fun onClickFavNumber() {
        trackingUtil.eventClickFavNumber()
        RouteManager.route(activity, APPLINK_HOME_FAV_LIST)
    }

    override fun onClickOrderList() {
        trackingUtil.eventClickOrderList()
        RouteManager.route(activity, ApplinkConst.DIGITAL_ORDER)
    }

    override fun onClickHelp() {
        trackingUtil.eventClickHelp()
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    override fun onClickMyBills() {
        trackingUtil.eventClickLangganan()
        RouteManager.route(activity, APPLINK_HOME_MYBILLS)
    }

    override fun onFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            digital_homepage_search_view.searchTextView.clearFocus()
            trackingUtil.eventClickSearchBox()
            context?.let{ context -> startActivity(DigitalHomePageSearchActivity.getCallingIntent(context)) }
        }
    }

    fun onBackPressed() {
        trackingUtil.eventClickBackButton()
    }

    companion object {
        val TOOLBAR_TRANSITION_RANGE = com.tokopedia.design.R.dimen.dp_8

        fun getInstance() = DigitalHomePageFragment()
    }
}
