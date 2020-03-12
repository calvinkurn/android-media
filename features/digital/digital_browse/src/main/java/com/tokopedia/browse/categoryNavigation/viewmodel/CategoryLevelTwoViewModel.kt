package com.tokopedia.browse.categoryNavigation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoriesItem
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryAllList
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryChildItem
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.ChildItem
import com.tokopedia.browse.categoryNavigation.domain.usecase.AllCategoryQueryUseCase
import com.tokopedia.browse.categoryNavigation.utils.Constants
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CategoryLevelTwoViewModel @Inject constructor(private var allCategoryQueryUseCase: AllCategoryQueryUseCase) : ViewModel() {

    var categoryDepth = 2
    var childItem = MutableLiveData<Result<List<CategoryChildItem>>>()
    var YANG_LAGI_HITS_TITLE = "yanglagihits"
    fun refresh(id: String) {
        allCategoryQueryUseCase.execute(allCategoryQueryUseCase.createRequestParams(categoryDepth, true), object : Subscriber<CategoryAllList>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                childItem.value = Fail(e)
            }

            override fun onNext(categoryAllList: CategoryAllList?) {
                childItem.value = createChildList((categoryAllList as CategoryAllList), id)
            }
        })
    }

    private fun createChildList(categoryAllList: CategoryAllList, id: String): Result<List<CategoryChildItem>>? {
        val defaultCaseID = "0"
        val iterator = categoryAllList.categories
        val childList: MutableList<CategoryChildItem>? = ArrayList()

        iterator?.forEach {
            if (it?.id.equals(id)) {
                if (id == defaultCaseID) {
                    it?.child?.let { levelOneList ->
                        for (levelOneChild in levelOneList) {
                            childList?.add(createChildItem(Constants.TextHeaderView, levelOneChild))
                            levelOneChild?.child.let { levelTwoChildItem ->
                                if (levelTwoChildItem != null) {
                                    val type = if (getTrimmedString(levelOneChild?.name
                                                    ?: "") == YANG_LAGI_HITS_TITLE) {
                                        Constants.YangLagiHitsView
                                    } else {
                                        Constants.ProductView
                                    }

                                    val pos = 1
                                    for (childItem in levelTwoChildItem) {
                                        lateinit var item: CategoryChildItem
                                        if (type == Constants.ProductView) {
                                            item = createChildItem(type, childItem, pos)
                                            item.isSeringKamuLihat = true
                                        } else {
                                            item = createChildItem(type, childItem)
                                        }
                                        childList?.add(item)
                                    }
                                }
                            }
                        }
                    }
                    return Success(childList as List<CategoryChildItem>)

                } else {
                    childList?.add(createChildItem(Constants.ProductHeaderView, it))
                    var position = 1
                    val totalCount = it?.child?.size ?: 0
                    it?.child?.let { levelOneList ->
                        for (element in levelOneList) {
                            childList?.add(createChildItem(Constants.ProductView, element, position, totalCount))
                            position++
                        }
                    }
                    return Success(childList as List<CategoryChildItem>)
                }
            }
        }
        return Fail(Throwable("NO DATA"))
    }

    private fun createChildItem(itemType: Int, childItem: ChildItem?, position: Int = 0, sameCategoryTotalCount: Int = 0): CategoryChildItem {
        return CategoryChildItem(sameCategoryTotalCount,
                position,
                itemType,
                false,
                childItem?.identifier,
                childItem?.hexColor,
                childItem?.parentName,
                if (itemType == Constants.ProductHeaderView || itemType == Constants.YangLagiHitsView) childItem?.iconBannerURL else childItem?.iconImageUrl,
                childItem?.applinks,
                childItem?.name,
                childItem?.id,
                childItem?.iconBannerURL,
                childItem?.url)

    }

    private fun createChildItem(itemType: Int, childItem: CategoriesItem?, position: Int = 0, sameCategoryTotalCount: Int = 0): CategoryChildItem {
        return CategoryChildItem(sameCategoryTotalCount,
                position,
                itemType,
                false,
                childItem?.identifier,
                childItem?.hexColor,
                childItem?.parentName,
                if (itemType == Constants.ProductHeaderView) childItem?.iconBannerURL else childItem?.iconImageUrl,
                childItem?.applinks,
                childItem?.name,
                childItem?.id,
                childItem?.iconBannerURL,
                childItem?.url)
    }

    private fun getTrimmedString(label: String): String {
        return label.replace(" ", "").toLowerCase()
    }

    fun getCategoryChildren(): MutableLiveData<Result<List<CategoryChildItem>>> = childItem

    override fun onCleared() {
        super.onCleared()
        allCategoryQueryUseCase.unsubscribe()
    }
}