package com.tokopedia.loginregister.common.data

import com.facebook.AccessToken
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import rx.Subscriber
import java.lang.Exception

/**
 * Created by Ade Fulki on 2019-10-15.
 * ade.hadian@tokopedia.com
 */

object ResponseConverter{

    fun <T : kotlin.Any> resultUsecaseCoroutineToSubscriber(
            onSuccessResult: (T) -> kotlin.Unit,
            onErrorResult: (kotlin.Throwable) -> kotlin.Unit
    ): Subscriber<T> {

        return object : Subscriber<T>(){

            override fun onNext(t: T) = onSuccessResult(t)

            override fun onError(e: Throwable) = onErrorResult(e)

            override fun onCompleted() { }
        }
    }

    fun resultUsecaseCoroutineToFacebookCredentialListener(
            onSuccessResult: (FacebookCredentialData) -> kotlin.Unit,
            onErrorResult: (kotlin.Throwable) -> kotlin.Unit
    ): GetFacebookCredentialSubscriber.GetFacebookCredentialListener{

        return object : GetFacebookCredentialSubscriber.GetFacebookCredentialListener{
            override fun onErrorGetFacebookCredential(errorMessage: Exception) = onErrorResult(errorMessage)

            override fun onSuccessGetFacebookCredential(accessToken: AccessToken, email: String)
                    = onSuccessResult(FacebookCredentialData(accessToken, email))

        }
    }
}