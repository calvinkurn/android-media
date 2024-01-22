package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SetCompleteUploaderUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<String, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: String): LargeUploader {
        return repository.completeUpload(params)
    }

    override fun graphqlQuery() = ""

}
