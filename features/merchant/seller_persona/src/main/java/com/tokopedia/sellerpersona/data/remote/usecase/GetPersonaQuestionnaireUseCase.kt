package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.mapper.QuestionnaireMapper
import com.tokopedia.sellerpersona.data.remote.model.FetchPersonaQuestionnaireResponse
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

@GqlQuery("FetchPersonaQuestionnaireGqlQuery", GetPersonaQuestionnaireUseCase.QUERY)
class GetPersonaQuestionnaireUseCase @Inject constructor(
    private val mapper: QuestionnaireMapper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FetchPersonaQuestionnaireResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FetchPersonaQuestionnaireGqlQuery())
        setTypeClass(FetchPersonaQuestionnaireResponse::class.java)
    }

    suspend fun execute(): List<QuestionnairePagerUiModel> {
        try {
            val response = super.executeOnBackground()
            if (response.data?.error.orFalse()) {
                throw MessageErrorException(response.data?.errorMsg.orEmpty())
            }
            return mapper.mapToUiModel(response.data?.questionnaire)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    companion object {
        const val QUERY = """
           query fetchUserPersonaQuestionnaire {
              fetchUserPersonaQuestionnaire {
                error
                errorMsg
                questionnaire {
                  id
                  question {
                    title
                    subtitle
                  }
                  type
                  options {
                    value
                    title
                  }
                  answersToSkipQuestions {
                    answers
                    nextQuestionId
                  }
                }
              }
           }
        """
    }
}