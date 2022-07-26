package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.request.ImageGeneratorArgumentRequest
import com.tokopedia.shop.flashsale.data.response.GenerateImageResponse
import com.tokopedia.shop.flashsale.domain.usecase.GenerateImageUseCase.Companion.QUERY
import com.tokopedia.shop.flashsale.domain.usecase.GenerateImageUseCase.Companion.QUERY_NAME
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants.ImageGeneratorSourceId.FS_TOKO
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY)
class GenerateImageUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<GenerateImageResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY_SOURCE_ID = "sourceID"
        private const val REQUEST_PARAM_KEY_ARGS = "args"
        const val QUERY_NAME = "ImageGeneratorGenerateImage"
        const val QUERY = """mutation ImageGeneratorGenerateImage(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(arguments : Map<String, String>): String {
        val imageGeneratorArguments = arguments.map { ImageGeneratorArgumentRequest(it.key, it.value) }
        val request = buildRequest(imageGeneratorArguments)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GenerateImageResponse>()
        return data.imageneratorGenerateImage.imageUrl
    }

    private fun buildRequest(arguments: List<ImageGeneratorArgumentRequest>): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_KEY_SOURCE_ID to FS_TOKO,
            REQUEST_PARAM_KEY_ARGS to arguments
        )
        return GraphqlRequest(
            ImageGeneratorGenerateImage(),
            GenerateImageResponse::class.java,
            params
        )
    }
}