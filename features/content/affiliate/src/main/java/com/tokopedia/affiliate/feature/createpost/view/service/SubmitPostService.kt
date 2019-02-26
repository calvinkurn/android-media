package com.tokopedia.affiliate.feature.createpost.view.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.cachemanager.PersistentCacheManager
import java.util.*

/**
 * @author by milhamj on 26/02/19.
 */
class SubmitPostService : IntentService(TAG) {

    private lateinit var notificationManager: NotificationManager

    companion object {
        private val TAG = SubmitPostService::class.java.simpleName
        private const val DRAFT_ID = "draft_id"

        fun createIntent(context: Context, draftId: String): Intent {
            return Intent(context, SubmitPostService::class.java).apply {
                putExtra(DRAFT_ID, draftId)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onHandleIntent(intent: Intent) {
        val id: String = intent.getStringExtra(DRAFT_ID)
        val cacheManager = PersistentCacheManager(baseContext, id)
        val viewModel: CreatePostViewModel = cacheManager.get(
                CreatePostViewModel.TAG,
                CreatePostViewModel::class.java,
                null
        ) ?: return
        val notificationId = Random().nextInt()
    }
}