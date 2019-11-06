package com.tokopedia.search.result.data.repository.dynamicfilter

import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterCoroutineDataSource
import kotlinx.coroutines.withContext

internal class DynamicFilterCoroutineRepositoryV4(
        private val dynamicFilterCoroutineDataSource: DynamicFilterCoroutineDataSource,
        private val coroutineDispatcher: DispatcherProvider
): Repository<DynamicFilterModel> {

    override suspend fun getResponse(inputParameter: Map<String, Any>): DynamicFilterModel {
        return withContext(coroutineDispatcher.io()) {
            dynamicFilterCoroutineDataSource.getDynamicAttributeV4(inputParameter)
        }
    }
}
