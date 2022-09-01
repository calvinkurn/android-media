package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToIngredientIds
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToSortBy
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addFilterItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeItems
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

abstract class BaseTokoNowRecipeListViewModel(
    private val getRecipeListUseCase: GetRecipeListUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val visitableList: LiveData<List<Visitable<*>>>
        get() = _visitableList
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    val showHeaderBackground: LiveData<Boolean>
        get() = _showHeaderBackground

    private val _visitableList = MutableLiveData<List<Visitable<*>>>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showHeaderBackground = MutableLiveData<Boolean>()

    protected var getRecipeListParam = RecipeListParam()
    protected var visitableItems = mutableListOf<Visitable<*>>()

    var selectedFilters = emptyList<SelectedFilter>()
    var enableHeaderBackground: Boolean = true

    abstract val sourcePage: String

    fun getRecipeList() {
        launchCatchError(block = {
            getRecipeListParam.sourcePage = sourcePage
            getRecipeListParam.warehouseID = addressData.getWarehouseId().toString()
            val response = getRecipeListUseCase.execute(getRecipeListParam)

            if (enableHeaderBackground) {
                showHeaderBackground()
                visitableItems.addHeaderItem()
            }

            visitableItems.addFilterItems()
            visitableItems.addRecipeItems(response)

            _visitableList.postValue(visitableItems)
            hideProgressBar()
        }) {
            hideHeaderBackground()
            hideProgressBar()
            showError()
        }
    }

    fun applyFilter(filters: List<SelectedFilter>) {
        val sortBy = mapToSortBy(filters)
        val ingredientID = mapToIngredientIds(filters)
        selectedFilters = filters

        getRecipeListParam.apply {
            this.sortBy = sortBy
            this.ingredientID = ingredientID
        }

        refreshPage()
    }

    fun refreshPage() {
        resetVisitableItems()
        showProgressBar()
        getRecipeList()
    }

    private fun showError() {
        visitableItems.clear()
        visitableItems.add(TokoNowServerErrorUiModel)
        _visitableList.postValue(visitableItems)
    }

    private fun resetVisitableItems() {
        visitableItems.clear()
        _visitableList.postValue(visitableItems)
    }

    private fun showProgressBar() {
        _showProgressBar.postValue(false)
    }

    private fun hideProgressBar() {
        _showProgressBar.postValue(false)
    }

    private fun showHeaderBackground() {
        _showHeaderBackground.postValue(true)
    }

    private fun hideHeaderBackground() {
        _showHeaderBackground.postValue(false)
    }
}