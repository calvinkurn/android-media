package com.tokopedia.tokopedianow.shoppinglist.domain.extension

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_OOC_PARAM
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_OOC
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder.Companion.SOURCE
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.EXPAND
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.MAX_TOTAL_PRODUCT_DISPLAYED
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST

internal object MainVisitableExtension {
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
    ): MutableList<Visitable<*>> {
        add(
            TokoNowThematicHeaderUiModel(
                pageTitle = headerModel.pageTitle,
                pageTitleColor = headerModel.pageTitleColor,
                ctaText = headerModel.ctaText,
                ctaTextColor = headerModel.ctaTextColor,
                ctaChevronColor = headerModel.ctaChevronColor,
                backgroundGradientColor = headerModel.backgroundGradientColor,
                isSuperGraphicImageShown = headerModel.isSuperGraphicImageShown,
                iconPullRefreshType = headerModel.iconPullRefreshType,
                chosenAddress = TokoNowThematicHeaderUiModel.ChosenAddress(
                    isShown = headerModel.isChooseAddressShown,
                    chooseAddressResIntColor = headerModel.chooseAddressResIntColor
                ),
                state = state
            )
        )
        return this
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
        add(ShoppingListLoadingMoreUiModel())
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

    fun MutableList<Visitable<*>>.addLocalLoad(): MutableList<Visitable<*>> {
        add(TokoNowLocalLoadUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.getExpandCollapse(
        productLayoutType: ShoppingListProductLayoutType
    ): ShoppingListExpandCollapseUiModel? = firstOrNull { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == productLayoutType } as? ShoppingListExpandCollapseUiModel

    fun MutableList<Visitable<*>>.removeLoadMore(): MutableList<Visitable<*>> {
        removeFirst { it is ShoppingListLoadingMoreUiModel }
        return this
    }

    fun MutableList<Visitable<*>>.removeRetry(): MutableList<Visitable<*>> {
        removeFirst { it is ShoppingListRetryUiModel }
        return this
    }

    fun MutableList<Visitable<*>>.addProductCarts(
        products: List<ShoppingListCartProductItemUiModel>
    ): MutableList<Visitable<*>> {
        add(
            ShoppingListCartProductUiModel(
                productList = products.toMutableList()
            )
        )
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

    fun MutableList<Visitable<*>>.addEmptyStateOoc(): MutableList<Visitable<*>> {
        add(
            TokoNowEmptyStateOocUiModel(
                hostSource = SOURCE,
                serviceType = NOW_OOC
            )
        )
        return this
    }

    fun MutableList<Visitable<*>>.addProductRecommendationOoc(): MutableList<Visitable<*>> {
        add(
            TokoNowProductRecommendationOocUiModel(
                pageName = PAGE_NAME_RECOMMENDATION_OOC_PARAM,
                isFirstLoad = true,
                isBindWithPageName = true,
                isTokoNow = false,
                miniCartSource = MiniCartSource.TokonowShoppingList
            )
        )
        return this
    }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.removeProduct(
        productId: String
    ) = removeFirst { it.id == productId }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.updateProductSelections(
        productId: String = String.EMPTY,
        isSelected: Boolean
    ): MutableList<ShoppingListHorizontalProductCardItemUiModel> {
        val products = if (productId.isNotBlank()) {
            map { product -> if (product.id == productId) product.copy(isSelected = isSelected) else product }.toList()
        } else {
            map { it.copy(isSelected = isSelected) }.toList()
        }
        clear()
        addAll(products)
        return this
    }

    fun MutableList<ShoppingListCartProductItemUiModel>.addProductCartItem(
        productList: MutableList<ShoppingListHorizontalProductCardItemUiModel>,
        miniCartItems: List<MiniCartItem.MiniCartItemProduct>
    ): MutableList<ShoppingListHorizontalProductCardItemUiModel> {
        val iterator = productList.iterator()
        while (iterator.hasNext()) {
            val nextProduct = iterator.next()
            for (miniCartItem in miniCartItems) {
                if (nextProduct.id == miniCartItem.productId) {
                    add(ShoppingListCartProductItemUiModel(nextProduct.image, nextProduct.productLayoutType))
                    iterator.remove()
                    break
                }
            }
        }
        return productList
    }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.filteredBy(
        productList: MutableList<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<ShoppingListHorizontalProductCardItemUiModel> {
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val nextProduct = iterator.next()
            for (product in productList) {
                if (nextProduct.id == product.id) {
                    iterator.remove()
                    break
                }
            }
        }
        return this
    }
}
