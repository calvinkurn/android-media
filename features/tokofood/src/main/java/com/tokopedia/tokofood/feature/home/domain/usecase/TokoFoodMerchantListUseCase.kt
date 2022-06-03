package com.tokopedia.tokofood.feature.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.query.TokoFoodMerchantListQuery
import javax.inject.Inject

class TokoFoodMerchantListUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<TokoFoodMerchantListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TokoFoodMerchantListQuery)
        setTypeClass(TokoFoodMerchantListResponse::class.java)
    }

    suspend fun execute(localCacheModel: LocalCacheModel?, option: Int = 0, brandId: String = "",
                        sortBy: Int = 0, orderById: Int = 0, cuisine: String = "", pageKey: String = ""): TokoFoodMerchantListResponse {
        setRequestParams(TokoFoodMerchantListQuery.createRequestParams(localCacheModel, option,
            brandId, sortBy, orderById, cuisine, pageKey))
        return executeOnBackground()
    }
}