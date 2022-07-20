package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.internal.MediaUploaderUrl
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SetAbortUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices,
    private val url: MediaUploaderUrl
) : CoroutineUseCase<String, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: String): LargeUploader {
        return services.abortLargeUpload(
            url = url.largeAbortUpload(),
            uploadId = params
        )
    }

    override fun graphqlQuery() = ""

}