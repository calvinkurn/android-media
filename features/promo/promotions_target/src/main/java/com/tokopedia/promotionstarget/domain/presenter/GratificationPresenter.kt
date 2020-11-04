package com.tokopedia.promotionstarget.domain.presenter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.tokopedia.notifications.inApp.CmDialogVisibilityContract
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.ACTIVITY_STOP
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_DESTROYED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_RESUME
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_SELECTED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_STARTED
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_STOP
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType.Companion.FRAGMENT_UNSELECTED
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.COUPON_CODE_EMPTY
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.DIALOG_ALREADY_ACTIVE
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.INVALID_COUPON
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.NO_SUCCESS
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.TIMEOUT
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType.Companion.UNKOWN_ERROR
import com.tokopedia.promotionstarget.domain.usecase.NotificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.TokopointsCouponDetailUseCase
import com.tokopedia.promotionstarget.presentation.GratificationAnalyticsHelper
import com.tokopedia.promotionstarget.presentation.ui.Locks
import com.tokopedia.promotionstarget.presentation.ui.dialog.CmGratificationDialog
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class GratificationPresenter @Inject constructor(val context: Context) {
    val TAG = "GratifTag"

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
    var dialogVisibilityContract: CmDialogVisibilityContract? = null

    init {
        DaggerCmGratificationPresenterComponent.builder()
                .appModule(AppModule(context))
                .build()
                .inject(this)
    }


    private fun getNotification(weakActivity: WeakReference<Activity>?,
                                notificationID: Int,
                                @NotificationEntryType notificationEntryType: Int,
                                paymentID: Long = 0L,
                                gratifPopupCallback: GratifPopupCallback? = null,
                                screenName: String,
                                timeout: Long
    ): Job? {
        return scope?.launch(worker + ceh) {
            Locks.notificationMutex.withLock {
                if (timeout > 0L) {
                    var responseReceived = false
                    withTimeout(timeout) {
                        processApis(weakActivity, notificationID, notificationEntryType, paymentID, gratifPopupCallback, screenName)
                        responseReceived = true
                    }
                    if (!responseReceived) {
                        gratifPopupCallback?.onIgnored(GratifPopupIngoreType.TIMEOUT)
                        Timber.d("$TAG GRATIF ENGINE - Manual Cancellation timeout reached $timeout ")
                    }
                } else {
                    processApis(weakActivity, notificationID, notificationEntryType, paymentID, gratifPopupCallback, screenName)
                }

            }
        }
    }

    private suspend fun processApis(weakActivity: WeakReference<Activity>?,
                                    notificationID: Int,
                                    @NotificationEntryType notificationEntryType: Int,
                                    paymentID: Long = 0,
                                    gratifPopupCallback: GratifPopupCallback? = null,
                                    screenName: String) {

        val map = notificationUseCase.getQueryParams(notificationID, notificationEntryType, paymentID)
        Timber.d("$TAG GRATIF ENGINE API START with=$map")
//        val notifResponse = notificationUseCase.getResponse(map)
        val notifResponse = notificationUseCase.getFakeResponse(map)
        Timber.d("$TAG GRATIF ENGINE API END with=$notifResponse")
        //todo Rahul verify key later
        val reason = notifResponse.response?.resultStatus?.code
        if (reason == GratifResultStatus.SUCCESS) {
            //todo Rahul refactor later
//            val code = notifResponse.response.promoCode
            val code = "NUPLBDAY5D7RUU5M329"
//                    val code = "UNDIANMITRA05D7RUC66K7ZJDG8JA"  //expired
//                    val code = "UNDIANMITRA205D7RUC66K7ZJ9QUR9"  //used
            if (!code.isNullOrEmpty()) {
//                val couponDetail = tpCouponDetailUseCase.getResponse(tpCouponDetailUseCase.getQueryParams(code))
                val couponDetail = tpCouponDetailUseCase.getFakeResponse(tpCouponDetailUseCase.getQueryParams(code))
                val couponStatus = couponDetail?.coupon?.realCode ?: ""
                if (couponStatus.isNotEmpty()) {
                    withContext(uiWorker) {
                        weakActivity?.get()?.let { activity ->
                            if (notificationEntryType == NotificationEntryType.PUSH) {
                                performShowDialog(activity, notifResponse.response, couponDetail, notificationEntryType, gratifPopupCallback, screenName)
                            } else if (dialogVisibilityContract?.isDialogVisible(activity) == true) {
                                Timber.d("$TAG Android Side ERROR pop-up is already visible for screen name = $screenName")
                                gratifPopupCallback?.onIgnored(GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE)
                            } else {
                                Timber.d("$TAG ALL GOOD show dialog, notifId=$notificationID")
                                performShowDialog(activity, notifResponse.response, couponDetail, notificationEntryType, gratifPopupCallback, screenName)
                            }
                        }
                    }
                } else {
                    Timber.d("$TAG COUPON ENGINE INVALID COUPON, notifId = $notificationID, couponCode=$code")
                    gratifPopupCallback?.onIgnored(GratifPopupIngoreType.INVALID_COUPON)
                }

            } else {
                Timber.d("$TAG GRATIF ENGINE COUPON CODE EMPTY, notifId = $notificationID")
                gratifPopupCallback?.onIgnored(GratifPopupIngoreType.COUPON_CODE_EMPTY)
            }
        } else {
            Timber.d("$TAG GRATIF ENGINE FAIL, notifId = $notificationID, resultStatus=$reason")
            gratifPopupCallback?.onIgnored(GratifPopupIngoreType.NO_SUCCESS)
        }
    }

    private fun performShowDialog(activity: Activity,
                                  gratifNotification: GratifNotification,
                                  couponDetail: TokopointsCouponDetailResponse,
                                  @NotificationEntryType notificationEntryType: Int,
                                  gratifPopupCallback: GratifPopupCallback? = null,
                                  screenName: String
    ) {

        val dialog = CmGratificationDialog().show(activity,
                gratifNotification,
                couponDetail,
                notificationEntryType,
                DialogInterface.OnShowListener { dialog ->
                    if (dialog != null) {
                        gratifPopupCallback?.onShow(dialog)
                    }
                }, screenName)
        dialogVisibilityContract?.onDialogShown(activity)

        dialog?.setOnDismissListener { dialogInterface ->
            dialogVisibilityContract?.onDialogDismiss(activity)
            gratifPopupCallback?.onDismiss(dialogInterface)
            val userId = UserSession(activity).userId
            GratificationAnalyticsHelper.handleDismiss(userId, notificationEntryType, gratifNotification, couponDetail, screenName)
        }
        dialog?.setOnCancelListener { dialogInterface ->
            dialogVisibilityContract?.onDialogDismiss(activity)
            gratifPopupCallback?.onDismiss(dialogInterface)
        }
    }


    fun showGratificationInApp(weakActivity: WeakReference<Activity>?,
                               gratificationId: String? = "",
                               @NotificationEntryType notificationEntryType: Int,
                               gratifPopupCallback: GratifPopupCallback,
                               screenName: String,
                               paymentID: Long = 0L,
                               timeout: Long = 0L
    ): Job? {
        try {

            synchronized(this) {
                initSafeJob()
                initSafeScope()
            }
            weakActivity?.get()?.let { activity ->
                if (dialogVisibilityContract?.isDialogVisible(activity) == true && (notificationEntryType == NotificationEntryType.ORGANIC)) {
                    Timber.d("$TAG Android Side ERROR pop-up is already visible for screen name = $screenName")
                    gratifPopupCallback.onIgnored(GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE)
                    return null
                }
                //special handle for push
                else if (notificationEntryType == NotificationEntryType.PUSH) {
                    dialogVisibilityContract?.onDialogShown(activity)
                }
            }
            val tempGratifId = if (gratificationId.isNullOrEmpty()) 0 else gratificationId.toInt()
            return getNotification(weakActivity,
                    tempGratifId,
                    notificationEntryType,
                    gratifPopupCallback = gratifPopupCallback,
                    screenName = screenName,
                    paymentID = paymentID,
                    timeout = timeout
            )

        } catch (ex: Exception) {
            Timber.d("$TAG unexpected error for gratifId=$gratificationId")
            exceptionCallback?.onError(ex)
            ex.printStackTrace()

            //special handle for push
            weakActivity?.get()?.let { activity ->
                if (notificationEntryType == NotificationEntryType.PUSH) {
                    dialogVisibilityContract?.onDialogDismiss(activity)
                }
            }

        }
        return null
    }

    fun onClear() {
        try {
            scope?.cancel()
        } catch (ex: Exception) {
            Timber.e(ex)
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

    interface GratifPopupCallback {
        fun onShow(dialog: DialogInterface)
        fun onDismiss(dialog: DialogInterface)
        fun onIgnored(@GratifPopupIngoreType reason: Int)
        fun onExeption(ex: Exception)
    }

    abstract class AbstractGratifPopupCallback : GratifPopupCallback {
        override fun onShow(dialog: DialogInterface) {

        }

        override fun onDismiss(dialog: DialogInterface) {

        }

        override fun onIgnored(reason: Int) {

        }

        override fun onExeption(ex: Exception) {

        }
    }

    interface ExceptionCallback {
        fun onError(th: Throwable?)
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(COUPON_CODE_EMPTY, NO_SUCCESS, UNKOWN_ERROR, INVALID_COUPON, DIALOG_ALREADY_ACTIVE, TIMEOUT)
annotation class GratifPopupIngoreType {
    companion object {
        const val COUPON_CODE_EMPTY = 0
        const val NO_SUCCESS = 1
        const val UNKOWN_ERROR = 2
        const val INVALID_COUPON = 3
        const val DIALOG_ALREADY_ACTIVE = 4
        const val TIMEOUT = 5
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(FRAGMENT_STARTED, FRAGMENT_SELECTED, FRAGMENT_RESUME, FRAGMENT_STOP, FRAGMENT_DESTROYED, ACTIVITY_STOP, FRAGMENT_UNSELECTED)
annotation class GratifCancellationExceptionType {
    companion object {
        const val FRAGMENT_STARTED = "fragment started"
        const val FRAGMENT_RESUME = "fragment resume"
        const val FRAGMENT_SELECTED = "fragment selected"
        const val FRAGMENT_UNSELECTED = "fragment unselected"
        const val FRAGMENT_STOP = "fragment stop"
        const val FRAGMENT_DESTROYED = "fragment destroyed"
        const val ACTIVITY_STOP = "activity stop"
    }
}

