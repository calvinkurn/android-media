package com.tokopedia.applink.tokonow

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow

object DeeplinkMapperTokopediaNow {

    private const val INDEX_CATEGORY_L1 = 4
    private const val INDEX_CATEGORY_L2 = 5
    private const val PARAM_CATEGORY_L1 = "category_l1"
    private const val PARAM_CATEGORY_L2 = "category_l2"

    private const val INDEX_RECIPE_ID = 2

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
        val appLink = ApplinkConstInternalTokopediaNow.RECIPE_DETAIL

        return "${UriUtil.buildUri(appLink, recipeId)}$queryString"
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
}