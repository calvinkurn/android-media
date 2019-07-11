package com.tokopedia.twitter_share

import android.content.SharedPreferences
import com.tokopedia.twitter_share.session.TwitterSession
import rx.Observable
import rx.schedulers.Schedulers
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder
import java.io.File

class TwitterManager(
    twitterPrefs: SharedPreferences
) : TwitterAuthenticator.TwitterAuthenticatorListener {

    interface TwitterManagerListener {

        fun onAuthenticationSuccess(oAuthToken: String, oAuthSecret: String)
    }

    companion object {
        const val OAUTH_VERIFIER = "oauth_verifier"
        const val OAUTH_TOKEN = "oauth_token"

        private const val CALLBACK_URL = "https://localhost"
    }

    private val session: TwitterSession = TwitterSession(twitterPrefs)

    private val config: Configuration = ConfigurationBuilder()
            .setOAuthConsumerKey(BuildConfig.TWITTER_API_KEY)
            .setOAuthConsumerSecret(BuildConfig.TWITTER_API_SECRET_KEY)
            .apply {
                val accessToken = session.getAccessToken()
                val tokenSecret = session.getAccessTokenSecret()

                if (accessToken != null) setOAuthAccessToken(accessToken)
                if (tokenSecret != null) setOAuthAccessTokenSecret(tokenSecret)
            }
            .build()

    private val isAuthenticated: Boolean
        get() = config.oAuthAccessToken != null && config.oAuthAccessTokenSecret != null

    private var instance: Twitter = TwitterFactory(config).instance

    private var listener: TwitterManagerListener? = null

    fun setListener(listener: TwitterManagerListener?) {
        this.listener = listener
    }

    override fun onAuthenticateSuccess(requestToken: RequestToken, oAuthToken: String, oAuthVerifier: String) {
        verifyAuthData(requestToken, oAuthVerifier)
    }

    fun getAuthenticator(): Observable<TwitterAuthenticator> {
        return Observable.fromCallable {
            TwitterAuthenticator(getRequestTokenInstance(), this)
        }.subscribeOn(Schedulers.io())
    }

    private fun verifyAuthData(requestToken: RequestToken, oAuthVerifier: String) {
        val accessToken = getAccessToken(requestToken, oAuthVerifier)
        saveAccessToken(accessToken)
        listener?.onAuthenticationSuccess(accessToken.token, accessToken.tokenSecret)
    }

    private fun getRequestTokenInstance(): RequestToken {
        return instance.getOAuthRequestToken(CALLBACK_URL)
    }

    private fun getAccessToken(requestToken: RequestToken, oAuthVerifier: String): AccessToken {
        return instance.getOAuthAccessToken(requestToken, oAuthVerifier)
    }

    private fun saveAccessToken(accessToken: AccessToken) {
        session.setAccessTokenAndSecret(accessToken.token, accessToken.tokenSecret)
    }

    fun postTweet(message: String, fileList: List<File>): Observable<Status> {
        return doIfAuthenticated {
            return@doIfAuthenticated Observable.from(fileList)
                    .concatMap(::uploadMedia)
                    .map(UploadedMedia::getMediaId)
                    .toList()
                    .flatMap { ids -> updateStatus(message, ids.toLongArray()) }
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun updateStatus(message: String, mediaIds: LongArray): Observable<Status> {
        return Observable.fromCallable {
            instance.tweets().updateStatus(
                    StatusUpdate(message)
                            .apply {
                                setMediaIds(*mediaIds)
                            }
            )
        }
    }

    private fun uploadMedia(file: File): Observable<UploadedMedia> {
        return Observable.fromCallable { instance.tweets().uploadMedia(file) }
    }

    private fun<T> doIfAuthenticated(action: () -> Observable<T>): Observable<T> {
        return if (isAuthenticated) action()
        else Observable.error(IllegalStateException("User not authenticated"))
    }
}