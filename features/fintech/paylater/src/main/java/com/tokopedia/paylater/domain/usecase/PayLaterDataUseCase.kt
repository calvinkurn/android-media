package com.tokopedia.paylater.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.GQL_PAYLATER_ACTIVITY_DATA
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.domain.model.PayLaterActivityResponse
import com.tokopedia.paylater.domain.model.PayLaterProductData
import javax.inject.Inject
import javax.inject.Named

class PayLaterDataUseCase @Inject constructor(
        @Named(GQL_PAYLATER_ACTIVITY_DATA) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<PayLaterActivityResponse>(graphqlRepository) {

    fun getPayLaterData(onSuccess: (PayLaterProductData) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(PayLaterActivityResponse::class.java)
            this.setGraphqlQuery(query)
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