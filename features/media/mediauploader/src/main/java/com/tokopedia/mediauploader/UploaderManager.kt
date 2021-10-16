package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.addPrefix
import java.io.File

interface UploaderManager {

    suspend fun invoke(
        file: File,
        sourceId: String,
        progress: ProgressCallback?
    ): UploadResult

    suspend fun upload(
        file: File,
        sourceId: String,
        policy: SourcePolicy
    ): UploadResult

    suspend fun validate(
        file: File,
        sourceId: String,
        onUpload: suspend (sourcePolicy: SourcePolicy) -> UploadResult
    ): UploadResult

    /*
    * common error tracker and expose to user
    * */
    fun setError(message: List<String>, sourceId: String, file: File): UploadResult {
        val errorMessages = mutableListOf<String>()

        // this validation to preventing overload logging on scalyr if error message is empty
        if (message.isNotEmpty()) {
            errorMessages.addAll(message)

            // add the `Kode Error:` as prefix
            val messages = errorMessages.map {
                it.addPrefix()
            }.toList()

            trackToTimber(file, sourceId, messages)
        } else {
            // if error message "really" empty, adding a network error message as general message
            errorMessages.add(NETWORK_ERROR)
        }

        // get the first (as readable) error message and add the prefix
        val errorMessage = errorMessages.first().addPrefix()

        return UploadResult.Error(errorMessage)
    }
}