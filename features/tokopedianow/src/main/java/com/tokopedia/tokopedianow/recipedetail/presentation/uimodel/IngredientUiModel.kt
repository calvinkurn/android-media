package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientUiModel(
    val name: String,
    val quantity: Int,
    val unit: String,
    val isLastItem: Boolean = false
): Visitable<RecipeInstructionTypeFactory>, Parcelable {

    override fun type(typeFactory: RecipeInstructionTypeFactory): Int {
        return typeFactory.type(this)
    }
}