package com.tokopedia.usercomponents.explicit.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usercomponents.explicit.domain.model.QuestionDataModel
import javax.inject.Inject

class GetQuestionUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, QuestionDataModel>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            query explicitprofileGetQuestion(${'$'}checkActive: Boolean, ${'$'}templateName: String) {
              explicitprofileGetQuestion(checkActive: ${'$'}checkActive, templateName: ${'$'}templateName) {
                activeConfig {
                    value
                }
                template {
                  id
                  name
                  sections {
                    sectionID
                    questions {
                      questionId
                      answerId
                      answerValue
                      property {
                        name
                        title
                        subtitle
                        image
                        options {
                          value
                          caption
                          message
                          applink
                          textApplink
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: String): QuestionDataModel {
        val parameters = mapOf(
            PARAM_CHECK_ACTIVE to true,
            PARAM_TEMPLATE_NAME to params
        )
        return repository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val PARAM_CHECK_ACTIVE = "checkActive"
        private const val PARAM_TEMPLATE_NAME = "templateName"
    }
}
