package com.tokopedia.usercomponents.explicit.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usercomponents.explicit.domain.model.ExplicitDataModel
import javax.inject.Inject

class ExplicitUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, ExplicitDataModel>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            query explicitprofileGetQuestion(${'$'}templateName: String) {
              explicitprofileGetQuestion(templateName: ${'$'}templateName) {
                activeConfig {
                  value
                  detail {
                    configActive
                    templateActive
                  }
                }
                template {
                  id
                  name
                  description
                  property {
                    title
                    image
                  }
                  sections {
                    sectionID
                    layout
                    property {
                      title
                      infoHeader
                    }
                    questions {
                      questionId
                      answerId
                      questionType
                      answerValue
                      property {
                        name
                        title
                        subtitle
                        infoContent
                        image
                        options {
                          value
                          caption
                          message
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: String): ExplicitDataModel {
        val parameters = mapOf(
            "checkActive" to false,
            "templateName" to params
        )
        return repository.request(graphqlQuery(), parameters)
    }
}