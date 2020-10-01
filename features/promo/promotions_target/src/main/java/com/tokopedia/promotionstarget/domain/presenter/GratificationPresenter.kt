package com.tokopedia.promotionstarget.domain.presenter

//import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import android.app.Activity
import android.app.Application
import android.util.Log
import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.usecase.NotificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.TokopointsCouponDetailUseCase
import com.tokopedia.promotionstarget.presentation.ui.dialog.CmGratificationDialog
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class GratificationPresenter @Inject constructor(val application: Application) {
    @Inject
    lateinit var notificationUseCase: NotificationUseCase

    @Inject
    lateinit var tpCouponDetailUseCase: TokopointsCouponDetailUseCase


    @Inject
    @field:Named(IO)
    lateinit var worker: CoroutineDispatcher

    @Inject
    @field:Named(MAIN)
    lateinit var uiWorker: CoroutineDispatcher

    private var job: Job? = null
    private var scope: CoroutineScope? = null
    var exceptionCallback:ExceptionCallback?=null


    init {
        DaggerCmGratificationPresenterComponent.builder()
                .appModule(AppModule(application))
                .build()
                .inject(this)
    }


    private fun getNotification(weakActivity: WeakReference<Activity>,
                                notificationID: Int,
                                @NotificationEntryType notificationEntryType: Int,
                                paymentID: Int = 0,
                                gratifPopupCallback: GratifPopupCallback? = null) {
        scope?.launch(worker + ceh) {
            val map = notificationUseCase.getQueryParams(notificationID, notificationEntryType, paymentID)
            val notifResponse = notificationUseCase.getResponse(map)
//            val notifResponse = notificationUseCase.getFakeResponse(map)
            //todo Rahul verify key later
            val reason = notifResponse.response?.resultStatus?.code
            if (reason == GratifResultStatus.SUCCESS) {
                //todo Rahul refactor later
//                val code = notifResponse.response.promoCode
                val code = "NUPLBDAY5D7RUU5M329"
                if (!code.isNullOrEmpty()) {
                    val couponDetail = tpCouponDetailUseCase.getResponse(tpCouponDetailUseCase.getQueryParams(code))
//                    val couponDetail = tpCouponDetailUseCase.getFakeResponse(tpCouponDetailUseCase.getQueryParams(code))
                    withContext(uiWorker) {
                        weakActivity.get()?.let {
                            val dialog = CmGratificationDialog().show(it, notifResponse.response, couponDetail, notificationEntryType)
                            dialog?.setOnShowListener {
                                gratifPopupCallback?.onShow()
                            }
                            dialog?.setOnDismissListener {
                                gratifPopupCallback?.onDismiss()
                            }
                        }
                    }
                }else{
                    gratifPopupCallback?.onDismiss()
                }
            }else{
                gratifPopupCallback?.onDismiss()
            }
        }
    }


    fun showGratificationInApp(weakActivity: WeakReference<Activity>,
                               gratificationId: String?,
                               @NotificationEntryType notificationEntryType: Int,
                               gratifPopupCallback: GratifPopupCallback
    ) {
        Log.d("NOOB", "showGratificationInApp")
        try {

            synchronized(this) {
                initSafeJob()
                initSafeScope()
            }
            if (!gratificationId.isNullOrEmpty()) {
                getNotification(weakActivity, gratificationId.toInt(), notificationEntryType,gratifPopupCallback = gratifPopupCallback)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun initSafeScope() {
        try {
            if (scope == null) {
                if (job != null) {
                    scope = CoroutineScope(job!!)
                }
            }
        } catch (th: Throwable) {
        }
    }

    private fun initSafeJob() {
        try {
            if (job == null) {
                job = SupervisorJob()
            }
        } catch (th: Throwable) {
        }
    }

    private val ceh = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        exceptionCallback?.onError(exception)
    }

    fun dismissGratification() {}

    interface GratifPopupCallback {
        fun onShow()
        fun onDismiss()
    }

    interface ExceptionCallback{
        fun onError(th:Throwable?)
    }
}