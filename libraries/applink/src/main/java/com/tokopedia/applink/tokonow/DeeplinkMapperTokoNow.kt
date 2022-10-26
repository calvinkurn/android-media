package com.tokopedia.applink.tokonow

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow

object DeeplinkMapperTokopediaNow {

    private const val INDEX_CATEGORY_L1 = 4
    private const val INDEX_CATEGORY_L2 = 5
    private const val INDEX_RECIPE_ID = 2
    private const val INDEX_URL_PATH = 2

    private const val PARAM_CATEGORY_L1 = "category_l1"
    private const val PARAM_CATEGORY_L2 = "category_l2"
    private const val PARAM_RECIPE_SLUG = "slug"

    const val PARAM_RECIPE_ID = "recipe_id"

    fun getRegisteredNavigationFromHttp(uri: Uri): String {
        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "&" + uri.encodedQuery

        val uriSegments = uri.pathSegments
        val urlPath = uriSegments[INDEX_URL_PATH]

        val appLink = when {
            isRecipeSlug(urlPath) -> {
                val slug = uriSegments.last()
                UriUtil.buildUriAppendParam(
                    ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
                    mapOf(PARAM_RECIPE_SLUG to slug)
                )
            }
            else -> ApplinkConstInternalTokopediaNow.RECIPE_HOME
        }

        return "$appLink$queryString"
    }

    fun getRegisteredNavigationTokopediaNowSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.SEARCH + queryString
    }

    fun getRegisteredNavigationTokopediaNowCategory(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "&" + uri.encodedQuery

        val deeplinkWithoutQuery = if (queryString.isEmpty()) deeplink else deeplink.split("?")[0]

        val content = deeplinkWithoutQuery.split("/")

        val categoryL1 = "$PARAM_CATEGORY_L1=${content.getOrElse(INDEX_CATEGORY_L1) { "" }}"
        val categoryL2 = content.getOrElse(INDEX_CATEGORY_L2) { "" }.let {
            if (it.isNotEmpty()) "&$PARAM_CATEGORY_L2=$it" else ""
        }

        return "${ApplinkConstInternalTokopediaNow.CATEGORY}?$categoryL1$categoryL2$queryString"
    }

    fun getRegisteredNavigationTokopediaNowRecipeDetail(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "&" + uri.encodedQuery

        val recipeId = uri.pathSegments.getOrNull(INDEX_RECIPE_ID).orEmpty()
        val appLink = UriUtil.buildUriAppendParam(
            ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
            mapOf(PARAM_RECIPE_ID to recipeId)
        )

        return "$appLink$queryString"
    }

    fun getRegisteredNavigationTokopediaNowRecipeBookmark(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK + queryString
    }

    fun getRegisteredNavigationTokopediaNowRecipeHome(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.RECIPE_HOME + queryString
    }

    fun getRegisteredNavigationTokopediaNowRecipeSearch(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.RECIPE_SEARCH + queryString
    }

    fun getRegisteredNavigationTokopediaNowRecipeAutoComplete(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.RECIPE_AUTO_COMPLETE + queryString
    }

    private fun isRecipeSlug(string: String): Boolean {
        return string.matches(Regex("^([a-zA-Z0-9]*-[a-zA-Z0-9]*)+"))
    }
}