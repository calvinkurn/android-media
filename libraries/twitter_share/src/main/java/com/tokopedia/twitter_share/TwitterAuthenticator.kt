package com.tokopedia.twitter_share

import android.content.Context
import android.content.Intent
import com.tokopedia.twitter_share.view.activity.TwitterWebViewActivity
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import twitter4j.Twitter
import twitter4j.auth.RequestToken
import java.net.URL

class TwitterAuthenticator(
        private val twitterInstance: Twitter,
        private val listener: TwitterAuthenticatorListener
) {

    private val requestToken = twitterInstance.getOAuthRequestToken(CALLBACK_URL)
    private lateinit var callbackUrlSubscription: Subscription

    interface TwitterAuthenticatorListener {
        fun onAuthenticateSuccess(twitterInstance: Twitter, requestToken: RequestToken, oAuthToken: String, oAuthVerifier: String)
    }

    sealed class TwitterAuthenticationState {
        data class Success(val url: String): TwitterAuthenticationState()
        object Cancel: TwitterAuthenticationState()
    }

    companion object {
        private const val CALLBACK_URL = "https://localhost"

        private val callbackUrlSubject = PublishSubject.create<TwitterAuthenticationState>()

        fun broadcastState(state: TwitterAuthenticationState) {
            callbackUrlSubject.onNext(state)
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

        context.startActivity(Intent(context, TwitterWebViewActivity::class.java).apply {
            putExtra(TwitterWebViewActivity.EXTRA_URL, requestToken.authenticationURL)
        })
    }

    private fun initSubscription(): Subscription {
        return TwitterAuthenticator.callbackUrlSubject
                .observeOn(Schedulers.io())
                .subscribe { state ->
                    when (state) {
                        is TwitterAuthenticationState.Success -> {
                            val (token, verifier) = processCallbackUrl(state.url)
                            if (token.isNotEmpty() && verifier.isNotEmpty()) {
                                listener.onAuthenticateSuccess(twitterInstance, requestToken, token, verifier)
                            }

                        }
                        is TwitterAuthenticationState.Cancel -> {

                        }
                    }
                    if (::callbackUrlSubscription.isInitialized && !callbackUrlSubscription.isUnsubscribed) callbackUrlSubscription.unsubscribe()
                }
    }
}