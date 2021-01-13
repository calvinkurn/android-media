package com.tokopedia.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.domain.model.PayLaterApplicationStatusResponse
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.domain.query.GQL_PAY_LATER_APPLICATION_STATUS
import javax.inject.Inject

@GqlQuery("PayLaterApplicationStatusQuery", GQL_PAY_LATER_APPLICATION_STATUS)
class PayLaterApplicationStatusUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<PayLaterApplicationStatusResponse>(graphqlRepository) {

    fun getPayLaterApplicationStatus(
            onSuccess: (UserCreditApplicationStatus) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(PayLaterApplicationStatusResponse::class.java)
            this.setGraphqlQuery(PayLaterApplicationStatusQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.userCreditApplicationStatus)

                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}