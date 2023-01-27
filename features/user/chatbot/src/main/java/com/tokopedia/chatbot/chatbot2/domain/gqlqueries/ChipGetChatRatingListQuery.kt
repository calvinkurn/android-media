package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

const val CHIP_GET_CHAT_RATING_LIST_QUERY = """query chipGetChatRatingListV5(${'$'}input: GetChatRatingListV5Request!) {
  chipGetChatRatingListV5(input: ${'$'}input){
    status
    serverProcessTime
    data {
      isSuccess
    	list{
        caseChatID
        attachmentType
        value
        isSubmitted
      }  
    }
    messageError
  }
}"""
