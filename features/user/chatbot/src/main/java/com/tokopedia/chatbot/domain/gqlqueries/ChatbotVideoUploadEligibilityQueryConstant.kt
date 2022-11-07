package com.tokopedia.chatbot.domain.gqlqueries

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