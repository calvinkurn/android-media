package com.tokopedia.officialstore.category.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
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
import com.tokopedia.officialstore.category.presentation.viewmodel.OfficialStoreCategoryViewModel
import com.tokopedia.officialstore.category.presentation.widget.OfficialCategoriesTab
import com.tokopedia.officialstore.common.listener.RecyclerViewScrollListener
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_official_home.*
import java.util.*
import javax.inject.Inject

class OfficialHomeContainerFragment : BaseDaggerFragment(), HasComponent<OfficialStoreCategoryComponent>,
        AllNotificationListener,
        RecyclerViewScrollListener {

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeContainerFragment().apply { arguments = bundle }
        const val KEY_CATEGORY = "key_category"
    }

    @Inject
    lateinit var viewModel: OfficialStoreCategoryViewModel

    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var tabLayout: OfficialCategoriesTab? = null
    private var loadingCategoryLayout: View? = null
    private var viewPager: ViewPager? = null
    private var appbarCategory: AppBarLayout? = null
    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var keyCategory = "0"

    private lateinit var tracking: OfficialStoreTracking
    private lateinit var categoryPerformanceMonitoring: PerformanceMonitoring
    private var officialStorePerformanceMonitoringListener: OfficialStorePerformanceMonitoringListener? = null

    private val tabAdapter: OfficialHomeContainerAdapter by lazy {
        OfficialHomeContainerAdapter(context, childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        officialStorePerformanceMonitoringListener = context?.let { castContextToHomePerformanceMonitoring(it) }
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
        return inflater.inflate(R.layout.fragment_official_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        observeOfficialCategoriesData()
        viewModel.getOfficialStoreCategories()
    }

    override fun onDestroy() {
        viewModel.officialStoreCategoriesResult.removeObservers(this)
        super.onDestroy()
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
        tabLayout?.adjustTabCollapseOnScrolled(dy)
    }

    // from: GlobalNav, to show notification maintoolbar
    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        mainToolbar?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
        }
        badgeNumberNotification = notificationCount
        badgeNumberInbox = inboxCount
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun observeOfficialCategoriesData() {
        viewModel.officialStoreCategoriesResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    removeLoading()
                    populateCategoriesData(it.data)
                }
                is Fail -> {
                    removeLoading()
                    NetworkErrorHelper.showEmptyState(context, coordinator_layout_fragment_os) {
                        viewModel.getOfficialStoreCategories()
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
        officialStoreCategories.categories.forEachIndexed { _, category ->
            tabAdapter.categoryList.add(category)
        }
        tabAdapter.notifyDataSetChanged()
        tabLayout?.setup(viewPager!!, convertToCategoriesTabItem(officialStoreCategories.categories), appbarCategory!!)
        val categorySelected = getSelectedCategory(officialStoreCategories)
        tabLayout?.getTabAt(categorySelected)?.select()

        tabAdapter.categoryList.forEachIndexed { index, category ->
            tracking.eventImpressionCategory(
                    category.title,
                    category.categoryId,
                    index,
                    category.icon
            )
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

    private fun convertToCategoriesTabItem(data: List<Category>): List<OfficialCategoriesTab.CategoriesItemTab> {
        val tabItemDataList = ArrayList<OfficialCategoriesTab.CategoriesItemTab>()
        data.forEach {
            tabItemDataList.add(OfficialCategoriesTab.CategoriesItemTab(it.title, it.icon, it.imageInactiveURL))
        }
        return tabItemDataList
    }

    private fun init(view: View) {
        configStatusBar(view)
        configMainToolbar(view)
        tabLayout = view.findViewById(R.id.tablayout)
        loadingCategoryLayout = view.findViewById(R.id.view_category_tab_loading)
        viewPager = view.findViewById(R.id.viewpager)
        appbarCategory = view.findViewById(R.id.appbarLayout)
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    //status bar background compability
    private fun configStatusBar(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        activity?.let {
            statusBar?.layoutParams?.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        statusBar?.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun removeLoading() {
        loadingCategoryLayout?.visibility = View.GONE
        tabLayout?.visibility = View.VISIBLE
    }

    private fun configMainToolbar(view: View) {
        mainToolbar = view.findViewById(R.id.maintoolbar)
        mainToolbar?.searchApplink = ApplinkConstant.OFFICIAL_SEARCHBAR
        mainToolbar?.setQuerySearch(getString(R.string.os_query_search))
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox) // notify badge after toolbar created
    }

    private fun castContextToHomePerformanceMonitoring(context: Context): OfficialStorePerformanceMonitoringListener? {
        return if (context is OfficialStorePerformanceMonitoringListener) {
            context
        } else null
    }

    private fun startOfficialStorePerformanceMonitoring(){
        officialStorePerformanceMonitoringListener?.startOfficialStorePerformanceMonitoring()
        officialStorePerformanceMonitoringListener = null
    }
}