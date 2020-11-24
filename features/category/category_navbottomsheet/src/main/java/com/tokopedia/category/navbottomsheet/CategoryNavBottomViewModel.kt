package com.tokopedia.category.navbottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.category.navbottomsheet.model.*
import com.tokopedia.category.navbottomsheet.repository.CategoryRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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


    fun getPositionFromCategoryId(categoryList: LinkedList<CategoriesItem?>, selectedLevelOneID: String): Int {
        for (i in 0 until categoryList.size) {
            categoryList[i]?.id?.let {
                if (it == selectedLevelOneID) {
                    return i
                }
            }
        }
        return 0
    }

    fun getPositionFromL2L3CategoryId(categoryList: List<ChildItem?>?, selectedLevelOneID: String): Int {
        categoryList?.let {
            for (i in categoryList.indices) {
                categoryList[i]?.id?.let {
                    if (it == selectedLevelOneID) {
                        return i
                    }
                }
            }
        }
        return -1
    }

    fun moveSelectedCatToFirst(categoryList: LinkedList<CategoriesItem?>, positionToMove: Int) {
        if (positionToMove != 0 && positionToMove < categoryList.size) {
            val item = categoryList.removeAt(positionToMove)
            categoryList.addFirst(item)
        }
    }

    fun addShimmerItems(categoryList: LinkedList<CategoriesItem?>) {
        // adding shimmer elements in recyclerview
        val item = CategoriesItem()
        item.type = 0
        for (i in 0..8) {
            categoryList.add(item)
        }
    }

    fun addShimmerItemsToL2() : List<ChildItem> {
        // adding shimmer elements in recyclerview
        val list = ArrayList<ChildItem>()
        val item = ChildItem()
        item.viewType = 0
        for (i in 0..12) {
            list.add(item)
        }
        return list
    }
}