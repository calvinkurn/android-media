package com.tokopedia.creation.common.upload.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.di.uploader.DaggerCreationUploaderComponent
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 08, 2022
 */
class PlayShortsUploadReceiver : BroadcastReceiver() {

    @Inject
    lateinit var creationUploader: CreationUploader

    @Inject
    lateinit var analytic: PlayShortsUploadAnalytic

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    override fun onReceive(context: Context, intent: Intent?) {
        inject(context)

        val uploadDataRaw = intent?.getStringExtra(EXTRA_UPLOAD_DATA).orEmpty()
        val uploadData = CreationUploadQueue.parse(uploadDataRaw)

        NotificationManagerCompat.from(context).cancel(uploadData.notificationIdAfterUpload)

        val action = intent?.getIntExtra(EXTRA_ACTION, 0).orZero()

        when(action) {
            Action.Retry.value -> {
                analytic.clickRetryUpload(uploadData.authorId, uploadData.authorType, uploadData.creationId)
                CoroutineScope(dispatchers.io).launch {
                    creationUploader.upload(uploadData)
                }
            }
        }
    }

    private fun inject(context: Context) {
        DaggerCreationUploaderComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {

        private const val EXTRA_UPLOAD_DATA = "EXTRA_UPLOAD_DATA"
        private const val EXTRA_ACTION = "EXTRA_ACTION"

        fun getIntent(
            context: Context,
            uploadData: CreationUploadQueue,
            action: Action,
        ): Intent {
            return Intent(context, PlayShortsUploadReceiver::class.java).apply {
                putExtra(EXTRA_UPLOAD_DATA, uploadData.toString())
                putExtra(EXTRA_ACTION, action.value)
            }
        }

        enum class Action(val value: Int) {
            Retry(1)
        }
    }
}
