package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.FollowShop
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-10.
 */

class PostFollowPartnerUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, FollowShop.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val response = gqlResponse.getData<FollowShop.Response>(FollowShop.Response::class.java)
        if (response.followShop.success)  {
            return true
        } else {
            throw MessageErrorException(response.followShop.message)
        }
    }

    companion object {

        private const val SHOP_ID = "shopID"
        private const val ACTION = "action"
        private const val INPUT = "input"

        private val query = getQuery()

        private fun getQuery() : String {
            val input = "\$input"

            return """
            mutation followShop($input: ParamFollowShop!) {
              followShop(input:$input){
                success
                message
              }
            }
            """.trimIndent()
        }

        fun createParam(shopId: String, action: PartnerFollowAction): HashMap<String, Any> {
            return hashMapOf(
                    INPUT to hashMapOf(
                            SHOP_ID to shopId,
                            ACTION to action.value
                    )
            )
        }
    }
}
