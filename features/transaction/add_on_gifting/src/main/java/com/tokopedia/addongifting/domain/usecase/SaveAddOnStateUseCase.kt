package com.tokopedia.addongifting.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.purchase_platform.common.feature.addongifting.data.SaveAddOnStateResult
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(SaveAddOnStateUseCase.QUERY_NAME, SaveAddOnStateUseCase.QUERY)
class SaveAddOnStateUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<SaveAddOnStateResult>() {

    var mockResponse: String = ""

    override suspend fun executeOnBackground(): SaveAddOnStateResult {
        if (mockResponse.isNotBlank()) {
            return Gson().fromJson(mockResponse, SaveAddOnStateResult::class.java)
        }

        val request = GraphqlRequest(SaveAddOnStateQuery(), SaveAddOnStateResult::class.java)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "SaveAddOnStateQuery"
        const val QUERY = """
            
        """
    }
}