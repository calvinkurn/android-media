package com.tokopedia.browse.categoryNavigation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoryAllList
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryLevelOneUseCase
import rx.Subscriber
import javax.inject.Inject


class CategoryLevelOneViewModel @Inject constructor(var getCategoryListUseCase: GetCategoryLevelOneUseCase) : ViewModel() {


    val categoryAllList = MutableLiveData<CategoryAllList>()


    fun bound() {
        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(true), object : Subscriber<CategoryAllList>() {
            override fun onNext(t: CategoryAllList?) {
                categoryAllList.value = t

            }

            override fun onCompleted() {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

        })

    }


    fun getCategoryList(): MutableLiveData<CategoryAllList> {

        return categoryAllList
    }


}