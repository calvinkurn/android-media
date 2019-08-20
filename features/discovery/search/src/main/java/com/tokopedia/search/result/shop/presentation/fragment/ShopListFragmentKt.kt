package com.tokopedia.search.result.shop.presentation.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.discovery.common.data.Option
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ShopListItemDecoration
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactoryImpl
import com.tokopedia.search.result.shop.presentation.adapter.ShopListAdapter
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel
import com.tokopedia.search.utils.State
import com.tokopedia.topads.sdk.domain.model.CpmData

class ShopListFragmentKt:
        BaseDaggerFragment(),
        ShopListener,
        BannerAdsListener {

    companion object {
        private const val SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab"

        @JvmStatic
        fun newInstance(): ShopListFragmentKt {
            return ShopListFragmentKt()
        }
    }

    private var gridLayoutManager: GridLayoutManager? = null
    private var shopListAdapter: ShopListAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var gridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var searchShopViewModel: SearchShopViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_list_search, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        observeViewModelData()
    }

    private fun initViewModel() {
        activity?.let {
            searchShopViewModel = ViewModelProviders.of(it).get(SearchShopViewModel::class.java)
        }
    }

    private fun initViews() {
        initRefreshLayout()
        initLoadMoreListener()
        initRecyclerView()
    }

    private fun initRefreshLayout() {
        refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        refreshLayout?.setOnRefreshListener {
            showRefreshLayout()
            reloadSearchShop()
        }
    }

    private fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    private fun initLoadMoreListener() {
        gridLayoutLoadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (userVisibleHint) {
                    searchMoreShop()
                }
            }
        }
    }

    private fun initRecyclerView() {
        activity?.let { activity ->
            recyclerView = view?.findViewById(R.id.recyclerview)
            recyclerView?.layoutManager = createGridLayoutManager(activity)
            recyclerView?.adapter = createShopListAdapter()
            recyclerView?.addItemDecoration(createShopItemDecoration(activity))

            gridLayoutLoadMoreTriggerListener?.let {
                recyclerView?.addOnScrollListener(it)
            }
        }
    }

    private fun createGridLayoutManager(activity: Activity): RecyclerView.LayoutManager {
        return GridLayoutManager(activity, 1)
    }

    private fun createShopListAdapter(): RecyclerView.Adapter<AbstractViewHolder<*>> {
        val shopListTypeFactory = createShopListTypeFactory()
        return ShopListAdapter(shopListTypeFactory)
    }

    private fun createShopListTypeFactory(): ShopListTypeFactory {
        return ShopListTypeFactoryImpl(this, getDummyEmptyStateListener(), this)
    }

    private fun createShopItemDecoration(activity: Activity): RecyclerView.ItemDecoration {
        return ShopListItemDecoration(
                    activity.resources.getDimensionPixelSize(R.dimen.dp_16),
                    activity.resources.getDimensionPixelSize(R.dimen.dp_16),
                    activity.resources.getDimensionPixelSize(R.dimen.dp_16),
                    activity.resources.getDimensionPixelSize(R.dimen.dp_16)
            )
    }

    private fun observeViewModelData() {
        searchShopViewModel?.getSearchShopLiveData()?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            searchShop()
        }
    }

    private fun searchShop() {
        searchShopViewModel?.searchShop()
    }

    private fun searchMoreShop() {
        searchShopViewModel?.searchMoreShop()
    }

    private fun reloadSearchShop() {

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
    override fun onItemClicked(shopItem: ShopViewModel.ShopItem) {

    }

    override fun onProductItemClicked(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {

    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {

    }

    override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {

    }

    @Deprecated("Please create a better empty state view holder and listener")
    private fun getDummyEmptyStateListener(): EmptyStateListener {
        return object : EmptyStateListener {
            override fun onEmptyButtonClicked() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSelectedFilterRemoved(uniqueId: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getRegistrationId(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getUserId(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getSelectedFilterAsOptionList(): MutableList<Option> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }
}