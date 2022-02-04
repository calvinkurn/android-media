package com.tokopedia.addongifting.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addongifting.data.response.GetAddOnSavedStateResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(GetAddOnSavedStateUseCase.QUERY_NAME, GetAddOnSavedStateUseCase.QUERY)
class GetAddOnSavedStateUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetAddOnSavedStateResponse>() {

    override suspend fun executeOnBackground(): GetAddOnSavedStateResponse {
        val request = GraphqlRequest(GetAddOnByProductQuery(), GetAddOnSavedStateResponse::class.java)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "GetAddOnSavedStateQuery"
        const val QUERY = """
            
        """
    }
}