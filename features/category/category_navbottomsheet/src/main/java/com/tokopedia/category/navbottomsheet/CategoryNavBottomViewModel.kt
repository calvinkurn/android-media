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
import com.tokopedia.category.navbottomsheet.model.CategoryAllListResponse
import com.tokopedia.category.navbottomsheet.model.CategoryDetailResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class CategoryNavBottomViewModel @Inject constructor() : ViewModel() {
    private var categoryListLiveData = MutableLiveData<Result<CategoryAllList>>()

    @Inject
    lateinit var repository: CategoryRepository


    fun getCategoryList(): LiveData<Result<CategoryAllList>> {
        return categoryListLiveData
    }

    fun getCategoriesFromServer(categoryID: String) {

        viewModelScope.launchCatchError(
                block = {
                    val response = repository.getCategoryListWithCategoryDetail(categoryID)
                    val item: CategoryAllList? =
                            response?.getData<CategoryAllListResponse>(CategoryAllListResponse::class.java)?.categoryAllList
                    response?.getData<CategoryDetailResponse>(CategoryDetailResponse::class.java)?.categoryDetailQuery?.data?.let {
                        item?.categoryDetailData = it
                    }
                    item?.let {
                        categoryListLiveData.value = Success(it)
                    }
                },
                onError = {
                    categoryListLiveData.value = Fail(it)
                }
        )
    }


}