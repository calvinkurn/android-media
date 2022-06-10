package com.tokopedia.common_sdk_affiliate_toko.utils

import android.content.Context
import android.net.Uri
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageType
import com.tokopedia.common_sdk_affiliate_toko.usecase.CheckCookieUseCase
import com.tokopedia.common_sdk_affiliate_toko.usecase.CreateCookieUseCase
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class AffiliateCookieHelper @Inject constructor(
    private val createCookieUseCase: CreateCookieUseCase,
    private val checkCookieUseCase: CheckCookieUseCase,
) {

    private var affiliateUUID: String = ""

    suspend fun initCookie(
        context: Context,
        params: AffiliateCookieParams,
    ) {
        val userSession = UserSession(context)
        when (params.affiliatePageDetail.pageType) {
            AffiliateSdkPageType.PDP -> {
                if (params.affiliateUUID.isNotEmpty()) {
                    createCookieUseCase.createCookieRequest(
                        params,
                        userSession.deviceId,
                        userSession.androidId
                    )
                }
            }
            else -> {
                affiliateUUID = params.affiliateUUID
                if (affiliateUUID.isNotEmpty()) {
                    createCookieUseCase.createCookieRequest(
                        params,
                        userSession.deviceId,
                        userSession.androidId
                    )
                } else {
                    try {
                        val response = checkCookieUseCase.checkAffiliateCookie(params)
                        affiliateUUID = response.affiliateUuId ?: ""
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
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