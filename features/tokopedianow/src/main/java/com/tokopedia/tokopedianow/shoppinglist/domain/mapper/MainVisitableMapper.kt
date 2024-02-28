package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.EXPAND
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.MAX_TOTAL_PRODUCT_DISPLAYED
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST

internal object MainVisitableMapper {
    fun mapAvailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.AvailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listAvailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            priceInt = it.priceInt,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            isSelected = it.isSelected,
            appLink = it.applink,
            productLayoutType = AVAILABLE_SHOPPING_LIST
        )
    }

    fun mapUnavailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.UnavailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listAvailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            appLink = it.applink,
            productLayoutType = UNAVAILABLE_SHOPPING_LIST
        )
    }

    fun MutableList<Visitable<*>>.addShoppingListProducts(
        products: List<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<Visitable<*>> {
        addAll(products)
        return this
    }

    fun MutableList<Visitable<*>>.addLoadingState(): MutableList<Visitable<*>> {
        val newList = arrayListOf(
            TokoNowThematicHeaderUiModel(
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = AVAILABLE_SHOPPING_LIST,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = AVAILABLE_SHOPPING_LIST,
                state = LOADING
            ),
            TokoNowDividerUiModel(),
            TokoNowTitleUiModel(
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                productLayoutType = PRODUCT_RECOMMENDATION,
                state = LOADING
            )
        )
        addAll(newList)
        return this
    }

    fun MutableList<Visitable<*>>.addHeader(
        headerModel: HeaderModel,
        @TokoNowLayoutState state: Int
    ) {
        add(
            TokoNowThematicHeaderUiModel(
                pageTitle = headerModel.pageTitle,
                pageTitleColor = headerModel.pageTitleColor,
                ctaText = headerModel.ctaText,
                ctaTextColor = headerModel.ctaTextColor,
                ctaChevronIsShown = headerModel.ctaChevronIsShown,
                ctaChevronColor = headerModel.ctaChevronColor,
                backgroundGradientColor = headerModel.backgroundGradientColor,
                state = state
            )
        )
    }

    fun MutableList<Visitable<*>>.addEmptyShoppingList(): MutableList<Visitable<*>> {
        add(ShoppingListEmptyUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addTopCheckAllShoppingList(
        productState: ShoppingListProductState,
        isSelected: Boolean
    ): MutableList<Visitable<*>> {
        add(
            ShoppingListTopCheckAllUiModel(
                productState = productState,
                isSelected = isSelected
            )
        )
        return this
    }

    fun MutableList<Visitable<*>>.addProductCartWidget(
        productList: List<ShoppingListProductCartItemUiModel>
    ) {
        add(
            ShoppingListProductCartUiModel(
                productList = productList
            )
        )
    }

    fun MutableList<Visitable<*>>.addExpandCollapse(
        productState: ShoppingListProductState,
        productLayoutType: ShoppingListProductLayoutType,
        remainingTotalProduct: Int
    ): MutableList<Visitable<*>> {
        add(
            ShoppingListExpandCollapseUiModel(
                productState = productState,
                remainingTotalProduct = remainingTotalProduct,
                productLayoutType = productLayoutType
            )
        )
        return this
    }

    fun MutableList<Visitable<*>>.addTitle(
        title: String
    ): MutableList<Visitable<*>> {
        add(TokoNowTitleUiModel(title = title))
        return this
    }

    fun MutableList<Visitable<*>>.addLoadMore(): MutableList<Visitable<*>> {
        add(LoadingMoreModel())
        return this
    }

    fun MutableList<Visitable<*>>.addRetry(): MutableList<Visitable<*>> {
        add(ShoppingListRetryUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addDivider(): MutableList<Visitable<*>> {
        add(TokoNowDividerUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.removeLoadMore(): MutableList<Visitable<*>> {
        removeFirst { it is LoadingMoreModel }
        return this
    }

    fun MutableList<Visitable<*>>.removeRetry(): MutableList<Visitable<*>> {
        removeFirst { it is ShoppingListRetryUiModel }
        return this
    }

    fun MutableList<Visitable<*>>.modifyExpandCollapseProducts(
        state: ShoppingListProductState,
        productLayoutType: ShoppingListProductLayoutType,
        products: List<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<Visitable<*>> {
        val firstProductIndex = indexOfFirst { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == productLayoutType }
        val lastProductIndex = indexOfLast { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == productLayoutType }

        if (firstProductIndex != INVALID_INDEX && lastProductIndex != INVALID_INDEX) {
            subList(
                firstProductIndex,
                lastProductIndex.inc()
            ).clear()

            addAll(firstProductIndex, if (state == EXPAND) products else products.take(MAX_TOTAL_PRODUCT_DISPLAYED))
        }

        return this
    }

    fun MutableList<Visitable<*>>.modifyTopCheckAllState(
        productState: ShoppingListProductState
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListTopCheckAllUiModel }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListTopCheckAllUiModel
            removeAt(index)
            add(index, item.copy(productState = productState))
        }

        return this
    }

    fun MutableList<Visitable<*>>.modifyExpandCollapseState(
        productState: ShoppingListProductState,
        productLayoutType: ShoppingListProductLayoutType
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == productLayoutType }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListExpandCollapseUiModel
            removeAt(index)
            add(index, item.copy(productState = productState))
        }

        return this
    }

    fun MutableList<Visitable<*>>.modifyTopCheckAll(
        isSelected: Boolean
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListTopCheckAllUiModel }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListTopCheckAllUiModel
            removeAt(index)
            add(index, item.copy(isSelected = isSelected))
        }

        return this
    }

    fun MutableList<Visitable<*>>.modifyProduct(
        productId: String,
        isSelected: Boolean
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListHorizontalProductCardItemUiModel && it.id == productId }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListHorizontalProductCardItemUiModel
            removeAt(index)
            add(index, item.copy(isSelected = isSelected))
        }

        return this
    }

    fun MutableList<Visitable<*>>.doIf(
        predicate: Boolean,
        then: MutableList<Visitable<*>>.() -> Unit,
        ifNot: MutableList<Visitable<*>>.() -> Unit = {}
    ): MutableList<Visitable<*>> {
        if (predicate) then.invoke(this) else ifNot.invoke(this)
        return this
    }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.updateProductSelections(
        productId: String = String.EMPTY,
        isSelected: Boolean
    ) {
        val products = if (productId.isNotBlank()) {
            map { product -> if (product.id == productId) product.copy(isSelected = isSelected) else product }.toList()
        } else {
            map { it.copy(isSelected = isSelected) }.toList()
        }
        clear()
        addAll(products)
    }

    fun MutableList<ShoppingListProductCartItemUiModel>.addProductCartItem(
        productList: MutableList<ShoppingListHorizontalProductCardItemUiModel>,
        miniCartItems: List<MiniCartItem.MiniCartItemProduct>
    ): MutableList<ShoppingListHorizontalProductCardItemUiModel> {
        val iterator = productList.iterator()
        while (iterator.hasNext()) {
            val nextProduct = iterator.next()
            for (miniCartItem in miniCartItems) {
                if (nextProduct.id == miniCartItem.productId) {
                    add(ShoppingListProductCartItemUiModel(nextProduct.image, nextProduct.appLink))
                    iterator.remove()
                    break
                }
            }
        }
        return productList
    }
}
