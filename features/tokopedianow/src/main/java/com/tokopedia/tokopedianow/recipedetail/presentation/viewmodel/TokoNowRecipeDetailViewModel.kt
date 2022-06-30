package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
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
            val recipeInfo = RecipeInfoUiModel(
                title = "Nasi Goreng",
                portion = 1,
                duration = 15,
                labels = listOf("Pedas", "Gurih", "Nasi", "Goreng")
            )

            val layoutList = listOf(recipeInfo)

            _layoutList.postValue(Success(layoutList))
        }) {

        }
    }
}