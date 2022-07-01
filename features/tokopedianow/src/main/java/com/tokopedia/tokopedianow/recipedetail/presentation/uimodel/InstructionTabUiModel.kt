package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstructionTabUiModel(
    val ingredients: List<IngredientUiModel>,
    val instruction: InstructionUiModel
): Parcelable