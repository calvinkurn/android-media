package com.tokopedia.promotionstarget.subscriber

import android.app.Activity
import android.app.Dialog
import android.content.Context
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
        println("Caught $exception")
    }

    init {
        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(appContext))
                .build()
        component.inject(this)
    }


    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity != null) {
            val gratificationData = shouldOpenTargetedPromotionsDialog(activity)
            if (gratificationData != null) {
                cancelAll()
                showGratificationDialog(activity, gratificationData)
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
        mapOfDialogs.remove(activity)
    }

    override fun onActivityResumed(activity: Activity?) {
        super.onActivityResumed(activity)
        if (activity != null) {
            val waitingForLogin = activity.intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)
            if (waitingForLogin) {
                mapOfDialogs[activity]?.first?.onActivityResumeIfWaitingForLogin()
            }
        }
    }

    private fun shouldOpenTargetedPromotionsDialog(activity: Activity?): GratificationData? {
        var showGratificationDialog = false
        var gratificationData: GratificationData? = null
        if (activity != null) {
            val intent = activity.intent
            val bundle = intent?.extras?.getBundle(RouteManager.QUERY_PARAM)

            var popSlug = bundle?.getString(CouponGratificationParams.POP_SLUG)
            var page = bundle?.getString(CouponGratificationParams.PAGE)

            if (page.isNullOrEmpty()) {
                page = intent?.getStringExtra(CouponGratificationParams.PAGE)
            }

            if (popSlug.isNullOrEmpty()) {
                popSlug = intent?.getStringExtra(CouponGratificationParams.POP_SLUG)
            }

            if (page.isNullOrEmpty()) {
                page = ""
            }

            showGratificationDialog = (!TextUtils.isEmpty(popSlug))
            if (showGratificationDialog) {
                gratificationData = GratificationData(popSlug!!, page)
            }
        }
        return gratificationData
    }

    private fun showGratificationDialog(activity: Activity, gratificationData: GratificationData) {
        val weakActivity = WeakReference(activity)
        scope.launch(Dispatchers.IO + ceh) {
            supervisorScope {
                val childJob = launch {
                    val response = presenter.getGratificationAndShowDialog(gratificationData)
                    val couponDetail = presenter.composeApi(gratificationData)
                    withContext(Dispatchers.Main) {
                        if (weakActivity.get() != null && !weakActivity.get()?.isFinishing!!) {
                            show(weakActivity, response, couponDetail, gratificationData)
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
                     gratificationData: GratificationData) {
        val dialog = TargetPromotionsDialog(this)
        if (weakActivity.get() != null) {
            val activity = weakActivity.get()!!
            val autoHitActionButton = activity.intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)

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
            mapOfDialogs[activity] = Pair(dialog, bottomSheetDialog)
        }
    }

    private fun cancelAll() {
        mapOfJobs.forEach {
            it.value.cancel()
        }
        mapOfJobs.clear()

        mapOfDialogs.forEach {
            val dialogPair = it.value
            dialogPair.second.dismiss()
        }
        mapOfDialogs.clear()
    }
}

data class GratificationData(val popSlug: String, val page: String)