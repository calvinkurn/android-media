package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presenter

import android.content.Context
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.activity.OrderHistoryView
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.interactor.OrderHistoryInteractor
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

/**
 * Created by kris on 11/17/17. Tokopedia
 */
class OrderHistoryPresenterImpl(private val orderHistoryInteractor: OrderHistoryInteractor) : OrderHistoryPresenter {

    private var mainView: OrderHistoryView? = null

    fun setMainViewListener(mainView: OrderHistoryView?) {
        this.mainView = mainView
    }

    override fun fetchHistoryData(context: Context, orderId: String, userMode: Int) {
        mainView?.showMainViewLoadingPage()
        val userSession: UserSessionInterface = UserSession(context)
        val temporaryParams = HashMap<String?, String?>()
        temporaryParams["order_id"] = orderId
        temporaryParams["user_id"] = userSession.userId
        temporaryParams["lang"] = "id"
        val params = HashMap<String?, Any?>(temporaryParams)
        params["request_by"] = userMode
        orderHistoryInteractor.requestOrderHistoryData(orderHistorySubscriber, generateParamsNetwork2(context, params))
    }

    override fun onDestroy() {
        orderHistoryInteractor.onViewDestroyed()
    }

    private fun generateParamsNetwork2(context: Context, params: HashMap<String?, Any?>): HashMap<String?, Any?> {
        val userSession: UserSessionInterface = UserSession(context)
        val deviceId = userSession.deviceId
        val userId = userSession.userId
        val hash = md5("$userId~$deviceId")
        params[PARAM_USER_ID] = userId
        params[PARAM_DEVICE_ID] = deviceId
        params[PARAM_HASH] = hash
        params[PARAM_OS_TYPE] = "1"
        params[PARAM_TIMESTAMP] = (Date().time / 1000).toString()
        return params
    }

    private fun md5(s: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()
            for (b in messageDigest) {
                hexString.append(String.format("%02x", b and 0xff.toByte()))
            }
            hexString.toString()
        } catch (e: Throwable) {
            e.printStackTrace()
            ""
        }
    }

    private val orderHistorySubscriber: Subscriber<OrderHistoryData>
        get() = object : Subscriber<OrderHistoryData>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                mainView?.hideMainViewLoadingPage()
                mainView?.onLoadError(e.message)
            }

            override fun onNext(data: OrderHistoryData) {
                mainView?.hideMainViewLoadingPage()
                mainView?.receivedHistoryData(data)
            }
        }

    companion object {
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_DEVICE_ID = "device_id"
        private const val PARAM_HASH = "hash"
        private const val PARAM_OS_TYPE = "os_type"
        private const val PARAM_TIMESTAMP = "device_time"
    }

}