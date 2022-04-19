package com.tokopedia.chatbot.domain.gqlqueries

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