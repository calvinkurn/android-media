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
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToIngredientIds
import com.tokopedia.tokopedianow.recipelist.domain.mapper.FilterParamMapper.mapToSortBy
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_SORT_BY
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TITLE
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addQuickFilterItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeCount
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.addRecipeItems
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.removeHeaderItem
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeListMapper.removeLoadMoreItem
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

open class BaseTokoNowRecipeListViewModel(
    private val getRecipeListUseCase: GetRecipeListUseCase,
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
    val removeScrollListener: LiveData<Boolean>
        get() = _removeScrollListener

    private val _visitableList = MutableLiveData<List<Visitable<*>>>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showHeaderBackground = MutableLiveData<Boolean>()
    private val _searchKeyword = MutableLiveData<String>()
    private val _removeScrollListener = MutableLiveData<Boolean>()

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

            visitableItems.addRecipeCount(response)
            visitableItems.addRecipeItems(response)

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

    fun applyFilter(filters: List<SelectedFilter>) {
        val sortBy = mapToSortBy(filters)
        val ingredientID = mapToIngredientIds(filters)
        selectedFilters = filters

        getRecipeListParam.apply {
            queryParamsMap[PARAM_SORT_BY] = sortBy
            queryParamsMap[PARAM_INGREDIENT_ID] = ingredientID
        }

        refreshPage()
    }

    fun refreshPage() {
        resetPageParam()
        onViewCreated()
        getRecipeList()
    }

    fun setKeywordToSearchbar() {
        _searchKeyword.postValue(getRecipeListParam.queryParamsMap.getOrElse(PARAM_TITLE) { "" })
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
        visitableItems.addQuickFilterItems()
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
        _showProgressBar.postValue(false)
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
        val notLoading = visitableItems.firstOrNull { it is LoadingMoreModel } == null
        val scrolledToBottom = lastVisibleItemIndex == visitableItems.count() - DEFAULT_INDEX
        return scrolledToBottom && notLoading && hasNext
    }
}