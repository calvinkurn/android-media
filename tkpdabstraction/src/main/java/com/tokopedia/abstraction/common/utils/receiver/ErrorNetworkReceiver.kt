package com.tokopedia.abstraction.common.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.abstraction.common.utils.receiver.ErrorNetworkReceiver.ReceiveListener
import android.content.Intent
import com.tokopedia.abstraction.common.utils.receiver.ErrorNetworkReceiver
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.util.HashMap

/**
 * Created by ricoharisin on 7/26/16.
 */
class ErrorNetworkReceiver : BroadcastReceiver() {
    private var mReceiver: ReceiveListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (mReceiver != null && intent != null) {
            val action = intent.action
            var accessToken: String? = ""
            var path: String? = ""
            if (intent.getStringExtra(ACCESS_TOKEN) != null) {
                accessToken = intent.getStringExtra(ACCESS_TOKEN)
            }
            if (intent.getStringExtra(PATH) != null) {
                path = intent.getStringExtra(PATH)
            }
            val messageMap = mutableMapOf(
                "type" to "ErrorNetworkReceiver",
                "action" to action.orEmpty(),
                "accessToken" to accessToken.orEmpty()
            )
            messageMap["path"] = path ?: "/"

            if (path?.startsWith("FORCE_LOGOUT_INFO") == true) {
                messageMap["path"] = "/"
            } else {
                messageMap["path"] = path ?: "/"
            }

            log(Priority.P1, "BROADCAST_RECEIVER", messageMap)

            if (action == null) {
                return
            }

            when (action) {
                "com.tokopedia.tkpd.FORCE_LOGOUT" -> {
                    mReceiver?.onForceLogout()
                }
                "com.tokopedia.tkpd.SERVER_ERROR" -> {
                    mReceiver?.onServerError()
                }
                "com.tokopedia.tkpd.TIMEZONE_ERROR" -> {
                    mReceiver?.onTimezoneError()
                }
            }
        }
    }

    interface ReceiveListener {
        fun onForceLogout()
        fun onServerError()
        fun onTimezoneError()
    }

    fun setReceiver(receiver: ReceiveListener?) {
        mReceiver = receiver
    }

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        private const val PATH = "path"
    }
}