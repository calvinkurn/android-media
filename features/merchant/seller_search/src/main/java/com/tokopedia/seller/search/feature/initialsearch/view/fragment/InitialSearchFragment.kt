package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.SearchSellerAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel

class InitialSearchFragment: BaseDaggerFragment(), HistorySearchListener, OrderSearchListener, ProductSearchListener, NavigationSearchListener {

    private var viewModel: InitialSearchViewModel? = null

    private val adapterTypeFactory by lazy {
        InitialSearchAdapterTypeFactory(this, this, this, this )
    }

    private val searchSellerAdapter by lazy {
        SearchSellerAdapter(adapterTypeFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initial_search_fragment, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InitialSearchViewModel::class.java)
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
