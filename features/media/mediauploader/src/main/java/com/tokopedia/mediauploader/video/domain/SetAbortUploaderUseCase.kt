package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.AbortParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SetAbortUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<AbortParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: AbortParam): LargeUploader {
        val (uploadId, accessToken) = params

        return services.abortLargeUpload(
            url = UrlBuilder.abortUrl(),
            uploadId = uploadId,
            accessToken = accessToken
        )
    }

    override fun graphqlQuery() = ""

}