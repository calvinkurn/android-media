package com.tokopedia.discovery.find.view.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.ui.customview.SearchNavigationView
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.find.view.fragment.FindNavFragment
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import java.util.*

class FindNavActivity : BaseActivity(), CategoryNavigationListener,
        BottomSheetListener, SearchNavigationView.SearchNavClickListener, BaseCategorySectionFragment.SortAppliedListener {

    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: SearchNavigationView? = null
    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()
    private lateinit var findNavFragment: BaseCategorySectionFragment
    private lateinit var findSearchParam: String
    private var findNavScreenName: String = ""
    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3
    private val ORDER_BY = "ob"

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
        for(i in splits){
            findNavScreenName = findNavScreenName.plus(i.capitalize()+" ")
        }
        findNavScreenName.trim()
    }

    private fun initToolbar() {
        findViewById<ImageView>(R.id.action_up_btn).setOnClickListener {
            onBackPressed()
        }
        findViewById<AppCompatTextView>(R.id.et_search).text = findNavScreenName

        findViewById<ImageView>(R.id.search_button).setOnClickListener {
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
            override fun onApplyFilter(filterParameter: Map<String, String>) {
                applyFilter(filterParameter)
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
                    //to add gtm
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
        findNavFragment.clearDataFilterSort()
        findNavFragment.reloadData()
    }

    private fun sendBottomSheetHideEvent() {
        findNavFragment.onBottomSheetHide()
    }

    private fun showBottomNavigation() {
        searchNavContainer?.show()
    }

    private fun initView() {
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.search_nav_container)
        searchNavContainer?.setSearchNavListener(this)
        val imageDisplayButton: ImageButton = findViewById(R.id.img_display_button)
        imageDisplayButton.tag = STATE_GRID

        imageDisplayButton.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }

            when (imageDisplayButton.tag) {

                STATE_GRID -> {
                    imageDisplayButton.tag = STATE_LIST
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    imageDisplayButton.tag = STATE_BIG
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    imageDisplayButton.tag = STATE_GRID
                    imageDisplayButton.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
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

    override fun loadFilterItems(filters: ArrayList<Filter>?, searchParameter: MutableMap<String, String>?) {
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

}
