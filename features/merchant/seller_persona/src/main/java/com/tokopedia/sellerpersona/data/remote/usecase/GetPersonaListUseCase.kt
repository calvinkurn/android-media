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

@GqlQuery("FetchPersonaListQuery", GetPersonaListUseCase.QUERY)
class GetPersonaListUseCase @Inject constructor(
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
            if (response.data.error) {
                throw MessageErrorException(response.data.errorMsg)
            }
            return mapper.mapToUiModel(response.data.personaList)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    companion object {
        const val QUERY = """
            {
              fetchPersonaListData {
                data {
                  value
                  header {
                    title
                    subtitle
                    image
                    backgroundImage
                  }
                  body {
                    title
                    itemList
                  }
                }
                error
                errorMsg
              }
            }
        """
    }
}