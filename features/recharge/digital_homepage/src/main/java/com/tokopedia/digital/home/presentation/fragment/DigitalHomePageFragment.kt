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
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
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
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.Util.DigitalHomePageCategoryDataMapper
import com.tokopedia.digital.home.presentation.Util.DigitalHomeTrackingUtil
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageSearchActivity
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeHomeSectionDecoration
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageTransactionViewHolder
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageDynamicLegoBannerCallback
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageReminderWidgetCallback
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.layout_digital_home.*
import javax.inject.Inject

class DigitalHomePageFragment : BaseListFragment<Visitable<*>, DigitalHomePageTypeFactory>(),
        OnItemBindListener,
        DigitalHomePageTransactionViewHolder.TransactionListener,
        SearchInputView.FocusChangeListener {

    @Inject
    lateinit var trackingUtil: DigitalHomeTrackingUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DigitalHomePageViewModel

    private lateinit var sections: List<RechargeHomepageSections.Section>

    private var searchBarTransitionRange = 0

    private var platformId: Int = 0
    private var enablePersonalize: Boolean = false

    lateinit var sectionSkeleton: List<RechargeHomepageSectionSkeleton.Item>
    lateinit var homeComponentsData: List<RechargeHomepageSections.Section>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_digital_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageViewModel::class.java)
        }

        arguments?.let {
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
            enablePersonalize = it.getBoolean(EXTRA_ENABLE_PERSONALIZE, false)
        }

        searchBarTransitionRange = TOOLBAR_TRANSITION_RANGE_DP.dpToPx(resources.displayMetrics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        digital_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        digital_homepage_search_view.setFocusChangeListener(this)
        calculateToolbarView(0)

        while (recycler_view.itemDecorationCount > 0) recycler_view.removeItemDecorationAt(0)
        recycler_view.addItemDecoration(RechargeHomeSectionDecoration(
                SECTION_SPACING_DP.dpToPx(resources.displayMetrics)
        ))
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(getRecyclerView(view).computeVerticalScrollOffset())
            }
        })

        digital_homepage_order_list.setOnClickListener {
            onClickOrderList()
        }
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
            digital_homepage_order_list.setColorFilter(com.tokopedia.unifyprinciples.R.color.Neutral_N200)
            context?.run {
                searchBarContainer.background =
                        MethodChecker.getDrawable(this, R.drawable.bg_digital_homepage_search_view_background_gray)
            }
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            digital_homepage_toolbar.toInitialMode()
            digital_homepage_order_list.clearColorFilter()
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

        viewModel.digitalHomePageList.observe(this, Observer {
            clearAllData()
            it?.run {
                DigitalHomePageCategoryDataMapper.mapCategoryData(this[DigitalHomePageViewModel.CATEGORY_SECTION_ORDER])?.let { categoryData ->
                    trackingUtil.eventCategoryImpression(categoryData)
                }
                val list = this.filter { item -> !item.isLoaded || item.isSuccess }
                renderList(list)
            }
        })

        viewModel.isAllError.observe(this, Observer {
            it?.let { isAllError ->
                if (isAllError) NetworkErrorHelper.showEmptyState(context, view?.rootView) { loadInitialData() }
            }
        })

        viewModel.rechargeHomepageSectionSkeleton.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    sectionSkeleton = it.data
                    val mappedData = RechargeHomepageSectionMapper.mapHomepageSectionsFromSkeleton(it.data).filterNotNull()
                    renderList(mappedData)
                }
                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.rechargeHomepageSections.observe(this, Observer {
            when (it) {
                is Success -> {
                    with(it.data) {
                        // If there is an error in the section request, remove such section
                        if (error.isEmpty() && sections.isNotEmpty()) {
                            homeComponentsData = sections.filter { section ->
                                DigitalHomePageViewModel.SECTION_HOME_COMPONENTS.contains(section.template)
                            }
                            // Update sections (remove / insert / insert multiple)
                            for (id in requestIDs) {
                                recycler_view.post {
                                    val sectionTemplate = sectionSkeleton.find { item -> item.id == id }?.template
                                            ?: ""
                                    val sectionIndex = RechargeHomepageSectionMapper.getSectionIndex(adapter.data, id)
                                    if (sectionTemplate.isNotEmpty() && sectionIndex >= 0) {
                                        val updatedSectionItems = RechargeHomepageSectionMapper.getUpdatedSectionsOfType(sections, sectionTemplate)
                                        adapter.apply {
                                            when (updatedSectionItems.size) {
                                                0 -> {
                                                    data.removeAt(sectionIndex)
                                                    notifyItemRemoved(sectionIndex)
                                                }
                                                1 -> {
                                                    data[sectionIndex] = updatedSectionItems.first()
                                                    notifyItemChanged(sectionIndex)
                                                }
                                                else -> {
//                                                    data[sectionIndex] = updatedSectionItems.first()
//                                                    notifyItemChanged(sectionIndex)
//                                                    data.addAll(sectionIndex + 1, updatedSectionItems.subList(1, updatedSectionItems.size))
//                                                    notifyItemRangeInserted(sectionIndex + 1, updatedSectionItems.size - 1)
                                                    data.removeAt(sectionIndex)
                                                    notifyItemRemoved(sectionIndex)
                                                    data.addAll(sectionIndex, updatedSectionItems)
                                                    notifyItemRangeInserted(sectionIndex, updatedSectionItems.size)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (sectionId in requestIDs) {
                                onRechargeSectionEmpty(sectionId)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun loadData(page: Int) {
        // Load dynamic sub-homepage if platformId is provided; else load old sub-homepage
        if (isDynamicPage()) {
            viewModel.getRechargeHomepageSectionSkeleton(
                    viewModel.createRechargeHomepageSectionSkeletonParams(platformId, enablePersonalize),
                    swipeToRefresh?.isRefreshing ?: false
            )
        } else {
            val queryList = mapOf(
                    DigitalHomePageUseCase.QUERY_BANNER to GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_banner),
                    DigitalHomePageUseCase.QUERY_CATEGORY to GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_category),
                    DigitalHomePageUseCase.QUERY_SECTIONS to GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_section),
                    DigitalHomePageUseCase.QUERY_RECOMMENDATION to GraphqlHelper.loadRawString(resources, com.tokopedia.common_digital.R.raw.digital_recommendation_list)
            )
            viewModel.initialize(queryList)
            viewModel.getData(swipeToRefresh?.isRefreshing ?: false)
        }
    }

    override fun loadRechargeSectionData(sectionID: Int) {
        if (sectionID >= 0) {
            viewModel.getRechargeHomepageSections(
                    viewModel.createRechargeHomepageSectionsParams(platformId, listOf(sectionID), enablePersonalize)
            )
        }
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
        // Do nothing
    }

    override fun onRecommendationImpression(elements: List<RecommendationItemEntity>) {
        trackingUtil.eventRecommendationImpression(elements)
    }

    override fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section) {
        RouteManager.route(context, section.applink)
    }

    override fun onRechargeLegoBannerItemClicked(sectionID: Int, itemID: Int, itemPosition: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.items?.find { it.id == itemID }?.tracking?.find {
                it.action == DigitalHomeTrackingUtil.ACTION_CLICK
            }?.run {
                trackingUtil.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeBannerAllItemClicked(section: RechargeHomepageSections.Section) {
        trackingUtil.eventClickAllBanners()
        RouteManager.route(context, section.applink)
    }

    override fun onRechargeReminderWidgetClicked(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.tracking?.find {
                it.action == DigitalHomeTrackingUtil.ACTION_CLICK
            }?.run {
                trackingUtil.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeReminderWidgetClosed(sectionID: Int) {
        val index = adapter.data.indexOfFirst { it is HomeComponentVisitable && it.visitableId()?.toIntOrNull() == sectionID }
        if (index >= 0) {
            // Trigger close reminder widget action
            val section = homeComponentsData.find { it.id == sectionID }
            if (section != null && section.items.isNotEmpty()) {
                viewModel.triggerRechargeSectionAction(
                        viewModel.createRechargeHomepageSectionAction(sectionID, "ActionClose", section.objectId, section.items.first().objectId)
                )
            }
            onRechargeSectionEmpty(sectionID)
        }
    }

    override fun onRechargeProductBannerClosed(section: RechargeHomepageSections.Section) {
        val index = adapter.data.indexOfFirst { it is RechargeHomepageSectionModel && it.visitableId() == section.id }
        if (index >= 0) {
            // Trigger close product banner action
            if (section.items.isNotEmpty()) {
                with(section) {
                    viewModel.triggerRechargeSectionAction(
                            viewModel.createRechargeHomepageSectionAction(id, "ActionClose", objectId, items.first().objectId)
                    )
                }
            }
            onRechargeSectionEmpty(section.id)
        }
    }

    override fun onRechargeSectionItemImpression(element: RechargeHomepageSections.Section) {
        element.tracking.find { it.action == DigitalHomeTrackingUtil.ACTION_IMPRESSION }?.run {
            trackingUtil.rechargeEnhanceEcommerceEvent(data)
        }
    }

    override fun onRechargeSectionItemClicked(element: RechargeHomepageSections.Item) {
        element.tracking.find { it.action == DigitalHomeTrackingUtil.ACTION_CLICK }?.run {
            trackingUtil.rechargeEnhanceEcommerceEvent(data)
        }

        RouteManager.route(context, element.applink)
    }

    override fun onRechargeBannerImpression(element: RechargeHomepageSections.Section) {
        element.tracking.find { it.action == DigitalHomeTrackingUtil.ACTION_IMPRESSION }?.run {
            trackingUtil.rechargeEnhanceEcommerceEvent(data)
        }
    }

    override fun onRechargeReminderWidgetImpression(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.tracking?.find {
                it.action == DigitalHomeTrackingUtil.ACTION_IMPRESSION
            }?.run {
                trackingUtil.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeLegoBannerImpression(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.tracking?.find {
                it.action == DigitalHomeTrackingUtil.ACTION_IMPRESSION
            }?.run {
                trackingUtil.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeSectionEmpty(sectionID: Int) {
        val index = adapter.data.indexOfFirst {
            (it is RechargeHomepageSectionModel && it.visitableId() == sectionID) ||
            (it is HomeComponentVisitable && it.visitableId()?.toIntOrNull() == sectionID)
        }
        if (index >= 0) {
            recycler_view.post {
                adapter.apply {
                    data.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
    }

    override fun getAdapterTypeFactory(): DigitalHomePageTypeFactory {
        return DigitalHomePageTypeFactory(this, RechargeHomepageReminderWidgetCallback(this),
                RechargeHomepageDynamicLegoBannerCallback(this), this)
    }

    override fun onItemClicked(t: Visitable<*>) {
        // Do nothing
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
            context?.let { context -> startActivity(DigitalHomePageSearchActivity.getCallingIntent(context)) }
        }
    }

    fun onBackPressed() {
        trackingUtil.eventClickBackButton()
    }

    private fun isDynamicPage(): Boolean {
        return platformId > 0
    }

    companion object {
        const val EXTRA_PLATFORM_ID = "platform_id"
        const val EXTRA_ENABLE_PERSONALIZE = "personalize"

        const val TOOLBAR_TRANSITION_RANGE_DP = 8
        const val SECTION_SPACING_DP = 16

        fun newInstance(platformId: Int, enablePersonalize: Boolean = false): DigitalHomePageFragment {
            val fragment = DigitalHomePageFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PLATFORM_ID, platformId)
            bundle.putBoolean(EXTRA_ENABLE_PERSONALIZE, enablePersonalize)
            fragment.arguments = bundle
            return fragment
        }
    }
}
