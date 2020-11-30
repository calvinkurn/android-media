package com.tokopedia.deals.brand.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.di.component.DealsBrandComponent
import com.tokopedia.deals.brand.domain.viewmodel.DealsBrandViewModel
import com.tokopedia.deals.brand.listener.DealsBrandSearchTabListener
import com.tokopedia.deals.brand.mapper.DealsBrandMapper.mapBrandListToBaseItemView
import com.tokopedia.deals.brand.model.DealsEmptyDataView
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.brand.ui.adapter.DealsBrandAdapter
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.common.listener.OnBaseLocationActionListener
import com.tokopedia.deals.common.listener.SearchBarActionListener
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.common.ui.activity.DealsBaseBrandCategoryActivity
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.fragment.DealsBaseFragment
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_base_deals.*
import kotlinx.android.synthetic.main.fragment_deals_brand.*
import javax.inject.Inject

class DealsBrandFragment : DealsBaseFragment(), DealsBrandActionListener,
        DealsBrandSearchTabListener, OnBaseLocationActionListener, EmptyStateListener,
        SearchBarActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DealsBrandViewModel::class.java) }
    private val baseViewModel by lazy { ViewModelProviders.of(requireActivity(), viewModelFactory).get(DealsBaseViewModel::class.java) }

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    private var isShimerring = true
    private var currentPage = 0

    private var childCategoryId: String? = null

    private var tabName: String = ""
    private var brandAdapter = DealsBrandAdapter(this, this)

    @Inject
    lateinit var analytics: DealsAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childCategoryId = arguments?.getString(CHILD_CATEGORY_ID)
        tabName = arguments?.getString(TAB_NAME) ?: ""

        (activity as DealsBaseActivity).searchBarActionListener = this

        loadInitialData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.dealsShimmerData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    isShimerring = true
                    renderList(it.data, false)
                }
                is Fail -> {
                    renderList(listOf(getErrorNetworkModel()), false)
                }
            }
        })

        viewModel.dealsSearchResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    checkIfShimmering()
                    val totalItem = it.data.brands.size
                    if (totalItem == 0 && currentPage == 1) {
                        toggleNotFound()
                    } else {
                        showTitle(it.data)
                        val nextPage = totalItem >= DEFAULT_MIN_ITEMS
                        renderList(mapBrandListToBaseItemView(it.data.brands, showTitle()), nextPage)
                        cacheData(nextPage)
                    }
                }
                is Fail -> {
                    renderList(listOf(getErrorNetworkModel()), false)
                }
            }
        })

        baseViewModel.observableCurrentLocation.observe(viewLifecycleOwner, Observer {
            onBaseLocationChanged(it)
        })
    }

    private fun checkIfShimmering() {
        if (currentPage == 1 || isShimerring) {
            brandAdapter.clearAllItemsAndAnimateChanges()
            if (isShimerring) {
                isShimerring = false
            }
        }
    }

    private fun renderNotFound() {
        clearAllData()
        hideTitleShimmering()
        renderList(listOf(DealsEmptyDataView(getString(R.string.deals_category_empty_title), getString(R.string.deals_category_empty_description))), false)
    }

    private fun toggleNotFound() {
        (activity as DealsBrandActivity).searchNotFound = true
        if ((activity as DealsBrandActivity).searchNotFound) {
            renderNotFound()
            if (isAnalyticsInitialized) {
                analytics.eventViewSearchNoResultBrandPage((activity as DealsBrandActivity).getSearchKeyword(),
                        tabName, getCurrentLocation().name)
            }
        }
    }

    private fun cacheData(nextPage: Boolean) {
        if (currentPage == 1 && !(activity as DealsBrandActivity).searchNotFound && nextPage) {
            endlessRecyclerViewScrollListener?.loadMoreNextPage()
        }
    }

    private fun showTitle(): Boolean {
        return if (currentPage == 1 && !(activity as DealsBrandActivity).userTyped) {
            true
        } else if (currentPage == 1 && (activity as DealsBrandActivity).userTyped) {
            false
        } else {
            false
        }
    }

    private fun getErrorNetworkModel(): ErrorNetworkModel {
        val errorModel = ErrorNetworkModel()
        errorModel.onRetryListener = ErrorNetworkModel.OnRetryListener { loadData(1) }
        return errorModel
    }

    override fun createAdapterInstance(): BaseCommonAdapter = brandAdapter

    override fun loadData(page: Int) {
        (activity as DealsBrandActivity).searchNotFound = false

        val keyword = (activity as DealsBrandActivity).getSearchKeyword()
        viewModel.getBrandList(keyword, getCurrentLocation(), childCategoryId, page)
        currentPage = page
    }

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(DealsBrandComponent::class.java).inject(this)
    }

    override fun hasInitialLoadingModel(): Boolean = false
    override fun getMinimumScrollableNumOfItems() = DEFAULT_MIN_ITEMS

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        (activity as DealsBrandActivity).registerDataUpdateListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as DealsBrandActivity).unregisterDataUpdateListener(this)
        clearAllData()
    }

    override fun onClickBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        if (isAnalyticsInitialized) {
            analytics.eventClickSearchResultBrandBrandPage(brand, tabName)
        }
        val brandApplink = DealsSearchConstants.PREFIX_DEALS_APPPLINK + DealsSearchConstants.PREFIX_APPLINK_BRAND + brand.brandUrl
        RouteManager.route(context, brandApplink)
    }

    override fun onClickSeeAllBrand(seeAllUrl: String) {}
    override fun callInitialLoadAutomatically() = false

    override fun onTextChanged() {
        clearAllData()
        currentPage = 1
        loadInitialData()
    }

    override fun onUserType() {
        if (!isShimerring) {
            clearAllData()
            isShimerring = true
            viewModel.getInitialData()
        }
    }

    override fun onBaseLocationChanged(location: Location) {
        if (isAnalyticsInitialized) {
            if (getCurrentLocation() != location) {
                analytics.eventClickChangeLocationBrandPage(getCurrentLocation().name, location.name)
            }
        }
        setCurrentLocation(location)
        loadData(1)
    }

    override fun onImpressionBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        if ((activity as DealsBrandActivity).getSearchKeyword().isNotEmpty()) {
            analytics.eventViewSearchResultBrandPage((activity as DealsBrandActivity).getSearchKeyword(),
                    getCurrentLocation().name, listOf(brand), tabName)
        } else {
            analytics.eventViewPopularBrandBrandPage(listOf(brand), tabName)
        }
    }

    override fun onClickSearchBar() {
        (activity as DealsBaseBrandCategoryActivity).appBarLayoutSearchContent.setExpanded(true)
    }

    override fun afterSearchBarTextChanged(text: String) {

    }

    private fun getCurrentLocation() = (activity as DealsBaseActivity).currentLoc
    private fun setCurrentLocation(location: Location) {
        (activity as DealsBaseActivity).currentLoc = location
    }

    private val isAnalyticsInitialized: Boolean
        get() = this::analytics.isInitialized

    override fun resetFilter() {}

    override fun getInitialLayout(): Int = R.layout.fragment_deals_brand
    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.deals_brand_recycler_view)

    override fun showTitle(brands: DealsBrandsDataView) {
        if (!brands.title.isNullOrEmpty() && showTitle()) {
            hideTitleShimmering()
            tv_brand_title.show()
            unused_line.show()
            tv_brand_title.text = brands.title
        }
    }

    private fun hideTitleShimmering() {
        shimmering_title.hide()
    }

    companion object {
        const val TAG = "DealsBrandFragment"
        const val CHILD_CATEGORY_ID = "childCategoryId"
        const val TAB_NAME = "tabName"
        const val DEFAULT_MIN_ITEMS = 20
        fun getInstance(childCategoryId: String?, tabName: String): DealsBrandFragment {
            val fragment = DealsBrandFragment()
            val bundle = Bundle()
            if (childCategoryId != null) {
                bundle.putString(CHILD_CATEGORY_ID, childCategoryId)
            }
            bundle.putString(TAB_NAME, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }
}