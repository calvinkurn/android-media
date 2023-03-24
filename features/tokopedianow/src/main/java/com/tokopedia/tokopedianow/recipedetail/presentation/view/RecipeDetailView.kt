package com.tokopedia.tokopedianow.recipedetail.presentation.view

import androidx.fragment.app.FragmentActivity
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics

interface RecipeDetailView {

    fun showChooseAddressBottomSheet()
    fun onCartQuantityChanged(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    )
    fun getFragmentActivity(): FragmentActivity?
    fun getProductTracker(): RecipeProductAnalytics
    fun getTracker(): RecipeDetailAnalytics
    fun createAffiliateLink(url: String): String
}
