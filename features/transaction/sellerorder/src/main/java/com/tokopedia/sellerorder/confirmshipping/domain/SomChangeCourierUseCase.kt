package com.tokopedia.sellerorder.confirmshipping.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomChangeCourierUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomChangeCourier.Data>) {

    init {
        useCase.setTypeClass(SomChangeCourier.Data::class.java)
    }

    suspend fun execute(query: String): SomChangeCourier.Data {
        useCase.setGraphqlQuery(query)
        return  useCase.executeOnBackground()
    }
}