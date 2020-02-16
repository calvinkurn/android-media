package com.tokopedia.brandlist.brandlist_search.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
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
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderInterface
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderItemDecoration
import com.tokopedia.brandlist.brandlist_search.data.mapper.BrandlistSearchMapper
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchModule
import com.tokopedia.brandlist.brandlist_search.di.DaggerBrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchResultAdapter
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchAllBrandLabelViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BrandlistSearchFragment: BaseDaggerFragment(),
        HasComponent<BrandlistSearchComponent>,
        BrandlistSearchTrackingListener,
        BrandlistSearchNotFoundViewHolder.Listener {

    companion object {
        const val BRANDLIST_SEARCH_GRID_SPAN_COUNT = 3

        @JvmStatic
        fun createInstance(): Fragment {
            return BrandlistSearchFragment().apply {
                arguments = Bundle().apply {

                }
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
    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadMoreBrands()
                if (adapterBrandSearch?.getVisitables()?.lastOrNull() is BrandlistSearchResultViewModel) {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_brandlist_search, container, false)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        layoutManager = GridLayoutManager(context, BRANDLIST_SEARCH_GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when(adapterBrandSearch?.getItemViewType(position)) {
                        BrandlistSearchHeaderViewHolder.LAYOUT -> 3
                        BrandlistSearchNotFoundViewHolder.LAYOUT -> 3
                        BrandlistSearchAllBrandLabelViewHolder.LAYOUT -> 3
                        else -> 1
                    }
                }
            }
        }
        val adapterTypeFactory = BrandlistSearchAdapterTypeFactory(this)
        adapterBrandSearch = BrandlistSearchResultAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapterBrandSearch
        recyclerView?.addItemDecoration(StickyHeaderItemDecoration(adapterBrandSearch as StickyHeaderInterface))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTotalBrands()
        searchView = view.findViewById(R.id.search_input_view)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        initView(view)
        observeSearchResultData()
        observeSearchRecommendationResultData()
        observeTotalBrands()
        observeAllBrands()
        recyclerView?.addOnScrollListener(endlessScrollListener)
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
    }

    private fun initView(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        toolbar = view.findViewById(R.id.toolbar)
        configToolbar(view)
        initSearchView()
    }

    private fun configToolbar(view: View){
        toolbar?.setNavigationIcon(com.tokopedia.brandlist.R.drawable.brandlist_icon_arrow_black)
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayShowTitleEnabled(false)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                    if (it.isNotEmpty()) {
                        val categoryId = 0
                        val offset = 0
                        val sortType = 1
                        val firstLetter = ""
                        val brandSize = 10
                        keywordSearch = it
                        viewModel.searchBrand(categoryId, offset, it,
                                brandSize, sortType, firstLetter)
                        adapterBrandSearch?.showShimmering()
                    }
                }
            }

        })
    }

    private fun observeSearchResultData() {
        viewModel.brandlistSearchResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val response = it.data.brands
                    if(response.isEmpty()) {
                        viewModel.searchRecommendation(
                                3045010,
                                categoryIds = "0")
                    } else {
                        adapterBrandSearch?.updateSearchResultData(
                                BrandlistSearchMapper.mapSearchResultResponseToVisitable(
                                        response, searchView?.searchText ?: "", this))
                    }
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeSearchRecommendationResultData() {
        viewModel.brandlistSearchRecommendationResponse.observe(this, Observer {
            when(it) {
                is Success -> {
                    val response = it.data.shops
                    adapterBrandSearch?.updateSearchRecommendationData(
                            BrandlistSearchMapper.mapSearchRecommendationResponseToVisitable(response, this))
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeTotalBrands() {
        viewModel.brandlistAllBrandTotal.observe(this, Observer {
            when(it) {
                is Success -> {
                    adapterBrandSearch?.updateAllBrandsValue(it.data)
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
            when(it) {
                is Success -> {
                    adapterBrandSearch?.hideLoading()
                    val response = it.data
                    endlessScrollListener.updateStateAfterGetData()
                    val currentOffset = viewModel.currentOffset
                    val groupHeader = viewModel.currentLetter.toUpperCase()

                    if (currentOffset == 0) {
                        adapterBrandSearch?.getVisitables()?.add(BrandlistSearchAllBrandLabelViewModel(groupHeader.toString()))
                        adapterBrandSearch?.notifyItemRangeInserted((adapterBrandSearch as BrandlistSearchResultAdapter).lastIndex, 1)
                    }
                    adapterBrandSearch?.updateBrands(BrandlistSearchMapper.mapSearchResultResponseToVisitable(response.brands, "", this))
                    viewModel.updateTotalBrandSize(response.totalBrands)
                    viewModel.updateCurrentOffset(response.brands.size)
                    viewModel.updateCurrentLetter()
                    viewModel.updateEndlessRequestParameter()
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun loadInitialData() {
        if (!isInitialDataLoaded) {
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

}