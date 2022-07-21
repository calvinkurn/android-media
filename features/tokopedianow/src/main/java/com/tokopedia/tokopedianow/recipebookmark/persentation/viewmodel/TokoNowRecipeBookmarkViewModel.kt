package com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.GetRecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment.Companion.DEFAULT_PER_PAGE
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipebookmark.util.UiState
import com.tokopedia.user.session.UserSessionInterface
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
    private var noNeedLoadMore: Boolean = true
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

    private suspend fun getRecipeBookmarks(page: Int): Triple<List<RecipeUiModel>, GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Header, Boolean> {
        val response = getRecipeBookmarksUseCase.execute(
            userId = userId,
            warehouseId = warehouseId,
            page = page,
            limit = DEFAULT_PER_PAGE
        ).data.tokonowGetRecipeBookmarks

        return Triple(
            response.data.recipes.mapResponseToUiModelList(),
            response.header,
            response.metadata.hasNext
        )
    }

    /**
     * @see toaster - failed toaster will be shown with capability to retry the event
     * @see loadRecipeBookmarks - layout will be updated with latest layout
     */
    private fun onResponseAddRemoveRecipeBE(isRemoving: Boolean, position: Int, recipeId: String, errorMessage: String, isSuccess: Boolean,  onSuccess: () -> Unit) {
        if (isSuccess) {
            onSuccess.invoke()
        } else {
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

        _loadRecipeBookmarks.value = UiState.Success(
            data = layout
        )
    }

    /**
     * @see tempRecipeRemoved - keep recipe before removed
     * @see toaster - success toaster will be shown with capability to add recipe back
     */
    private fun onSuccessRemoveRecipe(position: Int, recipeId: String, title: String, isSuccess: Boolean) {
        tempRecipeRemoved = Pair(position, layout[position])

        layout.removeFirst { it.id == recipeId }

        _toaster.value = UiState.Success(ToasterUiModel(
            isRemoving = isSuccess,
            position = position,
            model = ToasterModel(
                title = title,
                recipeId = recipeId,
                isSuccess = isSuccess
            ),
        ))
    }

    /**
     * @see tempRecipeRemoved - restore recipe back to layout
     * @see layout - will be updated by restoring recipe back
     */
    private fun onSuccessAddRecipe() {
        tempRecipeRemoved?.apply {
            layout.add(first, second)
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
    private fun onResponseLoadFirstPageBE(recipeBookmarks: List<RecipeUiModel>, header: GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Header, hasNext: Boolean) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE
        layout.addAll(recipeBookmarks)

        if (header.success) {
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
     * @see loadRecipeBookmarks - layout will be updated with latest layout or just hide load more loading
     */
    private fun onResponseLoadMoreBE(recipeBookmarks: List<RecipeUiModel>, header: GetRecipeBookmarksResponse.Data.TokonowGetRecipeBookmarks.Header, hasNext: Boolean) {
        noNeedLoadMore = !hasNext || recipeBookmarks.size < DEFAULT_PER_PAGE
        layout.addAll(recipeBookmarks)

        if (header.success) {
            _moreRecipeBookmarks.value = UiState.Success(
                data = layout
            )
        } else {
            _moreRecipeBookmarks.value = UiState.Fail(
                errorCode = header.statusCode
            )
        }
    }

    /**
     * @see loadRecipeBookmarks - will hide load more loading
     */
    private fun onFailLoadMoreFE(throwable: Throwable) {
        _moreRecipeBookmarks.value = UiState.Fail(
            throwable = throwable
        )
    }

    fun loadFirstPage() {
        launchCatchError(block =  {
            _loadRecipeBookmarks.value = UiState.Loading()
            val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter)
            onResponseLoadFirstPageBE(recipeBookmarks, header, hasNext)
        }, onError = { throwable ->
            onFailLoadFirstPageFE(throwable)
        })
    }

    fun removeRecipeBookmark(title: String, position: Int, recipeId: String) {
        val isRemoving = true
        launchCatchError(block = {
            _toaster.value = UiState.Loading(
                ToasterUiModel(
                    position = position,
                    isRemoving = isRemoving
                )
            )

            val response = removeRecipeBookmarkUseCase.execute(
                userId = userId,
                recipeId = recipeId
            )

            onResponseAddRemoveRecipeBE(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                errorMessage = response.header.message,
                isSuccess = response.header.success
            ) {
                onSuccessRemoveRecipe(
                    position = position,
                    recipeId = recipeId,
                    title = title,
                    isSuccess = response.header.success
                )
            }
        }, onError = { throwable ->
            onFailAddRemoveRecipeFE(
                isRemoving = isRemoving,
                position = position,
                recipeId = recipeId,
                throwable = throwable
            )
        })
    }

    fun addRecipeBookmark(recipeId: String) {
        val isRemoving = false
        launchCatchError(block = {
            _toaster.value = UiState.Loading(
                ToasterUiModel(
                    position = tempRecipeRemoved?.first
                )
            )

            val response = addRecipeBookmarkUseCase.execute(
                userId = userId,
                recipeId = recipeId
            )

            onResponseAddRemoveRecipeBE(
                isRemoving = isRemoving,
                position = tempRecipeRemoved?.first.orZero(),
                recipeId = recipeId,
                errorMessage = response.header.message,
                isSuccess = response.header.success
            ) {
                onSuccessAddRecipe()
            }
        }, onError = { throwable ->
            onFailAddRemoveRecipeFE(
                isRemoving = isRemoving,
                position = tempRecipeRemoved?.first.orZero(),
                recipeId = recipeId,
                throwable = throwable
            )
        })
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean,
        isLoadMoreLoading: Boolean,
    ) {
        launchCatchError(block = {
            if (noNeedLoadMore) {
                _isOnScrollNotNeeded.value = true
            } else if (isAtTheBottomOfThePage && !isLoadMoreLoading) {
                _moreRecipeBookmarks.value = UiState.Loading()
                val (recipeBookmarks, header, hasNext) = getRecipeBookmarks(pageCounter++)
                onResponseLoadMoreBE(recipeBookmarks, header, hasNext)
            }
        }) { throwable ->
            onFailLoadMoreFE(throwable)
        }
    }

    fun removeToaster() {
        _toaster.value = null
    }

}