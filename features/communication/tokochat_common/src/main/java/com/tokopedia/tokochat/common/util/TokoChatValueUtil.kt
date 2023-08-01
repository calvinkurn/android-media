package com.tokopedia.tokochat.common.util

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
    const val TOKOFOOD_SERVICE_TYPE = 5
    fun getSource(serviceType: Int): String {
        return when (serviceType) {
            TOKOFOOD_SERVICE_TYPE -> TOKOFOOD
            else -> ""
        }
    }
    const val GOFOOD = "GoFood"

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
}
