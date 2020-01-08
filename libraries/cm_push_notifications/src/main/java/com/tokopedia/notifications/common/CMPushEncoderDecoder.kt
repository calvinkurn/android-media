package com.tokopedia.notifications.common

import android.text.TextUtils
import com.tokopedia.notifications.model.BaseNotificationModel

const val initialVector = "tokopedia1234567"

object CMPushEncoderDecoder {

    fun encodeBaseNotificationModel(model: BaseNotificationModel?) {
        model?.let {
            if (!TextUtils.isEmpty(it.title))
                it.title = EncoderDecoder.Encrypt(it.title!!, initialVector)
            if (!TextUtils.isEmpty(it.message))
                it.message = EncoderDecoder.Encrypt(it.message!!, initialVector)
            if (!TextUtils.isEmpty(it.appLink))
                it.appLink = EncoderDecoder.Encrypt(it.appLink!!, initialVector)
            if (!TextUtils.isEmpty(it.detailMessage))
                it.detailMessage = EncoderDecoder.Encrypt(it.detailMessage!!, initialVector)
            if (!TextUtils.isEmpty(it.subText))
                it.subText = EncoderDecoder.Encrypt(it.subText!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Encrypt(it.campaignUserToken!!, initialVector)
        }

    }

    fun decodeBaseNotificationModel(model: BaseNotificationModel?) {
        model?.let {
            if (!TextUtils.isEmpty(it.title))
                it.title = EncoderDecoder.Decrypt(it.title!!, initialVector)
            if (!TextUtils.isEmpty(it.message))
                it.message = EncoderDecoder.Decrypt(it.message!!, initialVector)
            if (!TextUtils.isEmpty(it.appLink))
                it.appLink = EncoderDecoder.Decrypt(it.appLink!!, initialVector)
            if (!TextUtils.isEmpty(it.detailMessage))
                it.detailMessage = EncoderDecoder.Decrypt(it.detailMessage!!, initialVector)
            if (!TextUtils.isEmpty(it.subText))
                it.subText = EncoderDecoder.Decrypt(it.subText!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Decrypt(it.campaignUserToken!!, initialVector)
        }

    }
}