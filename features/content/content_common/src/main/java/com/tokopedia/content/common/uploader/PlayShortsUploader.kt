package com.tokopedia.content.common.uploader

import com.tokopedia.content.common.model.shorts.PlayShortsUploadModel

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
interface PlayShortsUploader {

    fun upload(uploadData: PlayShortsUploadModel)
}
