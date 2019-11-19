package com.tokopedia.search.result.data.source.dynamicfilter

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.network.service.BrowseApi
import retrofit2.Response

open class DynamicFilterCoroutineDataSource(
        private val browseApi: BrowseApi,
        private val dynamicFilterMapper: Mapper<Response<String>, DynamicFilterModel>
) {

    suspend fun getDynamicAttribute(param: Map<String, Any?>): DynamicFilterModel {
        val response = browseApi.getDynamicAttributeDeferred(param).await() ?: return DynamicFilterModel()

        return dynamicFilterMapper.convert(response)
    }

    suspend fun getDynamicAttributeV4(param: Map<String, Any?>): DynamicFilterModel {
        val response = browseApi.getDynamicAttributeV4Deferred(param).await() ?: return DynamicFilterModel()

        return dynamicFilterMapper.convert(response)
    }
}