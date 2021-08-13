package com.tokopedia.loginregister.common.data

import com.facebook.AccessToken
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import rx.Subscriber

/**
 * Created by Ade Fulki on 2019-10-15.
 * ade.hadian@tokopedia.com
 */

object ResponseConverter{

    fun <T : Any> resultUsecaseCoroutineToSubscriber(
            onSuccessResult: (T) -> Unit,
            onErrorResult: (Throwable) -> Unit
    ): Subscriber<T> {

        return object : Subscriber<T>(){

            override fun onNext(t: T) = onSuccessResult(t)

            override fun onError(e: Throwable) = onErrorResult(e)

            override fun onCompleted() { }
        }
    }

    fun resultUsecaseCoroutineToFacebookCredentialListener(
            onSuccessEmailResult: (FacebookCredentialData) -> Unit,
            onSuccessPhoneResult: (FacebookCredentialData) -> Unit,
            onErrorResult: (Throwable) -> Unit
    ): GetFacebookCredentialSubscriber.GetFacebookCredentialListener{

        return object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener{
            override fun onErrorGetFacebookCredential(errorMessage: Exception?) {
                if(errorMessage != null) onErrorResult(errorMessage)
            }

            override fun onSuccessGetFacebookEmailCredential(accessToken: AccessToken?, email: String?) {
                LetUtil.ifLet(accessToken, email) { (accessToken, email) ->
                    onSuccessEmailResult(FacebookCredentialData(accessToken as AccessToken, email as String, ""))
                }
            }

            override fun onSuccessGetFacebookPhoneCredential(accessToken: AccessToken?, phone: String?) {
                LetUtil.ifLet(accessToken, phone) { (accessToken, phone) ->
                    onSuccessPhoneResult(FacebookCredentialData(accessToken as AccessToken, "", phone as String))
                }
            }
        }
    }
}