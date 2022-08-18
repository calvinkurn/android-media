package com.tokopedia.digital.home.old.presentation.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.APPLINK_HOME_FAV_LIST
import com.tokopedia.digital.home.APPLINK_HOME_MYBILLS
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeBinding
import com.tokopedia.digital.home.old.di.DigitalHomePageComponent
import com.tokopedia.digital.home.old.domain.DigitalHomePageUseCase.Companion.QUERY_BANNER
import com.tokopedia.digital.home.old.domain.DigitalHomePageUseCase.Companion.QUERY_CATEGORY
import com.tokopedia.digital.home.old.domain.DigitalHomePageUseCase.Companion.QUERY_RECOMMENDATION
import com.tokopedia.digital.home.old.domain.DigitalHomePageUseCase.Companion.QUERY_SECTIONS
import com.tokopedia.digital.home.old.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.model.DigitalHomePageItemModel
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.digital.home.old.presentation.adapter.viewholder.DigitalHomePageTransactionViewHolder
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.old.presentation.util.DigitalHomePageCategoryDataMapper
import com.tokopedia.digital.home.old.presentation.util.DigitalHomeTrackingUtil
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.NEW_USER_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBHOME_WIDGET_IMPRESSION
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.old.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageSearchActivity
import com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment
import com.tokopedia.digital.home.util.QueryDigitalHomeBanner
import com.tokopedia.digital.home.util.QueryDigitalHomeCategory
import com.tokopedia.digital.home.util.QueryDigitalHomeRechargeFavoriteRecommendationList
import com.tokopedia.digital.home.util.QueryDigitalHomeSection
import com.tokopedia.digital.home.widget.RechargeSearchBarWidget
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DigitalHomePageFragment : BaseListFragment<DigitalHomePageItemModel, DigitalHomePageTypeFactory>(),
        OnItemBindListener,
        DigitalHomePageTransactionViewHolder.TransactionListener,
        RechargeSearchBarWidget.FocusChangeListener {

    @Inject
    lateinit var trackingUtil: DigitalHomeTrackingUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: DigitalHomePageViewModel
    private var searchBarTransitionRange = 0
    private var sliceOpenApp: Boolean = false

    private var binding by autoCleared<ViewRechargeHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewRechargeHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageViewModel::class.java)
        }

        arguments?.let {
            sliceOpenApp = it.getBoolean(RechargeHomepageFragment.RECHARGE_HOME_PAGE_EXTRA, false)
        }

        searchBarTransitionRange = context?.resources?.getDimensionPixelSize(TOOLBAR_TRANSITION_RANGE) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        
        binding.digitalHomepageToolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        binding.digitalHomepageSearchView.setFocusChangeListener(this)
        calculateToolbarView(0)

        getRecyclerView(view)?.run {
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    getRecyclerView(view)?.computeVerticalScrollOffset()?.let {
                        calculateToolbarView(it)
                    }
                }
            })
        }

        binding.digitalHomepageOrderList.setOnClickListener {
            onClickOrderList()
        }

        if(sliceOpenApp){
            trackingUtil.sliceOpenApp(userSession.userId)
            trackingUtil.onOpenPageFromSlice()
        }
    }

    private fun hideStatusBar() {

        binding.digitalHomepageContainer.fitsSystemWindows = false
        binding.digitalHomepageContainer.requestApplyInsets()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = binding.digitalHomepageContainer.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            binding.digitalHomepageContainer.systemUiVisibility = flags
            context?.run {
                activity?.window?.statusBarColor =
                        androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }

        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT..Build.VERSION_CODES.KITKAT_WATCH) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    private fun calculateToolbarView(offset: Int) {

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = OFFSET_ALPHA / searchBarTransitionRange * (offset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        val searchBarContainer = binding.digitalHomepageSearchView.findViewById<LinearLayout>(R.id.search_input_view_container)
        if (offsetAlpha >= OFFSET_ALPHA) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            binding.digitalHomepageToolbar.toOnScrolledMode()
            context?.run {

                binding.digitalHomepageOrderList.setColorFilter(
                        ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N200), PorterDuff.Mode.MULTIPLY
                )
                searchBarContainer.background =
                        MethodChecker.getDrawable(this, R.drawable.bg_digital_homepage_search_view_background_gray)
            }
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            binding.digitalHomepageToolbar.toInitialMode()
            binding.digitalHomepageOrderList.clearColorFilter()
            context?.run {
                searchBarContainer.background =
                        MethodChecker.getDrawable(this, R.drawable.bg_digital_homepage_search_view_background)
            }
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

        viewModel.digitalHomePageList.observe(viewLifecycleOwner, Observer {
            clearAllData()
            it?.run {
                DigitalHomePageCategoryDataMapper.mapCategoryData(this[DigitalHomePageViewModel.CATEGORY_SECTION_ORDER])?.let { categoryData ->
                    trackingUtil.eventCategoryImpression(categoryData)
                }
                val list = this.filter { item -> !item.isEmpty }
                renderList(list)
            }
        })

        viewModel.isAllError.observe(viewLifecycleOwner, Observer {
            it?.let { isAllError ->
                if (isAllError) NetworkErrorHelper.showEmptyState(context, view?.rootView) { loadInitialData() }
            }
        })
    }

    override fun loadData(page: Int) {

    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()

        val queryList = mapOf(
                QUERY_BANNER to QueryDigitalHomeBanner(),
                QUERY_CATEGORY to QueryDigitalHomeCategory(),
                QUERY_SECTIONS to QueryDigitalHomeSection(),
                QUERY_RECOMMENDATION to QueryDigitalHomeRechargeFavoriteRecommendationList()
        )
        viewModel.initialize(queryList)
        viewModel.getData(swipeToRefresh?.isRefreshing ?: false)
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

    override fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, position: Int, sectionType: String) {
        if (sectionType == SUBSCRIPTION_GUIDE_CLICK) {
            trackingUtil.eventClickSubscriptionGuide()
        } else {
            trackingUtil.eventSectionClick(element, position, sectionType)
        }
        RouteManager.route(activity, element.applink)
    }

    override fun onRecommendationClicked(element: RecommendationItemEntity, position: Int) {
        trackingUtil.eventRecommendationClick(element, position)
        RouteManager.route(activity, element.applink)
    }

    override fun onSectionItemImpression(elements: List<DigitalHomePageSectionModel.Item>, sectionType: String) {
        trackingUtil.eventSectionImpression(elements, sectionType)
    }

    override fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, position: Int) {
        trackingUtil.eventBannerImpression(banner, position)
    }

    override fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        // do nothing
    }

    override fun onRecommendationImpression(elements: List<RecommendationItemEntity>) {
        trackingUtil.eventRecommendationImpression(elements)
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
            binding.digitalHomepageSearchView.getSearchTextView()?.let { it.clearFocus() }
            trackingUtil.eventClickSearchBox()
            context?.let { context -> startActivity(DigitalHomePageSearchActivity.getCallingIntent(context)) }
        }
    }

    fun onBackPressed() {
        trackingUtil.eventClickBackButton()
    }

    companion object {
        val TOOLBAR_TRANSITION_RANGE = com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        private const val OFFSET_ALPHA = 255f

        val initialImpressionTrackingConst = mapOf(
                DYNAMIC_ICON_IMPRESSION to true,
                BANNER_IMPRESSION to true,
                BEHAVIORAL_CATEGORY_IMPRESSION to true,
                NEW_USER_IMPRESSION to true,
                SPOTLIGHT_IMPRESSION to true,
                SUBHOME_WIDGET_IMPRESSION to true
        )

        fun getInstance(sliceOpenApp: Boolean = false): DigitalHomePageFragment {
            val bundle = Bundle()
            val fragment = DigitalHomePageFragment()
            bundle.putBoolean(RECHARGE_HOME_PAGE_EXTRA, sliceOpenApp)
            fragment.arguments = bundle
            return fragment
        }
    }
}
