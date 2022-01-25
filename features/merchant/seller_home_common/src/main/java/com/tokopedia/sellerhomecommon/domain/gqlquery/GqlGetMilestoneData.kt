package com.tokopedia.sellerhomecommon.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 10/01/22.
 */

object GqlGetMilestoneData : GqlQueryInterface {

    private const val OPERATION_NAME = "fetchMilestoneWidgetData"
    private val GQL_QUERY = """
            query $OPERATION_NAME(${'$'}dataKeys: [dataKey!]!) {
              $OPERATION_NAME(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  title
                  subtitle
                  backgroundColor
                  backgroundImageUrl
                  showNumber
                  timeDeadline
                  progressBar {
                    description
                    percentage
                    percentageFormatted
                    taskCompleted
                    totalTask
                  }
                  mission {
                    imageUrl
                    title
                    subtitle
                    missionCompletionStatus
                    progress {
                      appDescription
                      percentage
                      completed
                      target
                    }
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  finishMission {
                    imageUrl
                    title
                    subtitle
                    button {
                      title
                      urlType
                      url
                      applink
                      buttonStatus
                    }
                  }
                  cta {
                    text
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}