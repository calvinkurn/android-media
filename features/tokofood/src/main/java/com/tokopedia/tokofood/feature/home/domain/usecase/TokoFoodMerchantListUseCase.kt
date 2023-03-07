package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.domain.param.TokoFoodMerchantListParamMapper
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodMerchantListQuery
import javax.inject.Inject

class TokoFoodMerchantListUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<TokoFoodMerchantListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TokoFoodMerchantListQuery)
        setTypeClass(TokoFoodMerchantListResponse::class.java)
    }

    suspend fun execute(localCacheModel: LocalCacheModel?, option: Int = Int.ZERO, brandUId: String = String.EMPTY,
                        sortBy: Int = Int.ZERO, orderById: Int = Int.ZERO, cuisine: String = String.EMPTY, pageKey: String = String.EMPTY): TokoFoodMerchantListResponse {
        setRequestParams(
            TokoFoodMerchantListParamMapper.createRequestParams(localCacheModel, option,
            brandUId, sortBy, orderById, cuisine, pageKey))
        return executeOnBackground()
    }
}