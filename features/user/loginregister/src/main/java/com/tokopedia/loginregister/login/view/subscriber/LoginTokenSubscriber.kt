package com.tokopedia.loginregister.login.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 11/06/19.
 */
class LoginTokenSubscriber(val userSession: UserSessionInterface,
                           val onSuccessLoginToken: (pojo: LoginTokenPojo) -> Unit,
                           val onErrorLoginToken: (e: Throwable) -> Unit) :
        Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val pojo = response.getData<LoginTokenPojo>(LoginTokenPojo::class.java)
        if (pojo.loginToken.errors.isEmpty()
                && pojo.loginToken.accessToken.isNotBlank()) {
            saveAccessToken(pojo)
            onSuccessLoginToken(pojo)
        } else if (pojo.loginToken.errors.isNotEmpty()) {
            onErrorLoginToken(Throwable(pojo.loginToken.errors[0].message))
        } else {
            onErrorLoginToken(Throwable())
        }
    }

    private fun saveAccessToken(pojo: LoginTokenPojo?) {
        pojo?.loginToken?.run {
            userSession.setToken(
                    accessToken,
                    tokenType,
                    EncoderDecoder.Encrypt(accessToken, userSession.refreshTokenIV))
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.run {
            onErrorLoginToken(this)
        }
    }


}