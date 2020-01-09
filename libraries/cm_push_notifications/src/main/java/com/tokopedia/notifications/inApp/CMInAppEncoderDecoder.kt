package com.tokopedia.notifications.inApp

import android.text.TextUtils
import android.util.Base64
import com.tokopedia.notifications.common.EncoderDecoder
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

const val initialVector = "dG9rb3BlZGlhNzg5Jioo"

object CMInAppEncoderDecoder {

    fun encodeCMInApp(model: CMInApp?) {
        model?.let {
            val decodedKey = Base64.decode(initialVector, Base64.DEFAULT).toString()
            if (!TextUtils.isEmpty(it.cmLayout?.appLink))
                it.cmLayout?.appLink = EncoderDecoder.Encrypt(it.cmLayout?.appLink!!,
                        decodedKey)
            if (!TextUtils.isEmpty(it.cmLayout?.titleText?.txt))
                it.cmLayout?.titleText?.txt = EncoderDecoder.Encrypt(it.cmLayout?.titleText?.txt!!, decodedKey)
            if (!TextUtils.isEmpty(it.cmLayout?.messageText?.txt))
                it.cmLayout?.messageText?.txt = EncoderDecoder.Encrypt(it.cmLayout?.messageText?.txt!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignId))
                it.campaignId = EncoderDecoder.Encrypt(it.campaignId!!, decodedKey)
            if (!TextUtils.isEmpty(it.parentId))
                it.parentId = EncoderDecoder.Encrypt(it.parentId!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Encrypt(it.campaignUserToken!!, decodedKey)
        }

    }

    fun decodeCMInApp(model: CMInApp?) {
        model?.let {
            val decodedKey = Base64.decode(initialVector, Base64.DEFAULT).toString()
            if (!TextUtils.isEmpty(it.cmLayout?.appLink))
                it.cmLayout?.appLink = EncoderDecoder.Decrypt(it.cmLayout?.appLink!!, decodedKey)
            if (!TextUtils.isEmpty(it.cmLayout?.titleText?.txt))
                it.cmLayout?.titleText?.txt = EncoderDecoder.Decrypt(it.cmLayout?.titleText?.txt!!, decodedKey)
            if (!TextUtils.isEmpty(it.cmLayout?.messageText?.txt))
                it.cmLayout?.messageText?.txt = EncoderDecoder.Decrypt(it.cmLayout?.messageText?.txt!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignId))
                it.campaignId = EncoderDecoder.Decrypt(it.campaignId!!, decodedKey)
            if (!TextUtils.isEmpty(it.parentId))
                it.parentId = EncoderDecoder.Decrypt(it.parentId!!, decodedKey)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Decrypt(it.campaignUserToken!!, decodedKey)
        }

    }
}