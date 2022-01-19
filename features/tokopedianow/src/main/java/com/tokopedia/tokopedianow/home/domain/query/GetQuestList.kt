package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.tokopedianow.home.domain.usecase.GetQuestWidgetListUseCase.Companion.CHANNEL_SLUG

internal object GetQuestList {
    val QUERY = """
        query quest_list(${'$'}${CHANNEL_SLUG}: String){
          questList(input: {limit: 0, nextQuestID: 0, channel: 0, channelSlug: ${'$'}${CHANNEL_SLUG}}) {
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