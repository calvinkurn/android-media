package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.ui.layout.ERROR_MAINTENANCE
import com.tokopedia.tokopedianow.common.ui.layout.ERROR_PAGE_NOT_FOUND
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.addRecipeProgressBar
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.addRecipeShimmering
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.removeRecipeProgressBar
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.removeRecipeShimmering
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse.TokonowGetRecipeBookmarks.Header
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeShimmeringUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkAction
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent.AddRecipeBookmark
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent.ClickEmptyStateActionButton
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent.LoadMoreRecipeBookmarkList
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent.LoadRecipeBookmarkList
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent.RemoveRecipeBookmark
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowRecipeBookmarkViewModel @Inject constructor(
    private val chooseAddressData: LocalCacheModel,
    private val getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FIRST_ITEM_INDEX = 0
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PER_PAGE = 10
        private const val RESTORE_ADD_POSITION = 0
    }

    private val _uiState: MutableStateFlow<RecipeBookmarkState> = MutableStateFlow(
        RecipeBookmarkState.Loading
    )
    private val _uiAction = MutableSharedFlow<RecipeBookmarkAction>()

    val uiState: StateFlow<RecipeBookmarkState> = _uiState.asStateFlow()
    val uiAction: SharedFlow<RecipeBookmarkAction> = _uiAction.asSharedFlow()

    private val visitableList: SnapshotStateList<Visitable<*>> = mutableStateListOf()

    private var pageCounter: Int = DEFAULT_PAGE
    private var noNeedLoadMore: Boolean = true
    private var tempRecipeRemoved: MutableMap<Int, Visitable<*>> = mutableMapOf()

    fun onEvent(event: RecipeBookmarkEvent) {
        when (event) {
            is LoadRecipeBookmarkList -> loadFirstPage()
            is LoadMoreRecipeBookmarkList -> loadMore(event)
            is AddRecipeBookmark -> addRecipeBookmark(event)
            is RemoveRecipeBookmark -> removeRecipeBookmark(event)
            is ClickEmptyStateActionButton -> onClickEmptyStateButton(event)
            else -> setAction(event.toAction())
        }
    }

    private suspend fun getRecipeBookmarks(page: Int): Triple<List<RecipeUiModel>, Header, Boolean> {
        val warehouseId = chooseAddressData.warehouse_id

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

    private fun onResponseAddRecipeBE(
        position: Int,
        isRemoving: Boolean,
        recipeId: String,
        errorMessage: String,
        isSuccess: Boolean
    ) {
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
                errorMessage = errorMessage
            )
        }
        updateVisitableList()
    }

    /**
     * @see visitableList - will be updated by removing shimmering model
     */
    private fun onFailAddRecipe(
        isRemoving: Boolean,
        position: Int,
        recipeId: String,
        errorMessage: String
    ) {
        visitableList.removeRecipeShimmering(recipeId)

        setAction(
            RecipeBookmarkAction.ShowToaster(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    message = errorMessage,
                    recipeId = recipeId,
                    isSuccess = false
                )
            )
        )
    }

    private fun onResponseRemoveRecipeBE(
        isRemoving: Boolean,
        title: String,
        position: Int,
        recipeId: String,
        errorMessage: String,
        isSuccess: Boolean
    ) {
        if (isSuccess) {
            onSuccessRemoveRecipe(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                title = title
            )
        } else {
            onFailRemoveRecipe(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                errorMessage = errorMessage
            )
        }
        updateVisitableList()
    }

    /**
     * @see visitableList - will be updated by removing shimmering model
     */
    private fun onSuccessRemoveRecipe(
        isRemoving: Boolean,
        position: Int,
        recipeId: String,
        title: String
    ) {
        visitableList.removeRecipeShimmering(recipeId)

        setAction(
            RecipeBookmarkAction.ShowToaster(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    title = title,
                    recipeId = recipeId,
                    isSuccess = true
                )
            )
        )
    }

    private fun onFailRemoveRecipe(
        isRemoving: Boolean,
        position: Int,
        recipeId: String,
        errorMessage: String
    ) {
        restoreRecipeRemoved(
            tempPosition = position,
            restorePosition = position,
            recipeId = recipeId
        )

        setAction(
            RecipeBookmarkAction.ShowToaster(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    message = errorMessage,
                    recipeId = recipeId,
                    isSuccess = false
                )
            )
        )
    }

    /**
     * @see tempRecipeRemoved - restore recipe back to layout
     * @see visitableList - will be updated by restoring recipe back
     */
    private fun restoreRecipeRemoved(tempPosition: Int, restorePosition: Int, recipeId: String) {
        visitableList.removeRecipeShimmering(recipeId)
        tempRecipeRemoved[tempPosition]?.apply {
            visitableList.add(restorePosition, this)
        }
    }

    private fun onFailAddRemoveRecipeFE(
        isRemoving: Boolean,
        position: Int,
        recipeId: String,
        throwable: Throwable
    ) {
        updateVisitableList()

        setAction(
            RecipeBookmarkAction.ShowToaster(
                isRemoving = isRemoving,
                position = position,
                model = ToasterModel(
                    recipeId = recipeId,
                    throwable = throwable,
                    isSuccess = false
                )
            )
        )
    }

    /**
     * @see noNeedLoadMore - if true no need to load widgets more
     */
    private fun onResponseLoadFirstPageBE(
        recipeBookmarks: List<RecipeUiModel>,
        header: Header,
        hasNext: Boolean
    ) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE

        if (header.success) {
            visitableList.addAll(recipeBookmarks)
            updateVisitableList()
        } else {
            updateErrorState(
                errorCode = header.statusCode
            )
        }
    }

    /**
     * @see noNeedLoadMore - if true no need to load more recipe bookmark
     */
    private fun onResponseLoadMoreBE(
        recipeBookmarks: List<RecipeUiModel>,
        header: Header,
        hasNext: Boolean
    ) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE
        visitableList.removeRecipeProgressBar()

        if (header.success) {
            visitableList.addAll(recipeBookmarks)
        }

        updateVisitableList()
    }

    private fun onFailLoadMoreFE() {
        visitableList.removeRecipeProgressBar()
        updateVisitableList()
    }

    private fun showLoadMoreLoading() {
        visitableList.addRecipeProgressBar()
        updateVisitableList()
    }

    private fun showItemLoading(isRemoving: Boolean, position: Int, recipeId: String) {
        if (isRemoving) {
            visitableList.removeAt(position)
            visitableList.addRecipeShimmering(position, recipeId)
        } else {
            visitableList.addRecipeShimmering(RESTORE_ADD_POSITION, recipeId)
        }
        updateVisitableList()
    }

    private fun updateLayoutWithTempRecipeRemoved(
        isRemoving: Boolean,
        position: Int,
        recipeId: String
    ) {
        if (isRemoving) {
            restoreRecipeRemoved(
                tempPosition = position,
                restorePosition = position,
                recipeId = recipeId
            )
        } else {
            visitableList.removeAt(RESTORE_ADD_POSITION)
        }
    }

    private fun loadFirstPage() {
        launchCatchError(block = {
            showPageLoading()
            pageCounter = DEFAULT_PAGE

            val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter++)
            onResponseLoadFirstPageBE(recipeBookmarks, header, hasNext)
        }, onError = { throwable ->
                updateErrorState(throwable)
            })
    }

    private fun onClickEmptyStateButton(event: ClickEmptyStateActionButton) {
        val errorCode = event.errorCode

        if (errorCode == ERROR_PAGE_NOT_FOUND || errorCode == ERROR_MAINTENANCE) {
            setAction(RecipeBookmarkAction.PressBackButton)
        } else {
            loadFirstPage()
        }
    }

    private fun removeRecipeBookmark(event: RemoveRecipeBookmark) {
        val title = event.title
        val position = event.position
        val recipeId = event.recipeId
        val isRemoving = event.isRemoving

        launchCatchError(block = {
            tempRecipeRemoved[position] = visitableList[position]

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

    private fun addRecipeBookmark(event: AddRecipeBookmark) {
        val recipeId = event.recipeId
        val position = event.position
        val isRemoving = event.isRemoving

        launchCatchError(block = {
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

    private fun loadMore(event: LoadMoreRecipeBookmarkList) {
        if (!shouldLoadMore(event)) return

        launchCatchError(block = {
            showLoadMoreLoading()
            val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter++)
            onResponseLoadMoreBE(recipeBookmarks, header, hasNext)
        }) {
            onFailLoadMoreFE()
        }
    }

    private fun setAction(action: RecipeBookmarkAction) {
        viewModelScope.launch { _uiAction.emit(action) }
    }

    private fun showPageLoading() {
        _uiState.update { RecipeBookmarkState.Loading }
    }

    private fun updateVisitableList() {
        _uiState.update {
            if (visitableList.isNotEmpty()) {
                val scrollToTop = visitableList
                    .getOrNull(FIRST_ITEM_INDEX) is RecipeShimmeringUiModel
                RecipeBookmarkState.Show(visitableList, scrollToTop)
            } else {
                RecipeBookmarkState.Empty
            }
        }
    }

    private fun updateErrorState(throwable: Throwable? = null, errorCode: String? = null) {
        _uiState.update {
            RecipeBookmarkState.Error(throwable, errorCode)
        }
    }

    private fun shouldLoadMore(event: LoadMoreRecipeBookmarkList): Boolean {
        return !noNeedLoadMore && event.scrolledToBottom &&
            visitableList.lastOrNull() !is RecipeProgressBarUiModel
    }
}
