package com.tokopedia.play_common.shortsuploader

import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
interface PlayShortsUploader {

    fun upload(uploadData: PlayShortsUploadModel)

    fun observe(
        observer: (progress: Int, uploadData: PlayShortsUploadModel) -> Unit
    ): Observer<List<WorkInfo>>

    fun cancelObserve(observer: Observer<List<WorkInfo>>)
}
