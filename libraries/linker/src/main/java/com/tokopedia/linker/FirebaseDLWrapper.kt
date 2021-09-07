package com.tokopedia.linker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.tokopedia.applink.RouteManager
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import java.util.*


class FirebaseDLWrapper {
    val urlPath = "link"
    val androidUrlPath = "android_url"
    val iosUrlPath = "ios_url"
    fun getFirebaseDynamicLink(activity: Activity, intent: Intent) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var firebaseUrl: Uri? = null
                if (pendingDynamicLinkData != null) {
                    firebaseUrl = pendingDynamicLinkData.link
                    if (firebaseUrl != null) {
                        Log.d("FirebaseDLWrapper", firebaseUrl.toString())

                        var link: String? = firebaseUrl.getQueryParameter(androidUrlPath)
                        if (link == null) {
                            link = firebaseUrl.getQueryParameter(urlPath)
                            if (link != null) {
                                var tempLink: String? =
                                    firebaseUrl.getQueryParameter(androidUrlPath)
                                if (tempLink != null) {
                                    link = tempLink
                                }
                            }
                        }
                        if (link == null) { // in case of long firebase URL
                            link = firebaseUrl.toString()
                        }
                        if (link != null && activity != null) {
                            RouteManager.route(activity, link)
                            processUtmParams( link, firebaseUrl)
                        }
                    }
                }

            }
            .addOnFailureListener(activity) { e ->
                Log.w(
                    "FirebaseDLWrapper",
                    "getDynamicLink:onFailure",
                    e
                )
            }

    }

    private fun processUtmParams( link: String, firebaseUrl: Uri) {
        var linkUri: Uri? = null
        linkUri = Uri.parse(link)
        var utmSource: String? = null
        var utmMedium: String? = null
        var utmCampaign: String? = null
        var utmTerm: String? = null
        if (linkUri != null) {
            utmSource = linkUri.getQueryParameter("utm_source")
            utmMedium = linkUri.getQueryParameter("utm_medium")
            utmCampaign = linkUri.getQueryParameter("utm_campaign")
            utmTerm = linkUri.getQueryParameter("utm_term")
        }
        if (utmSource == null) {
            utmSource = firebaseUrl.getQueryParameter("utm_source")
        }
        if (utmMedium == null) {
            utmMedium = firebaseUrl.getQueryParameter("utm_medium")
        }
        if (utmCampaign == null) {
            utmCampaign = firebaseUrl.getQueryParameter("utm_campaign")
        }
        if (utmTerm == null) {
            utmTerm = firebaseUrl.getQueryParameter("utm_term")
        }

        convertToCampaign(
            utmSource,
            utmCampaign,
            utmMedium,
            utmTerm
        )
    }

    fun createShortLink(shareCallback: ShareCallback, data: LinkerData) {
        // [START create_short_link]
        Firebase.dynamicLinks.shortLinkAsync {
            var uri: String? = null
            uri = data.uri
            if (uri == null) {
                uri = data.desktopUrl
            }
            if (uri != null) {
                var deeplink = createLinkProperties(data)
                if (uri.contains("?")) {
                    uri = "$uri&$androidUrlPath=$deeplink"
                    uri = "$uri&$iosUrlPath=$deeplink"

                } else {
                    uri = "$uri?$androidUrlPath=$deeplink"
                    uri = "$uri&$iosUrlPath=$deeplink"
                }
                uri = Uri.encode(uri)
                var deeplinkdata = "https://tkpd.page.link/?$urlPath=$uri"
                link = Uri.parse(deeplinkdata)
                domainUriPrefix = "https://tkpd.page.link"
                androidParameters { }
                iosParameters("com.tokopedia.Tokopedia") { }
                socialMetaTagParameters {
                    title = data.ogTitle
                    description = data.description
                }

                if (!uri.contains("utm_source")) {
                    googleAnalyticsParameters {
                        source = LinkerData.ARG_UTM_SOURCE
                        medium = LinkerData.ARG_UTM_MEDIUM
                        campaign = data.campaignName
                    }
                }

            }
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
            // com.google.firebase.dynamiclinks.ktx.component2

            // Short link created
            var link = shortLink.toString()
            shareCallback.urlCreated(LinkerUtils.createShareResult(link, link, link))

        }.addOnFailureListener {
            // Error
            // ...
        }

    }

    fun createLinkProperties(data: LinkerData): String? {
        var deeplinkPath = getApplinkPath(data.renderShareUri(), "")
        var desktopUrl = data.desktopUrl
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
        }

        if (deeplinkPath != null) {
            if (!deeplinkPath.contains("utm_source")) {
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
        Log.d(
            "FirebaseDLWrapper",
            "source=$utmSource, campaign=$utmCampaign, medium=$utmMedium, term=$utmTerm"
        )
    }

    private fun sendCampaignToTrackApp( param: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendCampaign(param)
        Log.d("FirebaseDLWrapper", "sent")
    }
}