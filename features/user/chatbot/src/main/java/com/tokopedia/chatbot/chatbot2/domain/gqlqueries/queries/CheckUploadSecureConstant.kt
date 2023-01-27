package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val UPLOAD_SECURE_QUERY = """query topbotUploadSecureAvailability(${'$'}msgId: String!, ${'$'}deviceID: String!) {
  topbotUploadSecureAvailability(msgID: ${'$'}msgId, deviceID: ${'$'}deviceID) {
    UploadSecureAvailabilityData {
      IsUsingUploadSecure
    }
  }
}"""
