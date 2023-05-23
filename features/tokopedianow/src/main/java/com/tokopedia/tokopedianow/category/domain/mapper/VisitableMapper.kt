package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryPageMapper.mapToShowcaseProductCard
import com.tokopedia.tokopedianow.category.domain.response.CategoryHeaderResponse
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

internal object VisitableMapper {
    const val DEFAULT_PRODUCT_PARENT_ID = "0"
    const val DEFAULT_PRODUCT_QUANTITY = 0

    /**
     * -- Header Section --
     */

    fun MutableList<Visitable<*>>.addHeaderSpace(
        space: Int,
        headerResponse: CategoryHeaderResponse
    ) {
        add(
            headerResponse.mapToHeaderSpace(
                space = space
            )
        )
    }

    /**
     * -- Choose Address Section --
     */

    fun MutableList<Visitable<*>>.addChooseAddress(
        headerResponse: CategoryHeaderResponse
    )  {
        add(headerResponse.mapToChooseAddress())
    }

    /**
     * -- Category Title Section --
     */

    fun MutableList<Visitable<*>>.addCategoryTitle(
        headerResponse: CategoryHeaderResponse
    ) {
        add(headerResponse.mapToCategoryTitle())
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
        add(
            TokoNowProductRecommendationUiModel(
                id = CategoryLayoutType.PRODUCT_RECOMMENDATION.name,
                requestParam = GetRecommendationRequestParam(
                    queryParam = TOKONOW_CLP,
                    pageName = TOKONOW_CLP,
                    categoryIds = categoryId,
                    xSource = RECOM_WIDGET,
                    isTokonow = true,
                    pageNumber = PAGE_NUMBER_RECOM_WIDGET,
                    xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE,
                )
            )
        )
    }

    /**
     * -- Showcase Section --
     */

    fun MutableList<Visitable<*>>.addCategoryShowcase(
        model: AceSearchProductModel = AceSearchProductModel(),
        categoryIdL2: String = String.EMPTY,
        title: String = String.EMPTY,
        seeAllAppLink: String = String.EMPTY,
        miniCartData: MiniCartSimplifiedData?,
        @TokoNowLayoutState state: Int
    ) {
        add(
            model.mapToShowcaseProductCard(
                categoryIdL2 = categoryIdL2,
                title = title,
                state = state,
                seeAllAppLink = seeAllAppLink,
                miniCartData = miniCartData
            )
        )
    }

    fun MutableList<Visitable<*>>.mapCategoryShowcase(
        model: AceSearchProductModel,
        categoryIdL2: String,
        title: String,
        seeAllAppLink: String,
        miniCartData: MiniCartSimplifiedData?
    ) {
        updateItemById(
            id = categoryIdL2,
            block = {
                model.mapToShowcaseProductCard(
                    categoryIdL2 = categoryIdL2,
                    title = title,
                    state = TokoNowLayoutState.SHOW,
                    seeAllAppLink = seeAllAppLink,
                    miniCartData = miniCartData
                )
            }
        )
    }

    /**
     * -- Progressbar Section --
     */

    fun MutableList<Visitable<*>>.addRecipeProgressBar() {
        add(TokoNowProgressBarUiModel(CategoryLayoutType.MORE_PROGRESS_BAR.name))
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
