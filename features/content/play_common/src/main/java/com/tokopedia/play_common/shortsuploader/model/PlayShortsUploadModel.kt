package com.tokopedia.play_common.shortsuploader.model

import androidx.work.Data
import androidx.work.workDataOf
import com.tokopedia.kotlin.extensions.view.toIntOrZero

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

    val notificationId: Int
        get() = shortsId.toIntOrZero()

    /**
     * Need to differentiate notification Id between in progress & success / error
     * since [notificationId] is already set for Foreground Work and will be dismissed
     * automatically when the worker is done.
     */
    val notificationIdAfterUpload: Int
        get() = notificationId + 1

    fun format(): Data {
        return workDataOf(
            KEY_SHORTS_ID to shortsId,
            KEY_ACCOUNT_ID to authorId,
            KEY_ACCOUNT_TYPE to authorType,
            KEY_MEDIA_URI to mediaUri,
            KEY_COVER_URI to coverUri,
        )
    }

    override fun toString(): String {
        return mapOf(
            KEY_SHORTS_ID to shortsId,
            KEY_ACCOUNT_ID to authorId,
            KEY_ACCOUNT_TYPE to authorType,
            KEY_MEDIA_URI to mediaUri,
            KEY_COVER_URI to coverUri,
        ).toString()
    }

    companion object {
        val Empty: PlayShortsUploadModel
            get() = PlayShortsUploadModel(
                shortsId = "",
                authorId = "",
                authorType = "",
                mediaUri = "",
                coverUri = "",
            )

        fun parse(inputData: Data): PlayShortsUploadModel {
            return PlayShortsUploadModel(
                shortsId = inputData.getString(KEY_SHORTS_ID).orEmpty(),
                authorId = inputData.getString(KEY_ACCOUNT_ID).orEmpty(),
                authorType = inputData.getString(KEY_ACCOUNT_TYPE).orEmpty(),
                mediaUri = inputData.getString(KEY_MEDIA_URI).orEmpty(),
                coverUri = inputData.getString(KEY_COVER_URI).orEmpty(),
            )
        }

        fun parse(rawData: String): PlayShortsUploadModel {
            return try {
                val map = if(rawData.isEmpty())
                    mapOf()
                else rawData
                    .replace(OPEN_BRACKET, "")
                    .replace(CLOSE_BRACKET, "")
                    .split(ELEMENT_SEPARATOR)
                    .associate {
                        val (key, value) = it.split(KEY_VALUE_SEPARATOR)
                        key to value
                    }

                PlayShortsUploadModel(
                    shortsId = map[KEY_SHORTS_ID].orEmpty(),
                    authorId = map[KEY_ACCOUNT_ID].orEmpty(),
                    authorType = map[KEY_ACCOUNT_TYPE].orEmpty(),
                    mediaUri = map[KEY_MEDIA_URI].orEmpty(),
                    coverUri = map[KEY_COVER_URI].orEmpty(),
                )
            }
            catch (e: Exception) {
                Empty
            }
        }

        private const val KEY_SHORTS_ID = "KEY_SHORTS_ID"
        private const val KEY_ACCOUNT_ID = "KEY_ACCOUNT_ID"
        private const val KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE"
        private const val KEY_MEDIA_URI = "KEY_MEDIA_URI"
        private const val KEY_COVER_URI = "KEY_COVER_URI"

        private const val OPEN_BRACKET = "{"
        private const val CLOSE_BRACKET = "}"
        private const val ELEMENT_SEPARATOR = ", "
        private const val KEY_VALUE_SEPARATOR = "="
    }
}

fun PlayShortsUploadModel?.orEmpty() = this ?: PlayShortsUploadModel.Empty
