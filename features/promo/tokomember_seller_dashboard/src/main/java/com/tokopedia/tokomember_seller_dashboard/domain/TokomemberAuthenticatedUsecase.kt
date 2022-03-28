package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.tokomember_seller_dashboard.model.TmSellerInfoResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class TokomemberAuthenticatedUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmSellerInfoResponse>(graphqlRepository) {

    @GqlQuery("TmSellerInfo", IS_AUTHENTICATED)
    fun getSellerInfo(
        success: (TmSellerInfoResponse) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        this.setTypeClass(TmSellerInfoResponse::class.java)
        this.setGraphqlQuery(TmSellerInfo.GQL_QUERY)
        execute({
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