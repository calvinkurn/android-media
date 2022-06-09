package com.tokopedia.common_sdk_affiliate_toko.utils

import android.content.Context
import android.net.Uri
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.URI
import java.net.URL
import java.net.URLEncoder

object AffiliateCookieHelper {

    private var affiliateUUID: String = ""

    fun initCookie(
        context: Context,
        params: AffiliateCookieParams
    ) {
        when (params) {
            is AffiliateCookieParams.PDP -> {
                if (params.affiliateUUID.isNotEmpty()) {
                    //TODO call createAffiliateCookie
                }
            }
            is AffiliateCookieParams.AffiliateSdkPage -> {
                affiliateUUID = params.affiliateUUID
                if (affiliateUUID.isNotEmpty()) {
                    //TODO call createAffiliateCookie
                } else {
                    //TODO call checkAffiliateCookie
                }
            }
        }
    }

    fun onProductClick(productUrl: String): String {
        if (affiliateUUID.isNotEmpty()) {
            return Uri.parse(productUrl)
                .buildUpon()
                .appendQueryParameter("aff_unique_id", affiliateUUID)
                .build()
                .toString()
        }
        return productUrl
    }

}