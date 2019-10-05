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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.R
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ShopListItemDecoration
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactoryImpl
import com.tokopedia.search.result.shop.presentation.adapter.ShopListAdapter
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel
import com.tokopedia.topads.sdk.domain.model.CpmData

class ShopListFragmentKt:
        BaseDaggerFragment(),
        ShopListener,
        EmptyStateListener,
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
        return inflater.inflate(R.layout.search_result_shop_fragment_layout, null)
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
        initGridLayoutManager()
        initLoadMoreListener()
        initRecyclerView()
    }

    private fun initRefreshLayout() {
        refreshLayout = view?.findViewById(R.id.swipe_refresh_layout)

        refreshLayout?.setOnRefreshListener {
            searchShopViewModel?.onViewReloadData()
        }
    }

    private fun initGridLayoutManager() {
        gridLayoutManager = GridLayoutManager(activity, 1)
    }

    private fun initLoadMoreListener() {
        gridLayoutLoadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                searchShopViewModel?.onViewLoadMore(userVisibleHint)
            }
        }
    }

    private fun initRecyclerView() {
        activity?.let { activity ->
            initShopListAdapter()

            recyclerView = view?.findViewById(R.id.recyclerview)
            recyclerView?.layoutManager = gridLayoutManager
            recyclerView?.adapter = shopListAdapter
            recyclerView?.addItemDecoration(createShopItemDecoration(activity))
            gridLayoutLoadMoreTriggerListener?.let {
                recyclerView?.addOnScrollListener(it)
            }
        }
    }

    private fun initShopListAdapter() {
        val shopListTypeFactory = createShopListTypeFactory()
        shopListAdapter = ShopListAdapter(shopListTypeFactory)
    }

    private fun createShopListTypeFactory(): ShopListTypeFactory {
        return ShopListTypeFactoryImpl(this, this, this)
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

    private fun updateAdapter(searchShopLiveData: State<List<Visitable<*>>>?) {
        when(searchShopLiveData) {
            is State.Loading -> {
                showRefreshLayout()
                updateList(searchShopLiveData)
                updateScrollListener()
            }
            is State.Success -> {
                hideRefreshLayout()
                updateList(searchShopLiveData)
                updateScrollListener()
            }
            is State.Error -> {
                hideRefreshLayout()
                showRetryLayout(searchShopLiveData)
            }
        }
    }

    private fun showRefreshLayout() {
        refreshLayout?.isRefreshing = true
    }

    private fun hideRefreshLayout() {
        refreshLayout?.isRefreshing = false
    }

    private fun updateList(searchShopLiveData: State<List<Visitable<*>>>) {
        shopListAdapter?.updateList(searchShopLiveData.data ?: listOf())
    }

    private fun updateScrollListener() {
        gridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
        gridLayoutLoadMoreTriggerListener?.setHasNextPage(searchShopViewModel?.getHasNextPage() ?: false)
    }

    private fun showRetryLayout(searchShopLiveData: State<List<Visitable<*>>>) {
        val retryClickedListener = NetworkErrorHelper.RetryClickedListener {
            searchShopViewModel?.onViewClickRetry()
        }

        if (isSearchShopLiveDataContainItems(searchShopLiveData)) {
            NetworkErrorHelper.showEmptyState(activity, view, retryClickedListener)
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, retryClickedListener).showRetrySnackbar()
        }
    }

    private fun isSearchShopLiveDataContainItems(searchShopLiveData: State<List<Visitable<*>>>): Boolean {
        return searchShopLiveData.data?.size == 0
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        searchShopViewModel?.onViewVisibilityChanged(isVisibleToUser, isAdded)
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

    override fun onEmptyButtonClicked() {

    }

    override fun onSelectedFilterRemoved(uniqueId: String?) {

    }

    override fun getRegistrationId(): String {
        return searchShopViewModel?.getRegistrationId() ?: ""
    }

    override fun getUserId(): String {
        return searchShopViewModel?.getUserId() ?: ""
    }

    override fun getSelectedFilterAsOptionList(): MutableList<Option> {
        return mutableListOf()
    }
}
