package com.tokopedia.tokochat.common.util

import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.IC_SHOPPING_LOGISTIC_SOURCE
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam

object TokoChatCommonValueUtil {
    /**
     * Role Value
     */
    const val DRIVER = "driver"
    const val CUSTOMER = "tokopedia_customer"

    /**
     * SOURCE
     */
    const val SOURCE_LOGISTIC = "logistic"
    const val SOURCE_TOKOFOOD = "tokofood"
    const val SOURCE_GOSEND_INSTANT = "gosend_instant"
    const val SOURCE_GOSEND_SAMEDAY = "gosend_sameday"

    const val TOKOFOOD_SERVICE_TYPE = 5
    const val GOSEND_INSTANT_SERVICE_TYPE = 14
    const val GOSEND_SAMEDAY_SERVICE_TYPE = 23

    fun getSource(serviceType: Int): String {
        return when (serviceType) {
            TOKOFOOD_SERVICE_TYPE -> SOURCE_TOKOFOOD
            GOSEND_INSTANT_SERVICE_TYPE -> SOURCE_GOSEND_INSTANT
            GOSEND_SAMEDAY_SERVICE_TYPE -> SOURCE_GOSEND_SAMEDAY
            else -> SOURCE_GOSEND_INSTANT
        }
    }

    fun getSourceIcon(serviceType: Int): String {
        return when (serviceType) {
            TOKOFOOD_SERVICE_TYPE -> IC_TOKOFOOD_SOURCE
            GOSEND_INSTANT_SERVICE_TYPE -> IC_SHOPPING_LOGISTIC_SOURCE
            GOSEND_SAMEDAY_SERVICE_TYPE -> IC_SHOPPING_LOGISTIC_SOURCE
            else -> IC_SHOPPING_LOGISTIC_SOURCE
        }
    }

    fun isChatTokofood(serviceType: Int): Boolean {
        return serviceType == TOKOFOOD_SERVICE_TYPE
    }

    fun getSourceIcon(source: String): String {
        return when (source) {
            SOURCE_TOKOFOOD -> IC_TOKOFOOD_SOURCE
            SOURCE_GOSEND_INSTANT -> IC_SHOPPING_LOGISTIC_SOURCE
            SOURCE_GOSEND_SAMEDAY -> IC_SHOPPING_LOGISTIC_SOURCE
            else -> IC_SHOPPING_LOGISTIC_SOURCE
        }
    }

    fun getSourceCategory(source: String): String {
        return when (source) {
            SOURCE_TOKOFOOD -> SOURCE_TOKOFOOD
            SOURCE_GOSEND_INSTANT -> SOURCE_LOGISTIC
            SOURCE_GOSEND_SAMEDAY -> SOURCE_LOGISTIC
            else -> SOURCE_LOGISTIC
        }
    }

    const val GOFOOD = "GoFood"
    const val SHOPPING_LOGISTIC = "Belanja"

    /**
     * Message status
     */
    const val PENDING_VALUE = 0
    const val SENT_VALUE = 1
    const val READ_VALUE = 2
    const val FAILED_VALUE = 3

    /**
     * Compose & Bubble Message
     */
    const val MAX_DISPLAYED_OFFSET = 10_000
    const val MAX_DISPLAYED_STRING = "10.000+"
    const val MAX_MESSAGE_IN_BUBBLE = 400
    const val IMAGE_ATTACHMENT_MSG = "Image Attachment"

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
     * Tokochat Attachment Menu
     */
    const val ATTACHMENT_IMAGE = "Gambar"

    /**
     * TokoChat Consent
     */
    private const val CONSENT_ID_PROD = "365acb97-61a9-44d8-9a68-464dc9465ec7"
    private const val CONSENT_ID_STAGING = "1f829d46-e138-42b6-8bcc-d74ada5ada04"
    val consentParam = ConsentCollectionParam(
        collectionId = getCollectionId()
    )
    private fun getCollectionId(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            CONSENT_ID_STAGING
        } else {
            CONSENT_ID_PROD
        }
    }

    /**
     * TokoChat Bubbles
     */
    const val IS_FROM_BUBBLE_KEY = "isFromBubble"

    /**
     * TokoChat List
     */
    const val BATCH_LIMIT = 10

    /**
     * Driver Name
     */
    fun getFirstNameDriver(driverName: String): String {
        return driverName.split(" ").firstOrNull() ?: ""
    }
}
