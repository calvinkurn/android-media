package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryPageMapper.mapToShowcaseProductCard
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper.createProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.ProgressBarMapper.createProgressBar
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.common.constant.ConstantValue.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.updateProductAdsQuantity
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.unifycomponents.ticker.TickerData

internal object VisitableMapper {
    const val DEFAULT_PRODUCT_PARENT_ID = "0"
    const val DEFAULT_PRODUCT_QUANTITY = 0

    private val LAYOUT_TYPES = listOf(
        CATEGORY_SHOWCASE.name,
        PRODUCT_ADS_CAROUSEL
    )

    /**
     * -- Header Section --
     */

    fun MutableList<Visitable<*>>.addHeaderSpace(
        space: Int,
        detailResponse: CategoryDetailResponse
    ) {
        add(
            detailResponse.mapToHeaderSpace(
                space = space
            )
        )
    }

    /**
     * -- Choose Address Section --
     */

    fun MutableList<Visitable<*>>.addChooseAddress(
        detailResponse: CategoryDetailResponse
    )  {
        add(detailResponse.mapToChooseAddress())
    }

    /**
     * -- Ticker Section --
     */

    fun MutableList<Visitable<*>>.addTicker(
        detailResponse: CategoryDetailResponse,
        tickerData: Pair<Boolean, List<TickerData>>?
    ): Boolean  {
        return if (tickerData != null) {
            add(detailResponse.mapToTicker(tickerData))
            tickerData.first
        } else {
            false
        }
    }

    /**
     * -- Category Title Section --
     */

    fun MutableList<Visitable<*>>.addCategoryTitle(
        detailResponse: CategoryDetailResponse
    ) {
        add(detailResponse.mapToCategoryTitle())
    }

    /**
     * -- Category Navigation Section --
     */

    fun MutableList<Visitable<*>>.addCategoryNavigation(
        categoryNavigationUiModel: CategoryNavigationUiModel
    ) {
        add(categoryNavigationUiModel)
    }

    /**
     * -- Product Recommendation Section --
     */

    fun MutableList<Visitable<*>>.addProductRecommendation(categoryId: List<String>) {
        add(createProductRecommendation(categoryId))
    }

    /**
     * -- Showcase Section --
     */

    fun MutableList<Visitable<*>>.addCategoryShowcase(
        totalData: Int = Int.ZERO,
        productList: List<AceSearchProductModel.Product> = listOf(),
        categoryIdL2: String = String.EMPTY,
        title: String = String.EMPTY,
        seeAllAppLink: String = String.EMPTY,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean,
        @TokoNowLayoutState state: Int
    ) {
        add(
            mapToShowcaseProductCard(
                totalData = totalData,
                productList = productList,
                categoryIdL2 = categoryIdL2,
                title = title,
                state = state,
                seeAllAppLink = seeAllAppLink,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        )
    }

    fun MutableList<Visitable<*>>.mapCategoryShowcase(
        totalData: Int,
        productList: List<AceSearchProductModel.Product>,
        categoryIdL2: String,
        title: String,
        seeAllAppLink: String,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        updateItemById(
            id = categoryIdL2,
            block = {
                mapToShowcaseProductCard(
                    totalData = totalData,
                    productList = productList,
                    categoryIdL2 = categoryIdL2,
                    title = title,
                    state = TokoNowLayoutState.SHOW,
                    seeAllAppLink = seeAllAppLink,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            }
        )
    }

    fun MutableList<Visitable<*>>.findCategoryShowcaseItem(productId: String): CategoryShowcaseItemUiModel? {
        var categoryShowcase: CategoryShowcaseItemUiModel? = null
        filterIsInstance<CategoryShowcaseUiModel>().forEach { showcase ->
            showcase.productListUiModels.firstOrNull { it.getProductId() == productId }?.let {
                categoryShowcase = it
            }
        }
        return categoryShowcase
    }

    /**
     * -- Progressbar Section --
     */

    fun MutableList<Visitable<*>>.addProgressBar() {
        add(createProgressBar())
    }

    /**
     * -- Category Menu Section --
     */

    fun MutableList<Visitable<*>>.addCategoryMenu(
        categoryMenuUiModel: TokoNowCategoryMenuUiModel
    ) {
        add(categoryMenuUiModel)
    }

    /**
     * -- Product Section --
     */

    private fun removeProductAtcQuantity(
        productId: String,
        parentProductId: String,
        miniCartData: MiniCartSimplifiedData,
        updateProductQuantity: (String, Int) -> Unit
    ) {
        if (parentProductId != DEFAULT_PRODUCT_PARENT_ID) {
            val totalQuantity = miniCartData.miniCartItems
                .getMiniCartItemParentProduct(parentProductId)?.totalQuantity.orZero()
            if (totalQuantity == DEFAULT_PRODUCT_QUANTITY) {
                updateProductQuantity(productId, DEFAULT_QUANTITY)
            } else {
                updateProductQuantity(productId, totalQuantity)
            }
        } else {
            updateProductQuantity(productId, DEFAULT_QUANTITY)
        }
    }

    private fun MutableList<Visitable<*>>.updateShowcaseProductQuantity(
        productId: String,
        quantity: Int
    ) {
        filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
            val productUiModel = uiModel.productListUiModels.firstOrNull { it.productCardModel.productId == productId }

            productUiModel?.apply {
                if (productCardModel.orderQuantity != quantity) {
                    val index = uiModel.productListUiModels.indexOf(this)

                    uiModel.productListUiModels.getOrNull(index)?.productCardModel?.copy(orderQuantity = quantity)?.let {
                        uiModel.productListUiModels[index].copy(productCardModel = it)
                    }?.let { resultUiModel ->
                        val productListUiModels: MutableList<CategoryShowcaseItemUiModel> = uiModel.productListUiModels.toMutableList()
                        productListUiModels[index] = resultUiModel

                        updateItemById(uiModel.getVisitableId()) {
                            uiModel.copy(productListUiModels = productListUiModels)
                        }
                    }
                }
            }
        }
    }

    private fun MutableList<Visitable<*>>.updateProductAdsQuantity(
        productId: String,
        quantity: Int
    ) {
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { item ->
            updateItemById(item.id) {
                item.updateProductAdsQuantity(productId, quantity)
            }
        }
    }

    private fun MutableList<Visitable<*>>.updateAllProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        layoutType: String
    ) {
        miniCartData.miniCartItems.values.map { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct) {
                val productId = miniCartItem.productId
                val quantity = HomeLayoutMapper.getAddToCartQuantity(productId, miniCartData)
                updateProductQuantity(productId, quantity, layoutType)
            }
        }
    }

    fun MutableList<Visitable<*>>.updateProductQuantity(
        productId: String,
        quantity: Int,
        layoutType: String
    ) {
        when (layoutType) {
            CATEGORY_SHOWCASE.name -> updateShowcaseProductQuantity(productId, quantity)
            PRODUCT_ADS_CAROUSEL -> updateProductAdsQuantity(productId, quantity)
        }
    }

    private fun MutableList<Visitable<*>>.updateDeletedProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        layoutType: String
    ) {
        val cartProductIds = miniCartData.miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemProduct) it.productId else null
        }

        when (layoutType) {
            CATEGORY_SHOWCASE.name -> updateDeletedShowcaseProduct(cartProductIds, miniCartData)
            PRODUCT_ADS_CAROUSEL -> updateDeletedProductAds(cartProductIds, miniCartData)
        }
    }

    private fun MutableList<Visitable<*>>.updateDeletedShowcaseProduct(
        cartProductIds: List<String>,
        miniCartData: MiniCartSimplifiedData
    ) {
        filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
            uiModel.productListUiModels.filter { it.getProductId() !in cartProductIds }.forEach {
                removeProductAtcQuantity(
                    productId = it.getProductId(),
                    parentProductId = it.parentProductId,
                    miniCartData = miniCartData
                ) { productId, quantity ->
                    updateShowcaseProductQuantity(productId, quantity)
                }
            }
        }
    }

    private fun MutableList<Visitable<*>>.updateDeletedProductAds(
        cartProductIds: List<String>,
        miniCartData: MiniCartSimplifiedData
    ) {
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { uiModel ->
            uiModel.items.filter { it.getProductId() !in cartProductIds }.forEach {
                removeProductAtcQuantity(
                    productId = it.getProductId(),
                    parentProductId = it.parentId,
                    miniCartData = miniCartData
                ) { productId, quantity ->
                    updateProductAdsQuantity(productId, quantity)
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.updateProductQuantity(miniCartData: MiniCartSimplifiedData) {
        LAYOUT_TYPES.forEach { layoutType ->
            updateAllProductQuantity(
                miniCartData = miniCartData,
                layoutType = layoutType
            )
            updateDeletedProductQuantity(
                miniCartData = miniCartData,
                layoutType = layoutType
            )
        }
    }

    fun MutableList<Visitable<*>>.updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
            uiModel.productListUiModels.find { it.productCardModel.productId == productId }?.apply {
                val index = uiModel.productListUiModels.indexOf(this)

                uiModel.productListUiModels.getOrNull(index)?.productCardModel?.copy(hasBeenWishlist = hasBeenWishlist)?.let {
                    uiModel.productListUiModels[index].copy(productCardModel = it)
                }?.let { resultUiModel ->
                    val productListUiModels: MutableList<CategoryShowcaseItemUiModel> = uiModel.productListUiModels.toMutableList()
                    productListUiModels[index] = resultUiModel

                    updateItemById(uiModel.getVisitableId()) {
                        uiModel.copy(productListUiModels = productListUiModels)
                    }
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.mapProductAdsCarousel(
        response: ProductAdsResponse,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        updateItemById(PRODUCT_ADS_CAROUSEL) {
            ProductAdsMapper.mapProductAdsCarousel(
                response,
                miniCartData,
                hasBlockedAddToCart
            )
        }
    }

    /**
     * -- Others Section --
     */

    private fun MutableList<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun MutableList<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is CategoryShowcaseUiModel -> id
            is TokoNowProgressBarUiModel -> id
            is TokoNowProductRecommendationUiModel -> id
            is TokoNowAdsCarouselUiModel -> id
            else -> null
        }
    }

    fun MutableList<Visitable<*>>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }
}
