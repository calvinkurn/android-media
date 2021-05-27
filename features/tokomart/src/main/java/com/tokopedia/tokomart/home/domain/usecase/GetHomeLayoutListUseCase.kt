package com.tokopedia.tokomart.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.domain.query.GetHomeLayoutList
import javax.inject.Inject

class GetHomeLayoutListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetHomeLayoutList.QUERY)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(): List<HomeLayoutResponse> {
        return executeOnBackground().response.data
    }
}