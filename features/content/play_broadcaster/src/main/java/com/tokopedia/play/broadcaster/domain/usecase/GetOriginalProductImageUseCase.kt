package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetProductV3Response
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by furqan on 12/06/2020
 */
class GetOriginalProductImageUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<List<String>>() {

    /**
     * we only get the field that we need to reduce and optimize payload
     */
    private val query = """
       query getPDPInfo(${'$'}$PARAMS_PRODUCT_ID: String!){
          getProductV3(productID: ${'$'}$PARAMS_PRODUCT_ID, 
            options: {
              picture: true
            }) {
            pictures {
              urlOriginal
            }
          }
        } 
    """
    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): List<String> {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetProductV3Response::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetProductV3Response>(GetProductV3Response::class.java)
        return response.getProductV3.pictures.map { mediaList ->
            mediaList.urlOriginal
        }.toList()
    }

    companion object {
        private const val PARAMS_PRODUCT_ID = "productId"

        fun createParams(productId: String) =
                mapOf(PARAMS_PRODUCT_ID to productId)
    }
}