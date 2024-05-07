package com.tokopedia.home_account.explicitprofile.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetQuestionsUseCase.QuestionParams, ExplicitprofileGetQuestion>(dispatchers.io) {

    override suspend fun execute(params: QuestionParams): ExplicitprofileGetQuestion {
        val response : ExplicitprofileGetQuestion = repository.request(graphqlQuery(), params.toMapParam())

        response.explicitProfileQuestionDataModel.template.sections.forEach {
            it.questions.forEach { question ->
                val listAnswer = question.answerValueList
                question.property.options.forEach { option ->
                    option.isSelected = listAnswer.contains(option.value)
                }
            }
        }

        return response
    }

    override fun graphqlQuery(): String {
        return """
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
                  rules {
                    minAnswer
                    maxAnswer
                  }
                  property {
                    title
                    image
                  }
                  sections {
                    sectionID
                    maxAnswer
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
                      answerValueList
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
    }

    data class QuestionParams(
        @SerializedName("checkActive")
        var checkActive: Boolean = false,
        @SerializedName("templateName")
        var templateName: String = ""
    ) : GqlParam
}
