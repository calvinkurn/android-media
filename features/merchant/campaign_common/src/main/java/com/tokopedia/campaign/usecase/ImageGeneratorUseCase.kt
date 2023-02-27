package com.tokopedia.campaign.usecase

import com.tokopedia.campaign.data.request.ImageGeneratorRequest
import com.tokopedia.campaign.data.response.ImageGeneratorResponse
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class ImageGeneratorUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<String>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY_SOURCE_ID = "sourceID"
        private const val REQUEST_PARAM_KEY_ARGS = "args"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "ImageGeneratorGenerateImage"
        private val MUTATION = """mutation ImageGeneratorGenerateImage(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
              }
            }
        """

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): String {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ImageGeneratorResponse>()
        return data.image.imageUrl.orEmpty()
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val arguments = param.arguments.map { ImageGeneratorRequest(it.key, it.value) }

        val params = mapOf(
            REQUEST_PARAM_KEY_SOURCE_ID to param.sourceId,
            REQUEST_PARAM_KEY_ARGS to arguments
        )
        
        return GraphqlRequest(
            mutation,
            ImageGeneratorResponse::class.java,
            params
        )
    }

    /**
     * SourceId and argument params can be found here:
     * https://docs.google.com/spreadsheets/d/10Kee8re2G87hS5elK4XASlHYaav8nsvjjiU9L5qbGKQ/edit#gid=0
     */
    data class Param(val sourceId: String, val arguments: Map<String, String>)
}
