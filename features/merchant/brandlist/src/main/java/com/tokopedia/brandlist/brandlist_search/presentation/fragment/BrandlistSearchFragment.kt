package com.tokopedia.brandlist.brandlist_search.presentation.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.analytic.BrandlistTracking
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment.Companion.CATEGORY_INTENT
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.MarginItemDecoration
import com.tokopedia.brandlist.brandlist_search.data.mapper.BrandlistSearchMapper
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchModule
import com.tokopedia.brandlist.brandlist_search.di.DaggerBrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchResultAdapter
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultUiModel
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import com.tokopedia.brandlist.common.Constant
import com.tokopedia.brandlist.common.LoadAllBrandState
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BrandlistSearchFragment : BaseDaggerFragment(),
        HasComponent<BrandlistSearchComponent>,
        BrandlistSearchTrackingListener,
        BrandlistSearchNotFoundViewHolder.Listener,
        BrandlistSearchRecommendationNotFoundViewHolder.Listener,
        BrandlistHeaderBrandInterface {

    companion object {
        const val BRANDLIST_SEARCH_GRID_SPAN_COUNT = 3

        @JvmStatic
        fun createInstance(category: Category?) = BrandlistSearchFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CATEGORY_INTENT, category)
            }
        }
    }

    @Inject
    lateinit var viewModel: BrandlistSearchViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var brandlistTracking: BrandlistTracking? = null
    private var searchView: SearchInputView? = null
    private var statusBar: View? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var adapterBrandSearch: BrandlistSearchResultAdapter? = null
    private var toolbar: Toolbar? = null
    private var keywordSearch = ""
    private var categoryName = ""
    private var categoryData: Category? = null
    private val INITIAL_OFFSET = 0
    private val ALL_BRANDS_REQUEST_SIZE = 30
    private val ALPHABETIC_ASC_SORT = 3

    private var selectedChip: Int = Constant.DEFAULT_SELECTED_CHIPS
    private var isLoadMore: Boolean = false
    private var stateLoadBrands: String = LoadAllBrandState.LOAD_INITIAL_ALL_BRAND
    private var selectedBrandLetter: String = "A"
    private val defaultBrandLetter: String = ""
    private var totalBrandsNumber: Int = 0
    private var recyclerViewLastState: Parcelable? = null
    private var lastTimeChipIsClicked: Long = 0L

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val brandFirstLetter: String = if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) selectedBrandLetter else ""
                viewModel.loadMoreBrands(brandFirstLetter)
                isLoadMore = true

                if (adapterBrandSearch?.getVisitables()?.lastOrNull() is BrandlistSearchResultUiModel) {
                    adapterBrandSearch?.showLoading()
                }
            }
        }
    }
    private var isInitialDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            brandlistTracking = BrandlistTracking(it)
        }
        arguments?.let {
            categoryData = it.getParcelable(CATEGORY_INTENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_brandlist_search, container, false)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        layoutManager = GridLayoutManager(context, BRANDLIST_SEARCH_GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapterBrandSearch?.getItemViewType(position)) {
                        BrandlistSearchRecommendationViewHolder.LAYOUT -> 1
                        BrandlistSearchShimmeringViewHolder.LAYOUT -> 1
                        BrandlistSearchResultViewHolder.LAYOUT -> 1
                        else -> 3
                    }
                }
            }
        }
        val adapterTypeFactory = BrandlistSearchAdapterTypeFactory(this, this)
        adapterBrandSearch = BrandlistSearchResultAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapterBrandSearch
        recyclerView?.addItemDecoration(MarginItemDecoration(resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTotalBrands()
        searchView = view.findViewById(R.id.search_input_view)
        searchView?.requestFocus()
        searchView?.setOnClickListener {
            searchView?.requestFocus()
        }
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        initView(view)
        observeSearchResultData()
        observeSearchRecommendationResultData()
        observeTotalBrands()
        observeAllBrands()
        recyclerView?.addOnScrollListener(endlessScrollListener)
        recyclerView?.setOnTouchListener { _, _ ->
            searchView?.hideKeyboard()
            false
        }
    }

    override fun getComponent(): BrandlistSearchComponent? {
        return activity?.run {
            DaggerBrandlistSearchComponent
                    .builder()
                    .brandlistSearchModule(BrandlistSearchModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onDestroy() {
        viewModel.brandlistSearchResponse.removeObservers(this)
        viewModel.brandlistAllBrandTotal.removeObservers(this)
        viewModel.brandlistAllBrandsSearchResponse.removeObservers(this)
        viewModel.brandlistSearchRecommendationResponse.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun focusSearchView() {
        searchView?.requestFocus()
        searchView?.searchText = ""
    }

    private fun initView(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        toolbar = view.findViewById(R.id.toolbar)
        configToolbar()
        initSearchView()
    }

    private fun configToolbar() {
        toolbar?.setNavigationIcon(R.drawable.brandlist_icon_arrow_black)
        activity?.let {
            (it as? AppCompatActivity)?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(toolbar)
                appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
                appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initSearchView() {
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String?) {
                text?.let {
                    categoryData?.let {
                        brandlistTracking?.clickSearchBox(
                                it.title, text.toString(), false)
                    }
                    if (it.isNotEmpty()) {
                        val offset = 0
                        val firstLetter = ""
                        val brandSize = 10
                        keywordSearch = it
                        viewModel.searchBrand(offset, it,
                                brandSize, firstLetter)
                        adapterBrandSearch?.showShimmering()
                    } else {
                        isInitialDataLoaded = false
                        viewModel.resetParams()
                        viewModel.getTotalBrands()
                        recyclerView?.addOnScrollListener(endlessScrollListener)
                    }
                }
            }

        })
    }

    private fun observeSearchResultData() {
        val userId = if (userSession.userId.isEmpty()) "0" else userSession.userId
        viewModel.brandlistSearchResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val response = it.data.brands
                    if (response.isEmpty()) {
                        viewModel.searchRecommendation(
                                userId,
                                categoryData?.categories)
                    } else {
                        adapterBrandSearch?.updateSearchResultData(
                                BrandlistSearchMapper.mapSearchResultResponseToVisitable(
                                        response, searchView?.searchText ?: "", this))
                    }
                    recyclerView?.removeOnScrollListener(endlessScrollListener)
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeSearchRecommendationResultData() {
        viewModel.brandlistSearchRecommendationResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val response = it.data.shops
                    adapterBrandSearch?.updateSearchRecommendationData(
                            BrandlistSearchMapper.mapSearchRecommendationResponseToVisitable(response, this)
                    )
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeTotalBrands() {
        viewModel.brandlistAllBrandTotal.observe(this, Observer {
            when (it) {
                is Success -> {
                    totalBrandsNumber = it.data
                    loadInitialData()
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeAllBrands() {
        viewModel.brandlistAllBrandsSearchResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    adapterBrandSearch?.hideLoading()
                    val response = it.data
                    endlessScrollListener.updateStateAfterGetData()

                    val totalBrandPerCharacter = it.data.totalBrands
                    val totalBrandsFiltered = if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND ||
                            stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) totalBrandsNumber else it.data.totalBrands

                    val existingTotalBrands: Int = viewModel.getTotalBrandSizeForChipHeader()
                    if (!isLoadMore && existingTotalBrands !== totalBrandsFiltered) {
                        adapterBrandSearch?.updateHeaderChipsBrandSearch(this, totalBrandsFiltered, selectedChip, lastTimeChipIsClicked, recyclerViewLastState)
                    }

                    var _brandlistSearchMapperResult: List<BrandlistSearchResultUiModel> = listOf()
                    if (totalBrandPerCharacter == 0) {
                        adapterBrandSearch?.mappingBrandSearchNotFound(
                                _brandlistSearchMapperResult,
                                isLoadMore)
                    } else {
                        _brandlistSearchMapperResult = BrandlistSearchMapper.mapSearchResultResponseToVisitable(
                                response.brands, "", this)
                        adapterBrandSearch?.updateBrands(_brandlistSearchMapperResult, stateLoadBrands, isLoadMore)
                    }

                    viewModel.updateTotalBrandSizeForChipHeader(response.totalBrands)
                    viewModel.updateTotalBrandSize(response.totalBrands)
                    viewModel.updateCurrentOffset(response.brands.size)
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun setStateLoadBrands(stateLoadData: String) {
        stateLoadBrands = stateLoadData
    }

    private fun loadInitialData() {
        if (!isInitialDataLoaded) {
            isLoadMore = false
            viewModel.loadInitialBrands()
            isInitialDataLoaded = true
        }
    }

    private fun showErrorNetwork(t: Throwable) {
        view?.let {
            Toaster.showError(it, ErrorHandler.getErrorMessage(context, t), Snackbar.LENGTH_LONG)
        }
    }

    override fun clickBrandOnSearchBox(shopId: String) {
        val isLogin = userSession.isLoggedIn
        val optionalParam = "optional parameter"
        brandlistTracking?.clickBrandOnSearchBox(categoryName, optionalParam, isLogin, keywordSearch, shopId)
    }

    override fun impressionBrand(shopId: String, shoplogoPosition: String,
                                 shopName: String, imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.impressionBrand(isLogin, shopId, categoryName, shoplogoPosition, shopName,
                imgUrl, true, keywordSearch)
    }

    override fun clickBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrand(
                isLogin, shopId, categoryName, shoplogoPosition, shopName,
                imgUrl, true, keywordSearch)
    }

    override fun onClickedChip(position: Int, chipName: String, current: Long, recyclerViewState: Parcelable?) {
        selectedChip = position
        recyclerViewLastState = recyclerViewState
        lastTimeChipIsClicked = current

        if (position > 0 && position < 2) {     // Load Semua Brand
            isLoadMore = false
            selectedBrandLetter = defaultBrandLetter
            setStateLoadBrands(LoadAllBrandState.LOAD_ALL_BRAND)
            viewModel.resetParams()
            isInitialDataLoaded = false
            loadInitialData()
        } else if (position >= 2) {     // Load per alphabet
            isLoadMore = false
            selectedBrandLetter = chipName
            setStateLoadBrands(LoadAllBrandState.LOAD_BRAND_PER_ALPHABET)
            viewModel.resetParams()
            viewModel.searchAllBrands(
                    INITIAL_OFFSET, "",
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT, chipName)
            adapterBrandSearch?.refreshSticky()
        }
    }

    override fun onClickSearchButton() {

    }

}