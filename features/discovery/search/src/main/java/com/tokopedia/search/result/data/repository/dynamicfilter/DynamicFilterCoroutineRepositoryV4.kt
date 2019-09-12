package com.tokopedia.search.result.data.repository.dynamicfilter

import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.data.source.dynamicfilter.DynamicFilterCoroutineDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DynamicFilterCoroutineRepositoryV4(
        private val dynamicFilterCoroutineDataSource: DynamicFilterCoroutineDataSource,
        private val coroutineDispatcher: CoroutineDispatcher
): Repository<DynamicFilterModel> {

    override suspend fun query(params: Map<String, Any?>): DynamicFilterModel {
        return withContext(coroutineDispatcher) {
            dynamicFilterCoroutineDataSource.getDynamicAttributeV4(params)
        }
    }
}
