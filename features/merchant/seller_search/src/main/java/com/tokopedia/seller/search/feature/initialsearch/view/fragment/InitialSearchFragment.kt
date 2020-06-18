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
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SearchSellerAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.InitialSearchAdapterTypeFactory
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSearchFragment: BaseListFragment<Visitable<*>, InitialSearchAdapterTypeFactory>(), HistorySearchListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val searchSellerAdapterTypeFactory by lazy {
        InitialSearchAdapterTypeFactory(this)
    }

    private val searchSellerAdapter: SearchSellerAdapter
        get() = adapter as SearchSellerAdapter

    private var viewModel: InitialSearchViewModel? = null

    private var searchSeller: SellerSearchUiModel? = null

    private var searchKeyword = ""
    private var shopId = ""

    private var historyViewUpdateListener: HistoryViewUpdateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId.orEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(InitialSearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initial_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        observeLiveData()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun loadInitialData() {
        super.loadInitialData()
        viewModel?.getSellerSearch(keyword = searchKeyword, shopId = shopId)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, InitialSearchAdapterTypeFactory> {
        return SearchSellerAdapter(searchSellerAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): InitialSearchAdapterTypeFactory = searchSellerAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvSearchHistorySeller)
    }

    override fun onDestroy() {
        viewModel?.getSellerSearch?.removeObservers(this)
        viewModel?.deleteHistorySearch?.removeObservers(this)
        viewModel?.flush()
        super.onDestroy()
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view).run {
            clearOnScrollListeners()
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClearSearchItem(keyword: String) {
        viewModel?.deleteSuggestionSearch(listOf(keyword))
    }

    override fun onClearAllSearch() {
        viewModel?.deleteSuggestionSearch(searchSeller?.titleList ?: listOf())
    }

    override fun onHistoryItemClicked(appUrl: String) {
        startActivityFromAutoComplete(appUrl)
        dropKeyBoard()
    }

    private fun observeLiveData() {
       viewModel?.getSellerSearch?.observe(this, Observer {
           when(it) {
               is Success -> {
                   setHistorySearch(it.data)
               }
               is Fail -> { }
           }
       })

        viewModel?.deleteHistorySearch?.observe(this, Observer {
            when(it) {
                is Success -> {
                    viewModel?.getSellerSearch(keyword = searchKeyword, shopId = shopId)
                }
                is Fail -> { }
            }
        })
    }

    private fun setHistorySearch(data: List<SellerSearchUiModel>) {
        if(data.isEmpty()) {
            searchSellerAdapter.removeEmptyOrErrorState()
            searchSellerAdapter.addSellerSearchNoHistory()
        } else {
            searchSellerAdapter.removeEmptyOrErrorState()
            searchSellerAdapter.setSellerSearchListData(data)
        }
        historyViewUpdateListener?.showHistoryView()
    }

    fun setHistoryViewUpdateListener(historyViewUpdateListener: HistoryViewUpdateListener) {
        this.historyViewUpdateListener = historyViewUpdateListener
    }

    fun historySearch(keyword: String) {
        this.searchKeyword = keyword
        loadInitialData()
    }

    fun setSearchKeyword(keyword: String) {
        this.searchKeyword = keyword
    }

     fun onMinCharState() {
        searchSellerAdapter.removeEmptyOrErrorState()
        searchSellerAdapter.addSellerSearchMinChar()
    }

    private fun dropKeyBoard() {
        if (activity != null && activity is InitialSellerSearchActivity) {
            (activity as InitialSellerSearchActivity).dropKeyboardHistory()
        }
    }

    private fun startActivityFromAutoComplete(appLink: String) {
        if (activity == null) return

        RouteManager.route(activity, appLink)
        activity?.finish()
    }
}
