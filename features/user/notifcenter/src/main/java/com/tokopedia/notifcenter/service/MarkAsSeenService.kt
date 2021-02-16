package com.tokopedia.notifcenter.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.analytics.MarkAsSeenAnalytic
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.domain.NotificationMarkAsSeenUseCase
import javax.inject.Inject

class MarkAsSeenService : JobIntentService() {

    @Inject
    lateinit var markAsSeenUseCase: NotificationMarkAsSeenUseCase

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        DaggerNotificationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .commonModule(CommonModule(this))
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        val role = intent.getIntExtra(KEY_ROLE, -1).also {
            if (it == -1) return
        }
        val notifIds = intent.getStringArrayListExtra(KEY_TRACK_IDs) ?: return
        markAsSeenUseCase.markAsSeen(role, notifIds)
    }

    companion object {
        private const val JOB_ID = 11420
        private const val KEY_ROLE = "key_role"
        private const val KEY_TRACK_IDs = "key_track_ids"
        fun startService(
                context: Context?,
                @RoleType
                role: Int?,
                markAsSeenAnalytic: MarkAsSeenAnalytic
        ) {
            // https://issuetracker.google.com/issues/112157099
            try {
                if (context == null || role == null || markAsSeenAnalytic.isEmpty()) return
                val intent = createJobIntent(context, role, markAsSeenAnalytic)
                enqueueWork(context, MarkAsSeenService::class.java, JOB_ID, intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun createJobIntent(
                context: Context,
                @RoleType
                previousRole: Int,
                markAsSeenAnalytic: MarkAsSeenAnalytic
        ): Intent {
            val intent = Intent(context, MarkAsSeenService::class.java)
            intent.putExtra(KEY_ROLE, previousRole)
            intent.putStringArrayListExtra(KEY_TRACK_IDs, markAsSeenAnalytic.dequeue())
            return intent
        }
    }

}