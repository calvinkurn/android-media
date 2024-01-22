package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChunkUploaderUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<ChunkUploadParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: ChunkUploadParam): LargeUploader {
        return repository.chunkUpload(params)
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""
}
