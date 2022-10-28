package com.tokopedia.core.common.category.data.source.db

import rx.Observable
import rx.schedulers.Schedulers
import java.lang.RuntimeException

class CategoryDataManager(val categoryDao: CategoryDao) {


    fun clearDatabase() {
        categoryDao.clearTables()
    }

    fun fetchCategoryFromParent(categoryId: Long): List<CategoryDataBase> {
        return categoryDao.getCategoryListByParent(categoryId)
    }

    fun fetchFromDatabase(): List<CategoryDataBase> {
        return categoryDao.getAllCategories()
    }

    fun getCategoryName(categoryId: Long): Observable<String> {
        return Observable.fromCallable {
            categoryDao.getCategoryName(
                categoryId
            )
        }
    }

    fun storeData(categoryDataBases: List<CategoryDataBase>) {
        Observable.fromCallable {
            categoryDao.insertMultiple(categoryDataBases)
            true
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun fetchCategoryWithId(categoryId: Long): CategoryDataBase {
        val category = categoryDao.getSingleCategory(categoryId)
        return category ?: throw RuntimeException("No category found")
    }
}