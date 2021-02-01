package com.tokopedia.paylater.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.paylater.domain.model.PayLaterActivityResponse
import com.tokopedia.paylater.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.common.constants.GQL_PAY_LATER_PRODUCT_DETAIL
import com.tokopedia.paylater.paylater.domain.usecase.PayLaterProductDetailQuery
import javax.inject.Inject

@GqlQuery("PayLaterProductDetailQuery", GQL_PAY_LATER_PRODUCT_DETAIL)
class PayLaterProductDetailUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<PayLaterActivityResponse>(graphqlRepository) {

    fun getPayLaterData(
            onSuccess: (PayLaterProductData?) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(PayLaterActivityResponse::class.java)
            this.setGraphqlQuery(PayLaterProductDetailQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.productData)

                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}