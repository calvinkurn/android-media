package com.tokopedia.promotionstarget.subscriber

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.promotionstarget.DialogManager
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

class GratificationSubscriber(val appContext: Context) : BaseApplicationLifecycleCallbacks, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    private val map = ConcurrentHashMap<Activity, DialogManager>()

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

                launch(handler) {
                    try {
                        val dialogManager = DialogManager(appContext)
                        dialogManager.getGratificationAndShowDialog(activity, gratificationData)
                        map[activity] = dialogManager
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        }
    }

    private fun cancelAll() {
        map.forEach {
            it.value.cancel()
        }
        map.clear()
    }
}

data class GratificationData(val campaignSlug: String, val page: String)