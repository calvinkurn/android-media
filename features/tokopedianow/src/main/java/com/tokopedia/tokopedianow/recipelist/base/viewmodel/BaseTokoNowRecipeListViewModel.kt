package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToIngredientIds
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToSortBy
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addEmptyStateItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addFilterItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeItems
import com.tokopedia.tokopedianow.recipelist.util.StatusLoadPage
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

abstract class BaseTokoNowRecipeListViewModel(
    private val getRecipeListUseCase: GetRecipeListUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val visitableList: LiveData<List<Visitable<*>>>
        get() = _visitableList
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    val showHeaderBackground: LiveData<Boolean>
        get() = _showHeaderBackground
    val searchKeyword: LiveData<String>
        get() = _searchKeyword
    val showToaster: LiveData<ToasterUiModel>
        get() = _showToaster

    val warehouseId: String
        get() = addressData.getWarehouseId().toString()

    private val _visitableList = MutableLiveData<List<Visitable<*>>>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showHeaderBackground = MutableLiveData<Boolean>()
    private val _searchKeyword = MutableLiveData<String>()
    private val _showToaster = MutableLiveData<ToasterUiModel>()

    private var statusPage = StatusLoadPage.EMPTY

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
            statusPage = if (response.data.recipes.isNotEmpty()) {
                visitableItems.addRecipeItems(
                    response = response
                )
                StatusLoadPage.SUCCESS
            } else {
                visitableItems.addEmptyStateItem(
                    isFilterSelected = selectedFilters.isNotEmpty(),
                    title = getRecipeListParam.title.orEmpty()
                )
                StatusLoadPage.EMPTY
            }

            _visitableList.postValue(visitableItems)
            hideProgressBar()
        }) {
            hideHeaderBackground()
            hideProgressBar()
            showError()
        }
    }

    fun addRecipeBookmark(recipeId: String, position: Int, title: String) {
        val isRemoving = false
        var isSuccess = false
        launchCatchError(block = {
            val response = addRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            isSuccess = response.header.success
            if (response.header.success) {
                _showToaster.postValue(
                    ToasterUiModel(
                        isRemoving = isRemoving,
                        position = position,
                        model = ToasterModel(
                            title = title,
                            recipeId = recipeId,
                            isSuccess = isSuccess
                        )
                    )
                )
            } else {
                _showToaster.postValue(
                    ToasterUiModel(
                        isRemoving = isRemoving,
                        position = position,
                        model = ToasterModel(
                            message = response.header.message,
                            recipeId = recipeId,
                            isSuccess = isSuccess
                        )
                    )
                )
            }
        }) {
            _showToaster.postValue(
                ToasterUiModel(
                    isRemoving = isRemoving,
                    position = position,
                    model = ToasterModel(
                        recipeId = recipeId,
                        isSuccess = isSuccess
                    )
                )
            )
        }
    }

    fun removeRecipeBookmark(recipeId: String, position: Int, title: String) {
        val isRemoving = true
        var isSuccess = false
        launchCatchError(block = {
            val response = removeRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            isSuccess = response.header.success
            if (response.header.success) {
                _showToaster.postValue(ToasterUiModel(
                    isRemoving = isRemoving,
                    position = position,
                    model = ToasterModel(
                        title = title,
                        recipeId = recipeId,
                        isSuccess = isSuccess
                    )
                ))
            } else {
                _showToaster.postValue(ToasterUiModel(
                    isRemoving = isRemoving,
                    position = position,
                    model = ToasterModel(
                        message = response.header.message,
                        recipeId = recipeId,
                        isSuccess = isSuccess
                    )
                ))
            }
        }) {
            _showToaster.value = ToasterUiModel(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    recipeId = recipeId,
                    isSuccess = isSuccess
                )
            )
        }
    }

    fun whenLoadPage(onSuccessLoaded: () -> Unit, onFailedLoaded: () -> Unit, onEmptyLoaded: () -> Unit) {
        when (statusPage) {
            StatusLoadPage.SUCCESS -> onSuccessLoaded()
            StatusLoadPage.ERROR -> onFailedLoaded()
            StatusLoadPage.EMPTY -> onEmptyLoaded()
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

    fun setKeywordToSearchbar() {
        _searchKeyword.postValue(if (getRecipeListParam.title.isNullOrBlank()) "" else getRecipeListParam.title)
    }

    private fun showError() {
        visitableItems.clear()
        visitableItems.add(TokoNowServerErrorUiModel)
        _visitableList.postValue(visitableItems)
        statusPage = StatusLoadPage.ERROR
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