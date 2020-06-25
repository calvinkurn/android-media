package com.tokopedia.kategori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.usecase.CategoryLevelTwoItemsUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

const val categoryDepth = 2

class CategoryLevelTwoViewModel @Inject constructor(val categoryLevelTwoItemsUseCase: CategoryLevelTwoItemsUseCase) : ViewModel() {

     var childItem = MutableLiveData<Result<List<CategoryChildItem>>>()

    fun refresh(id: String) {
        viewModelScope.launchCatchError(
                block = {
                    val response = categoryLevelTwoItemsUseCase.getCategoryListItems(categoryLevelTwoItemsUseCase.createRequestParams(categoryDepth, true, id))
                    response?.let {
                        childItem.value = Success(it)
                    }

                },
                onError = {
                    childItem.value = Fail(it)

                }
        )
    }

    fun getLevelTwoList(): LiveData<Result<List<CategoryChildItem>>> {
        return childItem
    }

}