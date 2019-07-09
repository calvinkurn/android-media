package com.tokopedia.twitter_share

import android.content.Context
import com.tokopedia.twitter_share.session.TwitterPreference
import com.tokopedia.twitter_share.session.TwitterSession
import rx.Observable
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder
import java.io.File

class TwitterManager(
    context: Context,
    apiKey: String = "lDTUm7KOSe0hPJvmIxz8VbSYR",
    apiSecretKey: String = "Rrk7C9SJCEY3Grf3QuBvEUSPceu7K9UGMVoA6XJuXjTv1FkBT9"
) {

    companion object {
        private const val CALLBACK_URL = "https://localhost"
    }

    private val session: TwitterSession = TwitterSession(
            TwitterPreference.getSharedPreferences(context)
    )

    private val config: Configuration = ConfigurationBuilder()
            .setOAuthConsumerKey(apiKey)
            .setOAuthConsumerSecret(apiSecretKey)
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

    fun getAuthenticator(): TwitterAuthenticator {
        return TwitterAuthenticator(getRequestTokenInstance())
    }

    fun processAuthenticator(authenticator: TwitterAuthenticator) {
        val oAuthVerifier = authenticator.getOAuthVerifier()
        val accessToken = getAccessToken(authenticator.requestToken, oAuthVerifier)
        saveAccessToken(accessToken)
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