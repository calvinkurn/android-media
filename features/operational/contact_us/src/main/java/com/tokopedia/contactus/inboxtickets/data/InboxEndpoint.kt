package com.tokopedia.contactus.inboxtickets.data

interface InboxEndpoint {
    companion object {
        const val LIST_TICKET = "/ws/contact-us/v2/inbox"
        const val DETAIL_TICKET = "/ws/contact-us/v2/inbox/"
        const val SEND_MESSAGE = "/ws/contact-us/v2/reply/step/1"
        const val COMMENT_RATING = "/ws/contact-us/v2/rating"
        const val SEND_MESSAGE_ATTACHMENT = "/ws/contact-us/v2/reply/step/2"
    }
}
