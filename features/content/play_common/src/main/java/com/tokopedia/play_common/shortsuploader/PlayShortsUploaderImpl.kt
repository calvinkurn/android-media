package com.tokopedia.play_common.shortsuploader

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.tokopedia.play_common.shortsuploader.const.PlayShortsUploadConst
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.worker.PlayShortsUploadWorker
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
class PlayShortsUploaderImpl @Inject constructor(
    private val workManager: WorkManager,
) : PlayShortsUploader {

    override fun upload(uploadData: PlayShortsUploadModel) {
        val uploadWorker = PlayShortsUploadWorker.build(uploadData)

        workManager.enqueueUniqueWork(
            PlayShortsUploadConst.PLAY_SHORTS_UPLOAD,
            ExistingWorkPolicy.KEEP,
            uploadWorker,
        )
    }
}
