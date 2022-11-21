package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.addRecipeProgressBar
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.addRecipeShimmering
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.removeRecipeProgressBar
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.removeRecipeShimmering
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PER_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.RESTORE_ADD_POSITION
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TokoNowRecipeBookmarkViewModel @Inject constructor(
    private val chooseAddressData: LocalCacheModel,
    private val getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val layout: MutableList<Visitable<*>> = mutableListOf()
    private var pageCounter: Int = DEFAULT_PAGE
    private var noNeedLoadMore: Boolean = true
    private var tempRecipeRemoved: MutableMap<Int, Visitable<*>> = mutableMapOf()

    private val _loadRecipeBookmarks: MutableStateFlow<UiState<List<Visitable<*>>>?> = MutableStateFlow(null)
    private val _moreRecipeBookmarks: MutableStateFlow<UiState<List<Visitable<*>>>?> = MutableStateFlow(null)
    private val _toaster: MutableStateFlow<UiState<ToasterUiModel>?> = MutableStateFlow(null)
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val warehouseId: String
        get() = chooseAddressData.warehouse_id

    val loadRecipeBookmarks: StateFlow<UiState<List<Visitable<*>>>?>
        get() = _loadRecipeBookmarks
    val moreRecipeBookmarks: StateFlow<UiState<List<Visitable<*>>>?>
        get() = _moreRecipeBookmarks
    val toaster: StateFlow<UiState<ToasterUiModel>?>
        get() = _toaster
    val isOnScrollNotNeeded: StateFlow<Boolean>
        get() = _isOnScrollNotNeeded

    private suspend fun getRecipeBookmarks(page: Int): Triple<List<RecipeUiModel>, GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Header, Boolean> {
        val response = getRecipeBookmarksUseCase.execute(
            warehouseId = warehouseId,
            page = page,
            limit = DEFAULT_PER_PAGE
        ).tokonowGetRecipeBookmarks

        return Triple(
            response.data.recipes.mapResponseToUiModelList(),
            response.header,
            response.metadata.hasNext
        )
    }

    private fun onResponseAddRecipeBE(position: Int, isRemoving: Boolean, recipeId: String, errorMessage: String, isSuccess: Boolean) {
        if (isSuccess) {
            restoreRecipeRemoved(
                tempPosition = position,
                restorePosition = RESTORE_ADD_POSITION,
                recipeId = recipeId
            )
        } else {
            onFailAddRecipe(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                isSuccess = isSuccess,
                errorMessage = errorMessage,
            )
        }

        _loadRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    /**
     * @see layout - will be updated by removing shimmering model
     * @see toaster - fail toaster will be shown with capability to try it again
     */
    private fun onFailAddRecipe(isRemoving: Boolean, position: Int, recipeId: String, isSuccess: Boolean, errorMessage: String) {
        layout.removeRecipeShimmering(recipeId)

        _toaster.value = UiState.Fail(
            data = ToasterUiModel(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    message = errorMessage,
                    recipeId = recipeId,
                    isSuccess = isSuccess
                )
            )
        )
    }

    private fun onResponseRemoveRecipeBE(isRemoving: Boolean, title: String, position: Int, recipeId: String, errorMessage: String, isSuccess: Boolean) {
        if (isSuccess) {
            onSuccessRemoveRecipe(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                title = title,
                isSuccess = isSuccess
            )
        } else {
            onFailRemoveRecipe(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                isSuccess = isSuccess,
                errorMessage = errorMessage
            )
        }

        _loadRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    /**
     * @see layout - will be updated by removing shimmering model
     * @see toaster - success toaster will be shown with capability to add recipe back
     */
    private fun onSuccessRemoveRecipe(isRemoving: Boolean, position: Int, recipeId: String, title: String, isSuccess: Boolean) {
        layout.removeRecipeShimmering(recipeId)

        _toaster.value = UiState.Success(ToasterUiModel(
            isRemoving = isRemoving,
            position = position,
            model = ToasterModel(
                title = title,
                recipeId = recipeId,
                isSuccess = isSuccess
            ),
        ))
    }

    /**
     * @see toaster - fail toaster will be shown with capability to try it again
     */
    private fun onFailRemoveRecipe(isRemoving: Boolean, position: Int, recipeId: String, isSuccess: Boolean, errorMessage: String) {
        restoreRecipeRemoved(
            tempPosition = position,
            restorePosition = position,
            recipeId = recipeId
        )

        _toaster.value = UiState.Fail(
            data = ToasterUiModel(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    message = errorMessage,
                    recipeId = recipeId,
                    isSuccess = isSuccess
                )
            )
        )
    }

    /**
     * @see tempRecipeRemoved - restore recipe back to layout
     * @see layout - will be updated by restoring recipe back
     */
    private fun restoreRecipeRemoved(tempPosition: Int, restorePosition: Int, recipeId: String) {
        layout.removeRecipeShimmering(recipeId)
        tempRecipeRemoved[tempPosition]?.apply {
            layout.add(restorePosition, this)
        }
    }

    /**
     * @see loadRecipeBookmarks - layout will be updated with latest layout
     * @see toaster - failed toaster will be shown with capability to retry the event
     */
    private fun onFailAddRemoveRecipeFE(isRemoving: Boolean, position: Int, recipeId: String, throwable: Throwable) {
        _loadRecipeBookmarks.value = UiState.Success(
            data = layout
        )

        _toaster.value = UiState.Fail(
            data = ToasterUiModel(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    recipeId = recipeId
                ),
            ),
            throwable = throwable
        )
    }

    /**
     * @see noNeedLoadMore - if true no need to load widgets more
     * @see loadRecipeBookmarks - layout will be updated with latest layout or show global error
     */
    private fun onResponseLoadFirstPageBE(recipeBookmarks: List<RecipeUiModel>, header: GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Header, hasNext: Boolean) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE

        if (header.success) {
            layout.addAll(recipeBookmarks)
            _loadRecipeBookmarks.value = UiState.Success(
                data = layout
            )
        } else {
            _loadRecipeBookmarks.value = UiState.Fail(
                errorCode = header.statusCode
            )
        }
    }

    /**
     * @see loadRecipeBookmarks - global error layout will be shown
     */
    private fun onFailLoadFirstPageFE(throwable: Throwable) {
        _loadRecipeBookmarks.value = UiState.Fail(
            throwable = throwable
        )
    }

    /**
     * @see noNeedLoadMore - if true no need to load widgets more
     * @see moreRecipeBookmarks - layout will be updated with latest layout or just hide load more loading
     */
    private fun onResponseLoadMoreBE(recipeBookmarks: List<RecipeUiModel>, header: GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Header, hasNext: Boolean) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE
        layout.removeRecipeProgressBar()

        if (header.success) {
            layout.addAll(recipeBookmarks)
            _moreRecipeBookmarks.value = UiState.Success(
                data = layout
            )
        } else {
            _moreRecipeBookmarks.value = UiState.Success(
                data = layout
            )
        }
    }

    /**
     * @see moreRecipeBookmarks - will hide load more loading
     */
    private fun onFailLoadMoreFE() {
        layout.removeRecipeProgressBar()

        _moreRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    /**
     * @see moreRecipeBookmarks - will show load more loading
     */
    private fun showLoadMoreLoading() {
        layout.addRecipeProgressBar()

        _moreRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    /**
     * @see loadRecipeBookmarks - item will be changed to shimmering or just add shimmering model
     */
    private fun showItemLoading(isRemoving: Boolean, position: Int, recipeId: String) {
        if (isRemoving) {
            layout.removeAt(position)
            layout.addRecipeShimmering(position, recipeId)
        } else {
            layout.addRecipeShimmering(RESTORE_ADD_POSITION, recipeId)
        }
        _loadRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    private fun updateLayoutWithTempRecipeRemoved(isRemoving: Boolean, position: Int, recipeId: String) {
        if (isRemoving) {
            restoreRecipeRemoved(
                tempPosition = position,
                restorePosition = position,
                recipeId = recipeId
            )
        } else {
            layout.removeAt(RESTORE_ADD_POSITION)
        }
    }

    fun loadFirstPage() {
        launchCatchError(block =  {
            _loadRecipeBookmarks.value = UiState.Loading()
            val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter++)
            onResponseLoadFirstPageBE(recipeBookmarks, header, hasNext)
        }, onError = { throwable ->
            onFailLoadFirstPageFE(throwable)
        })
    }

    fun removeRecipeBookmark(title: String, position: Int, recipeId: String, isRemoving: Boolean) {
        launchCatchError(block = {
            _toaster.value = UiState.Loading()

            tempRecipeRemoved[position] = layout[position]

            showItemLoading(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId
            )

            val response = removeRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            onResponseRemoveRecipeBE(
                isRemoving = isRemoving,
                title = title,
                position = position,
                recipeId = recipeId,
                errorMessage = response.header.message,
                isSuccess = response.header.success
            )
        }, onError = { throwable ->
            updateLayoutWithTempRecipeRemoved(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId
            )

            onFailAddRemoveRecipeFE(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                throwable = throwable
            )
        })
    }

    fun addRecipeBookmark(recipeId: String, position: Int, isRemoving: Boolean) {
        launchCatchError(block = {
            _toaster.value = UiState.Loading()

            showItemLoading(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId
            )

            val response = addRecipeBookmarkUseCase.execute(
                recipeId = recipeId
            )

            onResponseAddRecipeBE(
                position = position,
                isRemoving = isRemoving,
                recipeId = recipeId,
                errorMessage = response.header.message,
                isSuccess = response.header.success
            )
        }, onError = { throwable ->
            updateLayoutWithTempRecipeRemoved(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId
            )

            onFailAddRemoveRecipeFE(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                throwable = throwable
            )
        })
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        launchCatchError(block = {
            if (noNeedLoadMore) {
                _isOnScrollNotNeeded.value = true
            } else if (isAtTheBottomOfThePage && layout.last() !is RecipeProgressBarUiModel) {
                showLoadMoreLoading()

                val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter++)

                onResponseLoadMoreBE(recipeBookmarks, header, hasNext)
            }
        }) {
            onFailLoadMoreFE()
        }
    }

    fun removeToaster() {
        _toaster.value = null
    }

}
