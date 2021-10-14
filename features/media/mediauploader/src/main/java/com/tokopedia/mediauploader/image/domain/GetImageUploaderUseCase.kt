package com.tokopedia.mediauploader.image.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.image.data.params.ImageUploaderParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class GetImageUploaderUseCase @Inject constructor(
    private val services: ImageUploadServices
) : CoroutineUseCase<ImageUploaderParam, ImageUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: ImageUploaderParam): ImageUploader {
        if (params.hasNoParams()) throw RuntimeException("No param found")
        val (hostUrl, sourceId, _, timeOut) = params

        return services.uploadImage(
            urlToUpload = UrlBuilder.imageUploadUrl(hostUrl, sourceId),
            partBody = params.fileBody(progressCallback),
            timeOut = timeOut
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}