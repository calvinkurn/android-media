package com.tokopedia.mediauploader.image.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.internal.MediaUploaderUrl
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class GetImageUploaderUseCase @Inject constructor(
    private val services: ImageUploadServices,
    private val url: MediaUploaderUrl
) : CoroutineUseCase<ImageUploadParam, ImageUploader>(Dispatchers.IO) {

    var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: ImageUploadParam): ImageUploader {
        if (params.hasNoParams()) throw RuntimeException("No param found")
        val (sourceId, timeOut) = params

        return services.uploadImage(
            urlToUpload = url.imageUpload(sourceId),
            partBody = params.fileBody(progressUploader),
            timeOut = timeOut
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}