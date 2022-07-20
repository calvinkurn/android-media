package com.tokopedia.quest_widget.constants

object GQLQueryQuestWidget {
    const val IO = "IO"
    const val QUERY_QUEST_WIDGET = """
    
 query questWidgetList(${'$'}channel: Int, ${'$'}channelSlug: String, ${'$'}page: String) { 
 questWidgetList: questWidgetList(input: {channel: ${'$'}channel, channelSlug: ${'$'}channelSlug, page: ${'$'}page}) {
   questWidgetList {
      id
      title
      description
      isDisabledIcon
      progressInfoText
      expiredDate
      cardBannerBackgroundURL
      detailBannerBackgroundURL
      widgetPrizeIconURL
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
    resultStatus {
      code
      reason
    }
    isEligible
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
  
  """

}