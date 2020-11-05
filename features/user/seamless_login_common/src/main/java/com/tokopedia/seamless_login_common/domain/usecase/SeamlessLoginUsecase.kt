package com.tokopedia.seamless_login_common.domain.usecase

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.seamless_login_common.data.UserDataPojo
import com.tokopedia.seamless_login_common.internal.SeamlessLoginConstant
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.seamless_login_common.utils.AESUtils
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginUsecase @Inject constructor(
        private val getKeygenUsecase: GetKeygenUsecase,
        private val userSession: UserSessionInterface,
        private val gson: Gson) {

    fun generateSeamlessUrl(callbackUrl: String, listener: SeamlessLoginSubscriber?){
        if(!userSession.isLoggedIn){
            listener?.onUrlGenerated(callbackUrl)
        }else {
            getKeygenUsecase.execute(
                    onSuccess = {
                        listener?.onUrlGenerated(
                                createData(callbackUrl, it.data.key)
                        )
                    },
                    onError = {
                        listener?.onError(it.message ?: "")
                    }
            )
        }
    }

    private fun buildUrl(callbackUrl: String): Uri.Builder {
        return Uri.parse(TokopediaUrl.getInstance().JS)
                .buildUpon()
                .appendPath(SeamlessLoginConstant.SEAMLESS_PATH)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.TOKEN.value, userSession.deviceId)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.OS_TYPE.value, SeamlessLoginConstant.OS_TYPE_VALUE)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.UID.value, userSession.userId)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.URL.value, callbackUrl.safeEncodeUtf8())
                .appendQueryParameter(SeamlessLoginConstant.PARAM.VERSION.value, GlobalConfig.VERSION_CODE.toString())
                .appendQueryParameter(SeamlessLoginConstant.PARAM.TIMESTAMP.value, System.currentTimeMillis().toString())
                .appendQueryParameter(SeamlessLoginConstant.PARAM.BROWSER.value, SeamlessLoginConstant.BROWSER_VALUE)
    }

    private fun String.safeEncodeUtf8(): String {
        return try {
            this.encodeToUtf8()
        }
        catch(throwable: Throwable) {
            this
        }
    }

    private fun generateSignature(contentMD5: String, date: String): String {
        val authString = (
                SeamlessLoginConstant.METHOD_GET
                        + "\n" + contentMD5
                        + "\n" + AuthUtil.CONTENT_TYPE
                        + "\n" + date
                        + "\n" + SeamlessLoginConstant.SEAMLESS_KEY)
        return AuthHelper.calculateRFC2104HMAC(authString, AuthUtil.KEY.KEY_WSV4)
    }

    private fun createData(url: String, aesKey: String): String {
        val date = AuthHelper.generateDate(SeamlessLoginConstant.DATE_FORMAT)

        val seamlessUrl = buildUrl(url)
        val decodedURL = seamlessUrl.build().query?.decodeToUtf8()
        val contentMD5 = AuthUtil.md5(decodedURL)

        val signature = generateSignature(contentMD5, date)

        val userData = UserDataPojo(
            date = date,
            content_md5 = contentMD5,
            authorization = "${SeamlessLoginConstant.HMAC_SIGNATURE}${signature.trim()}",
            accountAuth = "${SeamlessLoginConstant.BEARER}${userSession.accessToken}"
        )

        val encryptedUserData = AESUtils.encrypt(gson.toJson(userData), key = aesKey)
        seamlessUrl.appendQueryParameter(SeamlessLoginConstant.PARAM.DATA.value, encryptedUserData.encodeToUtf8())
        return seamlessUrl.build().toString().decodeToUtf8()
    }
}