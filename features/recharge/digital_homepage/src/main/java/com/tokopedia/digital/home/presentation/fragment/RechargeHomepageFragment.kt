package com.tokopedia.digital.home.presentation.fragment

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.analytics.RechargeHomepageAnalytics
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.model.RechargeHomepageSectionModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageSearchActivity
import com.tokopedia.digital.home.presentation.adapter.RechargeHomeSectionDecoration
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageAdapter
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageAdapterTypeFactory
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageDynamicLegoBannerCallback
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageReminderWidgetCallback
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel
import com.tokopedia.digital.home.widget.RechargeSearchBarWidget
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.view_recharge_home.*
import javax.inject.Inject

class RechargeHomepageFragment : BaseDaggerFragment(),
        RechargeHomepageItemListener,
        RechargeHomepageAdapter.LoaderListener,
        RechargeSearchBarWidget.FocusChangeListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: RechargeHomepageViewModel

    @Inject
    lateinit var rechargeHomepageAnalytics: RechargeHomepageAnalytics

    lateinit var adapter: RechargeHomepageAdapter

    private var searchBarTransitionRange = 0

    private var platformId: Int = 0
    private var enablePersonalize: Boolean = false
    private var sliceOpenApp: Boolean = false

    lateinit var homeComponentsData: List<RechargeHomepageSections.Section>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_recharge_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(RechargeHomepageViewModel::class.java)

            adapter = RechargeHomepageAdapter(it, RechargeHomepageAdapterTypeFactory(this,
                    RechargeHomepageReminderWidgetCallback(this),
                    RechargeHomepageDynamicLegoBannerCallback(this)), this)
        }

        arguments?.let {
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
            enablePersonalize = it.getBoolean(EXTRA_ENABLE_PERSONALIZE, true)
            sliceOpenApp = it.getBoolean(RECHARGE_HOME_PAGE_EXTRA, false)
        }

        searchBarTransitionRange = TOOLBAR_TRANSITION_RANGE_DP.dpToPx(resources.displayMetrics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        digital_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        digital_homepage_search_view.setFocusChangeListener(this)
        calculateToolbarView(0)

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        while (recycler_view.itemDecorationCount > 0) recycler_view.removeItemDecorationAt(0)
        recycler_view.addItemDecoration(RechargeHomeSectionDecoration(
                SECTION_SPACING_DP.dpToPx(resources.displayMetrics)
        ))
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(recycler_view.computeVerticalScrollOffset())
            }
        })

        swipe_refresh_layout.setOnRefreshListener {
            swipe_refresh_layout.isRefreshing = true
            loadData()
        }

        digital_homepage_order_list.setOnClickListener {
            rechargeHomepageAnalytics.eventClickOrderList()
            RouteManager.route(activity, ApplinkConst.DIGITAL_ORDER)
        }

        // Recharge Branch Event
        rechargeHomepageAnalytics.eventHomepageLaunched(userSession.userId)

        if(sliceOpenApp){
            rechargeHomepageAnalytics.sliceOpenApp(userSession.userId)
            rechargeHomepageAnalytics.onOpenPageFromSlice()
        }

        loadData()
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
            context?.run {
                activity?.window?.statusBarColor =
                        androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
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

        val searchBarContainer = digital_homepage_search_view.findViewById<LinearLayout>(R.id.search_input_view_container)
        if (offsetAlpha >= 255) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            digital_homepage_toolbar.toOnScrolledMode()
            context?.run {
                digital_homepage_order_list.setColorFilter(
                        ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N200), PorterDuff.Mode.MULTIPLY
                )
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

    override fun initInjector() {
        getComponent(RechargeHomepageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.rechargeHomepageSectionSkeleton.observe(viewLifecycleOwner, Observer {
            if (it is Fail) adapter.showGetListError(it.throwable)
        })

        viewModel.rechargeHomepageSections.observe(viewLifecycleOwner, Observer {
            hideLoading()

            val mappedData = RechargeHomepageSectionMapper.mapHomepageSections(it)
            val homeComponentIDs: List<Int> = mappedData.filterIsInstance<HomeComponentVisitable>().mapNotNull { homeComponent ->
                homeComponent.visitableId()?.toInt()
            }
            homeComponentsData = it.filter { section -> section.id in homeComponentIDs }
            adapter.renderList(mappedData)
        })
    }

    override fun loadData() {
        adapter.showLoading()
        viewModel.getRechargeHomepageSectionSkeleton(
                viewModel.createRechargeHomepageSectionSkeletonParams(platformId, enablePersonalize),
                swipe_refresh_layout.isRefreshing
        )
    }

    override fun loadRechargeSectionData(sectionID: Int) {
        if (sectionID >= 0) {
            viewModel.getRechargeHomepageSections(
                    viewModel.createRechargeHomepageSectionsParams(platformId, listOf(sectionID), enablePersonalize)
            )
        }
    }

    override fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section) {
        RouteManager.route(context, section.applink)
    }

    override fun onRechargeLegoBannerItemClicked(sectionID: Int, itemID: Int, itemPosition: Int) {
        if (::homeComponentsData.isInitialized) {
            val bannerItem = homeComponentsData.find { it.id == sectionID }?.items?.find { it.id == itemID }
            bannerItem?.run {
                tracking.find {
                    it.action == RechargeHomepageAnalytics.ACTION_CLICK
                }?.run {
                    rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
                }

                RouteManager.route(context, applink)
            }
        }
    }

    override fun onRechargeBannerAllItemClicked(section: RechargeHomepageSections.Section) {
        rechargeHomepageAnalytics.eventClickAllBanners()
        RouteManager.route(context, section.applink)
    }

    override fun onRechargeReminderWidgetClicked(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            val reminderData = homeComponentsData.find { it.id == sectionID }?.items?.firstOrNull()
            reminderData?.run {
                tracking.find {
                    it.action == RechargeHomepageAnalytics.ACTION_CLICK
                }?.run {
                    rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
                }

                RouteManager.route(context, applink)
            }
        }
    }

    override fun onRechargeReminderWidgetClosed(sectionID: Int, toggleTracking: Boolean) {
        val index = adapter.data.indexOfFirst { it is HomeComponentVisitable && it.visitableId()?.toIntOrNull() == sectionID }
        if (index >= 0 && ::homeComponentsData.isInitialized) {
            // Trigger close reminder widget action
            val section = homeComponentsData.find { it.id == sectionID }
            if (toggleTracking && section != null && section.items.isNotEmpty()) {
                viewModel.triggerRechargeSectionAction(
                        viewModel.createRechargeHomepageSectionActionParams(sectionID, "ActionClose", section.objectId, section.items.first().objectId)
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
                            viewModel.createRechargeHomepageSectionActionParams(id, "ActionClose", objectId, items.first().objectId)
                    )
                }
            }
            onRechargeSectionEmpty(section.id)
        }
    }

    override fun onRechargeSectionItemImpression(element: RechargeHomepageSections.Section) {
        element.tracking.find { it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION }?.run {
            rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
        }
    }

    override fun onRechargeSectionItemClicked(element: RechargeHomepageSections.Item) {
        element.tracking.find { it.action == RechargeHomepageAnalytics.ACTION_CLICK }?.run {
            rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
        }

        RouteManager.route(context, element.applink)
    }

    override fun onRechargeBannerImpression(element: RechargeHomepageSections.Section) {
        element.tracking.find { it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION }?.run {
            rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
        }
    }

    override fun onRechargeReminderWidgetImpression(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.tracking?.find {
                it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION
            }?.run {
                rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeLegoBannerImpression(sectionID: Int) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id == sectionID }?.tracking?.find {
                it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION
            }?.run {
                rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
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

    override fun onFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            digital_homepage_search_view.getSearchTextView()?.let { it.clearFocus() }
            rechargeHomepageAnalytics.eventClickSearchBox(userSession.userId)
            context?.let { context -> startActivity(DigitalHomePageSearchActivity.getCallingIntent(context)) }
        }
    }

    fun onBackPressed() {
        rechargeHomepageAnalytics.eventClickBackButton()
    }

    private fun hideLoading() {
        swipe_refresh_layout.isEnabled = true
        swipe_refresh_layout.isRefreshing = false
        adapter.hideLoading()
    }

    companion object {
        const val EXTRA_PLATFORM_ID = "platform_id"
        const val EXTRA_ENABLE_PERSONALIZE = "personalize"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        const val TOOLBAR_TRANSITION_RANGE_DP = 8
        const val SECTION_SPACING_DP = 16

        fun newInstance(platformId: Int, enablePersonalize: Boolean = false, sliceOpenApp: Boolean = false): RechargeHomepageFragment {
            val fragment = RechargeHomepageFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PLATFORM_ID, platformId)
            bundle.putBoolean(EXTRA_ENABLE_PERSONALIZE, enablePersonalize)
            bundle.putBoolean(RECHARGE_HOME_PAGE_EXTRA, sliceOpenApp)
            fragment.arguments = bundle
            return fragment
        }
    }
}
