package com.tokopedia.mediauploader.image.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.entity.DataSourcePolicy
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetImageSecurePolicyUseCase @Inject constructor(
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
             uploadpedia_secure_policy(source: $$PARAM)  {
                header {
                  process_time
                  reason
                  error_code
                  messages
                }
                source_policy {
                  host
                  timeout
                  image_policy {
                    allowed_ext
                    max_file_size
                    max_res {
                      w
                      h
                    }
                    min_res {
                      w
                      h
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
