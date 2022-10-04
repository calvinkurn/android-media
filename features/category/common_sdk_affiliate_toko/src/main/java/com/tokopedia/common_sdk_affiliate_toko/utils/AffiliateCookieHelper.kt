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

/**
 *  Helper class to record affiliate attribution and create affiliate link
 *
 * @see initCookie
 *
 * @see createAffiliateLink
 */
class AffiliateCookieHelper @Inject constructor(
    private val createCookieUseCase: CreateCookieUseCase,
    private val checkCookieUseCase: CheckCookieUseCase,
    private val userSession: UserSessionInterface
) {
    companion object {
        private const val ATC_SOURCE = "shop_page"
    }

    var affiliateUUID: String = ""
        private set

    /**
     * Creates cookie if internally saved affiliateUUID is not empty otherwise Checks cookie and updates affiliateUUID
     *
     * @param[affiliateUUID] required and pass it from url query params
     * @param[affiliateChannel] required and pass it from url query params
     * @param[affiliatePageDetail] [AffiliatePageDetail]
     * @param[uuid] random UUID generated every time user land on PDP page. (Required for Product)
     * @param[isATC] mandatory for Direct ATC attribution
     * @param[additionalParam] some additional params of type [AdditionalParam]
     *
     */
    suspend fun initCookie(
        affiliateUUID: String,
        affiliateChannel: String,
        affiliatePageDetail: AffiliatePageDetail,
        uuid: String = "",
        isATC: Boolean = false,
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
                        if (isATC) ATC_SOURCE else ""
                    )
                }
            }
            else -> {
                this.affiliateUUID = params.affiliateUUID
                if (affiliateUUID.isNotEmpty()) {
                    createCookieUseCase.createCookieRequest(
                        params,
                        userSession.deviceId,
                        if (isATC) ATC_SOURCE else ""
                    )
                } else {
                    try {
                        val checkAffiliateResponse =
                            checkCookieUseCase.checkAffiliateCookie(params, userSession.deviceId)
                        this.affiliateUUID = checkAffiliateResponse.response.affiliateUuId ?: ""
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    /**
     * Helper function to create affiliate link of an url.
     *
     * @param[productUrl] url of product of which affiliate link is needed
     *
     * @return appends an affiliate UUID query to param to your AppLink or URL. if the cookie not recorded, will return the same [productUrl]
     */
    fun createAffiliateLink(productUrl: String): String {
        if (affiliateUUID.isNotEmpty()) {
            return Uri.parse(productUrl)
                .buildUpon()
                .appendQueryParameter(AffiliateSdkConstant.AFFILIATE_UUID, affiliateUUID)
                .build()
                .toString()
        }
        return productUrl
    }

}
