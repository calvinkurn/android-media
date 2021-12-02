package com.tokopedia.tokopedianow.home.domain.query

internal object GetQuestList {
    val QUERY = """
        query quest_list(){
          questList(input: {limit: 0, nextQuestID: 0, channel: 0, channelSlug: ""}) {
            questList {
              id
              title
              description
              isDisabledIcon
              progressInfoText
              expiredDate
              cardBannerBackgroundURL
              detailBannerBackgroundURL
              label {
                title
                description
                backgroundColor
                imageURL
                type
                textColor
              }
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
                text
                shortText
                backgroundColor
                isDisable
                cta {
                  url
                  applink
                }
              }
              prize {
                iconUrl
                shortText
                text
                textColor
              }
              category {
                id
                title
              }
            }
            hasNext
            nextQuestID
            resultStatus {
              code
              reason
            }
          }
        }
    """.trimIndent()
}