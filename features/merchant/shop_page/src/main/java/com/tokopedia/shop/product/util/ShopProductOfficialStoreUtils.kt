package com.tokopedia.shop.product.util

import android.app.Activity
import android.net.Uri
import com.tokopedia.shop.product.view.activity.SimpleWebViewActivity.Companion.createIntent
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import timber.log.Timber
import com.tokopedia.shop.product.view.activity.SimpleWebViewActivity
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

/**
 * Created by nathan on 3/5/18.
 */
object ShopProductOfficialStoreUtils {
    const val HTTP = "http"
    const val TOKOPEDIA_HOST = "tokopedia"
    private const val SEAMLESS = "seamless"
    private const val URL_QUERY_SORT = "sort"
    private const val URL_QUERY_KEYWORD = "keyword"
    private const val URL_QUERY_PAGE = "page"
    private const val URL_PATH_ETALASE = "etalase"
    private const val URL_PATH_PRODUCT = "product"
    private const val URL_PATH_PAGE = "page"
    private const val URL_RECHARGE_HOST = "pulsa.tokopedia.com"
    fun proceedUrl(activity: Activity, url: String, shopId: String, login: Boolean, fcmTokenId: String, uid: String): Boolean {
        val uri = Uri.parse(url)
        if (uri.scheme == TOKOPEDIA_HOST) {
            processUriTokopedia(activity, shopId, uri)
        } else if (uri.scheme?.startsWith(HTTP) == true) {
            if (isNeededToLogin(url) and !login) {
                return false
            } else if (isNeededToLogin(url) and login) {
                openWebViewWithSession(activity, url, fcmTokenId, uid)
            } else {
                openWebView(activity, url)
            }
        }
        return true
    }

    private fun processUriTokopedia(activity: Activity, shopId: String, uri: Uri) {
        var keyword = ""
        var page: String? = ""
        val paths = uri.pathSegments
        if (paths.size > 1) {
            when (paths[1]) {
                URL_PATH_ETALASE -> {
                    val id = uri.lastPathSegment
                    val params = getShopProductRequestModel(uri)
                    if (!TextUtils.isEmpty(params[URL_QUERY_KEYWORD])) {
                        keyword = params[URL_QUERY_KEYWORD].orEmpty()
                    }
                    // Pointing specific page on apps will be break the page
                    page = params[URL_QUERY_PAGE]
                    activity.startActivity(ShopProductListResultActivity.createIntent(activity, shopId, keyword, id,
                            "", params[URL_QUERY_SORT], ""))
                }
                URL_PATH_PRODUCT -> {
                    val productId = uri.lastPathSegment
                    RouteManager.route(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
                }
                URL_PATH_PAGE -> {
                    val params = getShopProductRequestModel(uri)
                    if (!TextUtils.isEmpty(params.get(URL_QUERY_KEYWORD))) {
                        keyword = params.get(URL_QUERY_KEYWORD).orEmpty()
                    }
                    // Pointing specific page on apps will be break the page
                    page = uri.lastPathSegment
                    activity.startActivity(ShopProductListResultActivity.createIntent(activity, shopId, keyword,
                            null, "", params.get(URL_QUERY_SORT), ""))
                }
            }
        } else {
            val params = getShopProductRequestModel(uri)
            // Pointing specific page on apps will be break the page
            if (!TextUtils.isEmpty(params[URL_QUERY_KEYWORD])) {
                keyword = params[URL_QUERY_KEYWORD].orEmpty()
            }
            page = params[URL_QUERY_PAGE]
            activity.startActivity(ShopProductListResultActivity.createIntent(activity, shopId, keyword,
                    null, "", params[URL_QUERY_SORT], ""))
        }
    }

    private fun getShopProductRequestModel(uri: Uri): HashMap<String, String?> {
        val params = HashMap<String, String?>()
        val parameterNames = uri.queryParameterNames
        for (parameterName in parameterNames) {
            when (parameterName) {
                URL_QUERY_SORT -> params[URL_QUERY_SORT] = uri.getQueryParameter(parameterName)
                URL_QUERY_KEYWORD -> params[URL_QUERY_KEYWORD] = uri.getQueryParameter(parameterName)
                URL_QUERY_PAGE -> params[URL_PATH_PAGE] = uri.getQueryParameter(parameterName)
            }
        }
        return params
    }

    fun getLogInUrl(url: String, fcmTokenId: String?, uid: String?): String {
        return if (!url.contains(SEAMLESS)) {
            URLGenerator.generateURLSessionLogin(encodeUrl(url), fcmTokenId, uid)
        } else url
    }

    private fun encodeUrl(url: String): String {
        try {
            return URLEncoder.encode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return url
    }

    private fun openWebViewWithSession(activity: Activity, url: String, fcmTokenId: String, uid: String) {
        val encodedUrl = encodeUrl(url)
        if (encodedUrl != null) {
            openWebView(activity, URLGenerator.generateURLSessionLogin(encodeUrl(url), fcmTokenId, uid))
        }
    }

    private fun isNeededToLogin(url: String): Boolean {
        when (Uri.parse(url).host) {
            URL_RECHARGE_HOST -> return true
        }
        return false
    }

    private fun openWebView(activity: Activity, url: String) {
        Timber.d(url)
        activity.startActivity(createIntent(activity, url))
    }
}