package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.menu.common.data.query.GoldGetPMShopInfoQuery
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.exception.UserShopInfoException
import javax.inject.Inject

class GoldGetPMShopInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<UserShopInfoResponse>(graphqlRepository) {

    companion object {
        private const val ERROR_TYPE = "GoldGetPMShopInfo"
    }

    init {
        setGraphqlQuery(GoldGetPMShopInfoQuery)
        setTypeClass(UserShopInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): UserShopInfoResponse.GoldGetPMShopInfo {
        setRequestParams(GoldGetPMShopInfoQuery.createRequestParams(shopId))
        return try {
            executeOnBackground().goldGetPMShopInfo
        } catch (ex: Exception) {
            throw UserShopInfoException(ex, ERROR_TYPE)
        }
    }

}