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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.analytics.RechargeHomepageAnalytics
import com.tokopedia.digital.home.databinding.ViewRechargeHomeBinding
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.model.RechargeHomepageSectionModel
import com.tokopedia.digital.home.model.RechargeHomepageSectionSkeleton
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeTickerHomepageModel
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
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
    var tickerList: RechargeTickerHomepageModel = RechargeTickerHomepageModel()

    private var binding by autoCleared<ViewRechargeHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewRechargeHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
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
        
        binding.digitalHomepageToolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        initSearchView()

        calculateToolbarView(0)

        
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        while (binding.recyclerView.itemDecorationCount > 0) binding.recyclerView.removeItemDecorationAt(0)
        binding.recyclerView.addItemDecoration(RechargeHomeSectionDecoration(
                SECTION_SPACING_DP.dpToPx(resources.displayMetrics)
        ))
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(binding.recyclerView.computeVerticalScrollOffset())
            }
        })

        
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            loadData()
        }

        // Recharge Branch Event
        rechargeHomepageAnalytics.eventHomepageLaunched(userSession.userId)

        if (sliceOpenApp) {
            rechargeHomepageAnalytics.sliceOpenApp(userSession.userId)
            rechargeHomepageAnalytics.onOpenPageFromSlice()
        }

        loadData()
    }

    private fun initSearchView() {
        
        binding.digitalHomepageSearchView.setFocusChangeListener(this)
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }
    }

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

    override fun initInjector() {
        getComponent(RechargeHomepageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.rechargeHomepageSectionSkeleton.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderSearchBarView(it.data)
                is Fail -> {
                    hideLoading()
                    adapter.showGetListError(it.throwable)
                    binding.digitalHomepageToolbar.hide()
                }
            }
        })

        viewModel.rechargeHomepageSections.observe(viewLifecycleOwner, Observer {
            hideLoading()
            renderList(it)
        })

        viewModel.rechargeTickerHomepageModel.observe(viewLifecycleOwner, Observer { tickerListResult ->
            when (tickerListResult) {
                is Success -> {
                    viewModel.rechargeHomepageSections.value?.let {
                        tickerList = tickerListResult.data
                        renderList(it)
                    }
                }
                is Fail -> tickerList =  RechargeTickerHomepageModel()
            }
        })
    }

    override fun loadData() {
        adapter.showLoading()
        binding.digitalHomepageToolbar.hide()
        viewModel.getRechargeHomepageSectionSkeleton(
                viewModel.createRechargeHomepageSectionSkeletonParams(platformId, enablePersonalize)
        )

        viewModel.getTickerHomepageSection(
                viewModel.createRechargeHomepageTickerParams(listOf(), platformId)
        )
    }

    override fun loadRechargeSectionData(sectionID: String) {
        if (sectionID.isNotEmpty()) {
            viewModel.getRechargeHomepageSections(
                    viewModel.createRechargeHomepageSectionsParams(platformId, listOf(sectionID.toIntOrZero()), enablePersonalize)
            )
        }
    }

    override fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section) {
        RouteManager.route(context, section.applink)
    }

    override fun onRechargeLegoBannerItemClicked(sectionID: String, itemID: String, itemPosition: Int) {
        if (::homeComponentsData.isInitialized) {
            val bannerItem = homeComponentsData.find {
                it.id.equals(sectionID)
            }?.items?.find { it.id.equals(itemID) }
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

    override fun onRechargeReminderWidgetClicked(sectionID: String) {
        if (::homeComponentsData.isInitialized) {
            val reminderData = homeComponentsData.find {
                it.id.equals(sectionID)
            }?.items?.firstOrNull()
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

    override fun onRechargeReminderWidgetClosed(sectionID: String, toggleTracking: Boolean) {
        val index = adapter.data.indexOfFirst { it is HomeComponentVisitable && it.visitableId().equals(sectionID) }
        if (index >= 0 && ::homeComponentsData.isInitialized) {
            // Trigger close reminder widget action
            val section = homeComponentsData.find {
                it.id.equals(sectionID)
            }
            if (toggleTracking && section != null && section.items.isNotEmpty()) {
                viewModel.triggerRechargeSectionAction(
                        viewModel.createRechargeHomepageSectionActionParams(sectionID.toIntOrZero(), "ActionClose", section.objectId, section.items.first().objectId)
                )
            }
            onRechargeSectionEmpty(sectionID)
        }
    }

    override fun onRechargeProductBannerClosed(section: RechargeHomepageSections.Section) {
        val index = adapter.data.indexOfFirst { it is RechargeHomepageSectionModel && it.visitableId().equals(section.id) }
        if (index >= 0) {
            // Trigger close product banner action
            if (section.items.isNotEmpty()) {
                with(section) {
                    viewModel.triggerRechargeSectionAction(
                            viewModel.createRechargeHomepageSectionActionParams(id.toIntOrZero(), "ActionClose", objectId, items.first().objectId)
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

    override fun onRechargeReminderWidgetImpression(sectionID: String) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id.equals(sectionID) }?.tracking?.find {
                it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION
            }?.run {
                rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeLegoBannerImpression(sectionID: String) {
        if (::homeComponentsData.isInitialized) {
            homeComponentsData.find { it.id.equals(sectionID) }?.tracking?.find {
                it.action == RechargeHomepageAnalytics.ACTION_IMPRESSION
            }?.run {
                rechargeHomepageAnalytics.rechargeEnhanceEcommerceEvent(data)
            }
        }
    }

    override fun onRechargeSectionEmpty(sectionID: String) {
        val index = adapter.data.indexOfFirst {
            (it is RechargeHomepageSectionModel && it.visitableId().equals(sectionID)) ||
                    (it is HomeComponentVisitable && it.visitableId().equals(sectionID))
        }
        binding.recyclerView?.post {
            adapter.apply {
                if (index >= 0 && index < adapter.data.size) {
                    data.removeAt(index)
                    notifyItemRemoved(index)
                }
            }
        }
    }

    override fun onFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            binding.digitalHomepageSearchView.getSearchTextView()?.let { it.clearFocus() }
            rechargeHomepageAnalytics.eventClickSearchBox(userSession.userId)

            redirectToSearchByDynamicIconsFragment()
        }
    }

    private fun redirectToSearchByDynamicIconsFragment() {
        val sectionIds = viewModel.getDynamicIconsSectionIds()
        if (sectionIds.isNotEmpty()) {
            startActivity(DigitalHomePageSearchActivity.getCallingIntent(
                    requireContext(), platformId, enablePersonalize,
                    sectionIds, viewModel.getSearchBarPlaceholder()))
        }
    }

    private fun renderList(sections: List<RechargeHomepageSections.Section>){
        val mappedData = RechargeHomepageSectionMapper.mapHomepageSections(sections, tickerList)
        val homeComponentIDs: List<Int> = mappedData.filterIsInstance<HomeComponentVisitable>().mapNotNull { homeComponent ->
            homeComponent.visitableId()?.toInt()
        }
        homeComponentsData = sections.filter { section -> section.id.toIntOrZero() in homeComponentIDs }
        adapter.renderList(mappedData)
    }

    fun onBackPressed() {
        rechargeHomepageAnalytics.eventClickBackButton()
    }

    private fun hideLoading() {
        binding.swipeRefreshLayout.isEnabled = true
        binding.swipeRefreshLayout.isRefreshing = false
        adapter.hideLoading()
    }

    private fun renderSearchBarView(rechargeHomepageSectionSkeleton: RechargeHomepageSectionSkeleton) {
        binding.digitalHomepageToolbar.show()
        binding.digitalHomepageSearchView.setSearchHint(rechargeHomepageSectionSkeleton.searchBarPlaceholder)
        binding.digitalHomepageOrderList.setOnClickListener {
            rechargeHomepageAnalytics.eventClickOrderList()
            RouteManager.route(activity, rechargeHomepageSectionSkeleton.searchBarAppLink)
        }
    }

    companion object {
        const val EXTRA_PLATFORM_ID = "platform_id"
        const val EXTRA_ENABLE_PERSONALIZE = "personalize"
        const val RECHARGE_HOME_PAGE_EXTRA = "RECHARGE_HOME_PAGE_EXTRA"

        const val TOOLBAR_TRANSITION_RANGE_DP = 8
        const val SECTION_SPACING_DP = 16

        private const val OFFSET_ALPHA = 255f

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
