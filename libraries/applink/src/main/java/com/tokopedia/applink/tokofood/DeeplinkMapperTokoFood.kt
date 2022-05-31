package com.tokopedia.applink.tokofood

import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood

object DeeplinkMapperTokoFood {

    const val PATH_ORDER_ID = "orderId"

    fun getTokoFoodPostPurchaseInternalAppLink(uri: Uri): String {
        val orderId = uri.lastPathSegment
        return Uri.parse(ApplinkConstInternalTokoFood.POST_PURCHASE)
            .buildUpon()
            .appendQueryParameter(PATH_ORDER_ID, orderId)
            .build().toString()
    }
}