package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.mapResponseToProductItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryProductMapper.updateProductCardItems
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.FEATURED_PRODUCT
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_FILTER
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.PRODUCT_LIST_INFINITE_SCROLL
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.STATIC_TEXT
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategorySortFilterItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

object CategoryL2TabMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        FEATURED_PRODUCT,
        PRODUCT_LIST_FILTER,
        STATIC_TEXT,
        PRODUCT_LIST_INFINITE_SCROLL
    )

    fun MutableList<Visitable<*>>.mapToCategoryTabLayout(components: List<Component>) {
        components.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }.forEach {
            when (it.type) {
                PRODUCT_LIST_FILTER -> addQuickFilter(it)
                PRODUCT_LIST_INFINITE_SCROLL -> addProductList(it)
            }
        }
    }

    private fun MutableList<Visitable<*>>.addQuickFilter(response: Component) {
        add(CategoryQuickFilterUiModel(id = response.id))
    }

    private fun MutableList<Visitable<*>>.addProductList(response: Component) {
        add(CategoryProductListUiModel(id = response.id))
    }

    fun MutableList<Visitable<*>>.mapToQuickFilter(
        productFilterItem: CategoryQuickFilterUiModel,
        filterResponse: DynamicFilterModel
    ) {
        val filterListResponse = filterResponse.data.filter

        val filterItemList = filterListResponse.map {
            val showNewNotification = it.options.firstOrNull()?.isNew ?: false
            CategorySortFilterItemUiModel(it.title, showNewNotification)
        }

        updateItemById(productFilterItem.id) {
            productFilterItem.copy(
                itemList = filterItemList,
                state = TokoNowLayoutState.LOADED
            )
        }
    }

    fun MutableList<Visitable<*>>.addProductCardItems(
        response: AceSearchProductModel,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ) {
        response.searchProduct.data.productList.forEachIndexed { index, product ->
            add(mapResponseToProductItem(index, product, miniCartData, hasBlockedAddToCart))
        }
    }

    fun MutableList<Visitable<*>>.updateAllProductQuantity(
        miniCartData: MiniCartSimplifiedData,
        hasBlockedAddToCart: Boolean
    ) {
        val cartProductIds = miniCartData.miniCartItems.values.mapNotNull {
            if (it is MiniCartItem.MiniCartItemProduct) it.productId else null
        }
        updateProductCardItems(cartProductIds, miniCartData, hasBlockedAddToCart)
    }

    fun MutableList<Visitable<*>>.filterNotLoadedLayout(): MutableList<Visitable<*>> {
        return filter { it.getLayoutState() == TokoNowLayoutState.LOADING }.toMutableList()
    }

    fun List<Component>.filterTabComponents(): List<Component> {
        return filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }
    }

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
            is TokoNowAdsCarouselUiModel -> id
            is CategoryQuickFilterUiModel -> id
            else -> null
        }
    }

    private fun Visitable<*>.getLayoutState(): Int? {
        return when (this) {
            is TokoNowAdsCarouselUiModel -> state
            is CategoryQuickFilterUiModel -> state
            is CategoryProductListUiModel -> state
            else -> null
        }
    }

    fun MutableList<Visitable<*>>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }
}
