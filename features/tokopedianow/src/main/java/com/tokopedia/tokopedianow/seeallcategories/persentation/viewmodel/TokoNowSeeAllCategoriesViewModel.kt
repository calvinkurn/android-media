package com.tokopedia.tokopedianow.seeallcategories.persentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowSeeAllCategoriesViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    val categoryList: LiveData<Result<GetCategoryListResponse.CategoryListResponse>>
        get() = _categoryList

    private val _categoryList = MutableLiveData<Result<GetCategoryListResponse.CategoryListResponse>>()

    fun getCategoryList(warehouseId: String) {
        launchCatchError(block = {
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
            _categoryList.postValue(Success(response))
        }) {
            _categoryList.postValue(Fail(it))
        }
    }

}
