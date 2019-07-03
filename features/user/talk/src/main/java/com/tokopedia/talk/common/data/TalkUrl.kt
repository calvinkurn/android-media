package com.tokopedia.talk.common.data

import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 9/3/18.
 */
class TalkUrl {
    companion object {

        var BASE_URL: String = com.tokopedia.url.TokopediaUrl.getInstance().INBOX
        const val PATH_GET_INBOX_TALK: String = "talk/v2/inbox"
        const val PATH_GET_PRODUCT_TALK: String = "talk/v2/read"
        const val PATH_DELETE_TALK: String = "talk/v2/delete"
        const val PATH_DELETE_COMMENT_TALK: String = "/talk/v2/comment/delete"
        const val PATH_FOLLOW_TALK: String = "talk/v2/follow"
        const val PATH_REPORT_TALK: String = "talk/v2/report"
        const val PATH_CREATE_TALK: String = "talk/v2/create"
        const val PATH_MARK_TALK_NOT_FRAUD: String = "talk/v2/mark/notfraud"
        const val PATH_GET_SHOP_TALK: String = "talk/v2/read"

    }
}