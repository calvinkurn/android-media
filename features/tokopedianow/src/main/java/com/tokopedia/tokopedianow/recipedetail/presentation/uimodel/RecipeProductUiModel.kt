package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeProductTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeProductUiModel(
    val id: String,
    val shopId: String,
    val name: String,
    val quantity: Int = 0,
    val stock: Int,
    val minOrder: Int,
    val maxOrder: Int,
    val priceFmt: String,
    val weight: String = "",
    val imageUrl: String,
    val slashedPrice: String = "",
    val discountPercentage: String = "",
    val similarProducts: List<RecipeProductUiModel> = emptyList(),
    val categoryId: String = "",
    val categoryName: String = "",
    val position: Int = 0
) : Visitable<RecipeProductTypeFactory>, Parcelable {

    val impressHolder
        get() = ImpressHolder()

    override fun type(typeFactory: RecipeProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}
