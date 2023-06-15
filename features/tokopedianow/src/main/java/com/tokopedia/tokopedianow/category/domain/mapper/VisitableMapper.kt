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
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.unifycomponents.ticker.TickerData

internal object VisitableMapper {
    const val DEFAULT_PRODUCT_PARENT_ID = "0"
    const val DEFAULT_PRODUCT_QUANTITY = 0

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

    private fun MutableList<Visitable<*>>.removeShowcaseProductAtc(
        productId: String,
        parentProductId: String,
        miniCartData: MiniCartSimplifiedData
    ) {
        if (parentProductId != DEFAULT_PRODUCT_PARENT_ID) {
            val totalQuantity = miniCartData.miniCartItems.getMiniCartItemParentProduct(parentProductId)?.totalQuantity.orZero()
            if (totalQuantity == DEFAULT_PRODUCT_QUANTITY) {
                updateShowcaseProductQuantity(productId, HomeLayoutMapper.DEFAULT_QUANTITY)
            } else {
                updateShowcaseProductQuantity(productId, totalQuantity)
            }
        } else {
            updateShowcaseProductQuantity(productId, HomeLayoutMapper.DEFAULT_QUANTITY)
        }
    }

    private fun MutableList<Visitable<*>>.updateShowcaseProductQuantity(
        productId: String,
        quantity: Int
    ) {
        filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
            val productUiModel = uiModel.productListUiModels?.firstOrNull { it.productCardModel.productId == productId }

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

    private fun MutableList<Visitable<*>>.updateAllProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        layoutType: CategoryLayoutType
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
        layoutType: CategoryLayoutType
    ) {
        when (layoutType) {
            CategoryLayoutType.CATEGORY_SHOWCASE -> updateShowcaseProductQuantity(
                productId = productId,
                quantity = quantity
            )
            else -> { /* no more layout type */ }
        }
    }

    private fun MutableList<Visitable<*>>.updateDeletedProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        layoutType: CategoryLayoutType
    ) {
        when (layoutType) {
            CategoryLayoutType.CATEGORY_SHOWCASE -> {
                filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
                    val cartProductIds = miniCartData.miniCartItems.values.mapNotNull { if (it is MiniCartItem.MiniCartItemProduct) it.productId else null }
                    val deletedProducts = uiModel.productListUiModels?.filter { it.productCardModel.productId !in cartProductIds }

                    deletedProducts?.forEach { item ->
                        removeShowcaseProductAtc(item.productCardModel.productId, item.parentProductId, miniCartData)
                    }
                }
            }
            else -> { /* no more layout type */ }
        }
    }

    fun MutableList<Visitable<*>>.updateProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        layoutType: CategoryLayoutType
    ) {
        updateAllProductQuantity(
            miniCartData = miniCartData,
            layoutType = layoutType
        )
        updateDeletedProductQuantity(
            miniCartData = miniCartData,
            layoutType = layoutType
        )
    }

    fun MutableList<Visitable<*>>.updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        filterIsInstance<CategoryShowcaseUiModel>().forEach { uiModel ->
            uiModel.productListUiModels?.find { it.productCardModel.productId == productId }?.apply {
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
            else -> null
        }
    }

    fun MutableList<Visitable<*>>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }
}
