package com.tokopedia.applink.promo

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalPromo


fun getRegisteredNavigationTokopoints(context: Context,deeplink : String) =
        when(deeplink) {
            ApplinkConst.TokoPoints.HOMEPAGE -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
            ApplinkConst.TokoPoints.HOMEPAGE2 -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
            else -> getDynamicDeeplinkForTokopoints(context,deeplink.replace(ApplinkConst.TOKOPOINTS,ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS))
    }




fun getDynamicDeeplinkForTokopoints(context: Context,deeplink : String) : String {
    if (deeplink.contains("kupon-saya/detail")){
        return deeplink.replace("kupon-saya/detail","kupon-detail")
    }
    if (deeplink.contains("tukar-point/detail")){
        return deeplink.replace("tukar-point/detail","tukar-detail")
    }
    return deeplink
}
