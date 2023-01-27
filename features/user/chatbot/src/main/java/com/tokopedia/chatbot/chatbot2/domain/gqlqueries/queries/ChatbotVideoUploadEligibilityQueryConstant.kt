package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val GQL_UPLOAD_VIDEO_ELIGIBILITY =
    """
       query topbotUploadVideoEligibility(${'$'}msgID: String!){
 topbotUploadVideoEligibility(msgID: ${'$'}msgID) {
    header {
        is_success
        reason
        messages
        error_code
    }
    data {
        is_eligible
    }
  }
} 
    """
