package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetQuestList: GqlQueryInterface {

    const val CHANNEL_SLUG = "channelSlug"

    private const val OPERATION_NAME = "questList"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${CHANNEL_SLUG}: String){
          $OPERATION_NAME(input: {limit: 0, nextQuestID: 0, channel: 0, channelSlug: ${'$'}${CHANNEL_SLUG}}) {
            questList {
              id
              title
              description
              config
              questUser {
                id
                status
              }
              task {
                id
                title
                progress {
                  current
                  target
                }
              }
            }
            resultStatus {
              code
              reason
            }
          }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}