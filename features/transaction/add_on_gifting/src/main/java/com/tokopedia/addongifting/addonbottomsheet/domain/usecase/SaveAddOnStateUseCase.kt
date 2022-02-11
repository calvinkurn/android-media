package com.tokopedia.addongifting.addonbottomsheet.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate.SaveAddOnStateRequest
import com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate.SaveAddOnStateResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(SaveAddOnStateUseCase.QUERY_NAME, SaveAddOnStateUseCase.QUERY)
class SaveAddOnStateUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<SaveAddOnStateResponse>() {

    var mockResponse: String = ""

    private var params: Map<String, Any>? = null

    fun setParams(saveAddOnStateRequest: SaveAddOnStateRequest) {
        params = mapOf("params" to saveAddOnStateRequest)
    }

    override suspend fun executeOnBackground(): SaveAddOnStateResponse {
        // Todo : remove mock data before merge to release
        if (mockResponse.isNotBlank()) {
            return Gson().fromJson(mockResponse, SaveAddOnStateResponse::class.java)
        }

        if (params.isNullOrEmpty()) {
            throw RuntimeException("Parameter can't be null or empty!")
        }

        val request = GraphqlRequest(SaveAddOnStateQuery(), SaveAddOnStateResponse::class.java, params)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "SaveAddOnStateQuery"
        const val QUERY = """
            
        """
    }
}