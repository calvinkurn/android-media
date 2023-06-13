package com.tokopedia.content.common.navigation.people

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.navigation.util.buildAppLink

/**
 * Created By : Jonathan Darwin on February 15, 2023
 */
object UserProfileNavigation {

    fun generateAppLink(
        userId: String,
        param: UserProfileParam.() -> Unit = {}
    ): String {
        val baseAppLink = UriUtil.buildUri(ApplinkConst.PROFILE, userId)
        val queries = UserProfileParam().apply(param).build()

        return buildAppLink(baseAppLink, queries)
    }
}
