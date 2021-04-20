package com.tokopedia.activation.domain

import com.tokopedia.activation.domain.mapper.UpdateShopFeatureMapper
import com.tokopedia.activation.model.UpdateFeatureModel
import com.tokopedia.activation.model.response.UpdateShopFeatureResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateShopFeatureUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<UpdateShopFeatureResponse>, private val mapper: UpdateShopFeatureMapper) : UseCase <UpdateFeatureModel>() {

    override suspend fun executeOnBackground(): UpdateFeatureModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(
                PARAM_TYPE to useCaseRequestParams.getInt(PARAM_TYPE, 1),
                PARAM_VALUE to useCaseRequestParams.getBoolean(PARAM_VALUE, true)
        ))
        graphqlUseCase.setTypeClass(UpdateShopFeatureResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        return mapper.convertToUIModel(result.data)
    }

    fun generateRequestParams(value: Boolean): RequestParams {
        return RequestParams.create().apply {
            putInt(PARAM_TYPE, 1)
            putBoolean(PARAM_VALUE, value)
        }
    }

    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_VALUE = "value"

        val QUERY = """
            mutation updateShopFeature(${'$'}type: Int!, ${'$'}value: Boolean!)
            {
              updateShopFeature(type: ${'$'}type, value: ${'$'}value) {
                success
                message
                createdId
              }
            }
        """.trimIndent()
    }
}
