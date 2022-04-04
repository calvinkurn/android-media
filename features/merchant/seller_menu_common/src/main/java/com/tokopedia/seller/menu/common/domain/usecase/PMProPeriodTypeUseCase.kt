package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.menu.common.data.query.PMProPeriodTypeQuery
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.exception.UserShopInfoException
import javax.inject.Inject

class PMProPeriodTypeUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<UserShopInfoResponse>(graphqlRepository) {

    companion object {
        private const val ERROR_TYPE = "PMProPeriodType"
    }

    init {
        setGraphqlQuery(PMProPeriodTypeQuery)
        setTypeClass(UserShopInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): String {
        setRequestParams(PMProPeriodTypeQuery.createRequestParams(shopId))
        return try {
            executeOnBackground().goldGetPMSettingInfo.periodTypePmPro
        } catch (ex: Exception) {
            throw UserShopInfoException(ex, ERROR_TYPE)
        }
    }

}