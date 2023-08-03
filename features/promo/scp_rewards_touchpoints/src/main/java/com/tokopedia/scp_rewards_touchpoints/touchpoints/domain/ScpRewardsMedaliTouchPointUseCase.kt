package com.tokopedia.scp_rewards_touchpoints.touchpoints.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpRewardsMedaliTouchPointModel
import javax.inject.Inject

@GqlQuery("scpRewardsMedaliTouchpointOrder", SCP_TOASTER_QUERY)
class ScpRewardsMedaliTouchPointUseCase @Inject constructor() : GraphqlUseCase<ScpRewardsMedaliTouchPointModel>() {

    companion object {
        private const val API_VERSION_KEY = "apiVersion"
        private const val ORDER_ID_KEY = "orderID"
        private const val PAGE_NAME_KEY = "pageName"
        private const val SOURCE_NAME_KEY = "sourceName"
        private const val SUCCESS_CODE = "200"
    }

    suspend fun getTouchPoint(
        orderId: Long,
        pageName: String,
        sourceName: String
    ): ScpRewardsMedaliTouchPointModel {
        setTypeClass(ScpRewardsMedaliTouchPointModel::class.java)
        setGraphqlQuery(ScpRewardsMedaliTouchpointOrder())
        setRequestParams(getRequestParams(orderId, pageName, sourceName))
        val result = executeOnBackground()
        if (result.scpRewardsMedaliTouchpointOrder.resultStatus.code == SUCCESS_CODE) {
            return result
        } else {
            throw MessageErrorException(result.scpRewardsMedaliTouchpointOrder.resultStatus.message.joinToString(". "))
        }
    }

    private fun getRequestParams(orderID: Long, pageName: String, sourceName: String) = mapOf(
        API_VERSION_KEY to "5.0.0",
        ORDER_ID_KEY to orderID,
        PAGE_NAME_KEY to pageName,
        SOURCE_NAME_KEY to sourceName,
    )
}

private const val SCP_TOASTER_QUERY = """
query scpRewardsMedaliTouchpointOrder(${'$'}apiVersion: String, ${'$'}pageName: String, ${'$'}sourceName: String, ${'$'}orderID: Int64) {
 scpRewardsMedaliTouchpointOrder(input: {apiVersion: ${'$'}apiVersion, pageName: ${'$'}pageName, sourceName: ${'$'}sourceName, orderID: ${'$'}orderID}) {
    resultStatus {
      code
      message
      status
    }
    isShown
    medaliTouchpointOrder {
      medaliID
      medaliSlug
      medaliIconImageURL
      medaliSunburstImageURL
      infoMessage {
        title
        subtitle
      }
      cta {
        text
        url
        appLink
        type
        icon
        isShown
      }
      retryChecking {
       isRetryable
       durationToRetry     
      }
    }
  }
}
"""
