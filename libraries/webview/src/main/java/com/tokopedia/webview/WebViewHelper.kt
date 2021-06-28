package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce
import timber.log.Timber
import java.net.URLDecoder

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

    @JvmStatic
    fun isUrlValid(url: String): Boolean {
        return if (isSeamless(url)) {
            val urlSeamless = getUrlSeamless(url)
            if (!urlSeamless.isNullOrEmpty()) isUrlValid(urlSeamless) else false
        } else {
            val domain = getDomainName(url)
            (domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN)
        }
    }

    private fun getDomainName(url: String): String {
        val domain = Uri.parse(url).host
        return if (domain != null)
            if (domain.startsWith(PREFIX_PATTERN)) domain.substring(4) else domain
        else ""
    }

    private fun isSeamless(url: String): Boolean = getDomainName(url) == JS_DOMAIN_PATTERN

    private fun getUrlSeamless(url: String): String? {
        val uri = Uri.parse(url)
        return uri.getQueryParameter(KEY_PARAM_URL)
    }


    /**
     * This function appends GA client ID as a query param for url contains tokopedia as domain
     *
     * @param url
     * @param context
     * @return
     */
    @JvmStatic
    fun appendGAClientIdAsQueryParam(url: String?, context: Context): String? {
        Timber.d("WebviewHelper before $url")
        var returnURl = url

        if (url?.contains("ta.tokopedia.com") == true)
            return url

        if (url != null && isPassingGAClientIdEnable(context)) {
            try {
                //parse url
                val uri = Uri.parse(url)


                //logic to append GA clientID in web URL to track app to web sessions
                if (uri != null && !url.contains(PARAM_APPCLIENT_ID)) {
                    val clientID = TrackApp.getInstance().getGTM().getCachedClientIDString();

                    if (clientID != null && url.contains("js.tokopedia.com")) {
                        var tokopediaEncodedUrl = uri!!.getQueryParameter("url")

                        if (tokopediaEncodedUrl != null) {
                            var tokopediaDecodedUrl =
                                URLDecoder.decode(tokopediaEncodedUrl!!, "UTF-8")
                            val tokopediaUri = Uri.parse(tokopediaDecodedUrl)
                            tokopediaDecodedUrl = tokopediaUri.buildUpon()
                                .appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build()
                                .toString()

                            returnURl = replaceUriParameter(uri!!, "url", tokopediaDecodedUrl)
                        }
                    } else if (clientID != null && url != null && url.contains(HOST_TOKOPEDIA)) {
                        returnURl =
                            uri!!.buildUpon().appendQueryParameter(PARAM_APPCLIENT_ID, clientID)
                                .build().toString()
                    }
                }
            } catch (ex: Exception) {
                //do nothing
            }
        }

        Timber.d("WebviewHelper after $returnURl")
        return returnURl
    }


    private fun isPassingGAClientIdEnable(context: Context?): Boolean {
        if (context == null) return false

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PASS_GA_CLIENT_ID_WEB, true)
    }

    private fun replaceUriParameter(uri: Uri, key: String, newValue: String): String {
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
        val query = intentUri.query
        return if (query != null && query.contains("$KEY_URL=")) {
            var url = query.substringAfter("$KEY_URL=").decode()
            url = url.normalizeSymbol()
            return getEncodedurl(url)
        } else {
            defaultUrl
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
    private fun String.normalizeSymbol(): String {
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
        } else null
    }

    /**
     * get Intent Action_VIEW for specific uri.
     * will return null if the intent is browser
     * will return action_view intent if the intent will be open other than browser.
     */
    @JvmStatic
    fun externalAppIntentNotBrowser(context: Context, uri: Uri): Intent? {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val resolveInfoOriginal: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, 0)

        if (resolveInfoOriginal.isEmpty()) {
            return null
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(EXAMPLE_DOMAIN))
        val resolveInfoBrowser = context.packageManager.queryIntentActivities(browserIntent, 0)

        val resolveInfoNative = resolveInfoOriginal.filterNot {
            resolveInfoBrowser.contains(it)
        }
        return if (resolveInfoNative.isNotEmpty()) {
            intent
        } else null
    }
}