package com.tokopedia.kategori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kategori.newcategory.CategoryAllList
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
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

    fun getCategoryList(): LiveData<Result<CategoryAllList>> = categoryListLiveData

    override fun onCleared() {
        super.onCleared()
        getCategoryListUseCase.unsubscribe()
    }
}