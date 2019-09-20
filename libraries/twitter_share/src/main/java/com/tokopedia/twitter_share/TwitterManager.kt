package com.tokopedia.twitter_share

import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
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
        private val userSession: UserSessionInterface
) : TwitterAuthenticator.TwitterAuthenticatorListener {

    interface TwitterManagerListener {

        fun onAuthenticationSuccess(oAuthToken: String, oAuthSecret: String)
    }

    companion object {
        const val OAUTH_VERIFIER = "oauth_verifier"
        const val OAUTH_TOKEN = "oauth_token"

        /**
         * Please ask @Radhitia / Socmed team on Slack for the Tokopedia Twitter account login credentials
         * username: Tokopedia
         */
        private val TWITTER_API_KEY = String(byteArrayOf(89, 83, 120, 120, 119, 82, 65, 75, 50, 107, 52, 55, 106, 54, 82, 107, 66, 78, 82, 110, 51, 79, 105, 79, 67))
        private val TWITTER_API_SECRET_KEY = String(byteArrayOf(55, 99, 85, 71, 78, 77, 121, 102, 106, 84, 75, 98, 54, 55, 109, 77, 70, 71, 102, 50, 104, 86, 86, 52, 106, 65, 65, 73, 77, 99, 103, 106, 115, 49, 99, 115, 84, 112, 77, 77, 75, 55, 54, 69, 69, 53, 57, 101, 72, 122))
    }

    private val config: Configuration = ConfigurationBuilder()
            .setOAuthConsumerKey(TWITTER_API_KEY)
            .setOAuthConsumerSecret(TWITTER_API_SECRET_KEY)
            .apply {
                val accessToken = userSession.twitterAccessToken
                val tokenSecret = userSession.twitterAccessTokenSecret

                if (accessToken != null) setOAuthAccessToken(accessToken)
                if (tokenSecret != null) setOAuthAccessTokenSecret(tokenSecret)
            }
            .build()

    val isAuthenticated: Boolean
        get() = userSession.twitterAccessToken != null && userSession.twitterAccessTokenSecret != null

    var shouldPostToTwitter: Boolean
        get() = userSession.twitterShouldPost
        set(value) {
            userSession.twitterShouldPost = value
        }

    private var instance: Twitter = getTwitterInstance()

    private var listener: TwitterManagerListener? = null

    fun setListener(listener: TwitterManagerListener?) {
        this.listener = listener
    }

    override fun onAuthenticateSuccess(twitterInstance: Twitter, requestToken: RequestToken, oAuthToken: String, oAuthVerifier: String) {
        verifyAuthData(twitterInstance, requestToken, oAuthVerifier)
    }

    suspend fun getAuthenticator(): TwitterAuthenticator = withContext(Dispatchers.IO) {
        TwitterAuthenticator(getTwitterInstance(), this@TwitterManager)
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
        userSession.setTwitterAccessTokenAndSecret(accessToken.token, accessToken.tokenSecret)
    }

    /**
     * To show cards when posting tweets, you should add some meta in the directed url
     * Error on duplicate tweets posted
     *
     * @see <a href="https://developer.twitter.com/en/docs/tweets/optimize-with-cards/guides/getting-started">Twitter API Docs</a>
     * @see <a href="https://cards-dev.twitter.com/validator">Twitter Card Validator</a>
     */
    suspend fun postTweet(message: String, fileList: List<File> = emptyList()) = coroutineScope {
        return@coroutineScope doIfAuthenticated {
            val mediaIds = fileList
                    .map { file -> async { uploadMedia(file) } }
                    .map { deferredMedia -> deferredMedia.await().mediaId }
                    .toList()

            return@doIfAuthenticated updateStatus(message, mediaIds.toLongArray())
        }
    }

    private suspend fun updateStatus(message: String, mediaIds: LongArray): Status = withContext(Dispatchers.IO) {
        instance.tweets().updateStatus(
                StatusUpdate(message)
                        .apply {
                            setMediaIds(*mediaIds)
                        }
        )
    }

    private suspend fun uploadMedia(file: File): UploadedMedia = withContext(Dispatchers.IO) {
        instance.tweets().uploadMedia(file)
    }

    private suspend fun<T> doIfAuthenticated(action: suspend () -> T) {
        if (isAuthenticated) withContext(Dispatchers.IO) { action() }
    }
}