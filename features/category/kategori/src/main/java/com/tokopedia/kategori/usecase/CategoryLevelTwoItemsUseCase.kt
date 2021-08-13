package com.tokopedia.kategori.usecase

import com.tokopedia.kategori.Constants
import com.tokopedia.common_category.data.kategorymodel.CategoriesItem
import com.tokopedia.common_category.data.kategorymodel.CategoryAllList
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.common_category.data.kategorymodel.ChildItem
import com.tokopedia.kategori.repository.KategoriRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


private const val KEY_DEPTH = "depth"
private const val KEY_IS_TRENDING = "isTrending"
private const val KEY_ID = "id"


class CategoryLevelTwoItemsUseCase @Inject constructor(val kategoriRepository: KategoriRepository) {

    private var YANG_LAGI_HITS_TITLE = "yanglagihits"

    suspend fun getCategoryListItems(reqParams: RequestParams): List<CategoryChildItem>? {

        val id = reqParams.getString(KEY_ID, "")

        val categoryRequestParams = RequestParams.create()
        categoryRequestParams.putInt(KEY_DEPTH, reqParams.getInt(KEY_DEPTH, 2))
        categoryRequestParams.putBoolean(KEY_IS_TRENDING, reqParams.getBoolean(KEY_IS_TRENDING, true))

        return createChildList(kategoriRepository.getCategoryListItems(categoryRequestParams.parameters), id
                ?: "0")
    }

    fun createRequestParams(depth: Int, isTrending: Boolean, id: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(KEY_DEPTH, depth)
        requestParams.putBoolean(KEY_IS_TRENDING, isTrending)
        requestParams.putString(KEY_ID, id)

        return requestParams
    }

    internal fun createChildList(categoryAllList: CategoryAllList?, id: String): List<CategoryChildItem>? {
        val defaultCaseID = "0"
        val iterator = categoryAllList?.categories
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
                    return childList as List<CategoryChildItem>

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
                    return childList as List<CategoryChildItem>
                }
            }
        }
        return null
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