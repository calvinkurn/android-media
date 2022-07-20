package com.tokopedia.core.common.category.data

import com.tokopedia.core.common.category.data.mapper.CategoryDataToDomainMapper
import com.tokopedia.core.common.category.data.source.db.CategoryDataBase
import com.tokopedia.core.common.category.data.source.db.CategoryDataManager
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import rx.Observable
import rx.functions.Func1
import java.util.ArrayList

class FetchCategoryDataSource(val categoryDataManager: CategoryDataManager) {
    fun fetchCategoryDisplay(categoryId: Long): Observable<List<String>> {
        return Observable
            .just(categoryId)
            .map(FetchCategoryFromSelected())
            .map(GetStringListCategory())
    }

    inner class FetchCategoryFromSelected :
        Func1<Long, List<CategoryLevelDomainModel>> {
        override fun call(categoryId: Long): List<CategoryLevelDomainModel> {
            val categoryLevelDomainModels: MutableList<CategoryLevelDomainModel> = mutableListOf()
            var currentLevelSelected = categoryId
            do {
                val categoryLevelDomain = CategoryLevelDomainModel()
                categoryLevelDomain.selectedCategoryId = currentLevelSelected
                val parentId: Long = getParentId(currentLevelSelected)
                categoryLevelDomain.parentCategoryId = parentId
                val categoryFromParent: List<CategoryDataBase> = getCategoryFromParent(parentId)
                categoryLevelDomain.categoryModels =
                    CategoryDataToDomainMapper.mapDomainModels(categoryFromParent)
                categoryLevelDomainModels.add(Int.ZERO, categoryLevelDomain)
                currentLevelSelected = parentId
            } while (categoryLevelDomainModels[Int.ZERO].parentCategoryId != CategoryDataBase.LEVEL_ONE_PARENT.toLong())
            return categoryLevelDomainModels
        }
    }

    private fun getCategoryFromParent(categoryId: Long): List<CategoryDataBase> {
        return categoryDataManager.fetchCategoryFromParent(categoryId)
    }

    private fun getParentId(categoryId: Long): Long {
        val parentId = categoryDataManager.fetchCategoryWithId(categoryId).parentId.orZero()
        return parentId
    }

    inner class GetStringListCategory :
        Func1<List<CategoryLevelDomainModel>, List<String>> {
        override fun call(categoryLevelDomainModels: List<CategoryLevelDomainModel>): List<String> {
            val listString: MutableList<String> = ArrayList()
            for (domainModel in categoryLevelDomainModels) {
                val (_, name) = categoryDataManager
                    .fetchCategoryWithId(domainModel.selectedCategoryId)
                listString.add(name)
            }
            return listString
        }
    }
}