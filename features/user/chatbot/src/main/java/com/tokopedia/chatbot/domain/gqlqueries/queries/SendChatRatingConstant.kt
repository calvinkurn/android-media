package com.tokopedia.chatbot.domain.gqlqueries.queries

const val SEND_CHAT_RATING_QUERY =
    """
    mutation post_rating(${'$'} msgId: String!, ${'$'}rating: Int!, ${'$'}timestamp: String!) {
    postRatingV2(msgID: ${'$'}msgId, rating: ${'$'}rating, timestamp: ${'$'}timestamp) {
        Data {
            Message
            IsSuccess
            Reason
        }
    }
}
"""
