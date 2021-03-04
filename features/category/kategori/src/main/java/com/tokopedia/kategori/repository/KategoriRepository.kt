package com.tokopedia.kategori.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.common_category.data.kategorymodel.CategoryAllList
import com.tokopedia.common_category.data.kategorymodel.Data
import com.tokopedia.common_category.data.raw.GQL_CATEGORY_LIST
import javax.inject.Inject

class KategoriRepository @Inject constructor() : BaseRepository() {

    suspend fun getCategoryListItems(reqParams: Map<String, Any>): CategoryAllList? {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`() * 2).setSessionIncluded(true).build()

        return (getGQLData(GQL_CATEGORY_LIST, Data::class.java, reqParams, cacheStrategy) as Data).categoryAllList
    }
}