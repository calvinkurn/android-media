package com.tokopedia.topchat.chatlist.data

object ChatListQueries {

    private const val MSG_ID = "\$msgIDs"

    val MUTATION_CHAT_MARK_READ = """
                mutation chat_mark_read($MSG_ID: [String!]!) {
           chatMarkRead(msgIDs: $MSG_ID){
              list {
                 msgID
                 isSuccess
                 detailResponse
              }
           }
        }
    """.trimIndent()

    val MUTATION_CHAT_MARK_UNREAD = """
        mutation chat_mark_unread($MSG_ID: [String!]!) {
           chatMarkUnread(msgIDs: $MSG_ID){
              list {
                 msgID
                 isSuccess
                 detailResponse
              }
           }
        }
    """.trimIndent()
}
