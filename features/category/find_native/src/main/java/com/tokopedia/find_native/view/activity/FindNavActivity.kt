package com.tokopedia.find_native.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.common_category.customview.SearchNavigationView
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.CategoryNavigationListener
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.find_native.R
import com.tokopedia.find_native.analytics.FindPageAnalytics.Companion.findPageAnalytics
import com.tokopedia.find_native.view.fragment.FindNavFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.activity_find_nav.*
import java.util.*

private const val STATE_GRID = 1
private const val STATE_LIST = 2
private const val STATE_BIG = 3
private const val ORDER_BY = "ob"

class FindNavActivity : BaseActivity(), CategoryNavigationListener,
        BottomSheetListener, SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener, BaseBannedProductFragment.OnBannedFragmentInteractionListener {

    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()
    private lateinit var findNavFragment: BaseCategorySectionFragment
    private lateinit var findSearchParam: String
    private var findNavScreenName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_nav)
        setUpDataFromIntent()
        initToolbar()
        initView()
        initBottomSheetFilterView()
        attachFragment()
    }

    private fun attachFragment() {
        val fragment = FindNavFragment.newInstance(findSearchParam)
        findNavFragment = fragment as BaseCategorySectionFragment
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
                fragment).commit()
        findNavFragment.setSortListener(this)
    }

    private fun setUpDataFromIntent() {
        intent.data?.let {
            val uri = it.pathSegments
            findSearchParam = uri[uri.lastIndex]
            getFindNavScreenName()
        }
    }

    private fun getFindNavScreenName() {
        findNavScreenName = findSearchParam.replace("-", " ")
        val splits = findNavScreenName.split(" ")
        findNavScreenName = ""
        for (i in splits) {
            findNavScreenName = findNavScreenName.plus(i.capitalize() + " ")
        }
        findNavScreenName.trim()
    }

    private fun initToolbar() {
        actionUpButton.setOnClickListener {
            findPageAnalytics.eventClickBackButton()
            onBackPressed()
        }
        etSearch.text = findNavScreenName

        searchButton.setOnClickListener {
            findPageAnalytics.eventClickSearchKeyword(findSearchParam)
            moveToAutoCompleteActivity(findNavScreenName)
        }
    }

    private fun moveToAutoCompleteActivity(searchParam: String) {
        RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + searchParam)
    }

    private fun initBottomSheetFilterView() {
        // also change the tracking events
        bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                FilterEventTracking.Event.CLICK_CATEGORY,
                FilterEventTracking.Category.FILTER_CATEGORY,
                "",
                FilterEventTracking.Category.PREFIX_CATEGORY_PAGE))
        bottomSheetFilterView?.setCallback(object : BottomSheetFilterView.Callback {
            override fun onApplyFilter(filterParameter: Map<String, String>?) {
                filterParameter?.let { applyFilter(it) }
            }

            override fun onShow() {
                hideBottomNavigation()
            }

            override fun onHide() {
                showBottomNavigation()
                sendBottomSheetHideEvent()
            }

            override fun getActivity(): AppCompatActivity {
                return this@FindNavActivity
            }
        })

    }

    private fun applyFilter(filterParameter: Map<String, String>) {
        val presentFilterList = findNavFragment.getSelectedFilter()
        if (presentFilterList.size < filterParameter.size) {
            for (i in filterParameter.entries) {
                if (!presentFilterList.containsKey(i.key)) {
                    findPageAnalytics.eventClickFilter()
                }
            }
        }
        if (filterParameter.isNotEmpty() && (filterParameter.size > 1 || !filterParameter.containsKey(ORDER_BY))) {
            searchNavContainer?.onFilterSelected(true)
        } else {
            searchNavContainer?.onFilterSelected(false)
        }
        findNavFragment.applyFilterToSearchParameter(filterParameter)
        findNavFragment.setSelectedFilter(HashMap(filterParameter))
//        findNavFragment.clearDataFilterSort()
        findNavFragment.reloadData()
    }

    private fun sendBottomSheetHideEvent() {
        findNavFragment.onBottomSheetHide()
    }

    private fun showBottomNavigation() {
        searchNavContainer?.show()
    }

    private fun initView() {
        searchNavContainer?.setSearchNavListener(this)
        imageDisplayButton.tag = STATE_GRID

        imageDisplayButton.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }

            when (imageDisplayButton.tag) {

                STATE_GRID -> {
                    findPageAnalytics.eventClickViewType()
                    imageDisplayButton.tag = STATE_LIST
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    findPageAnalytics.eventClickViewType()
                    imageDisplayButton.tag = STATE_BIG
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    findPageAnalytics.eventClickViewType()
                    imageDisplayButton.tag = STATE_GRID
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_grid_display))
                }
            }
        }
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

    override fun loadFilterItems(filters: ArrayList<Filter>?, searchParameter: Map<String, String>?) {
        bottomSheetFilterView?.loadFilterItems(filters, searchParameter)
    }

    override fun setFilterResultCount(formattedResultCount: String?) {
        bottomSheetFilterView?.setFilterResultCount(formattedResultCount)
    }

    override fun launchFilterBottomSheet() {
        bottomSheetFilterView?.launchFilterBottomSheet()
    }

    override fun onBackPressed() {
        bottomSheetFilterView?.let {
            if (!it.onBackPressed()) {
                finish()
            }
        }
    }

    override fun onSortButtonClicked() {
        visibleFragmentListener?.onSortClick()
    }

    override fun onFilterButtonClicked() {
        visibleFragmentListener?.onFilterClick()

    }

    override fun onSortApplied(showTick: Boolean) {
        searchNavContainer?.onSortSelected(showTick)
    }

    override fun onButtonClicked(bannedProduct: BannedData) {
        //To handle Analytics
    }

    override fun onBannedFragmentAttached() {
        hideBottomNavigation()
        imageDisplayButton.invisible()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleDefaultActivityResult(requestCode, resultCode, data)
    }

    private fun handleDefaultActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bottomSheetFilterView?.onActivityResult(requestCode, resultCode, data)
    }

}
