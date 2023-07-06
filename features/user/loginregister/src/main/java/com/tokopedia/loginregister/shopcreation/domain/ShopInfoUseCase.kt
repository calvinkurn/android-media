package com.tokopedia.loginregister.shopcreation.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.ShopInfoPojo
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ShopInfoParam, ShopInfoPojo>(dispatcher.io) {

    override suspend fun execute(params: ShopInfoParam): ShopInfoPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
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

data class ShopInfoParam(
    @SerializedName("shopID")
    val shopID: Int = 0
) : GqlParam
