package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToDuration
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToIngredientIds
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToPortion
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToSortBy
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToTagIds
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_DURATION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_PORTION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_SORT_BY
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TAG_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TITLE
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addEmptyStateItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addQuickFilterItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeCount
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.removeHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.removeLoadMoreItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.updateRecipeBookmark
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.util.LoadPageStatus
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

open class BaseTokoNowRecipeListViewModel(
    private val getRecipeListUseCase: GetRecipeListUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val DEFAULT_INDEX = 1
        private const val DEFAULT_PAGE = 1
    }

    val visitableList: LiveData<List<Visitable<*>>>
        get() = _visitableList
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar
    val showHeaderBackground: LiveData<Boolean>
        get() = _showHeaderBackground
    val searchKeyword: LiveData<String>
        get() = _searchKeyword
    val showBookmarkToaster: LiveData<ToasterUiModel>
        get() = _showBookmarkToaster
    val removeScrollListener: LiveData<Boolean>
        get() = _removeScrollListener

    val warehouseId: String
        get() = addressData.getWarehouseId().toString()

    private val _visitableList = MutableLiveData<List<Visitable<*>>>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showHeaderBackground = MutableLiveData<Boolean>()
    private val _searchKeyword = MutableLiveData<String>()
    private val _removeScrollListener = MutableLiveData<Boolean>()
    private val _showBookmarkToaster = MutableLiveData<ToasterUiModel>()

    private var visitableItems = mutableListOf<Visitable<*>>()
    private var hasNext = false

    protected val getRecipeListParam = RecipeListParam()

    var selectedFilters = emptyList<SelectedFilter>()
        private set
    var enableHeaderBackground: Boolean = true

    open val sourcePage: String = ""

    fun onViewCreated() {
        resetVisitableItems()
        showHideBackground()
        addQuickFilterItems()
        updateVisitableItems()
    }

    fun getRecipeList() {
        launchCatchError(block = {
            showProgressBar()
            getRecipeListParam.sourcePage = sourcePage
            getRecipeListParam.warehouseID = addressData.getWarehouseId().toString()

            val response = getRecipeListUseCase.execute(getRecipeListParam)
            hasNext = response.metadata.hasNext

            if (response.data.recipes.isNotEmpty()) {
                visitableItems.addRecipeCount(response)
                visitableItems.addRecipeItems(
                    response = response
                )
            } else {
                visitableItems.addEmptyStateItem(
                    isFilterSelected = selectedFilters.isNotEmpty(),
                    title = getRecipeListParam.decodeTitle()
                )
            }

            updateVisitableItems()
            hideProgressBar()
        }) {
            hideHeaderBackground()
            hideProgressBar()
            showError()
        }
    }

    fun onScroll(lastVisibleItemIndex: Int) {
        if (shouldLoadMore(lastVisibleItemIndex)) {
            loadMoreRecipe()
        }
    }

    fun addRecipeBookmark(recipeId: String, position: Int, title: String) {
        launchCatchError(block = {
            addRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            _showBookmarkToaster.postValue(
                ToasterUiModel(
                    isRemoving = false,
                    position = position,
                    model = ToasterModel(
                        title = title,
                        recipeId = recipeId,
                        isSuccess = true
                    )
                )
            )

            visitableItems.updateRecipeBookmark(
                recipeId = recipeId,
                isBookmarked = true
            )
            updateVisitableItems()
        }) {
            _showBookmarkToaster.postValue(
                ToasterUiModel(
                    isRemoving = false,
                    position = position,
                    model = ToasterModel(
                        recipeId = recipeId,
                        isSuccess = false
                    )
                )
            )
        }
    }

    fun removeRecipeBookmark(recipeId: String, position: Int, title: String) {
        launchCatchError(block = {
            removeRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            _showBookmarkToaster.postValue(
                ToasterUiModel(
                    isRemoving = true,
                    position = position,
                    model = ToasterModel(
                        title = title,
                        recipeId = recipeId,
                        isSuccess = true
                )
            ))

            visitableItems.updateRecipeBookmark(
                recipeId = recipeId,
                isBookmarked = false
            )
            updateVisitableItems()
        }) {
            _showBookmarkToaster.postValue(
                ToasterUiModel(
                    isRemoving = true,
                    position = position,
                    model = ToasterModel(
                        recipeId = recipeId,
                        isSuccess = false
                )
            ))
        }
    }

    fun getLoadPageStatus(): LoadPageStatus {
        return when (visitableItems.firstOrNull { it is RecipeUiModel || it is TokoNowServerErrorUiModel }) {
            is RecipeUiModel -> LoadPageStatus.SUCCESS
            is TokoNowServerErrorUiModel -> LoadPageStatus.ERROR
            else -> LoadPageStatus.EMPTY
        }
    }

    fun applyFilter(filters: List<SelectedFilter>) {
        val sortBy = mapToSortBy(filters)
        val tagID = mapToTagIds(filters)
        val ingredientID = mapToIngredientIds(filters)
        val duration = mapToDuration(filters)
        val portion = mapToPortion(filters)
        selectedFilters = filters

        getRecipeListParam.apply {
            queryParamsMap[PARAM_SORT_BY] = sortBy
            queryParamsMap[PARAM_TAG_ID] = tagID
            queryParamsMap[PARAM_INGREDIENT_ID] = ingredientID
            queryParamsMap[PARAM_DURATION] = duration
            queryParamsMap[PARAM_PORTION] = portion
        }

        refreshPage()
    }

    fun refreshPage() {
        resetPageParam()
        onViewCreated()
        getRecipeList()
    }

    fun resetFilter() {
        resetQueryParams()
        resetSelectedFilter()
        refreshPage()
    }

    fun setKeywordToSearchbar() {
        _searchKeyword.postValue(getRecipeListParam.decodeTitle())
    }

    private fun loadMoreRecipe() {
        showLoadMoreProgressBar()

        launchCatchError(block = {
            getRecipeListParam.sourcePage = sourcePage
            getRecipeListParam.warehouseID = addressData.getWarehouseId().toString()
            getRecipeListParam.page = getRecipeListParam.page + 1

            val response = getRecipeListUseCase.execute(getRecipeListParam)
            hasNext = response.metadata.hasNext

            visitableItems.addRecipeItems(response)

            _removeScrollListener.postValue(!hasNext)
            updateVisitableItems()
            hideLoadMoreProgressBar()
        }) {
            hideLoadMoreProgressBar()
        }
    }

    private fun showHideBackground() {
        if (enableHeaderBackground) {
            showHeaderBackground()
        } else {
            hideHeaderBackground()
        }
    }

    private fun addQuickFilterItems() {
        val selectedFiltersCount = selectedFilters.count()
        visitableItems.addQuickFilterItems(selectedFiltersCount)
    }

    private fun resetPageParam() {
        getRecipeListParam.page = DEFAULT_PAGE
    }

    private fun showError() {
        resetVisitableItems()
        addServerErrorItem()
        updateVisitableItems()
    }

    private fun resetVisitableItems() {
        visitableItems.clear()
    }

    private fun showProgressBar() {
        _showProgressBar.postValue(true)
    }

    private fun hideProgressBar() {
        _showProgressBar.postValue(false)
    }

    private fun showLoadMoreProgressBar() {
        addLoadMoreProgressBar()
        updateVisitableItems()
    }

    private fun hideLoadMoreProgressBar() {
        removeLoadMoreProgressBar()
        updateVisitableItems()
    }

    private fun addServerErrorItem() {
        visitableItems.add(TokoNowServerErrorUiModel)
    }

    private fun addLoadMoreProgressBar() {
        visitableItems.add(LoadingMoreModel())
    }

    private fun removeLoadMoreProgressBar() {
        visitableItems.removeLoadMoreItem()
    }

    private fun updateVisitableItems() {
        _visitableList.postValue(visitableItems)
    }

    private fun showHeaderBackground() {
        visitableItems.addHeaderItem()
        _showHeaderBackground.postValue(true)
    }

    private fun hideHeaderBackground() {
        visitableItems.removeHeaderItem()
        _showHeaderBackground.postValue(false)
    }

    private fun shouldLoadMore(lastVisibleItemIndex: Int): Boolean {
        val notLoading = visitableItems.firstOrNull { it is LoadingMoreModel } == null &&
            _showProgressBar.value == false
        val scrolledToBottom = lastVisibleItemIndex == visitableItems.count() - DEFAULT_INDEX
        return scrolledToBottom && notLoading && hasNext
    }

    private fun resetQueryParams() {
        getRecipeListParam.queryParamsMap.clear()
    }

    private fun resetSelectedFilter() {
        selectedFilters = emptyList()
    }
}
