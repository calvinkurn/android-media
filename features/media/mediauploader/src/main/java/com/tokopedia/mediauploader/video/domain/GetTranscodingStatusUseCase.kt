package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.entity.Transcoding
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetTranscodingStatusUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<String, Transcoding>(Dispatchers.IO) {

    override suspend fun execute(uploadId: String): Transcoding {
        return repository.shouldTranscodeSucceed(uploadId)
    }

    override fun graphqlQuery() = ""
}
