package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.seller.menu.common.data.query.DateShopCreatedQuery
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.exception.UserShopInfoException
import javax.inject.Inject

class DateShopCreatedUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<UserShopInfoResponse>(graphqlRepository) {

    companion object {
        private const val ERROR_TYPE = "DateShopCreated"
    }

    init {
        setGraphqlQuery(DateShopCreatedQuery)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        setTypeClass(UserShopInfoResponse::class.java)
    }

    suspend fun execute(): String {
        return try {
            executeOnBackground().userShopInfo.info.dateShopCreated
        } catch (ex: Exception) {
            throw UserShopInfoException(ex, ERROR_TYPE)
        }
    }

}