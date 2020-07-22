package com.tokopedia.deals.category.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.category.di.DealsCategoryComponent
import com.tokopedia.deals.category.listener.DealsCategoryFilterBottomSheetListener
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.category.ui.adapter.DealsCategoryAdapter
import com.tokopedia.deals.category.ui.viewmodel.DealCategoryViewModel
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.listener.DealChipsListActionListener
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.ProductListListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsChipsViewHolder
import com.tokopedia.deals.common.listener.*
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.deals.common.ui.fragment.DealsBaseFragment
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.home.ui.fragment.DealsHomeFragment
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.ui.activity.DealsSearchActivity
import javax.inject.Inject


class DealsCategoryFragment : DealsBaseFragment(),
        OnBaseLocationActionListener, SearchBarActionListener,
        DealsBrandActionListener, ProductListListener,
        DealChipsListActionListener, DealsCategoryFilterBottomSheetListener,
        EmptyStateListener {

    private var categoryID: String = ""
    private var chips : DealsChipsDataView = DealsChipsDataView()

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var analytics: DealsAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var dealCategoryViewModel: DealCategoryViewModel
    private lateinit var baseViewModel: DealsBaseViewModel

    private var filterBottomSheet: DealsCategoryFilterBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        dealCategoryViewModel = viewModelProvider.get(DealCategoryViewModel::class.java)
        baseViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(DealsBaseViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLayout()
    }

    private fun observeLayout() {
        dealCategoryViewModel.observableDealsCategoryLayout.observe(viewLifecycleOwner, Observer {
            isLoadingInitialData = true
            checkIfCategoryIdEqualsToFilterChipId(it)
            renderList(it, true)
        })

        dealCategoryViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            isLoadingInitialData = true
            renderList(listOf(getErrorNetworkModel()), false)
        })

        dealCategoryViewModel.observableProducts.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                renderList(it, true)
            }
        })

        dealCategoryViewModel.observableChips.observe(viewLifecycleOwner, Observer {
            val chipsViewHolder = recyclerView.findViewHolderForAdapterPosition(
                    CHIPS_VIEW_HOLDER_POSITION) as DealsChipsViewHolder
            chips = it
            chipsViewHolder.updateChips(chips)
        })

        baseViewModel.observableCurrentLocation.observe(this, Observer {
            onBaseLocationChanged(it)
        })
    }

    private fun checkIfCategoryIdEqualsToFilterChipId(layouts: List<DealsBaseItemDataView>) {
        if (layouts[CHIPS_VIEW_HOLDER_POSITION] is DealsChipsDataView) {
            (layouts[CHIPS_VIEW_HOLDER_POSITION] as DealsChipsDataView).chipList.forEach {
                if (it.id == categoryID) it.isSelected = true
            }
        }
    }

    override fun createAdapterInstance(): BaseCommonAdapter {
        return DealsCategoryAdapter(this, this, this, this)
    }

    override fun loadData(page: Int) {
        var categoryIDs = chips.chipList.filter { it.isSelected }.joinToString(separator = ",") { it.id }
        if (categoryIDs.isEmpty()) categoryIDs = categoryID
        getCurrentLocation().let {
            dealCategoryViewModel.getCategoryBrandData(
                    categoryIDs,
                    it.coordinates,
                    it.locType.name,
                    page,
                    false
            )
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsCategoryComponent::class.java).inject(this)
    }

    /** DealsBrandActionListener **/
    override fun onClickBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventClickBrandPopular(brand, position, true)
        RouteManager.route(requireContext(), brand.brandUrl)
    }

    override fun onClickSeeAllBrand(seeAllUrl: String) {
        analytics.eventSeeAllBrandPopularOnCategoryPage()
        startActivity(DealsBrandActivity.getCallingIntent(requireContext(), null, categoryID))
    }

    /** ProductListListener **/
    override fun onProductClicked(
            productCardDataView: ProductCardDataView,
            productItemPosition: Int
    ) {
        RouteManager.route(context, productCardDataView.appUrl)
    }

    /** DealChipsListActionListener **/
    override fun onFilterChipClicked(chips: DealsChipsDataView) {
        if (filterBottomSheet == null) {
            filterBottomSheet = DealsCategoryFilterBottomSheet(this)
        }
        filterBottomSheet?.showCategories(chips)
        filterBottomSheet?.show(childFragmentManager, "")
    }

    override fun onChipClicked(chips: DealsChipsDataView) {
        analytics.eventClickChipsCategory()
        applyFilter(chips)
    }

    /** DealsCategoryFilterBottomSheetListener **/
    override fun onFilterApplied(chips: DealsChipsDataView) {
        analytics.eventApplyChipsCategory(chips)
        applyFilter(chips)
    }

    private fun applyFilter(chips: DealsChipsDataView) {
        var categoryIDs = chips.chipList.filter { it.isSelected }.joinToString(separator = ",") { it.id }
        if (categoryIDs.isEmpty()) categoryIDs = categoryID
        dealCategoryViewModel.updateChips(chips, getCurrentLocation(), categoryIDs, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DealsHomeFragment.DEALS_SEARCH_REQUEST_CODE -> (activity as DealsBaseActivity).changeLocationBasedOnCache()
        }
    }

    companion object {
        const val DEALS_SEARCH_REQUEST_CODE = 27
        private const val CHIPS_VIEW_HOLDER_POSITION = 0

        fun getInstance(categoryId: String?): DealsCategoryFragment = DealsCategoryFragment().also {
            it.arguments = Bundle().apply {
                categoryId?.let { id ->
                    putString(DealsCategoryActivity.EXTRA_CATEGORY_ID, id)
                }
            }
        }
    }

    private fun getErrorNetworkModel(): ErrorNetworkModel {
        val errorModel = ErrorNetworkModel()
        errorModel.onRetryListener = ErrorNetworkModel.OnRetryListener { loadData(1) }
        return errorModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryID = arguments?.getString(DealsCategoryActivity.EXTRA_CATEGORY_ID, "") ?: ""

        (activity as DealsBaseActivity).searchBarActionListener = this

        loadInitialData()
    }

    override fun onBaseLocationChanged(location: Location) {
        setCurrentLocation(location)
        getCurrentLocation()
        dealCategoryViewModel.loadFilterShimmering()
        loadData(1)
        analytics.eventClickChangeLocationCategoryPage(getCurrentLocation().name, location.name)
    }

    override fun hasInitialLoadingModel(): Boolean = false

    override fun callInitialLoadAutomatically(): Boolean = false

    private fun getCurrentLocation() = (activity as DealsBaseActivity).currentLoc
    private fun setCurrentLocation(location: Location) {
        (activity as DealsBaseActivity).currentLoc = location
    }

    override fun onClickSearchBar() {
        analytics.eventClickSearchCategoryPage()
        startActivityForResult(Intent(activity, DealsSearchActivity::class.java), DEALS_SEARCH_REQUEST_CODE)
    }

    override fun afterSearchBarTextChanged(text: String) { /* do nothing */
    }

    override fun resetFilter() {
        val chipReseted = DealsChipsDataView(chips.chipList.map { it.copy(isSelected = false) })
        dealCategoryViewModel.updateChips(chipReseted, getCurrentLocation(), categoryID, false)
    }

    override fun onImpressionBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventScrollToBrandPopular(brand,position)
    }

    override fun onImpressionProduct(productCardDataView: ProductCardDataView, productItemPosition: Int, page: Int) {
        analytics.impressionProductCategory(productCardDataView,productItemPosition, page)
    }

    override fun showTitle(brand: DealsBrandsDataView) {
        /* do nothing */
    }
}
