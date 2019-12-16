package com.tokopedia.officialstore.category.presentation.fragment

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
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.officialstore.*
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
import java.util.*
import javax.inject.Inject

class OfficialHomeContainerFragment : BaseDaggerFragment(), HasComponent<OfficialStoreCategoryComponent>,
        AllNotificationListener,
        RecyclerViewScrollListener {

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeContainerFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModel: OfficialStoreCategoryViewModel

    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var tabLayout: OfficialCategoriesTab? = null
    private var viewPager: ViewPager? = null
    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0

    private lateinit var tracking: OfficialStoreTracking
    private lateinit var categoryPerformanceMonitoring: PerformanceMonitoring

    private val tabAdapter: OfficialHomeContainerAdapter by lazy {
        OfficialHomeContainerAdapter(context, childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryPerformanceMonitoring = PerformanceMonitoring.start(FirebasePerformanceMonitoringConstant.CATEGORY)
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
                    populateCategoriesData(it.data)
                }
                is Fail -> {
                    if (BuildConfig.DEBUG) {
                        it.throwable.printStackTrace()
                    }

                    NetworkErrorHelper.showEmptyState(context, view) {
                        viewModel.getOfficialStoreCategories()
                    }
                }
            }
            categoryPerformanceMonitoring.stopTrace()
        })
    }

    private fun populateCategoriesData(officialStoreCategories: OfficialStoreCategories) {
        officialStoreCategories.categories.forEachIndexed { _, category ->
            tabAdapter.categoryList.add(category)
        }
        tabAdapter.notifyDataSetChanged()
        tabLayout?.setup(viewPager!!, convertToCategoriesTabItem(officialStoreCategories.categories))
        tabLayout?.getTabAt(0)?.select()

        val category = tabAdapter.categoryList[0]
        tracking.eventImpressionCategory(
                category.title,
                category.categoryId,
                0,
                category.icon
        )

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
        viewPager = view.findViewById(R.id.viewpager)
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

    private fun configMainToolbar(view: View) {
        mainToolbar = view.findViewById(R.id.maintoolbar)
        mainToolbar?.searchApplink = ApplinkConstant.OFFICIAL_SEARCHBAR
        mainToolbar?.setQuerySearch(getString(R.string.os_query_search))
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox) // notify badge after toolbar created
    }
}