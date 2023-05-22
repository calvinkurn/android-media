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
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.REQUEST_FROM_PDP
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.SHIFTING_INDEX
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.decorator.UniversalInboxRecommendationDecoration
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.viewmodel.UniversalInboxViewModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.utils.PARAM_DEVICE
import com.tokopedia.topads.sdk.utils.PARAM_EP
import com.tokopedia.topads.sdk.utils.PARAM_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.PARAM_ITEM
import com.tokopedia.topads.sdk.utils.PARAM_PAGE
import com.tokopedia.topads.sdk.utils.PARAM_SRC
import com.tokopedia.topads.sdk.utils.PARAM_TEMPLATE_ID
import com.tokopedia.topads.sdk.utils.PARAM_USER_ID
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.utils.UrlParamHelper
import com.tokopedia.topads.sdk.utils.VALUE_DEVICE
import com.tokopedia.topads.sdk.utils.VALUE_EP
import com.tokopedia.topads.sdk.utils.VALUE_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxFragment :
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener,
    TdnBannerResponseListener,
    TopAdsImageViewClickListener,
    RecommendationListener {

    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    @Inject
    lateinit var viewModel: UniversalInboxViewModel

    @Inject
    lateinit var topAdsHeadlineViewModel: TopAdsHeadlineViewModel

    @Inject
    lateinit var topAdsAnalytic: UniversalInboxTopAdsAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()
    private lateinit var adapter: UniversalInboxAdapter

    // TopAds Banner
    private var topAdsBannerInProductCards: List<TopAdsImageViewModel>? = null
    private var topAdsBannerExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
    private var isTopAdsBannerAdded = false

    // TopAds Headline
    private var headlineData: CpmModel? = null
    private var headlineIndexList: ArrayList<Int>? = null
    private var headlineExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
    private var isAdded = false

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
        adapter = UniversalInboxAdapter(
            userSession,
            this,
            this,
            this
        )
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
                is Success -> onSuccessGetFirstRecommendationData(it.data)
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
        recommendation: RecommendationWidget
    ) {
        adapter.addItem(UniversalInboxRecommendationTitleUiModel(recommendation.title))
        addRecommendationItem(recommendation.recommendationItemList)
    }

    private fun addRecommendationItem(list: List<RecommendationItem>) {
        val itemCountBefore = adapter.itemCount
        adapter.addItems(list)
        adapter.notifyItemRangeInserted(itemCountBefore, itemCountBefore + list.size)
        setHeadlineAndBannerExperiment()
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun setHeadlineAndBannerExperiment() {
        try {
            setTopAdsHeadlineExperiment()
            setTopAdsBannerExperiment()
        } catch (throwable: Throwable) {
            context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
        }
    }

    private fun setTopAdsHeadlineExperiment() {
        var index = Int.ZERO
        if (headlineIndexList != null && headlineIndexList?.isNotEmpty() == true) {
            val pageNum = endlessRecyclerViewScrollListener?.currentPage ?: Int.ZERO
            if (pageNum == Int.ZERO) {
                headlineExperimentPosition =
                    headlineIndexList?.get(Int.ZERO) ?: HEADLINE_POS_NOT_TO_BE_ADDED
            } else if (headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT &&
                pageNum < HEADLINE_ADS_BANNER_COUNT
            ) {
                headlineExperimentPosition =
                    headlineIndexList?.get(Int.ONE) ?: HEADLINE_POS_NOT_TO_BE_ADDED
                index = Int.ONE
            }
            if ((headlineExperimentPosition != HEADLINE_POS_NOT_TO_BE_ADDED ||
                    (headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT && pageNum < HEADLINE_ADS_BANNER_COUNT)) &&
                headlineExperimentPosition <= adapter.itemCount &&
                (!isAdded || (headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT))
            ) {
                addTopAdsHeadlineUiModel(index)
            }
        }
    }

    private fun addTopAdsHeadlineUiModel(index: Int) {
        val position = if (isTopAdsBannerAdded) {
            headlineExperimentPosition + SHIFTING_INDEX
        } else {
            headlineExperimentPosition
        }
        adapter.addItem(
            position,
            UniversalInboxTopadsHeadlineUiModel(headlineData, Int.ZERO, index)
        )
        adapter.notifyItemInserted(position)
        isAdded = true
    }

    private fun setTopAdsBannerExperiment() {
        if (topAdsBannerExperimentPosition != TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED &&
            topAdsBannerExperimentPosition <= adapter.itemCount && !isTopAdsBannerAdded
        ) {
            val position = if (isAdded) {
                topAdsBannerExperimentPosition + SHIFTING_INDEX
            } else {
                topAdsBannerExperimentPosition
            }
            adapter.addItem(
                position,
                UniversalInboxTopAdsBannerUiModel(topAdsBannerInProductCards)
            )
            adapter.notifyItemInserted(position)
            isTopAdsBannerAdded = true
        }
    }

    private fun loadTopAdsAndRecommendation() {
        showLoadMoreLoading()
        topAdsHeadlineViewModel.getTopAdsHeadlineData(getHeadlineAdsParam(Int.ZERO), { data ->
            headlineData = data
            if (data.data.isEmpty()) {
                return@getTopAdsHeadlineData
            }
            setHeadlineIndexList(data)
            viewModel.loadFirstPageRecommendation()
        }, {
            viewModel.loadFirstPageRecommendation()
        })
    }

    private fun getHeadlineAdsParam(topAdsHeadLinePage: Int): String {
        return UrlParamHelper.generateUrlParamString(
            mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to topAdsHeadLinePage,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to PAGE_NAME,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userSession.userId
            )
        )
    }

    private fun setHeadlineIndexList(data: CpmModel) {
        headlineIndexList = ArrayList()
        val size = data.header.totalData
        for (i in 0 until size) {
            headlineIndexList?.add(data.data[i].cpm.position + adapter.itemCount)
        }
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
        if (categoriesList.isEmpty()) return
        if (categoriesList.size == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = categoriesList[Int.ONE]
            setTopAdsBannerExperimentPosition()
        } else if (categoriesList[Int.ZERO].size == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = listOf(categoriesList[Int.ZERO][Int.ONE])
            setTopAdsBannerExperimentPosition()
        }
        adapter.updateTopAdsBanner(categoriesList[Int.ZERO])
    }

    private fun setTopAdsBannerExperimentPosition() {
        if (!topAdsBannerInProductCards.isNullOrEmpty()) {
            val topAdsBannerInCardsPosition =
                topAdsBannerInProductCards?.get(Int.ZERO)?.position ?: -1
            val productRecommendationFirstPosition =
                adapter.getProductRecommendationFirstPosition() ?: adapter.itemCount
            topAdsBannerExperimentPosition =
                topAdsBannerInCardsPosition + productRecommendationFirstPosition
        }
    }

    override fun onError(t: Throwable) {
        // Do nothing
        Timber.d(t)
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
        if (applink == null) return
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
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
                        showErrorAddRemoveWishlistV2(throwable = throwable)
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
                        showErrorAddRemoveWishlistV2(throwable = throwable)
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
            showErrorAddRemoveWishlistV2(productCardOptionsModel.wishlistResult)
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

    private fun showErrorAddRemoveWishlistV2(
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null,
        throwable: Throwable? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.let {
            var errorMessage = ErrorHandler.getErrorMessage(it, throwable)
            if (wishlistResult != null) {
                if (wishlistResult.messageV2.isNotEmpty()) errorMessage = wishlistResult.messageV2
                if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                        errorMessage,
                        wishlistResult.ctaTextV2,
                        wishlistResult.ctaActionV2,
                        view,
                        it
                    )
                    return
                }
            }
            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, view)
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
