package com.tokopedia.tokopedianow.category.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2TabComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryL2TabAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.decoration.CategoryL2SpacingDecoration
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.view.CategoryL2View
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder.CategoryQuickFilterListener
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder.CategoryQuickFilterTrackerListener
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2TabViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_CARD_ITEM
import com.tokopedia.tokopedianow.common.decoration.HorizontalScrollDecoration
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addCategoryProductItemDecoration
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder.TokoNowTickerTrackerListener
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowL2TabBinding
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokopedianow.category.constant.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.category.constant.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker
import com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet.TokoNowProductFeedbackBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.CategoryChooserBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
import com.tokopedia.tokopedianow.similarproduct.presentation.fragment.TokoNowSimilarProductBottomSheetFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2TabFragment : Fragment() {

    companion object {
        fun newInstance(data: CategoryL2TabData): TokoNowCategoryL2TabFragment {
            return TokoNowCategoryL2TabFragment().apply {
                this.data = data
            }
        }

        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1

        private const val SCROLL_DOWN_DIRECTION = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var categoryL2Analytic: CategoryL2Analytic

    private var adapterTypeFactory: CategoryL2TabAdapterTypeFactory? = null
    private var categoryAdapter: CategoryL2TabAdapter? = null

    @Suppress("LateinitUsage")
    private lateinit var viewModel: TokoNowCategoryL2TabViewModel
    @Suppress("LateinitUsage")
    private lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowL2TabBinding>()

    private var loginActivityResult: ActivityResultLauncher<Intent>? = null
    private var addToCartVariantResult: ActivityResultLauncher<Intent>? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var categoryChooserBottomSheet: CategoryChooserBottomSheet? = null

    private var data: CategoryL2TabData = CategoryL2TabData()

    private val onScrollListener by lazy { createOnScrollListener() }

    var categoryL2View: CategoryL2View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        registerActivityResults()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowL2TabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        observeLiveData()
        onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onDestroy() {
        loginActivityResult = null
        addToCartVariantResult = null
        sortFilterBottomSheet = null
        categoryChooserBottomSheet = null
        adapterTypeFactory?.onDestroy()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    fun onCartItemsUpdated(data: MiniCartSimplifiedData) {
        viewModel.updateProductCartQuantity(data)
        updateProductRecommendationCart(data)
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider[TokoNowCategoryL2TabViewModel::class.java]
        productRecommendationViewModel = viewModelProvider[TokoNowProductRecommendationViewModel::class.java]
    }

    private fun observeLiveData() {
        observe(viewModel.visitableListLiveData) {
            removeOnScrollListener()
            submitList(it)
            addOnScrollListener()
        }

        observe(viewModel.addItemToCart) {
            onAddItemToCart(it)
        }

        observe(viewModel.updateCartItem) {
            when (it) {
                is Success -> categoryL2View?.updateMiniCartWidget()
                is Fail -> showErrorToaster(it.throwable)
            }
        }

        observe(viewModel.removeCartItem) {
            when (it) {
                is Success -> onSuccessRemoveCartItem(it.data.second)
                is Fail -> showErrorToaster(it.throwable)
            }
        }

        observe(viewModel.filterProductCountLiveData) {
            val countText = String.format(
                getString(R.string.tokopedianow_apply_filter_text),
                it
            )
            sortFilterBottomSheet?.setResultCountText(countText)
            categoryChooserBottomSheet?.setResultCountText(countText)
        }

        observe(viewModel.dynamicFilterModelLiveData) {
            openFilterBottomSheet(it)
        }

        observe(viewModel.routeAppLinkLiveData) {
            RouteManager.route(context, it)
        }

        observe(viewModel.openLoginPage) {
            openLoginPage()
        }

        observe(viewModel.miniCart) {
            when(it) {
                is Success -> {
                    categoryL2View?.updateMiniCartWidget(it.data)
                    updateProductRecommendationCart(it.data)
                }
                else -> {
                    // do nothing
                }
            }
        }
        
        observe(viewModel.atcDataTracker) {
            when(it.layoutType) {
                PRODUCT_ADS_CAROUSEL -> trackProductAdsAddToCart(it)
                PRODUCT_CARD_ITEM -> trackProductAddToCart(it)
            }
        }

        observe(viewModel.clickWishlistTracker) {
            trackClickWishlistButton(it.first, it.second)
        }

        observe(viewModel.clickSimilarProductTracker) {
            trackClickSimilarProduct(it.first, it.second)
        }

        observe(viewModel.shareLiveData) {
            if(isResumed) {
                categoryL2View?.setShareModel(it)
            }
        }

        observe(viewModel.updateToolbarNotification) {
            updateToolbarNotification()
        }

        observe(productRecommendationViewModel.updateToolbarNotification) {
            updateToolbarNotification()
        }

        observe(productRecommendationViewModel.addItemToCart) {
            onAddItemToCart(it)
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated(data)
    }

    private fun registerActivityResults() {
        loginActivityResult = registerActivityResult {
            viewModel.refreshPage()
        }
        addToCartVariantResult = registerActivityResult {
            getMiniCart()
        }
    }

    private fun setupAdapter() {
        adapterTypeFactory = CategoryL2TabAdapterTypeFactory(
            adsCarouselListener = createProductAdsCarouselListener(),
            quickFilterListener = createQuickFilterListener(),
            quickFilterTrackerListener = createQuickFilterTrackerListener(),
            productItemListener = createProductItemListener(),
            productCardCompactListener = createProductCardCompactListener(),
            similarProductTrackerListener = createSimilarProductTrackerListener(),
            emptyStateNoResultListener = createEmptyStateNoResultListener(),
            productRecommendationListener = createProductRecommendationListener(),
            categoryMenuListener = createCategoryMenuListener(),
            feedbackWidgetListener = createFeedbackWidgetListener(),
            tickerTrackerListener = createTickerTrackerListener()
        )

        adapterTypeFactory?.let {
            categoryAdapter = CategoryL2TabAdapter(it, CategoryL2TabDiffer())
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            addCategoryProductItemDecoration()
            addItemDecoration(HorizontalScrollDecoration())
            addItemDecoration(CategoryL2SpacingDecoration())
            layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                spanSizeLookup = createSpanSizeLookup()
            }
            removeOnScrollListener()
            addOnScrollListener()
            adapter = categoryAdapter
        }
    }

    private fun addOnScrollListener() {
        binding?.apply {
            recyclerView.post {
                recyclerView.addOnScrollListener(onScrollListener)
            }
        }
    }

    private fun removeOnScrollListener() {
        binding?.apply {
            recyclerView.post {
                recyclerView.removeOnScrollListener(onScrollListener)
            }
        }
    }

    private fun updateProductRecommendationCart(miniCartData: MiniCartSimplifiedData) {
        productRecommendationViewModel.updateMiniCartSimplified(miniCartData)
    }

    private fun updateToolbarNotification() {
        categoryL2View?.updateToolbarNotificationCounter()
    }

    private fun onAddItemToCart(result: Result<AddToCartDataModel>) {
        when (result) {
            is Success -> onSuccessAddToCart(result)
            is Fail -> showErrorToaster(result.throwable)
        }
    }

    private fun submitList(items: List<Visitable<*>>) {
        binding?.apply {
            recyclerView.post {
                if(!recyclerView.isComputingLayout) {
                    categoryAdapter?.submitList(items)
                }
            }
        }
    }

    private fun openVariantBottomSheet(productId: String, shopId: String) {
        AtcVariantHelper.goToAtcVariant(
            context = requireActivity(),
            productId = productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = String.format(TOKONOW_CATEGORY_ORGANIC, data.categoryIdL2),
            startActivitResult = { intent, _ ->
                addToCartVariantResult?.launch(intent)
            }
        )
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        loginActivityResult?.launch(intent)
    }

    private fun registerActivityResult(
        onActivityResult: () -> Unit
    ) = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            onActivityResult.invoke()
        }
    }

    private fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        duration: Int = Toaster.LENGTH_SHORT,
        onClickActionBtn: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickActionBtn
                )
                toaster.show()
            }
        }
    }

    private fun showErrorToaster(message: String) {
        showToaster(message = message, type = Toaster.TYPE_ERROR)
    }

    private fun showErrorToaster(throwable: Throwable) {
        val message = throwable.message.orEmpty()
        showToaster(message = message, type = Toaster.TYPE_ERROR)
    }

    private fun showAddToCartBlockedToaster() {
        val message = getString(
            R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop
        )
        showErrorToaster(message = message)
    }

    private fun onSuccessAddToCart(result: Success<AddToCartDataModel>) {
        val message = result.data.errorMessage
            .joinToString(separator = ", ")
        showToaster(message)
        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(message: String) {
        val actionText = getString(R.string.tokopedianow_toaster_ok)
        showToaster(message = message, actionText = actionText)
        getMiniCart()
    }

    private fun openCategoryChooserFilterPage(filter: Filter) {
        categoryChooserBottomSheet = CategoryChooserBottomSheet()
        val callback = createCategoryChooserCallback()

        categoryChooserBottomSheet?.show(
            parentFragmentManager,
            viewModel.getMapParameter(),
            filter,
            callback
        )
    }

    private fun injectDependencies() {
        DaggerCategoryL2TabComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun goToTokopediaHomePage() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    private fun goToTokopediaNowHomePage() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    private fun goToProductDetailPage(appLink: String) {
        val newAppLink = viewModel.createAffiliateLink(appLink)
        RouteManager.route(activity, newAppLink)
    }

    private fun goToAtcVariant(productId: String, shopId: String) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = String.format(TOKONOW_CATEGORY_ORGANIC, data.categoryIdL1),
            startActivitResult = ::startActivityForResult
        )
    }

    private fun trackClickProductRecommendation(
        product: ProductCardCompactCarouselItemUiModel,
        position: Int,
        userId: String
    ) {
        val recommendationItem =
            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
        val eventLabel = viewModel.getCategoryIdForTracking()

        SearchResultTracker.trackClickProduct(
            position,
            eventLabel,
            CategoryTracking.Action.CLICK_CLP_PRODUCT_TOKONOW,
            CategoryTracking.Category.TOKONOW_CATEGORY_PAGE,
            CategoryTracking.Misc.RECOM_LIST_PAGE_NON_OOC,
            userId,
            recommendationItem
        )
    }

    private fun trackProductRecommendationImpression(
        product: ProductCardCompactCarouselItemUiModel,
        position: Int,
        userId: String
    ) {
        val recommendationItem =
            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
        val eventLabel = viewModel.getCategoryIdForTracking()
        SearchResultTracker.trackImpressionProduct(
            position,
            eventLabel,
            CategoryTracking.Action.IMPRESSION_CLP_PRODUCT_TOKONOW,
            CategoryTracking.Category.TOKONOW_CATEGORY_PAGE,
            CategoryTracking.Misc.RECOM_LIST_PAGE_NON_OOC,
            userId,
            recommendationItem
        )
    }

    private fun trackClickFilterButton() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.quickFilterAnalytic
            .sendClickQuickFilterButtonEvent(categoryIdL2, warehouseIds)
    }

    private fun trackOpenBrandDropDown(filter: Filter) {
        if(filter.isBrandFilter) {
            val categoryIdL2 = data.categoryIdL2
            val warehouseIds = viewModel.getWarehouseIds()
            categoryL2Analytic.quickFilterAnalytic
                .sendClickBrandNavigationalDropdownEvent(categoryIdL2, warehouseIds)
        }
    }

    private fun trackOpenFilterPage() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.quickFilterAnalytic
            .sendClickFullFilterButtonEvent(categoryIdL2, warehouseIds)
    }

    private fun trackClickWishlistButton(index: Int, productId: String) {
        categoryL2Analytic.productAnalytic
            .sendClickWishlistButtonOosEvent(index, data.categoryIdL2, productId)
    }

    private fun trackFilterBottomSheetImpression() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.sortFilterAnalytic
            .sendImpressionFilterBottomSheetEvent(categoryIdL2, warehouseIds)
    }

    private fun trackClickApplySortFilter() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.sortFilterAnalytic
            .sendClickFilterBottomSheetApplyFilterEvent(categoryIdL2, warehouseIds)
    }

    private fun trackClickSimilarProduct(index: Int, productId: String) {
        val categoryIdL2 = data.categoryIdL2
        categoryL2Analytic.similarProductAnalytic
            .sendClickSimilarProductDropdownEvent(index, productId, categoryIdL2)
    }

    private fun directToSeeMorePage(appLink: String) {
        val categoryIdTracking = viewModel.getCategoryIdForTracking()
        val newAppLink = modifySeeMoreAppLink(appLink)

        CategoryTracking.sendRecommendationSeeAllClickEvent(categoryIdTracking)
        RouteManager.route(activity, newAppLink)
    }

    private fun showShopClosedToaster() {
        val message = getString(
            R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop)
        showToaster(message = message, type = Toaster.TYPE_ERROR)
    }

    private fun modifySeeMoreAppLink(
        originalAppLink: String
    ): String {
        val uri = Uri.parse(originalAppLink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: "")
        val ref = queryParamsMap[RECOM_QUERY_PARAM_REF] ?: ""

        return if (ref == RecomPageConstant.TOKONOW_CLP) {
            val recomCategoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] ?: ""

            if (recomCategoryId.isEmpty()) {
                queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = data.categoryIdL1
            }

            "${uri.scheme}://" +
                "${uri.host}/" +
                "${uri.path}?" +
                UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalAppLink
        }
    }

    private fun trackProductAdsAddToCart(atcData: CategoryAtcTrackerModel) {
        val title = getString(R.string.tokopedianow_product_ads_carousel_title)
        categoryL2Analytic.adsProductAnalytic.trackProductAdsAddToCart(title, atcData)
    }

    private fun trackProductAddToCart(atcData: CategoryAtcTrackerModel) {
        categoryL2Analytic.productAnalytic.trackProductAddToCart(atcData)
    }

    private fun trackImpressionOutOfStockTicker() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.tickerAnalytic
            .sendImpressionOosTickerEvent(categoryIdL2, warehouseIds)
    }

    private fun trackClickCloseOutOfStockTicker() {
        val categoryIdL2 = data.categoryIdL2
        val warehouseIds = viewModel.getWarehouseIds()
        categoryL2Analytic.tickerAnalytic
            .sendClickCloseButtonOnOosTickerEvent(categoryIdL2, warehouseIds)
    }

    private fun createSpanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (categoryAdapter?.getItemViewType(position)) {
                ProductItemViewHolder.LAYOUT -> SPAN_FULL_SPACE
                else -> SPAN_COUNT
            }
        }
    }

    private fun createOnScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val isAtTheBottomOfThePage =
                !recyclerView.canScrollVertically(SCROLL_DOWN_DIRECTION)
            viewModel.onScroll(isAtTheBottomOfThePage)
        }
    }

    private fun createProductAdsCarouselListener(): ProductAdsCarouselListener {
        val analytic = categoryL2Analytic.adsProductAnalytic

        return object : ProductAdsCarouselListener {
            override fun onProductCardClicked(
                position: Int,
                title: String,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                viewModel.routeToProductDetailPage(
                    product.getProductId(),
                    product.appLink
                )
                analytic.trackProductAdsClick(
                    position,
                    title,
                    data.categoryIdL2,
                    product
                )
            }

            override fun onProductCardImpressed(
                position: Int,
                title: String,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                analytic.trackProductAdsImpression(
                    position,
                    title,
                    data.categoryIdL2,
                    product
                )
            }

            override fun onProductCardQuantityChanged(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                quantity: Int
            ) {
                viewModel.onCartQuantityChanged(
                    product = product.productCardModel,
                    shopId = product.shopId,
                    quantity = quantity,
                    layoutType = PRODUCT_ADS_CAROUSEL
                )
            }

            override fun onProductCardAddVariantClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                openVariantBottomSheet(product.getProductId(), product.shopId)
            }

            override fun onProductCardAddToCartBlocked() {
                showAddToCartBlockedToaster()
            }
        }
    }

    private fun createProductItemListener(): ProductItemListener {
        val analytic = categoryL2Analytic.productAnalytic

        return object : ProductItemListener {
            override fun onProductImpressed(productItemDataView: ProductItemDataView) {
                analytic.trackProductImpression(data.categoryIdL2, productItemDataView)
            }

            override fun onProductClick(productItemDataView: ProductItemDataView) {
                val productId = productItemDataView.productCardModel.productId
                viewModel.routeToProductDetailPage(productId)
                analytic.trackProductClick(data.categoryIdL2, productItemDataView)
            }

            override fun onProductNonVariantQuantityChanged(
                productItemDataView: ProductItemDataView,
                quantity: Int
            ) {
                viewModel.onCartQuantityChanged(
                    product = productItemDataView.productCardModel,
                    shopId = productItemDataView.shop.id,
                    quantity = quantity,
                    layoutType = PRODUCT_CARD_ITEM
                )
            }

            override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
                val productCardModel = productItemDataView.productCardModel
                openVariantBottomSheet(productCardModel.productId, productItemDataView.shopId)
            }

            override fun onWishlistButtonClicked(
                productId: String,
                isWishlistSelected: Boolean,
                descriptionToaster: String,
                ctaToaster: String,
                type: Int,
                ctaClickListener: (() -> Unit)?
            ) {
                viewModel.updateWishlistStatus(productId, isWishlistSelected)
                showToaster(descriptionToaster, type, ctaToaster) {
                    ctaClickListener?.invoke()
                }
            }

            override fun onProductCardAddToCartBlocked() {
                showAddToCartBlockedToaster()
            }
        }
    }

    private fun createProductCardCompactListener(): ProductCardCompactListener {
        return object : ProductCardCompactListener {
            override fun onClickSimilarProduct(
                productId: String,
                similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?
            ) {
                val fragment = TokoNowSimilarProductBottomSheetFragment.newInstance(productId)
                fragment.setListener(similarProductTrackerListener)
                fragment.finishActivityOnDismiss = false
                categoryL2View?.showFragment(fragment)
            }
        }
    }

    private fun createSimilarProductTrackerListener(): ProductCardCompactSimilarProductTrackerListener {
        return object : ProductCardCompactSimilarProductTrackerListener {
            override fun trackImpressionBottomSheet(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String
            ) {
                val categoryIdL2 = data.categoryIdL2
                categoryL2Analytic.similarProductAnalytic
                    .sendProductImpressionEvent(categoryIdL2, similarProduct)
            }

            override fun trackClickProduct(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String
            ) {
                val categoryIdL2 = data.categoryIdL2
                val warehouseIds = viewModel.getWarehouseIds()
                categoryL2Analytic.similarProductAnalytic
                    .sendClickSimilarProductCardEvent(categoryIdL2, warehouseIds, similarProduct)
            }

            override fun trackClickAddToCart(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String,
                newQuantity: Int
            ) {
                val categoryIdL2 = data.categoryIdL2
                categoryL2Analytic.similarProductAnalytic
                    .sendProductAddToCartEvent(categoryIdL2, newQuantity, similarProduct)
            }

            override fun trackClickCloseBottomsheet(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {
                val categoryIdL2 = data.categoryIdL2
                val warehouseIds = viewModel.getWarehouseIds()
                categoryL2Analytic.similarProductAnalytic
                    .sendClickCloseOnSimilarProductEvent(categoryIdL2, warehouseIds)
                viewModel.getMiniCart()
            }

            override fun trackClickSimilarProductBtn(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {
                viewModel.onClickSimilarProduct(productIdTriggered)
            }

            override fun trackImpressionEmptyState(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {
            }
        }
    }

    private fun createQuickFilterListener(): CategoryQuickFilterListener {
        return object : CategoryQuickFilterListener {
            override fun openFilterPage() {
                viewModel.onOpenFilterPage()
                trackOpenFilterPage()
            }

            override fun openL3FilterPage(filter: Filter) {
                openCategoryChooserFilterPage(filter)
                trackOpenBrandDropDown(filter)
            }

            override fun applyFilter(filter: Filter, option: Option) {
                viewModel.applyQuickFilter(filter, option)
                trackClickFilterButton()
            }
        }
    }

    private fun createQuickFilterTrackerListener(): CategoryQuickFilterTrackerListener {
        return object : CategoryQuickFilterTrackerListener {
            override fun onImpressQuickFilterChip(option: Option, isActive: Boolean) {
                if(isActive) {
                    val categoryIdL2 = data.categoryIdL2
                    val filterType = option.inputType
                    val filterName = option.name
                    val warehouseIds = viewModel.getWarehouseIds()
                    categoryL2Analytic.quickFilterAnalytic.sendImpressionActiveQuickFilterEvent(
                        categoryIdL2 = categoryIdL2,
                        filterType = filterType,
                        filterName = filterName,
                        warehouseIds = warehouseIds
                    )
                }
            }
        }
    }

    private fun openFilterBottomSheet(dynamicFilterModel: DynamicFilterModel?) {
        if (dynamicFilterModel == null) return
        val mapParameter = viewModel.getMapParameter()

        sortFilterBottomSheet = SortFilterBottomSheet().apply {
            setDynamicFilterModel(dynamicFilterModel)
            setOnDismissListener {
                viewModel.onDismissFilterBottomSheet()
            }
        }

        sortFilterBottomSheet?.show(
            parentFragmentManager,
            mapParameter,
            dynamicFilterModel,
            createFilterBottomSheetCallback()
        )

        trackFilterBottomSheetImpression()
    }

    private fun createFilterBottomSheetCallback(): SortFilterBottomSheet.Callback {
        return object : SortFilterBottomSheet.Callback {
            override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
                viewModel.applySortFilter(applySortFilterModel)
                sortFilterBottomSheet?.dismiss()
                trackClickApplySortFilter()
            }

            override fun getResultCount(mapParameter: Map<String, String>) {
                viewModel.getProductCount(mapParameter)
            }
        }
    }

    private fun createCategoryChooserCallback(): CategoryChooserBottomSheet.Callback {
        return object : CategoryChooserBottomSheet.Callback {
            override fun onApplyCategory(selectedOption: Option) {
                categoryChooserBottomSheet?.dismiss()
                viewModel.applyFilterFromCategoryChooser(selectedOption)
            }

            override fun getResultCount(selectedOption: Option) {
                viewModel.getProductCount(selectedOption)
            }
        }
    }

    private fun createEmptyStateNoResultListener(): TokoNowEmptyStateNoResultListener {
        return object : TokoNowEmptyStateNoResultListener {
            override fun onFindInTokopediaClick() {
                goToTokopediaHomePage()
            }

            override fun goToTokopediaNowHome() {
                goToTokopediaNowHomePage()
            }

            override fun onRemoveFilterClick(option: Option) {
                viewModel.onRemoveFilter(option)
            }
        }
    }

    private fun createProductRecommendationListener(): TokoNowProductRecommendationListener {
        return object : TokoNowProductRecommendationListener {
            override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel {
                return productRecommendationViewModel
            }

            override fun hideProductRecommendationWidget() {
                viewModel.removeProductRecommendationWidget()
            }

            override fun openLoginPage() {
                this@TokoNowCategoryL2TabFragment.openLoginPage()
            }

            override fun productCardAddVariantClicked(productId: String, shopId: String) {
                goToAtcVariant(productId, shopId)
            }

            override fun productCardClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                isLogin: Boolean,
                userId: String
            ) {
                trackClickProductRecommendation(product, position, userId)
                goToProductDetailPage(product.appLink)
            }

            override fun productCardImpressed(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                isLogin: Boolean,
                userId: String
            ) {
                trackProductRecommendationImpression(product, position, userId)
            }

            override fun seeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {
                directToSeeMorePage(seeMoreUiModel.appLink)
            }

            override fun seeAllClicked(appLink: String) {
                directToSeeMorePage(appLink)
            }

            override fun productCardAddToCartBlocked() {
                showShopClosedToaster()
            }
        }
    }

    private fun createCategoryMenuListener(): TokoNowCategoryMenuListener {
        return object : TokoNowCategoryMenuListener {
            override fun onCategoryMenuWidgetRetried() {
                viewModel.getCategoryMenuData()
            }

            override fun onSeeAllCategoryClicked() {
            }

            override fun onCategoryMenuItemClicked(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) {
                val categoryIdL1 = data.id
                val categoryIdL2 = getCategoryIdL2()
                val headerName = data.headerName
                val warehouseIds = viewModel.getWarehouseIds()
                categoryL2Analytic.categoryMenuAnalytic
                    .sendClickCategoryRecomWidgetEvent(categoryIdL1, categoryIdL2, headerName, warehouseIds)
            }

            override fun onCategoryMenuItemImpressed(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) {
                val categoryIdL1 = data.id
                val categoryIdL2 = getCategoryIdL2()
                val headerName = data.headerName
                val warehouseIds = viewModel.getWarehouseIds()
                categoryL2Analytic.categoryMenuAnalytic
                    .sendImpressionCategoryRecomWidgetEvent(categoryIdL1, categoryIdL2, headerName, warehouseIds)
            }

            override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) {
            }
        }
    }

    private fun createFeedbackWidgetListener(): FeedbackWidgetListener {
        return object : FeedbackWidgetListener {
            override fun onFeedbackCtaClicked() {
                TokoNowProductFeedbackBottomSheet().also {
                    it.showBottomSheet(activity?.supportFragmentManager, view)
                }
            }
        }
    }

    private fun createTickerTrackerListener(): TokoNowTickerTrackerListener {
        return object : TokoNowTickerTrackerListener {
            override fun onImpressTicker(data: TokoNowTickerUiModel) {
                if(data.hasOutOfStockTicker) {
                    trackImpressionOutOfStockTicker()
                }
            }

            override fun onCloseTicker(data: TokoNowTickerUiModel) {
                if(data.hasOutOfStockTicker) {
                    trackClickCloseOutOfStockTicker()
                }
                viewModel.removeTicker()
            }
        }
    }

    private fun getCategoryIdL2() = data.categoryIdL2
}
