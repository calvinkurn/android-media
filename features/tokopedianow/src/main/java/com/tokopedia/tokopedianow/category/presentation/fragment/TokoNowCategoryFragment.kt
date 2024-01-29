package com.tokopedia.tokopedianow.category.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryL1Binding
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_REF
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryFragment : BaseCategoryFragment() {

    companion object {
        fun newInstance(
            categoryL1: String,
            queryParamMap: HashMap<String, String>
        ): TokoNowCategoryFragment {
            return TokoNowCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_CATEGORY_ID_L1, categoryL1)
                }
                this.currentCategoryId = categoryL1
                this.queryParamMap = queryParamMap
            }
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
     * -- private variable section --
     */

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryL1Binding>()

    private val recycledViewPool
        get() = RecyclerView.RecycledViewPool()

    /**
     * -- override function section --
     */

    override val viewModel: TokoNowCategoryViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[TokoNowCategoryViewModel::class.java]
    }

    override fun createAdapterTypeFactory(): CategoryAdapterTypeFactory {
        return CategoryAdapterTypeFactory(
            categoryNavigationListener = createCategoryNavigationCallback(),
            categoryShowcaseItemListener = createCategoryShowcaseItemCallback(),
            categoryShowcaseHeaderListener = createCategoryShowcaseHeaderCallback(),
            tokoNowView = createTokoNowViewCallback(),
            tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
            tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
            tokoNowProductRecommendationListener = createProductRecommendationCallback(),
            tokoNowHeaderListener = createTitleCallback(),
            productAdsCarouselListener = createProductCardAdsCallback(),
            recycledViewPool = recycledViewPool,
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun createAdapterDiffer() = CategoryDiffer()

    override val swipeRefreshLayout: SwipeToRefresh?
        get() = binding?.strRefreshLayout

    override val recyclerView: RecyclerView?
        get() = binding?.rvCategory

    override fun createMainView(): View? {
        binding = FragmentTokopedianowCategoryL1Binding.inflate(LayoutInflater.from(context))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setupOnScrollListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewResume()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        super.onCartItemsUpdated(miniCartSimplifiedData)
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onGetMiniCartSuccess(data: MiniCartSimplifiedData) {
        super.onGetMiniCartSuccess(data)
        productRecommendationViewModel.updateMiniCartSimplified(data)
    }

    override fun initInjector() {
        DaggerCategoryComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    /**
     * -- private function section --
     */

    private fun setupOnScrollListener() {
        navToolbar?.let {
            val navScrollListener = createNavRecyclerViewOnScrollListener(it)
            binding?.rvCategory?.addOnScrollListener(navScrollListener)
        }
    }

    private fun observeLiveData() {
        observeScrollNotNeeded()
        observeAtcDataTracker()
        observeProductRecommendationAddToCart()
        observeProductRecommendationRemoveCartItem()
        observeProductRecommendationUpdateCartItem()
        observeProductRecommendationToolbarNotification()
        observeProductRecommendationAtcDataTracker()
        observeSharingModel()
    }

    private fun observeScrollNotNeeded() {
        observe(viewModel.scrollNotNeeded) {
            recyclerView?.removeOnScrollListener(onScrollListener)
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
                    updateMiniCartData()
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
                isOos = model.productRecommendation.productCardModel.isOos(),
                name = model.productRecommendation.getProductName(),
                price = model.productRecommendation.getProductPrice().toIntSafely(),
                headerName = model.productRecommendation.headerName,
                quantity = model.quantity,
                productWarehouseId = model.productRecommendation.productCardModel.warehouseId
            )
        }
    }

    private fun observeSharingModel() {
        observe(viewModel.shareLiveData) {
            setCategorySharingModel(it)
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

    private fun trackCategoryShowcaseAddToCart(model: CategoryAtcTrackerModel) {
        analytic.categoryShowcaseAnalytic.sendClickAtcOnShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            index = model.index,
            productId = model.product.productId,
            isOos = model.product.isOos(),
            name = model.product.name,
            price = model.product.getPriceLong(),
            headerName = model.headerName,
            quantity = model.quantity,
            productWarehouseId = model.product.warehouseId
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

    private fun showToasterWhenAddToCartBlocked() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            type = Toaster.TYPE_ERROR
        )
    }

    /**
     * -- callback function section --
     */

    private fun createTitleCallback() = object : TokoNowHeaderViewHolder.TokoNowHeaderListener {
        override fun onClickCtaHeader() {
            RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY
            )

            analytic.categoryTitleAnalytics.sendClickMoreCategoriesEvent(
                categoryIdL1 = categoryIdL1,
                warehouseIds = viewModel.getWarehouseIds()
            )
        }

        override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView) { /* nothing to do */ }
    }

    private fun createCategoryNavigationCallback() = object : CategoryNavigationViewHolder.CategoryNavigationListener {
        override fun onCategoryNavigationItemClicked(
            data: CategoryNavigationItemUiModel,
            itemPosition: Int
        ) {
            analytic.categoryNavigationAnalytic.sendClickCategoryNavigationEvent(
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = data.id,
                warehouseIds = viewModel.getWarehouseIds()
            )
        }

        override fun onCategoryNavigationItemImpressed(
            data: CategoryNavigationItemUiModel,
            itemPosition: Int
        ) {
            analytic.categoryNavigationAnalytic.sendImpressionCategoryNavigationEvent(
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = data.id,
                warehouseIds = viewModel.getWarehouseIds()
            )
        }

        override fun onCategoryNavigationWidgetRetried() { /* nothing to do */ }

        override fun onCategoryNavigationWidgetImpression(data: CategoryNavigationUiModel) { /* nothing to do temp */ }
    }

    private fun createCategoryShowcaseItemCallback() = object : CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener {
        override fun onProductCardAddVariantClicked(
            context: Context,
            position: Int,
            product: CategoryShowcaseItemUiModel
        ) {
            AtcVariantHelper.goToAtcVariant(
                context = context,
                productId = product.productCardModel.productId,
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = shopId,
                trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
                startActivitResult = ::startActivityForResult
            )
        }

        override fun onProductCardQuantityChanged(
            position: Int,
            product: CategoryShowcaseItemUiModel,
            quantity: Int
        ) {
            viewModel.onCartQuantityChanged(
                product = product.productCardModel,
                shopId = product.shopId,
                quantity = quantity,
                layoutType = CATEGORY_SHOWCASE.name
            )
        }

        override fun onProductCardClicked(
            context: Context,
            position: Int,
            product: CategoryShowcaseItemUiModel
        ) {
            val appLink = UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                product.productCardModel.productId
            )
            clickProductCard(appLink)

            analytic.categoryShowcaseAnalytic.sendClickProductShowcaseLEvent(
                categoryIdL1 = categoryIdL1,
                headerName = product.headerName,
                index = position.getTrackerPosition(),
                productId = product.productCardModel.productId,
                productWarehouseId = product.productCardModel.warehouseId,
                isOos = product.productCardModel.isOos(),
                name = product.productCardModel.name,
                price = product.productCardModel.price.getDigits()?.toLong().orZero()
            )
        }

        override fun onProductCardImpressed(
            position: Int,
            product: CategoryShowcaseItemUiModel
        ) = analytic.categoryShowcaseAnalytic.sendImpressionProductInShowcaseLEvent(
            categoryIdL1 = categoryIdL1,
            headerName = product.headerName,
            index = position.getTrackerPosition(),
            productId = product.productCardModel.productId,
            productWarehouseId = product.productCardModel.warehouseId,
            isOos = product.productCardModel.isOos(),
            name = product.productCardModel.name,
            price = product.productCardModel.price.getDigits()?.toLong().orZero()
        )

        override fun onProductCardAddToCartBlocked() = showToasterWhenAddToCartBlocked()
    }

    private fun createCategoryShowcaseHeaderCallback() = object : TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {
        override fun onSeeAllClicked(
            context: Context,
            channelId: String,
            headerName: String,
            appLink: String,
            widgetId: String
        ) {
            RouteManager.route(context, appLink)

            analytic.categoryShowcaseAnalytic.sendClickArrowButtonShowcaseLEvent(
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = widgetId,
                headerName = headerName,
                warehouseIds = viewModel.getWarehouseIds()
            )
        }

        override fun onChannelExpired() { /* nothing to do */ }
    }

    private fun createTokoNowCategoryMenuCallback() = object : TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
        override fun onCategoryMenuWidgetRetried() { /* nothing to do */ }

        override fun onSeeAllCategoryClicked() { /* nothing to do */ }

        override fun onCategoryMenuItemClicked(
            data: TokoNowCategoryMenuItemUiModel,
            itemPosition: Int
        ) = analytic.categoryMenuAnalytic.sendClickCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = data.id,
            headerName = data.headerName,
            warehouseIds = viewModel.getWarehouseIds()
        )

        override fun onCategoryMenuItemImpressed(
            data: TokoNowCategoryMenuItemUiModel,
            itemPosition: Int
        ) = analytic.categoryMenuAnalytic.sendImpressionCategoryRecomWidgetEvent(
            categoryIdL1 = categoryIdL1,
            categoryRecomIdL1 = data.id,
            headerName = data.headerName,
            warehouseIds = viewModel.getWarehouseIds()
        )

        override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
    }

    private fun createTokoNowChooseAddressWidgetCallback() = object : TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener {
        override fun onChooseAddressWidgetRemoved() {
            /* nothing to do */
        }

        override fun onClickChooseAddressWidgetTracker() = analytic.sendClickWidgetChooseAddressEvent(
            categoryIdL1 = categoryIdL1,
            warehouseIds = viewModel.getWarehouseIds()
        )
    }

    private fun createProductRecommendationCallback() = object : TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
        override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel = productRecommendationViewModel

        override fun hideProductRecommendationWidget() = viewModel.removeProductRecommendation()

        override fun openLoginPage() = this@TokoNowCategoryFragment.openLoginPage()

        override fun productCardAddVariantClicked(
            productId: String,
            shopId: String
        ) {
            activity?.apply {
                AtcVariantHelper.goToAtcVariant(
                    context = this,
                    productId = productId,
                    pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                    isTokoNow = true,
                    shopId = shopId,
                    trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
                    startActivitResult = ::startActivityForResult
                )
            }
        }

        override fun productCardClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        ) {
            clickProductCard(product.appLink)

            analytic.categoryShowcaseAnalytic.sendClickProductShowcaseLEvent(
                categoryIdL1 = categoryIdL1,
                headerName = product.headerName,
                index = position.getTrackerPosition(),
                productId = product.productCardModel.productId,
                isOos = product.productCardModel.isOos(),
                name = product.productCardModel.name,
                price = product.productCardModel.price.getDigits()?.toLong().orZero(),
                productWarehouseId = product.productCardModel.warehouseId
            )
        }

        override fun productCardImpressed(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            isLogin: Boolean,
            userId: String
        ) {
            analytic.categoryProductRecommendationAnalytic.sendImpressionProductCarouselEvent(
                categoryIdL1 = categoryIdL1,
                headerName = product.headerName,
                index = position.getTrackerPosition(),
                productId = product.productCardModel.productId,
                isOos = product.productCardModel.isOos(),
                name = product.productCardModel.name,
                price = product.productCardModel.price.getDigits()?.toLong().orZero(),
                productWarehouseId = product.productCardModel.warehouseId
            )
        }

        override fun seeMoreClicked(
            seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
        ) {
            directToSeeMorePage(seeMoreUiModel.appLink)
        }

        override fun seeAllClicked(
            appLink: String
        ) {
            directToSeeMorePage(appLink)
        }

        override fun productCardAddToCartBlocked() = showToasterWhenAddToCartBlocked()

        private fun directToSeeMorePage(
            appLink: String
        ) {
            val modifiedAppLink = modifySeeMoreAppLink(appLink)
            RouteManager.route(activity, modifiedAppLink)
        }

        private fun modifySeeMoreAppLink(
            originalAppLink: String
        ): String {
            val uri = Uri.parse(originalAppLink)
            val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: String.EMPTY)
            val ref = queryParamsMap[RECOM_QUERY_PARAM_REF].orEmpty()

            return if (ref == RecomPageConstant.TOKONOW_CLP) {
                val categoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID].orEmpty()

                if (categoryId.isEmpty()) queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = categoryIdL1

                "${uri.scheme}://${uri.host}/${uri.path}?" + UrlParamUtils.generateUrlParamString(queryParamsMap)
            } else {
                originalAppLink
            }
        }
    }

    private fun createProductCardAdsCallback() = object : ProductAdsCarouselListener {
        override fun onProductCardClicked(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {
            RouteManager.route(context, product.appLink)
            analytic.productAdsAnalytic.trackProductClick(position, title, product)
        }

        override fun onProductCardImpressed(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {
            analytic.productAdsAnalytic.trackProductImpression(position, title, product)
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
            context?.apply {
                AtcVariantHelper.goToAtcVariant(
                    context = this,
                    productId = product.getProductId(),
                    pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                    isTokoNow = true,
                    shopId = product.shopId,
                    trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
                    startActivitResult = ::startActivityForResult
                )
            }
        }

        override fun onProductCardAddToCartBlocked() = showToasterWhenAddToCartBlocked()
    }

    private fun createNavRecyclerViewOnScrollListener(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = getNavToolbarHeight(navToolbar) - transitionRange - transitionRange,
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
}
