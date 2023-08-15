package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.webkit.WebView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.webview.BaseWebViewFragment.TOKOPEDIA_COM
import com.tokopedia.webview.data.model.WhiteListedPath
import com.tokopedia.webview.data.model.WhiteListedUrl
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import com.tokopedia.webview.ext.encodeQueryNested
import org.json.JSONArray
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * Created by Ade Fulki on 2019-06-21.
 * ade.hadian@tokopedia.com
 */

object WebViewHelper {

    private const val PREFIX_PATTERN: String = "www."
    private const val SUFFIX_PATTERN: String = ".tokopedia.com"
    private const val DOMAIN_PATTERN: String = "tokopedia.com"
    private const val JS_DOMAIN_PATTERN: String = "js.tokopedia.com"
    private const val KEY_PARAM_URL: String = "url"
    private const val PARAM_APPCLIENT_ID = "appClientId"
    private const val HOST_TOKOPEDIA = "tokopedia"
    private const val APP_WHITELISTED_DOMAINS_URL = "ANDROID_WEBVIEW_WHITELIST_DOMAIN"
    private const val APP_SCP_WHITELISTED_ROUTES = "android_scp_whitelisted_routes"
    var whiteListedDomains = WhiteListedDomains()
    var whiteListedFintechPath = WhiteListedPath()
    var whiteListedDisableGalleryPickerUrl = WhiteListedUrl()
    var contactUsForceReloadUrl = WhiteListedPath()

    private const val ANDROID_WEBVIEW_JS_ENCODE = "android_webview_js_encode"

    @JvmStatic
    fun isUrlValid(url: String): Boolean {
        return if (isSeamless(url)) {
            val urlSeamless = getUrlSeamless(url)
            if (!urlSeamless.isNullOrEmpty()) isUrlValid(urlSeamless) else false
        } else {
            isTokopediaDomain(url)
        }
    }

    fun isTokopediaDomain(url: String): Boolean {
        val domain = getDomainName(url)
        return (domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN)
    }

    private fun isDomainWhitelisted(context: Context, domain: String): Boolean {
        if (domain.isEmpty()) {
            return false
        }
        if (domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN) {
            return true
        }
        if (whiteListedDomains.domains.isEmpty()) {
            whiteListedDomains = getWhiteListedDomains(context.applicationContext)
        }
        if (whiteListedDomains.isEnabled) {
            whiteListedDomains.domains.forEach {
                if (domain == it || domain.endsWith(".$it")) {
                    return true
                }
            }
            return false
        }
        return false
    }

    @JvmStatic
    fun isUrlWhitelisted(context: Context, url: String): Boolean {
        return isDomainWhitelisted(context, getDomainName(url))
    }

    fun getWhiteListedDomains(context: Context): WhiteListedDomains {
        return try {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
            val whiteListedDomainsCsv = firebaseRemoteConfig.getString(APP_WHITELISTED_DOMAINS_URL)
            if (whiteListedDomainsCsv.isNotBlank()) {
                Gson().fromJson(whiteListedDomainsCsv, WhiteListedDomains::class.java)
            } else {
                WhiteListedDomains()
            }
        } catch (e: Exception) {
            if (!GlobalConfig.isAllowDebuggingTools()) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            WhiteListedDomains()
        }
    }

    fun getDomainName(url: String): String {
        val domain = Uri.parse(url).host
        return if (domain != null) {
            if (domain.startsWith(PREFIX_PATTERN)) domain.substring(4) else domain
        } else {
            ""
        }
    }

    private fun isSeamless(url: String): Boolean = getDomainName(url) == JS_DOMAIN_PATTERN

    private fun getUrlSeamless(url: String): String? {
        val uri = Uri.parse(url)
        return uri.getQueryParameter(KEY_PARAM_URL)
    }

    @JvmStatic
    fun appendGAClientIdAsQueryParam(url: String?, context: Context): String? {
        val rc = FirebaseRemoteConfigImpl(context)
        return if (rc.getBoolean(ANDROID_WEBVIEW_JS_ENCODE, true)) {
            appendGAClientIdAsQueryParamV2(url, context)
        } else {
            appendGAClientIdAsQueryParamLegacy(url, context)
        }
    }

    /**
     * This function appends GA client ID as a query param for url contains tokopedia as domain
     *
     * @param url
     * @param context
     * @return
     */
    @JvmStatic
    fun appendGAClientIdAsQueryParamLegacy(url: String?, context: Context): String? {
        var returnURl = url

        if (url?.contains("ta.tokopedia.com") == true) {
            return url
        }

        if (url != null && isPassingGAClientIdEnable(context)) {
            try {
                // parse url
                val uri = Uri.parse(url)

                // logic to append GA clientID in web URL to track app to web sessions
                if (uri != null && !url.contains(PARAM_APPCLIENT_ID)) {
                    val clientID = TrackApp.getInstance().getGTM().getCachedClientIDString()

                    if (clientID != null && url.contains("js.tokopedia.com")) {
                        val tokopediaDecodedUrl = uri.getQueryParameter("url")

                        if (tokopediaDecodedUrl != null) {
                            returnURl = appendAppClientId(tokopediaDecodedUrl, uri, clientID)
                        }
                    } else if (clientID != null && url.contains(HOST_TOKOPEDIA)) {
                        returnURl =
                            uri.buildUpon().appendQueryParameter(PARAM_APPCLIENT_ID, clientID)
                                .build().toString()
                    }
                }
            } catch (ex: Exception) {
                // do nothing
            }
        }
        return returnURl
    }

    private fun appendAppClientId(url: String, uri: Uri, clientID: String): String {
        val tokopediaUri = Uri.parse(url)
        val newUrl = appendAppClientQueryParameter(tokopediaUri, clientID)
        return replaceUriParameter(uri, "url", newUrl)
    }

    private fun appendAppClientQueryParameter(uri: Uri, clientID: String): String {
        return uri.buildUpon()
            .appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build()
            .toString()
    }

    /**
     * This function appends GA client ID as a query param for url contains tokopedia as domain
     *
     * @param url
     * @param context
     * @return
     */
    @JvmStatic
    fun appendGAClientIdAsQueryParamV2(url: String?, context: Context): String {
        var returnURl = url ?: ""

        if (url == null) {
            return ""
        }

        if (url.contains("ta.tokopedia.com")) {
            return url
        }

        try {
            val uri = Uri.parse(url)
            if (uri != null) {
                if (url.contains("js.tokopedia.com")) {
                    var urlQueryParam = getEncodedUrlCheckSecondUrl(uri, url)
                    urlQueryParam = urlQueryParam.encodeQueryNested()
                    val uriQueryParam = Uri.parse(urlQueryParam)
                    urlQueryParam = appendGAClientId(context, uriQueryParam)
                    return replaceUriParameter(uri, "url", urlQueryParam)
                } else if (url.contains(HOST_TOKOPEDIA)) {
                    returnURl = appendGAClientId(context, uri)
                }
            }
        } catch (ex: Exception) {
            // do nothing
        }
        return returnURl
    }

    fun appendGAClientId(context: Context, uri: Uri): String {
        val clientID = getClientId()
        if (clientID != null &&
            uri.getQueryParameter(PARAM_APPCLIENT_ID) == null &&
            isPassingGAClientIdEnable(context)
        ) {
            return uri.buildUpon()
                .appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build()
                .toString()
        } else {
            return uri.toString()
        }
    }

    fun getClientId(): String {
        return TrackApp.getInstance().gtm.cachedClientIDString
    }

    private fun isPassingGAClientIdEnable(context: Context?): Boolean {
        if (context == null) return false

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PASS_GA_CLIENT_ID_WEB, true)
    }

    fun replaceUriParameter(uri: Uri, key: String, newValue: String): String {
        val params = uri.getQueryParameterNames()
        val newUri = uri.buildUpon().clearQuery()
        for (param in params) {
            newUri.appendQueryParameter(
                param,
                if (param == key) newValue else uri.getQueryParameter(param)
            )
        }

        return newUri.build().toString()
    }

    /**
     * This function is to get the url from the Uri
     * Example:
     * Input: tokopedia://webview?url=http://www.tokopedia.com/help
     * Output:http://www.tokopedia.com/help
     *
     * Input: tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fhelp%2F
     * Output:http://www.tokopedia.com/help
     *
     * Input: tokopedia://webview?url=https://js.tokopedia.com?url=http://www.tokopedia.com/help
     * Output:https://js.tokopedia.com?url=https%3A%2F%2Fwww.tokopedia.com%2Fhelp%2F
     *
     * Input: tokopedia://webview?url=https://js.tokopedia.com?url=http://www.tokopedia.com/help?id=4&target=5&title=3
     * Output:https://js.tokopedia.com?target=5&title=3&url=http%3A%2F%2Fwww.tokopedia.com%2Fhelp%3Fid%3D4%26target%3D5%26title%3D3
     */
    fun getEncodedUrlCheckSecondUrl(intentUri: Uri, defaultUrl: String): String {
        val query = getQuery(intentUri)
        return if (query != null && query.contains("$KEY_URL=")) {
            var url = query.substringAfter("$KEY_URL=").decode()
            url = url.normalizeSymbol()
            return getEncodedurl(url)
        } else {
            query?.decode() ?: defaultUrl
        }
    }

    /**
     * Improvement from uri.query
     * Example url:
     * tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     * Expected url query = https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     *
     * Input2: tokopedia://webview?url=https://abc.com/#/a&titlebar=false
     * Expected url query = https://abc.com/#/a
     *
     * Input3: tokopedia://webview?url=https://abc.com/#/a?a=b&titlebar=false
     * Expected url query = https://abc.com/#/a?a=b&titlebar=false
     */
    fun getUrlQuery(uri: Uri): String? {
        val uriStringAfterMark = uri.toString().substringAfter("?").substringAfter("$KEY_URL=")
        return if (uriStringAfterMark.contains("#")) {
            if (uriStringAfterMark.contains("?")) {
                uriStringAfterMark
            } else {
                uriStringAfterMark.substringBefore("&")
            }
        } else {
            uri.getQueryParameter(KEY_URL)
        }
    }

    /**
     * Improvement from uri.query
     * Example url:
     * tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     * Expected query = https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     */
    private fun getQuery(uri: Uri): String? {
        val uriStringAfterQMark = uri.toString().substringAfter("?")
        if (uriStringAfterQMark.contains("#")) {
            return uriStringAfterQMark
        } else {
            return uri.query
        }
    }

    /**
     * make &url= or ?url= to be encoded
     */
    private fun getEncodedurl(url: String): String {
        val url2 = getUrlParam(url)
        return if (url2.isNullOrEmpty()) {
            url
        } else {
            val url2BeforeAnd = url2.substringBefore("&")
            val uriFromUrl =
                Uri.parse(url.replaceFirst("$KEY_URL=$url2BeforeAnd", "").normalizeDoubleSymbol())
            uriFromUrl.buildUpon()
                .appendQueryParameter(KEY_URL, url2.encodeOnce())
                .build().toString()
        }
    }

    /**
     * get substring after &url= or ?url=
     * Example:
     * Input: tokopedia://webview?url=http://www.tokopedia.com/help
     * Output:http://www.tokopedia.com/help
     * return null if not found
     */
    private fun getUrlParam(url: String): String? {
        val delimiterLength = "&$KEY_URL=".length
        var indexKeyUrl = url.indexOf("&$KEY_URL=")
        if (indexKeyUrl < 0) {
            indexKeyUrl = url.indexOf("?$KEY_URL=")
        }
        if (indexKeyUrl < 0) {
            return null
        }
        return url.substring(indexKeyUrl + delimiterLength, url.length).normalizeSymbol()
    }

    /**
     * Validate the & and ? symbol
     * Example input/output
     * https://www.tokopedia.com/events/hiburan
     * https://www.tokopedia.com/events/hiburan
     *
     * https://www.tokopedia.com/events/hiburan?parama=a&paramb=b
     * https://www.tokopedia.com/events/hiburan?parama=a&paramb=b
     *
     * https://www.tokopedia.com/events/hiburan&utm_source=7teOvA
     * https://www.tokopedia.com/events/hiburan
     */
    fun String.normalizeSymbol(): String {
        val indexAnd = indexOf("&")
        return if (indexAnd == -1) {
            this
        } else {
            val urlBeforeAnd = substringBefore("&", "")
            val indexQuestion = urlBeforeAnd.indexOf("?")
            if (indexQuestion == -1) {
                urlBeforeAnd
            } else {
                this
            }
        }
    }

    /**
     * trim invalid &
     * Example:
     * https://www.tokopedia.com/help?&a=b
     * https://www.tokopedia.com/help?a=b
     *
     * https://www.tokopedia.com/help?a=b&&c=d
     * https://www.tokopedia.com/help?a=b&c=d
     *
     * https://www.tokopedia.com/help?a=b?&c=d
     * https://www.tokopedia.com/help?a=b&c=d
     */
    private fun String.normalizeDoubleSymbol(): String {
        var url = replace("&&", "&")
        val indexQuestionAnd = url.indexOf("?&")
        if (indexQuestionAnd > -1) {
            val indexQuestion = url.indexOf("?")
            if (indexQuestion == indexQuestionAnd) {
                url = url.replaceFirst("?&", "?")
            } else {
                url = url.replaceFirst("?&", "&")
            }
        }
        return url
    }

    @JvmStatic
    fun actionViewIntent(context: Context, uri: Uri): Intent? {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val resolveInfos: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(intent, 0)
        return if (resolveInfos.isNotEmpty()) {
            intent
        } else {
            null
        }
    }

    fun getMediaPickerIntent(context: Context, hasVideo: Boolean = false, disableGallery: Boolean = false): Intent {
        return MediaPicker.intent(context) {
            pageSource(PageSource.WebView)
            modeType(if (hasVideo) ModeType.COMMON else ModeType.IMAGE_ONLY)
            pageType(if (disableGallery) PageType.CAMERA else PageType.COMMON)
            singleSelectionMode()
        }
    }

    fun isWhiteListedFintechPathEnabled(context: Context): Boolean {
        if (whiteListedFintechPath.path.isEmpty()) {
            getWhitelistFintechPrefixUrl(context)
        }
        return whiteListedFintechPath.isEnabled
    }

    private fun getWhitelistFintechPrefixUrl(context: Context) {
        try {
            val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
            val whitelistedFintechPrefixes = remoteConfig.getString(RemoteConfigKey.FINTECH_WEBVIEW_HIDE_TOOLBAR)
            if (whitelistedFintechPrefixes.isNotBlank()) {
                whiteListedFintechPath = Gson().fromJson(
                    whitelistedFintechPrefixes,
                    WhiteListedPath::class.java
                )
            }
        } catch (exception: Exception) {
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }

    fun isFintechUrlPathWhiteList(path: String): Boolean {
        whiteListedFintechPath.path.forEach {
            if (path.startsWith(it)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isScpUrl(context: Context?, url: String): Boolean {
        try {
            context?.let {
                val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it.applicationContext)
                val whiteListedScpDomains = firebaseRemoteConfig.getString(APP_SCP_WHITELISTED_ROUTES)
                if (!TextUtils.isEmpty(whiteListedScpDomains)) {
                    val domainArray = JSONArray(whiteListedScpDomains)
                    if (domainArray.length() > 0 && !TextUtils.isEmpty(url)) {
                        for (i in 0 until domainArray.length()) {
                            val domainObj = domainArray.get(i)
                            val domainStr = domainObj?.toString()
                            if (!TextUtils.isEmpty(domainStr) && url.contains(domainStr.toString())) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
            return false
        }
    }

    // Handler to inject image back to webview
    fun finishTakePicture(docType: String?, imageBase64: String?, webView: WebView) {
        if (imageBase64 != null && docType != null) {
            val script = String.format(
                "var event = new CustomEvent('cameraTriggered'," +
                    "{ detail: {document: '%s', image: 'data:image/jpeg;base64,%s'}});" +
                    "window.dispatchEvent(event);",
                docType,
                imageBase64
            )
            executeJs(script, webView)
        }
    }

    private fun executeJs(script: String, webView: WebView) {
        webView.evaluateJavascript(
            script
        ) { value: String -> Timber.d("executeJS result: $value") }
    }

    fun getBase64FromImagePath(imagePath: String): String? {
        try {
            val file = File(imagePath)
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream()
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            val imageBytes = outputStream.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }

    private fun getWhitelistDisableGalleryPicker(context: Context) {
        try {
            val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
            val data = remoteConfig.getString(RemoteConfigKey.FINTECH_WEBVIEW_DISABLE_GALLERY_PICKER)
            if (data.isNotBlank()) {
                whiteListedDisableGalleryPickerUrl = Gson().fromJson(
                    data,
                    WhiteListedUrl::class.java
                )
            }
        } catch (exception: Exception) {
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }

    @JvmStatic
    fun isWhitelistDisableGalleryPicker(context: Context, url: String): Boolean {
        if (whiteListedDisableGalleryPickerUrl.urls.isEmpty()) {
            getWhitelistDisableGalleryPicker(context)
        }

        if (whiteListedDisableGalleryPickerUrl.isEnabled) {
            whiteListedDisableGalleryPickerUrl.urls.forEach {
                if (it == url) {
                    return true
                }
            }
        }

        return false
    }

    @JvmStatic
    fun shouldReloadContactUsUrl(context: Context, url: String): Boolean {
        val uri = Uri.parse(url)
        val path = uri.path?.removePrefix("/") ?: ""
        if (uri.host?.endsWith(TOKOPEDIA_COM) == true && uri.pathSegments.isNotEmpty() && uri.pathSegments[0] == "help") {
            try {
                val remoteConfig: RemoteConfig =
                    FirebaseRemoteConfigImpl(context.applicationContext)
                val data = remoteConfig.getString(RemoteConfigKey.CONTACT_US_DISABLE_FORCE_RELOAD_URLS)
                if (data.isNotBlank()) {
                    contactUsForceReloadUrl = Gson().fromJson(
                        data,
                        WhiteListedPath::class.java
                    )
                }
            } catch (exception: Exception) {
                FirebaseCrashlytics.getInstance().recordException(exception)
            }

            if (contactUsForceReloadUrl.isEnabled) {
                contactUsForceReloadUrl.path.forEach {
                    if (path.startsWith(it)) {
                        return false
                    }
                }
            }

            return true
        }

        return false
    }
}
