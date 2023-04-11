package com.tokopedia.createpost.common.domain.entity

import com.tokopedia.createpost.common.domain.entity.request.SubmitPostMedium

/**
 * Created By : Jonathan Darwin on October 13, 2022
 */
data class UploadMediaDataModel(
    val images: Media,
    val videos: Media,
) {
    companion object {
        val Empty: UploadMediaDataModel
            get() = UploadMediaDataModel(
                images = Media.Unknown,
                videos = Media.Unknown,
            )
    }

    sealed interface Media {
        object Unknown : Media
        data class Success(val mediumList : List<SubmitPostMedium>) : Media
        data class Fail(val throwable: Throwable) : Media
    }
}
