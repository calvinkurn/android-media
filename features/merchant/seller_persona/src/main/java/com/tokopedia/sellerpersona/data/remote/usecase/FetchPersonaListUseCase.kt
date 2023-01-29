package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.mapper.PersonaListMapper
import com.tokopedia.sellerpersona.data.remote.model.FetchPersonaListResponse
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

@GqlQuery("FetchPersonaListQuery", FetchPersonaListUseCase.QUERY)
class FetchPersonaListUseCase @Inject constructor(
    private val mapper: PersonaListMapper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FetchPersonaListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FetchPersonaListQuery())
        setTypeClass(FetchPersonaListResponse::class.java)
    }

    suspend fun execute(): List<PersonaUiModel> {
        try {
            val response = executeOnBackground()
            if (response.error) {
                throw MessageErrorException(response.errorMsg)
            }
            return mapper.mapToUiModel(response.data)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    companion object {
        const val QUERY = """
            
        """
    }
}