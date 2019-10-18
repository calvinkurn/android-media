package com.tokopedia.promotionstarget.subscriber

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.promotionstarget.CouponGratificationParams
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.ui.TargetPromotionsDialog
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class GratificationSubscriber(val appContext: Context) : BaseApplicationLifecycleCallbacks {

    private val job = SupervisorJob()
    @Inject
    lateinit var presenter: DialogManagerPresenter
    private val mapOfJobs = ConcurrentHashMap<Activity, Job>()
    private val mapOfDialogs = ConcurrentHashMap<Activity, Pair<TargetPromotionsDialog, Dialog>>()
    private val scope = CoroutineScope(job)
    //    var waitingForLoginActivity: WeakReference<Activity>? = null
    val arrayActivityNames = arrayListOf<String>(
            "com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity",
            "com.tokopedia.loginregister.login.view.activity.LoginActivity",
            "com.tokopedia.session.register.view.activity.SmartLockActivity"
    )
    val allowedActivityNames = arrayListOf<String>("com.tokopedia.navigation.presentation.activity.MainParentActivity")

    companion object {
//        val waitingForLogin = AtomicBoolean(false)
    }


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
        //todo Rahul remove later
        if (activity != null) {

//            val isLoginActivity = arrayActivityNames.contains(activity.localClassName)
//            val isAllowedActivity = allowedActivityNames.contains(activity.localClassName)
//            if (!isAllowedActivity) {
            //Do nothingsubscriber.waitingForLoginActivity
//            } else {
            val gratificationData = shouldOpenTargetedPromotionsDialog(activity)
            if (gratificationData != null) {
                cancelAll()
                showGratificationDialog(activity, gratificationData)
            }
//            }
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        super.onActivityDestroyed(activity)
        mapOfJobs[activity]?.cancel()
        mapOfJobs.remove(activity)
//        if (waitingForLoginActivity?.get() == activity) {
//            waitingForLoginActivity?.clear()
//            waitingForLogin.set(false)
//        }
    }

    override fun onActivityResumed(activity: Activity?) {
        super.onActivityResumed(activity)
//        if (waitingForLogin.get()) {
//            mapOfDialogs[activity]?.first?.onActivityResumeIfWaitingForLogin()
//        }
    }

    private fun shouldOpenTargetedPromotionsDialog(activity: Activity?): GratificationData? {
        var showGratificationDialog = false
        var gratificationData: GratificationData? = null
        //todo Rahul remove test data
        if (activity != null) {
            val intent = activity.intent
            val campaignSlug = intent?.extras?.getString(CouponGratificationParams.CAMPAIGN_SLUG)
            val page = intent?.extras?.getString(CouponGratificationParams.PAGE)

//            val campaignSlug = "CampaignSlug"
//            val page = "Hot"

            showGratificationDialog = (!TextUtils.isEmpty(campaignSlug) && !TextUtils.isEmpty(page))
            if (showGratificationDialog) {
                gratificationData = GratificationData(campaignSlug!!, page!!)
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
                        if (weakActivity.get() != null && !weakActivity.get()?.isFinishing!!)
                            show(weakActivity, response, couponDetail, gratificationData)
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
            val bottomSheetDialog = dialog.show(activity, TargetPromotionsDialog.TargetPromotionsCouponType.SINGLE_COUPON, data, couponDetailResponse, gratificationData)
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

//        waitingForLogin.set(false)
//        waitingForLoginActivity = null
    }
}

data class GratificationData(val campaignSlug: String, val page: String)