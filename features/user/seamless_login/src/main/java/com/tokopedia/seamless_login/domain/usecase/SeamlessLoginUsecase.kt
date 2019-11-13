package com.tokopedia.seamless_login.domain.usecase

import android.net.Uri
import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.seamless_login.data.UserDataPojo
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.seamless_login.utils.AESUtils
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
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

    fun generateSeamlessUrl(callbackUrl: String, listener: SeamlessLoginSubscriber){
        getKeygenUsecase.execute(
            onSuccess = {
                listener.onUrlGenerated(
                        createData(callbackUrl,
                                it.data.key
//                                HexUtils.byteToHex(
//                                    Base64.decode(it.data.key.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
//                                )
                        )
                )
            },
            onError = {
                listener.onError(it.message ?: "")
            }
        )
    }

    private fun generateDate(): String{
        val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH)
        return simpleDateFormat.format(Date())
    }

    private fun calculateRFC2104HMAC(authString: String, authKey: String): String {
        return try {
            val signingKey = SecretKeySpec(authKey.toByteArray(), MAC_ALGORITHM)
            val mac = Mac.getInstance(MAC_ALGORITHM)
            mac.init(signingKey)
            val rawHmac = mac.doFinal(authString.toByteArray())
            Base64.encodeToString(rawHmac, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun createData(url: String, aesKey: String): String {
        val uri = Uri.parse(url)
        val date = generateDate()
        val contentMD5 = AuthUtil.md5(uri.query)

        val authString = ("GET"
                + "\n" + contentMD5
                + "\n" + "application/x-www-form-urlencoded"
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userSession.userId
                + "\n" + uri.path)
        val signature = calculateRFC2104HMAC(authString, AuthUtil.KEY.KEY_WSV4)

        val userData = UserDataPojo(
                date = date,
                content_md5 = contentMD5,
                authorization = "TKPD Tokopedia:${signature.trim()}",
                method = "GET",
                accountAuth = "Bearer ${userSession.accessToken}"
        )

        val aesData = AESUtils.encrypt(gson.toJson(userData), key = aesKey).toByteArray(Charsets.UTF_8)
        val encryptedUserData = Base64.encodeToString(aesData, Base64.NO_WRAP)
        println("EncryptedUserDataKey $aesKey")
        println("EncryptedUserData $encryptedUserData")
        return "https://js.tokopedia.com/seamless?" +
                "data=$encryptedUserData" +
                "&os_type=1" +
                "&url=$url"  +
                "&version=${GlobalConfig.VERSION_CODE}" +
                "&timestamp=${System.currentTimeMillis()}" +
                "&browser=1"
    }
}