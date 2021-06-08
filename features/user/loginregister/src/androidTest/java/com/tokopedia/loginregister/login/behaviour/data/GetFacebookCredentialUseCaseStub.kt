package com.tokopedia.loginregister.login.behaviour.data

import android.os.Parcel
import com.facebook.AccessToken
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.usecase.RequestParams

class GetFacebookCredentialUseCaseStub: GetFacebookCredentialUseCase() {
    var isEmail = true

    var accessToken = AccessToken(
            "abc123",
            "123",
            "userId1234",
            null, null, null, null, null, null, null
    )

    override fun execute(requestParams: RequestParams, subscriber: GetFacebookCredentialSubscriber) {
        if(isEmail) {
            subscriber.onSuccessEmail(accessToken = accessToken, email = "yoris.prayogo@tokopedia.com")
        }else {
            subscriber.onSuccessPhone(accessToken, "082242454511")
        }
    }
}