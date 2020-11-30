package com.tokopedia.promotionstarget.cm.lifecycle

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.tokopedia.promotionstarget.cm.broadcast.BroadcastHandler
import com.tokopedia.promotionstarget.cm.broadcast.BroadcastScreenNamesProvider
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType
import com.tokopedia.promotionstarget.presentation.subscriber.BaseApplicationLifecycleCallbacks
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap

class CmActivityLifecycleCallbacks(val context: Context,
                                   val broadcastHandler: BroadcastHandler? = null,
                                   val broadcastScreenNamesProvider: BroadcastScreenNamesProvider? = null,
                                   val mapOfGratifJobs: ConcurrentHashMap<Int, Job>
) : BaseApplicationLifecycleCallbacks, CmActivityLiefcycleContract {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)

        val className = activity.javaClass.name
        broadcastScreenNamesProvider?.screenNames()?.let {
            if (it.isNotEmpty() && it.contains(className)) {
                broadcastHandler?.processActivity(activity)
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        broadcastHandler?.onActivityStop(activity)
        cancelJob(activity.hashCode(), GratifCancellationExceptionType.ACTIVITY_STOP)
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        cancelJob(activity.hashCode(), GratifCancellationExceptionType.ACTIVITY_STOP)
    }

    override fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String?) {
        val job: Job? = mapOfGratifJobs[entityHashCode]
        if (job != null && job.isActive) {
            job.cancel(CancellationException(reason))
            mapOfGratifJobs.remove(entityHashCode)
        }
    }
}