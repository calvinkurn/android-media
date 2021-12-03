package com.tokopedia.tokopedianow.home.domain.query

internal object GetQuestWidgetList {
    val QUERY = """
        query quest_list(){
          questWidgetList(input: {channel: 0, channelSlug: "", page: "tokopedia-now"}) {
            questWidgetList {
              id
              title
              description
              widgetPrizeIconURL
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
                cta {
                  url
                  applink
                }
              }
            }
            resultStatus {
              code
              reason
            }
            widgetPageDetail {
              title
              cta {
                text
                url
                appLink
              }
            }
          }
        }
    """.trimIndent()
}