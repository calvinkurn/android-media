package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.entity.SimpleUploader
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSimpleUploaderUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<SimpleUploadParam, SimpleUploader>(Dispatchers.IO) {

    var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: SimpleUploadParam): SimpleUploader {
        return repository.simpleUpload(params, progressUploader)
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}
