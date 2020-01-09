package com.tokopedia.notifications.common

import android.text.TextUtils
import android.util.Base64
import com.tokopedia.notifications.model.BaseNotificationModel

const val initialVector = "dG9rb3BlZGlhNzg5Jioo"

object CMPushEncoderDecoder {

    fun encodeBaseNotificationModel(model: BaseNotificationModel?) {
        model?.let {
            val decodedKey = Base64.decode(initialVector, Base64.DEFAULT).toString()
            if (!TextUtils.isEmpty(it.title))
                it.title = EncoderDecoder.Encrypt(it.title!!, decodedKey)
            if (!TextUtils.isEmpty(it.message))
                it.message = EncoderDecoder.Encrypt(it.message!!, decodedKey)
            if (!TextUtils.isEmpty(it.appLink))
                it.appLink = EncoderDecoder.Encrypt(it.appLink!!, decodedKey)
            if (!TextUtils.isEmpty(it.detailMessage))
                it.detailMessage = EncoderDecoder.Encrypt(it.detailMessage!!, decodedKey)
            if (!TextUtils.isEmpty(it.subText))
                it.subText = EncoderDecoder.Encrypt(it.subText!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Encrypt(it.campaignUserToken!!, decodedKey)
        }

    }

    fun decodeBaseNotificationModel(model: BaseNotificationModel?) {
        model?.let {
            val decodedKey = Base64.decode(initialVector, Base64.DEFAULT).toString()
            if (!TextUtils.isEmpty(it.title))
                it.title = EncoderDecoder.Decrypt(it.title!!, decodedKey)
            if (!TextUtils.isEmpty(it.message))
                it.message = EncoderDecoder.Decrypt(it.message!!, decodedKey)
            if (!TextUtils.isEmpty(it.appLink))
                it.appLink = EncoderDecoder.Decrypt(it.appLink!!, decodedKey)
            if (!TextUtils.isEmpty(it.detailMessage))
                it.detailMessage = EncoderDecoder.Decrypt(it.detailMessage!!, decodedKey)
            if (!TextUtils.isEmpty(it.subText))
                it.subText = EncoderDecoder.Decrypt(it.subText!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Decrypt(it.campaignUserToken!!, decodedKey)
        }

    }
}