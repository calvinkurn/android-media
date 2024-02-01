package com.scp.auth.common.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scp.auth.GotoSdk
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSession

object ScpUtils {

    private const val ROLLENCE_KEY_SCP_LOGIN = "scp_goto_login_and"
    private const val ROLLENCE_KEY_PROGRESSIVE_SIGNUP = "and_prog_sign_up_sso"

    fun isGotoLoginEnabled(): Boolean {
        return (RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY_SCP_LOGIN)
            .isNotEmpty() && GlobalConfig.isSellerApp().not())
    }

    fun isProgressiveSignupEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY_PROGRESSIVE_SIGNUP)
            .isNotEmpty() && GlobalConfig.isSellerApp().not()
    }

    fun clearTokens() {
        GotoSdk.LSDKINSTANCE?.updateSsoToken("")
        GotoSdk.LSDKINSTANCE?.save("", "")
    }

    fun updateSsoToken(accessToken: String) {
        GotoSdk.LSDKINSTANCE?.updateSsoToken(accessToken = accessToken)
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        if (isGotoLoginEnabled()) {
            updateSsoToken(accessToken)
            GotoSdk.LSDKINSTANCE?.save(accessToken, refreshToken)
        }
    }

    fun logError(errorType: String, param: Map<String, Any?>) {
        val message = mutableMapOf(
            ScpConstants.ERROR_TYPE to errorType
        )
        for ((key, value) in param) {
            message.put(key, value.toString())
        }
        ServerLogger.log(Priority.P1, ScpConstants.LOGGER_SCP_AUTH_TAG, message)
    }

    fun updateUserSessionLoginMethod(context: Context, loginMethod: String) {
        val userSession = UserSession(context)
        userSession.loginMethod = loginMethod
    }

    fun AppCompatActivity.createNoConnectionBottomSheet(onClick: () -> Unit): BottomSheetUnify {
        val bs = BottomSheetUnify().apply {
            val mView = GlobalError(this@createNoConnectionBottomSheet).apply {
                setType(GlobalError.NO_CONNECTION)
                errorSecondaryAction.visibility = View.GONE
                setPadding(
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    16.toPx()
                )
                setActionClickListener {
                    onClick.invoke()
                    dismiss()
                }
            }
            setChild(mView)
        }
        return bs
    }

    fun AppCompatActivity.createGenericBottomSheet(onClick: () -> Unit): BottomSheetUnify {
        val bs = BottomSheetUnify()
        val mView = GlobalError(this).apply {
            setType(GlobalError.SERVER_ERROR)
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                16.toPx()
            )
            setActionClickListener {
                onClick.invoke()
                bs.dismiss()
            }
        }
        bs.setOnDismissListener {
            onClick.invoke()
        }
        bs.setChild(mView)
        return bs
    }

}
