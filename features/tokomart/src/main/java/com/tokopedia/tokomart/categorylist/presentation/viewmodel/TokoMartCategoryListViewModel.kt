package com.tokopedia.tokomart.categorylist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoMartCategoryListViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    val categoryList: LiveData<Result<List<CategoryListItemUiModel>>>
        get() = _categoryList

    private val _categoryList = MutableLiveData<Result<List<CategoryListItemUiModel>>>()

    fun getCategoryList(warehouseId: String) {
        launchCatchError(block = {
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
            val data = CategoryListMapper.mapToUiModel(response)
            _categoryList.postValue(Success(data))
        }) {
            _categoryList.postValue(Fail(it))
        }
    }
}