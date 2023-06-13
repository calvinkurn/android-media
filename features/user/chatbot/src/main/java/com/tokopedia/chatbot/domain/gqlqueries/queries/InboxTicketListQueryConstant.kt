package com.tokopedia.chatbot.domain.gqlqueries.queries


const val GQL_INBOX_LIST =
    """query TicketListQuery(${'$'}userID: String!, ${'$'}page: Int!, ${'$'}status: Int!, ${'$'}rating: Int) {
  ticket(userID: ${'$'}userID, page: ${'$'}page, status: ${'$'}status, rating: ${'$'}rating) {
    status
    data {
      is_success
      next_page
      previous_page
      tickets {
        id
        subject
        last_update
        read_status_id
        status_id
        url_detail
        message
        read_status
        status
        last_message
        need_rating
        is_official_store
        case_number
      }
      notice {
        is_active
        title
        subtitle
        content
        content_list
      }
      
    }
  }
}
"""
