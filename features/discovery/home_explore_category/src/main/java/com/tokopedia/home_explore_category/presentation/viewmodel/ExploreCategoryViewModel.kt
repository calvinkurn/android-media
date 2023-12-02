package com.tokopedia.home_explore_category.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_explore_category.domain.usecase.GetExploreCategoryUseCase
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploreCategoryViewModel @Inject constructor(
    private val getExploreCategoryUseCase: Lazy<GetExploreCategoryUseCase>,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _exploreCategoryState =
        MutableStateFlow<ExploreCategoryState<ExploreCategoryResultUiModel>>(ExploreCategoryState.Loading)

    val exploreCategoryState: StateFlow<ExploreCategoryState<ExploreCategoryResultUiModel>>
        get() = _exploreCategoryState.asStateFlow()

    private val _exploreCategoryUiEvent = MutableSharedFlow<ExploreCategoryUiEvent>(replay = Int.ONE)
    val exploreCategoryUiEvent: SharedFlow<ExploreCategoryUiEvent>
        get() = _exploreCategoryUiEvent.asSharedFlow()

    fun onExploreCategoryUiEvent(uiEvent: ExploreCategoryUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is ExploreCategoryUiEvent.OnExploreCategoryItemClicked -> {
                    toggleSelectedCategory(uiEvent.categoryId)
                }
                else -> {
                    _exploreCategoryUiEvent.emit(uiEvent)
                }
            }
        }
    }

    fun fetchExploreCategory() {
        _exploreCategoryState.tryEmit(ExploreCategoryState.Loading)

        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                getExploreCategoryUseCase.get().execute()
            }
            _exploreCategoryState.emit(ExploreCategoryState.Success(result))
        }, onError = {
                _exploreCategoryState.emit(ExploreCategoryState.Fail(it))
            })
    }

    private fun toggleSelectedCategory(categoryId: String) {
        _exploreCategoryState.update {
            when (it) {
                is ExploreCategoryState.Success -> {
                    val newCategoryList = it.data.copy(
                        exploreCategoryList = it.data.exploreCategoryList.map { exploreCategory ->
                            if (exploreCategory.id == categoryId) {
                                exploreCategory.copy(isSelected = !exploreCategory.isSelected)
                            } else {
                                exploreCategory.copy(isSelected = false)
                            }
                        }
                    )
                    ExploreCategoryState.Success(newCategoryList)
                }
                else -> it
            }
        }
    }
}
