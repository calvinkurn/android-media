package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.menu.common.data.query.GoldGetPMOSStatusQuery
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.exception.UserShopInfoException
import javax.inject.Inject

class GoldGetPMOSStatusUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<UserShopInfoResponse>(graphqlRepository) {

    companion object {
        private const val ERROR_TYPE = "GoldGetPMOSStatus"
    }

    init {
        setGraphqlQuery(GoldGetPMOSStatusQuery)
        setTypeClass(UserShopInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): UserShopInfoResponse.GoldGetPMOSStatus.Data {
        setRequestParams(GoldGetPMOSStatusQuery.createRequestParams(shopId))
        return try {
            executeOnBackground().goldGetPMOSStatus.data
        } catch (ex: Exception) {
            throw UserShopInfoException(ex, ERROR_TYPE)
        }
    }

}