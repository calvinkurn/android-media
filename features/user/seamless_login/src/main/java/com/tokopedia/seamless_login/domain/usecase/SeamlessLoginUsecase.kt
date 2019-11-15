package com.tokopedia.seamless_login.domain.usecase

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.seamless_login.data.UserDataPojo
import com.tokopedia.seamless_login.internal.SeamlessLoginConstant
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
        val date = AuthHelper.generateDate(DATE_FORMAT)

        val seamlessUrl = Uri.parse(TokopediaUrl.getInstance().JS)
                .buildUpon()
                .appendPath(SeamlessLoginConstant.SEAMLESS_PATH)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.TOKEN.value, userSession.deviceId)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.OS_TYPE.value, "1")
                .appendQueryParameter(SeamlessLoginConstant.PARAM.UID.value, userSession.userId)
                .appendQueryParameter(SeamlessLoginConstant.PARAM.URL.value, URLEncoder.encode(url, "UTF-8"))
                .appendQueryParameter(SeamlessLoginConstant.PARAM.VERSION.value, GlobalConfig.VERSION_CODE.toString())
                .appendQueryParameter(SeamlessLoginConstant.PARAM.TIMESTAMP.value, System.currentTimeMillis().toString())
                .appendQueryParameter(SeamlessLoginConstant.PARAM.BROWSER.value, "1")

//        val seamlessUrl =
//                TokopediaUrl.getInstance().JS +
//                        "seamless?" +
//                        "os_type=1" +
//                        "&url=${URLEncoder.encode(url, "UTF-8")}"  +
//                        "&version=${GlobalConfig.VERSION_CODE}" +
//                        "&timestamp=${System.currentTimeMillis()}" +
//                        "&uid=${userSession.userId}" +
//                        "&token=${userSession.deviceId}" +
//                        "&browser=1"

        val contentMD5 = AuthUtil.md5(seamlessUrl.build().query)

        val authString = (
                SeamlessLoginConstant.METHOD_GET
                + "\n" + contentMD5
                + "\n" + AuthUtil.CONTENT_TYPE
                + "\n" + date
                + "\n" + SeamlessLoginConstant.SEAMLESS_PATH)
        val signature = AuthHelper.calculateRFC2104HMAC(authString, AuthUtil.KEY.KEY_WSV4)

        val userData = UserDataPojo(
                date = date,
                content_md5 = contentMD5,
                authorization = "${SeamlessLoginConstant.HMAC_SIGNATURE}${signature.trim()}",
                accountAuth = "${SeamlessLoginConstant.BEARER}${userSession.accessToken}"
        )

        val encryptedUserData = AESUtils.encrypt(gson.toJson(userData), key = aesKey)
        println("EncryptedUserDataKey $aesKey")
        println("EncryptedUserData $encryptedUserData")

        seamlessUrl.appendQueryParameter(SeamlessLoginConstant.PARAM.DATA.value, URLEncoder.encode(encryptedUserData, "UTF-8"))
        return  seamlessUrl.build().toString()
    }
}