package com.tokopedia.home_account.explicitprofile.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import javax.inject.Inject

class ExplicitProfileGetQuestionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, List<SectionsDataModel>>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query explicitprofileGetQuestion(${'$'}checkActive: Boolean, ${'$'}templateName: String){
              explicitprofileGetQuestion(checkActive: ${'$'}checkActive, templateName: ${'$'}templateName) {
                activeConfig{
                  value
                  detail{
                    configActive
                    templateActive
                  }
                }
                template{
                  id
                  property{
                   title
                   image
                  }
                  name
                  description
                  sections{
                    sectionID
                    layout
                    property{
                      title
                      infoHeader
                    }
                    maxAnswer
                    questions{
                      questionId
                      answerId
                      questionType
                      property{
                        name
                        title
                        subtitle
                        infoContent
                        image
                        options{
                          value
                          caption
                          message
                          applink
                          textApplink
                        }
                      }
                      answerValue
                    }
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: String): List<SectionsDataModel> {
        val parameter = mapOf(CHECK_ACTIVE to false, TEMPLATE_NAME to params)
        val response: ExplicitprofileGetQuestion = repository.request(graphqlQuery(), parameter)

        response.explicitProfileQuestionDataModel.template.sections.forEach { section ->
            section.questions.forEach { question ->
                if (question.answerValueList.isNotEmpty()) {
                    val answerMaps = mutableMapOf<String, String>()
                    question.answerValueList.forEach { key ->
                        answerMaps[key] = ""
                    }

                    question.property.options.forEach { options ->
                        val isSelected = options.value in answerMaps
                        options.isSelected = isSelected
                    }
                }
            }
        }

        return response.explicitProfileQuestionDataModel.template.sections
    }

    companion object {
        private const val CHECK_ACTIVE = "checkActive"
        private const val TEMPLATE_NAME = "templateName"
    }
}
