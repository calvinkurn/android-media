package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.CategoryNavigationListener
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.linker.model.LinkerData

class CatalogDetailPageActivity :  BaseSimpleActivity(),
        CatalogDetailPageFragment.Listener,
        CategoryNavigationListener,
        BaseCategorySectionFragment.SortAppliedListener{

    private var catalogId: String = ""
    private var shareData: LinkerData? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()
    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private lateinit var catalogDetailFragment: CatalogDetailPageFragment
    private lateinit var catalogDetailListingFragment: Fragment
    private var catalogName: String = ""

    companion object {
        private const val CATALOG_DETAIL_TAG = "CATALOG_DETAIL_TAG"
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
        @JvmStatic
        fun createIntent(context: Context, catalogId: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            return intent
        }
    }

    override fun getParentViewResourceID(): Int {
        return R.id.catalog_detail_parent_view
    }

    override fun getNewFragment(): Fragment? {
        catalogDetailFragment =  getNewCatalogDetailFragment()
        return catalogDetailFragment
    }

    override fun getTagFragment(): String {
        return CATALOG_DETAIL_TAG
    }

    override fun getScreenName(): String? {
        return AppScreen.SCREEN_CATALOG
    }

    private fun getNewCatalogDetailFragment(): CatalogDetailPageFragment {
        return CatalogDetailPageFragment.newInstance(catalogId)
    }

    private fun getNewCatalogDetailListingFragment(catalogName: String, departmentId: String): Fragment {
        val departmentName: String? = intent.getStringExtra(EXTRA_CATEGORY_DEPARTMENT_NAME)
        val fragment: BaseCategorySectionFragment = CatalogDetailProductListingFragment.newInstance(catalogId, catalogName, departmentId, departmentName)
        fragment.setSortListener(this)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_detail_page)
        catalogId = if (intent.hasExtra(EXTRA_CATALOG_ID))
            intent.getStringExtra(EXTRA_CATALOG_ID)
        else {
            intent.data?.path?.replace("/", "") ?: ""
        }
        initData()
        prepareView()
    }

    private fun initData() {
        catalogDetailFragment.setCatalogId(catalogId)
    }

    private fun prepareView() {

    }

    override fun deliverCatalogShareData(shareData: LinkerData, catalogName: String, departmentId: String) {
        this.shareData = shareData
        this.catalogName = catalogName

        catalogDetailListingFragment = getNewCatalogDetailListingFragment(catalogName, departmentId)
        supportFragmentManager.beginTransaction()
                .add(R.id.catalog_detail_parent_view, catalogDetailListingFragment)
                .commit()
    }


    override fun setupSearchNavigation(clickListener: CategoryNavigationListener.ClickListener) {
        navigationListenerList.add(clickListener)
    }

    override fun setUpVisibleFragmentListener(visibleClickListener: CategoryNavigationListener.VisibleClickListener) {
        visibleFragmentListener = visibleClickListener
    }

    override fun hideBottomNavigation() {

    }

    override fun onSortApplied(showTick: Boolean) {

    }
}
