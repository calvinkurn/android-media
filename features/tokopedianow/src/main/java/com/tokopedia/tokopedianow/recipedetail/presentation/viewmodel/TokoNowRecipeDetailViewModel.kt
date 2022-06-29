package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    fun getRecipe(recipeId: String, warehouseId: String) {
        launchCatchError(block = {
            getRecipeUseCase.execute(recipeId, warehouseId)
        }) {

        }
    }
}