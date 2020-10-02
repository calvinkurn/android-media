package com.tokopedia.promotionstarget.domain.presenter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.ACTIVITY_STOP
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_DESTROYED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_RESUME
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_SELECTED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_STARTED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_STOP
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.COUPON_CODE_EMPTY
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.NO_SUCCESS
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.UNKOWN_ERROR
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
    var exceptionCallback: ExceptionCallback? = null


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
                                gratifPopupCallback: GratifPopupCallback? = null): Job? {
        return scope?.launch(worker + ceh) {
            val map = notificationUseCase.getQueryParams(notificationID, notificationEntryType, paymentID)
            val notifResponse = notificationUseCase.getResponse(map)
//            val notifResponse = notificationUseCase.getFakeResponse(map)
            //todo Rahul verify key later
            val reason = notifResponse.response?.resultStatus?.code
            if (reason == GratifResultStatus.SUCCESS) {
                //todo Rahul refactor later
                val code = notifResponse.response.promoCode
//                val code = "NUPLBDAY5D7RUU5M329"
                if (!code.isNullOrEmpty()) {
                    val couponDetail = tpCouponDetailUseCase.getResponse(tpCouponDetailUseCase.getQueryParams(code))
//                    val couponDetail = tpCouponDetailUseCase.getFakeResponse(tpCouponDetailUseCase.getQueryParams(code))
                    withContext(uiWorker) {
                        weakActivity.get()?.let {
                            val dialog = CmGratificationDialog().show(it, notifResponse.response, couponDetail, notificationEntryType)
                            dialog?.setOnShowListener {dialogInterface->
                                gratifPopupCallback?.onShow(dialogInterface)
                            }
                            dialog?.setOnDismissListener {dialogInterface->
                                gratifPopupCallback?.onDismiss(dialogInterface)
                            }
                        }
                    }
                } else {
                    gratifPopupCallback?.onIgnored(GratifPopupIngoreType.COUPON_CODE_EMPTY)
                }
            } else {
                gratifPopupCallback?.onIgnored(GratifPopupIngoreType.NO_SUCCESS)
            }
        }
    }


    fun showGratificationInApp(weakActivity: WeakReference<Activity>,
                               gratificationId: String?,
                               @NotificationEntryType notificationEntryType: Int,
                               gratifPopupCallback: GratifPopupCallback
    ): Job? {
        Log.d("NOOB", "showGratificationInApp")
        try {

            synchronized(this) {
                initSafeJob()
                initSafeScope()
            }
            if (!gratificationId.isNullOrEmpty()) {
                return getNotification(weakActivity, gratificationId.toInt(), notificationEntryType, gratifPopupCallback = gratifPopupCallback)
            }

        } catch (ex: Exception) {
            exceptionCallback?.onError(ex)
            ex.printStackTrace()
        }
        return null
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

    interface GratifPopupCallback {
        fun onShow(dialog: DialogInterface)
        fun onDismiss(dialog: DialogInterface)
        fun onIgnored(@GratifPopupIngoreType reason:Int)
    }

    interface ExceptionCallback {
        fun onError(th: Throwable?)
    }

}

@Retention(AnnotationRetention.SOURCE)
@IntDef(COUPON_CODE_EMPTY, NO_SUCCESS, UNKOWN_ERROR)
annotation class GratifPopupIngoreType {
    companion object {
        const val COUPON_CODE_EMPTY = 0
        const val NO_SUCCESS = 1
        const val UNKOWN_ERROR = 2
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(FRAGMENT_STARTED, FRAGMENT_SELECTED,FRAGMENT_RESUME,FRAGMENT_STOP,FRAGMENT_DESTROYED, ACTIVITY_STOP)
annotation class GratifCancellationExceptionType {
    companion object {
        const val FRAGMENT_STARTED = "fragment started"
        const val FRAGMENT_RESUME = "fragment resume"
        const val FRAGMENT_SELECTED = "fragment selected"
        const val FRAGMENT_STOP = "fragment stop"
        const val FRAGMENT_DESTROYED = "fragment destroyed"
        const val ACTIVITY_STOP = "activity stop"
    }
}

