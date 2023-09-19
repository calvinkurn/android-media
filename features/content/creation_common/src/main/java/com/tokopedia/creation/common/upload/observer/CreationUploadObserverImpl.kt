package com.tokopedia.creation.common.upload.observer

import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 19, 2023
 */
class CreationUploadObserverImpl @Inject constructor(
    private val workManager: WorkManager,
) : CreationUploadObserver {

    private val uploadLiveData = Transformations.map(
        workManager
            .getWorkInfosForUniqueWorkLiveData(CreationUploadConst.CREATION_UPLOAD_WORKER)
    ) {
        it.firstOrNull()?.let { workInfo ->
            if(workInfo.state == WorkInfo.State.RUNNING) {
                val progress = workInfo.progress.getInt(CreationUploadConst.PROGRESS, 0)
                val uploadData = CreationUploadData.parse(workInfo.progress.getString(CreationUploadConst.UPLOAD_DATA).orEmpty())

                return@map when (progress) {
                    CreationUploadConst.PROGRESS_COMPLETED -> {
                        CreationUploadResult.Success(uploadData)
                    }
                    CreationUploadConst.PROGRESS_FAILED -> {
                        CreationUploadResult.Failed(uploadData)
                    }
                    else -> {
                        CreationUploadResult.Progress(uploadData, progress)
                    }
                }
            }
        }

        return@map CreationUploadResult.Unknown
    }

    override fun observe(): Flow<CreationUploadResult> {
        return callbackFlow {
            val observer = Observer<CreationUploadResult> {
                trySendBlocking(it)
            }

            uploadLiveData.observeForever(observer)

            awaitClose {
                uploadLiveData.removeObserver(observer)
            }
        }
    }
}
