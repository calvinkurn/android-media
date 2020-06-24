package com.tokopedia.network.interceptor

import android.content.Context
import com.tokopedia.authentication.AuthHelper.Companion.generateHeaders
import com.tokopedia.authentication.AuthKey
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.network.utils.CommonUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Named

/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
open class TkpdAuthInterceptor : TkpdBaseInterceptor {
    private var context: Context
    @JvmField
    var userSession: UserSessionInterface
    var authKey: String
    var networkRouter: NetworkRouter

    @Deprecated("") /*
      use interface instead.
     */
    constructor(context: Context,
                networkRouter: NetworkRouter,
                userSession: UserSession) {
        this.context = context
        this.networkRouter = networkRouter
        this.userSession = userSession
        authKey = AuthKey.KEY_WSV4_NEW
    }

    constructor(context: Context,
                networkRouter: NetworkRouter,
                @Named("Session") userSession: UserSessionInterface) {
        this.context = context
        this.networkRouter = networkRouter
        this.userSession = userSession
        authKey = AuthKey.KEY_WSV4_NEW
    }

    constructor(context: Context,
                networkRouter: NetworkRouter,
                userSession: UserSessionInterface,
                authKey: String) {
        this.context = context
        this.networkRouter = networkRouter
        this.userSession = userSession
        this.authKey = authKey
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = chain.request().newBuilder()

        generateHmacAuthRequest(originRequest, newRequest)
        val finalRequest = newRequest.build()
        var response = getResponse(chain, finalRequest)

        if (!response.isSuccessful) {
            throwChainProcessCauseHttpError(response)
        }
        response = checkForceLogout(chain, response, finalRequest)
        checkResponse(response)
        return response
    }

    protected fun checkResponse(response: Response) {
        val bodyResponse: String
        try {
            // Improvement for response, only check maintenance, server error and timezone by only peeking the body
            // instead of getting all string and create the new response.
            bodyResponse = response.peekBody(BYTE_COUNT.toLong()).string()
            if (isMaintenance(bodyResponse)) {
                showMaintenancePage()
            } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
                showServerError(response)
            } else if (isForbiddenRequest(bodyResponse, response.code())
                    && isTimezoneNotAutomatic) {
                showTimezoneErrorSnackbar()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun throwChainProcessCauseHttpError(response: Response?) { /* this can override for throw error */ }

    protected val isTimezoneNotAutomatic: Boolean
        protected get() = CommonUtils.isTimezoneNotAutomatic(context)

    protected fun isForbiddenRequest(bodyResponse: String?, code: Int): Boolean {
        // improvement for by java.lang.OutOfMemoryError
        // at java.lang.AbstractStringBuilder.enlargeBuffer(AbstractStringBuilder.java:94)
        // do not parse too much JSONObject for checking forbidden request
        return try {
            if (code != ERROR_FORBIDDEN_REQUEST) {
                return false
            }
            val json = JSONObject(bodyResponse)
            val status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK)
            status == RESPONSE_STATUS_FORBIDDEN
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Throws(IOException::class)
    protected open fun generateHmacAuthRequest(originRequest: Request, newRequest: Request.Builder) {
        var authHeaders = prepareHeader(originRequest)
        generateHeader(authHeaders, originRequest, newRequest)
    }

    fun prepareHeader(originRequest: Request): Map<String, String> {
        var authHeaders = mapOf<String, String>()
        var contentTypeHeader: String? = null
        if (REQUEST_METHOD_GET != originRequest.method()
                && originRequest.body() != null && originRequest.body()?.contentType() != null) {
            contentTypeHeader = originRequest.body()?.contentType().toString()
        }
        if (REQUEST_METHOD_GET.equals(originRequest.method(), ignoreCase = true)) {
            contentTypeHeader = ""
        }
        when (originRequest.method()) {
            REQUEST_METHOD_PATCH, REQUEST_METHOD_DELETE, REQUEST_METHOD_POST, REQUEST_METHOD_PUT -> authHeaders = getHeaderMap(
                    originRequest.url().uri().path,
                    generateParamBodyString(originRequest),
                    originRequest.method(),
                    authKey,
                    contentTypeHeader
            )
            REQUEST_METHOD_GET -> authHeaders = getHeaderMap(
                    originRequest.url().uri().path,
                    generateQueryString(originRequest),
                    originRequest.method(),
                    authKey,
                    contentTypeHeader
            )
        }
        return authHeaders
    }

    protected open fun getHeaderMap(
            path: String, strParam: String, method: String, authKey: String, contentTypeHeader: String?): Map<String, String> {
        return generateHeaders(path, strParam, method, authKey, contentTypeHeader,
                userSession.userId, userSession)
    }

    protected open fun generateHeader(authHeaders: Map<String, String>, originRequest: Request, newRequest: Request.Builder) {
        for ((key, value) in authHeaders) {
            newRequest.addHeader(key, value)
        }
        newRequest.method(originRequest.method(), originRequest.body())
    }

    private fun generateParamBodyString(request: Request): String {
        var paramBody = ""
        try {
            if (request.body() != null) {
                val buffer = Buffer()
                request.body()?.writeTo(buffer)
                paramBody = buffer.readUtf8()
            }
        } catch (e: IOException) {
            paramBody = ""
        }
        return paramBody
    }

    private fun generateQueryString(request: Request): String {
        val query = request.url().query()
        return query ?: ""
    }

    protected fun isMaintenance(response: String): Boolean {
        return response.toUpperCase().contains(RESPONSE_STATUS_UNDER_MAINTENANCE)
    }

    protected fun isHasErrorMessage(response: String?): Boolean {
        val json: JSONObject
        return try {
            json = JSONObject(response)
            val errorMessage = json.optJSONArray(RESPONSE_PARAM_MESSAGE_ERROR)
            errorMessage.length() > 0
        } catch (e: JSONException) {
            e.printStackTrace()
            false
        }
    }

    protected fun isServerError(code: Int): Boolean = code >= 500

    protected fun showTimezoneErrorSnackbar() {
        networkRouter.showTimezoneErrorSnackbar()
    }

    protected fun showMaintenancePage() {
        networkRouter.showMaintenancePage()
    }

    protected fun showServerError(response: Response?) {
        networkRouter.showServerError(response)
    }

    protected fun isNeedGcmUpdate(response: Response): Boolean {
        return try { //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            val responseString = response.peekBody(512).string()
            responseString.toUpperCase().contains(RESPONSE_STATUS_REQUEST_DENIED) &&
                    !response.request().url().encodedPath().contains(RESPONSE_PARAM_MAKE_LOGIN)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    protected fun isUnauthorized(request: Request, response: Response): Boolean {
        return try { //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            val responseString = response.peekBody(512).string()
            (responseString.toUpperCase().contains(RESPONSE_STATUS_INVALID_REQUEST)
                    && request.header(HEADER_PARAM_AUTHORIZATION)?.contains(HEADER_PARAM_BEARER) == true)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun isInvalidGrantWhenRefreshToken(request: Request, response: Response): Boolean {
        return try {
            val responseString = response.peekBody(512).string()
            (responseString.toUpperCase().contains(RESPONSE_STATUS_INVALID_GRANT)
                    && response.request().url().encodedPath().contains(RESPONSE_PARAM_TOKEN)
                    && request.toString().contains(REQUEST_PARAM_REFRESH_TOKEN))
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    @Throws(IOException::class)
    @Synchronized
    protected fun refreshTokenAndGcmUpdate(chain: Interceptor.Chain, response: Response, finalRequest: Request): Response {
        val accessTokenRefresh = AccessTokenRefresh()
        return try {
            val newAccessToken = accessTokenRefresh.refreshToken(context, userSession, networkRouter)
            networkRouter.doRelogin(newAccessToken)
            val newestRequest: Request = if (finalRequest.header(AUTHORIZATION)?.contains(BEARER) == true) {
                recreateRequestWithNewAccessToken(chain)
            } else {
                recreateRequestWithNewAccessTokenAccountsAuth(chain)
            }
            if (isUnauthorized(newestRequest, response) || isNeedGcmUpdate(response)) {
                networkRouter.sendForceLogoutAnalytics(response, isUnauthorized(newestRequest,
                        response), isNeedGcmUpdate(response))
            }
            chain.proceed(newestRequest)
        } catch (e: IOException) {
            e.printStackTrace()
            response
        }
    }

    protected fun refreshToken(chain: Interceptor.Chain, response: Response): Response {
        val accessTokenRefresh = AccessTokenRefresh()
        return try {
            accessTokenRefresh.refreshToken(context, userSession, networkRouter)
            val newest = recreateRequestWithNewAccessToken(chain)
            chain.proceed(newest)
        } catch (e: IOException) {
            e.printStackTrace()
            response
        }
    }

    @Throws(IOException::class)
    protected fun checkForceLogout(chain: Interceptor.Chain, response: Response, finalRequest: Request): Response {
        return try {
            if (isNeedGcmUpdate(response) || (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED && userSession.isLoggedIn)) {
                refreshTokenAndGcmUpdate(chain, response, finalRequest)
            } else if (isUnauthorized(finalRequest, response)) {
                refreshToken(chain, response)
            } else if (isInvalidGrantWhenRefreshToken(finalRequest, response)) {
                networkRouter.logInvalidGrant(response)
                response
            } else {
                response
            }
        } catch (e: IOException) {
            e.printStackTrace()
            response
        }
    }

    private fun recreateRequestWithNewAccessToken(chain: Interceptor.Chain): Request {
        val freshAccessToken = userSession.accessToken
        val newRequest = chain.request().newBuilder()

        try {
            generateHmacAuthRequest(chain.request(), newRequest)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return newRequest
                .header(HEADER_PARAM_AUTHORIZATION, "$HEADER_PARAM_BEARER $freshAccessToken")
                .header(HEADER_ACCOUNTS_AUTHORIZATION, "$HEADER_PARAM_BEARER $freshAccessToken")
                .build()
    }

    private fun recreateRequestWithNewAccessTokenAccountsAuth(chain: Interceptor.Chain): Request {
        val freshAccessToken = userSession.accessToken
        val newRequest = chain.request().newBuilder()
        try {
            generateHmacAuthRequest(chain.request(), newRequest)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return newRequest
                .header(HEADER_ACCOUNTS_AUTHORIZATION, "$HEADER_PARAM_BEARER $freshAccessToken")
                .build()
    }

    companion object {
        private const val ERROR_FORBIDDEN_REQUEST = 403
        private const val ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR"
        private const val REQUEST_METHOD_GET = "GET"
        private const val REQUEST_METHOD_POST = "POST"
        private const val REQUEST_METHOD_PATCH = "PATCH"
        private const val REQUEST_METHOD_DELETE = "DELETE"
        private const val REQUEST_METHOD_PUT = "PUT"
        private const val RESPONSE_STATUS_OK = "OK"
        private const val RESPONSE_STATUS_FORBIDDEN = "FORBIDDEN"
        private const val RESPONSE_STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE"
        private const val RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED"
        private const val RESPONSE_STATUS_INVALID_REQUEST = "INVALID_REQUEST"
        private const val RESPONSE_STATUS_INVALID_GRANT = "INVALID_GRANT"
        private const val HEADER_PARAM_AUTHORIZATION = "authorization"
        protected const val HEADER_PARAM_BEARER = "Bearer"
        private const val RESPONSE_PARAM_MAKE_LOGIN = "make_login"
        private const val RESPONSE_PARAM_STATUS = "status"
        private const val RESPONSE_PARAM_MESSAGE_ERROR = "message_error"
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "authorization"
        private const val TOKEN = "token"
        private const val HEADER_ACCOUNTS_AUTHORIZATION = "accounts-authorization"
        private const val HEADER_PARAM_IS_BETA = "is_beta"
        private const val PARAM_DEFAULT_BETA = "0"
        private const val PARAM_BETA_TRUE = "1"
        private const val RESPONSE_PARAM_TOKEN = "token"
        private const val REQUEST_PARAM_REFRESH_TOKEN = "refresh_token"
        const val BYTE_COUNT = 512
    }
}