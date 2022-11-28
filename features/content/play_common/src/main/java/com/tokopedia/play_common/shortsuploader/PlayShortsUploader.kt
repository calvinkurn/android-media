package com.tokopedia.play_common.shortsuploader

import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
interface PlayShortsUploader {

    fun upload(uploadData: PlayShortsUploadModel)
}
