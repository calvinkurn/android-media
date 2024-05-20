package com.tokopedia.applink.navigation

import android.net.Uri
import com.tokopedia.applink.UriUtil.buildUriAppendParams
import com.tokopedia.applink.internal.ApplinkConsInternalHome

object DeeplinkMapperMainNavigation {

    private const val PATH_ME_PAGE = "me-page"

    const val EXTRA_TAB_TYPE = "tab_type"
    const val EXTRA_DISCO_ID = "disco_id"

    const val TAB_TYPE_HOME = "home"
    const val TAB_TYPE_FEED = "feed"
    const val TAB_TYPE_ME_PAGE = "me_page"

    fun getNavFromHttp(uri: Uri): String {
        val lastPathSegment = uri.lastPathSegment
        return if (lastPathSegment == PATH_ME_PAGE) {
            getMePageAppLink(uri)
        } else {
            ""
        }
    }

    fun getRegisteredNavigation(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val host = uri.host
        return if (host == PATH_ME_PAGE) {
            getMePageAppLink(uri)
        } else {
            ""
        }
    }

    private fun getMePageAppLink(uri: Uri): String {
        return buildHomeNavAppLink(
            TAB_TYPE_ME_PAGE,
            uri.queryParameterNames.associate {
                val parameters = uri.getQueryParameters(it)
                it to when (parameters.size) {
                    0 -> ""
                    1 -> parameters.first()
                    else -> parameters
                }
            }
        )
    }

    private fun buildHomeNavAppLink(type: String, queryParamsMap: Map<String, Any>): String {
        return buildUriAppendParams(
            ApplinkConsInternalHome.HOME_NAVIGATION,
            buildMap {
                put(EXTRA_TAB_TYPE, type)
                putAll(queryParamsMap)
            }
        )
    }
}

class DeeplinkNavigationUtil {

    /**
     * Determines whether the new home nav is enabled,
     * which will be used to route the applink to the new home nav.
     * This is mainly used for hansel purpose.
     *
     * @return whether new home nav is enabled or not.
     */
    fun newHomeNavEnabled(): Boolean {
        return true
    }
}
