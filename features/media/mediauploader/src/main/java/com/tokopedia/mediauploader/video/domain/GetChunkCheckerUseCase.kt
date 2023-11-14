package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChunkCheckerUseCase @Inject constructor(
    private val repository: VideoUploadRepository
) : CoroutineUseCase<ChunkCheckerParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: ChunkCheckerParam): LargeUploader {
        return repository.checkChunk(params)
    }

    override fun graphqlQuery() = ""
}
