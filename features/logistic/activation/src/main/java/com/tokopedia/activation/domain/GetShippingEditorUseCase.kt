package com.tokopedia.activation.domain

import com.tokopedia.activation.domain.mapper.ShippingEditorMapper
import com.tokopedia.activation.model.ShippingEditorModel
import com.tokopedia.activation.model.response.ShippingEditorResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShippingEditorUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<ShippingEditorResponse>, private val mapper: ShippingEditorMapper) : UseCase<ShippingEditorModel>() {

    override suspend fun executeOnBackground(): ShippingEditorModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_USER_ID to useCaseRequestParams.getInt(PARAM_USER_ID, 0 )))
        graphqlUseCase.setTypeClass(ShippingEditorResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        return mapper.convertToUIModel(result.keroGetShippingEditor.data.activatedShipping)
    }

    fun generateRequestParams(userId: Int): RequestParams {
        return RequestParams.create().apply {
            putInt(PARAM_USER_ID, userId)
        }
    }

    companion object {

        const val PARAM_USER_ID = "userid"

        val QUERY = """
            query kero_get_shipping_editor(${"$"}userid: Int!)
            {
              kero_get_shipping_editor(userid: ${"$"}userid) {
                status
                config
                server_process_time
                data {
                    activated_shipping
                    
                } 
              }
            }
        """.trimIndent()
    }
}