package com.tokopedia.play_common.shortsuploader

import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
interface PlayShortsUploader {

    fun upload(uploadData: PlayShortsUploadModel)

    fun observe(owner: LifecycleOwner, observer: (progress: Int, uploadData: PlayShortsUploadModel) -> Unit)
}
