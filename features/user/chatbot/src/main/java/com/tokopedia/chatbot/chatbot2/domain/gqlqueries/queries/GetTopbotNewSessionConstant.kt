package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val GQL_GET_TOP_BOT_NEW_SESSION =
"""query topbotGetNewSession(${'$'}msgId :String!, ${'$'}userId :String!){
    topbotGetNewSession(msgID: ${'$'}msgId, userID: ${'$'}userId) {
        isNewSession
        isTypingBlocked
  }
}"""
