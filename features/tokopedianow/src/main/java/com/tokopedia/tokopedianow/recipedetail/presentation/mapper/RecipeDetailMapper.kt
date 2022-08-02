package com.tokopedia.tokopedianow.recipedetail.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel

object RecipeDetailMapper {

    fun MutableList<Visitable<*>>.updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        val miniCartItems = miniCart.miniCartItems

        find { it is RecipeTabUiModel }?.let { item ->
            var recipeTab = item as RecipeTabUiModel
            var ingredientTab = recipeTab.ingredient

            val itemIndex = indexOf(recipeTab)
            val itemList = ingredientTab.items.toMutableList()
            val productList = itemList.filterIsInstance<RecipeProductUiModel>()

            miniCartItems.filter { it.value is MiniCartItem.MiniCartItemProduct }
                .forEach { miniCartItem ->
                    val miniCartProduct = miniCartItem.value as MiniCartItem.MiniCartItemProduct
                    productList.firstOrNull { it.id == miniCartProduct.productId }?.let {
                        val product = it.copy(quantity = miniCartProduct.quantity)
                        val index = itemList.indexOf(it)
                        itemList[index] = product
                    }
                }

            ingredientTab = ingredientTab.copy(items = itemList)
            recipeTab = recipeTab.copy(ingredient = ingredientTab)
            set(itemIndex, recipeTab)
        }
    }
}