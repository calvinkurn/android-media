package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val SUBMIT_CSAT_RATING = """
mutation submitRatingCSAT(${'$'}input: RatingCSATRequest!){
  submitRatingCSAT(input: ${'$'}input){
    header{
      is_success
      reason
      messages
      error_code
    }
    data{
      message
    }
  }
}
"""
