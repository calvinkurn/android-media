package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ALL
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.FilterSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SearchSellerAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.filter.FilterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.SuggestionSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SuggestionSearchFragment: BaseListFragment<Visitable<*>, InitialSearchAdapterTypeFactory>(),
        FilterSearchListener, ProductSearchListener, OrderSearchListener, NavigationSearchListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val filterAdapter by lazy { FilterSearchAdapter(this) }

    private val searchSellerAdapterTypeFactory by lazy {
        InitialSearchAdapterTypeFactory(this, this, this)
    }

    private val searchSellerAdapter: SearchSellerAdapter
        get() = adapter as SearchSellerAdapter

    private var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null

    private var viewModel: SuggestionSearchViewModel? = null

    private var searchKeyword = ""
    private var shopId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SuggestionSearchViewModel::class.java)
        shopId = userSession.shopId.orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.suggestion_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        observeLiveData()
    }

    override fun onDestroy() {
        viewModel?.getSellerSearch?.removeObservers(this)
        viewModel?.insertSuccessSearch?.removeObservers(this)
        viewModel?.flush()
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, InitialSearchAdapterTypeFactory> {
        return SearchSellerAdapter(searchSellerAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): InitialSearchAdapterTypeFactory = searchSellerAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun loadInitialData() {
        super.loadInitialData()
        viewModel?.getSellerSearch(keyword = searchKeyword, shopId = shopId)
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvSearchSuggestionSeller)
    }

    private fun observeLiveData() {
        viewModel?.getSellerSearch?.observe(this, Observer {
            when(it) {
                is Success -> {
                    setSuggestionSearch(it.data.first, it.data.second)
                }
                is Fail -> { }
            }
        })

        viewModel?.insertSuccessSearch?.observe(this, Observer {
            when(it) {
                is Success -> {
                    dropKeyBoard()
                }
                is Fail -> {
                    dropKeyBoard()
                }
            }
        })
    }

    private fun setSuggestionSearch(data: List<SellerSearchUiModel>, filterList: List<FilterSearchUiModel>) {
        if(data.isEmpty()) {
            searchSellerAdapter.removeEmptyOrErrorState()
            searchSellerAdapter.addSellerSearchNoResult()
        } else {
            searchSellerAdapter.removeEmptyOrErrorState()
            filterAdapter.setFilterSearch(filterList)
            searchSellerAdapter.setSellerSearchListData(data)
        }
        suggestionViewUpdateListener?.showSuggestionView()
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view).run {
            clearOnScrollListeners()
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setSuggestionViewUpdateListener(suggestionViewUpdateListener: SuggestionViewUpdateListener) {
        this.suggestionViewUpdateListener = suggestionViewUpdateListener
    }

    fun suggestionSearch(keyword: String) {
        this.searchKeyword = keyword
        loadInitialData()
    }

    fun setSearchKeyword(keyword: String) {
        this.searchKeyword = keyword
    }

    private fun dropKeyBoard() {
        if (activity != null && activity is InitialSellerSearchActivity) {
            (activity as InitialSellerSearchActivity).dropKeyboardSuggestion()
        }
    }

    private fun startActivityFromAutoComplete(appLink: String) {
        if (activity == null) return

        RouteManager.route(activity, appLink)
        activity?.finish()
    }

    override fun onFilterItemClicked(title: String, chipType: String, position: Int) {
        val section = if(title == ALL) "" else title
        filterAdapter.updatedSortFilter(position)
        viewModel?.getSellerSearch(searchKeyword, section, userSession.shopId.orEmpty())
    }

    override fun onNavigationItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel?.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

    override fun onOrderItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel?.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()    }

    override fun onProductItemClicked(data: ItemSellerSearchUiModel, position: Int) {
        viewModel?.insertSearchSeller(searchKeyword, data.id.orEmpty(), data.title.orEmpty(), position)
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        dropKeyBoard()
    }

}
