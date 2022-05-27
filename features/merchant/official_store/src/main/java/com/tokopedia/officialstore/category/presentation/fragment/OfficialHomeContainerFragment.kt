package com.tokopedia.officialstore.category.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.officialstore.*
import com.tokopedia.officialstore.OSPerformanceConstant.KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CACHE
import com.tokopedia.officialstore.OSPerformanceConstant.KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CLOUD
import com.tokopedia.officialstore.OSPerformanceConstant.KEY_PERFORMANCE_PREPARING_OS_CONTAINER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.category.di.DaggerOfficialStoreCategoryComponent
import com.tokopedia.officialstore.category.di.OfficialStoreCategoryComponent
import com.tokopedia.officialstore.category.di.OfficialStoreCategoryModule
import com.tokopedia.officialstore.category.presentation.adapter.OfficialHomeContainerAdapter
import com.tokopedia.officialstore.category.presentation.data.OSChooseAddressData
import com.tokopedia.officialstore.category.presentation.listener.OSContainerListener
import com.tokopedia.officialstore.category.presentation.viewmodel.OfficialStoreCategoryViewModel
import com.tokopedia.officialstore.category.presentation.viewutil.OSChooseAddressWidgetView
import com.tokopedia.officialstore.category.presentation.widget.OfficialCategoriesTab
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.officialstore.databinding.FragmentOfficialHomeBinding
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import javax.inject.Inject

class OfficialHomeContainerFragment
    : BaseDaggerFragment(),
        HasComponent<OfficialStoreCategoryComponent>,
        AllNotificationListener,
        RecyclerViewScrollListener,
        OSContainerListener {
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeContainerFragment().apply { arguments = bundle }
        const val KEY_CATEGORY = "key_category"
        const val PARAM_ACTIVITY_OFFICIAL_STORE = "param_activity_official_store"
        const val PARAM_HOME = "home"
    }

    private var binding: FragmentOfficialHomeBinding? by viewBinding()
    private var currentOfficialStoreCategories: OfficialStoreCategories? = null
    private val queryHashingKey = "android_do_query_hashing"
    private val tabAdapter: OfficialHomeContainerAdapter by lazy {
        OfficialHomeContainerAdapter(context, childFragmentManager)
    }

    private var statusBar: View? = null
    private var mainToolbar: NavToolbar? = null
    private var tabLayout: OfficialCategoriesTab? = null
    private var loadingCategoryLayout: View? = null
    private var viewPager: ViewPager? = null
    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var badgeNumberCart: Int = 0
    private var keyCategory = "0"
    private var chooseAddressView: OSChooseAddressWidgetView? = null
    private var chooseAddressData = OSChooseAddressData()
    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null
    private var selectedCategory: Category? = null
    private var activityOfficialStore = ""

    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private lateinit var tracking: OfficialStoreTracking
    private lateinit var categoryPerformanceMonitoring: PerformanceMonitoring
    private lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModel: OfficialStoreCategoryViewModel

    fun selectFirstTab() {
        val tab = tabLayout?.getTabAt(0)
        tab?.let {
            it.select()
        }
    }

    fun selectTabByCategoryId(categoryId: String) {
        val position = tabLayout?.getPositionBasedOnCategoryId(categoryId) ?: -1
        if (position != -1) {
            val tab = tabLayout?.getTabAt(position)
            tab?.let {
                it.select()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        officialStorePerformanceMonitoringListener = context?.let { castContextToOfficialStorePerformanceMonitoring(it) }
        startOfficialStorePerformanceMonitoring(savedInstanceState)
        super.onCreate(savedInstanceState)
        categoryPerformanceMonitoring = PerformanceMonitoring.start(FirebasePerformanceMonitoringConstant.CATEGORY)
        arguments?.let {
            keyCategory = it.getString(KEY_CATEGORY, "0")
        }
        context?.let {
            tracking = OfficialStoreTracking(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteConfig = FirebaseRemoteConfigImpl(context)
        init(view)
        observeOfficialCategoriesData()
        fetchOSCategory()
        if (savedInstanceState == null) {
            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(KEY_PERFORMANCE_PREPARING_OS_CONTAINER)
        }
    }

    override fun onDestroy() {
        viewModel.removeObservers(this)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        conditionalViewModelRefresh()
    }

    override fun getComponent(): OfficialStoreCategoryComponent? {
        return activity?.run {
            DaggerOfficialStoreCategoryComponent
                    .builder()
                    .officialStoreCategoryModule(OfficialStoreCategoryModule())
                    .officialStoreComponent(OfficialStoreInstance.getComponent(application))
                    .build()
        }
    }

    // config collapse & expand tablayout
    override fun onContentScrolled(dy: Int) {
        if(dy == 0) return

        tabLayout?.adjustTabCollapseOnScrolled(dy)
        chooseAddressView?.adjustViewCollapseOnScrolled(
                dy = dy,
                whenWidgetGone = {binding?.osDivider?.gone()},
                whenWidgetShow = {binding?.osDivider?.show()})
    }

    // from: GlobalNav, to show notification maintoolbar
    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int, cartCount: Int) {
        mainToolbar?.run {
            setBadgeCounter(IconList.ID_NOTIFICATION, notificationCount)
            setBadgeCounter(getInboxIcon(), inboxCount)
            setBadgeCounter(IconList.ID_CART, cartCount)
        }
        badgeNumberNotification = notificationCount
        badgeNumberInbox = inboxCount
        badgeNumberCart = cartCount
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            conditionalViewModelRefresh()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
    }

    override fun onChooseAddressUpdated() {
        val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        chooseAddressData.setLocalCacheModel(localCacheModel)
        chooseAddressView?.updateChooseAddressInitializedState(false)
        getChooseAddressWidget()?.updateWidget()
        updateCurrentFragmentData()
    }

    override fun onChooseAddressServerDown() {
        removeChooseAddressWidget()
    }

    private fun removeChooseAddressWidget() {
        chooseAddressView?.gone()
    }

    private fun updateCurrentFragmentData() {
        var currentTabPos: Int = -1
        selectedCategory?.let {
            currentTabPos = tabLayout?.getPositionBasedOnCategoryId(it.categoryId) ?: -1
        }
        if (currentTabPos != -1) {
            val currentFragment = tabAdapter.getCurrentFragment(currentTabPos) as? OfficialHomeFragment
            currentFragment?.forceLoadData()
        }
    }

    private fun fetchOSCategory() {
        viewModel.getOfficialStoreCategories(remoteConfig.getBoolean(queryHashingKey, false),
                onCacheStartLoad = {
                    officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CACHE)
                },
                onCacheStopLoad = {
                    officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CACHE)
                },
                onCloudStartLoad = {
                    officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CLOUD)
                },
                onCloudStopLoad = {
                    officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.stopCustomMetric(KEY_PERFORMANCE_OS_CONTAINER_CATEGORY_CLOUD)
                })
    }

    private fun observeOfficialCategoriesData() {
        viewModel.officialStoreCategoriesResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.categories.isNotEmpty() && (currentOfficialStoreCategories?.categories != it.data.categories || currentOfficialStoreCategories == null)) {
                        this.currentOfficialStoreCategories = it.data
                        removeLoading()
                        populateCategoriesData(it.data)
                    } else if(currentOfficialStoreCategories?.categories == it.data.categories && !it.data.isCache){
                            tabAdapter.categoryList.forEachIndexed { index, category ->
                                tracking.eventImpressionCategory(
                                        category.title,
                                        category.categoryId,
                                        index,
                                        category.icon
                                )
                            }
                    }
                }
                is Fail -> {
                    val throwable = it.throwable
                    removeLoading()
                    context?.let { ctx ->
                        NetworkErrorHelper.showEmptyState(ctx, binding?.officialHomeMotion, ErrorHandler.getErrorMessage(ctx, throwable)) {
                            fetchOSCategory()
                        }
                    }
                }
            }
            categoryPerformanceMonitoring.stopTrace()
        })
    }

    private fun getSelectedCategoryId(officialStoreCategories: OfficialStoreCategories): Int {
        officialStoreCategories.categories.forEachIndexed { index, category ->
            if (keyCategory !== "0" && category.categoryId == keyCategory) {
                return index
            }
        }
        return 0
    }

    private fun populateCategoriesData(officialStoreCategories: OfficialStoreCategories) {
        tabAdapter.categoryList = mutableListOf()
        officialStoreCategories.categories.forEachIndexed { _, category ->
            tabAdapter.categoryList.add(category)
        }
        tabAdapter.notifyDataSetChanged()
        tabLayout?.setup(viewPager!!, convertToCategoriesTabItem(officialStoreCategories.categories))
        val categorySelected = getSelectedCategoryId(officialStoreCategories)
        tabLayout?.getTabAt(categorySelected)?.select()
        selectedCategory = tabAdapter.categoryList.getOrNull(tabLayout?.getTabAt(categorySelected)?.position.toZeroIfNull())

        if(!officialStoreCategories.isCache){
            tabAdapter.categoryList.forEachIndexed { index, category ->
            tracking.eventImpressionCategory(
                    category.title,
                    category.categoryId,
                    index,
                    category.icon
            )
            }
        }

        tabLayout?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val categoryReselected = tabAdapter.categoryList.getOrNull(tab?.position.toZeroIfNull())
                chooseAddressView?.forceExpandView(whenWidgetShow = {binding?.osDivider?.show()})
                categoryReselected?.let {
                    selectedCategory = categoryReselected
                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categorySelected = tabAdapter.categoryList.getOrNull(tab?.position.toZeroIfNull())
                chooseAddressView?.forceExpandView(whenWidgetShow = {binding?.osDivider?.show()})
                categorySelected?.let {
                    selectedCategory = categorySelected
                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

        })
    }

    private fun convertToCategoriesTabItem(data: List<Category>): List<OfficialCategoriesTab.CategoriesItemTab> {
        val tabItemDataList = ArrayList<OfficialCategoriesTab.CategoriesItemTab>()
        data.forEach {
            tabItemDataList.add(OfficialCategoriesTab.CategoriesItemTab(
                    categoryId = it.categoryId,
                    title = it.title,
                    iconUrl = it.icon,
                    inactiveIconUrl = it.imageInactiveURL))
        }
        return tabItemDataList
    }

    private fun getInboxIcon(): Int {
        return IconList.ID_MESSAGE
    }

    private fun init(view: View) {
        activityOfficialStore = arguments?.getString(PARAM_ACTIVITY_OFFICIAL_STORE, "")?: ""
        configStatusBar(view)
        configMainToolbar(view)
        tabLayout = view.findViewById(R.id.tablayout)
        loadingCategoryLayout = view.findViewById(R.id.view_category_tab_loading)
        viewPager = view.findViewById(R.id.viewpager)
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)
        chooseAddressView = view.findViewById(R.id.view_widget_choose_address)
        chooseAddressView?.let {
            it.initChooseAddressWidget(
                    needToShowChooseAddress = isChooseAddressRollenceActive(),
                    listener = this,
                    fragment = this,
                    widgetShown = {
                        it.show()
                        it.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                            override fun onGlobalLayout() {
                                it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                it.setMeasuredHeight()
                            }
                        })
                    },
                    widgetGone = {
                        it.gone()
                    })
        }
    }

    //status bar background compability
    private fun configStatusBar(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        activity?.let {
            statusBar?.layoutParams?.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        statusBar?.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            else -> View.GONE
        }
    }

    private fun removeLoading() {
        loadingCategoryLayout?.gone()
        binding?.viewContentLoading?.root?.gone()
        tabLayout?.visible()
    }

    private fun configMainToolbar(view: View) {
        mainToolbar = view.findViewById(R.id.maintoolbar)
        mainToolbar?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(getToolbarIcons())
            setupSearchbar(
                    hints = listOf(HintData(placeholder = getString(R.string.os_query_search))),
                    applink = ApplinkConstant.OFFICIAL_SEARCHBAR
            )
            show()
        }
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox, badgeNumberCart) // notify badge after toolbar created
    }

    private fun getToolbarIcons(): IconBuilder {
        val pageSource = if (activityOfficialStore == PARAM_HOME) ApplinkConsInternalNavigation.SOURCE_HOME else ""
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = pageSource))
                .addIcon(getInboxIcon()) {}
        if(activityOfficialStore != PARAM_HOME)
        {
            mainToolbar?.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            statusBar?.visibility = View.GONE
        }
        icons.addIcon(IconList.ID_NOTIFICATION) {}
        icons.apply {
            addIcon(IconList.ID_CART) {}
            addIcon(IconList.ID_NAV_GLOBAL) {}
        }

        return icons
    }

    private fun castContextToOfficialStorePerformanceMonitoring(context: Context): OfficialStorePerformanceMonitoringListener? {
        return if (context is OfficialStorePerformanceMonitoringListener) {
            context
        } else null
    }

    private fun startOfficialStorePerformanceMonitoring(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            officialStorePerformanceMonitoringListener?.startOfficialStorePerformanceMonitoring()
            officialStorePerformanceMonitoringListener?.getOfficialStorePageLoadTimePerformanceInterface()?.startCustomMetric(KEY_PERFORMANCE_PREPARING_OS_CONTAINER)
        }
    }

    private fun chooseAddressAbTestCondition(
            ifChooseAddressActive: () -> Unit = {},
            ifChooseAddressNotActive: () -> Unit = {}) {
        val isActive = isChooseAddressRollenceActive()
        if (isActive) {
            ifChooseAddressActive.invoke()
        } else {
            ifChooseAddressNotActive.invoke()
        }
    }

    private fun isChooseAddressRollenceActive(): Boolean {
        return true
    }

    private fun getChooseAddressWidget(): ChooseAddressWidget? {
       return chooseAddressView?.getChooseAddressWidget()
    }


    private fun conditionalViewModelRefresh() {
        chooseAddressAbTestCondition(
                ifChooseAddressActive = {
                    if (isAddressDataChanged()) {
                        getChooseAddressWidget()?.updateWidget()
                        fetchOSCategory()
                    }
                },
                ifChooseAddressNotActive = {
                }
        )
    }

    private fun isAddressDataChanged(): Boolean {
        var isAddressChanged = false
        chooseAddressData.toLocalCacheModel().let {
            isAddressChanged = ChooseAddressUtils.isLocalizingAddressHasUpdated(requireContext(), it)
        }

        if (isAddressChanged) {
            val localChooseAddressData = ChooseAddressUtils.getLocalizingAddressData(requireContext())
            chooseAddressData = OSChooseAddressData(isActive = true)
                    .setLocalCacheModel(localChooseAddressData)
        }
        return isAddressChanged
    }

}