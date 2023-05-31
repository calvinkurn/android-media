package com.tokopedia.tokopedianow.category.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductRecommendationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseHeaderCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseItemCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowCategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryMainFragment : TokoNowCategoryBaseFragment() {

    companion object {
        fun newInstance(): TokoNowCategoryMainFragment {
            return TokoNowCategoryMainFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowCategoryMainViewModel

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    @Inject
    override lateinit var analytic: CategoryAnalytic

    override val userId: String
        get() = viewModel.getUserId()

    override val categoryIdL1: String
        get() = viewModel.categoryIdL1

    override val categoryIdL2: String
        get() = String.EMPTY

    override val categoryIdL3: String
        get() = String.EMPTY

    override val shopId: String
        get() = viewModel.getShopId().toString()

    override val currentCategoryId: String
        get() = viewModel.categoryIdL1

    override val addressData: LocalCacheModel
        get() = viewModel.getAddressData()

    override val adapter: CategoryAdapter by lazy {
        CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                categoryTitleListener = createTitleCallback(),
                categoryNavigationListener = createCategoryNavigationCallback(),
                categoryShowcaseItemListener = createCategoryShowcaseItemCallback(),
                categoryShowcaseHeaderListener = createCategoryShowcaseHeaderCallback(),
                tokoNowView = createTokoNowViewCallback(),
                tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
                tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
                tokoNowProductRecommendationListener = createProductRecommendationCallback()
            ),
            differ = CategoryDiffer()
        )
    }

    /**
     * -- override function section --
     */

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAffiliateCookie()
        getCategoryHeader()
        observer()
    }

    override fun loadMore(isAtTheBottomOfThePage: Boolean) =
        viewModel.loadMore(isAtTheBottomOfThePage)

    override fun refreshLayout() = viewModel.refreshLayout()

    override fun getMiniCart() = viewModel.getMiniCart()

    /**
     * -- private function section --
     */

    private fun getCategoryHeader() {
        binding?.navToolbar?.post {
            viewModel.getCategoryHeader(navToolbarHeight)
        }
    }

    private fun initAffiliateCookie() = viewModel.initAffiliateCookie()

    private fun observer() {
        observeCategoryHeader()
        observeCategoryPage()
        observeScrollNotNeeded()
        observeMiniCart()
        observeAddToCart()
        observeUpdateCartItem()
        observeRemoveCartItem()
        observeToolbarNotification()
        observeProductRecommendationAddToCart()
        observeProductRecommendationRemoveCartItem()
        observeProductRecommendationUpdateCartItem()
        observeProductRecommendationToolbarNotification()
        observeRefreshState()
        observeOosState()
        observeOpenScreenTracker()
    }

    private fun observeCategoryHeader() {
        viewModel.categoryHeader.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    adapter.submitList(result.data)

                    viewModel.getFirstPage()

                    binding?.showMainLayout()
                }
                is Fail -> binding?.showErrorLayout(
                    throwable = result.throwable
                )
            }
        }
    }

    private fun observeCategoryPage() {
        viewModel.categoryPage.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

    private fun observeScrollNotNeeded() {
        viewModel.scrollNotNeeded.observe(viewLifecycleOwner) { isNotNeeded ->
            if (isNotNeeded) {
                binding?.rvCategory?.removeOnScrollListener(onScrollListener)
            }
        }
    }

    private fun observeMiniCart() {
        viewModel.miniCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val data = result.data
                    showMiniCart(data)
                    setupPadding(data)
                    productRecommendationViewModel.updateMiniCartSimplified(result.data)
                }
                is Fail -> {
                    hideMiniCart()
                    resetPadding()
                }
            }
        }
    }

    private fun observeAddToCart() {
        viewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
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

    private fun observeUpdateCartItem() {
        viewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessUpdateCartItem()
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeRemoveCartItem() {
        viewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
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

    private fun observeToolbarNotification() {
        viewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
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

    private fun observeRefreshState() {
        viewModel.refreshState.observe(viewLifecycleOwner) {
            binding?.apply {
                showShimmeringLayout()
                rvCategory.removeOnScrollListener(onScrollListener)
                rvCategory.addOnScrollListener(onScrollListener)
            }
            viewModel.getCategoryHeader(navToolbarHeight)
        }
    }

    private fun observeOosState() {
        viewModel.oosState.observe(viewLifecycleOwner) {
            binding?.showOosLayout()
            analytic.sendOocOpenScreenEvent(viewModel.isLoggedIn())
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

    private fun changeProductCardQuantity(
        position: Int,
        product: CategoryShowcaseItemUiModel,
        quantity: Int
    ) {
        if (!viewModel.isLoggedIn()) {
            openLoginPage()
        } else {
            viewModel.onCartQuantityChanged(
                productId = product.productCardModel.productId,
                quantity = quantity,
                stock = product.productCardModel.availableStock,
                shopId = shopId,
                layoutType = CategoryLayoutType.CATEGORY_SHOWCASE
            )
        }
    }

    private fun hideProductRecommendationWidget() = viewModel.removeProductRecommendation()

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
    )

    private fun createCategoryShowcaseHeaderCallback() = CategoryShowcaseHeaderCallback(
        onClickSeeMore = ::clickSeeMoreShowcase
    )

    private fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@TokoNowCategoryMainFragment
    ) {
        viewModel.refreshLayout()
    }

    private fun createTokoNowCategoryMenuCallback() = TokoNowCategoryMenuCallback(
        onClickCategoryMenu = ::clickCategoryMenu,
        onImpressCategoryMenu = ::impressCategoryMenu
    )

    private fun createTokoNowChooseAddressWidgetCallback() = TokoNowChooseAddressWidgetCallback()

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
}
