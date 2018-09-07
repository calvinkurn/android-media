package com.tokopedia.talk.common.data

/**
 * @author by nisie on 9/3/18.
 */
class TalkUrl {
    companion object {

        var BASE_URL: String = "https://inbox.tokopedia.com/"
        const val PATH_GET_INBOX_TALK: String = "talk/v2/inbox"
        const val PATH_GET_PRODUCT_TALK: String = "talk/v2/read"
        val IMAGE_EMPTY_TALK: String? = "https://ecs7.tokopedia.net/img/android/others/page_1.png"
        //TODO CHANGE EMPTY TALK
    }
}