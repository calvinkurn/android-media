package com.tokopedia.home.util

import android.util.Log
import androidx.fragment.app.Fragment
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.navigation_common.listener.MainParentStateListener
import com.tokopedia.network.exception.MessageErrorException
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.json.JSONObject
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object HomeServerLogger {

    private const val HOME_STATUS_ERROR_TAG = "HOME_STATUS"



    const val TYPE_CANCELLED_INIT_FLOW = "revamp_cancelled_init_flow"
    const val TYPE_REVAMP_ERROR_INIT_FLOW = "revamp_error_init_flow"
    const val TYPE_REVAMP_ERROR_REFRESH = "revamp_error_refresh"
    const val TYPE_REVAMP_EMPTY_UPDATE = "revamp_empty_update"
    const val TYPE_WALLET_APP_ERROR = "wallet_app_error"
    const val TYPE_BALANCE_WIDGET_ERROR = "balance_widget_error"
    const val TYPE_WALLET_BALANCE_EMPTY = "wallet_balance_empty"
    const val TYPE_WALLET_POINTS_EMPTY = "wallet_points_empty"
    const val TYPE_SUBSCRIPTION_ERROR = "balance_widget_subscription_error"
    const val TYPE_RECOM_SET_ADAPTER_ERROR = "recom_set_adapter_error"
    const val TYPE_ERROR_SUBMIT_WALLET = "wallet_app_error_submit"
    const val TYPE_ERROR_ON_LAYOUT_CHILDREN_BALANCE = "error_on_layout_children_balance_adapter"
    const val TYPE_HOME_REPOSITORY_ERROR = "home_repository_error"
    const val TYPE_ERROR_DECLINE_RECHARGE_RECOMMENDATION = "home_decline_recharge_recommendation_error"
    const val TYPE_ERROR_DECLINE_SALAM_RECOMMENDATION = "home_decline_salam_recommendation_error"

    fun warning_error_decline_salam_recommendation(throwable: Throwable?) {
        logWarning(
                type = TYPE_ERROR_DECLINE_SALAM_RECOMMENDATION,
                throwable = throwable,
                reason = (throwable?.message ?: "").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = Log.getStackTraceString(throwable).take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun warning_error_decline_recharge_recommendation(throwable: Throwable?) {
        logWarning(
                type = TYPE_ERROR_DECLINE_RECHARGE_RECOMMENDATION,
                throwable = throwable,
                reason = (throwable?.message ?: "").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = Log.getStackTraceString(throwable).take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun warning_home_repository_error(throwable: Throwable?, modelName: String, repositoryName: String) {
        logWarning(
                type = TYPE_HOME_REPOSITORY_ERROR,
                throwable = throwable,
                reason = ("Error in model: "+ modelName + ", repository: " + repositoryName + ", msg:" +throwable?.message).take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = Log.getStackTraceString(throwable).take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun warning_error_refresh(throwable: Throwable?) {
        logWarning(
                type = TYPE_REVAMP_ERROR_REFRESH,
                throwable = throwable,
                reason = (throwable?.message ?: "").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = Log.getStackTraceString(throwable).take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun warning_empty_channel_update(homeNewDataModel: HomeDynamicChannelModel) {
        val error = "type:" + "revamp_empty_update; " +
                "reason:" + "Visitable is empty; " +
                "isFirstPage:" + homeNewDataModel.isFirstPage.toString() + ";" +
                "isCache:" + homeNewDataModel.isCache.toString()
        logWarning(
                type = TYPE_REVAMP_EMPTY_UPDATE,
                throwable = MessageErrorException(error),
                reason = error,
                data = error
        )
    }

    fun warning_error_flow(throwable: Throwable?) {
        val stackTrace = Log.getStackTraceString(throwable)
        logWarning(
                type = TYPE_REVAMP_ERROR_INIT_FLOW,
                throwable = throwable,
                reason = (throwable?.message ?: "".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                data = stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun warning_error_cancelled_flow(throwable: Throwable?) {
        val stackTrace = if (throwable != null) Log.getStackTraceString(throwable) else ""
        logWarning(
                type = TYPE_CANCELLED_INIT_FLOW,
                throwable = throwable,
                reason = (throwable?.message ?: "No error propagated").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
        )
    }

    fun logWarning(
        type: String?,
        throwable: Throwable?,
        reason: String = "",
        data: String = ""
    ) {
        if (type == null || throwable == null || isExceptionExcluded(throwable)) return
        timberLogWarning(type, ExceptionUtils.getStackTrace(throwable), reason, data)
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    private fun timberLogWarning(type: String, stackTrace: String, reason: String, data: String) {
        ServerLogger.log(Priority.P2, HOME_STATUS_ERROR_TAG, mapOf(
            "type" to type,
            "error" to stackTrace,
            "reason" to reason,
            "data" to data
        ))
    }
}
