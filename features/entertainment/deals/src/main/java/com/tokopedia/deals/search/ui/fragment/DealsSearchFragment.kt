package com.tokopedia.deals.search.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.ui.customview.SelectLocationBottomSheet
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.deals.search.di.component.DealsSearchComponent
import com.tokopedia.deals.search.domain.viewmodel.DealsSearchViewModel
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.mapper.DealsSearchMapper
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.deals.search.model.response.InitialLoadData
import com.tokopedia.deals.search.model.response.Item
import com.tokopedia.deals.search.model.visitor.MoreBrandModel
import com.tokopedia.deals.search.model.visitor.VoucherModel
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactory
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactoryImpl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_deals_search.*
import kotlinx.android.synthetic.main.layout_deals_search_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DealsSearchFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        DealsSearchListener,
        CurrentLocationCallback,
        CoroutineScope {

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DealsSearchViewModel::class.java) }
    private val baseViewModel: DealsBaseViewModel by lazy { viewModelFragmentProvider.get(DealsBaseViewModel::class.java) }

    private var userTyped: Boolean = false
    private var currentLocation: Location? = null

    private var childCategoryIds: String? = null

    private var totalItem = 0
    private var searchNotFound = false
    private var initialDataList: ArrayList<Visitable<DealsSearchTypeFactory>> = arrayListOf()
    private var bottomSheet: SelectLocationBottomSheet? = null

    @Inject
    lateinit var analytics: DealsAnalytics

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(DealsSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deals_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childCategoryIds = arguments?.getString(DealsSearchConstants.CHILD_CATEGORY_IDS)
        onLocationUpdated()
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        tv_location?.setCompoundDrawablesWithIntrinsicBounds(null, null,
                MethodChecker.getDrawable(context, R.drawable.ic_deals_dropdown_down_24dp), null)
        initObserver()
        setListener()
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        viewModel.getInitialData(currentLocation, childCategoryIds)
    }

    private fun initObserver() {
        initialObserver()
        loadMoreObserver()
        searchObserver()
        observerNearestLocation()
    }

    private fun initialObserver() {
        viewModel.dealsInitialResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    initialDataList = DealsSearchMapper.displayInitialDataSearch(it.data, currentLocation?.name
                            ?: DealsLocationUtils.DEFAULT_LOCATION_NAME)
                    DealsSearchMapper.initialProductList = it.data.eventSearch.products
                    totalItem = it.data.eventSearch.products.size
                    if (totalItem >= THRESHOLD_ITEMS) {
                        renderList(initialDataList.toList(), true)
                    } else {
                        renderList(initialDataList.toList(), false)
                    }
                    chipsAnalytics(it.data)
                }
                is Fail -> {
                    createToaster(getString(R.string.deals_search_load_initial_error), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun chipsAnalytics(data: InitialLoadData) {
        if (data.eventChildCategory.categories.isNotEmpty()) {
            analytics.eventViewChipsSearchPage()
        }

        if (data.travelCollectiveRecentSearches.items.isNotEmpty()) {
            if (isAnalyticsInitialized) {
                analytics.eventViewLastSeenSearchPage()
            }
        }
    }

    private fun loadMoreObserver() {
        viewModel.dealsLoadMoreResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    totalItem = it.data.products.size
                    if (totalItem >= THRESHOLD_ITEMS) {
                        loadMore(DealsSearchMapper.displayMoreData(it.data, currentPage), true)
                    } else {
                        loadMore(DealsSearchMapper.displayMoreData(it.data, currentPage), false)
                    }
                }
                is Fail -> {
                    createToaster(getString(R.string.deals_search_load_more_error), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun searchObserver() {
        viewModel.dealsSearchResponse.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    clearAllData()
                    totalItem = it.data.products.size

                    if (it.data.products.isNotEmpty()) {
                        analytics.eventSearchResultCaseShownOnCategoryPage(getSearchKeyword(), currentLocation?.name
                                ?: "", it.data.products, THRESHOLD_ITEMS, currentPage)
                    }

                    if (totalItem >= THRESHOLD_ITEMS) {
                        renderList(DealsSearchMapper.displayDataSearchResult(it.data,
                                currentLocation?.name
                                        ?: DealsLocationUtils.DEFAULT_LOCATION_NAME, getSearchKeyword()),
                                true)
                    } else {
                        if (totalItem == 0) {
                            searchNotFound = true
                            analytics.eventViewSearchNoResultSearchPage(getSearchKeyword(), currentLocation?.name
                                    ?: "")
                        }
                        renderList(DealsSearchMapper.displayDataSearchResult(it.data,
                                currentLocation?.name
                                        ?: DealsLocationUtils.DEFAULT_LOCATION_NAME, getSearchKeyword()),
                                searchNotFound)
                    }
                }
                is Fail -> {
                    createToaster(getString(R.string.deals_search_error), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observerNearestLocation() {
        baseViewModel.observableCurrentLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val currentLocation = Location()
            currentLocation.id = it.id
            currentLocation.name = it.name
            currentLocation.address = it.address
            currentLocation.coordinates = it.coordinates
            currentLocation.searchName = it.searchName
            currentLocation.cityId = it.cityId
            currentLocation.cityName = it.cityName

            dealsLocationUtils.updateLocation(currentLocation)
            setChangedLocation()
        })
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return DealsSearchTypeFactoryImpl(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_search_results

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        var keyword = getSearchKeyword()
        if (searchNotFound) {
            keyword = ""
        }
        viewModel.loadMoreData(keyword, currentLocation, childCategoryIds, page)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    private fun getSearchKeyword(): String {
        var query = ""
        if (search_bar?.searchBarTextField?.text?.isNotEmpty() == true) {
            query = search_bar?.searchBarTextField?.text.toString()
        }
        return query
    }

    private fun setListener() {
        setSearchBarListener()
        iv_button_back?.setOnClickListener { activity?.onBackPressed() }
        tv_location?.setOnClickListener {
            bottomSheet = SelectLocationBottomSheet(currentLocation, false, this)
            fragmentManager?.let { fm -> bottomSheet?.show(fm, BOTTOM_SHEET_TAG) }
        }
    }

    private fun setSearchBarListener() {
        search_bar?.requestFocus()
        search_bar?.searchBarTextField?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            KeyboardHandler.showSoftKeyboard(activity)
        }

        search_bar?.searchBarTextField?.afterTextChangedDelayed {
            onSearchTextChanged(it)
        }

        search_bar?.searchBarTextField?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                onSearchSubmitted(search_bar?.searchBarTextField?.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun onSearchSubmitted(text: String) {
        KeyboardHandler.hideSoftKeyboard(activity)
        viewModel.searchDeals(text, currentLocation, childCategoryIds, FIRST_PAGE)
    }

    private fun onSearchTextChanged(text: String) {
        viewModel.searchDeals(text, currentLocation, childCategoryIds, FIRST_PAGE)
    }

    private fun loadMore(list: List<Visitable<DealsSearchTypeFactory>>, hasNextPage: Boolean) {
        hideLoading()
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }
        adapter.addMoreData(list)
        updateScrollListenerState(hasNextPage)

        if (isListEmpty) {
            showEmpty()
        } else {
            isLoadingInitialData = false
        }
    }

    private fun getLocation(): Location? = dealsLocationUtils.getLocation()

    private fun onLocationUpdated(): String {
        val location = getLocation()
        if (location != null) {
            createToaster(String.format("%s %s", getString(R.string.deals_location_deals_changed_toast), location.name), Toaster.TYPE_NORMAL)
            currentLocation = location
            tv_location?.text = location.name
            return location.name
        }
        return ""
    }

    private fun createToaster(textBody: String, typeToaster: Int) {
        main_content?.let { Toaster.build(it, textBody, Snackbar.LENGTH_LONG, typeToaster).show() }
    }

    private fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText

                launch {
                    delay(DealsSearchConstants.DELAY)
                    if (searchText != searchFor) {
                        return@launch
                    } else {
                        userTyped = searchText.isEmpty() != true
                        searchNotFound = false
                        if (!userTyped) {
                            clearAllData()
                            renderList(initialDataList.toList(), true)
                        } else {
                            afterTextChanged.invoke(searchText)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = kotlin.Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = kotlin.Unit
        })
    }

    private fun onLocationItemUpdated() {
        analyticsLocation()
        search_bar.searchBarTextField.text.clear()
        loadInitialData()
    }

    private fun analyticsLocation() {
        val oldLocation = currentLocation?.name ?: ""
        val newLocation = onLocationUpdated()

        if (oldLocation != newLocation) {
            analytics.eventClickChangeLocationSearchPage(oldLocation, newLocation)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onVoucherClicked(itemView: View, voucher: VoucherModel) {
        itemView.setOnClickListener {
            analytics.eventClickSearchResultProductSearchPage(voucher)
            RouteManager.route(context, voucher.appUrl)
        }
    }

    override fun onBrandClicked(itemView: View, brand: Brand, position: Int) {
        itemView.setOnClickListener {
            analytics.eventClickSearchResultBrandSearchPage(brand, position)
            val brandApplink = DealsSearchConstants.PREFIX_DEALS_APPPLINK + DealsSearchConstants.PREFIX_APPLINK_BRAND + brand.seoUrl
            RouteManager.route(context, brandApplink)
        }
    }

    override fun onMoreBrandClicked(itemView: View, moreBrandModel: MoreBrandModel) {
        itemView.setOnClickListener {
            startActivityForResult(DealsBrandActivity.getCallingIntent(requireContext(), getSearchKeyword()), DEALS_BRAND_REQUEST_CODE)
        }
    }

    override fun onCuratedChipClicked(itemView: View, curated: Category) {
        itemView.setOnClickListener {
            if (isAnalyticsInitialized) {
                analytics.eventClickChipsSearchPage(curated.title)
            }
            RouteManager.route(itemView.context, curated.appUrl)
        }
    }

    override fun onLastSeenClicked(itemView: View, lastSeen: Item) {
        itemView.setOnClickListener {
            if (isAnalyticsInitialized) {
                analytics.eventClickLastSeenSearchPage(lastSeen.title)
            }
            RouteManager.route(itemView.context, lastSeen.appURL)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DEALS_BRAND_REQUEST_CODE -> changeLocationFromPreviousPage()
        }
    }

    private fun changeLocationFromPreviousPage() {
        val previousLocation = dealsLocationUtils.getLocation()
        if (currentLocation?.coordinates != previousLocation.coordinates) {
            currentLocation = previousLocation
            tv_location?.text = currentLocation?.name
            viewModel.searchDeals(getSearchKeyword(), currentLocation, childCategoryIds, FIRST_PAGE)
        }
    }

    override fun setCurrentLocation(location: Location) {
        if (location.name.isEmpty()) {
            baseViewModel.getCurrentLocation(location.coordinates)
        } else {
            baseViewModel.setCurrentLocation(location)
        }
    }

    override fun setChangedLocation() {
        onLocationItemUpdated()
        bottomSheet?.dismiss()
    }

    private val isAnalyticsInitialized: Boolean
        get() = this::analytics.isInitialized

    companion object {
        const val SCREEN_NAME = "deals search"
        const val TAG = "DealsSearchFragment"
        const val BOTTOM_SHEET_TAG = "DealsSearchLocationBottomSheet"
        private const val DEALS_BRAND_REQUEST_CODE = 1
        private const val FIRST_PAGE = 1
        private const val THRESHOLD_ITEMS = 20

        fun createInstance(childCategoryIds: String?): DealsSearchFragment {
            val fragment = DealsSearchFragment()
            val bundle = Bundle()
            bundle.putString(DealsSearchConstants.CHILD_CATEGORY_IDS, childCategoryIds)
            fragment.arguments = bundle
            return fragment
        }
    }
}