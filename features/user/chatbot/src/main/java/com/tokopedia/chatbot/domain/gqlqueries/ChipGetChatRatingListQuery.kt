package com.tokopedia.chatbot.domain.gqlqueries

const val CHIP_GET_CHAT_RATING_LIST_QUERY = """query chipGetChatRatingList(${'$'}input: GetChatRatingListRequest!) {
  chipGetChatRatingList(input: ${'$'}input){
    status
    serverProcessTime
    data {
      isSuccess
    	list{
        caseChatID
        value
        isSubmitted
      }  
    }
    messageError
  }
}"""