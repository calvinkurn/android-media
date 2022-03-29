package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.tokomember_seller_dashboard.model.TmSellerInfoResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.SellerData
import javax.inject.Inject

class TokomemberAuthenticatedUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<SellerData>(graphqlRepository) {

    @GqlQuery("TmSellerInfo", IS_AUTHENTICATED)
    fun getSellerInfo(
        success: (SellerData) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        this.setTypeClass(SellerData::class.java)
        this.setGraphqlQuery(TmSellerInfo.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

}


const val IS_AUTHENTICATED = """
    query isAuthenticatedQuery {
  userShopInfo {
    info {
      shop_id
    }
  }
}
"""