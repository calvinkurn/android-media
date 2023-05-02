package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.data.model.PopupManagerResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetShopManagerPopupsGqlQuery", GetShopManagerPopupsUseCase.QUERY)
class GetShopManagerPopupsUseCase @Inject constructor(repository: GraphqlRepository)
    : GraphqlUseCase<PopupManagerResponse>(repository) {

    companion object {
        private const val SHOP_ID = "shopID"
        const val QUERY = """
            query GetShopManagerPopups(${'$'}shopID:Int!){
              getShopManagerPopups(shopID: ${'$'}shopID) {
                 data {
                   showPopUp
                 }
              }
            }
        """

        private fun createRequestParams(shopId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID, shopId)
            }
        }
    }

    init {
        setGraphqlQuery(GetShopManagerPopupsGqlQuery())
        setTypeClass(PopupManagerResponse::class.java)
    }

    suspend fun execute(shopId: Long): Boolean {
        val requestParams = createRequestParams(shopId)
        setRequestParams(requestParams.parameters)

        val data = executeOnBackground()
        return data.getShopManagerPopups.shopManagerPopupsData.isShowPopup
    }

}
