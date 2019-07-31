package com.tokopedia.browse.categoryNavigation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.CategoryHotlist
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryHotListUseCase
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryLevelTwoUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CategoryLevelTwoViewModel @Inject constructor(var getCategoryListUseCase: GetCategoryLevelTwoUsecase,
                                                    var getCategoryHotListUseCase: GetCategoryHotListUseCase) : ViewModel() {

    var childItem = MutableLiveData<Result<List<ChildItem>>>()

    var hotlistItem = MutableLiveData<List<ListItem>>()


    fun refresh(id: String) {

        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(true, id), object : Subscriber<List<ChildItem>>() {
            override fun onNext(childItemList: List<ChildItem>) {
                childItem.value = Success(childItemList)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                childItem.value = Fail(e)
            }
        })
    }


    fun fetchHotlist(categoryId: String, categoryname: String) {

        getCategoryHotListUseCase.execute(getCategoryHotListUseCase.createRequestParams(categoryId.toInt(), categoryname), object : Subscriber<CategoryHotlist>() {
            override fun onNext(categoryHotlist: CategoryHotlist?) {
                categoryHotlist!!.list.let {
                    if (categoryHotlist.list!!.isNotEmpty()) {
                        hotlistItem.value = categoryHotlist.list
                    }

                }

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {


            }
        })


    }

    fun getCategoryChildren(): MutableLiveData<Result<List<ChildItem>>> {
        return childItem
    }

    fun getCategoryHotlist(): MutableLiveData<List<ListItem>> {
        return hotlistItem
    }
}