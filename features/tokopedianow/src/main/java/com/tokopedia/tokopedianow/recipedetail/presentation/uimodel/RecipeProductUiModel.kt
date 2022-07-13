package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeIngredientTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeProductUiModel(
    val id: String,
    val shopId: String,
    val name: String,
    val quantity: Int,
    val stock: Int,
    val price: Int,
    val priceFmt: String,
    val weight: String,
    val imageUrl: String,
    val slashedPrice: String = "",
    val discountPercentage: String = ""
) : Visitable<RecipeIngredientTypeFactory>, Parcelable {
    override fun type(typeFactory: RecipeIngredientTypeFactory): Int {
        return typeFactory.type(this)
    }
}