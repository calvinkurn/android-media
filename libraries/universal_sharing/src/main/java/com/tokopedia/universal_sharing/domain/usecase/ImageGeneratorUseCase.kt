package com.tokopedia.universal_sharing.domain.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.universal_sharing.model.ImageGeneratorModel
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import javax.inject.Inject

class ImageGeneratorUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<ImageGeneratorModel>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): ImageGeneratorModel {
        val gqlRequest = GraphqlRequest(QUERY, ImageGeneratorModel.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST).build())

        val response = gqlResponse.getData<ImageGeneratorModel.Response>(ImageGeneratorModel.Response::class.java)
        if (response?.imageGeneratorModel != null && !TextUtils.isEmpty(response.imageGeneratorModel.imageUrl)) {
            return response.imageGeneratorModel
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
                source_id
              }
            }
        """

        fun createParam(sourceId: String, args: ArrayList<ImageGeneratorRequestData>): HashMap<String, Any> {
            return hashMapOf(
                SOURCE_ID to sourceId,
                ARGS to args
            )
        }
    }
}
