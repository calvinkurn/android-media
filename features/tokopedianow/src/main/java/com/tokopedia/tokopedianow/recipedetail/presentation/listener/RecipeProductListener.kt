package com.tokopedia.tokopedianow.recipedetail.presentation.listener

import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel

class RecipeProductListener(
    private val viewModel: TokoNowRecipeDetailViewModel
) : RecipeProductViewHolder.RecipeProductListener {

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        viewModel.onQuantityChanged(productId, shopId, quantity)
    }

    override fun onDeleteCartItem(productId: String) {
        val miniCartItem = viewModel.getMiniCartItem(productId)
        val cartId = miniCartItem?.cartId.orEmpty()
        viewModel.removeCartItem(productId, cartId)
    }
}