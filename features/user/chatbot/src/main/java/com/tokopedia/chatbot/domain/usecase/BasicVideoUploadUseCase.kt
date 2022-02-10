package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BasicVideoUploadUseCase @Inject constructor(private val uploader: UploaderUseCase) {


    suspend fun uploadBasicVideo(file: File, sourceId: String) {
        val param = uploader.createParams(
            filePath = file, // required
            sourceId = sourceId, // required
            withTranscode = false // optional, default: true
        )

        when (val result = uploader(param)) {
            is UploadResult.Success -> result.uploadId
            is UploadResult.Error -> result.message
        }

    }


}