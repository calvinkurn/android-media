package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SearchSellerAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialSearchFragment: BaseListFragment<Visitable<*>, InitialSearchAdapterTypeFactory>(), HistorySearchListener, OrderSearchListener, ProductSearchListener, NavigationSearchListener {

    companion object {
        const val MIN_CHARACTER_SEARCH = 3
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewModel: InitialSearchViewModel? = null

    private val searchSellerAdapterTypeFactory by lazy {
        InitialSearchAdapterTypeFactory(this, this, this, this )
    }

    private val searchSellerAdapter: SearchSellerAdapter
        get() = adapter as SearchSellerAdapter

    private var searchKeyword = ""
    private var shopId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InitialSearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initial_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun loadInitialData() {
        super.loadInitialData()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, InitialSearchAdapterTypeFactory> {
        return SearchSellerAdapter(searchSellerAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): InitialSearchAdapterTypeFactory = searchSellerAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvSearchSeller)
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view).run {
            clearOnScrollListeners()
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClearSearchItem(keyword: String) {

    }

    override fun onClearAllSearch() {

    }

    override fun onHistoryItemClicked(appUrl: String) {

    }

    override fun onNavigationItemClicked(appUrl: String) {

    }

    override fun onOrderItemClicked(appUrl: String) {

    }

    override fun onProductItemClicked(appUrl: String) {

    }

}
