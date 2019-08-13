package com.tokopedia.search.result.presentation.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.viewmodel.shop.SearchShopViewModel
import com.tokopedia.search.utils.State

class ShopListFragmentKt: BaseDaggerFragment() {

    companion object {
        private const val SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab"

        @JvmStatic
        fun newInstance(): ShopListFragmentKt {
            return ShopListFragmentKt()
        }
    }

    private var gridLayoutManager: GridLayoutManager? = null
    private var gridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_list_search, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeViewModels() {
        activity?.let { activity ->
            val searchShopViewModel = ViewModelProviders.of(activity).get(SearchShopViewModel::class.java)

            gridLayoutManager = GridLayoutManager(activity, 1)

            refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
            refreshLayout?.setOnRefreshListener {
                refreshLayout?.isRefreshing = true
                reloadData(searchShopViewModel)
            }

            gridLayoutLoadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (userVisibleHint) {
                        searchMoreShop(searchShopViewModel)
                    }
                }
            }

            searchShop(searchShopViewModel)

            searchShopViewModel.getSearchShopLiveData().observe(viewLifecycleOwner, Observer {
                updateAdapter(it)
            })
        }
    }

    private fun searchShop(searchShopViewModel: SearchShopViewModel) {
        searchShopViewModel.searchShop()
    }

    private fun searchMoreShop(searchShopViewModel: SearchShopViewModel) {
        searchShopViewModel.searchMoreShop()
    }

    private fun reloadData(searchShopViewModel: SearchShopViewModel) {

    }

    private fun updateAdapter(searchShopLiveData: State<List<Visitable<*>>>?) {
        when(searchShopLiveData) {
            is State.Loading -> {

            }
            is State.Success -> {

            }
            is State.Error -> {

            }
        }
    }

    override fun getScreenName(): String {
        return SCREEN_SEARCH_PAGE_SHOP_TAB
    }

    override fun initInjector() {

    }
}