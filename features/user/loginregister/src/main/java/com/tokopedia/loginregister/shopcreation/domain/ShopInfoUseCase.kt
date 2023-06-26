package com.tokopedia.loginregister.shopcreation.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.param.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.data.entity.ShopInfoPojo
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ShopInfoParam, ShopInfoPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return getQuery()
    }

    override suspend fun execute(params: ShopInfoParam): ShopInfoPojo {
        return graphqlRepository.request(graphqlQuery(), params.toMap())
    }

    private fun getQuery(): String = """
        query shopInfoByID ($shopID: Int!){
          shopInfoByID(input:{shopIDs:[$shopID], fields:["other-shiploc"]}) {
            result {
              shippingLoc {
                provinceID
              }
            }
          }
        }
    """.trimIndent()

    companion object {
        private const val shopID = "\$shopID"
    }
}
