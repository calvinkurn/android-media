package com.tokopedia.core.common.category.data.repository

import rx.Observable

interface CategoryRepository {

    fun checkCategoryAvailable(): Observable<Boolean>

    fun fetchCategoryDisplay(categoryId: Long): Observable<List<String>>

    fun getCategoryName(categoryId: Long): Observable<String>
}