package com.tokopedia.twitter_share

import android.content.Context
import android.content.Intent
import com.tokopedia.twitter_share.view.activity.TwitterWebViewActivity
import rx.Subscription
import rx.subjects.PublishSubject
import twitter4j.auth.RequestToken
import java.net.URL

class TwitterAuthenticator(
    private val requestToken: RequestToken,
    private val listener: TwitterAuthenticatorListener
) {

    private lateinit var callbackUrlSubscription: Subscription

    interface TwitterAuthenticatorListener {
        fun onAuthenticateSuccess(requestToken: RequestToken, oAuthToken: String, oAuthVerifier: String)
    }

    companion object {
        private val callbackUrlSubject = PublishSubject.create<String>()

        fun broadcastCallbackUrl(callbackUrl: String) {
            callbackUrlSubject.onNext(callbackUrl)
        }
    }

    private fun URL.getQueryMap(): Map<String, String> {
        val queryList = query.split("&")
        return queryList.associate {
            val splitted = it.split("=")
            splitted.first() to splitted.last()
        }
    }

    private fun processCallbackUrl(urlString: String): Pair<String, String> {
        val url = URL(urlString)
        val queryMap = url.getQueryMap()
        return Pair(queryMap[TwitterManager.OAUTH_TOKEN].orEmpty(), queryMap[TwitterManager.OAUTH_VERIFIER].orEmpty())
    }

    fun startAuthenticate(context: Context) {
        callbackUrlSubscription = initSubscription()

        context.startActivity(Intent().apply {
            putExtra(TwitterWebViewActivity.EXTRA_URL, requestToken.authenticationURL)
        })
    }

    private fun initSubscription(): Subscription {
        return TwitterAuthenticator.callbackUrlSubject.subscribe { url ->
            val (token, verifier) = processCallbackUrl(url)
            if (token.isNotEmpty() && verifier.isNotEmpty()) {
                listener.onAuthenticateSuccess(requestToken, token, verifier)
            }
            if (::callbackUrlSubscription.isInitialized && !callbackUrlSubscription.isUnsubscribed) callbackUrlSubscription.unsubscribe()
        }
    }
}