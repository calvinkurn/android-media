package com.tokopedia.salam.umrah.search.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingUtil
import com.tokopedia.salam.umrah.common.data.DefaultOption
import com.tokopedia.salam.umrah.common.data.UmrahOption
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.di.UmrahSearchComponent
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_CATEGORY_SLUG_NAME
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_CITY_ID
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_PERIOD
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MIN
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MIN
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_SORT
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.REQUEST_FILTER
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchFilterActivity
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapter
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapterTypeFactory
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchSortAdapter
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchFilterSortViewModel
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchViewModel
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheets_umrah_search_sort.view.*
import kotlinx.android.synthetic.main.fragment_umrah_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 18/10/2019
 */
class UmrahSearchFragment : BaseListFragment<UmrahSearchProduct, UmrahSearchAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, UmrahSearchAdapter.OnClickListener, UmrahSearchActivity.OnBackListener {

    private val umrahSearchSortAdapter: UmrahSearchSortAdapter by lazy { UmrahSearchSortAdapter() }
    private var isSortLoaded = false
    private var sort = DefaultOption()
    private lateinit var sortBottomSheets: BottomSheetUnify
    private lateinit var sortView: View
    private var searchOrCategory: SearchOrCategory = SearchOrCategory.SEARCH
    private val searchParam = UmrahSearchProductDataParam()
    private val selectedFilter = ParamFilter()

    override fun onEmptyContentItemTextClicked() {
    }

    @Inject
    lateinit var umrahSearchViewModel: UmrahSearchViewModel

    @Inject
    lateinit var umrahSearchFilterSortViewModel: UmrahSearchFilterSortViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingUtil

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahSearchComponent::class.java).inject(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER && data != null) {
                data.let {
                    selectedFilter.apply {
                        departureCity = it.getStringExtra(EXTRA_DEPARTURE_CITY_ID)
                        departurePeriod = it.getStringExtra(EXTRA_DEPARTURE_PERIOD)
                        priceMinimum = it.getIntExtra(EXTRA_PRICE_MIN, 0)
                        priceMaximum = it.getIntExtra(EXTRA_PRICE_MAX, 0)
                        durationDaysMinimum = it.getIntExtra(EXTRA_DURATION_DAYS_MIN, 0)
                        durationDaysMaximum = it.getIntExtra(EXTRA_DURATION_DAYS_MAX, 0)
                    }
                }
                umrahSearchViewModel.setFilter(selectedFilter)
                umrahTrackingUtil.umrahSearchNCategoryFilterClick(selectedFilter, searchOrCategory)
                loadInitialData()
                isFilter = true
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isFilter) {
            getSearchParamFromBundle()
            setSelectedFilterFromBundle()
        }
        if (umrahSearchViewModel.getSlugName() != "") searchOrCategory = SearchOrCategory.CATEGORY
        initSortBottomSheets()
    }

    private fun setSelectedFilterFromBundle() {
        searchParam.let {
            selectedFilter.apply {
                departureCity = it.departureCityId
                departurePeriod = it.departurePeriod
                durationDaysMinimum = it.durationDaysMin
                durationDaysMaximum = it.durationDaysMax
                priceMinimum = it.priceMin
                priceMaximum = it.priceMax
            }
        }
        umrahSearchViewModel.setFilter(selectedFilter)
    }

    private fun getSearchParamFromBundle() {
        arguments?.let {
            searchParam.apply {
                categorySlugName = it.getString(EXTRA_CATEGORY_SLUG_NAME, "")
                departureCityId = it.getString(EXTRA_DEPARTURE_CITY_ID, "")
                departurePeriod = it.getString(EXTRA_DEPARTURE_PERIOD, "")
                priceMin = it.getInt(EXTRA_PRICE_MIN, 0)
                priceMax = it.getInt(EXTRA_PRICE_MAX, 0)
                if (sortMethod == "") sortMethod = it.getString(EXTRA_SORT, "")
                durationDaysMin = it.getInt(EXTRA_DURATION_DAYS_MIN, 0)
                durationDaysMax = it.getInt(EXTRA_DURATION_DAYS_MAX, 0)
            }
        }
        umrahSearchViewModel.setSearchParam(searchParam)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahSearchViewModel.searchResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError(it.throwable)
            }
        })
        umrah_search_bottom_action_view.setButton1OnClickListener { openSortBottomSheets() }
        umrah_search_bottom_action_view.setButton2OnClickListener { openFilterFragment() }
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.umrah_search_swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.umrah_search_recycler_view

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getAdapterTypeFactory(): UmrahSearchAdapterTypeFactory = UmrahSearchAdapterTypeFactory(this)

    override fun onItemClicked(product: UmrahSearchProduct?) {}
    override fun onItemClicked(product: UmrahSearchProduct, position: Int) {
        umrahTrackingUtil.umrahSearchNCategoryProductClick(product, position, umrahSearchViewModel.getSortValue(), selectedFilter, searchOrCategory)
        startActivity(context?.let { UmrahPdpActivity.createIntent(it, product.slugName) })
    }

    override fun createAdapterInstance(): BaseListAdapter<UmrahSearchProduct, UmrahSearchAdapterTypeFactory> {
        return UmrahSearchAdapter(this, adapterTypeFactory)
    }

    override fun loadData(page: Int) {
        umrahSearchViewModel.searchUmrahProducts(page,
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_search_product))
    }

    private fun onSuccessGetResult(data: List<UmrahSearchProduct>) {
        umrahTrackingUtil.umrahSearchNCategoryProductListImpression(data, umrahSearchViewModel.getSortValue(), selectedFilter, searchOrCategory)
        umrah_search_bottom_action_view.visible()
        renderList(data, data.size >= searchParam.limit)
    }

    private fun openFilterFragment() {
        startActivityForResult(context?.let { UmrahSearchFilterActivity.createIntent(it) }, REQUEST_FILTER)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        umrah_search_bottom_action_view.gone()
        val emptyModel = EmptyModel()
        val imageUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.umrah_img_empty_search_png))
                .appendPath(resources.getResourceTypeName(R.drawable.umrah_img_empty_search_png))
                .appendPath(resources.getResourceEntryName(R.drawable.umrah_img_empty_search_png))
                .build()
        emptyModel.urlRes = imageUri.toString()
        emptyModel.title = getString(R.string.umrah_search_empty_title)

        if (!isFilter) {
            emptyModel.content = getString(R.string.umrah_search_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.umrah_search_empty_button)
        } else {
            emptyModel.content = getString(R.string.umrah_search_filter_empty_subtitle)
            emptyModel.buttonTitle = getString(R.string.umrah_search_filter_empty_button)
        }
        return emptyModel
    }

    override fun onEmptyButtonClicked() {
        if (!isFilter) activity?.onBackPressed()
        else openFilterFragment()
    }

    @SuppressLint("InflateParams")
    private fun initSortBottomSheets() {
        sortView = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_search_sort, null)
        sortBottomSheets = BottomSheetUnify()
        sortBottomSheets.setChild(sortView)
        sortBottomSheets.setTitle(getString(R.string.umrah_search_sort_by))
        sortBottomSheets.setCloseClickListener { sortBottomSheets.dismiss() }
    }

    private fun openSortBottomSheets() {
        if (isSortLoaded) populateSortOptions()
        else {
            observeSortData()
            loadSortData()
        }
        sortBottomSheets.show(fragmentManager, "TEST")
    }

    private fun initRVSort(options: List<UmrahOption>) {
        umrahSearchSortAdapter.addOptions(options)
        sortView.rv_umrah_search_sort.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_18), LinearLayoutManager.VERTICAL))
            adapter = umrahSearchSortAdapter
        }
    }

    private fun observeSortData() {
        umrahSearchFilterSortViewModel.umrahSearchParameter.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetSort(it.data)
                is Fail -> Fail(it.throwable)
            }
        })
    }

    private fun onSuccessGetSort(data: UmrahSearchParameterEntity) {
        sort = data.umrahSearchParameter.sortMethods
        initRVSort(sort.options)
        if (umrahSearchViewModel.getSortValue() == "") umrahSearchViewModel.setSortValue(sort.options[sort.defaultOption].query)
        populateSortOptions()
        isSortLoaded = true
    }

    private fun loadSortData() {
        val searchQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_home_page_search_parameter)
        umrahSearchFilterSortViewModel.getUmrahSearchParameter(searchQuery)
    }

    private fun populateSortOptions() {
        umrahSearchSortAdapter.apply {
            setSelectedOption(umrahSearchViewModel.getSortValue())
            listener = object : UmrahSearchSortAdapter.OnSortMenuSelected {
                override fun onSelect(option: UmrahOption) {
                    umrahTrackingUtil.umrahSearchNCategorySortClick(option.query, searchOrCategory)
                    umrahSearchViewModel.setSortValue(option.query)
                    arguments?.putString("EXTRA_SORT", option.query)
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(250)
                        sortBottomSheets.dismiss()
                        loadInitialData()
                    }
                }
            }
        }
    }

    companion object {
        var isFilter = false
        fun getInstance(categorySlugName: String, departureCityId: String, departurePeriod: String,
                        priceMin: Int, priceMax: Int, durationMin: Int,
                        durationMax: Int, defaultSort: String) =
                UmrahSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_CATEGORY_SLUG_NAME, categorySlugName)
                        putString(EXTRA_DEPARTURE_CITY_ID, departureCityId)
                        putString(EXTRA_DEPARTURE_PERIOD, departurePeriod)
                        putInt(EXTRA_PRICE_MIN, priceMin)
                        putInt(EXTRA_PRICE_MAX, priceMax)
                        putInt(EXTRA_DURATION_DAYS_MIN, durationMin)
                        putInt(EXTRA_DURATION_DAYS_MAX, durationMax)
                        putString(EXTRA_SORT, defaultSort)
                    }
                }
    }

    override fun onBackPressed() {
        if (!isDetached) {
            isFilter = false
            if (umrahSearchViewModel.getSlugName() != "") umrahTrackingUtil.umrahSearchNCategoryBackClick(SearchOrCategory.CATEGORY)
            else umrahTrackingUtil.umrahSearchNCategoryBackClick(SearchOrCategory.SEARCH)
        }
    }
}