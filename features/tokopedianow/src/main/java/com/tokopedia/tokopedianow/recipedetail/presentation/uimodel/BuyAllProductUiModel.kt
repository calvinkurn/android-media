package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyAllProductUiModel(
    val totalPrice: String,
    val products: List<RecipeProductUiModel>
) : Visitable<RecipeIngredientTypeFactory>, Parcelable {
    override fun type(typeFactory: RecipeIngredientTypeFactory): Int {
        return typeFactory.type(this)
    }
}