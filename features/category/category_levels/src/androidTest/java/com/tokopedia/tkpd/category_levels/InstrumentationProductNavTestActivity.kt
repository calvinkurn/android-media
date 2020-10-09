package com.tokopedia.tkpd.category_levels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.categorylevels.test.R
import com.tokopedia.categorylevels.view.fragment.ProductNavFragment
import com.tokopedia.common_category.customview.SearchNavigationView
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.CategoryNavigationListener
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import java.util.*

class InstrumentationProductNavTestActivity : AppCompatActivity(),
        CategoryNavigationListener,
        SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener,
        BaseBannedProductFragment.OnBannedFragmentInteractionListener,
        BottomSheetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_nav_test)
        val fragment: Fragment = ProductNavFragment.newInstance("25", "Audio", "tokopedia-android-internal://category/25?categoryName=Audio", BannedData())
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container_product_nav, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onButtonClicked(bannedProduct: BannedData) {

    }

    override fun onBannedFragmentAttached() {

    }

    override fun setupSearchNavigation(clickListener: CategoryNavigationListener.ClickListener) {

    }

    override fun setUpVisibleFragmentListener(visibleClickListener: CategoryNavigationListener.VisibleClickListener) {

    }

    override fun hideBottomNavigation() {

    }

    override fun onSortApplied(showTick: Boolean) {

    }

    override fun loadFilterItems(filters: ArrayList<Filter>?, searchParameter: Map<String, String>?) {

    }

    override fun setFilterResultCount(formattedResultCount: String?) {

    }

    override fun launchFilterBottomSheet() {

    }
}