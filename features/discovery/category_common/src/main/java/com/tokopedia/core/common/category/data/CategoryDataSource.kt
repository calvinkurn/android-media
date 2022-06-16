package com.tokopedia.core.common.category.data

import com.tokopedia.core.common.category.data.mapper.CategoryServiceToDbMapper
import com.tokopedia.core.common.category.data.source.cloud.CategoryCloud
import com.tokopedia.core.common.category.data.source.db.CategoryDataBase
import com.tokopedia.core.common.category.data.source.db.CategoryDataManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import rx.Observable
import rx.functions.Func1
import java.lang.RuntimeException

class CategoryDataSource(val categoryDataManager: CategoryDataManager,
                         val categoryCloud: CategoryCloud) {

    fun checkCategoryAvailable(): Observable<Boolean> {
        return Observable
            .just(true)
            .map(FetchFromDatabase())
            .map(CheckDatabaseNotNull())
            .onErrorResumeNext(fetchDataFromNetwork())
    }

    fun getCategoryName(categoryId: Long): Observable<String> {
        return categoryDataManager.getCategoryName(categoryId)
    }

    private fun fetchDataFromNetwork(): Observable<Boolean> {
        return categoryCloud.fetchDataFromNetwork()
            .map(CategoryServiceToDbMapper())
            .map(StoreDataToDatabase())
    }



    inner class CheckDatabaseNotNull :
        Func1<List<CategoryDataBase>?, Boolean> {
        override fun call(categoryDataBases: List<CategoryDataBase>?): Boolean {
            if (categoryDataBases == null || categoryDataBases.isEmpty()) {
                throw RuntimeException(String.EMPTY)
            }
            return true
        }
    }

    inner class StoreDataToDatabase :
        Func1<List<CategoryDataBase>, Boolean> {
        override fun call(categoryDataBases: List<CategoryDataBase>): Boolean {
            categoryDataManager.storeData(categoryDataBases)
            return true
        }
    }

    inner class FetchFromDatabase() :
        Func1<Boolean?, List<CategoryDataBase>> {
        override fun call(aBoolean: Boolean?): List<CategoryDataBase> {
            return categoryDataManager.fetchFromDatabase()
        }
    }

}

