package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val GET_RESO_LINK_QUERY = """
mutation getResolutionlink(${'$'}input :GetResolutionLinkInput!) {
  get_resolution_link(input:${'$'}input) {
    data{
      orderList{
        dynamicLink
        resoList{
          id
          resoTypeString
          statusString
          url
        }
      }
    }
    messageError
  }
}
"""
