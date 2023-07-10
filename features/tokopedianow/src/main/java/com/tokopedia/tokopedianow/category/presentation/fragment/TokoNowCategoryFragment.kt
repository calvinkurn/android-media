package com.tokopedia.tokopedianow.category.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductCardAdsCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductRecommendationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseHeaderCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseItemCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.ProductCardCompactCallback
import com.tokopedia.tokopedianow.category.presentation.callback.ProductCardCompactSimilarProductTrackerCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowCategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryFragment : BaseCategoryFragment() {

    companion object {
        fun newInstance(): TokoNowCategoryFragment {
            return TokoNowCategoryFragment()
        }
    }

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    /**
     * -- private getter variable section --
     */

    private val recycledViewPool
        get() = RecyclerView.RecycledViewPool()

    /**
     * -- override function section --
     */

    override val categoryIdL1: String
        get() = viewModel.categoryIdL1

    override val currentCategoryId: String
        get() = viewModel.categoryIdL1

    override val viewModel: TokoNowCategoryViewModel
        get() = ViewModelProvider(requireActivity(), viewModelFactory)[TokoNowCategoryViewModel::class.java]

    override fun createAdapterTypeFactory(): CategoryAdapterTypeFactory {
        return CategoryAdapterTypeFactory(
            categoryTitleListener = createTitleCallback(),
            categoryNavigationListener = createCategoryNavigationCallback(),
            categoryShowcaseItemListener = createCategoryShowcaseItemCallback(),
            categoryShowcaseHeaderListener = createCategoryShowcaseHeaderCallback(),
            tokoNowView = createTokoNowViewCallback(),
            tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
            tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
            tokoNowProductRecommendationListener = createProductRecommendationCallback(),
            productCardCompactListener = createProductCardCompactCallback(),
            productCardCompactSimilarProductTrackerListener = createProductCardCompactSimilarProductTrackerCallback(),
            productAdsCarouselListener = createProductCardAdsCallback(),
            recycledViewPool = recycledViewPool,
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun createAdapterDiffer() = CategoryDiffer()

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        onViewCreated()
    }

    override fun setupNavigationToolbar(navToolbar: NavToolbar) {
        super.setupNavigationToolbar(navToolbar)
        activity?.let {
            navToolbar.setupToolbarWithStatusBar(activity = it)
            viewLifecycleOwner.lifecycle.addObserver(navToolbar)
        }
    }

    override fun setupRecyclerView(recyclerView: RecyclerView, navToolbar: NavToolbar) {
        super.setupRecyclerView(recyclerView, navToolbar)
        val navScrollListener = createNavRecyclerViewOnScrollListener(navToolbar)
        recyclerView.addOnScrollListener(navScrollListener)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        super.onCartItemsUpdated(miniCartSimplifiedData)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onGetMiniCartSuccess(data: MiniCartSimplifiedData) {
        super.onGetMiniCartSuccess(data)
        productRecommendationViewModel.updateMiniCartSimplified(data)
    }

    override fun getScreenName(): String = String.EMPTY

    /**
     * -- private function section --
     */

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = navToolbarHeight - transitionRange - transitionRange,
            toolbarTransitionRangePixel = transitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }

                override fun onSwitchToLightToolbar() { /* nothing to do */ }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }

                override fun onSwitchToDarkToolbar() {
                    navToolbar.hideShadow()
                }
            },
            fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
        )
    }

    private fun onViewCreated() {
        viewModel.onViewCreated()
    }

    private fun setupObserver() {
        observeCategoryHeader()
        observeScrollNotNeeded()
        observeAtcDataTracker()
        observeProductRecommendationAddToCart()
        observeProductRecommendationRemoveCartItem()
        observeProductRecommendationUpdateCartItem()
        observeProductRecommendationToolbarNotification()
        observeProductRecommendationAtcDataTracker()
        observeOpenScreenTracker()
    }

    private fun observeCategoryHeader() {
        viewModel.categoryFirstPage.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showMainLayout()
                    submitList(result.data)
                    viewModel.getFirstPage()
                }
                is Fail -> showErrorLayout(
                    throwable = result.throwable
                )
            }
        }
    }

    private fun observeScrollNotNeeded() {
        observe(viewModel.scrollNotNeeded) {
            removeScrollListener()
        }
    }

    private fun observeProductRecommendationAddToCart() {
        productRecommendationViewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessAddItemToCart(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeProductRecommendationUpdateCartItem() {
        productRecommendationViewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onSuccessUpdateCartItem()
                }
                is Fail -> {
                    showErrorToaster(
                        error = result
                    )
                }
            }
        }
    }

    private fun observeProductRecommendationRemoveCartItem() {
        productRecommendationViewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessRemoveCartItem(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeProductRecommendationToolbarNotification() {
        productRecommendationViewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
        }
    }

    private fun observeOpenScreenTracker() {
        viewModel.openScreenTracker.observe(viewLifecycleOwner) { model ->
            val uri = Uri.parse(model.url)
            uri.lastPathSegment?.let { categorySlug ->
                analytic.sendOpenScreenEvent(
                    slug = categorySlug,
                    id = model.id,
                    name = model.name,
                    isLoggedInStatus = viewModel.isLoggedIn()
                )
            }
        }
    }

    private fun observeAtcDataTracker() {
        viewModel.atcDataTracker.observe(viewLifecycleOwner) { model ->
            when(model.layoutType) {
                CATEGORY_SHOWCASE.name -> trackCategoryShowcaseAddToCart(model)
                PRODUCT_ADS_CAROUSEL -> trackProductAdsAddToCart(model)
            }
        }
    }

    private fun observeProductRecommendationAtcDataTracker() {
        productRecommendationViewModel.atcDataTracker.observe(viewLifecycleOwner) { model ->
            analytic.categoryProductRecommendationAnalytic.sendClickAtcCarouselWidgetEvent(
                categoryIdL1 = categoryIdL1,
                index = model.position,
                productId = model.productRecommendation.getProductId(),
                warehouseId = viewModel.getWarehouseId(),
                isOos = model.productRecommendation.productCardModel.isOos(),
                name = model.productRecommendation.getProductName(),
                price = model.productRecommendation.getProductPrice().toIntSafely(),
                headerName = model.productRecommendation.headerName,
                quantity = model.quantity
            )
        }
    }

    private fun clickProductCard(
        appLink: String
    ) {
        if (appLink.isNotEmpty()) {
            val affiliateLink = viewModel.createAffiliateLink(
                url = appLink
            )
            RouteManager.route(context, affiliateLink)
        }
    }

    private fun clickProductCardShowcase(
        appLink: String,
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        clickProductCard(appLink)

        analytic.categoryShowcaseAnalytic.sendClickProductShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun clickProductCardRecommendation(
        appLink: String,
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        clickProductCard(appLink)

        analytic.categoryProductRecommendationAnalytic.sendClickProductCarouselEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun impressProductCardShowcase(
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        analytic.categoryShowcaseAnalytic.sendImpressionProductInShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun impressProductCardRecommendation(
        headerName: String,
        index: Int,
        productId: String,
        productName: String,
        productPrice: String,
        isOos: Boolean
    ) {
        analytic.categoryProductRecommendationAnalytic.sendImpressionProductCarouselEvent(
            categoryIdL1 = categoryIdL1,
            headerName = headerName,
            index = index.getTrackerPosition(),
            productId = productId,
            warehouseId = viewModel.getWarehouseId(),
            isOos = isOos,
            name = productName,
            price = productPrice.getDigits()?.toLong().orZero()
        )
    }

    private fun clickMoreCategories() {
        analytic.categoryTitleAnalytics.sendClickMoreCategoriesEvent(
            categoryIdL1 = categoryIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickCategoryNavigation(
        categoryIdL2: String
    ) {
        analytic.categoryNavigationAnalytic.sendClickCategoryNavigationEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun impressCategoryNavigation(
        categoryIdL2: String
    ) {
        analytic.categoryNavigationAnalytic.sendImpressionCategoryNavigationEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickCategoryMenu(
        categoryRecomIdL1: String
    ) {
        analytic.categoryMenuAnalytic.sendClickCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = categoryRecomIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun impressCategoryMenu(
        categoryRecomIdL1: String
    ) {
        analytic.categoryMenuAnalytic.sendImpressionCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = categoryRecomIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun clickSeeMoreShowcase(
        categoryIdL2: String
    ) {
        analytic.categoryShowcaseAnalytic.sendClickArrowButtonShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun trackCategoryShowcaseAddToCart(model: CategoryAtcTrackerModel) {
        analytic.categoryShowcaseAnalytic.sendClickAtcOnShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            index = model.index,
            productId = model.product.productId,
            warehouseId = model.warehouseId,
            isOos = model.product.isOos(),
            name = model.product.name,
            price = model.product.getPriceLong(),
            headerName = model.headerName,
            quantity = model.quantity
        )
    }

    private fun trackProductAdsAddToCart(model: CategoryAtcTrackerModel) {
        val title = getString(R.string.tokopedianow_product_ads_carousel_title)
        analytic.productAdsAnalytic.trackProductAddToCart(
            position = model.index,
            title = title,
            quantity = model.quantity,
            shopId = model.shopId,
            shopName = model.shopName,
            shopType = model.shopType,
            categoryBreadcrumbs = model.categoryBreadcrumbs,
            product = model.product
        )
    }

    private fun changeProductCardQuantity(product: CategoryShowcaseItemUiModel, quantity: Int) {
        viewModel.onCartQuantityChanged(
            product = product.productCardModel,
            shopId = product.shopId,
            quantity = quantity,
            layoutType = CATEGORY_SHOWCASE.name
        )
    }

    private fun hideProductRecommendationWidget() = viewModel.removeProductRecommendation()

    private fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun clickWishlistButton(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        if (isWishlistSelected) {
            analytic.categoryOosProductAnalytic.trackClickAddToWishlist(
                warehouseId = viewModel.getWarehouseId(),
                productId = productId
            )
        } else {
            analytic.categoryOosProductAnalytic.trackClickRemoveFromWishlist(
                warehouseId = viewModel.getWarehouseId(),
                productId = productId
            )
        }
        viewModel.updateWishlistStatus(
            productId,
            isWishlistSelected
        )
        showToaster(
            message = descriptionToaster,
            type = type,
            actionText = ctaToaster
        ) {
            ctaClickListener?.invoke()
        }
    }

    /**
     * -- callback function section --
     */

    private fun createTitleCallback() = CategoryTitleCallback(
        context = context,
        warehouseId = viewModel.getWarehouseId(),
        onClickMoreCategories = ::clickMoreCategories
    )

    private fun createCategoryNavigationCallback() = CategoryNavigationCallback(
        onClickCategoryNavigation = ::clickCategoryNavigation,
        onImpressCategoryNavigation = ::impressCategoryNavigation
    )

    private fun createCategoryShowcaseItemCallback() = CategoryShowcaseItemCallback(
        shopId = shopId,
        categoryIdL1 = categoryIdL1,
        onClickProductCard = ::clickProductCardShowcase,
        onImpressProductCard = ::impressProductCardShowcase,
        onAddToCartBlocked = ::showToasterWhenAddToCartBlocked,
        onProductCartQuantityChanged = ::changeProductCardQuantity,
        startActivityForResult = ::startActivityForResult,
        onWishlistButtonClicked = ::clickWishlistButton
    )

    private fun createCategoryShowcaseHeaderCallback() = CategoryShowcaseHeaderCallback(
        onClickSeeMore = ::clickSeeMoreShowcase
    )

    private fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@TokoNowCategoryFragment
    ) {
        viewModel.refreshLayout()
    }

    private fun createTokoNowCategoryMenuCallback() = TokoNowCategoryMenuCallback(
        onClickCategoryMenu = ::clickCategoryMenu,
        onImpressCategoryMenu = ::impressCategoryMenu
    )

    private fun createTokoNowChooseAddressWidgetCallback() = TokoNowChooseAddressWidgetCallback {
        analytic.sendClickWidgetChooseAddressEvent(
            categoryIdL1 = categoryIdL1,
            warehouseId = viewModel.getWarehouseId()
        )
    }

    private fun createProductRecommendationCallback() = CategoryProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        activity = activity,
        categoryIdL1 = categoryIdL1,
        onClickProductCard = ::clickProductCardRecommendation,
        onImpressProductCard = ::impressProductCardRecommendation,
        onOpenLoginPage = ::openLoginPage,
        onAddToCartBlocked = ::showToasterWhenAddToCartBlocked,
        onHideProductRecommendationWidget = ::hideProductRecommendationWidget,
        startActivityResult = ::startActivityForResult
    )

    private fun createProductCardCompactCallback() = ProductCardCompactCallback { productId, similarProductTrackerListener ->
        context?.apply {
            val intent = TokoNowSimilarProductBottomSheetActivity.createNewIntent(
                this,
                productId,
                similarProductTrackerListener
            )
            startActivity(intent)
        }
    }

    private fun createProductCardCompactSimilarProductTrackerCallback(): ProductCardCompactSimilarProductTrackerCallback {
        return ProductCardCompactSimilarProductTrackerCallback(analytic.categoryOosProductAnalytic)
    }

    private fun createProductCardAdsCallback(): CategoryProductCardAdsCallback {
        return CategoryProductCardAdsCallback(
            context = context,
            viewModel = viewModel,
            analytic = analytic.productAdsAnalytic,
            categoryIdL1 = categoryIdL1,
            startActivityResult = ::startActivityForResult
        )
    }
}
