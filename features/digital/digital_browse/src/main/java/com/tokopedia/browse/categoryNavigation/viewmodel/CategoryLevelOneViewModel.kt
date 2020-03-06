package com.tokopedia.browse.categoryNavigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.domain.usecase.AllCategoryQueryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject


class CategoryLevelOneViewModel @Inject constructor(private var getCategoryListUseCase: AllCategoryQueryUseCase) : ViewModel() {


    val categoryAllList = MutableLiveData<Result<com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList>>()


    fun bound() {
        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(2, true), object : Subscriber<com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                categoryAllList.value = Fail(e)
            }

            override fun onNext(t: com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList?) {
                categoryAllList.value = Success((t as com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList))
            }
        })

    }


    fun getCategoryList(): MutableLiveData<Result<com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList>> {

        return categoryAllList
    }

    override fun onCleared() {
        super.onCleared()
        getCategoryListUseCase.unsubscribe()
    }

}