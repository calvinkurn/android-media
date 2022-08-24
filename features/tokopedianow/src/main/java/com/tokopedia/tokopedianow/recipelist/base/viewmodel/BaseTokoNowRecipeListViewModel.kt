package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addFilterItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeItems
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase

abstract class BaseTokoNowRecipeListViewModel(
    private val getRecipeListUseCase: GetRecipeListUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val visitableList: LiveData<List<Visitable<*>>>
        get() = _visitableList
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _visitableList = MutableLiveData<List<Visitable<*>>>()
    private val _showProgressBar = MutableLiveData<Boolean>()

    protected var getRecipeListParam = RecipeListParam()
    protected var visitableItems = mutableListOf<Visitable<*>>()

    var showHeaderBackground: Boolean = true

    fun getRecipeList() {
        launchCatchError(block = {
            getRecipeListParam.warehouseID = addressData.getWarehouseId().toString()
            val response = getRecipeListUseCase.execute(getRecipeListParam)

            if(showHeaderBackground) {
                visitableItems.addHeaderItem()
            }

            visitableItems.addFilterItems()
            visitableItems.addRecipeItems(response)

            _visitableList.postValue(visitableItems)
            hideProgressBar()
        }) {
            hideProgressBar()
            showError()
        }
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
}