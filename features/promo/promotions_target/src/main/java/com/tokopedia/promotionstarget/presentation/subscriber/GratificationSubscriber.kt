package com.tokopedia.promotionstarget.presentation.subscriber

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.NonNull
import com.tokopedia.applink.RouteManager
import com.tokopedia.promotionstarget.data.CouponGratificationParams
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.domain.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.domain.usecase.ClaimCouponApi
import com.tokopedia.promotionstarget.domain.usecase.ClaimPopGratificationUseCase
import com.tokopedia.promotionstarget.presentation.GratifCmInitializer
import com.tokopedia.promotionstarget.presentation.ui.dialog.TargetPromotionsDialog
import com.tokopedia.user.session.UserSession
import dagger.Lazy
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/*
* It has 2 flows, newer and older
* for the newer, a tag is added as NEW_FLOW
* */
class GratificationSubscriber(val appContext: Context) : BaseApplicationLifecycleCallbacks {

    @Inject
    lateinit var presenter: Lazy<DialogManagerPresenter>

    @Inject
    lateinit var claimGratificationUseCase: Lazy<ClaimPopGratificationUseCase>

    private var job: Job? = null
    private val mapOfJobs = ConcurrentHashMap<Activity, Job>()
    private val mapOfDialogs = ConcurrentHashMap<Activity, Pair<TargetPromotionsDialog, Dialog>>()
    private var scope: CoroutineScope? = null
    private var weakOldClaimCouponApi: WeakReference<ClaimCouponApi>? = null

    companion object {
        private val EXCLUDED_ACTIVITY_CLASS_NAMES = mutableSetOf<String>()

        @JvmStatic
        fun addActivityNameToExclude(@NonNull activityName: String) {
            if (!TextUtils.isEmpty(activityName)) {
                EXCLUDED_ACTIVITY_CLASS_NAMES.add(activityName)
            }
        }
    }


    private val ceh = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(appContext as Application))
                .build()
        component.inject(this)
    }

    fun onNewIntent(activity: Activity, newIntent: Intent) {
        processOnActivityCreated(activity, newIntent)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        if (savedInstanceState != null) {
            val waitingForLogin = savedInstanceState.getBoolean(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN)
            val refIdList = savedInstanceState.getIntArray(TargetPromotionsDialog.PARAM_REFERENCE_ID)
            if (waitingForLogin) {
                activity.intent?.putExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, true)
                activity.intent?.putExtra(TargetPromotionsDialog.PARAM_REFERENCE_ID, refIdList)
            }
        }
        processOnActivityCreated(activity, activity.intent)
    }

    private fun processOnActivityCreated(activity: Activity?, intent: Intent?) {
        val canonicalClassName = activity?.javaClass?.canonicalName
        if (activity != null && intent != null && !TextUtils.isEmpty(canonicalClassName)) {
            val isActivityAllowed = !EXCLUDED_ACTIVITY_CLASS_NAMES.contains(canonicalClassName)
            if (isActivityAllowed) {
                val gratificationData = shouldOpenTargetedPromotionsDialog(activity, intent)
                if (gratificationData != null) {
                    cancelAll()
                    showGratificationDialog(activity, gratificationData, intent)
                }
            }
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        clearMaps(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        super.onActivitySaveInstanceState(activity, outState)
        if (activity != null) {
            val waitingForLogin = activity.intent?.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)
            val refIdList = activity.intent?.getIntArrayExtra(TargetPromotionsDialog.PARAM_REFERENCE_ID)
            if (waitingForLogin != null && waitingForLogin) {
                outState?.putBoolean(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, true)
                outState?.putIntArray(TargetPromotionsDialog.PARAM_REFERENCE_ID, refIdList)
            }
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
        try {
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
        } catch (ex: Exception) {
            Timber.e(ex)
            return null
        }
    }

    private fun showGratificationDialog(activity: Activity, gratificationData: GratificationData, intent: Intent) {
        val weakActivity = WeakReference(activity)
        initSafeJob()
        initSafeScope()

        //NEW FLOW - when activity is killed due to low memory
        val waitingForLogin = intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)
        val referenceIdArray = intent.getIntArrayExtra(TargetPromotionsDialog.PARAM_REFERENCE_ID)

        if (waitingForLogin && referenceIdArray != null) {
            //Flow when activity is killed due to low memory
            showNonLoggedInDestroyedActivity(weakActivity, gratificationData)
        } else {
            scope?.launch(Dispatchers.IO + ceh) {
                supervisorScope {
                    val childJob = launch {
                        val response = presenter.get().getGratificationAndShowDialog(gratificationData)
                        val canShowDialog = response.popGratification?.isShow
                        var isAutoClaim = response.popGratification?.isAutoClaim

                        if (canShowDialog != null && canShowDialog) {
                            if (isAutoClaim != null && isAutoClaim) {

                                //NEW FLOW

                                if (presenter.get().userSession.isLoggedIn) {
                                    var claimPopGratificationResponse: ClaimPopGratificationResponse? = null
                                    var couponDetail: GetCouponDetailResponse? = null
                                    try {
                                        val claimPayload = ClaimPayload(gratificationData.popSlug, gratificationData.page)
                                        claimPopGratificationResponse = presenter.get().claimGratification(claimPayload)
                                        val popBenefits = response.popGratification?.popGratificationBenefits
                                        couponDetail = presenter.get().composeApi(popBenefits)

                                    } catch (ex: Exception) {
                                    }

                                    withContext(Dispatchers.Main) {
                                        if (weakActivity.get() != null && !weakActivity.get()?.isFinishing!!) {
                                            showNewLoggedIn(weakActivity, response, claimPopGratificationResponse, couponDetail, gratificationData)
                                        }
                                    }

                                } else {
                                    val couponDetail = presenter.get().composeApi(response.popGratification.popGratificationBenefits)
                                    withContext(Dispatchers.Main) {
                                        showNonLoggedIn(weakActivity, response, couponDetail, gratificationData)
                                    }
                                }
                            } else {
                                //OLD FLOW
                                val couponDetail = presenter.get().composeApi(response.popGratification.popGratificationBenefits)
                                withContext(Dispatchers.Main) {
                                    if (weakActivity.get() != null && !weakActivity.get()?.isFinishing!!) {
                                        showOld(weakActivity, response, couponDetail, gratificationData, intent)
                                    }
                                }
                            }
                        }
                    }
                    mapOfJobs[activity] = childJob
                }
            }
        }
    }

    private fun showNewLoggedIn(weakActivity: WeakReference<Activity>,
                                popGratificationResponse: GetPopGratificationResponse,
                                claimPopGratificationResponse: ClaimPopGratificationResponse?,
                                couponDetailResponse: GetCouponDetailResponse?,
                                gratificationData: GratificationData) {

        val targetPromotionsDialog = TargetPromotionsDialog(this)
        weakActivity.get()?.let { activity ->

            val dialog = targetPromotionsDialog.showAutoClaimLoggedIn(activity,
                    TargetPromotionsDialog.TargetPromotionsCouponType.MULTIPLE_COUPON,
                    popGratificationResponse,
                    claimPopGratificationResponse,
                    couponDetailResponse,
                    gratificationData)
            if (dialog != null) {
                mapOfDialogs[activity] = Pair(targetPromotionsDialog, dialog)
            }
        }
    }

    private fun showNonLoggedIn(weakActivity: WeakReference<Activity>, data: GetPopGratificationResponse, couponDetailResponse: GetCouponDetailResponse, gratificationData: GratificationData) {
        val targetPromotionsDialog = TargetPromotionsDialog(this)
        weakActivity.get()?.let { activity ->
            val dialog = targetPromotionsDialog.showNonLoggedInUi(activity, data, couponDetailResponse, gratificationData)
            if (dialog != null) {
                mapOfDialogs[activity] = Pair(targetPromotionsDialog, dialog)
            }
        }
    }

    private fun showNonLoggedInDestroyedActivity(weakActivity: WeakReference<Activity>, gratificationData: GratificationData) {
        val targetPromotionsDialog = TargetPromotionsDialog(this)
        weakActivity.get()?.let { activity ->
            val dialog = targetPromotionsDialog.showNonLoggedInDestroyedActivity(activity, gratificationData)
            if (dialog != null) {
                mapOfDialogs[activity] = Pair(targetPromotionsDialog, dialog)
            }
        }
    }

    private fun showOld(weakActivity: WeakReference<Activity>,
                        data: GetPopGratificationResponse,
                        couponDetailResponse: GetCouponDetailResponse?,
                        gratificationData: GratificationData,
                        intent: Intent) {
        val dialog = TargetPromotionsDialog(this)
        weakActivity.get()?.let { activity ->
            val autoHitActionButton = intent.getBooleanExtra(TargetPromotionsDialog.PARAM_WAITING_FOR_LOGIN, false)

            var claimApi: ClaimCouponApi? = null
            if (autoHitActionButton && weakOldClaimCouponApi?.get() != null) {
                claimApi = weakOldClaimCouponApi?.get()!!
            } else {
                if (scope != null) {
                    claimApi = ClaimCouponApi(scope!!, Dispatchers.Main, Dispatchers.IO, claimGratificationUseCase.get())
                    weakOldClaimCouponApi?.clear()
                    weakOldClaimCouponApi = WeakReference(claimApi)
                }
            }
            if (claimApi != null) {
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
}

data class GratificationData(val popSlug: String, val page: String)