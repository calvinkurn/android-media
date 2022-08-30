package com.tokopedia.tokopedianow.recipedetail.presentation.view

import androidx.fragment.app.FragmentActivity

interface RecipeDetailView {

    fun showChooseAddressBottomSheet()
    fun deleteCartItem(productId: String)
    fun onQuantityChanged(productId: String, shopId: String, quantity: Int)
    fun addItemToCart(productId: String, shopId: String, quantity: Int)
    fun getFragmentActivity(): FragmentActivity?
}