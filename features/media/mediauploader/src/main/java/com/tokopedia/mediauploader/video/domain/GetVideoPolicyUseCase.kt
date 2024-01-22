package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.entity.DataSourcePolicy
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetVideoPolicyUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<String, SourcePolicy>(Dispatchers.IO) {

    override suspend fun execute(params: String): SourcePolicy {
        val param = mapOf(PARAM to params)
        val result: DataSourcePolicy = repository.request(graphqlQuery(), param)
        return result.sourcePolicy()
    }

    override fun graphqlQuery(): String {
        return """
            query dataPolicyQuery($$PARAM: String) {
              uploadpedia_policy(source: $$PARAM) {
                source_policy {
                  host
                  timeout
                  vod_policy {
                    max_file_size
                    allowed_ext
                    simple_upload_size_threshold_mb
                    big_upload_chunk_size_mb
                    big_upload_max_concurrent
                    timeout_transcode
                    retry_interval
                    video_compression {
                      enable_compression
                      compress_threshold_mb
                      max_bitrate_bps
                      max_resolution
                      max_fps
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }

    companion object {
        private const val PARAM = "queryParamSourceId"
    }
}
