package com.tokopedia.creation.common.upload.uploader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponentProvider
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.domain.usecase.post.DeleteMediaPostCacheUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadType
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 08, 2022
 */
class CreationUploadReceiver : BroadcastReceiver() {

    @Inject
    lateinit var creationUploader: CreationUploader

    @Inject
    lateinit var analytic: PlayShortsUploadAnalytic

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var uploadQueueRepository: CreationUploadQueueRepository

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var deleteMediaPostCacheUseCase: DeleteMediaPostCacheUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        inject(context)

        val scope = CoroutineScope(dispatchers.io)

        val uploadDataRaw = intent?.getStringExtra(EXTRA_UPLOAD_DATA).orEmpty()
        val uploadData = CreationUploadData.parseFromJson(uploadDataRaw, gson)

        val action = intent?.getIntExtra(EXTRA_ACTION, 0).orZero()

        when(action) {
            Action.Retry.value -> {
                if (uploadData.uploadType == CreationUploadType.Shorts)
                    analytic.clickRetryUpload(uploadData.authorId, uploadData.authorType, uploadData.creationId)

                scope.launch {
                    creationUploader.retry(uploadData.notificationIdAfterUpload)
                }
            }
            Action.RemoveQueue.value -> {
                scope.launch {
                    creationUploader.removeFailedContentFromQueue(uploadData)
                }
            }
        }
    }

    private fun inject(context: Context) {
        CreationUploaderComponentProvider
            .get(context)
            .inject(this)
    }

    enum class Action(val value: Int) {
        Retry(1),
        RemoveQueue(2)
    }

    companion object {

        private const val EXTRA_UPLOAD_DATA = "EXTRA_UPLOAD_DATA"
        private const val EXTRA_ACTION = "EXTRA_ACTION"

        fun getIntent(
            context: Context,
            jsonUploadData: String,
            action: Action,
        ): Intent {
            return Intent(context, CreationUploadReceiver::class.java).apply {
                putExtra(EXTRA_UPLOAD_DATA, jsonUploadData)
                putExtra(EXTRA_ACTION, action.value)
            }
        }
    }
}
