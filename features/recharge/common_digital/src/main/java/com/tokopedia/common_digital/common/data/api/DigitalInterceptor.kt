package com.tokopedia.common_digital.common.data.api

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common_digital.common.DigitalAtcErrorException
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.authentication.AuthKeyExt
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

/**
 * Created by Rizky on 16/08/18.
 */
class DigitalInterceptor(@ApplicationContext val context: Context,
                         networkRouter: NetworkRouter,
                         userSessionInterface: UserSessionInterface) : TkpdOldAuthInterceptor(context, networkRouter, userSessionInterface) {

    private val digitalAuthKey: String
        get() = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            AuthKeyExt.RECHARGE_HMAC_API_KEY_STAGING
        } else {
            AuthKeyExt.RECHARGE_HMAC_API_KEY_PROD
        }

    @Throws(IOException::class)
    override fun throwChainProcessCauseHttpError(response: Response) {
        val errorBody = response.peekBody(BYTE_COUNT.toLong()).string()
        Timber.d(TAG, "Error body response : $errorBody")
        if (errorBody.isNotEmpty()) {
            val digitalErrorResponse: TkpdDigitalResponse.DigitalErrorResponse =
                TkpdDigitalResponse.DigitalErrorResponse.factory(errorBody, response.code)
            if (digitalErrorResponse.typeOfError == TkpdDigitalResponse.DigitalErrorResponse.ERROR_DIGITAL) {
                throw DigitalAtcErrorException(errorBody)
            } else if (digitalErrorResponse.typeOfError == TkpdDigitalResponse.DigitalErrorResponse.ERROR_SERVER) {
                if (digitalErrorResponse.status.equals(
                        DigitalError.STATUS_UNDER_MAINTENANCE,
                        true
                    )
                ) {
                    throw ResponseErrorException(digitalErrorResponse.serverErrorMessageFormatted)
                } else if (digitalErrorResponse.status.equals(
                        DigitalError.STATUS_REQUEST_DENIED,
                        true
                    )
                ) {
                    throw ResponseErrorException(digitalErrorResponse.serverErrorMessageFormatted)
                } else if (digitalErrorResponse.status.equals(
                        DigitalError.STATUS_FORBIDDEN,
                        true
                    ) &&
                    MethodChecker.isTimezoneNotAutomatic(context)
                ) {
                    throw ResponseErrorException(digitalErrorResponse.serverErrorMessageFormatted)
                } else {
                    throw HttpErrorException(response.code)
                }
            } else {
                throw HttpErrorException(response.code)
            }
        }
        throw HttpErrorException(response.code)
    }

    override fun getHeaderMap(
            path: String, strParam: String, method: String, authKey: String, contentTypeHeader: String
    ): Map<String, String> {
        val header = AuthUtil.generateHeadersWithXUserId(
            path, strParam, method, authKey, contentTypeHeader, userSession.userId, userSession,
            headerTheme
        )

        // replace with Digital's auth organization key
        header[AuthUtil.HEADER_AUTHORIZATION] = header[AuthUtil.HEADER_AUTHORIZATION]?.replace(DEFAULT_TOKOPEDIA_ORGANIZATION_NAME, digitalAuthOrganization)
        return header
    }

    private val digitalAuthOrganization: String
        get() = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            AuthKeyExt.RECHARGE_HMAC_API_ORGANIZATION_STAGING
        } else {
            AuthKeyExt.RECHARGE_HMAC_API_ORGANIZATION_PROD
        }

    companion object {
        private val TAG = DigitalInterceptor::class.java.simpleName
        private const val DEFAULT_TOKOPEDIA_ORGANIZATION_NAME = "Tokopedia"
    }

    init {
        authKey = digitalAuthKey
    }
}
