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
        _exploreCategoryState.update {
            when (it) {
                is ExploreCategoryState.Success -> {
                    val newCategoryList = it.data.exploreCategoryList.map { exploreCategory ->
                        if (exploreCategory.id == categoryId) {
                            exploreCategory.copy(isSelected = !exploreCategory.isSelected)
                        } else {
                            exploreCategory.copy(isSelected = false)
                        }
                    }

                    ExploreCategoryState.Success(it.data.copy(exploreCategoryList = newCategoryList))
                }

                else -> it
            }
        }
    }
}
