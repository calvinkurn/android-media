package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.ImageUploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.ImageUploaderParam
import com.tokopedia.mediauploader.data.state.ProgressCallback
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class GetImageUploaderUseCase @Inject constructor(
        private val services: ImageUploadServices
) : CoroutineUseCase<ImageUploaderParam, MediaUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: ImageUploaderParam): MediaUploader {
        if (params.hasNotParams()) throw RuntimeException("No param found")

        val multiPartBodyBuilder = params.imageBody(progressCallback)

        return services.uploadImage(
            urlToUpload = params.uploadUrl,
            timeOut = params.timeOut,
            partBody = multiPartBodyBuilder
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}