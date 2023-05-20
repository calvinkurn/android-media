package com.tokopedia.inbox.universalinbox.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.inbox.databinding.UniversalInboxFragmentBinding
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxTopAdsAnalytic
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CLICK_TYPE_WISHLIST
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.COMPONENT_NAME_TOP_ADS
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.REQUEST_FROM_PDP
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.decorator.UniversalInboxRecommendationDecoration
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.viewmodel.UniversalInboxViewModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import javax.inject.Inject

class UniversalInboxFragment :
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener,
    TdnBannerResponseListener,
    TopAdsImageViewClickListener,
    RecommendationListener {
    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()
    private var adapter = UniversalInboxAdapter(
        this,
        this,
        this
    )
    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    @Inject
    lateinit var viewModel: UniversalInboxViewModel

    @Inject
    lateinit var topAdsAnalytic: UniversalInboxTopAdsAnalytic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UniversalInboxFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding?.root
    }

    override fun initInjector() {
        getComponent(UniversalInboxComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
    }

    private fun initViews(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupRecyclerViewLoadMore()
        setupObservers()
        adapter.addItems(viewModel.dummy())
        loadTopAdsAndRecommendation()
    }

    private fun setupRecyclerView() {
        binding?.inboxRv?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding?.inboxRv?.setHasFixedSize(true)
        binding?.inboxRv?.itemAnimator = null
        binding?.inboxRv?.adapter = adapter
        binding?.inboxRv?.addItemDecoration(UniversalInboxRecommendationDecoration())
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            binding?.inboxRv?.layoutManager?.let {
                endlessRecyclerViewScrollListener = UniversalInboxEndlessScrollListener(
                    layoutManager = it as StaggeredGridLayoutManager,
                    this@UniversalInboxFragment
                )
            }
        }
        endlessRecyclerViewScrollListener?.let {
            binding?.inboxRv?.addOnScrollListener(it)
        }
    }

    private fun setupObservers() {
        viewModel.firstPageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> {
                    onSuccessGetFirstRecommendationData(it.data.recommendationWidget, it.data.tdnBanner)
                }
                is Fail -> {}
            }
        }

        viewModel.morePageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> {
                    addRecommendationItem(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun onSuccessGetFirstRecommendationData(
        recommendation: RecommendationWidget,
        topAds: List<TopAdsImageViewModel>
    ) {
        // TopAds
        addTopAdsItem(topAds)

        // Recommendation
        adapter.addItem(UniversalInboxRecommendationTitleUiModel(recommendation.title))
        addRecommendationItem(recommendation.recommendationItemList)
    }

    private fun addRecommendationItem(list: List<RecommendationItem>) {
        val itemCountBefore = adapter.itemCount
        adapter.addItems(list)
        adapter.notifyItemRangeInserted(itemCountBefore, itemCountBefore + list.size)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun addTopAdsItem(list: List<TopAdsImageViewModel>) {
        adapter.addItem(UniversalInboxTopAdsBannerUiModel(list))
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun loadTopAdsAndRecommendation() {
        showLoadMoreLoading()
        viewModel.loadTopAdsAndFirstPageRecommendation()
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        showLoadMoreLoading()
        viewModel.loadMoreRecommendation(page)
    }

    private fun showLoadMoreLoading() {
        adapter.addItem(adapter.getItems().size, UniversalInboxRecommendationLoaderUiModel())
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun removeLoadMoreLoading() {
        if (adapter.getItems().isNotEmpty() &&
            adapter.isRecommendationLoader(adapter.getItems().lastIndex)
        ) {
            adapter.removeItemAt(adapter.getItems().lastIndex)
            adapter.notifyItemRemoved(adapter.getItems().size)
        }
    }

    override fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageViewModel>>) {
        // TODO
    }

    override fun onError(t: Throwable) {
        // TODO
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
        // TODO
    }

    override fun onProductClick(
        item: RecommendationItem,
        layoutType: String?,
        vararg position: Int
    ) {
        if (item.isTopAds) {
            onClickTopAds(item)
        } else {
            onClickOrganic(item)
        }
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productId.toString()
        )
        if (position.isNotEmpty()) {
            intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[Int.ZERO])
        }
        // Need to use startActivityForResult to be able to use handleProductCardOptionsActivityResult
        startActivityForResult(intent, REQUEST_FROM_PDP)
    }

    private fun onClickTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitClickUrl(
            activity?.javaClass?.name,
            item.clickUrl,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.eventInboxTopAdsProductClick(
            item,
            item.position,
            item.isTopAds
        )
    }

    private fun onClickOrganic(item: RecommendationItem) {
        topAdsAnalytic.eventInboxTopAdsProductClick(
            item,
            item.position,
            item.isTopAds
        )
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) {
            onImpressionTopAds(item)
        } else {
            onImpressionOrganic(item)
        }
    }

    private fun onImpressionTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitImpressionUrl(
            activity?.javaClass?.name,
            item.trackerImageUrl,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    private fun onImpressionOrganic(item: RecommendationItem) {
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {
        if (isAddWishlist) {
            // Anonymous used because we need RecommendationItem
            viewModel.addWishlistV2(
                item,
                object : WishlistV2ActionListener {
                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        showSuccessAddWishlistV2(wishlistAddResult = result)
                        if (item.isTopAds) {
                            onClickTopAdsWishlistItem(item)
                        }
                    }
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        val view: View = activity?.findViewById(android.R.id.content) ?: return
                        val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, view)
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {}
                }
            )
        } else {
            viewModel.removeWishlistV2(
                item,
                object : WishlistV2ActionListener {
                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        val view: View = activity?.findViewById(android.R.id.content) ?: return
                        val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, view)
                    }
                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        showSuccessRemoveWishlistV2(wishListRemoveResult = result)
                    }

                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {}
                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {}
                }
            )
        }
    }

    private fun onClickTopAdsWishlistItem(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitClickUrl(
            activity?.javaClass?.name,
            item.clickUrl + CLICK_TYPE_WISHLIST,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        if (position.isEmpty()) return
        showProductCardOptions(this, createProductCardOptionsModel(item, position[Int.ZERO]))
    }

    private fun createProductCardOptionsModel(
        recommendationItem: RecommendationItem,
        productPosition: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = productPosition
        return productCardOptionsModel
    }

    /**
     * Result launcher section
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP && data != null) {
            val wishlistStatusFromPdp = data.getBooleanExtra(
                WISHLIST_STATUS_IS_WISHLIST,
                false
            )
            val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
            if (position < Int.ZERO || adapter.getItems().size <= position) return
            if (adapter.getItems()[position] is RecommendationItem) {
                val recommendation = adapter.getItems()[position] as RecommendationItem
                recommendation.isWishlist = wishlistStatusFromPdp
                adapter.notifyItemChanged(position)
            }
        }
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(
                    productCardOptionsModel: ProductCardOptionsModel
                ) {
                    handleWishListAction(productCardOptionsModel)
                }
            }
        )
    }

    private fun handleWishListAction(
        productCardOptionsModel: ProductCardOptionsModel
    ) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            handleWishListV2ActionSuccess(productCardOptionsModel)
        } else {
            handleWishlistV2ActionFailed(productCardOptionsModel.wishlistResult)
        }
    }

    private fun handleWishListV2ActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        val isAddWishlist = productCardOptionsModel.wishlistResult.isAddWishlist
        topAdsAnalytic.eventClickRecommendationWishlist(isAddWishlist)
        val payloads = Bundle().also {
            it.putBoolean(WISHLIST_STATUS_IS_WISHLIST, isAddWishlist)
        }
        adapter.notifyItemChanged(productCardOptionsModel.productPosition, payloads)
        if (isAddWishlist) {
            showSuccessAddWishlistV2(wishlistResult = productCardOptionsModel.wishlistResult)
        } else {
            showSuccessRemoveWishlistV2(wishlistResult = productCardOptionsModel.wishlistResult)
        }
    }

    private fun showSuccessAddWishlistV2(
        wishlistAddResult: AddToWishlistV2Response.Data.WishlistAddV2? = null,
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.run {
            wishlistAddResult?.let {
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
            wishlistResult?.let {
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
        }
    }

    private fun showSuccessRemoveWishlistV2(
        wishListRemoveResult: DeleteWishlistV2Response.Data.WishlistRemoveV2? = null,
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.run {
            wishListRemoveResult?.let {
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
            wishlistResult?.let {
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
        }
    }

    private fun handleWishlistV2ActionFailed(
        wishlistResult: ProductCardOptionsModel.WishlistResult
    ) {
        val rootView = view?.rootView
        rootView?.let { v ->
            var errorMessage = ErrorHandler.getErrorMessage(v.context, null)
            if (wishlistResult.messageV2.isNotEmpty()) errorMessage = wishlistResult.messageV2

            if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
                AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                    errorMessage,
                    wishlistResult.ctaTextV2,
                    wishlistResult.ctaActionV2,
                    v,
                    v.context
                )
            } else {
                AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, v)
            }
        }
    }

    companion object {
        private const val TAG = "UniversalInboxFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): UniversalInboxFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UniversalInboxFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UniversalInboxFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UniversalInboxFragment
        }
    }
}
