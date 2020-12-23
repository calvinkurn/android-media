package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.AuthorizeAccess
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AuthorizeAccessResponse
import javax.inject.Inject

class AuthorizeAccessUseCase @Inject constructor(
        gqlRepository: GraphqlRepository): GraphqlUseCase<AuthorizeAccessResponse>(gqlRepository) {

    companion object {
        private const val ERROR_MESSAGE = "Failed getting access"
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(AuthorizeAccess.QUERY)
        setTypeClass(AuthorizeAccessResponse::class.java)
    }

    suspend fun execute(shopId: Int, resourceId: Int): Boolean {
        setRequestParams(AuthorizeAccess.createRequestParams(shopId, resourceId).parameters)
        val response = executeOnBackground()
        response.authorizeAccessData?.let { accessData ->
            if (accessData.error.isNullOrEmpty()) {
                return accessData.isAuthorized ?: false
            } else {
                throw MessageErrorException(accessData.error)
            }
        }
        throw MessageErrorException(ERROR_MESSAGE)
    }

}