package com.tokopedia.tokopedianow.categorylist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CategoryListViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    companion object {
        const val ERROR_PAGE_NOT_FOUND = "400"
        const val ERROR_SERVER = "500"
        const val ERROR_PAGE_FULL = "501"
        const val ERROR_MAINTENANCE = "502"
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    val categoryList: LiveData<Result<CategoryListResponse>>
        get() = _categoryList

    private val _categoryList = MutableLiveData<Result<CategoryListResponse>>()

    fun getCategoryList(warehouseId: String) {
        launchCatchError(block = {
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
            _categoryList.postValue(Success(response))
        }) {
            _categoryList.postValue(Fail(it))
        }
    }

}