package com.tokopedia.chatbot.domain.gqlqueries.queries

const val GQL_LEAVE_QUEUE = """
mutation postLeaveQueue(${'$'}msgID: String!, ${'$'}timestamp: String!) {
  postLeaveQueue(msgID: ${'$'}msgID, timestamp: ${'$'}timestamp) {
    LeaveQueueHeader {
      ProcessTime
      TotalData
      Reason
      ErrorCode
    }
    LeaveQueueData {
      Message
    }
  }
}

"""