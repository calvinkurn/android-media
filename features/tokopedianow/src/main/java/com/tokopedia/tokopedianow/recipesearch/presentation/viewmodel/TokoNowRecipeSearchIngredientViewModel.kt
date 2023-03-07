package com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.recipesearch.presentation.mapper.RecipeIngredientMapper.addIngredients
import com.tokopedia.tokopedianow.recipesearch.presentation.mapper.RecipeIngredientMapper.updateIngredients
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import java.util.*
import javax.inject.Inject

class TokoNowRecipeSearchIngredientViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val ingredientItems: LiveData<List<RecipeSearchIngredientUiModel>>
        get() = _ingredientItems
    private val _ingredientItems = MutableLiveData<List<RecipeSearchIngredientUiModel>>()

    var selectedFilters: ArrayList<SelectedFilter> = arrayListOf()

    private val ingredientList = mutableListOf<RecipeSearchIngredientUiModel>()

    fun getIngredients() {
        launchCatchError(block = {
            val response = getSortFilterUseCase.execute()
            ingredientList.addIngredients(response, selectedFilters)
            _ingredientItems.postValue(ingredientList)
        }) {

        }
    }

    fun onSelectIngredient(id: String, isChecked: Boolean, title: String) {
        if (isChecked) {
            selectedFilters.add(SelectedFilter(id, PARAM_INGREDIENT_ID, title))
        } else {
            selectedFilters.removeFirst { it.id == id }
        }
        ingredientList.updateIngredients(id, isChecked)
        _ingredientItems.postValue(ingredientList)
    }
}