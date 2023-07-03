package com.tokopedia.mediauploader.image.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.MediaUploaderUrl
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.collections.HashMap

open class GetImageUploaderUseCase @Inject constructor(
    private val services: ImageUploadServices,
    private val url: MediaUploaderUrl
) : CoroutineUseCase<ImageUploadParam, ImageUploader>(Dispatchers.IO) {

    var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: ImageUploadParam): ImageUploader {
        if (params.hasNoParams()) throw RuntimeException("No param found")
        val (sourceId, timeOut) = params

        val uploadUrl = if (params.isSecure) params.hostUrl else url.imageUpload(sourceId)

        val extraBodyParam = HashMap<String, RequestBody>()
        for ((key, value) in params.extraBody){
            extraBodyParam[key] = RequestBody.create("text/plain".toMediaTypeOrNull(), value)
        }

        val fileBody = params.fileBody(progressUploader)

        val extraHeaderParam = HashMap<String, String>()
        for ((key, value) in params.extraHeader){
            extraHeaderParam[key] = value
        }

        return services.uploadImage(
            urlToUpload = uploadUrl,
            partBody = fileBody,
            partMap = extraBodyParam,
            headerMap = extraHeaderParam,
            timeOut = timeOut,
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}
