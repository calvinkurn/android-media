package com.tokopedia.applink.teleporter

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.lang.reflect.Type

object Teleporter {
    var teleporterPatternList: List<TeleporterPattern>? = null
    var lastFetch: Long = 0L
    const val DURATION_FETCH = 900000
    const val MAINAPP_TELEPORTER_REMOTE_CONFIG = "android_mainapp_teleporter"
    const val SELLERAPP_TELEPORTER_REMOTE_CONFIG = "android_sellerapp_teleporter"
    val gson = Gson()

    /**
     * Example input:
     * patternList:
     * listOf (Pattern(
     *            "tokopedia",
     *            "product",
     *            "{product_id}",
     *            "abc={abc_id}" // this query is must have. Input uri must have this query to map.
     *            "aff={aff_id}",// this query is optional. no need to exist in input
     *            "tokopedia-android-internal/product/test?id={product_id}&def={abc_id}&affiliate={aff_id}")
     *       , Pattern(...)
     *       , ...)
     * uriToCheck: tokopedia://product/123?abc=879&aff=2142&xyz=987
     *
     * output: tokopedia-android-internal/product/test?id=123&def=879&affiliate=2142&xyz=987
     */
    fun switchIfNeeded(context: Context, uriToCheck: Uri): String {
        val patternList = getConfig(context) ?: return ""
        return switchIfNeeded(patternList, uriToCheck)
    }

    fun switchIfNeeded(patternList: List<TeleporterPattern>, uriToCheck: Uri): String {
        try {
            val schemeToCheck = uriToCheck.scheme
            val hostToCheck = uriToCheck.host
            val query = uriToCheck.query

            // example: will result to query map (abc to 879, aff to 2142, xyz to 987)
            lateinit var queryToCheckMap: Map<String, String>
            lateinit var queryMustHaveMatchMap: Map<String, String>
            lateinit var pathMatchMap: Map<String, String>

            val patternMatch = patternList.asSequence().filter { pattern ->
                pattern.scheme == schemeToCheck && pattern.host == hostToCheck
            }.filter {
                var conditionPathPassed = true
                // check path
                // example: will result into map of ids {product_id} to 123}
                val pathMatchMapTemp = UriUtil.matchPathsWithPatternToMap(it.pathPatternList,
                        uriToCheck.pathSegments)
                if (pathMatchMapTemp == null) {
                    conditionPathPassed = false
                } else {
                    pathMatchMap = pathMatchMapTemp
                }
                conditionPathPassed
            }.filter {
                var conditionQueryPassed = true
                queryToCheckMap = UriUtil.stringQueryParamsToMap(query)
                queryMustHaveMatchMap = if (it.queryMustHavePatternMap.isNotEmpty()) {
                    val queryMap = UriUtil.matchQueryWithPatternToMap(it.queryMustHavePatternMap, queryToCheckMap)
                    if (queryMap == null) {
                        conditionQueryPassed = false
                        mapOf()
                    } else {
                        for ((_, value) in queryMap) {
                            if (value.isEmpty()) {
                                conditionQueryPassed = false
                                break
                            }
                        }
                        queryMap
                    }
                } else {
                    mapOf()
                }
                conditionQueryPassed
            }.firstOrNull() ?: return ""

            // will result to mapOf ({aff_id} to 2142)
            val queryOptionalMatchMap = UriUtil.matchQueryWithPatternToMap(patternMatch.queryOptionalPatternMap, queryToCheckMap)

            var resultUriString = UriUtil.buildUri(patternMatch.target, pathMatchMap)
            resultUriString = UriUtil.buildUri(resultUriString, queryMustHaveMatchMap)
            resultUriString = UriUtil.buildUri(resultUriString, queryOptionalMatchMap)

            ServerLogger.log(Priority.P1, "WEBVIEW_SWITCH", mapOf("type" to uriToCheck.toString(), "url" to resultUriString))

            // appendDiffDeeplinkWithQuery is used to append missing queries to target deeplink
            return UriUtil.appendDiffDeeplinkWithQuery(resultUriString, query)
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getConfig(context: Context): List<TeleporterPattern>? {
        val now = System.currentTimeMillis()
        if (now - lastFetch > DURATION_FETCH) {
            getLatestConfig(context)
            lastFetch = now
        }
        return teleporterPatternList
    }

    private fun getLatestConfig(context: Context) {
        try {
            val remoteConfig = FirebaseRemoteConfigInstance.get(context)
            var configString = ""

            configString = if (GlobalConfig.isSellerApp()) {
                remoteConfig.getString(SELLERAPP_TELEPORTER_REMOTE_CONFIG)
            } else {
                remoteConfig.getString(MAINAPP_TELEPORTER_REMOTE_CONFIG)
            }

            if (configString != null) {
                val listType: Type = object : TypeToken<MutableList<TeleporterPattern>>() {}.type
                teleporterPatternList = gson.fromJson(configString, listType)
            }
        } catch (e: Exception) {
            teleporterPatternList = null
        }

    }
}