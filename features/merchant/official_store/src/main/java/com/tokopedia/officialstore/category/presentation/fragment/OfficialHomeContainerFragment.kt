package com.tokopedia.officialstore.category.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.officialstore.ApplinkConstant
import com.tokopedia.officialstore.FirebasePerformanceMonitoringConstant
import com.tokopedia.officialstore.OfficialStoreInstance
import com.tokopedia.officialstore.R
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
import com.tokopedia.officialstore.category.presentation.widget.OfficialCategoriesTab
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.presentation.listener.OSChooseAddressWidgetCallback
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_official_home.*
import java.util.*
import javax.inject.Inject

class OfficialHomeContainerFragment : BaseDaggerFragment(), HasComponent<OfficialStoreCategoryComponent>,
        AllNotificationListener,
        RecyclerViewScrollListener,
        OSContainerListener{

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeContainerFragment().apply { arguments = bundle }

        const val KEY_CATEGORY = "key_category"
        private const val EXP_TOP_NAV = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
        private const val VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD
        private const val VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP

    }

    @Inject
    lateinit var viewModel: OfficialStoreCategoryViewModel

    private var statusBar: View? = null
    private var mainToolbar: NavToolbar? = null
    private var toolbar: MainToolbar? = null
    private var tabLayout: OfficialCategoriesTab? = null
    private var loadingCategoryLayout: View? = null
    private var viewPager: ViewPager? = null
    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var badgeNumberCart: Int = 0
    private var keyCategory = "0"
    private var useNewInbox = false
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var chooseAddressData = OSChooseAddressData()

    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private lateinit var tracking: OfficialStoreTracking
    private lateinit var categoryPerformanceMonitoring: PerformanceMonitoring
    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null

    private lateinit var remoteConfig: RemoteConfig
    private val queryHashingKey = "android_do_query_hashing"

    private var chooseAddressWidgetInitialized: Boolean = false

    private val tabAdapter: OfficialHomeContainerAdapter by lazy {
        OfficialHomeContainerAdapter(context, childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        officialStorePerformanceMonitoringListener = context?.let { castContextToOfficialStorePerformanceMonitoring(it) }
        startOfficialStorePerformanceMonitoring()
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
        initInboxAbTest()
        init(view)
        observeOfficialCategoriesData()
        fetchOSCategory()
    }

    override fun onDestroy() {
        viewModel.removeObservers(this)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        conditionalViewModelRefresh()
    }

    override fun onStop() {
        super.onStop()
        chooseAddressWidgetInitialized = false
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
    }

    // from: GlobalNav, to show notification maintoolbar
    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int, cartCount: Int) {
        mainToolbar?.run {
            if (!useNewInbox) {
                setBadgeCounter(IconList.ID_NOTIFICATION, notificationCount)
            }
            setBadgeCounter(getInboxIcon(), inboxCount)
            setBadgeCounter(IconList.ID_CART, cartCount)
        }
        toolbar?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
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

    private fun fetchOSCategory() {
        viewModel.getOfficialStoreCategories(remoteConfig.getBoolean(queryHashingKey, false))
    }

    private fun observeOfficialCategoriesData() {
        viewModel.officialStoreCategoriesResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    removeLoading()
                    populateCategoriesData(it.data)
                }
                is Fail -> {
                    removeLoading()
                    NetworkErrorHelper.showEmptyState(context, official_home_motion) {
                        fetchOSCategory()
                    }
                }
            }
            categoryPerformanceMonitoring.stopTrace()
        })
    }

    private fun getSelectedCategory(officialStoreCategories: OfficialStoreCategories): Int {
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
        val categorySelected = getSelectedCategory(officialStoreCategories)
        tabLayout?.getTabAt(categorySelected)?.select()

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
                categoryReselected?.let {
                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categorySelected = tabAdapter.categoryList.getOrNull(tab?.position.toZeroIfNull())
                categorySelected?.let {
                    tracking.eventClickCategory(tab?.position.toZeroIfNull(), it)
                }
            }

        })
    }

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

    private fun initInboxAbTest() {
        useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ) == AbTestPlatform.VARIANT_NEW_INBOX && isNavRevamp()
    }

    private fun getInboxIcon(): Int {
        return if (useNewInbox) {
            IconList.ID_INBOX
        } else {
            IconList.ID_MESSAGE
        }
    }

    private fun init(view: View) {
        configStatusBar(view)
        configMainToolbar(view)
        tabLayout = view.findViewById(R.id.tablayout)
        loadingCategoryLayout = view.findViewById(R.id.view_category_tab_loading)
        viewPager = view.findViewById(R.id.viewpager)
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)
        chooseAddressWidget = view.findViewById(R.id.widget_choose_address)
        initializeChooseAddressWidget(isChooseAddressRollenceActive())
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
        view_content_loading?.gone()
        tabLayout?.visible()
    }

    private fun configMainToolbar(view: View) {
        if(isNavRevamp()){
            mainToolbar = view.findViewById(R.id.maintoolbar)
            maintoolbar?.run {
                viewLifecycleOwner.lifecycle.addObserver(this)
                setIcon(getToolbarIcons())
                setupSearchbar(
                        hints = listOf(HintData(placeholder = getString(R.string.os_query_search))),
                        applink = ApplinkConstant.OFFICIAL_SEARCHBAR
                )
                show()
            }
            toolbar?.hide()
            onNotificationChanged(badgeNumberNotification, badgeNumberInbox, badgeNumberCart) // notify badge after toolbar created
        } else {
            toolbar = view.findViewById(R.id.toolbar)
            toolbar?.searchApplink = ApplinkConstant.OFFICIAL_SEARCHBAR
            toolbar?.setQuerySearch(getString(R.string.os_query_search))
            toolbar?.show()
            mainToolbar?.hide()
        }
    }

    private fun getToolbarIcons(): IconBuilder {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                        .addIcon(getInboxIcon()) {}

        if (!useNewInbox) {
            icons.addIcon(IconList.ID_NOTIFICATION) {}
        }

        icons.apply {
            addIcon(IconList.ID_CART) {}
            addIcon(IconList.ID_NAV_GLOBAL) {}
        }

        return icons
    }

    private fun isNavRevamp(): Boolean {
        return try {
            getAbTestPlatform().getString(EXP_TOP_NAV, VARIANT_OLD) == VARIANT_REVAMP
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun castContextToOfficialStorePerformanceMonitoring(context: Context): OfficialStorePerformanceMonitoringListener? {
        return if (context is OfficialStorePerformanceMonitoringListener) {
            context
        } else null
    }

    private fun startOfficialStorePerformanceMonitoring(){
        officialStorePerformanceMonitoringListener?.startOfficialStorePerformanceMonitoring()
        officialStorePerformanceMonitoringListener = null
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
        return ChooseAddressUtils.isRollOutUser(requireContext())
    }

    override fun onChooseAddressUpdated() {
        val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        chooseAddressData.setLocalCacheModel(localCacheModel)
        chooseAddressWidgetInitialized = false
    }

    override fun onChooseAddressServerDown() {
        removeChooseAddressWidget()
    }

    private fun removeChooseAddressWidget() {
        chooseAddressWidget?.gone()
    }

    private fun initializeChooseAddressWidget(needToShowChooseAddress: Boolean) {
        if (!chooseAddressWidgetInitialized) {
            chooseAddressWidget?.bindChooseAddress(OSChooseAddressWidgetCallback(context, this, this))
            chooseAddressWidget?.run {
                visibility = if (needToShowChooseAddress) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            chooseAddressWidgetInitialized = true
        }
    }

    private fun conditionalViewModelRefresh() {
        chooseAddressAbTestCondition(
                ifChooseAddressActive = {
                    if (isAddressDataChanged() && chooseAddressWidgetInitialized) {
                        chooseAddressWidget?.updateWidget()
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