package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.MediaUploaderUrl
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.Transcoding
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetTranscodingStatusUseCase @Inject constructor(
    private val services: VideoUploadServices,
    private val url: MediaUploaderUrl
) : CoroutineUseCase<String, Transcoding>(Dispatchers.IO) {

    override suspend fun execute(sourceId: String): Transcoding {
        return services.checkTranscodingStatus(
            url = url.hasLargeTranscodeStatus(sourceId)
        )
    }

    override fun graphqlQuery() = ""

}
