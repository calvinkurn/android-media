package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeProductTypeFactory

data class RecipeProductUiModel(
    val id: String,
    val shopId: String,
    val name: String,
    val quantity: Int = 0,
    val stock: Int,
    val minOrder: Int,
    val maxOrder: Int,
    val priceFmt: String,
    val weight: String,
    val imageUrl: String,
    val slashedPrice: String = "",
    val discountPercentage: String = ""
) : Visitable<RecipeProductTypeFactory> {
    override fun type(typeFactory: RecipeProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}