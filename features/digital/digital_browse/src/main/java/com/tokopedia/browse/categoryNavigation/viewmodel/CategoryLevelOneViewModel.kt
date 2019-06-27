package com.tokopedia.browse.categoryNavigation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoryAllList
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryLevelOneUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject


class CategoryLevelOneViewModel @Inject constructor(var getCategoryListUseCase: GetCategoryLevelOneUseCase) : ViewModel() {


    val categoryAllList = MutableLiveData<Result<CategoryAllList>>()


    fun bound() {
        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(true), object : Subscriber<CategoryAllList>() {
            override fun onNext(t: CategoryAllList?) {
                categoryAllList.value = Success((t as CategoryAllList))
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                categoryAllList.value = Fail(e)
            }

        })

    }


    fun getCategoryList(): MutableLiveData<Result<CategoryAllList>> {

        return categoryAllList
    }


}