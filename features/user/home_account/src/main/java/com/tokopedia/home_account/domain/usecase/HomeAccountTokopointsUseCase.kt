package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.TokopointsDataModel
import javax.inject.Inject

/**
 * Created by Ade Fulki on 21/02/21.
 */
class HomeAccountTokopointsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, TokopointsDataModel>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: Unit): TokopointsDataModel {
        return request(graphqlRepository, params)
    }

    companion object {
        private val query = """
            query {
              tokopointsStatusFiltered(filterKeys: ["points"], pointsExternalCurrency: "IDR", source: "account-page") {
                resultStatus {
                  code
                  status
                  message
                }
                statusFilteredData {
                  points {
                    iconImageURL
                    pointsAmount
                    pointsAmountStr
                    externalCurrencyAmount
                    externalCurrencyAmountStr
                    pointsSection {
                      redirectURL
                      redirectAppLink
                      sectionContent {
                        type
                        textAttributes {
                          text
                          color
                          isBold
                        }
                        tagAttributes {
                          text
                          backgroundColor
                        }
                      }
                    }
                  }
                }
              }
            }

        """.trimIndent()
    }
}