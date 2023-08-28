package com.tokopedia.play_common.shortsuploader

import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
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

    override fun observe(
        observer: (progress: Int, uploadData: PlayShortsUploadModel) -> Unit
    ): Observer<List<WorkInfo>> {
        val workObserver = Observer<List<WorkInfo>> {
            it.firstOrNull()?.let { workInfo ->
                if(workInfo.state == WorkInfo.State.RUNNING) {
                    val progress = workInfo.progress.getInt(PlayShortsUploadConst.PROGRESS, 0)
                    val uploadData = PlayShortsUploadModel.parse(workInfo.progress.getString(PlayShortsUploadConst.UPLOAD_DATA).orEmpty())

                    observer(progress, uploadData)
                }
            }
        }

        workManager
            .getWorkInfosForUniqueWorkLiveData(PlayShortsUploadConst.PLAY_SHORTS_UPLOAD)
            .observeForever(workObserver)

        return workObserver
    }

    override fun cancelObserve(observer: Observer<List<WorkInfo>>) {
        workManager
            .getWorkInfosForUniqueWorkLiveData(PlayShortsUploadConst.PLAY_SHORTS_UPLOAD)
            .removeObserver(observer)
    }
}
