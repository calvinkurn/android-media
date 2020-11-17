package com.tokopedia.category.navbottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.category.navbottomsheet.model.CategoryAllList
import com.tokopedia.category.navbottomsheet.repository.CategoryRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import androidx.lifecycle.viewModelScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class CategoryNavViewModel @Inject constructor(): ViewModel() {
    private var categoryListLiveData = MutableLiveData<Result<CategoryAllList>>()

    @Inject
    lateinit var repository: CategoryRepository


    fun getCategoryList(): LiveData<Result<CategoryAllList>> {
        return categoryListLiveData
    }

    fun getCategoriesFromServer() {

        viewModelScope.launchCatchError(
                block = {
                    val response = repository.getCategoryListItems(getRequestParams().parameters)
                    response?.let {
                        categoryListLiveData.value = Success(it)
                    }
                },
                onError = {
                    categoryListLiveData.value = Fail(it)
                }
        )
    }

    fun getRequestParams(): RequestParams {
        return RequestParams().apply {
//            putInt("categoryId", 0)
        }
    }


}