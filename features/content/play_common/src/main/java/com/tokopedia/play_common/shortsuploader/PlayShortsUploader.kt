package com.tokopedia.play_common.shortsuploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadResult

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
interface PlayShortsUploader {

    fun upload(uploadData: PlayShortsUploadModel)

    fun getUploadLiveData(): LiveData<PlayShortsUploadResult>
}
