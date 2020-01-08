package com.tokopedia.notifications.inApp

import android.text.TextUtils
import com.tokopedia.notifications.common.EncoderDecoder
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

const val initialVector = "tokopedia1234567"

object CMInAppEncoderDecoder {

    fun encodeCMInApp(model: CMInApp?) {
        model?.let {
            if (!TextUtils.isEmpty(it.cmLayout?.appLink))
                it.cmLayout?.appLink = EncoderDecoder.Encrypt(it.cmLayout?.appLink!!, initialVector)
            if (!TextUtils.isEmpty(it.cmLayout?.titleText?.txt))
                it.cmLayout?.titleText?.txt = EncoderDecoder.Encrypt(it.cmLayout?.titleText?.txt!!, initialVector)
            if (!TextUtils.isEmpty(it.cmLayout?.messageText?.txt))
                it.cmLayout?.messageText?.txt = EncoderDecoder.Encrypt(it.cmLayout?.messageText?.txt!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignId))
                it.campaignId = EncoderDecoder.Encrypt(it.campaignId!!, initialVector)
            if (!TextUtils.isEmpty(it.parentId))
                it.parentId = EncoderDecoder.Encrypt(it.parentId!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Encrypt(it.campaignUserToken!!, initialVector)
        }

    }

    fun decodeCMInApp(model: CMInApp?) {
        model?.let {
            if (!TextUtils.isEmpty(it.cmLayout?.appLink))
                it.cmLayout?.appLink = EncoderDecoder.Decrypt(it.cmLayout?.appLink!!, initialVector)
            if (!TextUtils.isEmpty(it.cmLayout?.titleText?.txt))
                it.cmLayout?.titleText?.txt = EncoderDecoder.Decrypt(it.cmLayout?.titleText?.txt!!, initialVector)
            if (!TextUtils.isEmpty(it.cmLayout?.messageText?.txt))
                it.cmLayout?.messageText?.txt = EncoderDecoder.Decrypt(it.cmLayout?.messageText?.txt!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignId))
                it.campaignId = EncoderDecoder.Decrypt(it.campaignId!!, initialVector)
            if (!TextUtils.isEmpty(it.parentId))
                it.parentId = EncoderDecoder.Decrypt(it.parentId!!, initialVector)
            if (!TextUtils.isEmpty(it.campaignUserToken))
                it.campaignUserToken = EncoderDecoder.Decrypt(it.campaignUserToken!!, initialVector)
        }

    }
}