package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InitVideoUploaderUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<InitParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: InitParam): LargeUploader {
        return repository.initUpload(params)
    }

    override fun graphqlQuery() = ""
}
