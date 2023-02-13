package com.tokopedia.content.common.comment

/**
 * @author by astidhiyaa on 08/02/23
 */
sealed class PageSource {
    abstract val id: String
    abstract val type: String
    abstract val reportType: String

    data class Play(val channelId: String) : PageSource() {
        override val id: String
            get() = channelId

        override val type: String
            get() = "PlayChannelID"

        override val reportType: String
            get() = "comment"
    }

    data class Feed(val postId: String) : PageSource() {
        override val id: String
            get() = postId

        override val type: String
            get() = "ActivityID"

        override val reportType: String
            get() = "post"
    }

    object Unknown : PageSource() {
        override val id: String
            get() = ""
        override val type: String
            get() = ""
        override val reportType: String
            get() = ""

    }
}
