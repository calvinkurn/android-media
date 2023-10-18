package com.tokopedia.content.common.navigation.shorts

import com.tokopedia.content.common.navigation.util.appendQuery
import com.tokopedia.content.common.types.ContentCommonUserType

/**
 * Created By : Jonathan Darwin on November 07, 2022
 */
class PlayShortsParam {

    private var authorType = ContentCommonUserType.TYPE_UNKNOWN

    fun setAuthorType(authorType: AuthorType) {
        this.authorType = authorType.value
    }

    fun buildParam(): String {
        return buildString {
            if(authorType != ContentCommonUserType.TYPE_UNKNOWN) {
                appendQuery(ContentCommonUserType.KEY_AUTHOR_TYPE, authorType)
            }
        }
    }

    enum class AuthorType(val value: String) {
        Shop(ContentCommonUserType.TYPE_SHOP),
        User(ContentCommonUserType.TYPE_USER)
    }
}
