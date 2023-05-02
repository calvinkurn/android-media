package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.model.SetPersonaResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 06/02/23.
 */

@GqlQuery("SetPersonaGqlMutation", SetPersonaUseCase.QUERY)
class SetPersonaUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SetPersonaResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(SetPersonaGqlMutation())
        setTypeClass(SetPersonaResponse::class.java)
    }

    suspend fun execute(
        shopId: String,
        persona: String,
        answers: List<QuestionnaireAnswerParam>
    ): String {
        try {
            val param = createParam(shopId, persona, answers).parameters
            setRequestParams(param)
            val result = executeOnBackground().setUserPersonaData
            if (result.isError) {
                throw MessageErrorException(result.errorMsg)
            }
            return result.persona
        } catch (e: Exception) {
            throw e
        }
    }

    private fun createParam(
        shopId: String,
        persona: String,
        answers: List<QuestionnaireAnswerParam>
    ): RequestParams {
        return RequestParams.create().apply {
            putLong(KEY_SHOP_ID, shopId.toLongOrZero())
            putString(KEY_PERSONA, persona)
            putObject(KEY_QUESTIONNAIRE, answers)
        }
    }

    companion object {

        const val QUERY = """
            mutation setUserPersona(${'$'}shopID: Int!, ${'$'}persona: String!, ${'$'}questionnaire: [Questionnaire!]!) {
              setUserPersona(shopID: ${'$'}shopID, persona: ${'$'}persona, questionnaire: ${'$'}questionnaire) {
                persona
                error
                errorMsg
              }
            }
        """
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PERSONA = "persona"
        private const val KEY_QUESTIONNAIRE = "questionnaire"
    }
}