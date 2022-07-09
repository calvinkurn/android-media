package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.GetRecipeBookmarksMapper.mapResponseToRecipeBookmarkUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_LIMIT
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_WIDGET_COUNTER
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterRecipeRemovedUiModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TokoNowRecipeBookmarkViewModel @Inject constructor(
    private val chooseAddressData: LocalCacheModel,
    private val userSessionInterface: UserSessionInterface,
    private val getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val layout: MutableList<Visitable<*>> = mutableListOf()
    private var pageCounter: Int = DEFAULT_PAGE
    private var newWidgetCounter: Int = DEFAULT_WIDGET_COUNTER

    private val _loadRecipeBookmarks: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(UiState.Empty())
    private val _moreRecipeBookmarks: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(UiState.Empty())
    private val _toasterMessage: MutableStateFlow<ToasterRecipeRemovedUiModel?> = MutableStateFlow(null)
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val userId: String
        get() = userSessionInterface.userId
    private val warehouseId: String
        get() = chooseAddressData.warehouse_id

    val loadRecipeBookmarks: StateFlow<UiState<List<Visitable<*>>>>
        get() = _loadRecipeBookmarks
    val moreRecipeBookmarks: StateFlow<UiState<List<Visitable<*>>>>
        get() = _moreRecipeBookmarks
    val toasterMessage: StateFlow<ToasterRecipeRemovedUiModel?>
        get() = _toasterMessage
    val isOnScrollNotNeeded: StateFlow<Boolean>
        get() = _isOnScrollNotNeeded

    private suspend fun getRecipeBookmarks(page: Int): List<RecipeUiModel> {
        return getRecipeBookmarksUseCase.execute(
            userId = userId,
            warehouseId = warehouseId,
            page = page,
            limit = DEFAULT_LIMIT
        ).mapResponseToRecipeBookmarkUiModelList()
    }

    fun loadFirstPage() {
        launchCatchError(block =  {
            _loadRecipeBookmarks.value = UiState.Loading()

            delay(3000)

            val recipeBookmarks = getRecipeBookmarks(pageCounter)
            newWidgetCounter = recipeBookmarks.size
            layout.addAll(recipeBookmarks)

            _loadRecipeBookmarks.value = UiState.Success(layout)
        }, onError = {
            _loadRecipeBookmarks.value = UiState.Fail(it)
        })
    }

    fun removeRecipeBookmark(recipeId: String) {
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
            layout.removeFirst { it is RecipeUiModel && it.id == recipeId }
            _loadRecipeBookmarks.value = UiState.Success(layout)
        }, onError = {
            _loadRecipeBookmarks.value = UiState.Fail(it)
        })
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean,
        isLoadMoreLoading: Boolean,
    ) {
        launchCatchError(block = {
            if (newWidgetCounter < DEFAULT_LIMIT) {
                _isOnScrollNotNeeded.value = true
            } else if (isAtTheBottomOfThePage && !isLoadMoreLoading) {
                _moreRecipeBookmarks.value = UiState.Loading()

                delay(3000)

                val recipeBookmarks = getRecipeBookmarks(pageCounter++).toMutableList()
                recipeBookmarks.removeLast()
                recipeBookmarks.removeLast()
                newWidgetCounter = recipeBookmarks.size
                layout.addAll(recipeBookmarks)

                _moreRecipeBookmarks.value = UiState.Success(layout)
            }
        }) {
            _moreRecipeBookmarks.value = UiState.Fail(it)
        }
    }

}