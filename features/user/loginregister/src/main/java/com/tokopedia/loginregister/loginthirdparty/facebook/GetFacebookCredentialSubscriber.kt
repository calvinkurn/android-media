package com.tokopedia.loginregister.loginthirdparty.facebook

import com.facebook.AccessToken

/**
 * @author by nisie on 10/11/17.
 */
class GetFacebookCredentialSubscriber(private val viewListener: GetFacebookCredentialListener) {

    interface GetFacebookCredentialListener {
        fun onErrorGetFacebookCredential(errorMessage: Exception?)
        fun onSuccessGetFacebookEmailCredential(accessToken: AccessToken?, email: String?)
        fun onSuccessGetFacebookPhoneCredential(accessToken: AccessToken?, phone: String?)
    }

    fun onError(e: Exception?) {
        viewListener.onErrorGetFacebookCredential(e)
    }

    fun onSuccessEmail(accessToken: AccessToken?, email: String?) {
        viewListener.onSuccessGetFacebookEmailCredential(accessToken, email)
    }

    fun onSuccessPhone(accessToken: AccessToken?, phone: String?) {
        viewListener.onSuccessGetFacebookPhoneCredential(accessToken, phone)
    }

}