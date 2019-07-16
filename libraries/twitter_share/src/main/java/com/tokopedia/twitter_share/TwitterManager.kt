package com.tokopedia.twitter_share

import android.content.SharedPreferences
import com.tokopedia.twitter_share.session.TwitterSession
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
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

    val isAuthenticated: Boolean
        get() = session.getAccessToken() != null && session.getAccessTokenSecret() != null

    var shouldPostToTwitter: Boolean
        get() = session.shouldPostToTwitter
        set(value) {
            session.shouldPostToTwitter = value
        }

    private var instance: Twitter = getTwitterInstance()

    private var listener: TwitterManagerListener? = null

    fun setListener(listener: TwitterManagerListener?) {
        this.listener = listener
    }

    override fun onAuthenticateSuccess(twitterInstance: Twitter, requestToken: RequestToken, oAuthToken: String, oAuthVerifier: String) {
        verifyAuthData(twitterInstance, requestToken, oAuthVerifier)
    }

    fun getAuthenticator(): Observable<TwitterAuthenticator> {
        return Observable.fromCallable {
            TwitterAuthenticator(getTwitterInstance(), this)
        }.subscribeOn(Schedulers.io())
    }

    private fun verifyAuthData(twitterInstance: Twitter, requestToken: RequestToken, oAuthVerifier: String) {
        Observable.fromCallable {
            getAccessToken(requestToken, oAuthVerifier)
        }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { accessToken ->
            instance = twitterInstance
            saveAccessToken(accessToken)
            listener?.onAuthenticationSuccess(accessToken.token, accessToken.tokenSecret)
        }
    }

    private fun getTwitterInstance() = TwitterFactory(config).instance

    private fun getAccessToken(requestToken: RequestToken, oAuthVerifier: String): AccessToken {
        return instance.getOAuthAccessToken(requestToken, oAuthVerifier)
    }

    private fun saveAccessToken(accessToken: AccessToken) {
        session.setAccessTokenAndSecret(accessToken.token, accessToken.tokenSecret)
    }

    /**
     * To show cards when posting tweets, you should add some meta in the directed url
     * Error on duplicate tweets posted
     *
     * @see <a href="https://developer.twitter.com/en/docs/tweets/optimize-with-cards/guides/getting-started">Twitter API Docs</a>
     * @see <a href="https://cards-dev.twitter.com/validator">Twitter Card Validator</a>
     */
    fun postTweet(message: String, fileList: List<File> = emptyList()): Observable<Status> {
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