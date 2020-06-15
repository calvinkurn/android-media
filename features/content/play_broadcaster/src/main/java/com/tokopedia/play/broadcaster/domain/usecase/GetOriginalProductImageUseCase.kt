package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.broadcaster.domain.model.GetPDPInfo
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
       query getPDPInfo(${'$'}productId: Int!){
          getPDPInfo(productID: ${'$'}productId) {
            media{
              URLOriginal
            }
          }
        } 
    """
    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): List<String> {
        val gqlRequest = GraphqlRequest(query, GetPDPInfo.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetPDPInfo.Response>(GetPDPInfo.Response::class.java)
        response?.let {
            return it.getPdpInfo.mediaList.map { mediaList ->
                mediaList.urlOriginal
            }.toList()
        }
        return arrayListOf()
    }

    companion object {
        private const val PARAMS_PRODUCT_ID = "productId"

        fun createParams(productId: Long) =
                mapOf(PARAMS_PRODUCT_ID to productId)
    }
}