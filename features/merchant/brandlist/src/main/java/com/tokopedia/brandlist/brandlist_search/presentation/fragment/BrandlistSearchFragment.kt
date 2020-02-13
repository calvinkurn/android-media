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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.data.mapper.BrandlistSearchMapper
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchModule
import com.tokopedia.brandlist.brandlist_search.di.DaggerBrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchResultAdapter
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchNotFoundViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchRecommendationTextViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchRecommendationViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchResultViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchRecommendationViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BrandlistSearchFragment: BaseDaggerFragment(),
        HasComponent<BrandlistSearchComponent> {

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
    lateinit var recommendationViewModel: BrandlistSearchRecommendationViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var searchView: SearchInputView? = null
    private var statusBar: View? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var adapterBrandSearch: BrandlistSearchResultAdapter? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_brandlist_search, container, false)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        layoutManager = GridLayoutManager(context, BRANDLIST_SEARCH_GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when(adapterBrandSearch?.getItemViewType(position)) {
                        BrandlistSearchResultViewHolder.LAYOUT -> 1
                        else -> 3
                    }
                }
            }
        }
        val adapterTypeFactory = BrandlistSearchAdapterTypeFactory()
        adapterBrandSearch = BrandlistSearchResultAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapterBrandSearch

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.search_input_view)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        initView(view)
        observeSearchResultData()
        observeSearchRecommendationResultData()
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
        viewModel.flush()
        super.onDestroy()
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
                println(text)
                text?.let {
                    if (it.isNotEmpty()) {
                        val categoryId = 0
                        val offset = 0
                        val sortType = 1
                        val firstLetter = ""
                        val brandSize = 10
                        viewModel.searchBrand(categoryId, offset, it,
                                brandSize, sortType, firstLetter)
                    }
                }
            }

        })
    }

    private fun observeSearchResultData() {
        viewModel.brandlistSearchResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val response = it.data.officialStoreAllBrands
                    if(response.brands.isEmpty()) {
                        recommendationViewModel.searchRecommendation(
                                userSession.userId.toIntOrNull(),
                                categoryIds = "0")
                    } else {
                        adapterBrandSearch?.updateSearchResultData(BrandlistSearchMapper.mapSearchResultResponseToVisitable(response.brands))
                    }
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun observeSearchRecommendationResultData() {
        recommendationViewModel.brandlistSearchRecommendationResponse.observe(this, Observer {
            when(it) {
                is Success -> {
                    val response = it.data.officialStoreBrandsRecommendation.shops
                    adapterBrandSearch?.updateSearchRecommendationData(BrandlistSearchMapper.mapSearchRecommendationResponseToVisitable(response))
                }
                is Fail -> {
                    showErrorNetwork(it.throwable)
                }
            }
        })
    }

    private fun showErrorNetwork(t: Throwable) {
        view?.let {
            Toaster.showError(it, ErrorHandler.getErrorMessage(context, t), Snackbar.LENGTH_LONG)
        }
    }

}