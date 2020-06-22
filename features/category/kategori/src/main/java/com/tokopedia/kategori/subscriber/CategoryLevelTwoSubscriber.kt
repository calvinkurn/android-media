package com.tokopedia.kategori.subscriber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kategori.Constants
import com.tokopedia.kategori.model.CategoriesItem
import com.tokopedia.kategori.model.CategoryAllList
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.model.ChildItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber

class CategoryLevelTwoSubscriber(val id: String) : Subscriber<CategoryAllList>() {


    private var YANG_LAGI_HITS_TITLE = "yanglagihits"
    var mutableChildItem = MutableLiveData<Result<List<CategoryChildItem>>>()

    override fun onNext(categoryAllList: CategoryAllList?) {
        mutableChildItem.value = createChildList((categoryAllList as CategoryAllList), id)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        mutableChildItem.value = Fail(e)
    }

    fun getCategoryList(): LiveData<Result<List<CategoryChildItem>>> {
        return mutableChildItem
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

                                    var pos = 0
                                    for (childItem in levelTwoChildItem) {
                                        lateinit var item: CategoryChildItem
                                        if (type == Constants.ProductView) {
                                            pos++
                                            item = createChildItem(type, childItem, pos, levelTwoChildItem.size)
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
}