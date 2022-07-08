package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.GetRecipeBookmarksMapper.mapResponseToRecipeBookmarkUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterRecipeRemovedUiModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TokoNowRecipeBookmarkViewModel @Inject constructor(
    private val getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val listItems = mutableListOf<RecipeUiModel>()

    private val _recipeBookmarks: MutableStateFlow<UiState<List<RecipeUiModel>>> = MutableStateFlow(UiState.Empty())
    val recipeBookmarks: StateFlow<UiState<List<RecipeUiModel>>>
        get() = _recipeBookmarks

    private val _toasterMessage: MutableStateFlow<ToasterRecipeRemovedUiModel?> = MutableStateFlow(null)
    val toasterMessage: StateFlow<ToasterRecipeRemovedUiModel?>
        get() = _toasterMessage

    fun getRecipeBookmarks(userId: String, warehouseId: String, page: Int, limit: Int) {
        launchCatchError(block =  {
            _recipeBookmarks.value = UiState.Loading()
            delay(300)
            listItems.addAll(
                getRecipeBookmarksUseCase.execute(
                    userId = userId,
                    warehouseId = warehouseId,
                    page = page,
                    limit = limit
                ).mapResponseToRecipeBookmarkUiModelList()
            )
            _recipeBookmarks.value = UiState.Success(listItems)
        }, onError = {

        })
    }

    fun removeRecipeBookmark(userId: String, recipeId: String) {
        launchCatchError(block = {
//            val bookmarkRemoved = removeRecipeBookmarkUseCase.execute(
//                userId = userId,
//                recipeId = recipeId
//            )
//            _toasterMessage.value = ToasterRecipeRemovedUiModel(
//                message = bookmarkRemoved.header.message,
//                recipeId = recipeId,
//                isSuccess = bookmarkRemoved.success
//            )
            // case where there are 2 times error, the toaster has the same value, toaster cannot be shown
            _toasterMessage.value = ToasterRecipeRemovedUiModel(
                message = "Success",
                recipeId = recipeId,
                isSuccess = true
            )
            listItems.removeFirst { it.id == recipeId }
            _recipeBookmarks.value = UiState.Success(listItems)
        }, onError = {

        })
    }

}