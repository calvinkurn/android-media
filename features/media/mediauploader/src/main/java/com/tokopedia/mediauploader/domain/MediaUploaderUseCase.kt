package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.UploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.MediaUploaderParam
import com.tokopedia.mediauploader.data.state.ProgressCallback
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class MediaUploaderUseCase @Inject constructor(
        private val services: UploadServices
) : CoroutineUseCase<MediaUploaderParam, MediaUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: MediaUploaderParam): MediaUploader {
        if (params.hasEmptyParams()) throw RuntimeException("No param found")

        val file = params.imageUploaderParam(
            params.filePath,
            progressCallback
        )

        return services.uploadImage(
            urlToUpload = params.uploadUrl,
            timeOut = params.timeOut,
            fileUpload = file
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}