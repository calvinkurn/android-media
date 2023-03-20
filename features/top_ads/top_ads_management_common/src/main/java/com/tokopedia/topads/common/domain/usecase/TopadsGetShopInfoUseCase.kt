package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.domain.model.TopadsShopInfoV2Model
import javax.inject.Inject


class TopadsGetShopInfoUseCase @Inject constructor() : GraphqlUseCase<TopadsShopInfoV2Model>() {
    fun getShopInfo(
        onSuccess : (TopadsShopInfoV2Model) -> Unit,
        onFailure: (Throwable) -> Unit,
        shopID: String
    ){
         setTypeClass(TopadsShopInfoV2Model::class.java)
         setGraphqlQuery(GQL_QUERY)
        setRequestParams(getRequestParams(shopID))
        execute(
            onSuccess,
            onFailure
        )
    }

    private fun getRequestParams(shopID:String) = mutableMapOf<String,Any>().apply {
        put(SHOP_ID_PARAM,shopID)
        put(SOURCE_PARAM,"android")
    }

    companion object{
        private const val GQL_QUERY = """
            query topadsGetShopInfoV2_1(${'$'}shopID: String!, ${'$'}source: String!){
              topadsGetShopInfoV2_1(shopID: ${'$'}shopID, source: ${'$'}source){
                data {
                  ads{
                    type
                    is_used
                  }
                }
                errors{ 
                    code 
                    detail 
                    title 
                }
              }
            }
        """

        private const val SOURCE_PARAM = "source"
        private const val SHOP_ID_PARAM = "shopID"
    }
}


