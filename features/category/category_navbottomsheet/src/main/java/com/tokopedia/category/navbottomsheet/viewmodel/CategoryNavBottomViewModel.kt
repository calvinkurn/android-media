package com.tokopedia.category.navbottomsheet.viewmodel

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

    fun getPositionFromL2L3CategoryId(categoryList: List<ChildItem?>?, selectedID: String): Int {
        categoryList?.let {
            for (i in categoryList.indices) {
                categoryList[i]?.id?.let {
                    if (it == selectedID) {
                        return i
                    }
                }
            }
        }
        return -1
    }

    fun getSelectedL3PositionWithSemua(levelThreeCategoryList: List<ChildItem?>?, selectedLevelThreeID: String): Int {
        return if (selectedLevelThreeID.isNotEmpty())
            getPositionFromL2L3CategoryId(levelThreeCategoryList, selectedLevelThreeID).let {selectedLevelThreePosition->
                if (selectedLevelThreePosition != -1)
                    selectedLevelThreePosition + 1
                else
                    selectedLevelThreePosition
            }
        else
            0
    }

    fun moveSelectedCatToFirst(categoryList: LinkedList<CategoriesItem?>, positionToMove: Int) {
        if (positionToMove != 0 && positionToMove < categoryList.size) {
            val item = categoryList.removeAt(positionToMove)
            categoryList.addFirst(item)
        }
    }

    fun setupStateModel(categoryDetailData: CategoryDetailData, model: CategoryNavStateModel) {
        if (0 != categoryDetailData.id) {
            if (0 != categoryDetailData.parent) {
                if (categoryDetailData.parent == categoryDetailData.rootId) {
//                    Given id is L2.
                    model.selectedLevelOneID = categoryDetailData.parent.toString()
                    model.selectedLevelTwoID = categoryDetailData.id.toString()
                    model.selectedLevelThreeID = ""
                } else {
//                    Given Id is L3
                    model.selectedLevelOneID = categoryDetailData.rootId.toString()
                    model.selectedLevelTwoID = categoryDetailData.parent.toString()
                    model.selectedLevelThreeID = categoryDetailData.id.toString()
                }
            } else {
                if (categoryDetailData.id == categoryDetailData.rootId) {
//                    Given id is L1
                    model.selectedLevelOneID = categoryDetailData.id.toString()
                    model.selectedLevelTwoID = ""
                    model.selectedLevelThreeID = ""
                }
            }
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

    fun addShimmerItemsToL2(): List<ChildItem> {
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