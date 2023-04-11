package com.tokopedia.home_account.explicitprofile.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<GetQuestionsUseCase.QuestionParams, ExplicitprofileGetQuestion>(Dispatchers.IO) {

    override suspend fun execute(params: QuestionParams): ExplicitprofileGetQuestion {
        return repository.request(graphqlQuery(), params.toMapParam())
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
        var templateName: String = "",
    ) : GqlParam
}
