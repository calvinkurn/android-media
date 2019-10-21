package com.tokopedia.officialstore.official.presentation

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.officialstore.BuildConfig
import com.tokopedia.officialstore.OfficialStoreInstance
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.di.DaggerOfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeModule
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.ProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_SCROLL
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import javax.inject.Inject

class OfficialHomeFragment : BaseDaggerFragment(), HasComponent<OfficialStoreHomeComponent>, RecommendationListener {
    companion object {

        const val DEFAULT_PAGE = 1
        const val GRID_SPAN_COUNT = 1
        const val PRODUCT_RECOMM_GRID_SPAN_COUNT = 2
        const val BUNDLE_CATEGORY = "category_os"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 898
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }
    @Inject
    lateinit var viewModel: OfficialStoreHomeViewModel

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var recyclerView: RecyclerView? = null

    private var layoutManager: StaggeredGridLayoutManager? = null
    private var endlesScrollListener: EndlessRecyclerViewScrollListener? = null
    private var category: Category? = null

    private var adapter: OfficialHomeAdapter? = null
    private var lastClickLayoutType: String? = null
    private var lastParentPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(BUNDLE_CATEGORY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home_child, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        layoutManager = StaggeredGridLayoutManager(PRODUCT_RECOMM_GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.layoutManager = layoutManager
        endlesScrollListener = getEndlessRecyclerViewScrollListener()

        val adapterTypeFactory = OfficialHomeAdapterTypeFactory(this)
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        recyclerView?.adapter = adapter

        return view
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                // Load more product recom
                viewModel.loadMore(category, page)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeBannerData()
        observeFeaturedShop()
        observeDynamicChannel()
        observeProductRecommendation()
        refreshData()
        setListener()
    }

    private fun refreshData() {
        adapter?.clearAllElements()
        endlesScrollListener?.resetState()
        viewModel.loadFirstData(category, DEFAULT_PAGE)
    }

    private fun observeBannerData() {
        viewModel.officialStoreBannersResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingBanners(it.data, adapter)
                }
                is Fail -> {
                    if (BuildConfig.DEBUG)
                        it.throwable.printStackTrace()
                }
            }
        })
    }

    private fun observeFeaturedShop() {
        viewModel.officialStoreFeaturedShopResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingFeaturedShop(it.data, adapter)
                }
                is Fail -> {
                    if (BuildConfig.DEBUG)
                        it.throwable.printStackTrace()
                }

            }
        })
    }

    private fun observeDynamicChannel() {
        viewModel.officialStoreDynamicChannelResult.observe(this, Observer {
            if (it is Success) {
                swipeRefreshLayout?.isRefreshing = false
                OfficialHomeMapper.mappingDynamicChannel(it.data, adapter)
            } else if (it is Fail) {
                it.throwable.printStackTrace()
            }
        })
    }

    private fun observeProductRecommendation() {
        viewModel.officialStoreProductRecommendationResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    OfficialHomeMapper.mappingProductRecommendation(it.data, adapter, this)
                    hideLoadMoreLoading()
                }
                is Fail -> {
                    if (BuildConfig.DEBUG) {
                        it.throwable.printStackTrace()
                    }
                }
            }
        })
    }

    private fun observeTopAdsWishlist() {
        viewModel.topAdsWishlistResult.observe(this, Observer {
            when (it) {
                is Success -> { }
                is Fail -> {
                    if (BuildConfig.DEBUG) {
                        it.throwable.printStackTrace()
                    }
                }
            }
        })
    }

    private fun setListener() {
        endlesScrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }

        swipeRefreshLayout?.setOnRefreshListener {
            refreshData()
        }

//        if (parentFragment is RecyclerViewScrollListener) {
//            val scrollListener = parentFragment as RecyclerViewScrollListener
//            layoutManager?.let {
//                var firstVisibleInListview = it.findFirstVisibleItemPositions(null)
//                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        super.onScrolled(recyclerView, dx, dy)
//                        val currentFirstVisible = it.findFirstVisibleItemPositions(layoutManager.getChildCount())
//
//                        // scroll up
//                        if (currentFirstVisible > firstVisibleInListview) {
//                            scrollListener.onScrollUp()
//                        } else { // scroll down
//                            scrollListener.onScrollDown()
//                        }
//                        firstVisibleInListview = currentFirstVisible
//
//                        // TODO logic load more
//                        // please see ProductDetailFragment > function addLoadMoreImpression
//                        viewModel.loadMore()
//                    }
//
//                })
//            }
//        }

    }

    private fun loadDataProduct() {
        // Get Product Recommendation

    }

    private fun onErrorGetRecommendation(errorMessage: String?) {
        // Show error
    }

    private fun hideLoadMoreLoading() {
        endlesScrollListener?.updateStateAfterGetData()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): OfficialStoreHomeComponent? {
        return activity?.run {
            DaggerOfficialStoreHomeComponent
                    .builder()
                    .officialStoreHomeModule(OfficialStoreHomeModule())
                    .officialStoreComponent(OfficialStoreInstance.getComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
            lastClickLayoutType = null
            lastParentPosition = null
        }
    }

    private fun goToPDP(item: RecommendationItem, position: Int) {
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        // TO_DO: Implement tracking
        lastClickLayoutType = layoutType
        if (position.size > 1) {
            lastParentPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if (position > -1 && adapter != null) {
            if (adapter?.list?.get(position) is ProductRecommendationViewModel) {
                (adapter!!.list.get(position) as ProductRecommendationViewModel).productItem.isWishlist = isWishlist
                adapter!!.notifyItemChanged(position)
            }
        }
    }

    override fun onProductImpression(item: RecommendationItem) {
        // TO_DO: Implement Product Impression
        Log.d("Test: ", "onProductImpression")
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if (viewModel.isLoggedIn()) {
            // Implement tracking
            if (isAddWishlist) {
                viewModel.addWishlist(item, callback)
                if (item.isTopAds) {
                    observeTopAdsWishlist()
                } else {

                }
            } else {
                viewModel.removeWishlist(item, callback)
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }
}
