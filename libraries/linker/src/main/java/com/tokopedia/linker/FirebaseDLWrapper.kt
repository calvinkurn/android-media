package com.tokopedia.linker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.webkit.URLUtil
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.track.TrackApp
import java.util.*


class FirebaseDLWrapper {
    private val linkPath = "link"
    private val androidUrlPath = "android_url"
    private val iosUrlPath = "ios_url"
    private val firebaseBaseUrl = "https://tkpd.page.link"
    fun getFirebaseDynamicLink(activity: Activity, intent: Intent) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData ->
                // Get deep link from result ( null if no link found)
                parseandLaunchFirebaseUrl(pendingDynamicLinkData,activity)
            }
            .addOnFailureListener(activity) { e ->
                    val messageMap = mapOf(
                        "type" to "validation",
                        "reason" to "FdlOpenError",
                        "data" to (e.message?:"")
                    )
                    logging(messageMap)
                }
            }
    /**
    * There are 3 possible cases
    * 1- Long link:  link = firebaseUrl.toString()
    * 2- Short link with custom format: link= firebaseUrl.getQueryParameter("android_url")
    * 3- Short link with standard format: link = firebaseUrl.getQueryParameter("link")
    * ex: https://tkpd.page.link/?link=https://www.tokopedia.com?android_url%3Dtokopedia://home%26ios_url%3Dtokopedia://home
    */
    private fun parseandLaunchFirebaseUrl(pendingDynamicLinkData: PendingDynamicLinkData?, activity: Activity?) {
        if (pendingDynamicLinkData==null) return
        val firebaseUrl: Uri? = pendingDynamicLinkData.link
            if (firebaseUrl != null) {
                var link: String? = firebaseUrl.getQueryParameter(androidUrlPath)
                if (link == null) {
                    link = firebaseUrl.getQueryParameter(linkPath)
                    if (link != null) {
                        val internalLink: String? =
                            Uri.parse(link).getQueryParameter(androidUrlPath)
                        if (internalLink != null) {
                            link = internalLink
                        }
                    }
                }
                if (link == null) { // in case of long firebase URL
                    link = firebaseUrl.toString()
                }

                launchActivity(activity,link,firebaseUrl)
                //no need to check link != null as  link = firebaseUrl.toString() and firebaseUrl cant be null in this block
                val messageMap = mapOf("type" to "validation", "reason" to "FdlOpen", "data" to link)
                logging(messageMap)
            }

    }

    private fun launchActivity(activity: Activity?, link: String?, firebaseUrl: Uri?){
        if (activity != null && link !=null && firebaseUrl!=null) {
            // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
            // because we need tracking UTM for those notification applink
            var tokopediaDeeplink: String? = link
            if (!URLUtil.isNetworkUrl(link)) {
                    if (link.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                        tokopediaDeeplink = link
                    } else {
                        tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + link
                    }
            }

            tokopediaDeeplink?.let {
                RouteManager.route(activity, tokopediaDeeplink)
                processUtmParams(getLinkToProcessUtm(activity, link), firebaseUrl) }
        }
    }

    private fun getLinkToProcessUtm(activity: Activity?, link: String) : String{
        activity?.let { containerActivity ->
            containerActivity.intent.data?.let { intentUri ->
                if(intentUri.queryParameterNames.contains(LinkerConstants.UTM_SOURCE)
                        && intentUri.queryParameterNames.contains(LinkerConstants.UTM_CAMPAIGN)){
                    return intentUri.toString()
                }
            }
        }
        return link
    }

    private fun processUtmParams(link: String, firebaseUrl: Uri) {
        var linkUri: Uri? = null
        linkUri = Uri.parse(link)
        var utmSource: String? = null
        var utmMedium: String? = null
        var utmCampaign: String? = null
        var utmTerm: String? = null
        if (linkUri != null) {
            utmSource = linkUri.getQueryParameter(LinkerConstants.UTM_SOURCE)
            utmMedium = linkUri.getQueryParameter(LinkerConstants.UTM_MEDIUM)
            utmCampaign = linkUri.getQueryParameter(LinkerConstants.UTM_CAMPAIGN)
            utmTerm = linkUri.getQueryParameter(LinkerConstants.UTM_TERM)
        }
        if (utmSource == null) {
            utmSource = firebaseUrl.getQueryParameter(LinkerConstants.UTM_SOURCE)
        }
        val linkParam = firebaseUrl.getQueryParameter(linkPath)
        if (utmMedium == null) {
            utmMedium = if(!TextUtils.isEmpty(linkParam)){
                Uri.parse(linkParam)?.getQueryParameter(LinkerConstants.UTM_MEDIUM) ?: ""
            } else {
                firebaseUrl.getQueryParameter(LinkerConstants.UTM_MEDIUM)
            }
        }
        if (utmCampaign == null) {
            utmCampaign = if(!TextUtils.isEmpty(linkParam)) {
                Uri.parse(linkParam)?.getQueryParameter(LinkerConstants.UTM_CAMPAIGN) ?: ""
            }else {
                firebaseUrl.getQueryParameter(LinkerConstants.UTM_CAMPAIGN)
            }
        }
        if (utmTerm == null) {
            utmTerm = firebaseUrl.getQueryParameter(LinkerConstants.UTM_TERM)
        }

        convertToCampaign(
            utmSource,
            utmCampaign,
            utmMedium,
            utmTerm
        )
    }

    fun createShortLink(shareCallback: ShareCallback, data: LinkerData) {
        Firebase.dynamicLinks.shortLinkAsync {

            var deeplinkdata = getDeeplinkData(data)
            if (deeplinkdata != null) {
                link = Uri.parse(deeplinkdata)
                domainUriPrefix = firebaseBaseUrl
                val fallbackUri = getFallbackUrl(data)
                androidParameters { if (needFallbakUrl(data)) fallbackUrl = fallbackUri }
                iosParameters(LinkerConstants.IOS_BUNDLE_ID) {
                    if (needFallbakUrl(data)) setFallbackUrl(
                        fallbackUri
                    )
                }
                socialMetaTagParameters {
                    title = data.ogTitle
                    description = data.description
                }
                if(!(deeplinkdata.contains(LinkerConstants.UTM_SOURCE))
                            || !(deeplinkdata.contains(LinkerConstants.UTM_MEDIUM))) {

                    googleAnalyticsParameters {
                        source = if (!TextUtils.isEmpty(data.channel)) {
                            data.channel
                        } else {
                            LinkerData.ARG_UTM_SOURCE
                        }
                        medium = if (!TextUtils.isEmpty(data.feature)) {
                            data.feature
                        } else {
                            LinkerData.ARG_UTM_MEDIUM
                        }
                        campaign = data.campaignName
                    }
                }
            }

        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            if (shortLink != null) {
                var link = shortLink.toString()
                shareCallback.urlCreated(LinkerUtils.createShareResult(link, link, link))
                val messageMap = mapOf("type" to "validation", "reason" to "FdlCreationSucess", "data" to link)
                logging(messageMap)
            }

        }.addOnFailureListener {
            if(it?.message!=null) {
                shareCallback.urlCreated(
                    LinkerUtils.createShareResult(
                        data.textContent,
                        getFailureFallbackUrl(data),
                        getFailureFallbackUrl(data)
                    )
                )

                val messageMap =
                    mapOf("type" to "validation", "reason" to "FdlCreationError", "data" to it.message.toString())
                logging(messageMap)
            }
        }

    }

    private fun getFailureFallbackUrl(data: LinkerData): String? {
        var fallbackUrl = data.renderShareUri()
        if (TextUtils.isEmpty(fallbackUrl)
            && !TextUtils.isEmpty(data.desktopUrl)
        ) {
            fallbackUrl = data.desktopUrl
        }
        return fallbackUrl
    }

    private fun getDeeplinkData(data: LinkerData): String? {
        var uri = data.renderShareUri() // FDL require URL starting with https
        if (uri == null && data.desktopUrl != null) {
            uri = data.desktopUrl
        } else if (uri == null) {
            uri = LinkerConstants.WEB_DOMAIN
        }

        var deeplink = createLinkProperties(data)
        deeplink= Uri.encode(deeplink)
        if (uri != null) {
            if (uri.contains("?")) {
                uri = "$uri&$androidUrlPath=$deeplink&$iosUrlPath=$deeplink"
            } else {
                uri = "$uri?$androidUrlPath=$deeplink&$iosUrlPath=$deeplink"
            }
        }
        return uri.toString()
    }

    private fun needFallbakUrl(data: LinkerData): Boolean {
        if (LinkerData.REFERRAL_TYPE.equals(data.type, ignoreCase = true)) {
            return false
        }else if (LinkerData.APP_SHARE_TYPE.equals(data.type, ignoreCase = true)) {
            return false
        }
        return true
    }

    private fun getFallbackUrl(data: LinkerData): Uri {
        var fallbackUrl = data.renderShareUri()
        if (fallbackUrl == null){
            if (LinkerData.GROUPCHAT_TYPE.equals(data.type, ignoreCase = true)) {
                fallbackUrl = LinkerConstants.DESKTOP_GROUPCHAT_URL
            } else if (LinkerData.REFERRAL_TYPE.equals(data.type, ignoreCase = true)||
                LinkerData.APP_SHARE_TYPE.equals(data.type, ignoreCase = true)) {
                fallbackUrl = LinkerConstants.REFERRAL_DESKTOP_URL
            }else{
                fallbackUrl = data.desktopUrl
            }
        }
        if (fallbackUrl == null) fallbackUrl = LinkerConstants.WEB_DOMAIN
        return Uri.parse(fallbackUrl)
    }

    private fun createLinkProperties(data: LinkerData): String? {
        var deeplinkPath = getApplinkPath(data.renderShareUri(), "")
        when {
            LinkerData.PRODUCT_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(LinkerConstants.PRODUCT_INFO, data.id)
            }
            LinkerData.SHOP_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath =
                    getApplinkPath(LinkerConstants.SHOP, data.id) //"shop/" + data.getId();
            }
            LinkerData.HOTLIST_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(
                    LinkerConstants.DISCOVERY_HOTLIST_DETAIL,
                    data.id
                ) //"hot/" + data.getId();
            }
            LinkerData.CATALOG_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(LinkerConstants.DISCOVERY_CATALOG, data.id)
            }
            LinkerData.PROMO_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(LinkerConstants.PROMO_DETAIL, data.id)
            }
            LinkerData.PLAY_BROADCASTER.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = data.uri
            }
            LinkerData.PLAY_VIEWER.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(LinkerConstants.PLAY, data.id) //"play/" + data.getId();
            }
            LinkerData.MERCHANT_VOUCHER.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = data.deepLink
            }
            LinkerData.GROUPCHAT_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = getApplinkPath(LinkerConstants.GROUPCHAT, data.id)
            }
            LinkerData.INDI_CHALLENGE_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = data.deepLink
            }
            LinkerData.HOTEL_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = data.deepLink
            }
            LinkerData.ENTERTAINMENT_TYPE.equals(data.type, ignoreCase = true) -> {
                deeplinkPath = data.deepLink
            }
        }
        if (deeplinkPath != null) {
            if (!deeplinkPath.contains(LinkerConstants.UTM_SOURCE)) {
                deeplinkPath = data.renderShareUri(deeplinkPath)
            }
        }
        return deeplinkPath
    }

    private fun getApplinkPath(url: String, id: String?): String? {
        var url = url
        if (url.contains(LinkerConstants.APPLINKS + "://")) {
            url = url.replace(LinkerConstants.APPLINKS + "://", "")
            url = url.replaceFirst(LinkerConstants.REGEX_APP_LINK.toRegex(), id ?: "")
        } else if (url.contains(LinkerConstants.WEB_DOMAIN)) {
            url = url.replace(LinkerConstants.WEB_DOMAIN, "")
        } else if (url.contains(LinkerConstants.MOBILE_DOMAIN)) {
            url = url.replace(LinkerConstants.MOBILE_DOMAIN, "")
        }
        return url

    }

    private fun convertToCampaign(
        utmSource: String?,
        utmCampaign: String?,
        utmMedium: String?,
        utmTerm: String?
    ) {
        if (!(TextUtils.isEmpty(utmSource) || TextUtils.isEmpty(utmMedium))) {
            val param: MutableMap<String, Any> = HashMap()
            param[LinkerConstants.SCREEN_NAME_KEY] = LinkerConstants.SCREEN_NAME_VALUE
            param[LinkerConstants.UTM_SOURCE] = utmSource ?: ""
            param[LinkerConstants.UTM_CAMPAIGN] = utmCampaign ?: ""
            param[LinkerConstants.UTM_MEDIUM] = utmMedium ?: ""
            if (!TextUtils.isEmpty(utmTerm)) {
                param[LinkerConstants.UTM_TERM] = utmTerm ?: ""
            }
            sendCampaignToTrackApp(param)
        }
    }

    private fun sendCampaignToTrackApp(param: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendCampaign(param)
    }

    private fun logging(messageMap: Map<String, String>){
        ServerLogger.log(Priority.P2, "FDL_VALIDATION", messageMap)
    }
}