package com.tokopedia.common_sdk_affiliate_toko.utils

import android.net.Uri
import com.tokopedia.common_sdk_affiliate_toko.model.AdditionalParam
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.usecase.CheckCookieUseCase
import com.tokopedia.common_sdk_affiliate_toko.usecase.CreateCookieUseCase
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

class AffiliateCookieHelper @Inject constructor(
    private val createCookieUseCase: CreateCookieUseCase,
    private val checkCookieUseCase: CheckCookieUseCase,
    private val userSession: UserSessionInterface
) {
    private var affiliateUUID: String = ""

    suspend fun initCookie(
        affiliateUUID: String,
        affiliateChannel: String,
        affiliatePageDetail: AffiliatePageDetail,
        uuid: String = "",
        additionalParam: List<AdditionalParam> = emptyList(),
    ) {
        val params = AffiliateCookieParams(
            affiliateUUID,
            affiliateChannel,
            affiliatePageDetail,
            uuid,
            additionalParam
        )
        when (affiliatePageDetail.source) {
            is AffiliateSdkPageSource.PDP -> {
                if (affiliateUUID.isNotEmpty()) {
                    createCookieUseCase.createCookieRequest(
                        params,
                        userSession.deviceId,
                        userSession.androidId
                    )
                }
            }
            else -> {
                this.affiliateUUID = params.affiliateUUID
                if (affiliateUUID.isNotEmpty()) {
                    createCookieUseCase.createCookieRequest(
                        params,
                        userSession.deviceId,
                        userSession.androidId
                    )
                } else {
                    try {
                        val response = checkCookieUseCase.checkAffiliateCookie(params)
                        this.affiliateUUID = response.affiliateUuId ?: ""
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    fun createAffiliateLink(productUrl: String): String {
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