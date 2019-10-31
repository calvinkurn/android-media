package com.tokopedia.discovery.catalogrevamp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.airbnb.deeplinkdispatch.DeepLink
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.core.share.DefaultShare
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalog.activity.CatalogDetailActivity
import com.tokopedia.discovery.catalogrevamp.analytics.CatalogDetailPageAnalytics
import com.tokopedia.discovery.catalogrevamp.ui.customview.SearchNavigationView
import com.tokopedia.discovery.catalogrevamp.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.discovery.catalogrevamp.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.android.synthetic.main.activity_catalog_detail_page.*

class CatalogDetailPageActivity : BaseActivity(),
        CatalogDetailPageFragment.Listener,
        CategoryNavigationListener,
        BottomSheetListener,
        SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener{
    private var catalogId: String = ""
    private var shareData: LinkerData? = null
    private var searchNavContainer: SearchNavigationView? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()
    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private lateinit var catalogDetailFragment : Fragment
    private lateinit var catalogDetailListingFragment : Fragment
    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var catalogName: String =""
    private var filters: ArrayList<Filter> = ArrayList()

    object DeeplinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.DISCOVERY_CATALOG)
        fun defaultIntent(context: Context, bundle: Bundle): Intent {
            val intent = createIntent(context, bundle.getString(EXTRA_CATALOG_ID))
            return intent
                    .putExtras(bundle)
        }
    }

    companion object {
        private const val STATE_GRID = 1
        private const val STATE_LIST = 2
        private const val STATE_BIG = 3
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
        private const val ORDER_BY = "ob"
        @JvmStatic
        fun createIntent(context: Context, catalogId: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            return intent
        }
        @JvmStatic
        fun isCatalogRevampEnabled(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_CATALOG_REVAMP, true)
        }
    }

    override fun getScreenName(): String? {
        return AppScreen.SCREEN_CATALOG
    }

    private fun getNewCatalogDetailFragment(): Fragment {
        val catalogId: String = intent.getStringExtra(EXTRA_CATALOG_ID)
        val fragment = CatalogDetailPageFragment.newInstance(catalogId)
        fragment.setListener(this)
        return fragment
    }

    private fun getNewCatalogDetailListingFragment(catalogName: String, departmentId: String): Fragment {
        val departmentName: String? = intent.getStringExtra(EXTRA_CATEGORY_DEPARTMENT_NAME)
        val fragment : BaseCategorySectionFragment = CatalogDetailProductListingFragment.newInstance(catalogId, catalogName, departmentId, departmentName)
        fragment.setSortListener(this)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_detail_page)
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.search_nav_container)
        catalogId = intent.getStringExtra(EXTRA_CATALOG_ID)
        sendTo()
        prepareView()
    }

    private fun sendTo() {
        if(!isCatalogRevampEnabled(this)){
            startActivity(CatalogDetailActivity.createIntent(this, catalogId))
            finish()
        }
    }

    private fun prepareView() {
        setupToolbar()
        setFragment()
        initSwitchButton()
        initBottomSheetListener()

        bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                FilterEventTracking.Event.CLICK_CATALOG_DETAIL,
                FilterEventTracking.Category.FILTER_CATALOG_DETAIL,
                "",
                FilterEventTracking.Category.PREFIX_CATALOG_PAGE))

    }

    private fun initBottomSheetListener() {
        bottomSheetFilterView?.setCallback(object : BottomSheetFilterView.Callback {
            override fun onApplyFilter(filterParameter: Map<String, String>) {
                applyFilter(filterParameter)
            }

            override fun onShow() {
                hideBottomNavigation()
            }

            override fun onHide() {
                showBottomNavigation()
            }

            override fun getActivity(): AppCompatActivity {
                return this@CatalogDetailPageActivity
            }
        })
        bottomSheetFilterView?.onBackPressed()
        if (supportActionBar != null)
            supportActionBar!!.setHomeAsUpIndicator(
                    com.tokopedia.core2.R.drawable.ic_webview_back_button
            )
    }

    private fun applyFilter(filterParameter: Map<String, String>) {
        val fragment = catalogDetailListingFragment as BaseCategorySectionFragment

        if(filterParameter.isNotEmpty() && (filterParameter.size > 1 || !filterParameter.containsKey(ORDER_BY))){
            searchNavContainer?.onFilterSelected(true)
        } else {
            searchNavContainer?.onFilterSelected(false)
        }
        val presentFilterList = fragment.getSelectedFilter()
        if (presentFilterList.size < filterParameter.size) {
            for (i in filterParameter.entries) {
                if (!presentFilterList.containsKey(i.key)) {
                    var title = ""
                    for (filter in filters) {
                        val option = filter.options.firstOrNull {
                            it.key == i.key
                        }
                        if (option != null) {
                            title = filter.title
                            break
                        }
                    }
                    CatalogDetailPageAnalytics.trackEvenFilterApplied(title, i.key, i.value)
                }
            }
        }
        fragment.applyFilterToSearchParameter(filterParameter)
        fragment.setSelectedFilter(HashMap(filterParameter))
        fragment.clearDataFilterSort()
        fragment.reloadData()
    }

    private fun setFragment() {
        catalogDetailFragment = getNewCatalogDetailFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.parent_view, catalogDetailFragment)
                .commit()
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
            toolbar.setBackgroundResource(com.tokopedia.core2.R.color.white)
        } else {
            toolbar.setBackgroundResource(com.tokopedia.core2.R.drawable.bg_white_toolbar_drop_shadow)
        }
        img_share_button.setOnClickListener {
            if (shareData != null) {
                CatalogDetailPageAnalytics.trackEventClickSocialShare()
                DefaultShare(this, shareData).show()
            } else
                NetworkErrorHelper.showSnackbar(this, "Data katalog belum tersedia")
        }
        action_up_btn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun deliverCatalogShareData(shareData: LinkerData, catalogName: String, departmentId:String) {
        this.shareData = shareData
        this.catalogName = catalogName
        title_toolbar.text = catalogName

        catalogDetailListingFragment = getNewCatalogDetailListingFragment(catalogName, departmentId)
        supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, catalogDetailListingFragment)
                .commit()
    }

    private fun initSwitchButton() {
        searchNavContainer?.setSearchNavListener(this)
        img_display_button.tag = STATE_GRID
        img_display_button.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }
            when (img_display_button.tag) {

                STATE_GRID -> {
                    img_display_button.tag = STATE_LIST
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
                }
            }
        }
    }

    override fun onBackPressed() {
        bottomSheetFilterView?.let {
            if (!it.onBackPressed()) {
                checkFragmentisCatalogDetailPageFragment()?.let {fragment->
                    fragment.onBackPress()
                    return
                }
                super.onBackPressed()
            }
        }
    }


    private fun checkFragmentisCatalogDetailPageFragment(): CatalogDetailPageFragment? {
        return if (catalogDetailFragment is CatalogDetailPageFragment) {
            catalogDetailFragment as CatalogDetailPageFragment
        } else {
            null
        }
    }

    fun showBottomNavigation() {
        searchNavContainer?.show()
    }

    override fun setupSearchNavigation(clickListener: CategoryNavigationListener.ClickListener) {
        navigationListenerList.add(clickListener)
    }

    override fun setUpVisibleFragmentListener(visibleClickListener: CategoryNavigationListener.VisibleClickListener) {
        visibleFragmentListener = visibleClickListener
    }

    override fun hideBottomNavigation() {
        searchNavContainer?.hide()
    }

    override fun loadFilterItems(filters: ArrayList<Filter>, searchParameter: MutableMap<String, String>?) {
        this.filters.addAll(filters)
        bottomSheetFilterView?.loadFilterItems(filters, searchParameter)
    }

    override fun setFilterResultCount(formattedResultCount: String?) {
        bottomSheetFilterView?.setFilterResultCount(formattedResultCount)
    }

    override fun launchFilterBottomSheet() {
        bottomSheetFilterView?.launchFilterBottomSheet()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleDefaultActivityResult(requestCode, resultCode, data)
    }

    private fun handleDefaultActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bottomSheetFilterView?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSortButtonClicked() {
        CatalogDetailPageAnalytics.trackEventClickSort()
        visibleFragmentListener?.onSortClick()
    }

    override fun onFilterButtonClicked() {
        CatalogDetailPageAnalytics.trackEventClickFilter()
        visibleFragmentListener?.onFilterClick()
    }

    override fun onSortApplied(showTick: Boolean) {
        searchNavContainer?.onSortSelected(showTick)
    }
}
