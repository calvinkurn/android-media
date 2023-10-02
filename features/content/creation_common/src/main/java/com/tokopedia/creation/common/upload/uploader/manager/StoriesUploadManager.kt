package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesAddMediaUseCase
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesUpdateStoryUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.uploader.notification.StoriesUploadNotificationManager
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.play_common.const.PlayUploadSourceIdConst
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.VideoSnapshotHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class StoriesUploadManager @Inject constructor(
    private val notificationManager: StoriesUploadNotificationManager,
    private val uploaderUseCase: UploaderUseCase,
    private val updateStoryUseCase: StoriesUpdateStoryUseCase,
    private val addMediaUseCase: StoriesAddMediaUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val snapshotHelper: VideoSnapshotHelper,
) : CreationUploadManager {

    private var currentProgress = 0

    private lateinit var uploadData: CreationUploadData.Stories

    private var mListener: CreationUploadManagerListener? = null

    private val step: Int by lazyThreadSafetyNone {
        if (uploadData.coverUri.isEmpty()) STEP_WITHOUT_COVER else STEP_WITH_COVER
    }

    private val progressPerStep by lazyThreadSafetyNone {
        PROGRESS_MAX / step
    }

    /**
     * Upload Flow
     * 1. Update story status to Transcoding (value = 6)
     * 2. Upload media to uploadpedia
     * 3. If user doesnt upload cover:
     *    3.a. Take first frame snapshot of selected media
     *    3.a. Upload snapshot to uploadpedia
     *    3.b. Update story status (updating cover)
     * 4. Add mediaId to backend
     * 5. Update story status to Active (value = 1)
     *
     * If error happens anywhere within the flow:
     * 1. Update story status to TranscodingFailed (value = 7)
     *
     * Note:
     * Result.success & Result.failure() is not used here because
     * WorkManager has caching mechanism for the last emitted state.
     * Handling Result.success & Result.failure will show the last state
     * in the upcoming observe.
     * So, to determine whether the result is success or not,
     * we are using [progress] variable:
     * 100 : success
     * -1 : failed
     * else : loading
     */

    override suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener
    ): Boolean {
        if (uploadData !is CreationUploadData.Stories) return false

        this.uploadData = uploadData
        this.mListener = listener

        return withContext(dispatchers.io) {
            try {
                broadcastInit(uploadData)

                /** TODO JOE: add logic here */

                broadcastComplete()

                true
            } catch (e: Exception) {
                /**
                 * update story status to TranscodingFailed may error
                 * if no network connection
                 */
                try {
                    snapshotHelper.deleteLocalFile()
                    /** TODO JOE: update story status to TranscodingFailed here */
                } catch (e: Exception) {
                }

                broadcastFail()

                false
            }
        }
    }

    private suspend fun updateProgress(progress: Int = progressPerStep) {
        currentProgress += progress
        broadcastProgress(currentProgress)
        notificationManager.onProgress(currentProgress)
    }

    private suspend fun broadcastInit(uploadData: CreationUploadData) {
        broadcastProgress(CreationUploadConst.PROGRESS_INIT)
        notificationManager.init(uploadData)
        mListener?.setupForegroundNotification(notificationManager.onStart())
    }

    private suspend fun broadcastComplete() {
        broadcastProgress(CreationUploadConst.PROGRESS_COMPLETED)
        notificationManager.onSuccess()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastFail() {
        broadcastProgress(CreationUploadConst.PROGRESS_FAILED)
        notificationManager.onError()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastProgress(progress: Int) {
        mListener?.setProgress(uploadData, progress)
    }

    private fun getSourceId(uploadType: UploadType): String {
        return when (uploadType) {
            UploadType.Image -> PlayUploadSourceIdConst.uploadImageSourceId
            UploadType.Video -> uploadData.sourceId
        }
    }

    enum class UploadType(
        val type: String,
        val withTranscode: Boolean
    ) {
        Image(UPLOAD_TYPE_IMAGE, false),
        Video(UPLOAD_TYPE_VIDEO, true)
    }

    companion object {
        private const val UPLOAD_TYPE_IMAGE = "UPLOAD_TYPE_IMAGE"
        private const val UPLOAD_TYPE_VIDEO = "UPLOAD_TYPE_VIDEO"

        private const val PROGRESS_MAX = 100
        private const val STEP_WITHOUT_COVER = 5
        private const val STEP_WITH_COVER = 4
        private const val UPLOAD_FINISH_DELAY = 1000L
    }
}
