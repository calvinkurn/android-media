package com.scp.auth.common.utils

import android.content.Context
import com.scp.auth.GotoSdk
import com.scp.login.core.domain.contracts.listener.LSdkRefreshCompleteListener
import com.tokopedia.network.data.model.ScpTokenModel

class ScpRefreshHelper(
    private val application: Context
) {

    @Synchronized
    fun refreshToken(): ScpTokenModel {
        val isSuccess = refreshWithGotoSdk()
        return if (isSuccess) {
            val accessToken = GotoSdk.LSDKINSTANCE?.getAccessToken()
            val refreshToken = GotoSdk.LSDKINSTANCE?.getRefreshToken()
            ScpTokenModel(accessToken, refreshToken)
        } else {
            ScpTokenModel("", "")
        }
    }

    private fun refreshWithGotoSdk(): Boolean {
        return GotoSdk.LSDKINSTANCE?.refreshToken(
            additionalHeaders = TkpdAdditionalHeaders(application),
            refreshCompletionListener = object : LSdkRefreshCompleteListener {
                override fun onRefreshCompleted(accessToken: String?) {}
            }
        ) ?: false
    }
}
