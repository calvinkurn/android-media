package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.GetRecipeBookmarksMapper.mapResponseToRecipeBookmarkUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_LIMIT
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_WIDGET_COUNTER
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
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
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private val layout: MutableList<RecipeUiModel> = mutableListOf()
    private var pageCounter: Int = DEFAULT_PAGE
    private var newWidgetCounter: Int = DEFAULT_WIDGET_COUNTER
    private var tempRecipeRemoved: Pair<Int, RecipeUiModel>? = null

    private val _loadRecipeBookmarks: MutableStateFlow<UiState<List<RecipeUiModel>>?> = MutableStateFlow(null)
    private val _moreRecipeBookmarks: MutableStateFlow<UiState<List<RecipeUiModel>>?> = MutableStateFlow(null)
    private val _toaster: MutableStateFlow<UiState<ToasterUiModel>?> = MutableStateFlow(null)
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val userId: String
        get() = userSessionInterface.userId
    private val warehouseId: String
        get() = chooseAddressData.warehouse_id

    val loadRecipeBookmarks: StateFlow<UiState<List<RecipeUiModel>>?>
        get() = _loadRecipeBookmarks
    val moreRecipeBookmarks: StateFlow<UiState<List<RecipeUiModel>>?>
        get() = _moreRecipeBookmarks
    val toaster: StateFlow<UiState<ToasterUiModel>?>
        get() = _toaster
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

    fun removeRecipeBookmark(title: String, position: Int, recipeId: String) {
        launchCatchError(block = {
            _toaster.value = UiState.Loading()
            val response = removeRecipeBookmarkUseCase.execute(
                userId = userId,
                recipeId = recipeId
            )

            if (response.success) {
                tempRecipeRemoved = Pair(position, layout[position])
                layout.removeFirst { it.id == recipeId }
                _toaster.value = UiState.Success(ToasterUiModel(
                    message = "\"$title\" sudah dihapus dari Buku Resep.",
                    recipeId = recipeId,
                    isSuccess = response.success,
                    cta = "Batalkan"
                ))
            } else {
                _toaster.value = UiState.Success(ToasterUiModel(
                    message = response.header.message,
                    recipeId = recipeId,
                    isSuccess = response.success
                ))
            }

            _loadRecipeBookmarks.value = UiState.Success(layout)
        }, onError = {
            _loadRecipeBookmarks.value = UiState.Fail(it)
        })
    }

    fun addRecipeBookmark(recipeId: String) {
        launchCatchError(block = {
            _toaster.value = UiState.Loading()
            val response = addRecipeBookmarkUseCase.execute(
                userId = userId,
                recipeId = recipeId
            )

            if (response.success) {
                tempRecipeRemoved?.apply {
                    layout.add(first, second)
                }
            } else {
                _toaster.value = UiState.Success(ToasterUiModel(
                    message = response.header.message,
                    recipeId = recipeId,
                    isSuccess = response.success
                ))
            }

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

    fun removeToaster() {
        _toaster.value = null
        tempRecipeRemoved = null
    }

}