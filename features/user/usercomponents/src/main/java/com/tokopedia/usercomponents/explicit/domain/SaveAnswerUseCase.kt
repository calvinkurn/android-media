package com.tokopedia.usercomponents.explicit.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usercomponents.explicit.domain.model.AnswerDataModel
import com.tokopedia.usercomponents.explicit.domain.model.InputParam
import javax.inject.Inject

class SaveAnswerUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<InputParam, AnswerDataModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation explicitprofileSaveMultiAnswers(${'$'}input: SaveMultiAnswersRequest!) {
              explicitprofileSaveMultiAnswers(input: ${'$'}input) {
                message
              }
            }
        """.trimIndent()

    override suspend fun execute(params: InputParam): AnswerDataModel {
        val parameters = mapOf(
            PARAM_INPUT to params
        )

        return repository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}