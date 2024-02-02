package com.tokopedia.home_explore_category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_explore_category.domain.usecase.GetExploreCategoryUseCase
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun toggleSelectedCategory(categoryId: String) {
        val exploreCategoryState = _exploreCategoryState.value
        if (exploreCategoryState is ExploreCategoryState.Success) {
            _exploreCategoryState.update {
                val newCategoryList =
                    exploreCategoryState.data.exploreCategoryList.map { exploreCategory ->
                        if (exploreCategory.id == categoryId) {
                            val inverseSelected = !exploreCategory.isSelected
                            exploreCategory.copy(isSelected = inverseSelected)
                        } else {
                            exploreCategory.copy(isSelected = false)
                        }
                    }

                ExploreCategoryState.Success(exploreCategoryState.data.copy(exploreCategoryList = newCategoryList))
            }
        }
    }
}
