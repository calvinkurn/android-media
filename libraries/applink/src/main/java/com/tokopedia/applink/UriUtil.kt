package com.tokopedia.applink

import android.net.Uri
import android.os.Bundle
import java.net.URLDecoder
import java.util.*
import java.util.regex.Pattern

object UriUtil {
    private const val ENCODING = "UTF-8"
    private const val QUERY_PARAM_SEPRATOR = "&"

    /**
     * Build pattern uri to uri String
     *
     * @param uriPattern example: "tokopedia-android-internal://merchant/product/{id}/edit/"
     * @param parameter  example: "213"
     * @return "tokopedia-android-internal://merchant/product/213/edit/
     */
    @JvmStatic
    fun buildUri(uriPattern: String,
                 vararg parameter: String?): String {
        var result = uriPattern
        if (parameter.isNotEmpty()) {
            val p = Pattern.compile("""\{(.*?)\}""")
            val m = p.matcher(uriPattern)
            var i = 0
            while (m.find()) {
                result = result.replace(m.group(), parameter[i]!!)
                i++
            }
        }
        return result
    }

    /**
     * Build pattern uri to uri String
     *
     * @param uriPattern example: "tokopedia-android-internal://merchant/product/{id}/edit/"
     * @param map  example: map("{id}" to "123", "{id2}" to "234")
     * @return "tokopedia-android-internal://merchant/product/213/edit/
     */
    @JvmStatic
    fun buildUri(uriPattern: String,
                 map: Map<String, String>?): String {
        var result = uriPattern
        if (map.isNullOrEmpty()) {
            return uriPattern
        } else {
            val p = Pattern.compile("""\{(.*?)\}""")
            val m = p.matcher(uriPattern)
            var i = 0
            while (m.find()) {
                val matchString = m.group()
                val mapTarget = map[matchString]
                if (mapTarget != null) {
                    result = result.replace(matchString, mapTarget)
                } else {
                    val idBracket = "{$matchString}"
                    val mapBracket = map[idBracket]
                    if (mapBracket != null) {
                        result = result.replace(matchString, mapBracket)
                    }
                }
                i++
            }
        }
        return result
    }

    /**
     * Destructure uri to Bundle
     *
     * @param uriPatternString example: "tokopedia-android-internal://marketplace/shop/{id_1}/etalase/{id_2}/"
     * @param uri              example: "tokopedia-android-internal://marketplace/shop/123/etalase/345"
     * @param bundle           it can be nullable if not then it will add key value in this bundle
     * @return bundle of ("id_1":123,"id_2":345)
     */
    fun destructiveUriBundle(uriPatternString: String, uri: Uri?, bundle: Bundle?): Bundle? {
        if (uri == null) {
            return null
        }
        var bundleOutput = bundle
        try {
            if (bundle == null) bundleOutput = Bundle()
            val uriPattern = Uri.parse(uriPatternString) ?: return bundle
            val size = uri.pathSegments.size.coerceAtMost(uriPattern.pathSegments.size)
            var i = 0
            while (i < size) {
                if (uriPattern.pathSegments[i].startsWith("{") &&
                        uriPattern.pathSegments[i].endsWith("}")) {
                    bundleOutput?.putString(uriPattern.pathSegments[i].substring(1, uriPattern.pathSegments[i].length - 1), uri.pathSegments[i])
                }
                i++
            }
        } catch (e: Exception) {
            return bundleOutput
        }
        return bundleOutput
    }

    /**
     * Destructure uri to list of String
     *
     * @param uriPatternString example: "tokopedia-android-internal://marketplace/shop/{id}/etalase/{id}/"
     * @param uri              example: "tokopedia-android-internal://marketplace/shop/123/etalase/345"
     * @param checkScheme      if true, will check pattern uri scheme
     * @return listOf (123, 345)
     */
    @JvmStatic
    @JvmOverloads
    fun destructureUri(uriPatternString: String, uri: Uri, checkScheme: Boolean = true): List<String> {
        val result = matchWithPattern(uriPatternString, uri, checkScheme)
        return result ?: ArrayList()
    }

    /**
     * Compare between uri pattern and uri input.
     *
     * @param pattern  example: "tokopedia-android-internal://marketplace/shop/{id_1}/etalase/{id_2}/"
     * @param inputUri example: "tokopedia-android-internal://marketplace/shop/123/etalase/345"
     * @param checkScheme      if true, will check pattern uri scheme
     * @return list of ids listOf (123, 345), if not match, will return null
     */
    @JvmOverloads
    fun matchWithPattern(pattern: String, inputUri: Uri, checkScheme: Boolean = true): List<String>? {
        return try {
            val uriPattern = Uri.parse(pattern)
            val scheme = uriPattern.scheme
            val host = uriPattern.host
            if (checkScheme && scheme != null && scheme != inputUri.scheme) {
                return null
            }
            if (host != null && host != inputUri.host) {
                return null
            }
            return matchPathsWithPattern(uriPattern.pathSegments, inputUri.pathSegments)
        } catch (e: Exception) {
            null
        }
    }

    fun matchPathsWithPattern(patternPaths: List<String>, inputPaths: List<String>): List<String>? {
        try {
            val resultList: MutableList<String> = ArrayList()
            val uriPatternSize = patternPaths.size
            if (uriPatternSize != inputPaths.size) {
                return null
            }
            var i = 0
            while (i < uriPatternSize) {
                val pathpattern = patternPaths[i]
                if (pathpattern.startsWith("{") && pathpattern.endsWith("}")) {
                    resultList.add(inputPaths[i])
                    i++
                    continue
                }
                if (pathpattern != inputPaths[i]) {
                    return null
                }
                i++
            }
            return resultList
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Compare between uri pattern and uri input.
     *
     * @param pattern  example: "shop/{id_1}/etalase/{id_2}/"
     * @param inputUri example: "shop/123/etalase/345"
     * @return map of ids mapOf ({id_1} to 123, {id_2} to 345), if not match, will return null
     */
    fun matchPathsWithPatternToMap(patternPaths: List<String>, inputPaths: List<String>): Map<String, String>? {
        try {
            val resultMap: MutableMap<String, String> = mutableMapOf()
            val uriPatternSize = patternPaths.size
            if (uriPatternSize != inputPaths.size) {
                return null
            }
            var i = 0
            while (i < uriPatternSize) {
                val pathpattern = patternPaths[i]
                if (pathpattern.startsWith("{") && pathpattern.endsWith("}")) {
                    resultMap[pathpattern] = inputPaths[i]
                    i++
                    continue
                }
                if (pathpattern != inputPaths[i]) {
                    return null
                }
                i++
            }
            return resultMap
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Compare between uri pattern and uri input.
     *
     * @param pattern  example: aff={aff_id} b={b_id}
     * @param inputUri example: aff=123 b=345
     * @return map of ids mapOf ({aff_id} to 123, {b} to 345), if not match, will return null
     */
    fun matchQueryWithPatternToMap(patternQuery: Map<String, String>, inputQueries: Map<String, String>): Map<String, String>? {
        return try {
            val resultMap: MutableMap<String, String> = mutableMapOf()
            for (pattern in patternQuery) {
                val key = pattern.key
                val inputValue = inputQueries[key]
                if (inputValue != null) {
                    resultMap[pattern.value] = inputValue
                } else {
                    resultMap[pattern.value] = ""
                }
            }
            resultMap
        } catch (e: Exception) {
            null
        }
    }

    fun destructureUriToMap(uriPatternString: String, uri: Uri, checkScheme: Boolean): MutableMap<String, Any> {
        val result: MutableMap<String, Any> = HashMap()
        try {
            val uriPattern = Uri.parse(uriPatternString) ?: return result
            if (checkScheme && uriPattern.scheme != null && uriPattern.scheme != uri.scheme) {
                return result
            }
            var uriSegmentSize = uri.pathSegments.size
            if (uriSegmentSize == 0) {
                uriSegmentSize = uriPattern.queryParameterNames.size
                if (uriSegmentSize > 0) {
                    val itr: Iterator<*> = uriPattern.queryParameterNames.iterator()
                    while (itr.hasNext()) {
                        val paramName = itr.next().toString()
                        val paramValue: Any? = uri.getQueryParameter(paramName)
                        if (paramValue != null) {
                            result[paramName] = paramValue
                        }
                    }
                } else {
                    return result
                }
            } else {
                val itr: Iterator<*> = uriPattern.pathSegments.iterator()
                var i = 0
                while (itr.hasNext()) {
                    val segmentName = itr.next().toString()
                    if (segmentName.startsWith("{") &&
                            segmentName.endsWith("}")) {
                        result[segmentName.substring(1, segmentName.length - 1)] = uri.pathSegments[i]
                    }
                    i++
                }
            }
        } catch (e: Exception) {
            return result
        }
        return result
    }

    /**
     * Build pattern uri to uri String
     *
     * @param uri             example: "tokopedia-android-internal://merchant/product/edit/"
     * @param queryParameters example: mapOf ("userId" to "222")
     * @return "tokopedia-android-internal://merchant/product/edit/?userId=222"
     */
    fun buildUriAppendParam(uri: String,
                            queryParameters: Map<String, String?>?): String {
        val stringBuilder = StringBuilder(uri)
        if (queryParameters != null && queryParameters.isNotEmpty()) {
            stringBuilder.append("?")
            for ((key, value) in queryParameters) {
                stringBuilder.append(key).append("=").append(value)
            }
        }
        return stringBuilder.toString()
    }

    fun buildUriAppendParams(uri: String,
                             queryParameters: Map<String, Any>?): String {
        val stringBuilder = StringBuilder(uri)
        if (queryParameters != null && queryParameters.isNotEmpty()) {
            stringBuilder.append("?")
            var i = 0
            for ((key, value) in queryParameters) {
                if (i > 0) stringBuilder.append("&")
                stringBuilder.append(key).append("=").append(value)
                i++
            }
        }
        return stringBuilder.toString()
    }

    @JvmStatic
    fun uriQueryParamsToMap(url: String): Map<String, String> {
        return (uriQueryParamsToMap(Uri.parse(url)))
    }

    @JvmStatic
    fun uriQueryParamsToMap(uri: Uri): Map<String, String> {
        try {
            val query = uri.query
            return stringQueryParamsToMap(query)
        } catch (e: Exception) {
        }
        return mapOf()
    }

    @JvmStatic
    fun stringQueryParamsToMap(query: String?): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        try {
            if (!query.isNullOrEmpty()) {
                val pairs = query.split(QUERY_PARAM_SEPRATOR)
                for (pair in pairs) {
                    val idx = pair.indexOf("=")
                    map[URLDecoder.decode(pair.substring(0, idx), ENCODING)] = URLDecoder.decode(pair.substring(idx + 1), ENCODING)
                }
            }
            return map
        } catch (e: Exception) {
        }
        return map
    }

    fun trimDeeplink(uri: Uri, deeplink: String): String {
        val qIndex = deeplink.indexOf('?')
        val deeplinkWithoutQuery = if (uri.query?.isNotEmpty() == true && qIndex > 0) {
            deeplink.substring(0, qIndex)
        } else deeplink
        return if (deeplinkWithoutQuery.endsWith("/")) {
            deeplinkWithoutQuery.substringBeforeLast("/")
        } else {
            deeplinkWithoutQuery
        }
    }

    /**
     * @param query "a=123&b=234
     * @param uri: tokopedia://home?a=12
     * @return b=234
     */
    fun getDiffQuery(query: String, uri: Uri): String {
        return try {
            val queryList = query.split("&")
            val strResultList: MutableList<String> = mutableListOf()
            for (q in queryList) {
                val keyValueSplit = q.split("=")
                val key = keyValueSplit[0]
                val value = keyValueSplit[1]
                val valueOriginalUri = uri.getQueryParameter(key)
                if (valueOriginalUri == null) {
                    strResultList.add(keyValueSplit[0] + "=" + value)
                }
            }
            strResultList.joinToString("&")
        } catch (e: Exception) {
            query
        }
    }

    /**
     * only append the difference query
     * deeplink: a://a?a=123&b=234
     * query: c=345&a=234
     * result: a://a?a=123&b=234&c=345 (a is not overwriten)
     */
    fun appendDiffDeeplinkWithQuery(deeplink: String, query: String?): String {
        val uri = Uri.parse(deeplink)
        return if (query?.isNotEmpty() == true && deeplink.isNotEmpty()) {
            val diffQuery = UriUtil.getDiffQuery(query, uri)
            appendDeeplinkWithQuery(deeplink, diffQuery)
        } else {
            deeplink
        }
    }

    fun appendDeeplinkWithQuery(deeplink: String, query: String): String {
        if (query.isEmpty()) {
            return deeplink
        }
        val questionMarkIndex = deeplink.indexOf("?")
        return deeplink + if (questionMarkIndex == -1) {
            "?$query"
        } else {
            "&$query"
        }
    }
}