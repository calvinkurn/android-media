package com.tokopedia.applink.tokonow

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow

object DeeplinkMapperTokopediaNow {
    private const val INDEX_RECIPE_ID = 2
    private const val INDEX_URL_PATH = 2

    private const val PARAM_CATEGORY_L1 = "category_l1"
    private const val PARAM_CATEGORY_L2 = "category_l2"
    private const val PARAM_RECIPE_SLUG = "slug"
    private const val PARAM_L1 = "l1"

    private const val RECIPE_SEGMENT_COUNT = 3
    private const val RECIPE_PATH_INDEX = 1
    private const val PATH_RECIPE = "recipe"

    const val PARAM_RECIPE_ID = "recipe_id"

    fun getRegisteredNavigationFromHttp(uri: Uri): String {
        val uriSegments = uri.pathSegments
        val urlPath = uriSegments.getOrNull(INDEX_URL_PATH).orEmpty()

        val queryString = if(isRecipeSlug(urlPath)) {
            getQueryString(uri, "&")
        } else {
            getQueryString(uri, "?")
        }

        val appLink = when {
            isRecipeSlug(urlPath) -> {
                val slug = uriSegments.last()
                UriUtil.buildUriAppendParam(
                    ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
                    mapOf(PARAM_RECIPE_SLUG to slug)
                )
            }
            isRecipeUrl(uri) -> {
                ApplinkConstInternalTokopediaNow.RECIPE_HOME
            }
            else -> {
                ApplinkConstInternalTokopediaNow.HOME
            }
        }

        return "$appLink$queryString"
    }

    fun getRegisteredNavigationTokopediaNowHome(deeplink: String): String {
        val uri = Uri.parse(deeplink)

        val query = uri.encodedQuery
        val queryString = if (query.isNullOrEmpty()) "" else "?" + uri.encodedQuery

        return ApplinkConstInternalTokopediaNow.HOME + queryString
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

        val indexCategoryId = 4
        return if (content.getOrElse(indexCategoryId) { "" } == PARAM_L1) {
            val categoryL1 = "$PARAM_CATEGORY_L1=${content.getOrElse(indexCategoryId+1) { "" }}"
            "${ApplinkConstInternalTokopediaNow.CATEGORY}?$categoryL1$queryString"
        } else {
            val categoryL1 = "$PARAM_CATEGORY_L1=${content.getOrElse(indexCategoryId) { "" }}"
            val categoryL2 = content.getOrElse(indexCategoryId+1) { "" }.let {
                if (it.isNotEmpty()) "&$PARAM_CATEGORY_L2=$it" else ""
            }
            "${ApplinkConstInternalTokopediaNow.OLD_CATEGORY}?$categoryL1$categoryL2$queryString"
        }
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

    private fun isRecipeUrl(uri: Uri): Boolean {
        val uriSegments = uri.pathSegments
        val recipePath = uriSegments.getOrNull(RECIPE_PATH_INDEX).orEmpty()
        return uriSegments.size == RECIPE_SEGMENT_COUNT && recipePath == PATH_RECIPE
    }

    private fun getQueryString(uri: Uri, delimiter: String): String {
        return if (uri.encodedQuery.isNullOrEmpty()) "" else "$delimiter${uri.encodedQuery}"
    }
}
