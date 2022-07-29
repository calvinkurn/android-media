package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientTabUiModel(
    val buyAllProductItem: BuyAllProductUiModel,
    val productList: List<RecipeProductUiModel>
): Parcelable