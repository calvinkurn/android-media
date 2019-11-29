package com.tokopedia.abstraction.base.view.webview

import android.content.Context
import android.net.Uri
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import java.net.URLDecoder
import java.net.URLEncoder

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
    private const val HOST_TOKOPEDIA = "tokopedia.com"

    @JvmStatic
    fun isUrlValid(url: String): Boolean {
        return if(isSeamless(url)){
            val urlSeamless = getUrlSeamless(url)
            if(!urlSeamless.isNullOrEmpty()) isUrlValid(urlSeamless) else false
        }else{
            val domain = getDomainName(url)
            (domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN)
        }
    }

    private fun getDomainName(url: String): String {
        val domain = Uri.parse(url).host
        return if(domain != null)
            if (domain.startsWith(PREFIX_PATTERN)) domain.substring(4) else domain
        else ""
    }

    private fun isSeamless(url: String): Boolean = getDomainName(url) == JS_DOMAIN_PATTERN

    private fun getUrlSeamless(url: String): String?{
        val uri = Uri.parse(url)
        return uri.getQueryParameter(KEY_PARAM_URL)
    }

//    /**
//     * This function appends GA client ID as a query param for url contains tokopedia as domain
//     * @param url
//     * @param context
//     * @return
//     */
//    @JvmStatic
//    fun appendGAClientIdAsQueryParam(url: String?, context: Context): String? {
//        var newUrl = url ?:  return ""
//
//        if (isPassingGAClientIdEnable(context)) {
//            try {
//                val decodedUrl = URLDecoder.decode(newUrl, "UTF-8")
//
//                //parse url
//                val uri = Uri.parse(decodedUrl)
//
//                //logic to append GA clientID in web URL to track app to web sessions
//                if (uri != null) {
//                    val clientID = TrackApp.getInstance().gtm.clientIDString
//
//                    if (clientID != null && newUrl.contains(HOST_TOKOPEDIA)) {
//                        newUrl = uri.buildUpon().appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build().toString()
//                        newUrl = URLEncoder.encode(newUrl, "UTF-8")
//                    }
//                }
//            } catch (ex: Exception) {
//                //do nothing
//            }
//
//        }
//
//        return newUrl
//    }


    /**
     * This function appends GA client ID as a query param for url contains tokopedia as domain
     *
     * @param url
     * @param context
     * @return
     */
    private fun appendGAClientIdAsQueryParam(url: String?, context: Context): String {
        Log.d("WebviewHelper before URL" , url)
        var url: String? = url ?: return ""


        if (isPassingGAClientIdEnable(context)) {
            try {
                val decodedUrl = URLDecoder.decode(url!!, "UTF-8")

                //parse url
                val uri = Uri.parse(decodedUrl)


                //logic to append GA clientID in web URL to track app to web sessions
                if (uri != null  && !decodedUrl.contains(PARAM_APPCLIENT_ID)) {
                    val clientID = "testid"//TrackApp.getInstance().getGTM().getClientIDString();

                    if (decodedUrl.contains("js.tokopedia.com")) {
                        var tokopediaUrl = uri!!.getQueryParameter("url")
                        if (tokopediaUrl != null) {
                            val tokopediaUri = Uri.parse(tokopediaUrl)
                            tokopediaUrl = tokopediaUri.buildUpon().appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build().toString()
                            tokopediaUrl = URLEncoder.encode(tokopediaUrl!!, "UTF-8")

                            //url = uri.buildUpon().a

                            url = replaceUriParameter(uri!!, "url", tokopediaUrl)

                        }
                    } else if (clientID != null && url != null && url.contains(HOST_TOKOPEDIA)) {
                        url = uri!!.buildUpon().appendQueryParameter(PARAM_APPCLIENT_ID, clientID).build().toString()
                    }
                }
            } catch (ex: Exception) {
                //do nothing
            }

        }

        Log.d("WebviewHelper updated URL" , url)
        return url
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
            newUri.appendQueryParameter(param,
                    if (param == key) newValue else uri.getQueryParameter(param))
        }

        return newUri.build().toString()
    }
}