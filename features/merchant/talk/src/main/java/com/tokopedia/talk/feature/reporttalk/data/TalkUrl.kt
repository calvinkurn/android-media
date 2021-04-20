package com.tokopedia.talk.feature.reporttalk.data

import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 9/3/18.
 */
class TalkUrl {
    companion object {

        var BASE_URL: String = TokopediaUrl.getInstance().INBOX
        const val PATH_REPORT_TALK: String = "talk/v2/report"

    }
}