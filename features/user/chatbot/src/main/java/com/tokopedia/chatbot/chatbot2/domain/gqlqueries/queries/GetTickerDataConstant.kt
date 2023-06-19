package com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries

const val GET_TICKER_DATA = """
{
  chipGetActiveTickerV4(isHTML: true) {
    status
    server_process_time
    data {
      is_success
      items {
        title
        text
      }
      type
    }
    message_error
  }
}
"""
