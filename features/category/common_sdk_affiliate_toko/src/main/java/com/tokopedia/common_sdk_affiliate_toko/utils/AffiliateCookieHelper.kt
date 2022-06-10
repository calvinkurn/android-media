package com.tokopedia.common_sdk_affiliate_toko.utils

import android.content.Context
import android.net.Uri
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageType
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieRequest
import com.tokopedia.common_sdk_affiliate_toko.usecase.CreateCookieUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.URI
import java.net.URL
import java.net.URLEncoder

object AffiliateCookieHelper {

    private var affiliateUUID: String = ""

    suspend fun initCookie(
        context: Context,
        params: AffiliateCookieParams
    ) {
        when (params.affiliatePageDetail.pageType) {
            AffiliateSdkPageType.PDP -> {
                if (params.affiliateUUID.isNotEmpty()) {
                    //TODO call createAffiliateCookie
                    CreateCookieUseCase().createCookieRequest(params, UserSession(context).deviceId)
                }
            }
            else -> {
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