package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.InitParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InitVideoUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<InitParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: InitParam): LargeUploader {
        val (sourceId, fileName) = params

        return services.initLargeUpload(
            url = UrlBuilder.initUrl(),
            fileName = fileName,
            sourceId = sourceId
        )
    }

    override fun graphqlQuery() = ""

}
