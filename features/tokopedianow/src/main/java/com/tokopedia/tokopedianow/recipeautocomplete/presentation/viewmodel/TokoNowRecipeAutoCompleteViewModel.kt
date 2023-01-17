package com.tokopedia.tokopedianow.recipeautocomplete.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TITLE
import javax.inject.Inject

class TokoNowRecipeAutoCompleteViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _recipeListParam: MutableLiveData<String> =  MutableLiveData<String>()
    val recipeListParam: LiveData<String>
        get() = _recipeListParam

    fun submitSearch(title: String) {
        val getRecipeListParam = RecipeListParam()
        getRecipeListParam.queryParamsMap[PARAM_TITLE] = title
        val queryParamsGenerated = getRecipeListParam.generateQueryParams()
        val queryParams = queryParamsGenerated.ifBlank { "" }
        _recipeListParam.value = "?${queryParams}"
    }
}