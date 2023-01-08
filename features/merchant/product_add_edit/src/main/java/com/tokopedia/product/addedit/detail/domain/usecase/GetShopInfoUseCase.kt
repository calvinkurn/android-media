package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.GetShopInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<GetShopInfoResponse>(repository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private val query = """
            query shopInfoByID (${'$'}shopID: Int!) {
                shopInfoByID(input:{shopIDs: [${'$'}shopID], fields:["shopstats","other-goldos"]}) {
                    result {
                        shopStats {
                            totalTxSuccess
                        }
                        goldOS {
                            shopTier
                        }
                    }
                }
            }
            """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetShopInfoResponse::class.java)
    }

    fun setParam(shopId: Int) {
        requestParams.putInt(PARAM_SHOP_ID, shopId)
        setRequestParams(requestParams.parameters)
    }
}
