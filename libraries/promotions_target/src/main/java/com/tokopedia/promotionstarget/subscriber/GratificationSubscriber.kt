package com.tokopedia.promotionstarget.subscriber

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.promotionstarget.ClaimCouponApi
import com.tokopedia.promotionstarget.CouponGratificationParams
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.ui.TargetPromotionsDialog
import com.tokopedia.promotionstarget.usecase.ClaimPopGratificationUseCase
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class GratificationSubscriber(val appContext: Context) : BaseApplicationLifecycleCallbacks {

    @Inject
    lateinit var presenter: DialogManagerPresenter
    @Inject
    lateinit var claimGratificationUseCase: ClaimPopGratificationUseCase

    private val job = SupervisorJob()
    private val mapOfJobs = ConcurrentHashMap<Activity, Job>()
    private val mapOfDialogs = ConcurrentHashMap<Activity, Pair<TargetPromotionsDialog, Dialog>>()
    private val scope = CoroutineScope(job)
    private var weakOldClaimCouponApi: WeakReference<ClaimCouponApi>? = null


    private val ceh = CoroutineExceptionHandler { _, exception ->
    }

    init {
        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(appContext))
                .build()
        component.inject(this)
    }

    fun onNewIntent(activity: Activity, newIntent: Intent) {
        processOnActivityCreated(activity, newIntent)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        processOnActivityCreated(activity, activity?.intent)
    }

    private fun processOnActivityCreated(activity: Activity?, intent: Intent?) {
        if (activity != null && intent != null) {
            val gratificationData = shouldOpenTargetedPromotionsDialog(activity, intent)
            if (gratificationData != null) {
                cancelAll()
                showGratificationDialog(activity, gratificationData, intent)
            }
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        super.onActivityDestroyed(activity)
        if (activity != null) {
            clearMaps(activity)
        }
    }

    fun clearMaps(activity: Activity) {

        mapOfJobs[activity]?.cancel()
        mapOfJobs.remove(activity)
        mapOfDialogs[activity]?.first?.removeAutoApplyLiveDataObserver()
        mapOfDialogs.remove(activity)
    }

    override fun onActivityResumed(activity: Activity?) {
        super.onActivityResumed(activity)
        processOnActivityResumed(activity, activity?.intent)
    }

    private fun processOnActivityResumed(activity: Activity?, intent: Intent?) {
        if (activity != null && intent != null) {
            val waitingForLogin = intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)
            if (waitingForLogin) {
                val isLoggedIn = UserSession(activity).isLoggedIn
                mapOfDialogs[activity]?.first?.onActivityResumeIfWaitingForLogin(isLoggedIn)
            }
        }
    }

    private fun shouldOpenTargetedPromotionsDialog(activity: Activity?, intent: Intent?): GratificationData? {
        var showGratificationDialog = false
        var gratificationData: GratificationData? = null
        if (activity != null && intent != null) {

            val bundle = intent.extras?.getBundle(RouteManager.QUERY_PARAM)

            var popSlug = bundle?.getString(CouponGratificationParams.POP_SLUG)
            var page = bundle?.getString(CouponGratificationParams.PAGE)

            if (page.isNullOrEmpty()) {
                page = intent.getStringExtra(CouponGratificationParams.PAGE)
            }

            if (popSlug.isNullOrEmpty()) {
                popSlug = intent.getStringExtra(CouponGratificationParams.POP_SLUG)
            }

            if (popSlug.isNullOrEmpty()) {
                val uri = intent.data
                if (uri != null) {
                    popSlug = uri.getQueryParameter(CouponGratificationParams.POP_SLUG)
                    page = uri.getQueryParameter(CouponGratificationParams.PAGE)
                }
            }

            if (page.isNullOrEmpty()) {
                page = ""
                val activityName = activity?.javaClass?.canonicalName
                if (!TextUtils.isEmpty(activityName)) {
                    page = activityName
                }
            }

            showGratificationDialog = (!TextUtils.isEmpty(popSlug))
            if (showGratificationDialog) {
                gratificationData = GratificationData(popSlug!!, page!!)
            }
        }
        return gratificationData
    }

    private fun showGratificationDialog(activity: Activity, gratificationData: GratificationData, intent: Intent) {
        val weakActivity = WeakReference(activity)
        scope.launch(Dispatchers.IO + ceh) {
            supervisorScope {
                val childJob = launch {
                    val response = presenter.getGratificationAndShowDialog(gratificationData)
                    val canShowDialog = response.popGratification?.isShow
                    if (canShowDialog != null && canShowDialog) {
                        val couponDetail = presenter.composeApi(gratificationData)
                        withContext(Dispatchers.Main) {
                            if (weakActivity.get() != null && !weakActivity.get()?.isFinishing!!) {
                                show(weakActivity, response, couponDetail, gratificationData, intent)
                            }
                        }
                    }
                }
                mapOfJobs[activity] = childJob
            }
        }
    }

    private fun show(weakActivity: WeakReference<Activity>,
                     data: GetPopGratificationResponse,
                     couponDetailResponse: GetCouponDetailResponse,
                     gratificationData: GratificationData,
                     intent: Intent) {
        val dialog = TargetPromotionsDialog(this)
        if (weakActivity.get() != null) {
            val activity = weakActivity.get()!!
            val autoHitActionButton = intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)

            val claimApi: ClaimCouponApi
            if (autoHitActionButton && weakOldClaimCouponApi?.get() != null) {
                claimApi = weakOldClaimCouponApi?.get()!!
            } else {
                claimApi = ClaimCouponApi(scope, claimGratificationUseCase)
                weakOldClaimCouponApi?.clear()
                weakOldClaimCouponApi = WeakReference(claimApi)
            }
            val bottomSheetDialog = dialog.show(activity,
                    TargetPromotionsDialog.TargetPromotionsCouponType.SINGLE_COUPON,
                    data,
                    couponDetailResponse,
                    gratificationData,
                    claimApi,
                    autoHitActionButton)
            if (bottomSheetDialog != null) {
                mapOfDialogs[activity] = Pair(dialog, bottomSheetDialog)
            }
        }
    }

    private fun cancelAll() {
        mapOfJobs.forEach {
            it.value.cancel()
        }
        mapOfJobs.clear()

        mapOfDialogs.forEach {
            val dialogPair = it.value
            dialogPair.first.removeAutoApplyLiveDataObserver()
            dialogPair.second.dismiss()
        }
        mapOfDialogs.clear()
    }
}

data class GratificationData(val popSlug: String, val page: String)