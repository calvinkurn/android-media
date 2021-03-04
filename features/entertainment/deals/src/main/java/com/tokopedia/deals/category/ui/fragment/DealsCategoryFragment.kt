package com.tokopedia.deals.category.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.model.DealsEmptyDataView
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.category.di.DealsCategoryComponent
import com.tokopedia.deals.category.listener.DealsCategoryFilterBottomSheetListener
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.category.ui.adapter.DealsCategoryAdapter
import com.tokopedia.deals.category.ui.viewmodel.DealCategoryViewModel
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.listener.*
import com.tokopedia.deals.common.model.LoadingMoreUnifyModel
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.common.ui.dataview.*
import com.tokopedia.deals.common.ui.fragment.DealsBaseFragment
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.home.ui.fragment.DealsHomeFragment
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.ui.activity.DealsSearchActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.fragment_deals_category.*
import kotlinx.android.synthetic.main.item_deals_chips_list.*
import javax.inject.Inject


class DealsCategoryFragment : DealsBaseFragment(),
        OnBaseLocationActionListener, SearchBarActionListener,
        DealsBrandActionListener, ProductCardListener,
        DealChipsListActionListener, DealsCategoryFilterBottomSheetListener,
        EmptyStateListener {

    private var categoryID: String = ""

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
            handleRanderList(it)
            handleShimering(it)
        })

        dealCategoryViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            isLoadingInitialData = true
            renderList(listOf(getErrorNetworkModel()), false)
        })

        dealCategoryViewModel.observableProducts.observe(viewLifecycleOwner, Observer {
            val nextPage = it.size >= DEFAULT_MIN_ITEMS_PRODUCT
            renderList(it, nextPage)
        })

        dealCategoryViewModel.observableChips.observe(viewLifecycleOwner, Observer {
            chips = it
            renderChips()
        })

        baseViewModel.observableCurrentLocation.observe(viewLifecycleOwner, Observer {
            onBaseLocationChanged(it)
        })

        handleRecycler()
    }

    private fun handleRanderList(list: List<DealsBaseItemDataView>) {
        val nextPage = list.size >= DEFAULT_MIN_ITEMS
        if (list.size == INITIAL_SIZE_BASE_ITEM_VIEW) {
            var isNotEmpty = false
            list.map {
                if (it.isLoaded) {
                    isNotEmpty = true
                }
            }
            if (isNotEmpty) {
                renderList(list, nextPage)
            }
        } else {
            renderList(list, nextPage)
        }
    }

    private fun handleShimering(list: List<DealsBaseItemDataView>) {

        list.forEach {dealsBaseItemDataView ->
            when (dealsBaseItemDataView) {
                is DealsBrandsDataView -> {
                    if (dealsBaseItemDataView.isSuccess) {
                        one_row_shimmering.hide()
                        shimmering.hide()
                    }  else {
                        one_row_shimmering.show()
                        shimmering.show()
                    }
                }

                is DealsEmptyDataView -> {
                    shimmering.hide()
                    one_row_shimmering.hide()
                }
            }
        }
    }

    override fun enableLoadMore() {
        val layoutManager = GridLayoutManager(context, PRODUCT_SPAN_COUNT)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
              return when (position) {
                0 -> if (adapter.data.first()::class == DealsBrandsDataView::class) 2 else if (adapter.data.first()::class == DealsEmptyDataView::class) 2 else 1
                adapter.data.lastIndex -> if (adapter.data[adapter.data.lastIndex]::class == LoadingMoreUnifyModel::class) 2 else 1
                else -> 1
              }
            }
        }

        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    showLoadMoreLoading()
                    loadData(page)
                }
            }
        }
        endlessRecyclerViewScrollListener?.let {
            recyclerView.addOnScrollListener(it)
        }

        recyclerView.layoutManager = layoutManager
    }


    private fun handleRecycler() {

        val decoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = parent.getChildAdapterPosition(view)
                val totalSpanCount = getTotalSpanCount(parent)

                if (position != RecyclerView.NO_POSITION) {
                    outRect.bottom = if (isInTheFirstRow(position + 2, totalSpanCount)) resources.getInteger(R.integer.sixteen).toPx() else resources.getInteger(R.integer.four).toPx()
                    outRect.left = if (isFirstInRow(position + 2, totalSpanCount)) resources.getInteger(R.integer.two).toPx() else resources.getInteger(R.integer.sixteen).toPx()
                    outRect.right = if (isFirstInRow(position + 2, totalSpanCount))resources.getInteger(R.integer.sixteen).toPx() else resources.getInteger(R.integer.two).toPx()
                }
            }
        }
        recyclerView.addItemDecoration(decoration)

    }

    private fun isInTheFirstRow(position: Int, spanCount: Int): Boolean {
        return position < spanCount
    }

    private fun isFirstInRow(position: Int, spanCount: Int): Boolean {
        return position % spanCount == 0
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanCount ?: 1
    }

    override fun createAdapterInstance(): BaseCommonAdapter {
        return DealsCategoryAdapter(this, this, this)
    }

    override fun loadData(page: Int) {
        getCurrentLocation().let {
            dealCategoryViewModel.getCategoryBrandData(
                    categoryID,
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

    private var chips: List<ChipDataView> = listOf()

    private fun renderChips() {
        filter_shimmering.hide()
        sort_filter_deals_category.visible()

        val filterItems = arrayListOf<SortFilterItem>()
        try {
            val showingChips = if (chips.size > 5) {
                chips.subList(0, 4)
            } else {
                chips
            }
            showingChips.forEach {
                val item = SortFilterItem(it.title)
                filterItems.add(item)
            }
        } catch (e: Exception) { }

        for (chip in chips) {
            if (chip.id == categoryID) chip.isSelected = true
        }

        sort_filter_deals_category.run {
            filterItems.forEachIndexed { index, sortFilterItem ->
                if (chips[index].isSelected) {
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                }

                sortFilterItem.listener = {
                    sortFilterItem.toggle()

                    val mutableChipList = chips.toMutableList()
                    val chipItem = mutableChipList[index]

                    val isItemSelected = filterItems[index].type == ChipsUnify.TYPE_SELECTED
                    mutableChipList[index] = chipItem.copy(isSelected = isItemSelected)
                    chips = mutableChipList

                    onChipClicked(mutableChipList)
                }
            }

            addItem(filterItems)

            if (chips.size <= 5) {
                sortFilterPrefix.hide()
            } else {
                sortFilterPrefix.setOnClickListener {
                    onFilterChipClicked(chips)
                }
            }

            selectFilterFromChipsData()
        }
    }

    override fun getInitialLayout(): Int = R.layout.fragment_deals_category
    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.deals_category_recycler_view)

    private var additionalSelectedFilterCount = 0

    private fun selectFilterFromChipsData() {
        sort_filter_deals_category.let { sortFilter ->
            sortFilter.chipItems?.let { chipItems ->
                for ((i, item) in chipItems.withIndex()) {
                    if (chips[i].isSelected) item.type = ChipsUnify.TYPE_SELECTED
                    else item.type = ChipsUnify.TYPE_NORMAL
                }

                sortFilter.indicatorCounter -= additionalSelectedFilterCount
                additionalSelectedFilterCount = 0
                if (chips.size > chipItems.size) {
                    for (i in chipItems.size until chips.size) {
                        if (chips[i].isSelected) additionalSelectedFilterCount++
                    }
                }
                sortFilter.indicatorCounter += additionalSelectedFilterCount
            }
        }
    }

    private fun SortFilterItem.toggle() {
        type = if (type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    /** DealsBrandActionListener **/
    override fun onClickBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventClickBrandPopular(brand, position, true)
        RouteManager.route(context, brand.brandUrl)
    }

    override fun onClickSeeAllBrand(seeAllUrl: String) {
        analytics.eventSeeAllBrandPopularOnCategoryPage()
        startActivity(DealsBrandActivity.getCallingIntent(requireContext(), null, categoryID))
    }

    /** DealChipsListActionListener **/
    override fun onFilterChipClicked(chips: List<ChipDataView>) {
        if (filterBottomSheet == null) {
            filterBottomSheet = DealsCategoryFilterBottomSheet(this)
        }
        filterBottomSheet?.showCategories(DealsChipsDataView(chips))
        filterBottomSheet?.show(childFragmentManager, "")
    }

    override fun onChipClicked(chips: List<ChipDataView>) {
        analytics.eventClickChipsCategory()
        this.chips = chips
        applyFilter()
    }

    /** DealsCategoryFilterBottomSheetListener **/
    override fun onFilterApplied(chips: DealsChipsDataView) {
        analytics.eventApplyChipsCategory(chips)
        this.chips = chips.chipList
        selectFilterFromChipsData()

        applyFilter()
    }

    private fun getErrorNetworkModel(): ErrorNetworkModel {
        val errorModel = ErrorNetworkModel()
        errorModel.onRetryListener = ErrorNetworkModel.OnRetryListener { loadData(1) }
        return errorModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryID = arguments?.getString(DealsCategoryActivity.EXTRA_CATEGORY_ID, "") ?: ""
        getRecyclerView(view).tag = arguments?.getString(EXTRA_TAB_NAME, "") ?: ""

        (activity as DealsBaseActivity).searchBarActionListener = this

        dealCategoryViewModel.getChipsData()
    }

    override fun onBaseLocationChanged(location: Location) {
        getCurrentLocation()
        dealCategoryViewModel.shimmeringCategory()
        applyFilter()

        if ((activity as DealsBaseActivity).currentLoc != location) {
            analytics.eventClickChangeLocationCategoryPage(oldLocation = (activity as DealsBaseActivity).currentLoc.name, newLocation = location.name)
        }
        setCurrentLocation(location)
        recyclerView.smoothScrollToPosition(0)
    }

    override fun hasInitialLoadingModel(): Boolean = false
    override fun getMinimumScrollableNumOfItems(): Int = DEFAULT_MIN_ITEMS
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

    override fun onImpressionBrand(brand: DealsBrandsDataView.Brand, position: Int) {
        analytics.eventScrollToBrandPopular(brand, position)
    }

    override fun onProductClicked(itemView: View, productCardDataView: ProductCardDataView, position: Int) {
        RouteManager.route(context, productCardDataView.appUrl)
    }

    override fun onImpressionProduct(productCardDataView: ProductCardDataView, productItemPosition: Int, page: Int) {
        analytics.impressionProductCategory(productCardDataView, productItemPosition, page)
    }

    override fun resetFilter() {
        chips.forEach { it.isSelected = false }
        selectFilterFromChipsData()
        dealCategoryViewModel.updateChips(getCurrentLocation(), categoryID, false)
    }

    private fun applyFilter() {
        var categoryIDs = chips.filter { it.isSelected }.joinToString(separator = ",") { it.id }
        if (categoryIDs.isEmpty()) categoryIDs = categoryID
        dealCategoryViewModel.updateChips(getCurrentLocation(), categoryIDs, categoryIDs.isNotEmpty())
    }

    override fun showTitle(brand: DealsBrandsDataView) {
        /* do nothing */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DealsHomeFragment.DEALS_SEARCH_REQUEST_CODE -> (activity as DealsBaseActivity).changeLocationBasedOnCache()
        }
    }

    companion object {
        const val DEALS_SEARCH_REQUEST_CODE = 27
        private const val PRODUCT_SPAN_COUNT = 2
        const val DEFAULT_MIN_ITEMS_PRODUCT = 20
        const val DEFAULT_MIN_ITEMS = 21
        const val INITIAL_SIZE_BASE_ITEM_VIEW = 2

        private const val EXTRA_TAB_NAME = ""

        fun getInstance(categoryId: String?, tabName: String = ""): DealsCategoryFragment = DealsCategoryFragment().also {
            it.arguments = Bundle().apply {
                categoryId?.let { id ->
                    putString(DealsCategoryActivity.EXTRA_CATEGORY_ID, id)
                }
                putString(EXTRA_TAB_NAME, tabName)
            }
        }
    }
}
