package com.tokopedia.dilayanitokopedia.home.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.di.component.DaggerHomeComponent
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeFeedEndlessScrollListener
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeFeedItemDecoration
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeRecommendationForYouAdapter
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.factory.HomeRecommendationTypeFactoryImpl
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.recomendation.HomeRecommendationFeedViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeRecommendationForYouViewModel
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import javax.inject.Inject

class DtHomeRecommendationForYouFragment : Fragment(), TopAdsBannerClickListener {

    companion object {
        private const val REQUEST_FROM_PDP = 349
        private const val MAX_RECYCLED_VIEWS = 20

        fun newInstance(): DtHomeRecommendationForYouFragment {
            return DtHomeRecommendationForYouFragment()
        }
    }

    @Inject
    lateinit
    var appExecutors: SmartExecutors

    @Inject
    lateinit var viewModel: DtHomeRecommendationForYouViewModel

    private var endlessRecyclerViewScrollListener: HomeFeedEndlessScrollListener? = null

    private var totalScrollY = 0

    private var parentPool: RecyclerView.RecycledViewPool? = null

    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl(this) }
    private val adapter by lazy {
        HomeRecommendationForYouAdapter(appExecutors, adapterFactory, provideListener())
    }

    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dt_home_recommendation_for_you_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun initInjector() {
        DaggerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        loadLoading()
        loadFirstPageData()
        initListeners()
        observeLiveData()
    }

    private fun loadLoading() {
        viewModel.loadLoading()
    }

    private fun observeLiveData() {
        viewModel.homeRecommendationLiveData.observe(viewLifecycleOwner) { data ->
            updateAdapter(data)
            updateScrollEndlessListener(data.isHasNextPage)
        }
    }

    private fun updateAdapter(data: HomeRecommendationDataModel) {
        adapter.submitList(data.homeRecommendations)
    }

    private fun setupRecyclerView() {
        recyclerView?.layoutManager = staggeredGridLayoutManager
        (recyclerView?.layoutManager as? StaggeredGridLayoutManager)?.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView?.addItemDecoration(HomeFeedItemDecoration(4f.toDpInt()))
        recyclerView?.adapter = adapter
        parentPool?.setMaxRecycledViews(
            HomeRecommendationFeedViewHolder.LAYOUT,
            MAX_RECYCLED_VIEWS
        )
        recyclerView?.setRecycledViewPool(parentPool)
        createEndlessRecyclerViewListener()
        endlessRecyclerViewScrollListener?.let { recyclerView?.addOnScrollListener(it) }
    }

    private fun initListeners() {
        if (view == null) return
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalScrollY += dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // no-op
            }
        })
    }

    @VisibleForTesting
    fun goToProductDetail(productId: Long, position: Int) {
        if (activity != null) {
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId.toString()
            )
            intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, position)
            try {
                startActivityForResult(intent, REQUEST_FROM_PDP)
            } catch (exception: ActivityNotFoundException) {
                // no-op
            }
        }
    }

    private fun loadFirstPageData() {
        viewModel.loadInitialPage(getLocationParamString())
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        // no-op
    }

    private fun createProductCardOptionsModel(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = homeRecommendationItemDataModel.product.isWishlist
        productCardOptionsModel.productId = homeRecommendationItemDataModel.product.id.toString()
        productCardOptionsModel.isTopAds = homeRecommendationItemDataModel.product.isTopads
        productCardOptionsModel.topAdsWishlistUrl =
            homeRecommendationItemDataModel.product.wishlistUrl
        productCardOptionsModel.topAdsClickUrl = homeRecommendationItemDataModel.product.clickUrl
        productCardOptionsModel.productName = homeRecommendationItemDataModel.product.name
        productCardOptionsModel.productImageUrl = homeRecommendationItemDataModel.product.imageUrl
        productCardOptionsModel.productPosition = position
        return productCardOptionsModel
    }

    private fun provideListener(): HomeRecommendationListener {
        return object : HomeRecommendationListener {
            override fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
                // no-op
            }

            override fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
                goToProductDetail(homeRecommendationItemDataModel.product.id, position)
            }

            override fun onProductThreeDotsClick(
                homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
                position: Int
            ) {
                showProductCardOptions(
                    this@DtHomeRecommendationForYouFragment,
                    createProductCardOptionsModel(homeRecommendationItemDataModel, position)
                )
            }

            override fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel) {
                // no-op
            }

            override fun onBannerTopAdsClick(
                homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
                position: Int
            ) {
                // no-op
            }

            override fun onBannerTopAdsImpress(
                homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
                position: Int
            ) {
                // no-op
            }

            override fun onRetryGetProductRecommendationData() {
                viewModel.loadInitialPage(getLocationParamString())
            }
        }
    }

    private val staggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(
            DynamicChannelTabletConfiguration.getSpanCountForHomeRecommendationAdapter(
                requireContext()
            ),
            StaggeredGridLayoutManager.VERTICAL
        )
    }

    private fun getLocationParamString(): String {
        return ChooseAddressUtils.getLocalizingAddressData(requireContext()).convertToLocationParams() ?: ""
    }

    private fun updateScrollEndlessListener(hasNextPage: Boolean) {
        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun createEndlessRecyclerViewListener() {
        endlessRecyclerViewScrollListener = object : HomeFeedEndlessScrollListener(recyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadNextData(page, getLocationParamString())
            }
        }
    }
}
