package com.tokopedia.libra.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.data.entity.LibraResponse
import com.tokopedia.libra.data.query.LibraGqlParam
import com.tokopedia.libra.data.query.LibraQuery
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.LibraMapper
import com.tokopedia.libra.domain.model.LibraUiModel
import javax.inject.Inject

class GetLibraRemoteUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val cacheRepository: CacheRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<LibraOwner, LibraUiModel>(dispatchers.io) {

    override fun graphqlQuery() = LibraQuery.getQuery()

    override suspend fun execute(params: LibraOwner): LibraUiModel {
        val request = request(params)

        return LibraMapper.map(request).also {
            cacheRepository.save(params, it)
        }
    }

    private suspend fun request(params: LibraOwner): LibraResponse {
        return repository.request(LibraQuery, LibraGqlParam(params.type))
    }
}
