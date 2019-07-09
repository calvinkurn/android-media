package com.tokopedia.twitter_share

import twitter4j.auth.RequestToken
import java.net.URL

class TwitterAuthenticator(
    val requestToken: RequestToken
) {

    companion object {
        private const val OAUTH_VERIFIER = "oauth_verifier"
    }

    private lateinit var callbackUrl: String

    fun getAuthenticationUrl(): String = requestToken.authenticationURL

    fun setCallbackUrl(callbackUrl: String) {
        this.callbackUrl = callbackUrl
    }

    fun getOAuthVerifier(): String {
        val url = URL(callbackUrl)
        return url.getQueryMap()[OAUTH_VERIFIER] ?: throw IllegalStateException("$OAUTH_VERIFIER must not be null")
    }

    private fun URL.getQueryMap(): Map<String, String> {
        val queryList = query.split("&")
        return queryList.associate {
            val splitted = it.split("=")
            splitted.first() to splitted.last()
        }
    }
}