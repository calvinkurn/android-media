package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterRecipeRemovedUiModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowRecipeBookmarkViewModel @Inject constructor(
    private val getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val _recipeBookmarks: MutableStateFlow<UiState<List<RecipeUiModel>>> = MutableStateFlow(UiState.Empty())
    val recipeBookmarks: StateFlow<UiState<List<RecipeUiModel>>>
        get() = _recipeBookmarks

    private val _toasterMessage: MutableStateFlow<ToasterRecipeRemovedUiModel?> = MutableStateFlow(null)
    val toasterMessage: StateFlow<ToasterRecipeRemovedUiModel?>
        get() = _toasterMessage

    fun getRecipeBookmarks(userId: String, warehouseId: String, page: Int, limit: Int) {
        launchCatchError(block =  {
            _recipeBookmarks.value = UiState.Loading()
//            _recipeBookmarks.value = UiState.Success(
//                getRecipeBookmarksUseCase.execute(
//                    userId = userId,
//                    warehouseId = warehouseId,
//                    page = page,
//                    limit = limit
//                ).mapResponseToUiModelList()
//            )
            delay(300)
            _recipeBookmarks.value = UiState.Success(
                listOf(
                    RecipeUiModel(
                        id = "123",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    ),
                    RecipeUiModel(
                        id = "123",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    ),
                    RecipeUiModel(
                        id = "123",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    ),
                    RecipeUiModel(
                        id = "123",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    ),
                    RecipeUiModel(
                        id = "123",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    ),
                    RecipeUiModel(
                        id = "12355",
                        title = "Hello world",
                        portion = 5,
                        duration = 20,
                        picture = "https://cdn.kincir.com/2/hRjTIcHktyxU-ZyOBn9pXmYEmO_9PeIH2tHgjbISVQo/transform/rs:fit:764/src/production/2021-02/602x354_c019c149f0f621f329edf88731d00af80d0e6dd3.jpg",
                        tags = listOf()
                    )
                )
            )
        }, onError = {

        })
    }

    fun removeRecipeBookmark(userId: String, recipeId: String) {
        launchCatchError(block = {
            val bookmarkRemoved = removeRecipeBookmarkUseCase.execute(
                userId = userId,
                recipeId = recipeId
            )
            _toasterMessage.value = ToasterRecipeRemovedUiModel(
                message = bookmarkRemoved.header.message,
                recipeId = recipeId,
                isSuccess = bookmarkRemoved.success
            )
        }, onError = {

        })
    }

}