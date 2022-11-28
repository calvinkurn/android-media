package com.tokopedia.content.common.model.shorts

import androidx.work.Data
import androidx.work.workDataOf

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
data class PlayShortsUploadModel(
    val shortsId: String,
    val authorId: String,
    val authorType: String,
    val mediaUri: String,
    val coverUri: String,
) {
    fun format(): Data {
        return workDataOf(
            KEY_SHORTS_ID to shortsId,
            KEY_ACCOUNT_ID to authorId,
            KEY_ACCOUNT_TYPE to authorType,
            KEY_MEDIA_URI to mediaUri,
            KEY_COVER_URI to coverUri,
        )
    }

    companion object {
        fun parse(inputData: Data): PlayShortsUploadModel {
            return PlayShortsUploadModel(
                shortsId = inputData.getString(KEY_SHORTS_ID).orEmpty(),
                authorId = inputData.getString(KEY_ACCOUNT_ID).orEmpty(),
                authorType = inputData.getString(KEY_ACCOUNT_TYPE).orEmpty(),
                mediaUri = inputData.getString(KEY_MEDIA_URI).orEmpty(),
                coverUri = inputData.getString(KEY_COVER_URI).orEmpty(),
            )
        }

        private const val KEY_SHORTS_ID = "KEY_SHORTS_ID"
        private const val KEY_ACCOUNT_ID = "KEY_ACCOUNT_ID"
        private const val KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE"
        private const val KEY_MEDIA_URI = "KEY_MEDIA_URI"
        private const val KEY_COVER_URI = "KEY_COVER_URI"
    }
}
