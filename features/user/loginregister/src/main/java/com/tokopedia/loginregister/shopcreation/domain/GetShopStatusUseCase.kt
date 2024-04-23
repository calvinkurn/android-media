package com.tokopedia.loginregister.shopcreation.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.GetShopInfoResponse
import com.tokopedia.loginregister.shopcreation.data.ShopStatus
import javax.inject.Inject

class GetShopStatusUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, ShopStatus>(dispatcher.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: String): ShopStatus {
        val param = mapOf(PARAM_SOURCE to params)
        val response: GetShopInfoResponse = graphqlRepository.request<Map<String, String>, GetShopInfoResponse>(query, param)
        with (response.shopInfo.reserveStatusInfo) {
            if (this.isShopPending()) {
                return ShopStatus.Pending
            } else {
                return ShopStatus.NotRegistered
            }
        }
    }

    companion object {
        private const val OPERATION_NAME = "userShopInfo"
        private const val PARAM_SOURCE = "source"

        private var query = object: GqlQueryInterface {
            override fun getOperationNameList(): List<String> {
                return listOf(OPERATION_NAME)
            }
            override fun getQuery(): String {
                return """
                query $OPERATION_NAME(${'$'}source: String!){
                    $OPERATION_NAME(source:${'$'}source) {
                        reserveStatusInfo {
                          shopID
                          shopName
                          domain
                          status
                          reasonID
                        }
                    }
                }
            """.trimIndent()
            }
            override fun getTopOperationName(): String {
                return OPERATION_NAME
            }
        }
    }
}
