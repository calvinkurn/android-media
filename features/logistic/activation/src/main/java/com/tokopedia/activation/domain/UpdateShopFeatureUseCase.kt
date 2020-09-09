package com.tokopedia.activation.domain

import com.tokopedia.activation.model.response.UpdateShopFeatureResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class UpdateShopFeatureUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<UpdateShopFeatureResponse>) {

    fun execute(value: Boolean, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_TYPE to 1, PARAM_VALUE to value))
        graphqlUseCase.setTypeClass(UpdateShopFeatureResponse::class.java)
        graphqlUseCase.execute({response : UpdateShopFeatureResponse ->
            onSuccess("success")
        }, {
            throwable: Throwable -> onError(throwable)
        })
    }

    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_VALUE = "value"

        val QUERY = """
            mutation{
              updateShopFeature(type: ${'$'}type, value:${'$'}value) {
                success
                message
                createdId
              }
            }
        """.trimIndent()
    }
}
