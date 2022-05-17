package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.SellerData
import com.tokopedia.tokomember_seller_dashboard.model.TmKuponCreateMVResponse
import javax.inject.Inject

class TmKuponCreateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmKuponCreateMVResponse>(graphqlRepository) {

    @GqlQuery("TmKuponCreate", KUPON_CREATE)
    fun createKupon(
        success: (TmKuponCreateMVResponse) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        this.setTypeClass(TmKuponCreateMVResponse::class.java)
        this.setGraphqlQuery(TmKuponCreate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

}

const val KUPON_CREATE = """
     mutation merchantPromotionCreateMV(${'$'}merchantVoucherData: MVCreateData!) {
  merchantPromotionCreateMV(merchantVoucherData: ${'$'}merchantVoucherData) {
  status
  message
  process_time
  data{
    redirect_url
    voucher_id
    status
  }
}
}
"""