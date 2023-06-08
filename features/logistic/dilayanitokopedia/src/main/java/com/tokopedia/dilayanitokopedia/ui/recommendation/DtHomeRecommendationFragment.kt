package com.tokopedia.dilayanitokopedia.ui.recommendation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.data.analytics.DtHomepageAnalytics
import com.tokopedia.dilayanitokopedia.data.analytics.ProductCardAnalyticsMapper
import com.tokopedia.dilayanitokopedia.di.component.DaggerHomeComponent
import com.tokopedia.dilayanitokopedia.ui.home.adapter.viewholder.HomeRecommendationFeedViewHolder
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeFeedEndlessScrollListener
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeFeedItemDecoration
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeRecommendationForYouAdapter
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory.HomeRecommendationTypeFactoryImpl
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import javax.inject.Inject

class DtHomeRecommendationFragment : Fragment(), TopAdsBannerClickListener {

    companion object {
        private const val REQUEST_FROM_PDP = 349
        private const val MAX_RECYCLED_VIEWS = 20

        private const val WISHLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"

        fun newInstance(): DtHomeRecommendationFragment {
            return DtHomeRecommendationFragment()
        }
    }

    @Inject
    lateinit
    var appExecutors: SmartExecutors

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: DtHomeRecommendationViewModel

    private var endlessRecyclerViewScrollListener: HomeFeedEndlessScrollListener? = null

    private var totalScrollY = 0

    private var parentPool: RecyclerView.RecycledViewPool? = null

    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl(this) }
    private val adapter by lazy {
        HomeRecommendationForYouAdapter(appExecutors, adapterFactory, provideListener(userSession))
    }

    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dt_home_recommendation_for_you_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
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
        parentPool?.setMaxRecycledViews(HomeRecommendationFeedViewHolder.LAYOUT, MAX_RECYCLED_VIEWS)
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
            startActivityForResult(intent, REQUEST_FROM_PDP)
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
        return ProductCardOptionsModel().apply {
            hasWishlist = true
            isWishlisted = homeRecommendationItemDataModel.product.isWishlist
            productId = homeRecommendationItemDataModel.product.id.toString()
            isTopAds = homeRecommendationItemDataModel.product.isTopads
            topAdsWishlistUrl = homeRecommendationItemDataModel.product.wishlistUrl
            topAdsClickUrl = homeRecommendationItemDataModel.product.clickUrl
            productName = homeRecommendationItemDataModel.product.name
            productImageUrl = homeRecommendationItemDataModel.product.imageUrl
            productPosition = position
        }
    }

    private fun provideListener(userSession: UserSessionInterface): HomeRecommendationListener {
        return object : HomeRecommendationListener {
            override fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
                DtHomepageAnalytics.sendImpressionProductCardsDtEvent(
                    userSession,
                    ProductCardAnalyticsMapper.fromRecommendation(position, homeRecommendationItemDataModel)
                )
            }

            override fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
                DtHomepageAnalytics.sendClickProductCardsDtEvent(
                    userSession,
                    ProductCardAnalyticsMapper.fromRecommendation(position, homeRecommendationItemDataModel)
                )
                goToProductDetail(homeRecommendationItemDataModel.product.id, position)
            }

            override fun onProductThreeDotsClick(
                homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
                position: Int
            ) {
                showProductCardOptions(
                    this@DtHomeRecommendationFragment,
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
        return ChooseAddressUtils.getLocalizingAddressData(requireContext()).convertToLocationParams()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_PDP && data != null && data.hasExtra(
                    WISHLIST_STATUS_IS_WISHLIST
                )
            ) {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID) ?: ""
                val wishlistStatusFromPdp = data.getBooleanExtra(WISHLIST_STATUS_IS_WISHLIST, false)
                val position = data.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1)
                updateWishlist(id, wishlistStatusFromPdp, position)
            }
        }
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    showMessageSuccessAddWishlistV2(wishlistResult)
                    if (productCardOptionsModel.isTopAds) {
                        hitWishlistClickUrl(
                            productCardOptionsModel
                        )
                    }
                } else {
                    showMessageSuccessRemoveWishlistV2(wishlistResult)
                }
                updateWishlist(
                    productCardOptionsModel.productId,
                    wishlistResult.isAddWishlist,
                    productCardOptionsModel.productPosition
                )
            } else {
                showMessageFailedWishlistV2Action(wishlistResult)
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun showMessageSuccessRemoveWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)

        context?.let { context ->
            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                wishlistResult,
                context,
                view
            )
        }
    }

    private fun showMessageFailedWishlistV2Action(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = activity?.findViewById<View>(android.R.id.content)

        var msgError = ErrorHandler.getErrorMessage(activity, Throwable())
        if (wishlistResult.messageV2.isNotEmpty()) msgError = wishlistResult.messageV2

        if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
            activity?.let { activity ->
                view?.let {
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                        msgError,
                        wishlistResult.ctaTextV2,
                        wishlistResult.ctaActionV2,
                        it,
                        activity
                    )
                }
            }
        } else {
            view?.let { AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(msgError, it) }
        }
    }

    private fun showMessageSuccessAddWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)

        context?.let { context ->
            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                wishlistResult,
                context,
                view
            )
        }
    }

    private fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                productCardOptionsModel.topAdsClickUrl + CLICK_TYPE_WISHLIST,
                productCardOptionsModel.productId,
                productCardOptionsModel.productName,
                productCardOptionsModel.productImageUrl
            )
        }
    }

    private fun updateWishlist(id: String, isWishlist: Boolean, position: Int) {
        if (position > -1 && adapter.itemCount > 0 &&
            adapter.itemCount > position
        ) {
            viewModel.updateWishlist(id, position, isWishlist)
        }
    }
}
