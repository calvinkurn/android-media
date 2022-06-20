package com.tokopedia.chatbot.domain.gqlqueries.queries

const val CHIP_SUBMIT_CHAT_CSAT = """mutation chipSubmitChatCSAT(${'$'}input: SubmitChatCSATRequest!) {
  chipSubmitChatCSAT(input: ${'$'}input) {
	status
    serverProcessTime
    data {
      isSuccess
      toasterMessage
    }
    messageError
  }
}"""