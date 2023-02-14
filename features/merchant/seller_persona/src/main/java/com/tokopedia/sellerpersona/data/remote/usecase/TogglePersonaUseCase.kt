package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.model.TogglePersonaResponse
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 01/02/23.
 */

@GqlQuery("TogglePersonaGqlQuery", TogglePersonaUseCase.QUERY)
class TogglePersonaUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TogglePersonaResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TogglePersonaGqlQuery())
        setTypeClass(TogglePersonaResponse::class.java)
    }

    suspend fun execute(shopId: String, status: PersonaStatus): PersonaStatus {
        try {
            setRequestParams(createParam(shopId, status).parameters)
            val result = executeOnBackground().toggleUserPersona
            if (result.isError) {
                throw MessageErrorException(result.errorMsg)
            }
            return status
        } catch (e: Exception) {
            throw e
        }
    }

    private fun createParam(shopId: String, status: PersonaStatus): RequestParams {
        return RequestParams.create().apply {
            putLong(KEY_SHOP_ID, shopId.toLongOrNull() ?: Long.ZERO)
            putInt(KEY_STATUS, status.value)
        }
    }

    companion object {
        const val QUERY = """
            mutation toggleUserPersona(${'$'}shopID: Int!, ${'$'}status: Int!) {
              toggleUserPersona(shopID: ${'$'}shopID, status: ${'$'}status) {
                error
                errorMsg
              }
            }
        """
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_STATUS = "status"
    }
}