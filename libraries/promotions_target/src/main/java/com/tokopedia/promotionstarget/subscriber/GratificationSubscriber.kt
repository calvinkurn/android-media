package com.tokopedia.promotionstarget.subscriber

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.components.AppModule
import com.tokopedia.promotionstarget.di.components.DaggerPromoTargetComponent
import com.tokopedia.promotionstarget.presenter.DialogManagerPresenter
import com.tokopedia.promotionstarget.ui.TargetPromotionsDialog
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class GratificationSubscriber(val appContext: Context) : BaseApplicationLifecycleCallbacks {

    private val job = SupervisorJob()
    @Inject
    lateinit var presenter: DialogManagerPresenter
    private val map = ConcurrentHashMap<Activity, Job>()
    private val scope = CoroutineScope(job)

    val ceh = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    init {
        val component = DaggerPromoTargetComponent.builder()
                .appModule(AppModule(appContext))
                .build()
        component.inject(this)
    }


    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        cancelAll()
        tryShowingGratificationDialog(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        super.onActivityDestroyed(activity)
        map[activity]?.cancel()
        map.remove(activity)
    }

    fun tryShowingGratificationDialog(activity: Activity?) {
        var showGratificationDialog = false
        activity?.run {
            val intent = this.intent
//            val campaignSlug = intent?.extras?.getString(CouponGratificationParams.CAMPAIGN_SLUG)
//            val page = intent?.extras?.getString(CouponGratificationParams.PAGE)
            val campaignSlug = "CampaignSlug"
            val page = "Hot"

            showGratificationDialog = (!TextUtils.isEmpty(campaignSlug) && !TextUtils.isEmpty(page))

            if (showGratificationDialog) {
                val gratificationData = GratificationData(campaignSlug!!, page!!)

                scope.launch(Dispatchers.IO + ceh) {
                    supervisorScope {
                        val childJob = launch {
                            val response = presenter.getGratificationAndShowDialog(gratificationData)
                            presenter.composeApi(gratificationData)
                            withContext(Dispatchers.Main) {
                                show(activity, response)
                            }
                        }
                        map[activity] = childJob
                    }
                }
            }
        }
    }

    private fun show(activity: Activity, data: GetPopGratificationResponse) {
        val dialog = TargetPromotionsDialog()
        dialog.show(activity, TargetPromotionsDialog.TargetPromotionsCouponType.SINGLE_COUPON, data)
    }

    private fun cancelAll() {
        map.forEach {
            it.value.cancel()
        }
        map.clear()
    }
}

data class GratificationData(val campaignSlug: String, val page: String)