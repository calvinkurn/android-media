package com.tokopedia.browse.categoryNavigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList
import com.tokopedia.browse.categoryNavigation.domain.usecase.AllCategoryQueryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject


class CategoryLevelOneViewModel @Inject constructor(private var getCategoryListUseCase: AllCategoryQueryUseCase) : ViewModel() {

    var categoryDepth = 2
    val categoryListLiveData = MutableLiveData<Result<CategoryAllList>>()

    fun bound() {
        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(categoryDepth, true), object : Subscriber<CategoryAllList>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                categoryListLiveData.value = Fail(e)
            }

            override fun onNext(categoryAllList: CategoryAllList?) {
                categoryListLiveData.value = Success(categoryAllList as CategoryAllList)
            }
        })

    }

    fun getCategoryList(): MutableLiveData<Result<CategoryAllList>> = categoryListLiveData

    override fun onCleared() {
        super.onCleared()
        getCategoryListUseCase.unsubscribe()
    }
}