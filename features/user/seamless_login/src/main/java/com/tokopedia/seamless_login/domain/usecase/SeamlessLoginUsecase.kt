package com.tokopedia.seamless_login.domain.usecase

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.seamless_login.data.UserDataPojo
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.seamless_login.utils.AESUtils
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLEncoder
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginUsecase @Inject constructor(
        private val getKeygenUsecase: GetKeygenUsecase,
        private val userSession: UserSessionInterface,
        private val gson: Gson) {

    private val MAC_ALGORITHM = "HmacSHA1"
    private val PARAM_X_TKPD_USER_ID = "x-tkpd-userid"
    private val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ"

    fun generateSeamlessUrl(callbackUrl: String, listener: SeamlessLoginSubscriber){
        getKeygenUsecase.execute(
            onSuccess = {
                listener.onUrlGenerated(
                        createData(callbackUrl,
                                it.data.key
                        )
                )
            },
            onError = {
                listener.onError(it.message ?: "")
            }
        )
    }

    private fun createData(url: String, aesKey: String): String {
        val uri = Uri.parse(url)
        val date = AuthHelper.generateDate(DATE_FORMAT)

        val seamlessUrl =
                TokopediaUrl.getInstance().JS +
                        "seamless?" +
                        "os_type=1" +
                        "&url=${URLEncoder.encode(url, "UTF-8")}"  +
                        "&version=${GlobalConfig.VERSION_CODE}" +
                        "&timestamp=${System.currentTimeMillis()}" +
                        "&uid=${userSession.userId}" +
                        "&token=${userSession.deviceId}" +
                        "&browser=1"

        val contentMD5 = AuthUtil.md5(Uri.parse(seamlessUrl).query)

        val authString = ("GET"
                + "\n" + contentMD5
                + "\n" + "application/x-www-form-urlencoded"
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userSession.userId
                + "\n" + uri.path)
        val signature = AuthHelper.calculateRFC2104HMAC(authString, AuthUtil.KEY.KEY_WSV4)

        val userData = UserDataPojo(
                date = date,
                content_md5 = contentMD5,
                authorization = "TKPD Tokopedia:${signature.trim()}",
                accountAuth = "Bearer ${userSession.accessToken}"
        )

        val encryptedUserData = AESUtils.encrypt(gson.toJson(userData), key = aesKey)
        println("EncryptedUserDataKey $aesKey")
        println("EncryptedUserData $encryptedUserData")
        return  "$seamlessUrl&data=${URLEncoder.encode(encryptedUserData, "UTF-8")}"
    }
}