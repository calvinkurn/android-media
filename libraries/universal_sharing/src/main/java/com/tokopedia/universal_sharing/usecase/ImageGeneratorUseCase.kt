package com.tokopedia.universal_sharing.usecase

import android.text.TextUtils
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.universal_sharing.model.ImageGeneratorModel
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-10.
 */
@GqlQuery(ImageGeneratorUseCase.QUERY_NAME, ImageGeneratorUseCase.QUERY)
class ImageGeneratorUseCase constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<String>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(ImageGeneratorUseCaseQuery.GQL_QUERY, ImageGeneratorModel.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST).build())

        val response = gqlResponse.getData<ImageGeneratorModel.Response>(ImageGeneratorModel.Response::class.java)
        if (!TextUtils.isEmpty(response.imageGeneratorModel.imageUrl)) {
            return response.imageGeneratorModel.imageUrl
        } else {
            throw MessageErrorException(response.followShop.message)
        }
    }

    companion object {

        private const val SOURCE_ID = "sourceID"
        private const val ARGS = "args"

        const val QUERY_NAME = "ImageGeneratorUseCaseQuery"
        const val QUERY = """
            mutation imagenerator_generate_image(${'$'}sourceID: String!, ${'$'}args: [ImageneratorGenerateImageArg]){
              imagenerator_generate_image(${'$'}sourceID: String!, ${'$'}args: [ImageneratorGenerateImageArg]){
                image_url
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
