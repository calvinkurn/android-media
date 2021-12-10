package com.tokopedia.tokopedianow.home.domain.query

internal object GetQuestList {
    val QUERY = """
        query quest_list(){
          questList(input: {limit: 0, nextQuestID: 0, channel: 0, channelSlug: ""}) {
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
              actionButton {
                shortText
                cta {
                  applink
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