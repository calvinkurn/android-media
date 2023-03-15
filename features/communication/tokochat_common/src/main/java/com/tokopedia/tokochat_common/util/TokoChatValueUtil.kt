package com.tokopedia.tokochat_common.util

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam

object TokoChatValueUtil {
    /**
     * Role Value
     */
    const val DRIVER = "driver"
    const val CUSTOMER = "tokopedia_customer"

    /**
     * SOURCE
     */
    const val TOKOFOOD = "tokofood"

    /**
     * Message status
     */
    const val PENDING_VALUE = 0
    const val SENT_VALUE = 1
    const val READ_VALUE = 2
    const val FAILED_VALUE = 3

    /**
     * Date Format
     */
    const val DATE_FORMAT = "d MMM yyyy"
    const val RELATIVE_TODAY = "Hari ini"
    const val RELATIVE_YESTERDAY = "Kemarin"
    const val HEADER_DATE_FORMAT = "d MMMM, yyyy"

    /**
     * Compose & Bubble Message
     */
    const val MAX_DISPLAYED_OFFSET = 10_000
    const val MAX_DISPLAYED_STRING = "10.000+"
    const val MAX_MESSAGE_IN_BUBBLE = 400

    /**
     * TokoChatPrefManager
     */
    const val TOKOCHAT_CACHE = "tokoChatCache"

    /**
     * Tokochat Censor
     */
    const val MAX_PERCENTAGE = 100
    const val DEFAULT_CENSOR_PERCENTAGE = 40
    const val CENSOR_TEXT = "*"

    /**
     * TokoChat Consent
     */
    private const val CONSENT_ID_PROD = ""
    private const val CONSENT_ID_STAGING = "1f829d46-e138-42b6-8bcc-d74ada5ada04"
    val consentParam = ConsentCollectionParam(
        collectionId = getConsentId()
    )
    private fun getConsentId(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            TokoChatValueUtil.CONSENT_ID_STAGING
        } else {
            TokoChatValueUtil.CONSENT_ID_PROD
        }
    }
}
