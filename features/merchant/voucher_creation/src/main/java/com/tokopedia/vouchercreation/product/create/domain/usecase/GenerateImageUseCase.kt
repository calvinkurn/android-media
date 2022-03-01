package com.tokopedia.vouchercreation.product.create.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.data.response.GenerateImageResponse

class GenerateImageUseCase constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<String>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, GenerateImageResponse::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST).build())

        val response = gqlResponse.getData<GenerateImageResponse>(GenerateImageResponse::class.java)
        if (!TextUtils.isEmpty(response.imageGeneratorModel.imageUrl)) {
            return response.imageGeneratorModel.imageUrl
        } else {
            throw MessageErrorException("Error in image generation")
        }
    }

    companion object {

        private const val SOURCE_ID = "sourceID"
        private const val ARGS = "args"

        const val QUERY = """mutation imagenerator_generate_image(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
              }
            }
        """

        fun createParam(sourceId: String, args: ArrayList<GenerateImageParams>): HashMap<String, Any> {
            return hashMapOf(
                SOURCE_ID to sourceId,
                ARGS to args
            )
        }
    }
}

