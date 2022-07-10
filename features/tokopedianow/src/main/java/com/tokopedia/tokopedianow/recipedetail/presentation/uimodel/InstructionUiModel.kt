package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeInstructionTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstructionUiModel(
    val htmlText: String
) : Visitable<RecipeInstructionTypeFactory>, Parcelable {

    override fun type(typeFactory: RecipeInstructionTypeFactory): Int {
        return typeFactory.type(this)
    }
}