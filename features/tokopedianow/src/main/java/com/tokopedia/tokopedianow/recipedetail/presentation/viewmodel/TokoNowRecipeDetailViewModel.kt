package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

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

            val products = listOf(
                RecipeProductUiModel(
                    id = "1",
                    name = "Kacang Hijau Curah",
                    quantity = 3,
                    stock = 5,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://utils.api.stdlib.com/automicon/codeworks.png",
                    slashedPrice = "Rp30.000",
                    discountPercentage = "50%"
                ),
                RecipeProductUiModel(
                    id = "2",
                    name = "Garam",
                    quantity = 3,
                    stock = 0,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://www.iconsdb.com/icons/preview/guacamole-green/square-xxl.png"
                ),
                RecipeProductUiModel(
                    id = "3",
                    name = "Gula",
                    quantity = 3,
                    stock = 2,
                    price = 15000,
                    priceFmt = "Rp15.000",
                    weight = "500 g",
                    imageUrl = "https://www.iconsdb.com/icons/preview/pink/square-xxl.png"
                )
            )

            val totalPrice = "Rp45.000"
            val buyAllProductItem = BuyAllProductUiModel(totalPrice, products)

            val recipeTab = RecipeTabUiModel(
                IngredientTabUiModel(buyAllProductItem, products),
                InstructionTabUiModel(ingredients, instruction)
            )

            val layoutList = listOf(mediaSlider, recipeInfo, recipeTab)

            _layoutList.postValue(Success(layoutList))
        }) {

        }
    }
}