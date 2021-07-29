package com.tokopedia.saldodetails.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.domain.model.GQLSalesTransactionListResponse
import com.tokopedia.saldodetails.domain.query.GQL_PENJUALAN_TRANSACTION_LIST
import javax.inject.Inject


@GqlQuery("GqlPenjualanTransactionList", GQL_PENJUALAN_TRANSACTION_LIST)
class GetSalesTransactionListUseCase
@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GQLSalesTransactionListResponse>(graphqlRepository) {



}