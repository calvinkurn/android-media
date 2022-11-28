package com.tokopedia.content.common.uploader

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.tokopedia.content.common.const.PlayShortsUploadConst
import com.tokopedia.content.common.model.shorts.PlayShortsUploadModel
import com.tokopedia.content.common.uploader.worker.PlayShortsUploadWorker
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
