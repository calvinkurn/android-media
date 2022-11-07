package com.tokopedia.content.common.navigation.shorts

import com.tokopedia.createpost.common.TYPE_CONTENT_SHOP
import com.tokopedia.createpost.common.TYPE_CONTENT_USER

/**
 * Created By : Jonathan Darwin on November 07, 2022
 */
class PlayShortsParam {

    private var authorType = ""

    fun setAuthorType(authorType: AuthorType) {
        this.authorType = authorType.value
    }

    enum class AuthorType(val value: String) {
        Shop(TYPE_CONTENT_SHOP),
        User(TYPE_CONTENT_USER)
    }
}
