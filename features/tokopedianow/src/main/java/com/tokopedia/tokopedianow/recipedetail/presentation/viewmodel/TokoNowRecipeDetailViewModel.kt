package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val layoutList: LiveData<Result<List<Visitable<*>>>>
        get() = _layoutList

    private val _layoutList = MutableLiveData<Result<List<Visitable<*>>>>()

    fun getRecipe(recipeId: String, warehouseId: String) {
        launchCatchError(block = {
            // Temporary Hardcode Data
            val mediaSlider = MediaSliderUiModel("", "")
            val recipeInfo = RecipeInfoUiModel(
                title = "Bubur Kacang Hijau",
                portion = 1,
                duration = 15,
                labels = listOf("Snack", "Santan", "Manis", "Kacang", "Hijau")
            )

            val ingredients = listOf(
                IngredientUiModel("Kacang Hijau Muda, dikupas", 500, "gram"),
                IngredientUiModel("Garam", 1, "sendok teh"),
                IngredientUiModel("Gula pasir", 4, "sendok makan"),
                IngredientUiModel("Pandan Segar", 2, "ikat"),
                IngredientUiModel("Perisa pandan ", 1, "sendok teh"),
                IngredientUiModel("Santan segar, dicairkan", 2, "cup", true)
            )

            val instruction = InstructionUiModel(
                "<ol>\n" +
                    "<li>&nbsp;Beli bahannya</li>\n" +
                    "<li>&nbsp;Persiapkan alatnya</li>\n" +
                    "<li>&nbsp;Dimasak</li>\n" +
                    "<li>&nbsp;Disajikan</li>\n" +
                    "</ol>"
            )

            val recipeTab = RecipeTabUiModel(
                IngredientTabUiModel(emptyList()),
                InstructionTabUiModel(ingredients, instruction)
            )

            val layoutList = listOf(mediaSlider, recipeInfo, recipeTab)

            _layoutList.postValue(Success(layoutList))
        }) {

        }
    }
}