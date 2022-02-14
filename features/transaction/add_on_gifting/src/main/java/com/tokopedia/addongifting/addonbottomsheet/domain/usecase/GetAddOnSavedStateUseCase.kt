package com.tokopedia.addongifting.addonbottomsheet.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.RuntimeException
import javax.inject.Inject

@GqlQuery(GetAddOnSavedStateUseCase.QUERY_NAME, GetAddOnSavedStateUseCase.QUERY)
class GetAddOnSavedStateUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetAddOnSavedStateResponse>() {

    var mockResponse: String = ""

    private var params: Map<String, Any>? = null

    fun setParams(getAddOnSavedStateRequest: GetAddOnSavedStateRequest) {
        params = mapOf("params" to getAddOnSavedStateRequest)
    }

    override suspend fun executeOnBackground(): GetAddOnSavedStateResponse {
        // Todo : remove mock data before merge to release
        if (mockResponse.isNotBlank()) {
            return Gson().fromJson(mockResponse, GetAddOnSavedStateResponse::class.java)
        }

        if (params.isNullOrEmpty()) {
            throw RuntimeException("Parameter can't be null or empty!")
        }

        val request = GraphqlRequest(GetAddOnByProductQuery(), GetAddOnSavedStateResponse::class.java, params)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "GetAddOnSavedStateQuery"
        const val QUERY = """
            query get_add_ons {
                get_add_ons {
                    error_message
                    status
                    data {
                        add_ons {
                            add_on_key
                            add_on_level
                            add_on_data {
                                add_on_id
                                add_on_qty
                                add_on_price
                                add_on_metadata {
                                    add_on_note{
                                        is_custom_note
                                        to
                                        from
                                        notes
                                    }
                                }
                            }
                        }
                    }
                }
            }
        """
    }
}