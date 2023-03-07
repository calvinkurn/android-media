package com.tokopedia.applink.promo

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp


fun getRegisteredNavigationTokopoints(deeplink: String) =
    when (deeplink) {
        ApplinkConst.TOKOPEDIA_REWARD, ApplinkConst.TOKOPOINTS -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
        else ->
            getDynamicDeeplinkForTokopoints(deeplink)
    }

fun getDynamicDeeplinkForTokopoints(deeplink: String): String {
    val uri = Uri.parse(deeplink)
   return when {
        (deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) && uri.pathSegments.isEmpty())
                || (deeplink.contains(ApplinkConst.TOKOPOINTS) && uri.pathSegments.isEmpty()) -> {
            getSourceDeeplink(deeplink)
        }
        else -> {
            getDestinationDeeplink(deeplink)
        }
    }
}

fun getSourceDeeplink(deeplink: String): String {
   return when {
        deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) -> {
            return deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.TOKOPOINTS_HOME
            )
        }
        deeplink.contains(ApplinkConst.TOKOPOINTS) -> {
            return deeplink.replace(
                ApplinkConst.TOKOPOINTS,
                ApplinkConstInternalPromo.TOKOPOINTS_HOME
            )
        }
       else -> ""
   }
}

fun getDestinationDeeplink(deeplink: String): String {
    val uri = Uri.parse(deeplink)
    val deepLinkInternal: String = when {
        deeplink.contains(ApplinkConst.TOKOPOINTS) -> {
            deeplink.replace(
                ApplinkConst.TOKOPOINTS,
                ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
            )
        }
        deeplink.contains(ApplinkConst.TOKOPEDIA_REWARD) -> {
            deeplink.replace(
                ApplinkConst.TOKOPEDIA_REWARD,
                ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
            )
        }
        else -> ""
    }
    when {
        deepLinkInternal.contains(ApplinkConst.TokoPoints.COUPON_DETAIL) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.COUPON_DETAIL,
                ApplinkConst.TokoPoints.COUPON_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_DETAIL_NEW,
                ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_DETAIL) -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_DETAIL,
                ApplinkConst.TokoPoints.CATALOG_DETAIL_VALUE
            )
        }
        deepLinkInternal.contains(ApplinkConst.TokoPoints.CATALOG_LIST_NEW) &&
                uri.pathSegments[0] == ApplinkConst.TokoPoints.CATALOG_LIST_NEW -> {
            return deepLinkInternal.replace(
                ApplinkConst.TokoPoints.CATALOG_LIST_NEW,
                ApplinkConst.TokoPoints.CATALOG_LIST_VALUE
            )
        }
        else -> {
        }
    }
    return deepLinkInternal
}

// To handle Tokomember applinks with params
fun getDynamicDeeplinkForTokomember(deeplink: String) : String{
    val uri = Uri.parse(deeplink)
    return when {
        deeplink.contains(ApplinkConst.Tokomember.PROGRAM_EXTENSION) -> getDeeplinkForProgramExtension(uri)
        else -> ""
    }
}

fun getDeeplinkForProgramExtension(deeplink: Uri):String{
    if(UriUtil.matchWithPattern(ApplinkConst.SellerApp.TOKOMEMBER_PROGRAM_EXTENSION,deeplink)!=null){
        val programId = deeplink.lastPathSegment
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_EXTENSION,programId)
    }
    return ""
}

fun getRegisteredNavigationPromoFromHttp(deeplink:Uri) : String{
   val query = deeplink.encodedQuery
    val queryString = if(query.isNullOrEmpty()) "" else "?${query}"
    val path = deeplink.encodedPath ?: ""
    val regexMap = getPromoRegexMap()
    when{
       isMatchPattern(regexMap[ApplinkConst.Tokomember.COUPON_DETAIL],path) -> {
           val applink = ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_DETAIL
           val couponId = deeplink.lastPathSegment
           return UriUtil.buildUri(applink,couponId) + queryString
       }
        else -> ""
    }
    return ""
}

fun getPromoRegexMap() : MutableMap<String,Regex> {
    val couponDetailRegex = "^(/.*/voucher/[0-9]+)"
    return mutableMapOf(
      ApplinkConst.Tokomember.COUPON_DETAIL to  Regex(couponDetailRegex)
    )
}


fun isMatchPattern(pattern:Regex?,link:String) : Boolean{
    return pattern?.matches(link) ?: false
}

